package com.github.rightshiro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rightshiro.model.entity.AuthUserRoleDO;
import com.github.rightshiro.model.vo.Message;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.service.UserService;
import com.github.rightshiro.model.entity.AuthUserDO;

import io.swagger.annotations.ApiOperation;

/**
 * @author weiguangchao
 * @date 2020/12/3
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/authority/role/{uid}/{roleId}")
    @ApiOperation(value = "删除已经授权的用户角色", httpMethod = "DELETE")
    public Message deleteAuthorityUserRole(@PathVariable String uid, @PathVariable Long roleId) {
        userService.deleteUserRole(uid, roleId);
        return new Message().ok(6666, "delete success");
    }

    @PostMapping("/authority/role")
    @ApiOperation(value = "给用户授权添加角色", httpMethod = "POST")
    public Message authorityUserRole(@RequestBody AuthUserRoleDO userRole) {
        userService.grantUserRole(userRole.getUserId(), userRole.getRoleId());
        return new Message().ok(6666, "authority success");
    }

    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "获取用户列表", notes = "GET获取所有注册用户的信息列表")
    public Message getUserList(@PathVariable Integer page, @PathVariable Integer size) {
        PageInfo<AuthUserDO> pageInfo = new PageInfo<>(page, size);
        userService.listAllUser(pageInfo);
        return new Message().ok(6666, "return user list success").addData("pageInfo", pageInfo);
    }

    @GetMapping("/role/{appId}")
    @ApiOperation(value = "获取对应用户角色", notes = "GET根据用户的appId获取对应用户的角色")
    public Message getUserRoleList(@PathVariable String appId) {
        List<String> roleList = userService.listUserRoleByUid(appId);
        return new Message().ok(6666, "return roles success").addData("roles", roleList);
    }
}
