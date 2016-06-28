package com.tgh.devkit.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 *
 * Created by albert on 15/11/9.
 */
public class BaseViewPager extends ViewPager {
    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 当本ViewPager在ScrollView内部时，调用本方法来处理横向滑动和纵向滑动的冲突
     * @param scrollView 本ViewPager的父ScrollView
     * @param flag true，设置OnTouchListener，false，清楚OnTouchListener
     */
    public void setVerticalScrollEnable(ScrollView scrollView,boolean flag){
        if (!flag){
            parentScrollView = null;
            setOnTouchListener(null);
        }else {
            parentScrollView = scrollView;
            setOnTouchListener(touchListener);
        }
    }

    private ScrollView parentScrollView;

    OnTouchListener touchListener = new OnTouchListener() {

        int dragthreshold = 30;
        int downX;
        int downY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (parentScrollView == null){
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) event.getRawX();
                    downY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int distanceX = Math.abs((int) event.getRawX() - downX);
                    int distanceY = Math.abs((int) event.getRawY() - downY);

                    if (distanceY > distanceX && distanceY > dragthreshold) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        parentScrollView.getParent().requestDisallowInterceptTouchEvent(true);
                    } else if (distanceX > distanceY && distanceX > dragthreshold) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        parentScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    parentScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        }
    };
}
