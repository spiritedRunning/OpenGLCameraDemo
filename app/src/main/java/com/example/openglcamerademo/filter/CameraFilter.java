package com.example.openglcamerademo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.example.openglcamerademo.R;


public class CameraFilter extends AbstractFBOFilter  {

    private float[] matrix;
    private int vMatrix;


    public CameraFilter(Context context) {
        super(context, R.raw.camera_vert, R.raw.camera_frag);
    }


    @Override
    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        super.initGL(context, vertexShaderId, fragmentShaderId);
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    @Override
    public void beforeDraw() {
        super.beforeDraw();
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, matrix, 0);
    }

    public void setTransformMatrix(float[] matrix) {
        this.matrix = matrix;
    }
}
