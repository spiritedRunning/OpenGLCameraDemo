package com.example.openglcamerademo.filter;

import com.example.openglcamerademo.face.Face;

public class FilterContext {

    public Face face;

    public float[] cameraMatrix;  // 摄像头转换矩阵

    public int width;
    public int height;

    public float beautyLevel;

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setTransformMatrix(float[] matrix) {
        this.cameraMatrix = matrix;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public void setBeautyLevel(float level) {
        this.beautyLevel = level;
    }
}
