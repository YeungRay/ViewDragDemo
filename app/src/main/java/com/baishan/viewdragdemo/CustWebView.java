package com.baishan.viewdragdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by RayYeung on 2017/2/8.
 */

public class CustWebView extends WebView {


    private float downY;

    public CustWebView(Context context) {
        this(context,null);
    }

    public CustWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
       if(ev.getAction()==MotionEvent.ACTION_DOWN){
           downY = ev.getRawY();
       }else if(ev.getAction()==MotionEvent.ACTION_MOVE){

       }
        return super.dispatchTouchEvent(ev);
    }
}
