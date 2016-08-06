package com.lehemobile.shopingmall.ui.shoppingCart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.model.ShoppingSession;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.goods.GoodsDetailActivity_;
import com.lehemobile.shopingmall.utils.pageList.PageListHelper;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.text.SpannableStringHelper;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 27/7/16.
 */
@EActivity(R.layout.activity_shopping)
public class ShoppingCartActivity extends BaseActivity {
    @ViewById
    TextView goPay;

    @ViewById
    CheckBox selectAll;

    @ViewById
    TextView totalPrice;

    @ViewById
    PullToRefreshListView listView;
    @ViewById
    TextView tv_empty;
    private PageListHelper<ShoppingSession> pageListHelper;


    @AfterViews
    void init() {

        pageListHelper = new PageListHelper<ShoppingSession>(listView) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseListAdapter<ShoppingSession> newAdapter(List<ShoppingSession> data) {
                return new ShoppingAdapter(ShoppingCartActivity.this, data);
            }

            @Override
            public void onItemClicked(int position, ShoppingSession session) {
                //TODO 查看商品详情
                GoodsDetailActivity_.intent(ShoppingCartActivity.this).goodsId(session.getGoods().getId()).start();
//                Intent intent = new Intent(FavoriteActivity.this, GoodsDetailScrollingActivity.class);
//                startActivity(intent);
            }
        };
        pageListHelper.setEmptyView(tv_empty);
        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();

    }

    private void load(int page, int pageCount) {
        //TODO 调用接口加载数据
        List<ShoppingSession> sessionList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            ShoppingSession session = new ShoppingSession();
            Goods goods = new Goods();
            goods.setId(i + 1);
            goods.setName("气垫BB霜 保湿遮瑕美白粉底液替换套盒 保湿遮瑕美白粉底 " + i);
            goods.setPrice(100 * i);
            goods.setThumbnail("http://c.vpimg1.com/upcb/2016/07/28/175/03023995.jpg");
            if (i % 3 == 0) {
                goods.setThumbnail("http://c.vpimg1.com/upcb/2016/07/29/109/31408761.jpg");
            } else if (i % 2 == 0) {
                goods.setThumbnail("http://c.vpimg1.com/upcb/2016/07/19/32/16159711.jpg");
            } else if (i % 5 == 0) {
                goods.setThumbnail("http://img30.360buyimg.com/da/jfs/t2980/287/2188733782/207324/ce09cc13/579ff5d1Ndc188521.jpg");
            }
            session.setGoods(goods);

            sessionList.add(session);
        }
        pageListHelper.onLoadSuccess(sessionList);

    }

    private class ShoppingAdapter extends BaseListAdapter<ShoppingSession> {

        public ShoppingAdapter(Context context, Collection<? extends ShoppingSession> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, ShoppingSession item) {
            ShoppingItemView itemView = (ShoppingItemView) convertView;
            itemView.setOnChangedListener(new ShoppingItemView.OnChangedListener() {
                @Override
                public void onChanged(ShoppingSession session) {
                    calculateTotalPrice();
                }
            });
            itemView.bindData(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return ShoppingItemView_.build(context);
        }
    }

    private void calculateTotalPrice() {
        ArrayList<ShoppingSession> data = pageListHelper.getData();
        double totalPrice = 0;
        int count = 0;
        for (ShoppingSession session : data) {
            if (session.isSelected()) {
                Goods goods = session.getGoods();
                totalPrice += goods.getPrice() * goods.getBuyCount();
                count += 1;
            }
        }
        updateTotalPrice(totalPrice);
        updateCount(count);
    }

    private void updateTotalPrice(double price) {
        String info = "￥" + Strings.doubleTrans(price);
        new SpannableStringHelper(info).relativeSize(Strings.doubleTrans(price), 1.3f).attachToTextView(totalPrice);
    }

    private void updateCount(int count) {
        goPay.setText("去结算(" + count + ")");
    }

    @CheckedChange(R.id.selectAll)
    void onSelectAll(boolean isChecked, CompoundButton button) {
        if (pageListHelper == null) return;
        ArrayList<ShoppingSession> data = pageListHelper.getData();
        if (data == null || data.isEmpty()) return;
        for (ShoppingSession session : data) {
            session.setSelected(isChecked);
        }
        pageListHelper.getAdapter().reset(data);
        calculateTotalPrice();
    }

    @Click
    void goPay() {
        //// TODO: 6/8/16 去支付
        List<Goods> goodsList = new ArrayList<>();
        if (pageListHelper == null) return;
        ArrayList<ShoppingSession> data = pageListHelper.getData();
        if (data == null || data.isEmpty()) return;
        for (ShoppingSession session : data) {
            if (session.isSelected()) {
                goodsList.add(session.getGoods());
            }
        }
        if (goodsList.isEmpty()) {
            showToast("你还没有选择任何商品哦(⊙o⊙)！");
            return;
        }
        //
        Logger.i("去付款");
    }

}
