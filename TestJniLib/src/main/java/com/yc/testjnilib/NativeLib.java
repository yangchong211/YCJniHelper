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
    public native String getMd5(String str);
    public native void initLib(String version);

    /**
     * native调用java代码，c/c++调用java
     */
    public native void callJavaField(String className,String fieldName) ;
    public native boolean callJavaMethod(String className,String methodName) ;

}