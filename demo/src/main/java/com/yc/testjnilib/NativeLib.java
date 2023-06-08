package com.yc.testjnilib;

import android.content.Context;

import com.getkeepsafe.relinker.ReLinker;

public class NativeLib {

    private static NativeLib instance;

    static {
        //System.loadLibrary("testjnilib");
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

    public void init(Context context){
        //ReLinker.loadLibrary(context,"testjnilib");
        ReLinker.loadLibrary(context, "testjnilib", new ReLinker.LoadListener() {
            @Override
            public void success() {

            }

            @Override
            public void failure(Throwable t) {

            }
        });
    }

    /**
     * java调用native代码，java调用c/c++
     */
    public native String stringFromJNI();
    public native String getMd5(String str);
    public native void initLib(String version);
}