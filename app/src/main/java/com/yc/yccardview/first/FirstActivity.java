package com.yc.yccardview.first;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.library.ShadowConfig;
import com.yc.library.ShadowTool;
import com.yc.yccardview.R;
/**
 * Created by PC on 2017/10/20.
 * 作者：PC
 */

public class FirstActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private Button btn3;
    private LinearLayout ll4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        init();
    }

    private void init() {
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        btn3 = findViewById(R.id.btn_3);
        ll4 = findViewById(R.id.ll_4);


        ShadowConfig config1 = new ShadowConfig();
        config1.setColor(this.getResources().getColor(R.color.default_fill_color))
                .setShadowRadius(5)
                .setShadowColor(this.getResources().getColor(R.color.colorPrimary))
                .setRadius(10)
                .setOffsetX(1)
                .setOffsetY(0)
                .builder();
        ShadowTool.setShadowBgForView(tv1,config1);

        ShadowConfig config4 = new ShadowConfig();
        config4.setColor(this.getResources().getColor(R.color.default_fill_color))
                .setShadowRadius(5)
                .setShadowColor(this.getResources().getColor(R.color.colorPrimary))
                .setRadius(10)
                .setOffsetX(1)
                .setOffsetY(0)
                .builder();
        ShadowTool.setShadowBgForView(ll4,config4);
    }

}
