package com.example.openglcamerademo.filter;

import com.example.openglcamerademo.face.Face;

import java.util.List;

/**
 * 责任链模式
 */
public class FilterChain {

    private FilterContext filterContext;
    private List<AbstractFilter> filters;
    private int index;
    private boolean pause = false;

    public FilterChain(List<AbstractFilter> filters, int index, FilterContext filterContext) {
        this.filters = filters;
        this.index = index;
        this.filterContext = filterContext;
    }


    public FilterContext getFilterContext() {
        return filterContext;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setFilterContext(FilterContext filterContext) {
        this.filterContext = filterContext;
    }

    public int proceed(int textureId) {
        if (index >= filters.size()) {
            return textureId;
        }
        if (pause) {
            return textureId;
        }

        FilterChain nextFilterChain = new FilterChain(filters, index + 1, filterContext);
        AbstractFilter abstractFilter = filters.get(index);
        return abstractFilter.onDraw(textureId, nextFilterChain);
    }

    public void setSize(int width, int height) {
        filterContext.setSize(width, height);
    }

    public void setTransformMatrix(float[] matrix) {
        filterContext.setTransformMatrix(matrix);
    }

    public void setFace(Face face) {
        filterContext.setFace(face);
    }

    public void release() {
        for (AbstractFilter filter : filters) {
            filter.release();
        }
    }

}
