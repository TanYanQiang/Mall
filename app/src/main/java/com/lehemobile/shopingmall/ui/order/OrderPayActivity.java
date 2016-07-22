package com.lehemobile.shopingmall.ui.order;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.session.OrderPaySession;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.order.pay.PaymentMode;
import com.lehemobile.shopingmall.ui.view.PayMethodItemView;
import com.lehemobile.shopingmall.ui.view.PayMethodItemView_;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropSquareTransformation;

/**
 * 支付订单
 * Created by  on 22/7/16.
 */
@EActivity(R.layout.activity_order_pay)
public class OrderPayActivity extends BaseActivity {
    @Extra
    Order order;

    @ViewById
    TextView orderNumber;
    @ViewById
    TextView orderPrice;

    @ViewById
    LinearLayout goodsInfo;


    @ViewById
    RadioGroup payMethodGroup;
    private OrderPaySession orderPaySession;


    @AfterViews
    void init() {
        orderPaySession = new OrderPaySession();

        updatePaymentMode();
        if (order == null) return;
        orderNumber.setText(getString(R.string.label_order_number, order.getOrderNumber()));
        orderPrice.setText(getString(R.string.label_order_price, order.getTotalPrice()));
        updateGoodsInfo();

    }

    @Click
    void payBtn() {
        if (orderPaySession.getCurrentPaymentMode() == null) {
            showToast("请选择支付方式");
            return;
        }

        Logger.i(orderPaySession.getCurrentPaymentMode().getTitle());
    }

    private void updateGoodsInfo() {
        View view = getLayoutInflater().inflate(R.layout.view_order_goods_info, null);
        ImageView orderThumb = (ImageView) view.findViewById(R.id.orderThumb);
        TextView goodsName = (TextView) view.findViewById(R.id.goodsName);
        TextView goodsPrice = (TextView) view.findViewById(R.id.goodsPrice);
        TextView count = (TextView) view.findViewById(R.id.count);

        goodsName.setText(order.getGoods().getName());
        goodsPrice.setText(getResources().getString(R.string.label_order_price, order.getGoods().getPrice()));
        count.setText(getResources().getString(R.string.label_order_count, order.getCount()));

        Glide.with(this).load(order.getGoods().getThumbnail())
                .bitmapTransform(new CropSquareTransformation(this))
                .into(orderThumb);

        goodsInfo.removeAllViews();
        goodsInfo.addView(view);
    }


    /**
     * 更新支付方式
     */
    private void updatePaymentMode() {
        payMethodGroup.removeAllViews();
        List<PaymentMode> paymentModes = new ArrayList<>();
        PaymentMode aliPay = new PaymentMode();
        aliPay.setTitle("支付宝");
        aliPay.setType(PaymentMode.TYPE_ALIPAY);
        paymentModes.add(aliPay);

        PaymentMode weixinPay = new PaymentMode();
        weixinPay.setTitle("微信支付");
        weixinPay.setType(PaymentMode.TYPE_WEIXINPAY);
        paymentModes.add(weixinPay);

        for (int i = 0; i < paymentModes.size(); i++) {
            PaymentMode paymentMode = paymentModes.get(i);
            addPaymentMode(paymentMode, i);
        }

    }

    private void addPaymentMode(final PaymentMode paymentMode, int index) {
        PayMethodItemView payMethodItemView = PayMethodItemView_.build(this);
        payMethodItemView.updateUI(paymentMode);
        payMethodItemView.setId(index);
        payMethodItemView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO 设置支付方式
                    orderPaySession.setCurrentPaymentMode(paymentMode);
                }
            }
        });

        payMethodItemView.setChecked(paymentMode == orderPaySession.getCurrentPaymentMode());

        payMethodGroup.addView(payMethodItemView);
    }

}
