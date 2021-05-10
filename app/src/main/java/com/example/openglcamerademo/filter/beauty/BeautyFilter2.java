package com.example.openglcamerademo.filter.beauty;

import android.content.Context;
import android.opengl.GLES20;

import com.example.openglcamerademo.R;
import com.example.openglcamerademo.filter.AbstractFBOFilter;
import com.example.openglcamerademo.filter.FilterContext;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Zach on 2021/5/10 13:30
 */
public class BeautyFilter2 extends AbstractFBOFilter {
    private FloatBuffer singleStepOffsetBuffer;

    private int beautyLevel;
    private int singleStepOffset;

    public BeautyFilter2(Context context) {
        super(context, R.raw.base_vert, R.raw.beauty_grinding);

        singleStepOffsetBuffer = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    @Override
    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        super.initGL(context, vertexShaderId, fragmentShaderId);

        beautyLevel = GLES20.glGetUniformLocation(program, "beautyLevel");
        singleStepOffset = GLES20.glGetUniformLocation(program, "singleStepOffset");
    }

    @Override
    public void afterDraw(FilterContext filterContext) {
        super.afterDraw(filterContext);

        GLES20.glUniform1f(beautyLevel, filterContext.beautyLevel);

        singleStepOffsetBuffer.clear();
        singleStepOffsetBuffer.put(2.0f / filterContext.width);
        singleStepOffsetBuffer.put(2.0f / filterContext.height);
        singleStepOffsetBuffer.position(0);

        GLES20.glUniform2fv(singleStepOffset, 1, singleStepOffsetBuffer);
    }
}
