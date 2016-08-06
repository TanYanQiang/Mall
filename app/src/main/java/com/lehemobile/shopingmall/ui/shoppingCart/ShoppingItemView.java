package com.lehemobile.shopingmall.ui.shoppingCart;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.model.ShoppingSession;
import com.lehemobile.shopingmall.ui.view.Picasso.PicassoHelper;
import com.lehemobile.shopingmall.ui.view.PlusReduceView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 6/8/16.
 */
@EViewGroup(R.layout.view_shopping_item)
public class ShoppingItemView extends RelativeLayout {

    public interface OnChangedListener {
        void onChanged(ShoppingSession session);
    }


    @ViewById
    CheckBox checkbox;

    @ViewById
    PlusReduceView plusReduceView;

    @ViewById
    ImageView goodsThumb;
    @ViewById
    TextView goodsPrice;
    @ViewById
    TextView goodsName;

    private OnChangedListener onChangedListener;

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    public ShoppingItemView(Context context) {
        super(context);
    }

    public ShoppingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShoppingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindData(final ShoppingSession session) {
        final Goods goods = session.getGoods();
        PicassoHelper.showGoodsThumb(getContext(), goods.getThumbnail(), goodsThumb);
        goodsPrice.setText(getResources().getString(R.string.label_order_price, goods.getPriceString()));
        goodsName.setText(goods.getName());
        plusReduceView.setNumber(goods.getBuyCount());
        plusReduceView.setOnPlusReduceListener(new PlusReduceView.OnPlusReduceListener() {
            @Override
            public void onNumberChanged(int number) {
                goods.setBuyCount(number);
                session.setGoods(goods);
                if (onChangedListener != null) {
                    onChangedListener.onChanged(session);
                }
            }
        });
        checkbox.setChecked(session.isSelected());
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                session.setSelected(b);
                if (onChangedListener != null) {
                    onChangedListener.onChanged(session);
                }
            }
        });

    }
}
