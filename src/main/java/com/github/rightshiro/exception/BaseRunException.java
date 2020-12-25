package com.github.rightshiro.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认会将异常抛出.
 *
 * @author weiguangchao
 * @date 2020/11/25
 */
public class BaseRunException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRunException.class);

    public BaseRunException(String message) {
        super(message);
    }

    public BaseRunException(String message, Throwable cause) {
        super(message, cause);
    }

    public void process() {
        this.process(true);
    }

    public void process(boolean isError) {
        if (isError) {
            LOGGER.error(this.getMessage(), this.getCause());
        }
        else {
            LOGGER.warn(this.getMessage(), this.getCause());
        }

        this.doProcess();
    }

    /**
     * 子类重写
     */
    protected void doProcess() {
        throw this;
    }
}
