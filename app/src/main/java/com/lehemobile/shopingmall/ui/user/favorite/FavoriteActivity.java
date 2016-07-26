package com.lehemobile.shopingmall.ui.user.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.event.CancelFavoriteEvent;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.goods.GoodsDetailActivity_;
import com.lehemobile.shopingmall.ui.goods.GoodsDetailScrollingActivity;
import com.lehemobile.shopingmall.utils.pageList.PageListHelper;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by tanyq on 23/7/16.
 */
@EActivity(R.layout.activity_favorite_list)
public class FavoriteActivity extends BaseActivity {
    @ViewById
    PullToRefreshListView listView;
    @ViewById
    TextView tv_empty;
    private PageListHelper<Goods> pageListHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @AfterViews
    void init() {

        pageListHelper = new PageListHelper<Goods>(listView) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseAdapter newAdapter(List<Goods> data) {
                return new FavoriteAdapter(FavoriteActivity.this, data);
            }

            @Override
            public void onItemClicked(int position, Goods goods) {
                //TODO 查看商品详情
                GoodsDetailActivity_.intent(FavoriteActivity.this).goodsId(goods.getId()).start();
//                Intent intent = new Intent(FavoriteActivity.this, GoodsDetailScrollingActivity.class);
//                startActivity(intent);
            }
        };
        pageListHelper.setEmptyView(tv_empty);
        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();


        //注册长按删除
        listView.getRefreshableView().setOnCreateContextMenuListener(this);
    }

    private void load(int page, int pageCount) {
        //TODO 调用接口加载数据
        List<Goods> goodsList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Goods goods = new Goods();
            goods.setId(i + 1);
            goods.setName("气垫BB霜 保湿遮瑕美白粉底液替换套盒 保湿遮瑕美白粉底 " + i);
            goods.setPrice(100 * i);
            goods.setThumbnail("http://s.cn.bing.net/az/hprichbg/rb/Bittermelon_ZH-CN13629728807_1920x1080.jpg");
            goodsList.add(goods);
        }
        pageListHelper.onLoadSuccess(goodsList);

    }

    private class FavoriteAdapter extends BaseListAdapter<Goods> {

        public FavoriteAdapter(Context context, Collection<? extends Goods> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Goods item) {
            FavoriteItemView favoriteItemView = (FavoriteItemView) convertView;
            favoriteItemView.bindData(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return FavoriteItemView_.build(context);
        }
    }

    public void onEventMainThread(CancelFavoriteEvent event) {
        Goods goods = event.getGoods();
        if (pageListHelper == null) return;

        ArrayList<Goods> data = pageListHelper.getData();
        boolean status = data.remove(goods);
        if (status) {
            showToast("取消成功");
            pageListHelper.notifyDataSetChanged();
        }
    }

}
