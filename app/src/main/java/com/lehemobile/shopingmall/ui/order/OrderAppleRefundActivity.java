package com.lehemobile.shopingmall.ui.order;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyq on 5/8/16.
 */
@EActivity(R.layout.activity_order_apply_refund)
public class OrderAppleRefundActivity extends BaseActivity {
    @Extra
    Order order;

    @ViewById
    TextView refundReason;
    @ViewById
    TextView refundPrice;
    @ViewById
    EditText refundDesc;
    private PopupWindow popupWindow;


    @AfterViews
    void init() {
        updateReason(getReasons().get(0));
        refundPrice.setText(String.valueOf(order.getTotalPrice()));
    }


    private void updateReason(String reason) {
        refundReason.setText(reason);
    }


    @Click
    void refundReason() {
        int measuredWidth = refundReason.getMeasuredWidth();
        Logger.i("measuredWidth:" + measuredWidth);

        popupWindow = new PopupWindow(crateReasonView(), measuredWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAsDropDown(refundReason);
    }

    @Click
    void submit() {
        CharSequence reason = refundReason.getText();
        CharSequence price = refundPrice.getText();
        String desc = getInputText(refundDesc);

        //TODO 提交申请退款
    }


    private View crateReasonView() {
        View view = getLayoutInflater().inflate(R.layout.view_order_refund_reason, null);
        view.setBackgroundResource(R.drawable.bg_dialog);
        LinearLayout containerView = (LinearLayout) view.findViewById(R.id.container);
        containerView.removeAllViews();
        for (int i = 0; i < getReasons().size(); i++) {
            String reason = getReasons().get(i);
            View lineView = getLayoutInflater().inflate(R.layout.list_view_single_line, containerView, false);

            TextView text = (TextView) lineView.findViewById(R.id.text);
            text.setBackgroundResource(R.drawable.select_bg);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Object tag = view.getTag();
                    if (tag != null) {
                        String _reason = (String) tag;
                        updateReason(_reason);
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                }
            });
            text.setText(reason);
            text.setTag(reason);
            containerView.addView(lineView);
        }
        return view;
    }

    private List<String> getReasons() {
        List<String> reasons = new ArrayList<>();
        reasons.add("质量问题");
        reasons.add("拍错");
        reasons.add("不满意");
        return reasons;
    }
}
