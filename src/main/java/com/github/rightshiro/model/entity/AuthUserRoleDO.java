package com.github.rightshiro.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.github.rightshiro.model.constant.DbTablePrefix;

/**
 * @author weiguangchao
 * @date 2020/11/21
 */
@Entity
@Table(name = DbTablePrefix.AUTH + "user_role")
public class AuthUserRoleDO extends BaseDO {

    @Column(name = "user_id")
    private String userId;
    @Column(name = "role_id")
    private Long roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "AuthUserRoleDO{" +
                "userId='" + userId + '\'' +
                ", roleId='" + roleId + '\'' +
                "} " + super.toString();
    }
}
