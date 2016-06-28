package com.tgh.devkit.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgh.devkit.list.R;

/**
 *
 * Created by albert on 16/3/10.
 */
public class LineView extends FrameLayout {

    private TextView tv_title;
    private TextView tv_content;
    private ImageView iv_forward;

    String title = null;
    String content = null;
    float titleSize = 14f;
    float contentSize = 14f;
    ColorStateList titleColor = null;
    ColorStateList contentColor = null;
    Drawable forwardIcon = null;
    Drawable titleIcon = null;
    Drawable contentBg = null;

    public LineView(Context context) {
        super(context);
        onFinishInflate();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        extraAttributes(context, attrs);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extraAttributes(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        inflate(getContext(),R.layout.v_line, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_forward = (ImageView) findViewById(R.id.iv_forward);
        init();
        super.onFinishInflate();
    }

    void extraAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.LineView, 0, 0);
        try {
            title = a.getString(R.styleable.LineView_LineView_Title);
            content = a.getString(R.styleable.LineView_LineView_Content);
            titleSize = a.getDimension(R.styleable.LineView_LineView_TitleSize, titleSize);
            contentSize = a.getDimension(R.styleable.LineView_LineView_ContentSize, contentSize);
            titleColor =  a.getColorStateList(R.styleable.LineView_LineView_TitleColor);
            contentColor =  a.getColorStateList(R.styleable.LineView_LineView_ContentColor);
            forwardIcon = a.getDrawable(R.styleable.LineView_LineView_ForwardIcon);
            titleIcon = a.getDrawable(R.styleable.LineView_LineView_TitleIcon);
            contentBg = a.getDrawable(R.styleable.LineView_LineView_ContentBg);
        }finally {
            a.recycle();
        }
    }

    private void init() {
        if (titleColor == null){
            titleColor = ColorStateList.valueOf(Color.BLACK);
        }

        if (contentColor == null){
            contentColor = ColorStateList.valueOf(Color.BLACK);
        }

        tv_title.setTextColor(titleColor);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);
        tv_title.setText(title);

        tv_content.setTextColor(contentColor);
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_PX,contentSize);
        tv_content.setText(content);

        setForwardIcon(forwardIcon);

        tv_title.setCompoundDrawablesWithIntrinsicBounds(titleIcon, null, null, null);
        tv_content.setBackgroundDrawable(contentBg);
    }

    public void setForwardIcon(Drawable forwardIcon){
        this.forwardIcon = forwardIcon;
        iv_forward.setImageDrawable(forwardIcon);
        iv_forward.setVisibility(forwardIcon == null ? GONE : VISIBLE);
    }


    public void setTitleText(@StringRes int resId){
        tv_title.setText(resId);
    }

    public void setTitleText(String title){
        tv_title.setText(title);
    }

    public void setTitleColor(@ColorRes int color){
        tv_title.setTextColor(getResources().getColor(color));
    }

    public void setTitleSize(@DimenRes int size){
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(size));
    }

    public void setContentText(@StringRes int resId){
        tv_content.setText(resId);
    }

    public void setContentText(String content){
        tv_content.setText(content);
    }

    public void setContentColor(@ColorRes int color){
        tv_content.setTextColor(getResources().getColor(color));
    }

    public void setContentSize(@DimenRes int size){
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(size));
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public TextView getTv_content() {
        return tv_content;
    }

    public ImageView getIv_forward() {
        return iv_forward;
    }

    public void setContentBg(@DrawableRes int bgRes){
        tv_content.setBackgroundResource(bgRes);
    }
}
