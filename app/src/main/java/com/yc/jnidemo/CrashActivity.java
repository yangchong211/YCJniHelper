package com.yc.jnidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wsy.crashcatcher.CrashDumper;
import com.wsy.crashcatcher.HandleMode;
import com.wsy.crashcatcher.NativeCrashListener;
import com.yc.jnidemo.R;

import java.io.FileInputStream;
import java.io.IOException;

public class CrashActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    static {
        System.loadLibrary("test_crash");
    }

    private static final String TAG = "MainActivity : ";

    public native void nativeCrash();

    private RadioGroup radioGroup;
    private TextView tvCrashLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        initView();
    }

    private void initView() {
        tvCrashLog = findViewById(R.id.tv_crash_log);
        radioGroup = findViewById(R.id.rg_handle_mode);
        radioGroup.setOnCheckedChangeListener(this);
        findViewById(R.id.btn_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crash(v);
            }
        });
    }

    public void crash(View view) {
        nativeCrash();
    }


    private String readContentFromFile(String path) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        HandleMode handleMode;
        switch (checkedId){
            case R.id.rb_notice_callback:
                handleMode = HandleMode.NOTICE_CALLBACK;
                break;
            case R.id.rb_raise_error:
                handleMode = HandleMode.RAISE_ERROR;
                break;
            case R.id.rb_do_nothing:
            default:
                handleMode = HandleMode.DO_NOTHING;
                break;
        }

        CrashDumper.init(getFilesDir().getAbsolutePath(), new NativeCrashListener() {
            @Override
            public void onSignalReceived(int signal, final String logPath) {
                final String content = readContentFromFile(logPath);
                Log.i(TAG, "onSignalReceived: " + content);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCrashLog.setText(content);
                    }
                });
            }
        }, handleMode);
    }
}
