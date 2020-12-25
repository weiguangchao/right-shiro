package com.github.rightshiro.support;

import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Spring ApplicationContext工具.
 *
 * @author weiguangchao
 * @date 2020/11/19
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;

    public static String getProperty(String key) {
        return getEnvironment().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return getEnvironment().getProperty(key, defaultValue);
    }

    public static Environment getEnvironment() {
        return get().getEnvironment();
    }

    public static ApplicationContext getApplicationContext() {
        return get();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) get().getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) get().getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 获取ApplicationContext之前进行检查.
     */
    private static ApplicationContext get() {
        if (Objects.isNull(APPLICATION_CONTEXT)) {
            throw new IllegalStateException("applicationContext为空，请检查是否注入springContextHolder！");
        }
        return APPLICATION_CONTEXT;
    }
}
