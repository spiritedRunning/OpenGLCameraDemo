package com.example.openglcamerademo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.example.openglcamerademo.R;

/**
 * Created by Zach on 2021/5/10 14:30
 */
public class SoulFilter extends AbstractFBOFilter {
    private int mixturePercent;
    private int scalePercent;

    public SoulFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.soul_frag);
    }

    @Override
    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        super.initGL(context, vertexShaderId, fragmentShaderId);

        mixturePercent = GLES20.glGetUniformLocation(program, "mixturePercent");
        scalePercent = GLES20.glGetUniformLocation(program, "scalePercent");
    }

    private float mix = 0.0f; // 透明度， 越大越透明
    private float scale = 0.0f;  // 缩放系数，值越大放的越大

    @Override
    public void beforeDraw(FilterContext filterContext) {
        super.beforeDraw(filterContext);

        GLES20.glUniform1f(mixturePercent, 1.0f - mix);
        GLES20.glUniform1f(scalePercent, scale + 1.0f);

        mix += 0.08f;
        scale += 0.08f;
        if (mix >= 1.0) {
            mix = 0.0f;
        }
        if (scale >= 1.0f) {
            scale = 0.0f;
        }
    }
}
