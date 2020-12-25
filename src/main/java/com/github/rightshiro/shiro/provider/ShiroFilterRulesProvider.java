package com.github.rightshiro.shiro.provider;

import java.util.List;

import com.github.rightshiro.shiro.rule.RoleResourceRule;

/**
 * 动态过滤规则提供者接口
 * @author weiguangchao
 * @date 2020/11/13
 */
@FunctionalInterface
public interface ShiroFilterRulesProvider {

    /**
     * 加载基于角色/资源的过滤规则
     * 即：用户-角色-资源（URL），对应关系存储与数据库中
     * 在shiro中生成的过滤器链为：url=jwt[角色1,角色2,角色n]
     */
    List<RoleResourceRule> loadRoleResourceRule();
}
