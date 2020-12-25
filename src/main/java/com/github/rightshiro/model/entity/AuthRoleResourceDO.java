package com.github.rightshiro.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.github.rightshiro.model.constant.DbTablePrefix;

/**
 * @author weiguangchao
 * @date 2020/11/27
 */
@Entity
@Table(name = DbTablePrefix.AUTH + "role_resource")
public class AuthRoleResourceDO extends BaseDO {

    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "resource_id")
    private Long resourceId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return "AuthRoleResource{" +
                "roleId=" + roleId +
                ", resourceId=" + resourceId +
                "} " + super.toString();
    }
}
