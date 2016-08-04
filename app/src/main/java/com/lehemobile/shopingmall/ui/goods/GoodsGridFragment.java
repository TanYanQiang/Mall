package com.lehemobile.shopingmall.ui.goods;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.user.favorite.FavoriteActivity;
import com.lehemobile.shopingmall.utils.pageList.PageListHelper;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 4/8/16.
 */
@EFragment(R.layout.fragment_goods_grid)
public class GoodsGridFragment extends BaseFragment {

    @FragmentArg
    Category category;

    @ViewById
    PullToRefreshGridView gridView;
    private PageListHelper<Goods> pageListHelper;

    @AfterViews
    void init() {

        initGridView();
    }

    void initGridView() {

        pageListHelper = new PageListHelper<Goods>(gridView) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseListAdapter<Goods> newAdapter(List<Goods> data) {
                return new GoodsAdapter(getContext(), data);
            }

            @Override
            public void onItemClicked(int position, Goods goods) {
                //TODO 查看商品详情
                GoodsDetailActivity_.intent(getContext()).goodsId(goods.getId()).start();
            }
        };

        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();


    }

    private void load(int page, int pageCount) {
        //TODO 调用接口加载数据
        List<Goods> goodsList = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            Goods goods = new Goods();
            goods.setId(i + 1);
            goods.setName("气垫BB霜 保湿遮瑕美白粉底液替换套盒 保湿遮瑕美白粉底 " + i);
            goods.setPrice(100 * page);
            goods.setThumbnail("http://c.vpimg1.com/upcb/2016/07/28/175/03023995.jpg");
            if (i % 3 == 0) {
                goods.setThumbnail("http://c.vpimg1.com/upcb/2016/07/29/109/31408761.jpg");
            } else if (i % 2 == 0) {
                goods.setThumbnail("http://c.vpimg1.com/upcb/2016/07/19/32/16159711.jpg");
            } else if (i % 5 == 0) {
                goods.setThumbnail("http://img30.360buyimg.com/da/jfs/t2980/287/2188733782/207324/ce09cc13/579ff5d1Ndc188521.jpg");
            }
            goodsList.add(goods);
        }
        pageListHelper.onLoadSuccess(goodsList);

    }

    public void loadData(Category category) {
        this.category = category;
        initGridView();
    }

    private void updateUI(List<Goods> goodsList) {
        GoodsAdapter goodsAdapter = new GoodsAdapter(getContext(), goodsList);
        gridView.setAdapter(goodsAdapter);
    }

    private class GoodsAdapter extends BaseListAdapter<Goods> {

        public GoodsAdapter(Context context, Collection<? extends Goods> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Goods item) {
            GoodsGridItemView view = (GoodsGridItemView) convertView;
            view.updateUI(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return GoodsGridItemView_.build(context);
        }
    }

}
