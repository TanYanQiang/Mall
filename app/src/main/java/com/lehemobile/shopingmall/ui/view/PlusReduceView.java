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
    public interface OnPlusReduceListener {
        /**
         * 值改变
         *
         * @param number
         */
        void onNumberChanged(int number);
    }

    @ViewById(R.id.number)
    EditText numberEt;

    public void setOnPlusReduceListener(OnPlusReduceListener onPlusReduceListener) {
        this.onPlusReduceListener = onPlusReduceListener;
    }

    private OnPlusReduceListener onPlusReduceListener;

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

    public void setNumber(int number) {
        numberEt.setText(String.valueOf(number));
    }


    public int getNumber() {
        String string = numberEt.getText().toString();
        return Integer.parseInt(string);
    }

    @Click
    void reduce() {
        int number = getNumber() - 1;
        number = number < 1 ? 1 : number;

        onChanage(number);
        numberEt.setText(String.valueOf(number));
    }

    @Click
    void plus() {
        int number = getNumber() + 1;
        numberEt.setText(String.valueOf(number));
        onChanage(number);
    }

    private void onChanage(int number) {
        if (onPlusReduceListener != null) {
            onPlusReduceListener.onNumberChanged(number);
        }
    }
}
