package com.tgh.devkit.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tgh.devkit.core.utils.Utils;
import com.tgh.devkit.list.R;

/**
 * 垂直表视图
 * Created by albert on 16/4/13.
 */
public class VerticalSheetView extends LinearLayout{


    public interface OnItemClickListener{
        void onItemClick(int position, String text);
    }

    public VerticalSheetView(Context context) {
        super(context);
        init();
    }

    public VerticalSheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        extraAttributeSet(attrs, 0);
    }

    public VerticalSheetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        extraAttributeSet(attrs,defStyleAttr);
    }

    private ColorStateList mTextColor;
    private float mTextSize;
    private float mCornerRadius;
    private int mLineHeight;
    private int mBgColorNor = Color.WHITE;
    private int mBgColorPressed = Color.parseColor("#EF6C00");

    private void init() {
        setOrientation(VERTICAL);
        initDefaultTextColor();
        mTextSize = Utils.sp2px(getContext(), 14);
        mLineHeight = Utils.dp2px(getContext(), 50);
    }

    private void extraAttributeSet(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.VerticalSheetView, defStyle, 0);


        mTextColor = a.getColorStateList(R.styleable.VerticalSheetView_VSV_textColor);
        if (mTextColor == null){
            initDefaultTextColor();
        }

        mTextSize = a.getDimension(R.styleable.VerticalSheetView_VSV_textSzie,
                Utils.sp2px(getContext(), 14));

        mCornerRadius = a.getDimension(R.styleable.VerticalSheetView_VSV_cornerRadius, 0);


        mBgColorNor = a.getColor(R.styleable.VerticalSheetView_VSV_bgColorNor, mBgColorNor);
        mBgColorPressed = a.getColor(R.styleable.VerticalSheetView_VSV_bgColorPressed, mBgColorPressed);


        mLineHeight = a.getDimensionPixelOffset(R.styleable.VerticalSheetView_VSV_lineHeight, mLineHeight);

        String string = a.getString(R.styleable.VerticalSheetView_VSV_arrays);
        if (!TextUtils.isEmpty(string)){
            String[] array;
            if (string.contains(",")){
                array = string.split(",");
            }else {
                array = new String[]{string};
            }
            addLines(array);
        }

        a.recycle();

    }

    private void initDefaultTextColor() {
        mTextColor = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[]{
                        Color.WHITE,
                        Color.parseColor("#cc000000")
                }
        );
    }


    private Drawable generateFullBackground(){
        return generateLineBackground(new float[]{
                mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius,
                mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius,});
    }

    private Drawable generateTopBackground(){
        return generateLineBackground(new float[]{mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius,0,0,0,0});
    }

    private Drawable generateBottomBackground(){
        return generateLineBackground(new float[]{0,0,0,0, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius});
    }

    private Drawable generateCenterBackground(){
        return generateLineBackground(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
    }


    private Drawable generateLineBackground(float[] radius){
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, generateShapeDrawable(radius,mBgColorPressed));
        stateListDrawable.addState(StateSet.WILD_CARD, generateShapeDrawable(radius, mBgColorNor));
        return stateListDrawable;
    }

    @NonNull
    private ShapeDrawable generateShapeDrawable(float[] radius,int color) {
        ShapeDrawable drawable = new ShapeDrawable();
        if (mCornerRadius > 0){
            drawable.setShape(new RoundRectShape(radius,null,null));
        }else {
            drawable.setShape(new RectShape());
        }
        drawable.getPaint().setColor(color);
        return drawable;
    }


    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addLines(String[] arrays){
        if (arrays == null || arrays.length == 0 ){
            removeAllViews();
            return;
        }


        for (int i = 0; i < arrays.length; i++) {
            String text = arrays[i];
            TextView textView = generateLineView();
            if (arrays.length == 1){
                textView.setBackgroundDrawable(generateFullBackground());
            }else if (i == 0){
                textView.setBackgroundDrawable(generateTopBackground());
            }else if (i == arrays.length-1){
                textView.setBackgroundDrawable(generateBottomBackground());
            }else {
                textView.setBackgroundDrawable(generateCenterBackground());
            }
            textView.setTag(i);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    int position = (Integer) tv.getTag();
                    if (itemClickListener!=null){
                        itemClickListener.onItemClick(position,tv.getText().toString());
                    }
                }
            });
            textView.setText(text);

            addView(textView);
        }


    }

    private TextView generateLineView() {
        TextView textView = new TextView(getContext());
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,mLineHeight);
        textView.setTextColor(mTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(params);
        return textView;
    }

}
