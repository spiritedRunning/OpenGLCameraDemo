package com.example.openglcamerademo.filter;

import android.content.Context;

import com.example.openglcamerademo.R;

/**
 * Created by Zach on 2021/5/12 9:51
 */
public class SplitFilter extends AbstractFBOFilter {
    public SplitFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.split3_screen);
    }

    @Override
    public int onDraw(int texture, FilterChain filterChain) {
        return super.onDraw(texture, filterChain);
    }
}
