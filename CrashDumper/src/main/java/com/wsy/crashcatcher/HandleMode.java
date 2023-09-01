package com.wsy.crashcatcher;

/**
 * 捕获信号，记录日志后的处理方式
 */
public enum HandleMode {
    /**
     * 什么都不做
     */
    DO_NOTHING(0),
    /**
     * 将捕获到的SIGNAL重新抛出
     */
    RAISE_ERROR(1),
    /**
     * 通知Java端的Callback
     */
    NOTICE_CALLBACK(2);

    int mode;

    HandleMode(int mode) {
        this.mode = mode;
    }
}
