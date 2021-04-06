package com.example.openglcamerademo.filter;

import android.content.Context;

import com.example.openglcamerademo.utils.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ScreenFilter {
    private final int vPosition;
    private final int vCoord;
    private final int vTexture;
    private final int vMatrix;

    private int program;
    private FloatBuffer vertexBuffer;  // 顶点坐标缓冲区
    private FloatBuffer textureBuffer;  // 纹理坐标

    private int mWidth;
    private int mHeight;
    private float[] matrix;


    public ScreenFilter(Context context) {
        // 4个坐标 * float * x,y 轴坐标
        vertexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(OpenGLUtils.VERTEX);

        textureBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer.clear();
        textureBuffer.put(OpenGLUtils.TEXTURE);

        String vertexShader = OpenGLUtils.readRawTextFile(context, R.raw.camera_vert);
    }

    public void setSize(int width, int height) {
    }

    public void setTransformMatrix(float[] matrix) {
    }

    public void onDraw(int texture) {
    }
}
