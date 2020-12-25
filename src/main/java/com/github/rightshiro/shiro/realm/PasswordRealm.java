package com.github.rightshiro.shiro.realm;

import java.util.Objects;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.github.rightshiro.config.debug.UserConfig;
import com.github.rightshiro.model.vo.Account;
import com.github.rightshiro.shiro.provider.AccountProvider;
import com.github.rightshiro.shiro.token.PasswordToken;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public class PasswordRealm extends AuthorizingRealm {

    private AccountProvider accountProvider;
    private boolean debugModel = false;
    private UserConfig userConfig;

    @Override
    public Class<PasswordToken> getAuthenticationTokenClass() {
        return PasswordToken.class;
    }

    /**
     * 使用jwt进行授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (!(token instanceof PasswordToken)) {
            return null;
        }

        boolean existed = Objects.isNull(token.getPrincipal()) || Objects.isNull(token.getCredentials());
        if (existed) {
            throw new UnknownAccountException("account not exists");
        }

        String appId = (String) token.getPrincipal();
        Account account = accountProvider.loadAccount(appId);
        if (Objects.nonNull(account)) {
            PasswordToken passwordToken = (PasswordToken) token;
            passwordToken.setPassword(SecureUtil.md5(passwordToken.getPassword() + account.getSalt()));
            return new SimpleAuthenticationInfo(appId, account.getPassword(), getName());
        }

        // 账户不存在时进行debug模式判断
        boolean debugFlag = debugModel
                && Objects.nonNull(userConfig)
                && StrUtil.equals(userConfig.getUsername(), appId);
        if (debugFlag) {
            return new SimpleAuthenticationInfo(
                    userConfig.getUsername(),
                    userConfig.getPassword(),
                    getName()
            );
        }

        return new SimpleAuthenticationInfo(appId, "", getName());
    }

    public void setAccountProvider(AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }

    public void setDebugModel(boolean debugModel) {
        this.debugModel = debugModel;
    }

    public void setUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
    }
}
