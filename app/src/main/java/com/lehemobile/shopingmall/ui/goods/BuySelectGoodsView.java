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
import com.lehemobile.shopingmall.session.GoodsDetailSession;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 27/7/16.
 */
@EViewGroup(R.layout.view_buy_select_goods)
public class BuySelectGoodsView extends LinearLayout {

    public interface OnBuySelectGoodsListener {
        void onClose();
    }

    @ViewById
    ImageView goodsThumb;

    @ViewById
    TextView goodsName;
    @ViewById
    TextView goodsPrice;
    @ViewById
    TextView stock;

    private GoodsDetailSession session;

    public BuySelectGoodsView(Context context) {
        super(context);
    }

    public BuySelectGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BuySelectGoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BuySelectGoodsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(GoodsDetailSession session) {
        this.session = session;
        Goods goods = session.getGoods();
        Picasso.with(getContext()).load(goods.getThumbnail()).centerCrop().into(goodsThumb);
        goodsName.setText(goods.getName());
        goodsPrice.setText(getResources().getString(R.string.label_order_price, goods.getPriceString()));
        stock.setText(getResources().getString(R.string.label_goods_stock, goods.getStock()));
    }

    @Click
    void close() {
        //TODO close
    }
}
