package com.github.rightshiro.shiro.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;

import com.github.rightshiro.shiro.util.ShiroUtils;

/**
 * 配置jwt规则解析resolver.
 *
 * @author weiguangchao
 * @date 2020/11/23
 */
public class RestShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestShiroFilterFactoryBean.class);

    /**
     * @see ShiroFilterFactoryBean#createInstance()
     */
    @Override
    protected AbstractShiroFilter createInstance() throws Exception {

        LOGGER.debug("Creating Shiro Filter instance.");

        SecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            String msg = "SecurityManager property must be set.";
            throw new BeanInitializationException(msg);
        }

        if (!(securityManager instanceof WebSecurityManager)) {
            String msg = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(msg);
        }

        FilterChainManager manager = createFilterChainManager();

        // 保存FilterChainManager到工具类
        ShiroUtils.setFilterChainManager(manager);

        // Expose the constructed FilterChainManager by first wrapping it in a
        // FilterChainResolver implementation. The AbstractShiroFilter implementations
        // do not know about FilterChainManagers - only resolvers:
        // 使用jwt规则的resolver
        RestPathMatchingFilterChainResolver chainResolver = new RestPathMatchingFilterChainResolver();
        // PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);

        // Now create a concrete ShiroFilter instance and apply the acquired SecurityManager and built
        // FilterChainResolver.  It doesn't matter that the instance is an anonymous inner class
        // here - we're just using it because it is a concrete AbstractShiroFilter instance that accepts
        // injection of the SecurityManager and FilterChainResolver:
        return new SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }

    /**
     * @see ShiroFilterFactoryBean.SpringShiroFilter
     */
    private static final class SpringShiroFilter extends AbstractShiroFilter {

        protected SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            super();
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
            }
            setSecurityManager(webSecurityManager);

            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }
    }
}
