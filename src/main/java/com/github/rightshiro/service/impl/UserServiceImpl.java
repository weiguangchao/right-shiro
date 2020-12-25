package com.github.rightshiro.service.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rightshiro.exception.BaseRunException;
import com.github.rightshiro.model.constant.SysRole;
import com.github.rightshiro.model.entity.AuthUserDO;
import com.github.rightshiro.model.entity.AuthUserRoleDO;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.repository.AuthRoleRepository;
import com.github.rightshiro.repository.AuthUserRepository;
import com.github.rightshiro.repository.AuthUserRoleRepository;
import com.github.rightshiro.service.UserService;
import com.github.rightshiro.util.AppUtils;
import com.github.rightshiro.util.JwtUtils;
import com.github.rightshiro.util.SystemUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;

/**
 * @author weiguangchao
 * @date 2020/11/26
 */
@Service
public class UserServiceImpl implements UserService {

    private AuthUserRepository authUserRepository;
    private AuthRoleRepository authRoleRepository;
    private AuthUserRoleRepository authUserRoleRepository;

    public UserServiceImpl(AuthUserRepository authUserRepository,
            AuthRoleRepository authRoleRepository,
            AuthUserRoleRepository authUserRoleRepository) {
        this.authUserRepository = authUserRepository;
        this.authRoleRepository = authRoleRepository;
        this.authUserRoleRepository = authUserRoleRepository;
    }

    @Override
    public List<String> listUserRoleByUid(String appId) {
        Assert.notNull(appId, "appId is empty");

        try {
            if (AppUtils.isDebugUser(appId)) {
                return Arrays.asList(authRoleRepository.listAllRoleCode());
            }
            return Arrays.asList(authUserRepository.listUserRoleByUid(appId));
        }
        catch (Exception ex) {
            new BaseRunException("获取用户角色失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public String getUserRoleByUid(String appId) {
        Assert.notNull(appId, "appId is empty");

        try {
            return JwtUtils.merge(this.listUserRoleByUid(appId).toArray(new String[0]));
        }
        catch (BaseRunException bre) {
            throw bre;
        }
        catch (Exception ex) {
            new BaseRunException("获取用户角色失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public void deleteUserRole(String uid, Long roleId) {
        Assert.notNull(uid, "uid is empty");
        Assert.notNull(roleId, "roleId is empty");

        try {
            AuthUserRoleDO authUserRoleDO = authUserRoleRepository.findByUserIdAndRoleId(uid, roleId);
            if (Objects.isNull(authUserRoleDO)) {
                new BaseRunException("用户角色不存在无法删除").process(false);
            }

            authUserRoleRepository.deleteById(authUserRoleDO.getId());
        }
        catch (Exception ex) {
            new BaseRunException("删除用户角色失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public List<AuthUserDO> listAllUser(PageInfo<AuthUserDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(page, "page is empty");
        Assert.notNull(size, "size is empty");

        try {
            Page<AuthUserDO> authUserDOPage = authUserRepository.findAll(PageRequest.of(page, size));

            return getAuthUserDOListFromPage(pageInfo, authUserDOPage);
        }
        catch (Exception ex) {
            new BaseRunException("获取用户列表失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    @Transactional
    public void register(AuthUserDO authUserDO) {
        Assert.notNull(authUserDO.getUid(), "uid is empty");
        Assert.notNull(authUserDO.getUsername(), "username is empty");
        Assert.notNull(authUserDO.getPassword(), "password is empty");
        Assert.notNull(authUserDO.getSalt(), "salt is empty");
        Assert.notNull(authUserDO.getStatus(), "status is empty");

        try {
            authUserRepository.save(authUserDO);
            this.grantUserRole(authUserDO.getUid(), SysRole.ID_GUEST);
        }
        catch (BaseRunException bre) {
            throw bre;
        }
        catch (Exception ex) {
            new BaseRunException("注册用户" + authUserDO.getUid() + "失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    @Transactional
    public void grantUserRole(String uid, Long roleId) {
        Assert.notNull(uid, "uid is empty");
        Assert.notNull(roleId, "roleId is empty");

        Timestamp currentTimestamp = SystemUtils.getCurrentTimestamp();
        AuthUserRoleDO authUserRoleDO = new AuthUserRoleDO();
        authUserRoleDO.setCreateTime(currentTimestamp);
        authUserRoleDO.setUpdateTime(currentTimestamp);
        authUserRoleDO.setUserId(uid);
        authUserRoleDO.setRoleId(roleId);
        try {
            authUserRoleRepository.save(authUserRoleDO);
        }
        catch (BaseRunException bre) {
            throw bre;
        }
        catch (Exception ex) {
            new BaseRunException("授予用户" + uid + "角色" + roleId + "失败::" + ex.getMessage(), ex).process();
        }
    }

    @Override
    public boolean isExistedUserByUid(String uid) {
        Assert.notNull(uid, "uid is empty");

        try {
            return Objects.nonNull(authUserRepository.getByUid(uid));
        }
        catch (Exception ex) {
            new BaseRunException("查询用户账户信息失败::" + ex.getMessage(), ex).process();
        }
        return false;
    }

    @Override
    public AuthUserDO getUserByUid(String appId) {
        Assert.notNull(appId, "appId is empty");

        try {
            if (AppUtils.isDebugUser(appId)) {
                AuthUserDO authUserDO = new AuthUserDO();
                authUserDO.setUid(appId);
                authUserDO.setUsername(appId);
                // 不需要设置密码
                return authUserDO;
            }
            return authUserRepository.getByUid(appId);
        }
        catch (Exception ex) {
            new BaseRunException("获取用户信息失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthUserDO> listUserByRoleId(Long roleId, PageInfo<AuthUserDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(roleId, "roleId is empty");
        Assert.notNull(page, "page is empty");
        Assert.notNull(size, "size is empty");

        try {
            Page<AuthUserDO> authUserDOPage = authUserRepository.listUserByRoleId(roleId, PageRequest.of(page, size));

            return getAuthUserDOListFromPage(pageInfo, authUserDOPage);
        }
        catch (Exception ex) {
            new BaseRunException("根据角色获取用户失败::" + ex.getMessage(), ex).process();
            return null;
        }
    }

    @Override
    public List<AuthUserDO> listNotAuthorityUserByRoleId(Long roleId, PageInfo<AuthUserDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(roleId, "roleId is empty");
        Assert.notNull(page, "page is empty");
        Assert.notNull(size, "size is empty");

        try {
            Page<AuthUserDO> authUserDOPage = authUserRepository.listNotAuthorityUserByRoleId(roleId,
                    PageRequest.of(page, size));

            return getAuthUserDOListFromPage(pageInfo, authUserDOPage);
        }
        catch (Exception ex) {

            return null;
        }
    }

    /**
     * 填充PageInfo信息，隐藏password、salt.
     *
     * @param pageInfo
     * @param authUserDOPage
     * @return
     */
    private List<AuthUserDO> getAuthUserDOListFromPage(PageInfo<AuthUserDO> pageInfo, Page<AuthUserDO> authUserDOPage) {
        pageInfo.setTotalElement(authUserDOPage.getTotalElements());
        pageInfo.setTotalPage(authUserDOPage.getTotalPages());

        List<AuthUserDO> authUserDOList = authUserDOPage.getContent();
        if (CollectionUtil.isNotEmpty(authUserDOList)) {
            authUserDOList.stream().peek(authUserDO -> {
                authUserDO.setPassword("");
                authUserDO.setSalt("");
            });
            pageInfo.setContent(authUserDOList);
            return authUserDOList;
        }

        return null;
    }
}
