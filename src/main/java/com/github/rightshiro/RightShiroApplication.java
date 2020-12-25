package com.github.rightshiro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author weiguangchao
 * @date 2020/11/13
 */
@SpringBootApplication
public class RightShiroApplication extends SpringBootServletInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RightShiroApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(RightShiroApplication.class, args);
        }
        catch (Throwable t) {
            LOGGER.error("应用启动失败::" + t.getMessage(), t);
        }
    }

    /**
     * 兼容外部tomcat启动的方式.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RightShiroApplication.class);
    }
}
