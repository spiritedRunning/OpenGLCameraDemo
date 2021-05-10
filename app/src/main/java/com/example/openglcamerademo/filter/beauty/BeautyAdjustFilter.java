package com.example.openglcamerademo.filter.beauty;

import android.content.Context;
import android.opengl.GLES20;

import com.example.openglcamerademo.R;
import com.example.openglcamerademo.filter.AbstractFBOFilter;
import com.example.openglcamerademo.filter.FilterContext;

/**
 * Created by Zach on 2021/5/10 11:20
 */
public class BeautyAdjustFilter extends AbstractFBOFilter {

    private int level;
    private int vBlurTexture;
    private int vHighpassBlurTexture;

    private int blurTexture;
    private int highpassBlurTexture;

    public BeautyAdjustFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.beauty_adjust);
    }

    public BeautyAdjustFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);

        level = GLES20.glGetUniformLocation(program, "level");
        vBlurTexture = GLES20.glGetUniformLocation(program, "blurTexture");
        vHighpassBlurTexture = GLES20.glGetUniformLocation(program, "highpassBlurTexture");
    }

    @Override
    public void beforeDraw(FilterContext filterContext) {
        super.beforeDraw(filterContext);

        GLES20.glUniform1f(level, filterContext.beautyLevel);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, blurTexture);
        GLES20.glUniform1i(vBlurTexture, 1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, highpassBlurTexture);
        GLES20.glUniform1i(vHighpassBlurTexture, 2);
    }

    public void setBlurTexture(int blurTexture) {
        this.blurTexture = blurTexture;
    }

    public void setHighpassBlurTexture(int highpassBlurTexture) {
        this.highpassBlurTexture = highpassBlurTexture;
    }
}
