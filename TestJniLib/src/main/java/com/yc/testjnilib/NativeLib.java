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
     * A native method that is implemented by the 'testjnilib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String getMd5(String str);

    public native void initLib(String version);

}