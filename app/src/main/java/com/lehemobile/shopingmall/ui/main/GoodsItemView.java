package com.lehemobile.shopingmall.ui.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 30/7/16.
 */
@EViewGroup(R.layout.view_home_goods_item)
public class GoodsItemView extends LinearLayout {

    @ViewById
    ImageView imageView;
    public GoodsItemView(Context context) {
        super(context);
    }

    public GoodsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GoodsItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(Goods goods){
//        Glide.with(getContext()).load(goods.getThumbnail()).into(imageView);
        Picasso.with(getContext()).load(goods.getThumbnail()).into(imageView);
    }
}
