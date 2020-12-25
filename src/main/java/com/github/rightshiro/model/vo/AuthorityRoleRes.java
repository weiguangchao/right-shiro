package com.github.rightshiro.model.vo;

/**
 * @author weiguangchao
 * @date 2020/12/5
 */
public class AuthorityRoleRes {

    private Long roleId;
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
        return "AuthorityRoleRes{" +
                "roleId=" + roleId +
                ", resourceId=" + resourceId +
                '}';
    }
}
