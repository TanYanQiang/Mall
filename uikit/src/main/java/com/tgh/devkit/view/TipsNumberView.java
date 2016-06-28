package com.tgh.devkit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.tgh.devkit.core.text.TextHelper;
import com.tgh.devkit.core.utils.Utils;
import com.tgh.devkit.list.R;

/**
 *
 * Created by albert on 16/3/30.
 */
public class TipsNumberView extends View {

    private TextPaint mPaint;
    private int number;
    private OvalShape mDefaultBg;
    private int size;
    private int maxNumber;
    private int bgColor = Color.RED;
    private int textColor = Color.WHITE;
    private Drawable bgDrawable;
    private int borderLeft,borderTop,borderRight,borderBottom;
    private float cx;
    private float cy;
    private boolean noNumber;
    private int noNumberSize;

    public TipsNumberView(Context context) {
        super(context);
        init();
    }

    public TipsNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        extraFromAttribute(attrs, 0);
    }

    public TipsNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        extraFromAttribute(attrs, defStyleAttr);
    }

    private void init() {
        size = Utils.dp2px(getContext(), 20);
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mDefaultBg = new OvalShape();
        maxNumber = 99;
    }

    private void extraFromAttribute(AttributeSet attrs, int defStyle){
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TipsNumberView, defStyle, 0);

        float scaleFactor = a.getFloat(R.styleable.TipsNumberView_TipsNumber_scale, 1.0f);
        size *= scaleFactor;

        noNumberSize = (int) (size * 0.7f);

        bgColor = a.getColor(R.styleable.TipsNumberView_TipsNumber_bgColor, bgColor);
        textColor = a.getColor(R.styleable.TipsNumberView_TipsNumber_textColor, textColor);

        bgDrawable = a.getDrawable(R.styleable.TipsNumberView_TipsNumber_bgDrawable);

        maxNumber = a.getInt(R.styleable.TipsNumberView_TipsNumber_maxNumber, maxNumber);
        number = a.getInt(R.styleable.TipsNumberView_TipsNumber_number, 0);

        noNumber = a.getBoolean(R.styleable.TipsNumberView_TipsNumber_noNumber, false);

        int border = a.getDimensionPixelOffset(R.styleable.TipsNumberView_TipsNumber_border, 0);
        border *= scaleFactor;
        borderLeft = borderRight = borderBottom = borderTop = border;

        if (a.hasValue(R.styleable.TipsNumberView_TipsNumber_borderLeft)){
            borderLeft = (int) (a.getDimensionPixelOffset(R.styleable.TipsNumberView_TipsNumber_borderLeft,0) *scaleFactor);
        }
        if (a.hasValue(R.styleable.TipsNumberView_TipsNumber_borderTop)){
            borderTop = (int) (a.getDimensionPixelOffset(R.styleable.TipsNumberView_TipsNumber_borderTop,0) *scaleFactor);
        }
        if (a.hasValue(R.styleable.TipsNumberView_TipsNumber_borderRight)){
            borderRight = (int) (a.getDimensionPixelOffset(R.styleable.TipsNumberView_TipsNumber_borderRight,0) *scaleFactor);
        }
        if (a.hasValue(R.styleable.TipsNumberView_TipsNumber_borderBottom)){
            borderBottom = (int) (a.getDimensionPixelOffset(R.styleable.TipsNumberView_TipsNumber_borderBottom,0) *scaleFactor);
        }


        int w = size - borderLeft - borderRight;
        int h = size - borderTop - borderBottom;
        cx = w /2f + borderLeft;
        cy = h/2f + borderTop;

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (noNumber){
            drawNoNumber(canvas);
            return;
        }

        if (number == 0){
            return;
        }

        drawBg(canvas);

        mPaint.setColor(textColor);
        mPaint.setTextSize(getNumberSize());
        mPaint.setFakeBoldText(true);
        TextHelper.drawTextCentred(canvas, mPaint, String.valueOf(getNumber()), cx, cy);
    }

    private void drawNoNumber(Canvas canvas) {
        float dx = (size-noNumberSize)/2f;
        canvas.translate(dx,dx);

        if (bgDrawable == null){
            mDefaultBg.resize(noNumberSize, noNumberSize);
            mPaint.setColor(bgColor);
            mDefaultBg.draw(canvas,mPaint);
        }else {
            bgDrawable.setBounds(0, 0,noNumberSize,noNumberSize);
            bgDrawable.draw(canvas);
        }
    }

    private void drawBg(Canvas canvas){
        if (bgDrawable == null){
            mDefaultBg.resize(size, size);
            mPaint.setColor(bgColor);
            mDefaultBg.draw(canvas,mPaint);
        }else {
            bgDrawable.setBounds(0, 0,size,size);
            bgDrawable.draw(canvas);
        }
    }

    public void setNumber(int number) {
        this.number = number;
        invalidate();
    }

    public void setNoNumber(boolean noNumber) {
        this.noNumber = noNumber;
        invalidate();
    }

    public boolean isNoNumber() {
        return noNumber;
    }

    private String getNumber(){
        if (number <= 0){
            return "0";
        }else if (number > maxNumber){
            return maxNumber+"+";
        }else {
            return String.valueOf(number);
        }
    }

    private float getNumberSize(){
        int s = Math.min(size-borderLeft-borderRight,size-borderTop-borderBottom);
        if (number > maxNumber){
            return s * 0.45f;
        }
        if (number >= 10){
            return s * 0.65f;
        }
        return s * 0.85f;
    }
}
