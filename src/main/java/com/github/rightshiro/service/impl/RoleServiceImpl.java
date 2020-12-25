package com.github.rightshiro.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.rightshiro.exception.BaseRunException;
import com.github.rightshiro.model.entity.AuthRoleDO;
import com.github.rightshiro.model.entity.AuthRoleResourceDO;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.repository.AuthRoleRepository;
import com.github.rightshiro.repository.AuthRoleResourceRepository;
import com.github.rightshiro.service.RoleService;
import com.github.rightshiro.util.SystemUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;

/**
 * @author weiguangchao
 * @date 2020/12/2
 */
@Service
public class RoleServiceImpl implements RoleService {

    private AuthRoleRepository authRoleRepository;
    private AuthRoleResourceRepository authRoleResourceRepository;

    public RoleServiceImpl(AuthRoleRepository authRoleRepository,
            AuthRoleResourceRepository authRoleResourceRepository) {
        this.authRoleRepository = authRoleRepository;
        this.authRoleResourceRepository = authRoleResourceRepository;
    }

    @Override
    public void grantRoleResource(Long roleId, Long resourceId) {
        Assert.notNull(roleId, "roleId is empty");
        Assert.notNull(resourceId, "resourceId is empty");

        Timestamp currentTimestamp = SystemUtils.getCurrentTimestamp();
        AuthRoleResourceDO authRoleResourceDO = new AuthRoleResourceDO();
        authRoleResourceDO.setCreateTime(currentTimestamp);
        authRoleResourceDO.setUpdateTime(currentTimestamp);
        authRoleResourceDO.setRoleId(roleId);
        authRoleResourceDO.setResourceId(resourceId);
        try {
            authRoleResourceRepository.save(authRoleResourceDO);
        }
        catch (Exception ex) {
            new BaseRunException("授予角色资源失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public void deleteAuthorityRoleResource(Long roleId, Long resourceId) {
        Assert.notNull(roleId, "roleId is empty");
        Assert.notNull(resourceId, "resourceId is empty");

        try {
            authRoleResourceRepository.deleteByRoleIdAndResourceId(roleId, resourceId);
        }
        catch (Exception ex) {
            new BaseRunException("删除角色资源失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public List<AuthRoleDO> listAllRole(PageInfo<AuthRoleDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(page, "page is empty");
        Assert.notNull(size, "size is empty");

        try {
            Page<AuthRoleDO> authRoleDOPage = authRoleRepository.findAll(PageRequest.of(page, size));

            return getAuthRoleListFromPage(pageInfo, authRoleDOPage);
        }
        catch (Exception ex) {
            new BaseRunException("获取角色失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public void addRole(AuthRoleDO role) {
        Assert.notNull(role.getCode(), "code is empty");
        Assert.notNull(role.getName(), "name is empty");
        Assert.notNull(role.getStatus(), "status is empty");

        try {
            authRoleRepository.save(role);
        }
        catch (Exception ex) {
            new BaseRunException("添加角色失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public void updateRole(AuthRoleDO role) {
        Long roleId = role.getId();
        Assert.notNull(roleId, "roleId is empty");

        try {
            AuthRoleDO queryRole = authRoleRepository.getOne(roleId);
            if (Objects.isNull(queryRole)) {
                new BaseRunException("role is not existed").process(false);
            }
            BeanUtil.copyProperties(
                    role,
                    queryRole,
                    CopyOptions.create().ignoreNullValue()
            );
            authRoleRepository.save(queryRole);
        }
        catch (BaseRunException bre) {
            throw bre;
        }
        catch (Exception ex) {
            new BaseRunException("更新角色失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public void deleteRoleByRoleId(Long roleId) {
        Assert.notNull(roleId, "roleId is empty");

        try {
            AuthRoleDO queryRole = authRoleRepository.getOne(roleId);
            if (Objects.isNull(queryRole)) {
                new BaseRunException("role is not existed").process(false);
            }

            authRoleRepository.deleteById(roleId);
        }
        catch (BaseRunException bre) {
            throw bre;
        }
        catch (Exception ex) {
            new BaseRunException("删除角色失败::" + ex.getMessage(), ex).process();
        }
    }

    private List<AuthRoleDO> getAuthRoleListFromPage(PageInfo<AuthRoleDO> pageInfo, Page<AuthRoleDO> authRoleDOPage) {
        List<AuthRoleDO> authRoleDOList = authRoleDOPage.getContent();

        pageInfo.setTotalElement(authRoleDOPage.getTotalElements());
        pageInfo.setTotalPage(authRoleDOPage.getTotalPages());
        pageInfo.setContent(authRoleDOList);
        return authRoleDOList;
    }
}
