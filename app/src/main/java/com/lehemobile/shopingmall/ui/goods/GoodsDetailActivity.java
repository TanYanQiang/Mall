package com.lehemobile.shopingmall.ui.goods;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.event.FavoriteEvent;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.session.GoodsDetailSession;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.shoppingCart.ShoppingCartActivity;
import com.lehemobile.shopingmall.ui.shoppingCart.ShoppingCartActivity_;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.tgh.devkit.core.text.SpannableStringHelper;
import com.tgh.devkit.dialog.DialogBuilder;
import com.tgh.devkit.viewpager.InfiniteViewPager;
import com.tgh.devkit.viewpager.adapter.BasePagerAdapter;
import com.tgh.devkit.viewpager.adapter.InfinitePagerAdapter;
import com.tgh.devkit.viewpager.indicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by  on 24/7/16.
 */
@EActivity(R.layout.activity_goods_detail)
@OptionsMenu(R.menu.menu_goods_detail)
public class GoodsDetailActivity extends BaseActivity {
    @Extra
    int goodsId;

    @ViewById
    InfiniteViewPager gallery;

    @ViewById
    CirclePageIndicator indicator;

    @ViewById
    TextView goodsName;
    @ViewById
    TextView goodsPrice;
    @ViewById
    TextView marketPrice;
    @ViewById
    TextView tradingCount;

    @ViewById
    View recommendLayout;
    @ViewById
    RecyclerView recommendList;


    @ViewById
    TabLayout tabs;

    @ViewById
    FrameLayout container;

    @ViewById
    TextView favorite;

    private GoodsDetailSession session;
    private GalleryAdapter galleryAdapter;
    private InfinitePagerAdapter infinitePagerAdapter;


    private void loadData() {
        //TODO 加载数据
        session = new GoodsDetailSession();

        Goods goods = new Goods();
        goods.setId(goodsId);
        goods.setName("Watbalan水博兰水润保湿平衡面膜10片/27g ，肌肤水润平衡动力源，镇定肌肤，强化水分，超微颗粒深入肌底，长效补充水分");
        goods.setPrice(100);
        goods.setThumbnail("http://a.vpimg2.com/upload/merchandise/pdc/473/177/1026334052051177473/6/8809383002388-110.jpg");
        goods.setTradingCount(200);
        List<String> images = new ArrayList<>();
        images.add("http://a.vpimg2.com/upload/merchandise/pdc/473/177/1026334052051177473/6/8809383002388-110.jpg");
//        images.add("http://a.appsimg.com/upload/merchandise/pdc/293/235/7649158865156235293/0/1981126-2_710x897_80.jpg");
        goods.setImages(images);

        goods.setDetail("http://a.appsimg.com/upload/merchandise/pdc/293/235/7649158865156235293/1/1981126-6_1440x8000_90.jpg");

        List<String> detailImages = new ArrayList<>();
//        detailImages.add("http://a.appsimg.com/upload/merchandise/pdc/884/474/1097265726116474884/9/8809261550468-110_1_1440x8000_90.jpg");
//        detailImages.add("http://a.appsimg.com/upload/merchandise/pdc/884/474/1097265726116474884/9/8809261550468-110_2_1440x8000_90.jpg");
//        detailImages.add("http://a.appsimg.com/upload/merchandise/pdc/884/474/1097265726116474884/9/8809261550468-110_4_1440x8000_90.jpg");
        detailImages.add("http://a.appsimg.com/upload/merchandise/pdc/473/177/1026334052051177473/6/8809383002388-110_2_1440x8000_90.jpg");
        goods.setDetailImages(detailImages);
        session.setGoods(goods);
        session.setFavorite(false);
        List<Goods> recommendGoods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Goods reGoods = new Goods();
            reGoods.setId(goodsId);
            reGoods.setName("Watbalan水博兰水润保湿平衡面膜10片/27g ，肌肤水润平衡动力源，镇定肌肤，强化水分，超微颗粒深入肌底，长效补充水分");
            reGoods.setPrice(100 * i);
            reGoods.setThumbnail("http://a.vpimg2.com/upload/merchandise/pdc/473/177/1026334052051177473/6/8809383002388-110.jpg");

            recommendGoods.add(reGoods);
        }

        session.setRecommendGoods(recommendGoods);

        updateUI();
    }

    @AfterViews
    void init() {


        initTabLayout();

        initGalleryUI();
        initRecommendList();

        loadData();
    }

    private void initTabLayout() {
        tabs.addTab(tabs.newTab().setText("图片详情").setTag(1));
        tabs.addTab(tabs.newTab().setText("产品参数").setTag(2));
        tabs.addTab(tabs.newTab().setText("用户评价").setTag(3));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = (int) tab.getTag();
                switch (index) {
                    case 1:
                        updateDetailUI();
                        break;
                    case 2:
                        updateGoodsDetailInfoUI();
                        break;
                    case 3:
                        updateGoodsCommentsUI();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initGalleryUI() {
        gallery.setPageMargin(20);
        gallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private void initRecommendList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recommendList.setLayoutManager(layoutManager);
    }


    private void updateUI() {

        updateGalleryUI(session.getGoods().getImages());
        updateGoodsInfoUI();
        updateDetailUI();
        updateFavoriteUI();

        updateRecommendGoodsUI(session.getRecommendGoods());
    }

    private void updateFavoriteUI() {
        if (session == null) return;
        favorite.setText(session.isFavorite() ? "已收藏" : "收藏");
        favorite.setCompoundDrawablesWithIntrinsicBounds(0, (session.isFavorite() ? R.mipmap.ic_collection : R.mipmap.ic_collection_normal), 0, 0);
    }


    private void updateGalleryUI(List<String> list) {
        galleryAdapter = new GalleryAdapter(this, list);
        gallery.setAdapter(galleryAdapter);
        indicator.addTabs(list.size());
        infinitePagerAdapter = new InfinitePagerAdapter(galleryAdapter);
    }

    private void updateGoodsInfoUI() {
        Goods goods = session.getGoods();
        goodsName.setText(goods.getName());
        String info = "￥" + goods.getPriceString();
        new SpannableStringHelper(info).relativeSize(goods.getPriceString(), 1.3f).attachToTextView(goodsPrice);

        marketPrice.getPaint().setColor(getResources().getColor(R.color.text_color_lv3));
        marketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        marketPrice.setText(getString(R.string.label_goods_detail_marketPrice, goods.getPriceString()));

        tradingCount.setText(getString(R.string.label_goods_trading_count, goods.getTradingCount()));
    }


    private void updateDetailUI() {
        if (session == null || session.getGoods() == null) return;

        GoodsDetailImageFragment fragment = GoodsDetailImageFragment_.builder().goods(session.getGoods()).build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    private void updateGoodsDetailInfoUI() {
        if (session == null || session.getGoods() == null) return;

        GoodsDetailInfoFragment fragment =
                GoodsDetailInfoFragment_.builder().goods(session.getGoods()).build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    private void updateGoodsCommentsUI() {

        if (session == null || session.getGoods() == null) return;

        GoodsDetailCommentListFragment fragment = GoodsDetailCommentListFragment_.builder().goods(session.getGoods()).build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }


    private class GalleryAdapter extends BasePagerAdapter<String> {

        public GalleryAdapter(Activity context, List<String> data) {
            super(context, data);
        }

        @Override
        protected void bindData(View view, String item) {
            ImageView imageView = (ImageView) view;
            Picasso.with(context).load(item).into(imageView);
        }

        @Override
        protected View newItemView(int position, ViewGroup container) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }
    }

    private void updateRecommendGoodsUI(List<Goods> recommendGoods) {
        if (recommendGoods == null || recommendGoods.isEmpty()) {
            recommendLayout.setVisibility(View.GONE);
        } else {
            recommendLayout.setVisibility(View.VISIBLE);
            RecommendGoodsAdapter adapter = new RecommendGoodsAdapter(this, recommendGoods);
            recommendList.setAdapter(adapter);
        }
    }

    @OptionsItem(R.id.action_shopping)
    void goShopping() {
        Logger.i("点击购物车");
        ShoppingCartActivity_.intent(this).start();
    }

    @OptionsItem(R.id.action_share)
    void doShare() {
        Logger.i("点击分享");
    }


    @Click(R.id.favorite)
    void doFavorite() {
        if (session == null) return;
        //TODO 收藏或取消收藏
        doFavoriteSuccess();
    }

    void doFavoriteSuccess() {
        Logger.i("favorite :" + session.isFavorite());

        session.setFavorite(!session.isFavorite());
        showToast(session.isFavorite() ? "收藏成功" : "取消收藏成功");
        Logger.i("2 favorite :" + session.isFavorite());
        EventBus.getDefault().post(new FavoriteEvent(session.isFavorite(), session.getGoods()));
        updateFavoriteUI();
    }

    @Click
    void addShoppingCart() {
        //TODO 点击到购物车
        showBuySelectView();
    }

    @Click
    void buy() {
        //购买
        showBuySelectView();
    }

    private void showBuySelectView() {
        final BuySelectGoodsView selectGoodsView = BuySelectGoodsView_.build(this);
        selectGoodsView.bindData(session);

        final Dialog dialog = new DialogBuilder(this).contentView(selectGoodsView).build();
        dialog.show();

        selectGoodsView.setOnBuySelectGoodsListener(new BuySelectGoodsView.OnBuySelectGoodsListener() {
            @Override
            public void onClose() {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

}
