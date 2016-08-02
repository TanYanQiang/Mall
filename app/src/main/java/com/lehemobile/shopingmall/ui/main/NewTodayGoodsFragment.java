package com.lehemobile.shopingmall.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.session.NewTodayGoodsSession;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.goods.GoodsDetailActivity_;
import com.lehemobile.shopingmall.utils.pageList.PageListHelper;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
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
    private NewTodayGoodsSession todayGoodsSession;
    private NewTodayGoodsHeaderView newTodayGoodsHeaderView;


    @AfterViews
    void init() {

        newTodayGoodsHeaderView = NewTodayGoodsHeaderView_.build(getActivity());
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

        todayGoodsSession = new NewTodayGoodsSession();


        //TODO 调用接口加载数据
        List<Goods> bannerData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Goods goods = new Goods();
            goods.setId(i + 1);
            goods.setName("气垫BB霜 保湿遮瑕美白粉底液替换套盒 保湿遮瑕美白粉底 " + i);
            goods.setPrice(100 * i);
            String thumbnail = "http://img20.360buyimg.com/da/jfs/t3037/169/247470885/188086/ba507386/579821b0Nab1cf0d7.jpg";
            if (i % 5 == 0) {
                thumbnail = "http://img12.360buyimg.com/da/jfs/t2590/105/3491337740/142976/48526944/57906678Nc515f1b0.jpg";
            } else if (i % 2 == 0) {
                thumbnail = "http://c.vpimg1.com/upcb/2016/07/29/104/34149634.jpg";
            } else if (i % 3 == 0) {
                thumbnail = "http://d.vpimg1.com/upcb/2016/07/29/94/04117816.jpg";
            } else if (i % 4 == 0) {
                thumbnail = "http://c.vpimg1.com/upcb/2016/07/29/190/32087675.jpg";
            }
            goods.setThumbnail(thumbnail);
            bannerData.add(goods);
        }
        todayGoodsSession.setBannersData(bannerData);

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
        todayGoodsSession.setNewData(goodsList);

        updateUI();
    }

    private void updateUI() {
        pageListHelper.onLoadSuccess(todayGoodsSession.getNewData());

        newTodayGoodsHeaderView.bindData(todayGoodsSession);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.i("Detach");
        if(newTodayGoodsHeaderView != null){
            newTodayGoodsHeaderView.onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
