package com.yc.mooner;

public class MoonerHelper {

    private static MoonerHelper instance;

    static {
        System.loadLibrary("mooner_core");
    }

    public static MoonerHelper getInstance() {
        if (instance == null) {
            synchronized (MoonerHelper.class) {
                if (instance == null) {
                    instance = new MoonerHelper();
                }
            }
        }
        return instance;
    }

    private MoonerHelper(){

    }

}
