package com.github.rightshiro.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public class PasswordToken implements AuthenticationToken {

    private String appId;
    private String password;
    private String timestamp;
    private String host;

    public PasswordToken(String appId, String password, String timestamp, String host) {
        this.appId = appId;
        this.timestamp = timestamp;
        this.host = host;
        this.password = password;

    }

    @Override
    public Object getPrincipal() {
        return appId;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "PasswordToken{" +
                "appId='" + appId + '\'' +
                ", password='" + password + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
