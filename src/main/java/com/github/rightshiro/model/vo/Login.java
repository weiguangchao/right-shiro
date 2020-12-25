package com.github.rightshiro.model.vo;

import java.sql.Timestamp;

/**
 * @author weiguangchao
 * @date 2020/11/26
 */
public class Login {

    private String appId;
    private String username;
    private String password;
    private String methodName;
    private Timestamp timestamp;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "appId='" + appId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", methodName='" + methodName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
