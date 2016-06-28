package com.tgh.devkit.viewpager.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tgh.devkit.list.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 *
 * 用法

    <com.tgh.devkit.viewpager.indicator.CirclePageIndicator
        android:id="@+id/indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        app:CPI_SelDrawable="@drawable/"
        app:CPI_NorDrawable="@drawable/"
        app:CPI_Padding="10dp"
    />

    circlePageIndicator.addTabs(size);
    circlePageIndicator.setCurrentItem(index);
 *
 *
 * Created by albert on 15/11/9.
 */
public class CirclePageIndicator extends LinearLayout {

    private Drawable selDrawable;
    private Drawable norDrawable;
    private int padding;

    public CirclePageIndicator(Context context) {
        this(context, null);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        extraAttributes(context,attrs);
    }


    void extraAttributes(Context context, AttributeSet attrs) {
        if (attrs == null){
            return;
        }

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CirclePageIndicator,
                0, 0);
        try {
            if (a.hasValue(R.styleable.CirclePageIndicator_CPI_SelDrawable)){
                selDrawable = a.getDrawable(R.styleable.CirclePageIndicator_CPI_SelDrawable);
            }
            if (a.hasValue(R.styleable.CirclePageIndicator_CPI_NorDrawable)){
                norDrawable = a.getDrawable(R.styleable.CirclePageIndicator_CPI_NorDrawable);
            }

            padding = a.getDimensionPixelOffset(R.styleable.CirclePageIndicator_CPI_Padding,padding);

        } finally {
            a.recycle();
        }
    }


    private void init() {
        setOrientation(HORIZONTAL);
        selDrawable = getResources().getDrawable(R.drawable.cpi_sel);
        norDrawable = getResources().getDrawable(R.drawable.cpi_nor);
        padding = (int) (getResources().getDisplayMetrics().density * 10 + 0.5f);
    }

    public void setSelDrawable(Drawable selDrawable) {
        this.selDrawable = selDrawable;
        invalidate();
    }

    public void setNorDrawable(Drawable norDrawable) {
        this.norDrawable = norDrawable;
        invalidate();
    }

    public void setPadding(int padding) {
        this.padding = padding;
        invalidate();
    }

    public Drawable getSelDrawable() {
        return selDrawable;
    }

    public Drawable getNorDrawable() {
        return norDrawable;
    }

    public int getPadding() {
        return padding;
    }

    public void addTabs(int count){
        removeAllViews();
        for (int i = 0; i < count; i++) {
            addTab(i==count-1);
        }
        setCurrentItem(0);
    }

    public void setCurrentItem(int index) {
        final int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final TabView child = (TabView) getChildAt(i);
            final boolean isSelected = (i == index);
            child.setChecked(isSelected);
        }
    }

    private void addTab(boolean isLast) {
        TabView tabView = new TabView(getContext());
        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        if (!isLast){
            params.rightMargin = padding;
        }
        addView(tabView, params);
    }

    class TabView extends ImageView implements Checkable {
        private boolean checked;
        public TabView(Context context) {
            super(context);
        }

        @Override
        public void setChecked(boolean checked) {
            this.checked = checked;
            updateUI();
        }

        private void updateUI() {
            if (checked){
                setImageDrawable(selDrawable);
            }else {
                setImageDrawable(norDrawable);
            }
        }

        @Override
        public boolean isChecked() {
            return checked;
        }

        @Override
        public void toggle() {
            setChecked(!checked);
        }

    }

}
