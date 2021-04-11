package com.example.openglcamerademo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.example.openglcamerademo.filter.CameraFilter;
import com.example.openglcamerademo.filter.ScreenFilter;
import com.example.openglcamerademo.record.MediaRecorder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private CameraView cameraView;

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

        mRecorder = new MediaRecorder(cameraView.getContext(), "/sdcard/opengl.mp4", EGL14.eglGetCurrentContext(), 480, 640);
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

    }

    @Override
    public void onUpdated(Preview.PreviewOutput output) {
        mCameraTexture = output.getSurfaceTexture();
    }


}
