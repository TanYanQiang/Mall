package com.lehemobile.shopingmall.ui.category;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.ui.order.pay.PaymentMode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;


/**
 * Created by tanyq on 23/3/15.
 */
@EView
public class CategoryItemView extends RadioButton {

    @DimensionPixelSizeRes(R.dimen.line_height_lv1)
    int minHeight;

    @ColorRes(R.color.text_category_item_color)
    int textColor;


    @DimensionPixelSizeRes(R.dimen.margin_8)
    int margin8;

    public CategoryItemView(Context context) {
        super(context);
    }

    public CategoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @AfterViews
    void initViews() {


        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        setGravity(Gravity.CENTER);
        setButtonDrawable(R.color.transparent);
        setTextColor(textColor);
        setTextSize(16);
        setBackgroundResource(R.drawable.category_item_bg);
        setMinHeight(minHeight);
        setLineSpacing(1.3f, 1.0f);

    }

    public void updateUI(Category category) {
        if (category == null) return;

        setText(category.getCategoryName());

    }


}
