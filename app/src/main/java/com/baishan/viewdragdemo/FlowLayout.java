package com.baishan.viewdragdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by RayYeung on 2016/9/29.
 */

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int HeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            // 当前子空间实际占据的宽度
            int cWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            // 当前子空间实际占据的高度
            int cHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (lineWidth + cWidth > widthSize) {
                width = Math.max(lineWidth, cWidth);
                lineWidth = cWidth;//重新开启新行
                height += lineHeight;
                lineHeight = cHeight;
            } else {
                lineWidth += cWidth;
                lineHeight = Math.max(lineHeight, cHeight);
            }
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == cCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, HeightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        int cCount = getChildCount();
        int left = 0;
        int top = 0;
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }
            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            if (lineWidth + cWidth + lp.leftMargin + lp.rightMargin > width) {
                top += lineHeight;
                left = 0;
                lineWidth = 0;
                lineHeight = 0;
            }
            lineWidth += cWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, cHeight + lp.topMargin + lp.bottomMargin);
            int cl = left + lp.leftMargin;
            int ct = top + lp.topMargin;
            int cr = cl + cWidth;
            int cb = ct + cHeight;
            childView.layout(cl, ct, cr, cb);
            left += lp.leftMargin + cWidth + lp.rightMargin;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
