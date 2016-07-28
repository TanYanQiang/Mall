package com.lehemobile.shopingmall.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lehemobile.shopingmall.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 27/7/16.
 */
@EViewGroup(R.layout.view_plus_reduce)
public class PlusReduceView extends LinearLayout {

    @ViewById(R.id.number)
    EditText numberEt;

    public PlusReduceView(Context context) {
        super(context);
    }

    public PlusReduceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlusReduceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlusReduceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @AfterViews
    void init() {
        numberEt.setText("1");
    }

    public int getNumber() {
        String string = numberEt.getText().toString();
        return Integer.parseInt(string);
    }

    @Click
    void reduce() {
        int number = getNumber() - 1;
        number = number < 1 ? 1 : number;
        numberEt.setText(String.valueOf(number));
    }

    @Click
    void plus() {
        numberEt.setText(String.valueOf(getNumber() + 1));
    }
}
