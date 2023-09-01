package com.wsy.crashcatcher;

public interface NativeCrashListener {
    void onSignalReceived(int signal, String logPath);
}
