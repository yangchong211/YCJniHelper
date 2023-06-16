package com.yc.jnidemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.calljni.CallNativeLib;
import com.yc.safetyjni.SafetyJniLib;
import com.yc.signalhooker.ILogger;
import com.yc.signalhooker.ISignalListener;
import com.yc.signalhooker.SigQuitHooker;
import com.yc.testjnilib.NativeLib;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //initSignal();
    }

    private void initSignal() {
        SigQuitHooker.initSignalHooker();
        SigQuitHooker.setSignalListener(new ISignalListener() {
            @Override
            public void onReceiveAnrSignal() {
                Log.d("SigQuitHooker: " , "onReceiveAnrSignal: do" );
            }
        });
        SigQuitHooker.setLogger(new ILogger() {
            @Override
            public void onPrintLog(String message) {
                Log.d("SigQuitHooker: " , "message: " + message);
            }
        });
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
                String stringFromJNI = SafetyJniLib.getInstance().stringFromJNI();
                String nameFromJNI = NativeLib.getInstance().getNameFromJNI();
                tv.setText("" + nameFromJNI);
                break;
            case R.id.tv_2:
                CallNativeLib.getInstance().callJavaField("com/yc/calljni/HelloCallBack","name");
                CallNativeLib.getInstance().callJavaMethod("com/yc/calljni/HelloCallBack","updateName");
                break;
            case R.id.tv_3:

                break;
            default:
                break;
        }
    }
}
