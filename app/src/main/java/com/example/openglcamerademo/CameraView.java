package com.example.openglcamerademo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class CameraView extends GLSurfaceView {
    private CameraRender renderer;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 使用OpenGL ES2.0 context
        setEGLContextClientVersion(2);

        renderer = new CameraRender(this);
        setRenderer(renderer);

        /**
         * 刷新方式：
         * 1、 RENDERMODE_WHEN_DIRTY 手动刷新，需要调用requestRender
         * 2、 RENDERMODE_CONTINUOUSLY 自动刷新，大概16ms自动回调一次onDraw方法
         */
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        renderer.onSurfaceDestroyed();
    }
}
