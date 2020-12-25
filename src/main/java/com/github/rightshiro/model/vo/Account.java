package com.github.rightshiro.model.vo;

import java.io.Serializable;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public class Account implements Serializable {

    private static final long serialVersionUID = -3210739368075636243L;

    private String appId;
    private String password;
    private String salt;

    public Account() {
    }

    public Account(String appId, String password, String salt) {
        this.appId = appId;
        this.password = password;
        this.salt = salt;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "appId='" + appId + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
