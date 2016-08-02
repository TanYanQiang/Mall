package com.lehemobile.shopingmall.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Order;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;


/**
 * Created by tanyq on 22/7/16.
 */
@EViewGroup(R.layout.view_order_goods_info)
public class OrderGoodsInfo extends RelativeLayout {

    @ViewById
    ImageView orderThumb;
    @ViewById
    TextView goodsPrice;
    @ViewById
    TextView goodsName;
    @ViewById
    TextView count;


    public OrderGoodsInfo(Context context) {
        super(context);
    }

    public OrderGoodsInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderGoodsInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OrderGoodsInfo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(Order order) {

        goodsName.setText(order.getGoods().getName());
        goodsPrice.setText(getResources().getString(R.string.label_order_price, order.getGoods().getPrice()));
        count.setText(getResources().getString(R.string.label_order_count, order.getCount()));

        Picasso.with(getContext()).load(order.getGoods().getThumbnail())
                .transform(new CropSquareTransformation())
                .into(orderThumb);
    }

}
