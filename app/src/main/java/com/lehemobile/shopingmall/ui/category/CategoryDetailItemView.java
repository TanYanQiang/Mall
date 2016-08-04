package com.lehemobile.shopingmall.ui.category;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Category;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 4/8/16.
 */
@EViewGroup(R.layout.view_category_detail_item)
public class CategoryDetailItemView extends LinearLayout {
    @ViewById
    ImageView image;
    @ViewById
    TextView title;


    public CategoryDetailItemView(Context context) {
        super(context);
    }

    public CategoryDetailItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryDetailItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CategoryDetailItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void updateUI(Category detail) {
        title.setText(detail.getCategoryName());
        Picasso.with(getContext()).load(detail.getCategoryImage()).placeholder(R.mipmap.test_category_detail_item).into(image);
    }

}
