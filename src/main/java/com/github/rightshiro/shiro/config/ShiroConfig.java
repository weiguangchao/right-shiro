package com.github.rightshiro.shiro.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.rightshiro.shiro.filter.StatelessWebSubjectFactory;
import com.github.rightshiro.shiro.realm.OnceModularRealmAuthenticator;
import com.github.rightshiro.shiro.support.RealmManager;
import com.github.rightshiro.shiro.support.ShiroFilterChainManager;

/**
 * @author weiguangchao
 * @date 2020/11/14
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            RealmManager realmManager,
            ShiroFilterChainManager filterChainManager) {

        SecurityManager securityManager = createSecurityManager(realmManager);

        RestShiroFilterFactoryBean filterFactoryBean = new RestShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setFilterChainDefinitionMap(filterChainManager.initFilterChain());
        filterFactoryBean.setFilters(filterChainManager.initFilter());
        return filterFactoryBean;
    }

    private SecurityManager createSecurityManager(RealmManager realmManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(new OnceModularRealmAuthenticator());
        securityManager.setRealms(realmManager.initRealm());

        // 无状态subjectFactory设置
        DefaultSessionStorageEvaluator evaluator = (DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager
                .getSubjectDAO()).getSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(Boolean.FALSE);
        securityManager.setSubjectFactory(new StatelessWebSubjectFactory());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }
}
