package com.github.rightshiro.model.vo;

import java.util.List;

/**
 * @author weiguangchao
 * @date 2020/11/27
 */
public class PageInfo<T> {

    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalElement;
    private List<T> content;

    public PageInfo() {
    }

    public PageInfo(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(Long totalElement) {
        this.totalElement = totalElement;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "page=" + page +
                ", size=" + size +
                ", totalPage=" + totalPage +
                ", totalElement=" + totalElement +
                ", content=" + content +
                '}';
    }
}
