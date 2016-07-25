package com.lehemobile.shopingmall.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;
import com.orhanobut.logger.Logger;

/**
 * Created by tanyq on 25/7/16.
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        Logger.i("---------->applyOptions");
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
