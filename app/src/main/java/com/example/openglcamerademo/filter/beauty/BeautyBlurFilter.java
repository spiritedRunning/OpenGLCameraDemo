package com.example.openglcamerademo.filter.beauty;

import android.content.Context;
import android.opengl.GLES20;

import com.example.openglcamerademo.R;
import com.example.openglcamerademo.filter.AbstractFBOFilter;
import com.example.openglcamerademo.filter.FilterContext;

/**
 * Created by Zach on 2021/5/10 9:48
 */
public class BeautyBlurFilter extends AbstractFBOFilter {

    private int textureWidthOffset;
    private int textureHeightOffset;
    private float mTextureWidth, mTextureHeight;


    public BeautyBlurFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.beauty_blur);
    }

    @Override
    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        super.initGL(context, vertexShaderId, fragmentShaderId);

        textureWidthOffset = GLES20.glGetUniformLocation(program, "textureWidthOffset");
        textureHeightOffset = GLES20.glGetUniformLocation(program, "textureHeightOffset");
    }

    @Override
    public void beforeDraw(FilterContext filterContext) {
        super.beforeDraw(filterContext);
        GLES20.glUniform1f(textureWidthOffset, mTextureWidth);
        GLES20.glUniform1f(textureHeightOffset, mTextureHeight);
    }

    public void setTextureOffset(float width, float height) {
        mTextureWidth = width;
        mTextureHeight = height;

        if (mTextureWidth != 0) {
            mTextureWidth = 1.0f / mTextureWidth;
        } else {
            mTextureWidth = 0;
        }

        if (mTextureHeight != 0) {
            mTextureHeight = 1.0f / mTextureHeight;
        } else {
            mTextureHeight = 0;
        }
    }
}
