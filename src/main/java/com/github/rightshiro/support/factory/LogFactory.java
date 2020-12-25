package com.github.rightshiro.support.factory;

import com.github.rightshiro.model.entity.AuthAccountLogDO;
import com.github.rightshiro.model.entity.AuthOperationLogDO;
import com.github.rightshiro.util.SystemUtils;

/**
 * @author weiguangchao
 * @date 2020/11/19
 */
public abstract class LogFactory {

    public static AuthOperationLogDO createOperationLog(String userId, String logName, String api, String method,
            Integer succeed, String message) {

        AuthOperationLogDO authOperationLogDO = new AuthOperationLogDO();
        authOperationLogDO.setUserId(userId);
        authOperationLogDO.setLogName(logName);
        authOperationLogDO.setApi(api);
        authOperationLogDO.setMethod(method);
        authOperationLogDO.setSucceed(succeed);
        authOperationLogDO.setMessage(message);
        authOperationLogDO.setCreateTime(SystemUtils.getCurrentTimestamp());
        return authOperationLogDO;
    }

    public static AuthAccountLogDO createAccountLog(String userId, String logName, String ip, Integer succeed,
            String message) {

        AuthAccountLogDO authAccountLogDO = new AuthAccountLogDO();
        authAccountLogDO.setUserId(userId);
        authAccountLogDO.setLogName(logName);
        authAccountLogDO.setIp(ip);
        authAccountLogDO.setSucceed(succeed);
        authAccountLogDO.setMessage(message);
        authAccountLogDO.setCreateTime(SystemUtils.getCurrentTimestamp());
        return authAccountLogDO;
    }
}
