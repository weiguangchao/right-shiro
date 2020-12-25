package com.github.rightshiro.shiro.provider;

import com.github.rightshiro.model.vo.Account;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public interface AccountProvider {

    Account loadAccount(String appId);
}
