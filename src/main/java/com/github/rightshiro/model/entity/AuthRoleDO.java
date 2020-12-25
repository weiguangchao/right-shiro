package com.github.rightshiro.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.github.rightshiro.model.constant.DbTablePrefix;

/**
 * @author weiguangchao
 * @date 2020/11/13
 */
@Entity
@Table(name = DbTablePrefix.AUTH + "role")
public class AuthRoleDO extends BaseDO {

    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private Integer status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AuthRoleDO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}
