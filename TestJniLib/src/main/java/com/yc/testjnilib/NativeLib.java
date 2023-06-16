package com.yc.testjnilib;

public class NativeLib {

    private static NativeLib instance;

    // Used to load the 'testjnilib' library on application startup.
    static {
        System.loadLibrary("testjnilib");
    }

    public static NativeLib getInstance() {
        if (instance == null) {
            synchronized (NativeLib.class) {
                if (instance == null) {
                    instance = new NativeLib();
                }
            }
        }
        return instance;
    }

    /**
     * java调用native代码，java调用c/c++
     */
    public native String stringFromJNI();
    public native String getNameFromJNI();
    public native String getMd5(String str);
    public native void initLib(String version);

    public native String callThirdSoMethod();
}