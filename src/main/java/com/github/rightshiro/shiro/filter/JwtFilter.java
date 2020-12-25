package com.github.rightshiro.shiro.filter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.github.rightshiro.model.constant.RedisKey;
import com.github.rightshiro.model.vo.Message;
import com.github.rightshiro.repository.AuthRoleRepository;
import com.github.rightshiro.repository.AuthUserRepository;
import com.github.rightshiro.shiro.token.JwtToken;
import com.github.rightshiro.support.factory.LogTaskFactory;
import com.github.rightshiro.support.manager.LogExeManager;
import com.github.rightshiro.util.AppUtils;
import com.github.rightshiro.util.IpUtils;
import com.github.rightshiro.util.JwtUtils;
import com.github.rightshiro.util.WebUtils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

/**
 * jwt过滤器.
 * jwt[xxx,xxx,xxx]
 *
 * @author weiguangchao
 * @date 2020/11/19
 */
public class JwtFilter extends PathMatchingFilter {

    private static final long REFRESH_PERIOD_TIME = 36000L; // 10 hours
    private static final String EXPIRED_STR = "expiredJwt";
    private static final LogExeManager LOG_EXE_MANAGER = LogExeManager.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    private LogTaskFactory logTaskFactory;
    private StringRedisTemplate redisTemplate;
    private AuthRoleRepository authRoleRepository;
    private AuthUserRepository authUserRepository;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {

        return this.isAccessAllowed(request, response, mappedValue)
                || this.onAccessDenied(request, response, mappedValue);
    }

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestUri = WebUtils.getRequestUri(request);

        // path: url==method eg: http://api/menu==GET
        String[] strArr = StrUtil.split(path, "==");
        String pathUri = WebUtils.splitUri(strArr[0]);

        if (ArrayUtil.length(strArr) < 1) { // 只有uri
            return this.pathsMatch(pathUri, requestUri);
        }
        else {
            String requestMethod = strArr[1].toUpperCase();
            String httpMethod = WebUtils.getRequestMethod(request);
            // 路径+方法都需要匹配
            return this.pathsMatch(pathUri, requestUri)
                    && StrUtil.equals(requestMethod, httpMethod);
        }
    }

    /**
     * 认证相关
     */
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        HttpServletRequest req = WebUtils.toHttp(request);

        LOG_EXE_MANAGER.executeLogTask(logTaskFactory.bussinssLog(
                req.getHeader("appId"),
                WebUtils.getRequestUri(req),
                WebUtils.getRequestMethod(req),
                1,
                null)
        );

        // 判断jwt认证请求
        boolean jwtAuthenticationRequest = Objects.nonNull(subject)
                && !subject.isAuthenticated()
                && isJwtRequest(request);
        if (jwtAuthenticationRequest) {
            AuthenticationToken token = createJwtToken(request); // 创建token
            try {
                subject.login(token);
                return this.checkRoles(subject, mappedValue);
            }
            catch (AuthenticationException ae) {
                // jwt过期
                if (StrUtil.equals(EXPIRED_STR, ae.getMessage())) {
                    String appId = req.getHeader("appId");
                    String jwt = req.getHeader("authorization");
                    String refreshJwt = redisTemplate.opsForValue().get("JWT-SESSION-" + appId);
                    // refresh time未过期
                    if (StrUtil.equals(jwt, refreshJwt)) {
                        // 重新申请新的JWT
                        // 根据appId获取其所拥有的角色
                        String roles;
                        if (AppUtils.isDebugUser(appId)) {
                            roles = JwtUtils.merge(authRoleRepository.listAllRoleCode());
                        }
                        else {
                            roles = JwtUtils.merge(authUserRepository.listUserRoleByUid(appId));
                        }

                        String newJwt = JwtUtils.issueJwt(
                                IdUtil.randomUUID(),
                                appId,
                                "token-server",
                                REFRESH_PERIOD_TIME >> 1,
                                roles,
                                null
                        );
                        // 将新签发的jwt保存到redis{JWT-SESSION-{appID} , jwt}
                        redisTemplate.opsForValue().set(
                                RedisKey.JWT_SESSION + appId,
                                newJwt,
                                REFRESH_PERIOD_TIME,
                                TimeUnit.SECONDS
                        );
                        Message message = new Message()
                                .ok(1005, "new jwt")
                                .addData("jwt", newJwt);
                        WebUtils.responseWriteJson(message, response);
                    }
                    else {
                        // jwt时间失效过期,jwt refresh time失效 返回jwt过期客户端重新登录
                        Message message = new Message().error(1006, "expired jwt");
                        WebUtils.responseWriteJson(message, response);
                    }
                }
                else { // 其他的判断为JWT错误无效
                    Message message = new Message().error(1007, "error Jwt");
                    WebUtils.responseWriteJson(message, response);
                }
                return false;
            }
            catch (Exception ex) { // 其他错误
                LOGGER.error(IpUtils.getIpFromRequest(req) + "::JWT认证失败::" + ex.getMessage(), ex);
                // 告知客户端JWT错误1005,需重新登录申请jwt
                Message message = new Message().error(1007, "error jwt");
                WebUtils.responseWriteJson(message, response);
                return false;
            }
        }
        else {
            // 请求未携带jwt 判断为无效请求
            Message message = new Message().error(1111, "error request");
            WebUtils.responseWriteJson(message, response);
            return false;
        }
    }


    /**
     * 验证用户角色.
     */
    private boolean checkRoles(Subject subject, Object mappedValue) {
        String[] roleArr = (String[]) mappedValue;
        return ArrayUtil.isEmpty(roleArr)
                || Stream.of(roleArr).anyMatch(role -> subject.hasRole(role.trim()));
    }

    private AuthenticationToken createJwtToken(ServletRequest request) {
        // tomcat会将request header转小写
        Map<String, String> requestHeaderMap = WebUtils.getRequestHeaders(request);
        String ipHost = request.getRemoteAddr();
        String jwt = requestHeaderMap.get("authorization");
        String appId = requestHeaderMap.get("appId");
        if (Objects.isNull(appId)) {
            appId = requestHeaderMap.get("appid");
        }
        String deviceInfo = requestHeaderMap.get("deviceInfo");
        if (Objects.isNull(deviceInfo)) {
            deviceInfo = requestHeaderMap.get("deviceinfo");
        }
        return new JwtToken(ipHost, deviceInfo, jwt, appId);
    }

    /**
     * 授权相关.
     */
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        // 未认证的情况上面已经处理  这里处理未授权
        if (Objects.nonNull(subject) && subject.isAuthenticated()) {
            // 已经认证但未授权的情况
            // 告知客户端JWT没有权限访问此资源
            Message message = new Message().error(1008, "no permission");
            WebUtils.responseWriteJson(message, response);
        }
        // 过滤链终止
        return false;
    }

    private Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    private boolean isJwtRequest(ServletRequest request) {
        HttpServletRequest req = WebUtils.toHttp(request);
        String jwt = req.getHeader("authorization");
        String appId = req.getHeader("appId");
        return StrUtil.isNotEmpty(jwt) && StrUtil.isNotEmpty(appId);
    }

    public void setLogTaskFactory(LogTaskFactory logTaskFactory) {
        this.logTaskFactory = logTaskFactory;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setAuthRoleRepository(AuthRoleRepository authRoleRepository) {
        this.authRoleRepository = authRoleRepository;
    }

    public void setAuthUserRepository(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }
}
