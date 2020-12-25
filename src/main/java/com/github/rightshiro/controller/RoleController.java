package com.github.rightshiro.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rightshiro.model.entity.AuthResourceDO;
import com.github.rightshiro.model.entity.AuthRoleDO;
import com.github.rightshiro.model.entity.AuthUserDO;
import com.github.rightshiro.model.vo.AuthorityRoleRes;
import com.github.rightshiro.model.vo.Message;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.service.ResourceService;
import com.github.rightshiro.service.RoleService;
import com.github.rightshiro.service.UserService;
import com.github.rightshiro.shiro.support.ShiroFilterChainManager;

import io.swagger.annotations.ApiOperation;

/**
 * @author weiguangchao
 * @date 2020/12/4
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private UserService userService;
    private RoleService roleService;
    private ResourceService resourceService;
    private ShiroFilterChainManager shiroFilterChainManager;

    public RoleController(UserService userService, RoleService roleService,
            ResourceService resourceService,
            ShiroFilterChainManager shiroFilterChainManager) {
        this.userService = userService;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.shiroFilterChainManager = shiroFilterChainManager;
    }

    @DeleteMapping("/{roleId}")
    @ApiOperation(value = "根据角色ID删除角色", httpMethod = "DELETE")
    public Message deleteRoleByRoleId(@PathVariable Long roleId) {
        roleService.deleteRoleByRoleId(roleId);
        return new Message().ok(6666, "delete success");
    }

    @PutMapping("/")
    @ApiOperation(value = "更新角色", httpMethod = "PUT")
    public Message updateRole(@RequestBody AuthRoleDO role) {
        roleService.updateRole(role);
        return new Message().ok(6666, "update success");
    }

    @PostMapping("/")
    @ApiOperation(value = "添加角色", httpMethod = "POST")
    public Message addRole(@RequestBody AuthRoleDO role) {
        roleService.addRole(role);
        return new Message().ok(6666, "add role success");
    }

    @GetMapping("/{page}/{size}")
    @ApiOperation(value = "获取角色LIST", httpMethod = "GET")
    public Message getRoles(
            @PathVariable Integer page,
            @PathVariable Integer size) {

        PageInfo<AuthRoleDO> pageInfo = new PageInfo<>(page, size);
        roleService.listAllRole(pageInfo);
        return new Message().ok(6666, "return roles success").addData("data", pageInfo);
    }

    @DeleteMapping("/authority/resource/{roleId}/{resourceId}")
    @ApiOperation(value = "删除对应的角色的授权资源", httpMethod = "DELETE")
    public Message deleteAuthorityRoleResource(
            @PathVariable Long roleId,
            @PathVariable Long resourceId) {

        roleService.deleteAuthorityRoleResource(roleId, resourceId);
        shiroFilterChainManager.reloadFilterChain();
        return new Message().ok(6666, "authority success");
    }

    @PostMapping("/authority/resource")
    @ApiOperation(value = "授权资源给角色", httpMethod = "POST")
    public Message authorityRoleResource(@RequestBody AuthorityRoleRes authorityRoleRes) {
        roleService.grantRoleResource(
                authorityRoleRes.getRoleId(),
                authorityRoleRes.getResourceId()
        );
        shiroFilterChainManager.reloadFilterChain();
        return new Message().ok(6666, "authority success");
    }

    @GetMapping("/menu/-/{roleId}/{page}/{size}")
    @ApiOperation(value = "获取角色(roleId)未被授权的menu资源")
    public Message getMenusExtendByRoleId(
            @PathVariable Long roleId,
            @PathVariable Integer page,
            @PathVariable Integer size) {

        PageInfo<AuthResourceDO> pageInfo = new PageInfo<>(page, size);
        resourceService.listNotAuthorityMenuByRoleId(roleId, pageInfo);
        return new Message().ok(6666, "return api success").addData("data", pageInfo);
    }

    @GetMapping("/menu/{roleId}/{page}/{size}")
    @ApiOperation(value = "获取角色(roleId)所被授权的menu资源")
    public Message getMenusByRoleId(
            @PathVariable Long roleId,
            @PathVariable Integer page,
            @PathVariable Integer size) {

        PageInfo<AuthResourceDO> pageInfo = new PageInfo<>(page, size);
        resourceService.listAuthorityMenuByRoleId(roleId, pageInfo);
        return new Message().ok(6666, "return api success").addData("data", pageInfo);
    }

    @GetMapping("/api/-/{roleId}/{page}/{size}")
    @ApiOperation(value = "获取角色(roleId)未被授权的API资源")
    public Message getRestApiByRoleId(
            @PathVariable Long roleId,
            @PathVariable Integer page,
            @PathVariable Integer size) {

        PageInfo<AuthResourceDO> pageInfo = new PageInfo<>(page, size);
        resourceService.listNotAuthorityApiByRoleId(roleId, pageInfo);
        return new Message().ok(6666, "return api success").addData("data", pageInfo);
    }

    @GetMapping("api/{roleId}/{page}/{size}")
    @ApiOperation(value = "获取角色(roleId)所被授权的API资源")
    public Message getRestApiExtendByRoleId(
            @PathVariable Long roleId,
            @PathVariable Integer page,
            @PathVariable Integer size) {

        PageInfo<AuthResourceDO> pageInfo = new PageInfo<>(page, size);
        resourceService.listAuthorityApiByRoleId(roleId, pageInfo);
        return new Message().ok(6666, "return api success").addData("data", pageInfo);
    }

    @GetMapping("user/-/{roleId}/{page}/{size}")
    @ApiOperation(value = "获取角色未关联的用户列表", httpMethod = "GET")
    public Message getUserListExtendByRoleId(
            @PathVariable Long roleId,
            @PathVariable Integer page,
            @PathVariable Integer size) {

        PageInfo<AuthUserDO> pageInfo = new PageInfo<>(page, size);
        userService.listNotAuthorityUserByRoleId(roleId, pageInfo);
        return new Message().ok(6666, "return users success").addData("data", pageInfo);
    }

    @GetMapping("user/{roleId}/{page}/{size}")
    @ApiOperation(value = "获取角色(roleId)关联的对应用户列表", httpMethod = "GET")
    public Message getUserListByRoleId(
            @PathVariable Long roleId,
            @PathVariable Integer page,
            @PathVariable Integer size) {

        PageInfo<AuthUserDO> pageInfo = new PageInfo<>(page, size);
        userService.listUserByRoleId(roleId, pageInfo);
        return new Message().ok(6666, "return users success").addData("data", pageInfo);
    }
}
