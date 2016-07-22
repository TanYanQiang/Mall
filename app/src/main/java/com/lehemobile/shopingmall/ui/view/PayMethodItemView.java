package com.lehemobile.shopingmall.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.order.pay.PaymentMode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;


/**
 * Created by tanyq on 23/3/15.
 */
@EView
public class PayMethodItemView extends RadioButton {

    @DimensionPixelSizeRes(R.dimen.line_height_lv1)
    int minHeight;

    @ColorRes(R.color.text_color_lv1)
    int textColor;

    @DimensionPixelSizeRes(R.dimen.margin_15)
    int margin15;
    @DimensionPixelSizeRes(R.dimen.margin_8)
    int margin8;

    public PayMethodItemView(Context context) {
        super(context);
    }

    public PayMethodItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PayMethodItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @AfterViews
    void initViews() {


        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        setButtonDrawable(R.color.transparent);
        setTextColor(textColor);
        setTextSize(16);
        setMinHeight(minHeight);
        setPadding(0, margin8, 0, margin8);
        setLineSpacing(1.3f, 1.0f);

    }

    public void updateUI(PaymentMode paymentMode) {
        if (paymentMode == null) return;

        updateDrawables(paymentMode);


        String title = paymentMode.getTitle();
        String value = paymentMode.getValue();

        setText(title);

    }

    private void updateDrawables(PaymentMode paymentMode) {

        Drawable drawable = getResources().getDrawable(R.drawable.orange_radio);
        if (drawable != null) {
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        }

        int leftDrawableResId = 0;

        switch (paymentMode.getType()) {
            case PaymentMode.TYPE_ALIPAY:
                leftDrawableResId = R.mipmap.icon_alipay;
                break;
            case PaymentMode.TYPE_WEIXINPAY:
                leftDrawableResId = R.mipmap.icon_weixin_pay;
                break;

        }
        Drawable leftDrawable = getResources().getDrawable(leftDrawableResId);
        if (leftDrawable != null) {
            /// 这一步必须要做,否则不会显示.
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        }
        setCompoundDrawables(leftDrawable, null, drawable, null);

        setCompoundDrawablePadding(margin15);

    }
}
