package com.example.openglcamerademo.utils;

import android.content.Context;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OpenGLUtils {

    private static final String TAG = "OpenGLUtils";

    public static final float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };

    public static final float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

    public static final float[] TEXTURE_180 = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };


    public static String readRawTextFile(Context context, int rawId) {
        InputStream is = context.getResources().openRawResource(rawId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static int loadProgram(String vSource, String fSource) {
        /**
         * 顶点着色器
         */
        int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vShader, vSource);  // 加载着色器代码
        GLES20.glCompileShader(vShader);  // 编译

        int[] status = new int[1];
        GLES20.glGetShaderiv(vShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("load vertex shader: " + GLES20.glGetShaderInfoLog(vShader));
        }

        /**
         * 片元着色器
         */
        int fShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fShader, fSource);
        GLES20.glCompileShader(fShader);

        GLES20.glGetShaderiv(fShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("load program shader: " + GLES20.glGetShaderInfoLog(fShader));
        }


        /**
         * 创建着色器程序
         */
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        GLES20.glLinkProgram(program);

        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("link program: " + GLES20.glGetProgramInfoLog(program));
        }

        GLES20.glDeleteShader(vShader);
        GLES20.glDeleteShader(fShader);
        return program;
    }

    public static void glGenTextures(int[] textures) {
        GLES20.glGenTextures(textures.length, textures, 0);

        for (int i = 0; i < textures.length; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
        }

        /**
         * 必须：设置纹理过滤参数
         * 设置纹理缩放过滤
         */
         // GL_NEAREST: 使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
         // GL_LINEAR: 使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);  // 放大过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);  // 缩小过滤

        /**
         * 可选： 设置纹理环绕方向
         * 纹理坐标的范围是 0-1， 超出这一范围的坐标将被OpenGL 根据GL_TEXTURE_WRAP参数的值进行处理
         */
        // GL_TEXTURE_WRAP_S, GL_TEXTURE_WRAP_T 分别为x, y方向
        // GL_REPEAT: 平铺
        // GL_MIRRORED_REPEAT: 纹理坐标是奇数时使用镜像平铺
        // GL_CLAMP_TO_EDGE: 坐标超出部分被截取成0,1  边缘拉伸
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }


    public static void copyAssets2Sdcard(Context context, String src, String dst) {
        try {
            File file = new File(dst);
            if (!file.exists()) {
                InputStream is = context.getAssets().open(src);
                FileOutputStream fos = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[2048];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }

                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
