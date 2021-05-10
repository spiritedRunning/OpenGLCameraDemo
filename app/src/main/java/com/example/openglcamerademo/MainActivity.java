package com.example.openglcamerademo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.openglcamerademo.ui.CameraView;
import com.example.openglcamerademo.ui.RecordButton;

public class MainActivity extends AppCompatActivity implements RecordButton.OnRecordListener, RadioGroup.OnCheckedChangeListener {

    private CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

        }, 100);

        cameraView = findViewById(R.id.cameraView);
        RecordButton btn_record = findViewById(R.id.btn_record);
        btn_record.setOnRecordListener(this);

        SeekBar sb = findViewById(R.id.seek_bar);
        sb.setMax(100);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    cameraView.setBeauty(progress * 1.0f / seekBar.getMax());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_extra_slow:
                cameraView.setSpeed(CameraView.Speed.MODE_EXTRA_SLOW);
                break;
            case R.id.btn_slow:
                cameraView.setSpeed(CameraView.Speed.MODE_SLOW);
                break;
            case R.id.btn_normal:
                cameraView.setSpeed(CameraView.Speed.MODE_NORMAL);
                break;
            case R.id.btn_fast:
                cameraView.setSpeed(CameraView.Speed.MODE_FAST);
                break;
            case R.id.btn_extra_fast:
                cameraView.setSpeed(CameraView.Speed.MODE_EXTRA_FAST);
                break;
        }
    }

    @Override
    public void onRecordStart() {
        cameraView.startRecord();
        Toast.makeText(this, "录制开始", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordStop() {
        cameraView.stopRecord();
        Toast.makeText(this, "录制结束", Toast.LENGTH_SHORT).show();
    }
}