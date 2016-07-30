package com.lehemobile.shopingmall.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.goods.GoodsDetailActivity_;
import com.lehemobile.shopingmall.ui.user.favorite.FavoriteItemView;
import com.lehemobile.shopingmall.ui.user.favorite.FavoriteItemView_;
import com.lehemobile.shopingmall.utils.pageList.PageListHelper;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 30/7/16.
 */
@EFragment(R.layout.fragment_main_new_today)
public class NewTodayGoodsFragment extends BaseFragment {


    @ViewById
    PullToRefreshListView listView;

    private PageListHelper<Goods> pageListHelper;


    @AfterViews
    void init() {

        NewTodayGoodsHeaderView newTodayGoodsHeaderView = NewTodayGoodsHeaderView_.build(getActivity());
        listView.getRefreshableView().addHeaderView(newTodayGoodsHeaderView);


        pageListHelper = new PageListHelper<Goods>(listView) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseAdapter newAdapter(List<Goods> data) {
                return new GoodsItemAdapter(getActivity(), data);
            }

            @Override
            public void onItemClicked(int position, Goods goods) {
                if (goods == null) return;
                //TODO 查看商品详情
                GoodsDetailActivity_.intent(getActivity()).goodsId(goods.getId()).start();
//                Intent intent = new Intent(FavoriteActivity.this, GoodsDetailScrollingActivity.class);
//                startActivity(intent);
            }
        };
        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();
    }

    private void load(int page, int pageCount) {
        //TODO 调用接口加载数据
        List<Goods> goodsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Goods goods = new Goods();
            goods.setId(i + 1);
            goods.setName("气垫BB霜 保湿遮瑕美白粉底液替换套盒 保湿遮瑕美白粉底 " + i);
            goods.setPrice(100 * i);
            String thumbnail = "http://img20.360buyimg.com/da/jfs/t3037/169/247470885/188086/ba507386/579821b0Nab1cf0d7.jpg";
            if (i % 2 == 0) {
                thumbnail = "http://img12.360buyimg.com/da/jfs/t2590/105/3491337740/142976/48526944/57906678Nc515f1b0.jpg";
            } else if (i % 3 == 0) {
                thumbnail = "http://c.vpimg1.com/upcb/2016/07/29/104/34149634.jpg";
            } else if (i % 4 == 0) {
                thumbnail = "http://d.vpimg1.com/upcb/2016/07/29/94/04117816.jpg";
            } else if (i % 5 == 0) {
                thumbnail = "http://c.vpimg1.com/upcb/2016/07/29/190/32087675.jpg";
            }
            goods.setThumbnail(thumbnail);
            goodsList.add(goods);
        }
        pageListHelper.onLoadSuccess(goodsList);

    }

    private class GoodsItemAdapter extends BaseListAdapter<Goods> {

        public GoodsItemAdapter(Context context, Collection<? extends Goods> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Goods item) {
            GoodsItemView goodsItemView = (GoodsItemView) convertView;
            goodsItemView.bindData(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            GoodsItemView itemView = GoodsItemView_.build(context);
            return itemView;
        }
    }


}
