package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

/**
 * Created by haha on 2015/3/23.
 */
public class UserDefinedLayout extends LoadingLayout {
    public UserDefinedLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs, PullToRefreshBase.UserDefinedMode ud_type) {
        super(context,mode,scrollDirection);
        if(ud_type== PullToRefreshBase.UserDefinedMode.HEADER
            &&attrs.hasValue(R.styleable.PullToRefresh_ptrUD_header_layout)){
            int layoutId=attrs.getResourceId(R.styleable.PullToRefresh_ptrUD_header_layout,-1);
            if(layoutId==-1){
                layoutId =R.layout.pull_to_refresh_header_horizontal;
            }
            LayoutInflater.from(context).inflate(layoutId, this);
        }else if(ud_type== PullToRefreshBase.UserDefinedMode.FOOTER
                &&attrs.hasValue(R.styleable.PullToRefresh_ptrUD_footer_layout)){
            int layoutId=attrs.getResourceId(R.styleable.PullToRefresh_ptrUD_footer_layout,-1);
            if(layoutId==-1){
                layoutId =R.layout.pull_to_refresh_header_horizontal;
            }
            LayoutInflater.from(context).inflate(layoutId, this);
        }else{
            LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_horizontal, this);
        }
    }

    @Override
    protected int getDefaultDrawableResId() {
        return 0;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {

    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {

    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {

    }
}
