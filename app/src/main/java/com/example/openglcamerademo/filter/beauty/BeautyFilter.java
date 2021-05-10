package com.example.openglcamerademo.filter.beauty;

import android.content.Context;

import com.example.openglcamerademo.filter.AbstractFilter;
import com.example.openglcamerademo.filter.FilterChain;

/**
 * Created by Zach on 2021/5/10 9:47
 */
public class BeautyFilter extends AbstractFilter {

    private BeautyBlurFilter beautyVerticalBlurFilter;
    private BeautyBlurFilter beautyHorizontalBlurFilter;
    private BeautyHighpassFilter beautyHighpassFilter;
    private BeautyHighpassBlurFilter beautyHighpassBlurFilter;
    private BeautyAdjustFilter beautyAdjustFilter;


    public BeautyFilter(Context context) {
        super(context, -1, -1);

        beautyVerticalBlurFilter = new BeautyBlurFilter(context);
        beautyHorizontalBlurFilter = new BeautyBlurFilter(context);
        beautyHighpassFilter = new BeautyHighpassFilter(context);
        beautyHighpassBlurFilter = new BeautyHighpassBlurFilter(context);
        beautyAdjustFilter = new BeautyAdjustFilter(context);
    }

    @Override
    public int onDraw(int texture, FilterChain filterChain) {
        filterChain.setPause(true);

        // 1、模糊处理
        beautyVerticalBlurFilter.setTextureOffset(0, filterChain.getFilterContext().height);
        int blurTexture = beautyVerticalBlurFilter.onDraw(texture, filterChain);
        beautyHorizontalBlurFilter.setTextureOffset(filterChain.getFilterContext().width ,0);
        blurTexture = beautyHorizontalBlurFilter.onDraw(blurTexture, filterChain);

        // 2、高反差保留 边缘锐化
        beautyHighpassFilter.setBlurTexture(blurTexture);
        int highpassTexture = beautyHighpassFilter.onDraw(texture, filterChain);

        // 3、保边预处理 保留边缘的细节不被模糊掉
        int highpassBlurTexture = beautyHighpassBlurFilter.onDraw(highpassTexture, filterChain);

        // 磨皮调整
        beautyAdjustFilter.setBlurTexture(blurTexture);
        beautyAdjustFilter.setHighpassBlurTexture(highpassBlurTexture);
        int beautyTexture = beautyAdjustFilter.onDraw(texture, filterChain);

        filterChain.setPause(false);
        return filterChain.proceed(beautyTexture);
    }
}
