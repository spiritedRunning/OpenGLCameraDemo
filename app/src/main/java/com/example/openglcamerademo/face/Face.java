package com.example.openglcamerademo.face;

public class Face {
    public int width;
    public int height;
    public int imgWidth;
    public int imgHeight;
    public int x;
    public int y;
    // 眼睛
    public float left_x;
    public float left_y;
    public float right_x;
    public float right_y;

    //鼻子
    public float nose_x;
    public float nose_y;

    //左嘴角
    public float mouseLeft_x;
    public float mouseLeft_y;

    //右嘴角
    public float mouseRight_x;
    public float mouseRight_y;


    public Face(int width, int height, int imgWidth, int imgHeight, int x, int y, float left_x, float left_y,
                float right_x, float right_y, float nose_x, float nose_y, float mouseLeft_x, float mouseLeft_y,
                float mouseRight_x, float mouseRight_y) {
        this.width = width;
        this.height = height;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.x = x;
        this.y = y;
        this.left_x = left_x;
        this.left_y = left_y;
        this.right_x = right_x;
        this.right_y = right_y;
        this.nose_x = nose_x;
        this.nose_y = nose_y;
        this.mouseLeft_x = mouseLeft_x;
        this.mouseLeft_y = mouseLeft_y;
        this.mouseRight_x = mouseRight_x;
        this.mouseRight_y = mouseRight_y;
    }

    @Override
    public String toString() {
        return "Face{" +
                "width=" + width +
                ", height=" + height +
                ", imgWidth=" + imgWidth +
                ", imgHeight=" + imgHeight +
                ", x=" + x +
                ", y=" + y +
                ", left_x=" + left_x +
                ", left_y=" + left_y +
                ", right_x=" + right_x +
                ", right_y=" + right_y +
                ", nose_x=" + nose_x +
                ", nose_y=" + nose_y +
                ", mouseLeft_x=" + mouseLeft_x +
                ", mouseLeft_y=" + mouseLeft_y +
                ", mouseRight_x=" + mouseRight_x +
                ", mouseRight_y=" + mouseRight_y +
                '}';
    }
}
