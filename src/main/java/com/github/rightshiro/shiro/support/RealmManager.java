package com.github.rightshiro.shiro.support;

import java.util.LinkedList;
import java.util.List;

import org.apache.shiro.realm.Realm;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.github.rightshiro.shiro.matcher.JwtMatcher;
import com.github.rightshiro.shiro.matcher.PasswordMatcher;
import com.github.rightshiro.shiro.provider.AccountProvider;
import com.github.rightshiro.shiro.realm.JwtRealm;
import com.github.rightshiro.shiro.realm.PasswordRealm;
import com.github.rightshiro.shiro.token.JwtToken;
import com.github.rightshiro.shiro.token.PasswordToken;
import com.github.rightshiro.util.AppUtils;

/**
 * @author weiguangchao
 * @date 2020/12/5
 */
@Component
@DependsOn("springContextHolder")
public class RealmManager {

    private AccountProvider accountProvider;

    public RealmManager(AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }

    public List<Realm> initRealm() {
        List<Realm> realmList = new LinkedList<>();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setAccountProvider(accountProvider);
        passwordRealm.setCredentialsMatcher(new PasswordMatcher());
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        if (AppUtils.isDebugModel()) {
            passwordRealm.setDebugModel(true);
            passwordRealm.setUserConfig(AppUtils.getDebugUser());
        }
        realmList.add(passwordRealm);

        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(new JwtMatcher());
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);

        return realmList;
    }
}
