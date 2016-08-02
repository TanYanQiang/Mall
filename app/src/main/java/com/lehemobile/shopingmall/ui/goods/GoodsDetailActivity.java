package com.lehemobile.shopingmall.ui.goods;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
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
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.tgh.devkit.dialog.DialogBuilder;
import com.tgh.devkit.viewpager.InfiniteViewPager;
import com.tgh.devkit.viewpager.adapter.BasePagerAdapter;
import com.tgh.devkit.viewpager.adapter.InfinitePagerAdapter;
import com.tgh.devkit.viewpager.indicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by  on 24/7/16.
 */
@EActivity(R.layout.activity_goods_detail)
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
    RadioGroup radioGroup;
    @ViewById
    RadioButton goodsDetail;
    @ViewById
    RadioButton goodsInfo;
    @ViewById
    RadioButton goodsComments;

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
        goods.setName("气垫BB霜 保湿遮瑕美白粉底液替换套盒 保湿遮瑕美白粉底 ");
        goods.setPrice(100);
        goods.setThumbnail("http://s.cn.bing.net/az/hprichbg/rb/Bittermelon_ZH-CN13629728807_1920x1080.jpg");
        List<String> images = new ArrayList<>();
        images.add("http://a.appsimg.com/upload/merchandise/pdc/293/235/7649158865156235293/0/1981126-1_710x897_80.jpg");
        images.add("http://a.appsimg.com/upload/merchandise/pdc/293/235/7649158865156235293/0/1981126-2_710x897_80.jpg");
        images.add("http://a.appsimg.com/upload/merchandise/pdc/293/235/7649158865156235293/0/1981126-3_710x897_80.jpg");
        images.add("http://a.appsimg.com/upload/merchandise/pdc/293/235/7649158865156235293/0/1981126-4_710x897_80.jpg");
        images.add("http://a.appsimg.com/upload/merchandise/pdc/293/235/7649158865156235293/0/1981126-15_710x897_80.jpg");
        goods.setImages(images);

        goods.setDetail("http://a.appsimg.com/upload/merchandise/pdc/293/235/7649158865156235293/1/1981126-6_1440x8000_90.jpg");
        session.setGoods(goods);
        session.setFavorite(true);
        updateUI();
    }

    @AfterViews
    void init() {
        initGalleryUI();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.goodsDetail:
                        updateDetailUI();
                        break;
                    case R.id.goodsInfo:
                        updateGoodsDetailInfoUI();
                        break;
                    case R.id.goodsComments:
                        updateGoodsCommentsUI();
                        break;
                }
            }
        });
        loadData();
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


    private void updateUI() {

        updateGalleryUI(session.getGoods().getImages());
        updateGoodsInfoUI();
        goodsDetail.setChecked(true);
        updateFavoriteUI();
    }

    private void updateFavoriteUI() {
        if (session == null) return;
        favorite.setText(session.isFavorite() ? "已收藏" : "收藏");
        favorite.setCompoundDrawablesWithIntrinsicBounds(0, (session.isFavorite() ? R.drawable.ic_menu_send : R.drawable.ic_menu_manage), 0, 0);
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
        goodsPrice.setText(getString(R.string.label_order_price, goods.getPrice()));

        marketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        marketPrice.getPaint().setColor(getResources().getColor(R.color.text_color_lv2));
        marketPrice.setText(getString(R.string.label_goods_detail_marketPrice, goods.getPrice()));
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
    }

    @Click
    void buy() {
        //购买
        showBuySelectView();
    }

    private void showBuySelectView() {
        BuySelectGoodsView selectGoodsView = BuySelectGoodsView_.build(this);
        selectGoodsView.bindData(session);
        Dialog dialog = new DialogBuilder(this).contentView(selectGoodsView).build();

        dialog.show();
    }
}
