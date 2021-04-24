package com.example.openglcamerademo.filter;

import android.content.Context;

import com.example.openglcamerademo.R;
import com.example.openglcamerademo.face.Face;

import java.nio.FloatBuffer;

public class BigEyeFilter extends AbstractFBOFilter{
    private FloatBuffer left, right;
    private int left_eye, right_eye;
    private Face face;


    public BigEyeFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.bigeye_frag);
    }
}
