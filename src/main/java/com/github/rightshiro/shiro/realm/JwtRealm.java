package com.github.rightshiro.shiro.realm;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.github.rightshiro.util.JwtUtils;
import com.github.rightshiro.shiro.token.JwtToken;

import cn.hutool.core.util.StrUtil;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public class JwtRealm extends AuthorizingRealm {

    @Override
    public Class<?> getAuthenticationTokenClass() {
        return JwtToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String payload = (String) principals.getPrimaryPrincipal();
        boolean parseFlag = StrUtil.isNotEmpty(payload)
                && StrUtil.startWith(payload, "jwt:")
                && payload.charAt(4) == '{'
                && payload.charAt(payload.length() - 1) == '}';

        if (parseFlag) {
            Map<String, Object> payloadMap = JwtUtils.readValue(payload.substring(4));
            Set<String> roleSet = JwtUtils.split((String) payloadMap.get("roles"));
            Set<String> permissionSet = JwtUtils.split((String) payloadMap.get("perms"));
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            if (CollectionUtils.isNotEmpty(roleSet)) {
                info.setRoles(roleSet);
            }
            if (CollectionUtils.isNotEmpty(permissionSet)) {
                info.setStringPermissions(permissionSet);
            }
            return info;
        }

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (!(token instanceof JwtToken)) {
            return null;
        }

        JwtToken jwtToken = (JwtToken) token;
        String jwt = (String) jwtToken.getCredentials();
        String payload;
        try {
            // 解析jwt，为进行签名校验
            payload = JwtUtils.parseJwtPayload(jwt);
        }
        catch (Exception ex) { // 令牌无效
            throw new AuthenticationException("errJwt");
        }
        if (Objects.isNull(payload)) { // 令牌无效
            throw new AuthenticationException("errJwt");
        }

        return new SimpleAuthenticationInfo("jwt:" + payload, jwt, this.getName());
    }
}
