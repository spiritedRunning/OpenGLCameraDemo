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
    public int onDraw(int texture, FilterChain filterChain) {
        matrix = filterChain.getFilterContext().cameraMatrix;
        return super.onDraw(texture, filterChain);
    }

    @Override
    public void beforeDraw(FilterContext filterContext) {
        super.beforeDraw(filterContext);
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, matrix, 0);
    }


}
