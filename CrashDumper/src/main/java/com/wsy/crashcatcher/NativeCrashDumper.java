package com.wsy.crashcatcher;



import java.io.File;

public class NativeCrashDumper {

    private static NativeCrashDumper instance;

    static {
        //java.lang.UnsatisfiedLinkError: dalvik.system.PathClassLoader[DexPathList
        //couldn't find "libcrash_dumper.so"
        System.loadLibrary("crash_dumper");
    }

    public static NativeCrashDumper getInstance() {
        if (instance == null) {
            synchronized (NativeCrashDumper.class) {
                if (instance == null) {
                    instance = new NativeCrashDumper();
                }
            }
        }
        return instance;
    }

    private native void nativeInit(String crashDumpDir,
                                   NativeCrashListener nativeCrashListener, int handleMode);

    public boolean init(String crashDumpDir,
                        NativeCrashListener nativeCrashListener, NativeHandleMode handleMode) {
        if (crashDumpDir == null) {
            return false;
        }
        File dir = new File(crashDumpDir);
        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            nativeInit(crashDumpDir, nativeCrashListener, handleMode.mode);
            return true;
        }
        return false;
    }
}
