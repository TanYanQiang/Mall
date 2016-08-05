package com.lehemobile.shopingmall.ui.view.Picasso;

import android.content.Context;
import android.widget.ImageView;

import com.lehemobile.shopingmall.R;
import com.squareup.picasso.Picasso;

/**
 * Created by tanyq on 5/8/16.
 */
public class PicassoHelper {
    public static void showGoodsThumb(Context context, String imageUrl, ImageView imageView) {
        Picasso.with(context).load(imageUrl)
                .resizeDimen(R.dimen.order_goods_thumb_width, R.dimen.order_goods_thumb_height)
                .centerCrop()
                .transform(new RoundedCornersTransformation(context.getResources().getDimensionPixelOffset(R.dimen.corners_small),
                        0,
                        context.getResources().getDimensionPixelOffset(R.dimen.goods_thumb_border_width),
                        context.getResources().getColor(R.color.goods_thumb_borderColor)))
                .into(imageView);
    }
}
