package com.lehemobile.shopingmall.ui.goods;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 4/8/16.
 */
@EViewGroup(R.layout.view_goods_grid_item)
public class GoodsGridItemView extends LinearLayout {

    @ViewById
    ImageView thumbImage;

    @ViewById
    TextView goodsName;
    @ViewById
    TextView goodsPrice;
    @ViewById
    TextView favorite;

    public GoodsGridItemView(Context context) {
        super(context);
    }

    public GoodsGridItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodsGridItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GoodsGridItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void updateUI(Goods goods) {
        Picasso.with(getContext()).load(goods.getThumbnail()).into(thumbImage);
        goodsPrice.setText(getResources().getString(R.string.label_order_price, goods.getPrice()));
        goodsName.setText(goods.getName());

        favorite.setText(String.valueOf(goods.getFavoriteCount()));
        int favoriteDrawables = goods.isFavorite() ? R.mipmap.ic_collection : R.mipmap.ic_collection_normal;
        favorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, favoriteDrawables, 0);
    }

    @Click
    void favorite() {
        Logger.i("favorite");
    }
}
