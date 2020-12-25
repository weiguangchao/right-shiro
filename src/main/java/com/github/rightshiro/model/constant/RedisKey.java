package com.github.rightshiro.model.constant;

/**
 * @author weiguangchao
 * @date 2020/11/26
 */
public interface RedisKey {

    String PREFIX = "RIGHT:";
    String TOKEN_KEY = PREFIX + "TOKEN_KEY:";
    String JWT_SESSION = PREFIX + "JWT_SESSION:";
}
