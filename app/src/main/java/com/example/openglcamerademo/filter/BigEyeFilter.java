package com.example.openglcamerademo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.example.openglcamerademo.R;
import com.example.openglcamerademo.face.Face;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class BigEyeFilter extends AbstractFBOFilter {
    private FloatBuffer left, right;
    private int left_eye, right_eye;
    private Face face;


    public BigEyeFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.bigeye_frag);

        left = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder()).asFloatBuffer();
        right = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    @Override
    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        super.initGL(context, vertexShaderId, fragmentShaderId);

        left_eye = GLES20.glGetUniformLocation(program, "left_eye");
        right_eye = GLES20.glGetUniformLocation(program, "right_eye");
    }

    @Override
    public int onDraw(int texture, FilterChain filterChain) {
        FilterContext filterContext = filterChain.getFilterContext();
        face = filterContext.face;

        return super.onDraw(texture, filterChain);
    }

    @Override
    public void beforeDraw() {
        super.beforeDraw();

        if (face == null) {
            return;
        }

        // left_x, left_y 的坐标原点为图片左上角， 需要转换为opengl的NDC坐标， 是以左下角为原点
        float x = face.left_x / face.imgWidth;
        float y = 1.0f - face.left_y / face.imgHeight;

        left.clear();
        left.put(x).put(y).position(0);

        // 将buffer传给着色器left_eye
        // param2:  1个点
        GLES20.glUniform2fv(left_eye, 1, left);

        x = face.right_x / face.imgWidth;
        y = 1.0f - face.right_y / face.imgHeight;

        right.clear();
        right.put(x).put(y).position(0);

        GLES20.glUniform2fv(right_eye, 1, right);
    }
}

