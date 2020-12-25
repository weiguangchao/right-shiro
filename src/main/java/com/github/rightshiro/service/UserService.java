package com.github.rightshiro.service;

import java.util.List;

import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.model.entity.AuthUserDO;

/**
 * @author weiguangchao
 * @date 2020/11/26
 */
public interface UserService {

    void deleteUserRole(String uid, Long roleId);

    List<AuthUserDO> listAllUser(PageInfo<AuthUserDO> pageInfo);

    void register(AuthUserDO authUserDO);

    void grantUserRole(String uid, Long roleId);

    List<String> listUserRoleByUid(String appId);

    String getUserRoleByUid(String appId);

    boolean isExistedUserByUid(String uid);

    AuthUserDO getUserByUid(String appId);

    List<AuthUserDO> listUserByRoleId(Long roleId, PageInfo<AuthUserDO> pageInfo);

    List<AuthUserDO> listNotAuthorityUserByRoleId(Long roleId, PageInfo<AuthUserDO> pageInfo);
}
