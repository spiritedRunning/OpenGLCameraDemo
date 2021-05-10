package com.example.openglcamerademo.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.openglcamerademo.R;
import com.example.openglcamerademo.face.Face;
import com.example.openglcamerademo.utils.OpenGLUtils;

public class StickFilter extends AbstractFBOFilter {
    private Bitmap nose;
    private int[] textures;


    public StickFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);

        textures = new int[1];
        OpenGLUtils.glGenTextures(textures);

        // 把图片加载到纹理中
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        nose = BitmapFactory.decodeResource(context.getResources(), R.mipmap.nose);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, nose, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public int onDraw(int texture, FilterChain filterChain) {
        textureBuffer.clear();
        textureBuffer.put(OpenGLUtils.TEXTURE);
        textureBuffer.position(0);

        return super.onDraw(texture, filterChain);
    }

    @Override
    public void afterDraw(FilterContext filterContext) {
        super.afterDraw(filterContext);

        Face face = filterContext.face;
        if (face == null) {
            return;
        }

        // 开启混合模式
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // 计算基于画布的鼻子中心点坐标
        float x = face.nose_x / face.imgWidth * filterContext.width;   // 图片坐标转换为画布坐标
        float y = (float) ((1.0 - face.nose_y / face.imgHeight) * filterContext.height);   // 画布原点在左下角

        // 通过鼻子左右嘴角x的差作为贴纸的宽
        float mouseR = face.mouseRight_x / face.imgWidth * filterContext.width;
        float mouseL = face.mouseLeft_x / face.imgWidth * filterContext.width;
        int width = (int) ((mouseR - mouseL) * 0.75f);

        // 以嘴角Y与鼻子中心点的Y差值作为贴纸的高
        float mouseY = (1.0f - face.mouseLeft_y / face.imgHeight) * filterContext.height;
        int height = (int) ((y - mouseY) * 0.75f);

        // 重置绘图区域
        GLES20.glViewport((int) x - width / 2, (int) y - height / 2, width, height);

        GLES20.glUseProgram(program);

        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        // 修改贴纸方向
        textureBuffer.clear();
        textureBuffer.put(OpenGLUtils.TEXTURE_180);

        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(vTexture, 0);

        //通知画画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        //关闭混合模式
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
