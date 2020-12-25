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
@Table(name = DbTablePrefix.AUTH + "resource")
public class AuthResourceDO extends BaseDO {

    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "uri")
    private String uri;
    /** 类型 1：菜单menu 2：资源element(rest-api) 3：资源分类 */
    @Column(name = "type")
    private Integer type;
    @Column(name = "method")
    private String method;
    @Column(name = "icon")
    private String icon;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AuthResourceDO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", uri='" + uri + '\'' +
                ", type=" + type +
                ", method='" + method + '\'' +
                ", icon='" + icon + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}
