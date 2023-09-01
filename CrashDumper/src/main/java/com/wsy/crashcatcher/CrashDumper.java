package com.wsy.crashcatcher;



import java.io.File;

public class CrashDumper {
    static {
        System.loadLibrary("crash_dumper");
    }

    private static native void nativeInit(String crashDumpDir, NativeCrashListener nativeCrashListener, int handleMode);

    public static boolean init(String crashDumpDir,
                               NativeCrashListener nativeCrashListener, HandleMode handleMode) {
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
