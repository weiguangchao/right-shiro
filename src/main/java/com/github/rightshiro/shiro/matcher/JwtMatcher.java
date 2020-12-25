package com.github.rightshiro.shiro.matcher;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import com.github.rightshiro.shiro.token.JwtToken;
import com.github.rightshiro.util.JwtUtils;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public class JwtMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String jwt = (String) info.getCredentials();
        JwtToken jwtToken = (JwtToken) token;
        String sub;
        try {
            // 验签之后判断用户与令牌是否对应
            Jws<Claims> claimsJws = JwtUtils.verifyJwt(jwt);
            sub = claimsJws.getBody().get("sub", String.class);
        }
        catch (ExpiredJwtException eje) {
            throw new AuthenticationException("expiredJwt"); // 令牌过期
        }
        catch (Exception ex) {
            throw new AuthenticationException("errJwt"); // 令牌错误
        }

        if (!StrUtil.equals(sub, jwtToken.getAppId())) {
            throw new AuthenticationException("errJwt"); // 令牌错误
        }

        return true;
    }
}
