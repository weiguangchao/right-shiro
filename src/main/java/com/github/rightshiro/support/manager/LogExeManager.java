package com.github.rightshiro.support.manager;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 日志执行管理器(单例)
 * @author weiguangchao
 * @date 2020/11/19
 */
public class LogExeManager {

    private static final int LOG_DELAY_TIME = 10;
    private static final ThreadFactory THREAD_FACTORY = Thread::new;
    private static final LogExeManager LOG_EXE_MANAGER = new LogExeManager();

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(20, THREAD_FACTORY);

    private LogExeManager() {}

    public void executeLogTask(TimerTask timerTask) {
        executor.schedule(timerTask, LOG_DELAY_TIME, TimeUnit.MICROSECONDS);
    }

    public static LogExeManager getInstance() {
        return LOG_EXE_MANAGER;
    }
}
