package com.lehemobile.shopingmall.ui.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.ui.BaseFragment;
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
 * Created by tanyq on 21/7/16.
 */
@EFragment(R.layout.fragment_order_list)
public class OrderListFragment extends BaseFragment {

    @FragmentArg
    int type;

    @ViewById
    PullToRefreshListView rlv_orders;
    @ViewById
    TextView tv_empty;
    private PageListHelper<Order> pageListHelper;

    @AfterViews
    void init() {


        pageListHelper = new PageListHelper<Order>(rlv_orders) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseListAdapter<Order> newAdapter(List<Order> data) {
                return new OrderAdapter(getActivity(), data);
            }

            @Override
            public void onItemClicked(int position, Order order) {
                OrderDetailActivity_.intent(getContext()).order(order).start();
            }
        };
        pageListHelper.setEmptyView(tv_empty);
        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();
    }

    private void load(int page, int pageCount) {
        //TODO 调用接口加载数据
        List<Order> orders = new ArrayList<>();
        //模拟数据
        List<Goods> goodsList = new ArrayList<>();


        List<Goods> goodsList2 = new ArrayList<>();

        Goods goods = new Goods();
        goods.setId(1);
        goods.setName("气垫BB霜 保湿遮瑕美白粉底液替换套盒");
        goods.setPrice(100);
        goods.setThumbnail("http://c.vpimg1.com/upcb/2016/07/28/175/03023995.jpg");
        goods.setBuyCount(1);
        goodsList.add(goods);
        Goods goods2 = new Goods();
        goods2.setId(2);
        goods2.setName("气垫BB霜 保湿遮瑕美白粉底液替换套盒");
        goods2.setPrice(200);
        goods2.setBuyCount(3);
        goods2.setThumbnail("http://c.vpimg1.com/upcb/2016/07/19/32/16159711.jpg");
        goodsList.add(goods2);

        goodsList2.add(goods);

        for (int i = 0; i < 30; i++) {

            //待付款订单
            if (type == 0 || type == 1) {

                Order order1 = new Order();
                order1.setGoodsList(i ==1 ? goodsList : goodsList2);
                order1.setId(10);
                order1.setOrderNumber("SH201606231042136362");
                order1.setStatus(Order.STATUS_WATING_PAY);
                order1.setStatusDesc("待付款");
                order1.setCount(2);
                order1.setTotalPrice(100);
                orders.add(order1);
            }
            //待发货
            if (type == 0 || type == 2) {
                Order order1 = new Order();
                order1.setGoodsList(i ==2 ? goodsList : goodsList2);
                order1.setId(11);
                order1.setOrderNumber("SH201606231042136362");
                order1.setStatus(Order.STATUS_WATING_DELIVER_GOODS);
                order1.setStatusDesc("待发货");
                order1.setCount(1);
                order1.setTotalPrice(100);
                orders.add(order1);
            }
            //待收货
            if (type == 0 || type == 3) {

                Order order1 = new Order();
                order1.setGoodsList(i ==1 ? goodsList : goodsList2);
                order1.setId(13);
                order1.setOrderNumber("SH201606231042136362");
                order1.setStatus(Order.STATUS_WATING_RECEIPT_GOODS);
                order1.setStatusDesc("待收货");
                order1.setCount(1);
                order1.setTotalPrice(100);
                orders.add(order1);
            }
            //待收货
            if (type == 0 || type == 4) {
                Order order1 = new Order();
                order1.setGoodsList(i ==1 ? goodsList : goodsList2);
                order1.setId(13);
                order1.setOrderNumber("SH201606231042136362");
                order1.setStatus(Order.STATUS_COMPLETED);
                order1.setStatusDesc("已完成");
                order1.setCount(1);
                order1.setTotalPrice(100);
                orders.add(order1);
            }
        }
        pageListHelper.onLoadSuccess(orders);
    }

    private class OrderAdapter extends BaseListAdapter<Order> {

        public OrderAdapter(Context context, Collection<? extends Order> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Order item) {
            OrderListItemView itemView = (OrderListItemView) convertView;
            itemView.bindData(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return OrderListItemView_.build(context);
        }
    }

}
