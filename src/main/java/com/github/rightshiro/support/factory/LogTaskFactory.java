package com.github.rightshiro.support.factory;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.rightshiro.model.entity.AuthAccountLogDO;
import com.github.rightshiro.model.entity.AuthOperationLogDO;
import com.github.rightshiro.repository.AuthAccountLogRepository;
import com.github.rightshiro.repository.AuthOperationLogRepository;

/**
 * @author weiguangchao
 * @date 2020/11/19
 */
@Component
public class LogTaskFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTaskFactory.class);

    private AuthAccountLogRepository authAccountLogRepository;
    private AuthOperationLogRepository authOperationLogRepository;

    public LogTaskFactory(AuthAccountLogRepository authAccountLogRepository,
            AuthOperationLogRepository authOperationLogRepository) {
        this.authAccountLogRepository = authAccountLogRepository;
        this.authOperationLogRepository = authOperationLogRepository;
    }

    public TimerTask exitLog(String userId, String ip, Integer succeed, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AuthAccountLogDO authAccountLogDO = LogFactory.createAccountLog(userId, "用户退出日志", ip, succeed,
                            message);
                    authAccountLogRepository.save(authAccountLogDO);
                }
                catch (Exception ex) {
                    LOGGER.error("写入用户退出日志异常::" + ex.getMessage(), ex);
                }
            }
        };
    }

    public TimerTask registerLog(String userId, String ip, Integer succeed, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AuthAccountLogDO accountLogDO = LogFactory.createAccountLog(userId, "用户注册日志", ip, succeed, message);
                    authAccountLogRepository.save(accountLogDO);
                }
                catch (Exception ex) {
                    LOGGER.error("写入用户注册日志异常::" + ex.getMessage(), ex);
                }
            }
        };
    }

    public TimerTask loginLog(String userId, String ip, Integer succeed, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AuthAccountLogDO authAccountLogDO = LogFactory.createAccountLog(
                            userId,
                            "用户登录日志",
                            ip,
                            succeed,
                            message
                    );
                    authAccountLogRepository.save(authAccountLogDO);
                }
                catch (Exception e) {
                    LOGGER.error("写入用户登录日志异常::" + e.getMessage(), e);
                }
            }
        };
    }

    public TimerTask bussinssLog(String userId, String api, String method, Integer succeed, String message) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    AuthOperationLogDO authOperationLogDO = LogFactory.createOperationLog(
                            userId,
                            "业务操作日志",
                            api,
                            method,
                            succeed,
                            message
                    );
                    authOperationLogRepository.save(authOperationLogDO);
                }
                catch (Exception ex) {
                    LOGGER.error("写入业务操作日志异常::" + ex.getMessage(), ex);
                }
            }
        };
    }
}
