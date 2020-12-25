DROP TABLE IF EXISTS sys_sequence;
CREATE TABLE sys_sequence
(
    code        VARCHAR(100)    NOT NULL COMMENT '序列号标识',
    no          BIGINT UNSIGNED NULL DEFAULT NULL COMMENT '序列号',
    PRIMARY KEY (code)
) COMMENT = '系统序列号表';

DROP TABLE IF EXISTS auth_resource;
CREATE TABLE auth_resource
(
    id          BIGINT UNSIGNED NOT NULL COMMENT '资源ID',
    code        VARCHAR(30)     NULL DEFAULT NULL COMMENT '资源名称',
    name        VARCHAR(30)     NULL DEFAULT NULL COMMENT '资源描述',
    parent_id   BIGINT          NULL DEFAULT NULL COMMENT '父资源编码->菜单',
    uri         VARCHAR(100)    NULL DEFAULT NULL COMMENT '访问地址URL',
    type        TINYINT         NULL DEFAULT NULL COMMENT '类型 1:菜单menu 2:资源element(rest-api) 3:资源分类',
    method      VARCHAR(10)     NULL DEFAULT NULL COMMENT '访问方式 GET POST PUT DELETE PATCH',
    icon        VARCHAR(100)    NULL DEFAULT NULL COMMENT '图标',
    status      TINYINT         NULL DEFAULT 1 COMMENT '状态   1:正常、9：禁用',
    create_time DATETIME        NULL DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME        NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '资源信息表(菜单,资源)';

DROP TABLE IF EXISTS auth_role;
CREATE TABLE auth_role
(
    id          BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    code        VARCHAR(30)     NOT NULL COMMENT '角色编码',
    name        VARCHAR(30)     NULL DEFAULT NULL COMMENT '角色名称',
    status      TINYINT         NULL DEFAULT 1 COMMENT '状态   1:正常、9：禁用',
    create_time DATETIME        NULL DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME        NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '角色信息表';

DROP TABLE IF EXISTS auth_role_resource;
CREATE TABLE auth_role_resource
(
    id          BIGINT UNSIGNED NOT NULL COMMENT '主键ID',
    role_id     BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    resource_id BIGINT UNSIGNED NOT NULL COMMENT '资源ID',
    create_time DATETIME        NULL DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME        NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '资源角色关联表';

DROP TABLE IF EXISTS auth_user;
CREATE TABLE auth_user
(
    id           BIGINT UNSIGNED NOT NULL COMMENT '主键ID',
    uid          varchar(30)     NOT NULL COMMENT 'uid,用户账号,唯一',
    username     varchar(30)     NOT NULL COMMENT '用户名(nick_name)',
    password     varchar(50)     NOT NULL COMMENT '密码(MD5(密码+盐))',
    salt         varchar(20)     NULL DEFAULT NULL COMMENT '盐',
    real_name    varchar(30)     NULL DEFAULT NULL COMMENT '用户真名',
    avatar       varchar(100)    NULL DEFAULT NULL COMMENT '头像',
    phone        varchar(20)     NULL DEFAULT NULL COMMENT '电话号码(唯一)',
    email        varchar(50)     NULL DEFAULT NULL COMMENT '邮件地址(唯一)',
    sex          tinyint         NULL DEFAULT NULL COMMENT '性别(1.男 2.女)',
    status       tinyint         NULL DEFAULT NULL COMMENT '账户状态(1.正常 2.锁定 3.删除 4.非法)',
    create_time  datetime        NULL DEFAULT NULL COMMENT '创建时间',
    update_time  datetime        NULL DEFAULT NULL COMMENT '更新时间',
    create_where tinyint         NULL DEFAULT NULL COMMENT '创建来源(1.web 2.android 3.ios 4.win 5.macos 6.ubuntu)',
    PRIMARY KEY (id)
) COMMENT = '用户信息表';


DROP TABLE IF EXISTS auth_account_log;
CREATE TABLE auth_account_log
(
    id          BIGINT UNSIGNED NOT NULL COMMENT '主键ID',
    log_name    varchar(255)    NULL DEFAULT NULL COMMENT '日志名称(login,register,logout)',
    user_id     varchar(30)     NULL DEFAULT NULL COMMENT '用户uid',
    succeed     tinyint         NULL DEFAULT NULL COMMENT '是否执行成功(0失败1成功)',
    message     varchar(255)    NULL DEFAULT NULL COMMENT '具体消息',
    ip          varchar(255)    NULL DEFAULT NULL COMMENT '登录ip',
    create_time datetime        NULL DEFAULT NULL COMMENT '创建时间',
    update_time datetime        NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '登录注册登出记录';


DROP TABLE IF EXISTS auth_operation_log;
CREATE TABLE auth_operation_log
(
    id          BIGINT UNSIGNED NOT NULL COMMENT '主键ID',
    log_name    varchar(255)    NULL DEFAULT NULL COMMENT '日志名称',
    user_id     varchar(30)     NULL DEFAULT NULL COMMENT '用户uid',
    api         varchar(255)    NULL DEFAULT NULL COMMENT 'api名称',
    method      varchar(255)    NULL DEFAULT NULL COMMENT '方法名称',
    create_time datetime        NULL DEFAULT NULL COMMENT '创建时间',
    update_time datetime        NULL DEFAULT NULL COMMENT '更新时间',
    succeed     tinyint         NULL DEFAULT NULL COMMENT '是否执行成功(0失败1成功)',
    message     varchar(255)    NULL DEFAULT NULL COMMENT '具体消息备注',
    PRIMARY KEY (id)
) COMMENT = '操作日志';

DROP TABLE IF EXISTS auth_user_role;
CREATE TABLE auth_user_role
(
    id          BIGINT UNSIGNED NOT NULL COMMENT '主键ID',
    user_id     varchar(30)     NOT NULL COMMENT '用户UID',
    role_id     BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    create_time datetime        NULL DEFAULT NULL COMMENT '创建时间',
    update_time datetime        NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '用户角色关联表';


INSERT INTO sys_sequence(code, no) VALUES ('auth_role', 4);