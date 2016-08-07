package com.lehemobile.shopingmall.ui.order;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.view.Picasso.PicassoHelper;
import com.lehemobile.shopingmall.ui.view.PlusReduceView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 7/8/16.
 */
@EViewGroup(R.layout.view_confirm_order_goods_item)
public class ConfirmOrderGoodsItemView extends RelativeLayout {

    public interface OnChangedListener {
        void onChanged(Goods goods);
    }

    @ViewById
    PlusReduceView plusReduceView;

    @ViewById
    ImageView goodsThumb;
    @ViewById
    TextView goodsPrice;
    @ViewById
    TextView goodsName;

    @ViewById
    TextView goodsStock;


    private OnChangedListener onChangedListener;

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    public ConfirmOrderGoodsItemView(Context context) {
        super(context);
    }

    public ConfirmOrderGoodsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConfirmOrderGoodsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmOrderGoodsItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(final Goods goods) {
        PicassoHelper.showGoodsThumb(getContext(), goods.getThumbnail(), goodsThumb);
        goodsPrice.setText(getResources().getString(R.string.label_order_price, goods.getPriceString()));
        goodsName.setText(goods.getName());
        plusReduceView.setNumber(goods.getBuyCount());
        plusReduceView.setOnPlusReduceListener(new PlusReduceView.OnPlusReduceListener() {
            @Override
            public void onNumberChanged(int number) {
                goods.setBuyCount(number);
                if (onChangedListener != null) {
                    onChangedListener.onChanged(goods);
                }
            }
        });
        goodsStock.setText("库存" + goods.getStock());

    }
}
