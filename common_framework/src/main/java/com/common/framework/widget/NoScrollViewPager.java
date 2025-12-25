package com.common.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;


public class NoScrollViewPager extends ViewPager {

    private boolean canScroll = false;
    private boolean smoothScroll = true;
    public boolean noDuration=true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void canScroll(boolean enable) {
        this.canScroll = enable;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        this.smoothScroll = smoothScroll;
    }


    //设置滑动viewpager不需要时间间隔
    public void setNoDuration(boolean noDuration) {
        this.noDuration = noDuration;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (canScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    //去除页面切换时的滑动翻页效果
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }


    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

}
