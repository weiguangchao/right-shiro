package com.github.rightshiro.support.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 入口过滤器(包装request防御xss、sql注入).
 *
 * @author weiguangchao
 * @date 2020/11/26
 */
public class StartupFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.debug("right-shiro StartupFilter init...");
    }

    /**
     * 包装request.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = WebUtils.toHttp(request);
        chain.doFilter(new XssSqlHttpServletRequestWrapper(req), response);
    }

    @Override
    public void destroy() {
        LOGGER.debug("right-shiro StartupFilter destroy...");
    }
}
