package com.github.rightshiro.util;

import com.github.rightshiro.config.debug.UserConfig;
import com.github.rightshiro.support.SpringContextHolder;

import cn.hutool.core.util.StrUtil;

/**
 * @author weiguangchao
 * @date 2020/12/2
 */
public abstract class AppUtils {

    public static boolean isDebugModel() {
        return Boolean.parseBoolean(SpringContextHolder.getProperty("right.debug.enable", "false"));
    }

    public static UserConfig getDebugUser() {
        return SpringContextHolder.getBean(UserConfig.class);
    }

    public static boolean isDebugUser(String appId) {
        boolean idDebugModel = Boolean.parseBoolean(
                SpringContextHolder.getProperty("right.debug.enable", "false")
        );

        return idDebugModel && StrUtil.equals(
                appId,
                getDebugUser().getUsername()
        );
    }
}
