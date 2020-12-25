package com.github.rightshiro.service;

import java.util.List;

import com.github.rightshiro.model.entity.AuthRoleDO;
import com.github.rightshiro.model.vo.PageInfo;

/**
 * @author weiguangchao
 * @date 2020/12/2
 */
public interface RoleService {

    void grantRoleResource(Long roleId, Long resourceId);

    void deleteAuthorityRoleResource(Long roleId, Long resourceId);

    List<AuthRoleDO> listAllRole(PageInfo<AuthRoleDO> pageInfo);

    void addRole(AuthRoleDO role);

    void updateRole(AuthRoleDO role);

    void deleteRoleByRoleId(Long roleId);
}
