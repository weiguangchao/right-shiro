package com.github.rightshiro.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weiguangchao
 * @date 2020/12/3
 */
public abstract class BaseTreeNode {

    protected Integer id;
    protected Integer parentId;
    protected List<BaseTreeNode> children = new ArrayList<>();

    public void addChildren(BaseTreeNode node) {
        this.children.add(node);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<BaseTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<BaseTreeNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "BaseTreeNode{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", children=" + children +
                '}';
    }
}
