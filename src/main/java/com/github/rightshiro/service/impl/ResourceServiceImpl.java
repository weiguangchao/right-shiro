package com.github.rightshiro.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.rightshiro.exception.BaseRunException;
import com.github.rightshiro.model.entity.AuthResourceDO;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.repository.AuthResourceRepository;
import com.github.rightshiro.service.ResourceService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;

/**
 * @author weiguangchao
 * @date 2020/11/27
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private AuthResourceRepository authResourceRepository;

    public ResourceServiceImpl(AuthResourceRepository authResourceRepository) {
        this.authResourceRepository = authResourceRepository;
    }

    @Override
    public List<AuthResourceDO> listAuthorityMenuByUid(String uid) {
        Assert.notNull(uid, "uid is empty");

        try {
            return authResourceRepository.listAuthorityMenuByUid(uid);
        }
        catch (Exception ex) {
            new BaseRunException("获取用户授权菜单失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthResourceDO> listAllMenu() {
        try {
            return authResourceRepository.getByType(1);
        }
        catch (Exception ex) {
            new BaseRunException("获取菜单失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public boolean isExistedRes(Long id) {
        Assert.notNull(id, "id is empty");

        try {
            Optional<AuthResourceDO> authResourceDOOptional = authResourceRepository.findById(id);
            return authResourceDOOptional.isPresent();
        }
        catch (Exception ex) {
            new BaseRunException("获取资源失败::" + ex.getMessage(), ex).process();
            return false;
        }
    }

    @Override
    public void deleteResById(Long apiId) {
        Assert.notNull(apiId, "id is empty");

        try {
            boolean existence = this.isExistedRes(apiId);
            if (!existence) {
                new BaseRunException("资源不存在，无法删除").process(false);
            }

            authResourceRepository.deleteById(apiId);
        }
        catch (BaseRunException bre) {
            throw bre;
        }
        catch (Exception ex) {
            new BaseRunException("删除资源失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public void updateRes(AuthResourceDO api) {
        Long id = api.getId();
        Assert.notNull(id, "id is empty");

        try {
            Optional<AuthResourceDO> authResourceDOOptional = authResourceRepository.findById(id);
            if (!authResourceDOOptional.isPresent()) {
                new BaseRunException("资源不存在，无法修改").process(false);
            }

            AuthResourceDO authResourceDO = authResourceDOOptional.get();
            BeanUtil.copyProperties(
                    api,
                    authResourceDO,
                    CopyOptions.create().ignoreNullValue()
            );

            authResourceRepository.save(authResourceDO);
        }
        catch (BaseRunException bre) {
            throw bre;
        }
        catch (Exception ex) {
            new BaseRunException("修改资源失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public void addRes(AuthResourceDO menu) {
        Assert.notNull(menu.getCode(), "code is empty");
        Assert.notNull(menu.getName(), "name is empty");
        Assert.notNull(menu.getType(), "type is empty");

        try {
            authResourceRepository.save(menu);
        }
        catch (Exception ex) {
            new BaseRunException("保存资源失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public List<AuthResourceDO> listApiTeam() {
        try {
            return authResourceRepository.getByType(3);
        }
        catch (Exception ex) {
            new BaseRunException("获取team api失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthResourceDO> listApi(PageInfo<AuthResourceDO> pageInfo) {
        Assert.notNull(pageInfo.getPage(), "page is empty");
        Assert.notNull(pageInfo.getSize(), "size is empty");

        try {
            Page<AuthResourceDO> page = authResourceRepository.getByType(
                    2,
                    PageRequest.of(pageInfo.getPage(), pageInfo.getSize())
            );
            return getAuthResourceDOListFromPage(pageInfo, page);
        }
        catch (Exception ex) {
            new BaseRunException("获取api失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthResourceDO> listApiByTeamId(Long teamId, PageInfo<AuthResourceDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(teamId, "teamId is empty");
        Assert.notNull(page, "page is empty");
        Assert.notNull(size, "size is empty");

        try {
            Page<AuthResourceDO> authResourceDOPage = authResourceRepository.listApiByTeamId(
                    teamId,
                    PageRequest.of(page, size)
            );
            return getAuthResourceDOListFromPage(pageInfo, authResourceDOPage);
        }
        catch (Exception ex) {
            new BaseRunException("通过teamId获取api失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthResourceDO> listAuthorityApiByRoleId(Long roleId, PageInfo<AuthResourceDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(roleId, "roleId is empty");
        Assert.notNull(pageInfo.getPage(), "page is empty");
        Assert.notNull(pageInfo.getSize(), "size is empty");

        try {
            Page<AuthResourceDO> authResourceDOPage = authResourceRepository.listAuthorityApiByRoleId(
                    roleId,
                    PageRequest.of(page, size)
            );
            return getAuthResourceDOListFromPage(pageInfo, authResourceDOPage);
        }
        catch (Exception ex) {
            new BaseRunException("通过roleId获取授权api失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthResourceDO> listNotAuthorityApiByRoleId(Long roleId, PageInfo<AuthResourceDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(roleId, "roleId is empty");
        Assert.notNull(pageInfo.getPage(), "page is empty");
        Assert.notNull(pageInfo.getSize(), "size is empty");

        try {
            Page<AuthResourceDO> authResourceDOPage = authResourceRepository.listNotAuthorityApiByRoleId(
                    roleId,
                    PageRequest.of(page, size)
            );

            return getAuthResourceDOListFromPage(pageInfo, authResourceDOPage);
        }
        catch (Exception ex) {
            new BaseRunException("通过roleId获取未授权api失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthResourceDO> listAuthorityMenuByRoleId(Long roleId, PageInfo<AuthResourceDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(roleId, "roleId is empty");
        Assert.notNull(pageInfo.getPage(), "page is empty");
        Assert.notNull(pageInfo.getSize(), "size is empty");

        try {
            Page<AuthResourceDO> authResourceDOPage = authResourceRepository.listAuthorityMenuByRoleId(
                    roleId,
                    PageRequest.of(page, size)
            );

            return getAuthResourceDOListFromPage(pageInfo, authResourceDOPage);
        }
        catch (Exception ex) {
            new BaseRunException("通过roleId获取授权菜单失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthResourceDO> listNotAuthorityMenuByRoleId(Long roleId, PageInfo<AuthResourceDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(roleId, "roleId is empty");
        Assert.notNull(pageInfo.getPage(), "page is empty");
        Assert.notNull(pageInfo.getSize(), "size is empty");

        try {
            Page<AuthResourceDO> authResourceDOPage = authResourceRepository.listNotAuthorityMenuByRoleId(
                    roleId,
                    PageRequest.of(page, size)
            );

            return getAuthResourceDOListFromPage(pageInfo, authResourceDOPage);
        }
        catch (Exception ex) {
            new BaseRunException("通过roleId获取未授权菜单失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    private List<AuthResourceDO> getAuthResourceDOListFromPage(PageInfo<AuthResourceDO> pageInfo,
            Page<AuthResourceDO> authResourceDOPage) {

        List<AuthResourceDO> authResourceDOList = authResourceDOPage.getContent();
        pageInfo.setTotalPage(authResourceDOPage.getTotalPages());
        pageInfo.setTotalElement(authResourceDOPage.getTotalElements());
        pageInfo.setContent(authResourceDOList);
        return authResourceDOList;
    }
}
