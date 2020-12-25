package com.github.rightshiro.config.debug;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.lang.Assert;


/**
 * @author weiguangchao
 * @date 2020/12/1
 */
@Configuration
@ConfigurationProperties("right.debug")
@ConditionalOnProperty(value = "right.debug.enable", havingValue = "true")
public class UserConfig {

    private boolean enable;
    private String username;
    private String password;

    @PostConstruct
    public void init() {
        Assert.notNull(this.getUsername(), "username is empty");
        Assert.notNull(this.getPassword(), "password is empty");
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
