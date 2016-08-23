package com.lehemobile.shopingmall.ui.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.session.NewTodayGoodsSession;
import com.lehemobile.shopingmall.ui.common.WebViewActivity;
import com.lehemobile.shopingmall.ui.goods.GoodsDetailActivity_;
import com.lehemobile.shopingmall.ui.view.GalleryImageView;
import com.lehemobile.shopingmall.ui.view.GalleryImageView_;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.tgh.devkit.viewpager.InfiniteViewPager;
import com.tgh.devkit.viewpager.adapter.BasePagerAdapter;
import com.tgh.devkit.viewpager.adapter.InfinitePagerAdapter;
import com.tgh.devkit.viewpager.indicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by tanyq on 30/7/16.
 */
@EViewGroup(R.layout.fragment_main_new_today_header)
public class NewTodayGoodsHeaderView extends LinearLayout {

    public static final int DELAY_MILLIS = 10000;
    @ViewById
    InfiniteViewPager banner;

    @ViewById
    CirclePageIndicator indicator;

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


    private BannerAdapter bannerAdapter;
    private InfinitePagerAdapter infinitePagerAdapter;
    private CountDownTimer countDownTimer;


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

    @AfterViews
    void init() {
        initGalleryUI();
    }

    private void initGalleryUI() {
        banner.setPageMargin(20);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentItem(position % infinitePagerAdapter.getRealCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateGalleryUI(List<Goods> list) {
        bannerAdapter = new BannerAdapter((Activity) getContext(), list);
        bannerAdapter.setItemClickListener(new BasePagerAdapter.ItemClickListener<Goods>() {
            @Override
            public void onPagerItemClicked(int position, Goods goods) {
                Logger.i("banner click Item:" + position);

                GoodsDetailActivity_.intent(getContext()).goodsId(goods.getId()).start();
            }
        });
        banner.setAdapter(bannerAdapter);
        indicator.addTabs(list.size());
        infinitePagerAdapter = new InfinitePagerAdapter(bannerAdapter);
    }

    public void bindData(NewTodayGoodsSession session) {
        updateRecommendUI();
        updateJifenUI();
        updateVipUI();
        updateGalleryUI(session.getBannersData());
    }

    private void updateRecommendUI() {

    }

    private void updateVipUI() {

    }

    private void updateJifenUI() {

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
        WebViewActivity.intent(getContext()).url(AppConfig.INTEGRAL_MALL_URL).title("积分商城").start();
    }


    private class BannerAdapter extends BasePagerAdapter<Goods> {

        public BannerAdapter(Activity context, List<Goods> data) {
            super(context, data);
        }

        @Override
        protected void bindData(View view, Goods item) {
            GalleryImageView galleryImageView = (GalleryImageView) view;
            galleryImageView.bindData(item.getThumbnail());
        }

        @Override
        protected View newItemView(final int position, ViewGroup container) {
            return GalleryImageView_.build(context);
        }
    }

    private void startCounting() {
        cancelCountDownTimer();

        countDownTimer = new CountDownTimer(Integer.MAX_VALUE, DELAY_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("" + millisUntilFinished);
                autoSelectedBanner();
            }

            @Override
            public void onFinish() {

            }
        };
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
            }
        }, DELAY_MILLIS);
    }

    private void autoSelectedBanner() {
        if (banner == null) return;

        int currentItem = banner.getCurrentItem();
        Logger.i("autoSelectedBanner: currentItem->" + currentItem);
        currentItem = (currentItem + 1) % banner.getAdapter().getCount();
        banner.setCurrentItem(currentItem, true);
    }

    private void cancelCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            Logger.i("cancel");
        }
    }

    public void stopBannerSwitch() {
        cancelCountDownTimer();
    }

    public void startBannerSwitch() {
        startCounting();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Logger.i("onDetachedFromWindow");
        cancelCountDownTimer();
    }
}
