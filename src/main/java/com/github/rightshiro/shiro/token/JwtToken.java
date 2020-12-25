package com.github.rightshiro.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public class JwtToken implements AuthenticationToken {

    /** 用户标识 */
    private String appId;
    /** 用户ip */
    private String ipHost;
    /** 设备信息 */
    private String deviceInfo;
    /** jwt */
    private String jwt;

    public JwtToken(String ipHost, String deviceInfo, String jwt, String appId) {
        this.ipHost = ipHost;
        this.deviceInfo = deviceInfo;
        this.jwt = jwt;
        this.appId = appId;
    }

    @Override
    public Object getPrincipal() {
        return appId;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIpHost() {
        return ipHost;
    }

    public void setIpHost(String ipHost) {
        this.ipHost = ipHost;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "JwtToken{" +
                "appId='" + appId + '\'' +
                ", ipHost='" + ipHost + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
