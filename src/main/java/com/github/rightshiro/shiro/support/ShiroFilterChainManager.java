package com.github.rightshiro.shiro.support;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.github.rightshiro.repository.AuthRoleRepository;
import com.github.rightshiro.repository.AuthUserRepository;
import com.github.rightshiro.shiro.filter.JwtFilter;
import com.github.rightshiro.shiro.filter.PasswordFilter;
import com.github.rightshiro.shiro.rule.RoleResourceRule;
import com.github.rightshiro.shiro.util.ShiroUtils;
import com.github.rightshiro.support.SpringContextHolder;
import com.github.rightshiro.shiro.config.RestShiroFilterFactoryBean;
import com.github.rightshiro.shiro.provider.ShiroFilterRulesProvider;
import com.github.rightshiro.support.factory.LogTaskFactory;

/**
 * @author weiguangchao
 * @date 2020/11/14
 */
@Component
public class ShiroFilterChainManager {

    @Value("${right.password.encrypt.enable:true}")
    private boolean isEncryptPassword;
    @Value("${right.login.token-key.timeout:5}")
    private Long tokenKeyTimeout;

    private StringRedisTemplate redisTemplate;
    private ShiroFilterRulesProvider shiroFilterRulesProvider;
    private LogTaskFactory logTaskFactory;
    private AuthRoleRepository authRoleRepository;
    private AuthUserRepository authUserRepository;

    public ShiroFilterChainManager(StringRedisTemplate redisTemplate,
            ShiroFilterRulesProvider shiroFilterRulesProvider,
            LogTaskFactory logTaskFactory, AuthRoleRepository authRoleRepository,
            AuthUserRepository authUserRepository) {
        this.redisTemplate = redisTemplate;
        this.shiroFilterRulesProvider = shiroFilterRulesProvider;
        this.logTaskFactory = logTaskFactory;
        this.authRoleRepository = authRoleRepository;
        this.authUserRepository = authUserRepository;
    }

    /**
     * 初始化shiro filter.
     *
     * @return filter
     */
    public Map<String, Filter> initFilter() {
        Map<String,Filter> filters = new LinkedHashMap<>();

        // ----- password
        PasswordFilter passwordFilter = new PasswordFilter();
        passwordFilter.setRedisTemplate(redisTemplate);
        passwordFilter.setEncryptPassword(isEncryptPassword);
        passwordFilter.setTokenKeyTimeout(tokenKeyTimeout);
        filters.put("auth",passwordFilter);

        // ----- jwt
        JwtFilter jwtFilter = new JwtFilter();
        jwtFilter.setRedisTemplate(redisTemplate);
        jwtFilter.setLogTaskFactory(logTaskFactory);
        jwtFilter.setAuthRoleRepository(authRoleRepository);
        jwtFilter.setAuthUserRepository(authUserRepository);
        filters.put("jwt",jwtFilter);

        return filters;
    }

    /**
     * 初始化shiro过滤器链规则.
     */
    public Map<String, String> initFilterChain() {
        Map<String, String> filterChain = new LinkedHashMap<>();
        // -------------anon 默认过滤器忽略的URL
        List<String> defaultAnon = Arrays.asList("/css/**", "/js/**");
        defaultAnon.forEach(ignored -> filterChain.put(ignored, "anon"));

        // -------------auth 默认需要认证过滤器的URL 走auth--PasswordFilter
        List<String> defaultAuth = Arrays.asList("/account/**");
        defaultAuth.forEach(auth -> filterChain.put(auth, "auth"));

        // -------------dynamic 动态URL
        List<RoleResourceRule> roleResourceRuleList = this.shiroFilterRulesProvider.loadRoleResourceRule();
        if (null != roleResourceRuleList) {
            roleResourceRuleList.forEach(rule -> {
                String chain = rule.toFilterChain();
                if (!StringUtils.isEmpty(chain)) {
                    filterChain.putIfAbsent(rule.getUrl(), chain);
                }
            });
        }
        return filterChain;
    }

    /**
     * 重新加载shiro过滤器链规则.
     */
    public void reloadFilterChain() {
        RestShiroFilterFactoryBean shiroFilterFactoryBean = SpringContextHolder.getBean(RestShiroFilterFactoryBean.class);
        DefaultFilterChainManager filterChainManager = ShiroUtils.getFilterChainManager();

        filterChainManager.getFilterChains().clear();
        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(this.initFilterChain());
        shiroFilterFactoryBean.getFilterChainDefinitionMap().forEach(filterChainManager::createChain);
    }
}
