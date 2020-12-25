package com.github.rightshiro.controller;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rightshiro.model.vo.Login;
import com.github.rightshiro.model.vo.Message;
import com.github.rightshiro.model.vo.Register;
import com.github.rightshiro.service.UserService;
import com.github.rightshiro.support.factory.LogTaskFactory;
import com.github.rightshiro.support.manager.LogExeManager;
import com.github.rightshiro.util.AesUtils;
import com.github.rightshiro.util.IpUtils;
import com.github.rightshiro.util.JwtUtils;
import com.github.rightshiro.util.SystemUtils;
import com.github.rightshiro.util.WebUtils;
import com.github.rightshiro.model.constant.RedisKey;
import com.github.rightshiro.model.entity.AuthUserDO;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.ApiOperation;

/**
 * @author weiguangchao
 * @date 2020/11/23
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private static final long REFRESH_PERIOD_TIME = 36000L;

    @Value("${right.password.encrypt.enable:true}")
    private boolean encryptPassword;

    private UserService userService;
    private LogTaskFactory logTaskFactory;
    private StringRedisTemplate redisTemplate;

    public AccountController(UserService userService,
            LogTaskFactory logTaskFactory, StringRedisTemplate redisTemplate) {
        this.userService = userService;
        this.logTaskFactory = logTaskFactory;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/exit")
    @ApiOperation(value = "用户登出", httpMethod = "POST")
    public Message accountExit() {
        SecurityUtils.getSubject().logout();

        HttpServletRequest request = WebUtils.getRequest();
        Map<String, String> requestHeaderMap = WebUtils.getRequestHeaders(request);

        String appId = requestHeaderMap.get("appId");
        if (StringUtils.isEmpty(appId)) {
            return new Message().error(1111, "用户未登录无法登出");
        }
        String jwt = redisTemplate.opsForValue().get("JWT-SESSION-" + appId);
        if (StringUtils.isEmpty(jwt)) {
            return new Message().error(1111, "用户未登录无法登出");
        }
        redisTemplate.opsForValue().getOperations().delete("JWT-SESSION-" + appId);
        LogExeManager.getInstance().executeLogTask(
                logTaskFactory.exitLog(appId, request.getRemoteAddr(), 1, "")
        );

        return new Message().ok(6666, "用户退出成功");
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "POST用户登录签发JWT")
    public Message accountLogin(@RequestBody Login login) {
        String appId = login.getAppId();
        // debug模式判断
        String roles = userService.getUserRoleByUid(appId);
        // 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
        String jwt = JwtUtils.issueJwt(
                IdUtil.randomUUID(),
                appId,
                "token-server",
                REFRESH_PERIOD_TIME >> 1,
                roles,
                null
        );
        // 将签发的JWT存储到Redis： {JWT-SESSION-{appID} , jwt}
        redisTemplate.opsForValue().set(RedisKey.JWT_SESSION + appId, jwt, REFRESH_PERIOD_TIME, TimeUnit.SECONDS);

        AuthUserDO authUserDO = userService.getUserByUid(appId);
        authUserDO.setPassword(null);
        authUserDO.setSalt(null);

        HttpServletRequest request = WebUtils.getRequest();

        LogExeManager.getInstance().executeLogTask(
                logTaskFactory.loginLog(appId, IpUtils.getIpFromRequest(request), 1, "登录成功"));

        return new Message().ok(1003, "issue jwt success").addData("jwt", jwt).addData("user", authUserDO);
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "POST用户注册")
    public Message register(@RequestBody Register register) {
        String uid = register.getUid();
        String password = register.getPassword();
        String userKey = register.getUserKey();

        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(password)) {
            // 必须信息缺一不可,返回注册账号信息缺失
            return new Message().error(1111, "账户信息缺失");
        }
        if (userService.isExistedUserByUid(uid)) {
            // 账户已存在
            return new Message().error(1111, "账户已存在");
        }

        HttpServletRequest request = WebUtils.getRequest();

        if (encryptPassword) {
            // 从Redis取出密码传输加密解密秘钥
            String tokenKey = redisTemplate.opsForValue().get(
                    RedisKey.TOKEN_KEY + IpUtils.getIpFromRequest(request).toUpperCase() + userKey
            );
            password = AesUtils.decrypt(password, tokenKey);
        }
        String salt = RandomUtil.randomString(6);

        AuthUserDO authUserDO = getAuthUserDOFromRequest(register);
        authUserDO.setUid(uid);
        // 存储到数据库的密码为 MD5(原密码+盐值)
        authUserDO.setPassword(SecureUtil.md5(password + salt));
        authUserDO.setSalt(salt);

        authUserDO.setStatus(1);
        Timestamp currentTimestamp = SystemUtils.getCurrentTimestamp();
        authUserDO.setCreateTime(currentTimestamp);
        authUserDO.setUpdateTime(currentTimestamp);

        try {
            userService.register(authUserDO);
            LogExeManager.getInstance().executeLogTask(logTaskFactory
                    .registerLog(uid, IpUtils.getIpFromRequest(request), 1, "注册成功"));
            return new Message().ok(2002, "注册成功");
        }
        catch (Exception ex) {
            LogExeManager.getInstance().executeLogTask(logTaskFactory
                    .registerLog(uid, IpUtils.getIpFromRequest(request), 0, "注册失败"));
            return new Message().ok(1111, "注册失败");
        }
    }

    private AuthUserDO getAuthUserDOFromRequest(Register register) {
        AuthUserDO authUserDO = new AuthUserDO();
        if (StrUtil.isNotEmpty(register.getUsername())) {
            authUserDO.setUsername(register.getUsername());
        }
        if (StrUtil.isNotEmpty(register.getRealName())) {
            authUserDO.setRealName(register.getRealName());
        }
        if (StrUtil.isNotEmpty(register.getAvatar())) {
            authUserDO.setAvatar(register.getAvatar());
        }
        if (StrUtil.isNotEmpty(register.getPhone())) {
            authUserDO.setPhone(register.getPhone());
        }
        if (StrUtil.isNotEmpty(register.getEmail())) {
            authUserDO.setEmail(register.getEmail());
        }
        if (StrUtil.isNotEmpty(register.getSex())) {
            authUserDO.setSex(Integer.valueOf(register.getSex()));
        }
        if (StrUtil.isNotEmpty(register.getCreateWhere())) {
            authUserDO.setCreateWhere(Integer.valueOf(register.getCreateWhere()));
        }

        return authUserDO;
    }
}
