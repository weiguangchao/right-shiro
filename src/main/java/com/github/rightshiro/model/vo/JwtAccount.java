package com.github.rightshiro.model.vo;

import java.util.Date;

/**
 * @author weiguangchao
 * @date 2020/11/19
 */
public class JwtAccount {

    /** 令牌id */
    private String tokenId;
    /** 客户标识(用户名、账号) */
    private String appId;
    /** 签发者(JWT令牌此项有值) */
    private String issuer;
    /** 签发时间 */
    private Date issuedAt;
    /** 接收方(JWT令牌此项有值) */
    private String audience;
    /** 访问主张-角色(JWT令牌此项有值) */
    private String roles;
    /** 访问主张-权限(JWT令牌此项有值) */
    private String perms;
    /** 客户地址 */
    private String host;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "JwtAccount{" +
                "tokenId='" + tokenId + '\'' +
                ", appId='" + appId + '\'' +
                ", issuer='" + issuer + '\'' +
                ", issuedAt=" + issuedAt +
                ", audience='" + audience + '\'' +
                ", roles='" + roles + '\'' +
                ", perms='" + perms + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
