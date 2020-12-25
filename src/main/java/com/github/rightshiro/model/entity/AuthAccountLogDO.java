package com.github.rightshiro.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.github.rightshiro.model.constant.DbTablePrefix;

/**
 * @author weiguangchao
 * @date 2020/11/19
 */
@Entity
@Table(name = DbTablePrefix.AUTH + "account_log")
public class AuthAccountLogDO extends BaseDO {

    @Column(name = "log_name")
    private String logName;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "ip")
    private String ip;
    @Column(name = "succeed")
    private Integer succeed;
    @Column(name = "message")
    private String message;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getSucceed() {
        return succeed;
    }

    public void setSucceed(Integer succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthAccountLog{" +
                "logName='" + logName + '\'' +
                ", userId='" + userId + '\'' +
                ", ip='" + ip + '\'' +
                ", succeed=" + succeed +
                ", message='" + message + '\'' +
                "} " + super.toString();
    }
}
