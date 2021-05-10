package com.example.openglcamerademo.filter.beauty;

import android.content.Context;
import android.opengl.GLES20;

import com.example.openglcamerademo.R;
import com.example.openglcamerademo.filter.AbstractFBOFilter;
import com.example.openglcamerademo.filter.FilterContext;

/**
 * Created by Zach on 2021/5/10 10:17
 */
public class BeautyHighpassFilter extends AbstractFBOFilter {

    private int vBlurTexture;
    private int blurTexture;

    public BeautyHighpassFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.beauty_highpass);
    }

    @Override
    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        super.initGL(context, vertexShaderId, fragmentShaderId);
        vBlurTexture = GLES20.glGetUniformLocation(program, "vBlurTexture");
    }

    @Override
    public void beforeDraw(FilterContext filterContext) {
        super.beforeDraw(filterContext);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, blurTexture);
        GLES20.glUniform1i(vBlurTexture, 1);
    }

    public void setBlurTexture(int blurTexture) {
        this.blurTexture = blurTexture;
    }
}
