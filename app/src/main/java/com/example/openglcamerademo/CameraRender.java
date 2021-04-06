package com.example.openglcamerademo;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.example.openglcamerademo.filter.ScreenFilter;
import com.example.openglcamerademo.utils.CameraHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private CameraView cameraView;
    private CameraHelper cameraHelper;

    private int[] textures;
    private ScreenFilter screenFilter;
    // 摄像头的图像， 用OpenGL 画出来
    private SurfaceTexture mCameraTexture;

    float[] matrix = new float[16];

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

        screenFilter = new ScreenFilter(cameraView.getContext());
    }

    public void onSurfaceDestroyed() {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenFilter.setSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mCameraTexture.updateTexImage();
        mCameraTexture.getTransformMatrix(matrix);

        screenFilter.setTransformMatrix(matrix);
        screenFilter.onDraw(textures[0]);
    }

    @Override
    public void onUpdated(Preview.PreviewOutput output) {
        mCameraTexture = output.getSurfaceTexture();
    }


}
