package com.yc.library;

import android.view.View;

import androidx.core.view.ViewCompat;

/**
 * 不用改动代码情况下设置阴影
 */
public final class ShadowTool {

    public static void setShadowBgForView(View view, ShadowConfig config) {
        if (view==null || config==null){
            return;
        }
        //关闭硬件加速
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, config.builder());
    }
}
