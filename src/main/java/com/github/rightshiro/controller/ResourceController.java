package com.github.rightshiro.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rightshiro.model.entity.AuthResourceDO;
import com.github.rightshiro.model.vo.MenuTreeNode;
import com.github.rightshiro.model.vo.Message;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.service.ResourceService;
import com.github.rightshiro.util.TreeUtils;
import com.github.rightshiro.util.WebUtils;

import io.swagger.annotations.ApiOperation;

/**
 * @author weiguangchao
 * @date 2020/11/27
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    private ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/authorityMenu")
    @ApiOperation(value = "获取用户被授权菜单", notes = "通过uid获取对应用户被授权的菜单列表,获取完整菜单树形结构")
    public Message getAuthorityMenu() {
        HttpServletRequest request = WebUtils.getRequest();
        String uid = request.getHeader("appId");
        List<MenuTreeNode> treeNodes = new ArrayList<>();
        List<AuthResourceDO> resources = resourceService.listAuthorityMenuByUid(uid);

        for (AuthResourceDO resource : resources) {
            MenuTreeNode node = new MenuTreeNode();
            BeanUtils.copyProperties(resource, node);
            treeNodes.add(node);
        }
        List<MenuTreeNode> menuTreeNodes = TreeUtils.buildTreeBy2Loop(treeNodes, -1);
        return new Message().ok(6666, "return menu list success").addData("menuTree", menuTreeNodes);
    }

    @GetMapping("/menu")
    @ApiOperation(value = "获取全部菜单列", httpMethod = "GET")
    public Message listAllMenu() {
        List<MenuTreeNode> treeNodes = new ArrayList<>();
        List<AuthResourceDO> resources = resourceService.listAllMenu();

        for (AuthResourceDO resource : resources) {
            MenuTreeNode node = new MenuTreeNode();
            BeanUtils.copyProperties(resource, node);
            treeNodes.add(node);
        }
        List<MenuTreeNode> menuTreeNodes = TreeUtils.buildTreeBy2Loop(treeNodes, -1);
        return new Message().ok(6666, "return menus success").addData("menuTree", menuTreeNodes);
    }

    @PostMapping("/menu")
    @ApiOperation(value = "增加菜单", httpMethod = "POST")
    public Message addMenu(@RequestBody AuthResourceDO menu) {
        resourceService.addRes(menu);
        return new Message().ok(6666, "add menu success");
    }

    @PutMapping("/menu")
    @ApiOperation(value = "修改菜单", httpMethod = "PUT")
    public Message updateMenu(@RequestBody AuthResourceDO menu) {
        resourceService.updateRes(menu);
        return new Message().ok(6666, "update menu success");
    }

    @DeleteMapping("/menu/{menuId}")
    @ApiOperation(value = "删除菜单", notes = "根据菜单ID删除菜单", httpMethod = "DELETE")
    public Message deleteMenuById(@PathVariable Long menuId) {
        resourceService.deleteResById(menuId);
        return new Message().ok(6666, "delete menu success");
    }

    @DeleteMapping("/api/{apiId}")
    @ApiOperation(value = "删除API", notes = "根据API_ID删除API", httpMethod = "DELETE")
    public Message deleteApiById(@PathVariable Long apiId) {
        resourceService.deleteResById(apiId);
        return new Message().ok(6666, "delete api success");
    }

    @PutMapping("/api")
    @ApiOperation(value = "修改API", httpMethod = "PUT")
    public Message updateApi(@RequestBody AuthResourceDO api) {
        resourceService.updateRes(api);
        return new Message().ok(6666, "update api success");
    }

    @PostMapping("/api")
    @ApiOperation(value = "增加API", httpMethod = "POST")
    public Message addApi(@RequestBody AuthResourceDO api) {
        resourceService.addRes(api);
        return new Message().ok(6666, "add api success");
    }

    @GetMapping("/api/{teamId}/{page}/{size}")
    @ApiOperation(
            value = "获取API list",
            notes = "需要分页,根据teamId判断,-1->获取api分类,0->获取全部api,id->获取对应分类id下的api",
            httpMethod = "GET"
    )
    public Message listApi(
            @PathVariable Long teamId,
            @PathVariable Integer page,
            @PathVariable Integer size) {

        if (teamId == -1) { // 为获取api分类
            List<AuthResourceDO> resultList = resourceService.listApiTeam();
            return new Message()
                    .ok(6666, "return apis success")
                    .addData("data", resultList);
        }
        PageInfo<AuthResourceDO> pageInfo = new PageInfo<>(page, size);
        if (teamId == 0) { // 为获取全部api
            resourceService.listApi(pageInfo);
        }
        else { // 其他查询teamId 对应分类下的apis
            resourceService.listApiByTeamId(teamId, pageInfo);
        }
        return new Message()
                .ok(6666, "return apis success")
                .addData("data", pageInfo);
    }
}
