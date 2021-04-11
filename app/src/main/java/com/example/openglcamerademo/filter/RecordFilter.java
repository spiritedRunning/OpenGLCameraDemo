package com.example.openglcamerademo.filter;

import android.content.Context;

import com.example.openglcamerademo.R;

public class RecordFilter extends AbstractFilter {

    public RecordFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);
    }
}
