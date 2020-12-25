package com.github.rightshiro.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author weiguangchao
 * @date 2020/11/25
 */
public abstract class SystemUtils {

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Date getCurrentDate() {
        return new Date();
    }
}
