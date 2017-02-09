package com.baishan.viewdragdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by RayYeung on 2016/9/29.
 */

public class CustomViewGroup extends ViewGroup {

    public CustomViewGroup(Context context, AttributeSet attrs) {
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

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParms = null;

        int tWidth = 0;
        int bWidth = 0;
        int lHeight = 0;
        int rHeight = 0;

        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParms = (MarginLayoutParams) childView.getLayoutParams();
            if (i == 0 || i == 1) {
                tWidth += cWidth + cParms.leftMargin + cParms.rightMargin;
            }
            if (i == 2 || i == 3) {
                bWidth += cWidth + cParms.leftMargin + cParms.rightMargin;
            }
            if (i == 0 || i == 2) {
                lHeight += cHeight + cParms.topMargin + cParms.bottomMargin;
            }
            if (i == 1 || i == 3) {
                rHeight += cHeight + cParms.topMargin + cParms.bottomMargin;
            }
        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, HeightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParms = null;
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParms = (MarginLayoutParams) childView.getLayoutParams();
            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (i) {
                case 0:
                    cl = cParms.leftMargin;
                    ct = cParms.topMargin;
                    break;
                case 1:
                    cl = getWidth() - cWidth - cParms.rightMargin;
                    ct = cParms.topMargin;
                    break;
                case 2:
                    cl = cParms.leftMargin;
                    ct = getHeight() - cHeight - cParms.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - cWidth - cParms.rightMargin;
                    ct = getHeight() - cHeight - cParms.bottomMargin;
                    break;
            }
            cr = cl + cWidth;
            cb = ct + cHeight;
            childView.layout(cl, ct, cr, cb);
        }


    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
