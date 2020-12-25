package com.github.rightshiro.shiro.provider.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.rightshiro.repository.AuthResourceRepository;
import com.github.rightshiro.shiro.provider.ShiroFilterRulesProvider;
import com.github.rightshiro.shiro.rule.RoleResourceRule;

import cn.hutool.core.util.StrUtil;

/**
 * @author weiguangchao
 * @date 2020/11/13
 */
@Component
public class ShiroFilterRulesProviderImpl implements ShiroFilterRulesProvider {

    private AuthResourceRepository authResourceRepository;

    public ShiroFilterRulesProviderImpl(AuthResourceRepository authResourceRepository) {
        this.authResourceRepository = authResourceRepository;
    }

    @Override
    public List<RoleResourceRule> loadRoleResourceRule() {
        List<Object[]> resourceAndRoleList = authResourceRepository.listResAndRole();
        // 0：resource uri method
        // 1：role
        Map<String, String> resourceRoleMap = new HashMap<>();
        resourceAndRoleList.forEach(resourceAndRole -> {
            String resource = (String) resourceAndRole[0];
            Object o = resourceAndRole[1];
            if (Objects.nonNull(o)) {
                String role = (String) o;
                if (!resourceRoleMap.containsKey(resource)) { // 第一次
                    resourceRoleMap.put(resource, role);
                }
                else {
                    String before = resourceRoleMap.get(resource);
                    resourceRoleMap.put(resource, before + "," + role);
                }
            }
        });
        return buildRoleResourceRuleFromMap(resourceRoleMap);
    }

    private List<RoleResourceRule> buildRoleResourceRuleFromMap(Map<String, String> resourceRoleMap) {
        return resourceRoleMap.entrySet()
                .stream().map(entry -> new RoleResourceRule(entry.getKey(), entry.getValue()))
                .sorted((rrr1, rrr2) -> StrUtil.compare(rrr1.getUrl(), rrr2.getUrl(), true))
                .collect(Collectors.toList());
    }
}
