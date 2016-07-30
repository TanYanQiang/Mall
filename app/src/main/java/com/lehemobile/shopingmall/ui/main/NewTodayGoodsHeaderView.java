package com.lehemobile.shopingmall.ui.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 30/7/16.
 */
@EViewGroup(R.layout.fragment_main_new_today_header)
public class NewTodayGoodsHeaderView extends LinearLayout {


    @ViewById
    ImageView banner;

    @ViewById
    ImageView recommendImage;
    @ViewById
    TextView recommendTitle;
    @ViewById
    TextView recommendDesc;
    @ViewById
    ImageView vipImage;
    @ViewById
    TextView vipTitle;
    @ViewById
    TextView vipDesc;
    @ViewById
    ImageView jifenImage;
    @ViewById
    TextView jifenTitle;
    @ViewById
    TextView jifenDesc;

    public NewTodayGoodsHeaderView(Context context) {
        super(context);
    }

    public NewTodayGoodsHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewTodayGoodsHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NewTodayGoodsHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData() {
        updateRecommendUI();
        updateJifenUI();
        updateVipUI();
    }

    private void updateRecommendUI() {

    }

    private void updateVipUI() {

    }

    private void updateJifenUI() {

    }

    @Click(R.id.banner)
    void clickBanner() {
        Logger.i("Banner");
    }

    @Click(R.id.recommendLayout)
    void goRecommend() {
        Logger.i("今日推荐");
    }

    @Click(R.id.vipLayout)
    void goVip() {
        Logger.i("会员专享");
    }

    @Click(R.id.jifenLayout)
    void gojiFen() {
        Logger.i("积分商城");
    }
}
