package com.lehemobile.shopingmall.ui.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 3/8/16.
 */
@EViewGroup(R.layout.list_view_single_line)
public class ListViewSingleLine extends LinearLayout {
    @ViewById
    TextView text;

    public ListViewSingleLine(Context context) {
        super(context);
    }

    public ListViewSingleLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewSingleLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListViewSingleLine(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(String str) {
        text.setText(str);
    }
}
