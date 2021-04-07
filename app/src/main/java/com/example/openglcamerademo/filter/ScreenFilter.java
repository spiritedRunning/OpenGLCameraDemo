package com.example.openglcamerademo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.example.openglcamerademo.R;
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
        String fragShader = OpenGLUtils.readRawTextFile(context, R.raw.camera_frag);
        program = OpenGLUtils.loadProgram(vertexShader, fragShader);

        vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        vCoord = GLES20.glGetAttribLocation(program, "vCoord");
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void setTransformMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    public void onDraw(int texture) {
        // 设置绘制区域
        GLES20.glViewport(0, 0, mWidth, mHeight);

        GLES20.glUseProgram(program);
        vertexBuffer.position(0);
        // normalized 是否归一化， 将坐标转化为[-1,1] 之间
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        // CPU传数据到GPU, 默认情况下着色器无法读取到这个数据，需要启用一下
        GLES20.glEnableVertexAttribArray(vPosition);

        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        // 激活一个用来显示图片的画框
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        // 0: 图层ID, 对应上面的GL_TEXTURE0
        GLES20.glUniform1i(vTexture, 0);

        GLES20.glUniformMatrix4fv(vMatrix, 1, false, matrix, 0);

        // 通知画画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
}
