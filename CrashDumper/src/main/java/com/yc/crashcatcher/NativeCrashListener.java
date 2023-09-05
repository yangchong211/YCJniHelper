package com.yc.crashcatcher;

public interface NativeCrashListener {
    void onSignalReceived(int signal, String logPath);
}
