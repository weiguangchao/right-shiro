package com.github.rightshiro.shiro.provider.impl;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.github.rightshiro.model.vo.Account;
import com.github.rightshiro.repository.AuthUserRepository;
import com.github.rightshiro.model.entity.AuthUserDO;
import com.github.rightshiro.shiro.provider.AccountProvider;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
@Component
public class AccountProviderImpl implements AccountProvider {

    private AuthUserRepository authUserRepository;

    public AccountProviderImpl(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public Account loadAccount(String appId) {
        AuthUserDO authUserDO = authUserRepository.getByUid(appId);
        return Objects.isNull(authUserDO) ?
                null :
                new Account(
                        authUserDO.getUsername(),
                        authUserDO.getPassword(),
                        authUserDO.getSalt()
                );
    }
}
