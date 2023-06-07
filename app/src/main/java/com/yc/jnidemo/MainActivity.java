package com.yc.jnidemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.jnidemo.first.FirstActivity;
import com.yc.jnidemo.second.SecondActivity;
import com.yc.jnidemo.three.ShadowViewDemo3Activity;
import com.yc.testjnilib.NativeLib;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        tv = findViewById(R.id.tv_4);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                String fromJNI = NativeLib.getInstance().stringFromJNI();
                String md5 = NativeLib.getInstance().getMd5("yc");
                NativeLib.getInstance().initLib("db");
                tv.setText("" + md5);
                break;
            case R.id.tv_2:

                break;
            case R.id.tv_3:

                break;
            default:
                break;
        }
    }
}
