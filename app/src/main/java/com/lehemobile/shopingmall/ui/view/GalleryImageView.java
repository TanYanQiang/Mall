package com.lehemobile.shopingmall.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lehemobile.shopingmall.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 23/8/16.
 */
@EViewGroup(R.layout.view_gallery_image)
public class GalleryImageView extends RelativeLayout {

    @ViewById
    ImageView imageView;
    @ViewById
    ContentLoadingProgressBar progressBar;

    public GalleryImageView(Context context) {
        super(context);
    }

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(String url) {
//        progressBar.hide();
        if (TextUtils.isEmpty(url)) return;
        progressBar.show();
        Picasso.with(getContext()).load(url).fit().centerCrop().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.hide();
            }

            @Override
            public void onError() {
                progressBar.hide();
            }
        });
    }

}
