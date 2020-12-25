package com.github.rightshiro.model.vo;

import java.sql.Timestamp;

/**
 * @author weiguangchao
 * @date 2020/12/3
 */
public class MenuTreeNode extends BaseTreeNode {

    private String code;
    private String name;
    private String uri;
    private Short type;
    private String method;
    private String icon;
    private Short status;
    private Timestamp createTime;
    private Timestamp updateTime;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
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

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MenuTreeNode{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", type=" + type +
                ", method='" + method + '\'' +
                ", icon='" + icon + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "} " + super.toString();
    }
}
