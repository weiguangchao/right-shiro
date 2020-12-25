package com.github.rightshiro.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

import com.github.rightshiro.model.constant.DbTablePrefix;

/**
 * @author weiguangchao
 * @date 2020/10/17
 */
@MappedSuperclass
public class BaseDO implements Serializable {

    private static final long serialVersionUID = 4570615730678677970L;

    /** 主键标识 */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sequence_no")
    @TableGenerator(
            name = "sequence_no",
            allocationSize = 1,
            table = DbTablePrefix.SYS + "sequence",
            pkColumnName = "code",
            valueColumnName = "no"
    )
    private Long id;
    /** 创建时间 */
    @Column(name = "create_time")
    private Timestamp createTime;
    /** 最后修改时间 */
    @Column(name = "update_time")
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "BaseDO{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
