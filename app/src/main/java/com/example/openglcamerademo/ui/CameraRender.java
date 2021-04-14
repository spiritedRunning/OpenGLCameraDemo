package com.example.openglcamerademo.ui;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.widget.Toast;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.example.openglcamerademo.filter.CameraFilter;
import com.example.openglcamerademo.filter.ScreenFilter;
import com.example.openglcamerademo.record.MediaRecorder;
import com.example.openglcamerademo.utils.CameraHelper;

import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "CameraRender";

    private CameraView cameraView;
    private CameraHelper cameraHelper;

    private int[] textures;
    private ScreenFilter screenFilter;
    private CameraFilter cameraFilter;

    // 摄像头的图像， 用OpenGL 画出来
    private SurfaceTexture mCameraTexture;

    float[] matrix = new float[16];

    private MediaRecorder mRecorder;

    public CameraRender(CameraView cameraView) {
        this.cameraView = cameraView;
        LifecycleOwner lifecycleOwner = (LifecycleOwner) cameraView.getContext();
        cameraHelper = new CameraHelper(lifecycleOwner, this);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // 请求执行一次onDrawFrame
        cameraView.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        textures = new int[1];  //当做能在opengl用的一个图片ID
        mCameraTexture.attachToGLContext(textures[0]);
        mCameraTexture.setOnFrameAvailableListener(this);

        Context context = cameraView.getContext();
        cameraFilter = new CameraFilter(context);
        screenFilter = new ScreenFilter(context);

        File file = new File("/sdcard/opengl.mp4");
        if (!file.exists()) {
            try {
                boolean ret = file.createNewFile();
                if (!ret) {
                    Log.e(TAG, "create file failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mRecorder = new MediaRecorder(cameraView.getContext(), file.getAbsolutePath(), EGL14.eglGetCurrentContext(), 480, 640);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        cameraFilter.setSize(width, height);
        screenFilter.setSize(width, height);
    }

    public void onSurfaceDestroyed() {
        cameraFilter.release();
        screenFilter.release();
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        mCameraTexture.updateTexImage();
        mCameraTexture.getTransformMatrix(matrix);

        cameraFilter.setTransformMatrix(matrix);
        int id = cameraFilter.onDraw(textures[0]);
        screenFilter.onDraw(id);

        mRecorder.fireFrame(id, mCameraTexture.getTimestamp());
    }

    @Override
    public void onUpdated(Preview.PreviewOutput output) {
        mCameraTexture = output.getSurfaceTexture();
    }

    public void startRecord(float speed) {
        try {
            mRecorder.start(speed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        mRecorder.stop();
    }


}
