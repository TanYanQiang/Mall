package com.lehemobile.shopingmall.ui.view.Picasso;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.R;
import com.squareup.picasso.Picasso;

/**
 * Created by tanyq on 5/8/16.
 */
public class PicassoHelper {
    public static void showGoodsThumb(Context context, String imageUrl, ImageView imageView) {
        Picasso.with(context).load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(context.getResources().getDimensionPixelOffset(R.dimen.corners_small),
                        0,
                        context.getResources().getDimensionPixelOffset(R.dimen.goods_thumb_border_width),
                        context.getResources().getColor(R.color.goods_thumb_borderColor)))
                .into(imageView);
    }

    public static void showAvatarCircleImage(String imageUrl, ImageView imageView) {

        showCircleImage(imageUrl, imageView, R.dimen.avatar_borderWidth, R.color.avatar_borderColor);
    }

    public static void showCircleImage(String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            Picasso.with(MyApplication.getInstance()).load(R.mipmap.avatar_default).into(imageView);
            return;
        }
        Picasso.with(MyApplication.getInstance()).load(imageUrl).transform(new CropCircleTransformation()).into(imageView);
    }

    public static void showCircleImage(String imageUrl, ImageView imageView, @DimenRes int borderWidth, @ColorRes int borderColor) {
        showCircleImage(imageUrl, imageView, R.mipmap.avatar_default, borderWidth, borderColor);
    }

    public static void showCircleImage(String imageUrl, ImageView imageView, @DrawableRes int placeholder, @DimenRes int borderWidth, @ColorRes int borderColor) {
        MyApplication context = MyApplication.getInstance();
        if (TextUtils.isEmpty(imageUrl)) {
            Picasso.with(context).load(R.mipmap.avatar_default).into(imageView);
            return;
        }
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(placeholder)
                .transform(new CropCircleTransformation(context.getResources().getDimension(borderWidth),
                        context.getResources().getColor(borderColor)))
                .into(imageView);
    }

}
