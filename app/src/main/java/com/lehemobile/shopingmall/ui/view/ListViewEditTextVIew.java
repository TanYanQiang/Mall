package com.lehemobile.shopingmall.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 4/8/16.
 */
@EViewGroup(R.layout.view_editbox_item)
public class ListViewEditTextVIew extends LinearLayout {

    @ViewById
    TextView text;
    @ViewById
    EditText editText;

    public ListViewEditTextVIew(Context context) {
        super(context);
    }

    public ListViewEditTextVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewEditTextVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListViewEditTextVIew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void updateUI(String label, String hint) {
        text.setText(label);
        editText.setHint(hint);
    }

    public String getEditText() {
        return editText.getText().toString().trim();
    }

}
