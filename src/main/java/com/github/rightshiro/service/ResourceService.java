package com.github.rightshiro.service;

import java.util.List;

import com.github.rightshiro.model.entity.AuthResourceDO;
import com.github.rightshiro.model.vo.PageInfo;

/**
 * @author weiguangchao
 * @date 2020/11/27
 */
public interface ResourceService {

    List<AuthResourceDO> listAuthorityMenuByUid(String uid);

    List<AuthResourceDO> listAllMenu();

    boolean isExistedRes(Long id);

    void deleteResById(Long apiId);

    void updateRes(AuthResourceDO api);

    void addRes(AuthResourceDO menu);

    List<AuthResourceDO> listApiTeam();

    List<AuthResourceDO> listApi(PageInfo<AuthResourceDO> pageInfo);

    List<AuthResourceDO> listApiByTeamId(Long teamId, PageInfo<AuthResourceDO> pageInfo);

    List<AuthResourceDO> listAuthorityApiByRoleId(Long roleId, PageInfo<AuthResourceDO> pageInfo);

    List<AuthResourceDO> listNotAuthorityApiByRoleId(Long roleId, PageInfo<AuthResourceDO> pageInfo);

    List<AuthResourceDO> listAuthorityMenuByRoleId(Long roleId, PageInfo<AuthResourceDO> pageInfo);

    List<AuthResourceDO> listNotAuthorityMenuByRoleId(Long roleId, PageInfo<AuthResourceDO> pageInfo);
}
