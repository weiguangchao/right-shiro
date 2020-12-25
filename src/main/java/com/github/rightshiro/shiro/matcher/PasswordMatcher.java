package com.github.rightshiro.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import cn.hutool.core.util.StrUtil;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public class PasswordMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return StrUtil.equals(
                token.getPrincipal().toString(),
                info.getPrincipals().toString()
        ) && StrUtil.equals(
                token.getCredentials().toString(),
                info.getCredentials().toString()
        );
    }
}
