package com.github.rightshiro.shiro.realm;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

/**
 * @author weiguangchao
 * @date 2020/11/26
 */
public class OnceModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        assertRealmsConfigured();

        List<Realm> realms = this.getRealms()
                .stream()
                .filter(realm -> realm.supports(authenticationToken))
                .collect(Collectors.toList());
        return realms.size() == 1 ?
                this.doSingleRealmAuthentication(realms.iterator().next(), authenticationToken) :
                this.doMultiRealmAuthentication(realms, authenticationToken);
    }
}
