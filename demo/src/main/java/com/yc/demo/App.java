package com.yc.demo;

import android.app.Application;

import com.yc.testjnilib.NativeLib;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NativeLib.getInstance().init(this);
    }
}
