package com.baishan.viewdragdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by RayYeung on 2017/2/8.
 */

public class CustScrollview extends ScrollView {

    boolean needConsumeTouch = true;
    int maxScroll = -1;
    private boolean allowDragBottom = true;
    private float downY;

    public CustScrollview(Context context) {
        this(context, null);
    }

    public CustScrollview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getRawY();
            needConsumeTouch = true;
            //System.out.println(getScrollY() +"-"+ getMeasuredHeight() +"-"+ maxScroll);
            if (maxScroll > 0 && getScrollY() + getMeasuredHeight() >= maxScroll-2) {
                allowDragBottom = true;
            } else {
                allowDragBottom = false;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!needConsumeTouch) {
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            } else if (allowDragBottom == true) {
                if (downY - ev.getRawY() > 2) {
                    needConsumeTouch = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            }
        }
        getParent().requestDisallowInterceptTouchEvent(needConsumeTouch);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (maxScroll < 0) {
            maxScroll = computeVerticalScrollRange();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
