package com.yc.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.testjnilib.NativeLib;

public class DemoActivity extends AppCompatActivity {

    private TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);
        tv = findViewById(R.id.tv_content);
        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String fromJNI = NativeLib.getInstance().stringFromJNI();
                String md5 = NativeLib.getInstance().getMd5("yc");
                NativeLib.getInstance().initLib("db");
                tv.setText("" + md5);
            }
        });
    }



}
