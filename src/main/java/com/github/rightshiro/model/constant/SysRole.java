package com.github.rightshiro.model.constant;

/**
 * 系统角色.
 *
 * @author weiguangchao
 * @date 2020/11/27
 */
public interface SysRole {

    /** 管理员角色 */
    Long ID_ADMIN = 1L;
    /** 用户角色 */
    Long ID_USER = 2L;
    /** 访客角色 */
    Long ID_GUEST = 3L;
    /** 非角色 */
    Long ID_ANNO = 4L;

    /** 管理员角色 */
    String CODE_ADMIN = "role_admin";
    /** 用户角色 */
    String CODE_USER = "role_user";
    /** 访客角色 */
    String CODE_GUEST = "role_guest";
    /** 非角色 */
    String CODE_ANNO = "role_anno";
}
