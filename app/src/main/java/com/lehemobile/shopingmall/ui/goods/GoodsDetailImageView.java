package com.lehemobile.shopingmall.ui.goods;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lehemobile.shopingmall.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 2/8/16.
 */
@EViewGroup(R.layout.view_goods_detail_image)
public class GoodsDetailImageView extends RelativeLayout {

    @ViewById
    ImageView imageView;

    @ViewById
    ProgressBar progress;



    public GoodsDetailImageView(Context context) {
        super(context);
    }

    public GoodsDetailImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodsDetailImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GoodsDetailImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) return;

        Picasso.with(getContext()).load(imageUrl).into(imageView, new Callback.EmptyCallback() {
            @Override
            public void onError() {
                super.onError();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                progress.setVisibility(View.GONE);
            }
        });
    }
}
