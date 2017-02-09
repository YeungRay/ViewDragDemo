package com.baishan.viewdragdemo;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by RayYeung on 2017/2/8.
 */

public class DragView extends ViewGroup {

    private ViewDragHelper mDragHelper;
    private View view1, view2;
    private int viewHeight;
    private GestureDetectorCompat gestureDetector;

    private static final int VEL_THRESHOLD = 100; // 滑动速度的阈值，超过这个绝对值认为是上下
    private static final int DISTANCE_THRESHOLD = 100; // 单位是像素，当上下滑动速度不够时，通过这个阈值来判定是应该粘到顶部还是底部

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 10f, new DragHelperCallback());
        //mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
        gestureDetector = new GestureDetectorCompat(getContext(),
                new YScrollDetector());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view1 = getChildAt(0);
        view2 = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        System.out.println(changed + "-" + l + "-" + t + "-" + r + "-" + b);
        viewHeight = view1.getMeasuredHeight();
        view1.layout(l, 0, r, viewHeight);
        view2.layout(l, viewHeight, r, viewHeight + view2.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(view1.getBottom()>0&&view1.getTop()<0){
            //正在动画中的时候，不处理touch事件
            return false;
        }

        boolean yScroll = gestureDetector.onTouchEvent(ev);
        boolean shouldIntercept = false;
        try {
            shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int action = ev.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) {
            // action_down时就让mDragHelper开始工作，否则有时候导致异常 他大爷的
            mDragHelper.processTouchEvent(ev);
        }

        return shouldIntercept && yScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx,
                                float dy) {

            // 垂直滑动时dy>dx，才被认定是上下拖动
            return Math.abs(dy) > Math.abs(dx);
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            onViewPosChanaged(changedView);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            animalBottomOrTop(releasedChild, yvel);
        }


        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int finalTop = top;
            if (child == view1) {
                if (top > 0) {
                    finalTop = 0;
                }
            } else if (child == view2) {
                if (top < 0) {
                    finalTop = 0;
                }
            }
            // finalTop代表的是理论上应该拖动到的位置。此处计算拖动的距离除以一个参数(3)，是让滑动的速度变慢。数值越大，滑动的越慢
            return child.getTop() + (finalTop - child.getTop()) / 3;
        }
    }

    private void animalBottomOrTop(View releasedChild, float yvel) {
        int finalTop = 0;
        if (releasedChild == view1) {
            if (yvel < -VEL_THRESHOLD || releasedChild.getTop() < -DISTANCE_THRESHOLD) {
                finalTop = -viewHeight;
            }
        }else{
            if (yvel > VEL_THRESHOLD || releasedChild.getTop() > DISTANCE_THRESHOLD) {
                finalTop = viewHeight;
            }
        }
        if(mDragHelper.smoothSlideViewTo(releasedChild,0,finalTop)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        if(mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void onViewPosChanaged(View changedView) {
        if (changedView == view1) {
            int offset = viewHeight + view1.getTop() - view2.getTop();
            view2.offsetTopAndBottom(offset);
        } else if (changedView == view2) {
            int offset = view2.getTop() - viewHeight
                    - view1.getTop();
            view1.offsetTopAndBottom(offset);
        }
        invalidate();
    }
}
