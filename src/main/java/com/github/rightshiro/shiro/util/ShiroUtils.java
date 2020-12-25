package com.github.rightshiro.shiro.util;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;

/**
 * shiro工具类，方便获取shiro内部的一些bean.
 *
 * @author weiguangchao
 * @date 2020/12/5
 */
public abstract class ShiroUtils {

    private static DefaultFilterChainManager filterChainManager;

    public static DefaultFilterChainManager getFilterChainManager() {
        return filterChainManager;
    }

    public static void setFilterChainManager(FilterChainManager filterChainManager) {
        if (filterChainManager instanceof DefaultFilterChainManager) {
            ShiroUtils.filterChainManager = (DefaultFilterChainManager) filterChainManager;
        }
    }
}
