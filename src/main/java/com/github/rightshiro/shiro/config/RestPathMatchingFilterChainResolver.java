package com.github.rightshiro.shiro.config;

import java.util.Iterator;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rightshiro.util.WebUtils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * jwt规则resolver.
 *
 * @author weiguangchao
 * @date 2020/11/23
 */
public class RestPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestPathMatchingFilterChainResolver.class);
    private static final String DEFAULT_PATH_SEPARATOR = "/";

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = this.getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }
        else {
            String requestUri = WebUtils.splitUri(this.getPathWithinApplication(request));
            // 获取所有的代理规则
            Iterator<String> iterator = filterChainManager.getChainNames().iterator();
            String pathPattern;
            String[] strArr;
            boolean flag;
            // 针对jwt规则进行解析 /xxx/xx==GET
            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                pathPattern = iterator.next();

                strArr = pathPattern.split("==");
                if (strArr.length == 2) {
                    // 判断request method与定义的是否一致，若不一致直接pass
                    flag = !StrUtil.equals(
                            WebUtils.getRequestMethod(request),
                            strArr[1].toUpperCase()
                    );
                }
                else {
                    flag = false;
                }
                // shiro代理的URL
                pathPattern = WebUtils.splitUri(strArr[0]);
            }
            // 首先进行路径匹配
            // 其次进行method匹配
            while (!super.pathMatches(pathPattern, requestUri) || flag);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestUri
                        + "].  Utilizing corresponding filter chain...");
            }

            if (ArrayUtil.length(strArr) == 2) {
                pathPattern = StrUtil.concat(
                        true,
                        pathPattern, "==", WebUtils.getRequestMethod(request)
                );
            }

            return filterChainManager.proxy(originalChain, pathPattern);
        }
    }
}
