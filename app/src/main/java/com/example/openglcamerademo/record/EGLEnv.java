package com.example.openglcamerademo.record;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;

import com.example.openglcamerademo.filter.FilterChain;
import com.example.openglcamerademo.filter.FilterContext;
import com.example.openglcamerademo.filter.RecordFilter;

import java.util.ArrayList;

public class EGLEnv {
    private final EGLConfig mEglConfig;
    private final EGLContext mEglContext;
    private final EGLSurface mEglSurface;
    private EGLDisplay mEglDisplay;

    private final FilterChain filterChain;

    private final RecordFilter recordFilter;


    public EGLEnv(Context context, EGLContext glContext, Surface surface, int width, int height) {
        // 获得显示窗口，作为OpenGL的绘制目标
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }

        // 初始化显示窗口
        int[] version = new int[2];
        if (!EGL14.eglInitialize(mEglDisplay, version, 0, version, 1)) {
            throw new RuntimeException("eglInitialize failed");
        }

        // 配置属性选项
        int[] configAttribs = {
                EGL14.EGL_RED_SIZE, 8,   // 颜色缓冲区中红色位数
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT, // opengl es 2.0
                EGL14.EGL_NONE
        };
        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        // EGL根据属性选择一个配置
        if (!EGL14.eglChooseConfig(mEglDisplay, configAttribs, 0, configs, 0, configs.length, numConfigs, 0)) {
            throw new RuntimeException("EGL ERROR: " + EGL14.eglGetError());
        }
        mEglConfig = configs[0];

        int[] context_attrib_list = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };
        // EGL上下文。与GLSurfaceView中的EGLContext共享数据，拿到处理完后显示的图像纹理
        mEglContext = EGL14.eglCreateContext(mEglDisplay, mEglConfig, glContext, context_attrib_list, 0);
        if (mEglContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("EGL error: " + EGL14.eglGetError());
        }


        /**
         * 创建EGLSurface
         */
        int[] surface_attrib_list = {
                EGL14.EGL_NONE
        };
        mEglSurface = EGL14.eglCreateWindowSurface(mEglDisplay, mEglConfig, surface, surface_attrib_list, 0);
        if (mEglSurface == null) {
            throw new RuntimeException("EGL error: " + EGL14.eglGetError());
        }

        // 绑定当前线程的display
        if (!EGL14.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
            throw new RuntimeException("EGL error: " + EGL14.eglGetError());
        }

        recordFilter = new RecordFilter(context);
        FilterContext filterContext = new FilterContext();
        filterContext.setSize(width, height);
        filterChain = new FilterChain(new ArrayList<>(), 0,filterContext);
    }

    public void draw(int textureId, long timeStamp) {
        recordFilter.onDraw(textureId, filterChain);
        EGLExt.eglPresentationTimeANDROID(mEglDisplay, mEglSurface, timeStamp);

        // EGLSurface是双缓冲模式
        EGL14.eglSwapBuffers(mEglDisplay, mEglSurface);
    }

    public void release() {
        EGL14.eglDestroySurface(mEglDisplay, mEglSurface);
        EGL14.eglMakeCurrent(mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(mEglDisplay, mEglContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(mEglDisplay);

        recordFilter.release();
    }
}
