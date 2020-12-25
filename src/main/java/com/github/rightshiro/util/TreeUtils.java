package com.github.rightshiro.util;

import java.util.ArrayList;
import java.util.List;

import com.github.rightshiro.model.vo.BaseTreeNode;

/**
 * @author weiguangchao
 * @date 2020/12/3
 */
public abstract class TreeUtils {

    /**
     * 用双重循环建树.
     *
     * @param treeNodes 树节点列表
     * @param root 根标志
     * @param <T>
     * @return
     */
    public static <T extends BaseTreeNode> List<T> buildTreeBy2Loop(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<>();
        for (T node : treeNodes) {
            if (root.equals(node.getParentId())) {
                trees.add(node);
            }

            for (T treeNode : treeNodes) {
                if (node.getId().equals(treeNode.getParentId())) {
                    if (node.getChildren() == null) {
                        node.setChildren(new ArrayList<>());
                    }
                    node.addChildren(treeNode);
                }
            }
        }
        return trees;
    }
}
