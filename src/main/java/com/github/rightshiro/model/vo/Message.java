package com.github.rightshiro.model.vo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weiguangchao
 * @date 2020/11/21
 */
public class Message {

    /** 消息头meta 存放状态信息 code message */
    private Map<String, Object> meta = new HashMap<>();
    /** 消息内容 存储实体交互数据 */
    private Map<String, Object> data = new HashMap<>();

    public Message ok(int statusCode, String statusMsg) {
        this.addMeta("success", Boolean.TRUE);
        this.addMeta("code", statusCode);
        this.addMeta("msg", statusMsg);
        this.addMeta("timestamp", new Timestamp(System.currentTimeMillis()));
        return this;
    }

    public Message error(int statusCode, String statusMsg) {
        this.addMeta("success", Boolean.FALSE);
        this.addMeta("code", statusCode);
        this.addMeta("msg", statusMsg);
        this.addMeta("timestamp", new Timestamp(System.currentTimeMillis()));
        return this;
    }

    public Message addMeta(String key, Object object) {
        this.meta.put(key, object);
        return this;
    }

    public Message addData(String key, Object object) {
        this.data.put(key, object);
        return this;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }
}
