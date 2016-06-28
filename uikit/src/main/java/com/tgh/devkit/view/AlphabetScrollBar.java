package com.tgh.devkit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.tgh.devkit.core.text.TextHelper;
import com.tgh.devkit.list.R;

/**
 * 滑动选择字符的控件
 * @author albert
 */
public class AlphabetScrollBar extends View {

    private float mTextSize = 24f;
    private int mNorTextColor = Color.parseColor("#3098f9");
    private int mPressedTextColor = Color.parseColor("#3098f9");
    private int mNorBackground = Color.WHITE;
    private int mPressedBackground = Color.WHITE;


    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private boolean mPressed;

    private static final String[] DEFAULT_ARRAY = {
            "A","B","C","D","E","F","G","H","I","J","K",
            "L","M","N","O","P","Q","R","S","T","U","V",
            "W","X","Y","Z","#"};

    private String[] mArray = DEFAULT_ARRAY;
    private int mCurPosIdx;
    private int mOldPosIdx;
    private OnTouchBarListener mTouchListener;
    private TextView LetterNotice;
    private int mActionIndex;
    private float singleLetterH;

    public AlphabetScrollBar(Context context) {
        super(context);
        init(null, 0);
    }

    public AlphabetScrollBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AlphabetScrollBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.AlphabetScrollBar, defStyle, 0);

        mNorBackground = a.getColor(
                R.styleable.AlphabetScrollBar_ASB_norBackground,
                mNorBackground);
        mNorTextColor = a.getColor(
                R.styleable.AlphabetScrollBar_ASB_norTextColor,
                mNorTextColor);

        mPressedBackground = a.getColor(
                R.styleable.AlphabetScrollBar_ASB_pressedBackground,
                mPressedBackground);
        mPressedTextColor = a.getColor(
                R.styleable.AlphabetScrollBar_ASB_pressedTextColor,
                mPressedTextColor);

        mTextSize = a.getDimension(
                R.styleable.AlphabetScrollBar_ASB_textSize,
                mTextSize);

        if (a.hasValue(R.styleable.AlphabetScrollBar_ASB_arrays)) {
            String array = a.getString(
                    R.styleable.AlphabetScrollBar_ASB_arrays);
            if (array!=null){
                mArray = array.split(",");
            }
        }
        a.recycle();

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mNorTextColor);
        mTextWidth = mTextPaint.measureText("A");
        mTextHeight = mTextPaint.getTextSize();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        computeSingleLetterH();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int height;
        if (mArray==null || mArray.length==0){
            height = 0;
        }else {
            height = (int) (mArray.length*mTextHeight) + getPaddingTop() + getPaddingBottom();
        }

        switch (heightSpecMode) {
            case MeasureSpec.EXACTLY:
                height = heightSpecSize;
                break;
        }
        setMeasuredDimension(width,height);
        computeSingleLetterH();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) (mTextWidth+getPaddingLeft()+getPaddingRight());
    }

    private void computeSingleLetterH(){
        if (mArray == null || mArray.length ==0){
            singleLetterH = 0;
            return;
        }

        singleLetterH = (getHeight()-getPaddingTop()-getPaddingBottom()) / (float)mArray.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mArray==null || mArray.length ==0){
            return;
        }

        if (mPressed) {
            //如果处于按下状态，改变背景及相应字体的颜色
            canvas.drawColor(mPressedBackground);
            mTextPaint.setColor(mPressedTextColor);
        }else {
            canvas.drawColor(mNorBackground);
            mTextPaint.setColor(mNorTextColor);
        }


        float cx = getMeasuredWidth() / 2f;
        float cy = singleLetterH/2f;

        for (int i = 0; i < mArray.length; i++) {
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop()+i*singleLetterH);
            TextHelper.drawTextCentred(canvas, mTextPaint, mArray[i], cx, cy);
            canvas.restore();
        }

    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        if (mArray == null || mArray.length == 0){
            return super.onTouchEvent(event);
        }

        int action = MotionEventCompat.getActionMasked(event);
        float y;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActionIndex = MotionEventCompat.getActionIndex(event);
                mPressed = true;
                y = MotionEventCompat.getY(event, mActionIndex);
                mCurPosIdx = (int) (y / this.getHeight() * mArray.length);
                if (mTouchListener != null && mOldPosIdx != mCurPosIdx) {
                    if ((mCurPosIdx >= 0) && (mCurPosIdx < mArray.length)) {
                        mTouchListener.onTouch(mCurPosIdx, mArray[mCurPosIdx]);
                        this.invalidate();
                    }
                    mOldPosIdx = mCurPosIdx;
                }
                if (LetterNotice != null) {
                    LetterNotice.setText(mArray[mCurPosIdx]);
                    LetterNotice.setVisibility(View.VISIBLE);
                }
                return true;
            case MotionEvent.ACTION_UP:

                if (LetterNotice != null) {
                    LetterNotice.setVisibility(View.INVISIBLE);
                }

                mPressed = false;
                mCurPosIdx = -1;
                this.invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                y = MotionEventCompat.getY(event, mActionIndex);
                mCurPosIdx = (int) (y / this.getHeight() * mArray.length);
                if (mTouchListener != null && mCurPosIdx != mOldPosIdx) {
                    if ((mCurPosIdx >= 0) && (mCurPosIdx < mArray.length)) {
                        mTouchListener.onTouch(mCurPosIdx, mArray[mCurPosIdx]);
                        this.invalidate();
                    }
                    mOldPosIdx = mCurPosIdx;
                }

                if (mCurPosIdx >= 0 && mCurPosIdx < mArray.length) {
                    if (LetterNotice != null) {
                        LetterNotice.setText(mArray[mCurPosIdx]);
                        LetterNotice.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public interface OnTouchBarListener {
        void onTouch(int selectionIndex, String letter);
    }

    /**
     * 向外公开的方法
     */
    public void setOnTouchBarListener(OnTouchBarListener listener) {
        mTouchListener = listener;
    }

    public void setTextView(TextView letterNotice) {
        LetterNotice = letterNotice;
    }

    public void setKeyword(String[] array) {
        this.mArray = array;
        requestLayout();
        invalidate();
    }

    public void setPressedBackground(@ColorRes int pressedBackground) {
        this.mPressedBackground = pressedBackground;
        invalidate();
    }

    public void setPressedTextColor(@ColorRes int pressedTextColor) {
        this.mPressedTextColor = pressedTextColor;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        invalidateTextPaintAndMeasurements();
        requestLayout();
        invalidate();
    }

    public void setNorTextColor(@ColorRes int norTextColor) {
        this.mNorTextColor = norTextColor;
        invalidate();
    }

    public void setNorBackground(@ColorRes int norBackground) {
        this.mNorBackground = norBackground;
        invalidate();
    }
}
