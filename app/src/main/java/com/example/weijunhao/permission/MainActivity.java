package com.example.weijunhao.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button record;
    private boolean granted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();
        initView();
        initListener();
    }

    private void initListener() {
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                granted = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                        .RECORD_AUDIO) == 0 ? true : false;
                Log.i("wjh", ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                        .RECORD_AUDIO) == 0 ? "已授权" : "未授权");
                if (granted) {
                    recode();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.RECORD_AUDIO)) {
                        Log.i("wjh", "true");
                        //Toast.makeText(MainActivity.this, "你还没有录音权限", Toast.LENGTH_SHORT).show();
                        explainDialog();
                    } else {
                        Log.i("wjh", "未授权，不再提醒");
                        Toast.makeText(MainActivity.this, "你需要进入设置页面设置权限", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void recode() {
        //录音代码
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(getCacheDir().getPath() + "1.mp3");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initView() {
        record = (Button) findViewById(R.id.button);
    }

    private void initPermission() {
        int permission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //需不需要解释的dialog
            //if (shouldRequest()) return;
            //请求权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private boolean shouldRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                .RECORD_AUDIO)) {
            explainDialog();
        }
        return false;
    }

    private void explainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("应用需要获取您的录音权限，是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recode();
            }
            Log.i("wjh", grantResults[0] + "");
        }
    }
}
