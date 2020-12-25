package com.github.rightshiro.shiro.rule;

import java.io.Serializable;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.github.rightshiro.util.JwtUtils;

/**
 * @author weiguangchao
 * @date 2020/11/13
 */
public class RoleResourceRule implements Serializable {

    private static final long serialVersionUID = -7377843698049410251L;

    /** 匿名用户(anno) */
    private static final String ANON_ROLE = "role_anon";

    /** 资源URL */
    private String url;

    /** 访问资源所需要的角色列表，多个列表用逗号间隔 */
    private String needRoles;

    public RoleResourceRule() {
    }

    public RoleResourceRule(String url, String needRoles) {
        this.url = url;
        this.needRoles = needRoles;
    }

    /**
     * 将url needRoles 转化成shiro可识别的过滤器链：url=jwt[角色1、角色2、角色n]
     */
    public String toFilterChain() {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        Set<String> roleSet = JwtUtils.split(needRoles);

        if (!StringUtils.isEmpty(needRoles)) {
            // 匿名访问
            if (roleSet.contains(ANON_ROLE)) {
                stringBuilder.append("anon");
            }
            else {
                stringBuilder.append("jwt[").append(this.getNeedRoles()).append("]");
            }
        }

        return stringBuilder.length() > 0 ? stringBuilder.toString() : null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNeedRoles() {
        return needRoles;
    }

    public void setNeedRoles(String needRoles) {
        this.needRoles = needRoles;
    }

    @Override
    public String toString() {
        return "RoleResourceRule{" +
                "url='" + url + '\'' +
                ", needRoles='" + needRoles + '\'' +
                '}';
    }
}
