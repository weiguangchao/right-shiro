package com.github.rightshiro.config.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.rightshiro.support.web.StartupFilter;

/**
 * @author weiguangchao
 * @date 2020/11/25
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<StartupFilter> startupFilterRegistrationBean() {
        FilterRegistrationBean<StartupFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new StartupFilter()
        );
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setName("startupFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
