package com.github.rightshiro.shiro.filter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.github.rightshiro.model.constant.RedisKey;
import com.github.rightshiro.model.vo.Message;
import com.github.rightshiro.shiro.token.PasswordToken;
import com.github.rightshiro.util.AesUtils;
import com.github.rightshiro.util.IpUtils;
import com.github.rightshiro.util.WebUtils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 基于用户名密码的过滤器.
 * auth[xxx,xxx,xxx]
 *
 * @author weiguangchao
 * @date 2020/11/21
 */
public class PasswordFilter extends AccessControlFilter {

    private static final String DEFAULT_PATH_SEPARATOR = "/";
    private static final String URI_ACCOUNT = DEFAULT_PATH_SEPARATOR + "account";
    private static final String URI_LOGIN = URI_ACCOUNT + DEFAULT_PATH_SEPARATOR + "login";
    private static final String URI_REGISTER = URI_ACCOUNT + DEFAULT_PATH_SEPARATOR + "register";
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordFilter.class);

    private boolean encryptPassword;
    private Long tokenKeyTimeout;
    private StringRedisTemplate redisTemplate;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {

        Subject subject = getSubject(request, response);
        // 如果其已经登录，再此发送登录请求
        // 拒绝的话，统一交给onAccessDenied处理
        return Objects.nonNull(subject)
                && subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 判断若为获取登录注册加密动态秘钥请求
        if (isPasswordTokenGet(request)) {
            // 动态生成秘钥，redis存储秘钥供之后秘钥验证使用，设置有效期5秒用完即丢弃
            String tokenKey = RandomUtil.randomString(16);
            String userKey = RandomUtil.randomString(6).toUpperCase();
            try {
                redisTemplate.opsForValue().set(
                        RedisKey.TOKEN_KEY
                                + IpUtils.getIpFromRequest(WebUtils.toHttp(request))
                                .toUpperCase()
                                + userKey,
                        tokenKey,
                        tokenKeyTimeout,
                        TimeUnit.SECONDS
                );
                // 动态秘钥response返回给前端
                Message message = new Message();
                message.ok(1000, "issued tokenKey success")
                        .addData("tokenKey", tokenKey).addData("userKey", userKey);
                WebUtils.responseWriteJson(message, response);
            }
            catch (Exception ex) {
                LOGGER.warn("签发动态秘钥失败::" + ex.getMessage(), ex);
                Message message = new Message();
                message.ok(1000, "issued tokenKey fail");
                WebUtils.responseWriteJson(message, response);
            }
            return false;
        }

        // 判断是否是登录请求
        if (isPasswordLoginPost(request)) {
            AuthenticationToken token;

            try {
                token = createPasswordToken(request);
            }
            catch (Exception ex) {
                Message message = new Message().error(1111, "error request");
                WebUtils.responseWriteJson(message, response);

                LOGGER.warn("创建身份token失败::" + ex.getMessage(), ex);
                return false;
            }

            Subject subject = getSubject(request, response);
            try {
                subject.login(token);
                return true;
            }
            catch (AuthenticationException ae) {
                LOGGER.warn(token.getPrincipal() + "::" + ae.getMessage());
                // 告知客户端认证失败
                Message message = new Message().error(1002, "login fail");
                WebUtils.responseWriteJson(message, response);
                return false;
            }
            catch (Exception ex) {
                LOGGER.error(token.getPrincipal() + "::认证异常::" + ex.getMessage(), ex);
                Message message = new Message().error(1002, "login fail");
                WebUtils.responseWriteJson(message, response);
                return false;
            }
        }
        // 判断是否为注册请求,若是通过过滤链进入controller注册
        if (isAccountRegisterPost(request)) {
            return true;
        }
        // 之后添加对账户的找回等
        Message message = new Message().error(1111, "error request");
        WebUtils.responseWriteJson(message, response);
        return false;
    }

    private boolean isAccountRegisterPost(ServletRequest request) {
        Map<String, String> map = WebUtils.getRequestBodyMap(request);
        String requestUri = WebUtils.getRequestUri(request);
        String uid = map.get("uid");
        String username = map.get("username");
        String password = map.get("password");
        return WebUtils.isPostRequest(request)
                && StrUtil.isNotEmpty(uid)
                && StrUtil.isNotEmpty(username)
                && StrUtil.isNotEmpty(password)
                && StrUtil.equals(requestUri, URI_REGISTER);
    }

    private AuthenticationToken createPasswordToken(ServletRequest request) {
        Map<String, String> map = WebUtils.getRequestBodyMap(request);
        String appId = map.get("appId");
        String timestamp = map.get("timestamp");
        String password = map.get("password");
        String host = IpUtils.getIpFromRequest(WebUtils.toHttp(request));
        String userKey = map.get("userKey");
        if (encryptPassword) {
            String tokenKey = redisTemplate.opsForValue().get(RedisKey.TOKEN_KEY + host.toUpperCase() + userKey);
            password = AesUtils.decrypt(password, tokenKey);
        }
        return new PasswordToken(appId, password, timestamp, host);
    }

    private boolean isPasswordLoginPost(ServletRequest request) {
        String requestUri = WebUtils.getRequestUri(request);
        Map<String, String> map = WebUtils.getRequestBodyMap(request);
        String password = map.get("password");
        String timestamp = map.get("timestamp");
        String appId = map.get("appId");

        return WebUtils.isPostRequest(request)
                && Objects.nonNull(password)
                && Objects.nonNull(timestamp)
                && Objects.nonNull(appId)
                && StrUtil.equals(requestUri, URI_LOGIN);
    }

    private boolean isPasswordTokenGet(ServletRequest request) {
        HttpServletRequest req = WebUtils.toHttp(request);
        String requestUri = WebUtils.getRequestUri(request);
        String tokenKey = req.getParameter("tokenKey");
        return WebUtils.isGetRequest(req)
                && StrUtil.equals(requestUri, URI_ACCOUNT)
                && StrUtil.equals(tokenKey, "get");
    }

    public void setEncryptPassword(boolean encryptPassword) {
        this.encryptPassword = encryptPassword;
    }

    public void setTokenKeyTimeout(Long tokenKeyTimeout) {
        this.tokenKeyTimeout = tokenKeyTimeout;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
