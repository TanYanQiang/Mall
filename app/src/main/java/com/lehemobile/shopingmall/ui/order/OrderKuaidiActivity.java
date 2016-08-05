package com.lehemobile.shopingmall.ui.order;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.model.Kuaidi;
import com.lehemobile.shopingmall.model.KuaidiItem;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.view.OrderGoodsInfo;
import com.lehemobile.shopingmall.ui.view.OrderGoodsInfo_;
import com.lehemobile.shopingmall.ui.view.Picasso.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;
import com.tgh.devkit.core.text.SpannableStringHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流信息
 * Created by  on 22/7/16.
 */
@EActivity(R.layout.activity_order_kuaidi)
public class OrderKuaidiActivity extends BaseActivity {
    @Extra
    Order order;

    @ViewById
    TextView kuaidiNumber;
    @ViewById
    TextView kuaidiStatus;
    @ViewById
    TextView lastDate;
    @ViewById
    TextView orderNumber;

    @ViewById(R.id.goodsContainer)
    LinearLayout goodsContainer;

    @ViewById
    TextView orderCountPrice;


    @ViewById
    LinearLayout kuaidiContainer;

    private void loadData() {
        //TODO 通过接口获取快递信息
        showLoading(R.string.loading);
        Kuaidi kuidi = new Kuaidi();
        kuidi.setNumber("1231231231232");
        kuidi.setStatus("运输途中");
        kuidi.setLastUpdateTime("2016-08-04");

        List<KuaidiItem> kuidiItems = new ArrayList<>();
        KuaidiItem kuidiItem = new KuaidiItem();
        kuidiItem.setTime("2016-8-4 09:00");
        kuidiItem.setDesc("快递已到达 杭州转运中心");
        kuidiItems.add(kuidiItem);
        for (int i = 0; i < 10; i++) {
            KuaidiItem kuidiItem2 = new KuaidiItem();
            kuidiItem2.setTime("2016-8-1 09:00:" + i);
            kuidiItem2.setDesc("商家通知快递公司揽件");
            kuidiItems.add(kuidiItem2);
        }
        kuidi.setItems(kuidiItems);

        updateKuaidiInfo(kuidi);
    }

    @AfterViews
    void init() {
        if (order == null) return;
        String price = Strings.doubleTrans(order.getTotalPrice());
        String info = getResources().getString(R.string.label_order_count_price, order.getCount(), price);
        new SpannableStringHelper(info)
                .relativeSize("￥" + price, 1.3f)
                .foregroundColor("￥" + price, getResources().getColor(R.color.text_color_lv1))
                .attachToTextView(orderCountPrice);
        updateOrderInfo(order);


        loadData();

    }

    private void updateOrderInfo(Order order) {

        orderNumber.setText(getString(R.string.label_order_number, order.getOrderNumber()));
        updateGoods(order.getGoodsList());
    }
    private void updateGoods(List<Goods> goodsList) {
        goodsContainer.removeAllViews();
        for (int i = 0; i < goodsList.size(); i++) {
            OrderGoodsInfo view = OrderGoodsInfo_.build(this);
            view.bindData(goodsList.get(i));
            goodsContainer.addView(view);
        }
    }
    private void updateKuaidiInfo(Kuaidi kuaidi) {
        dismissLoading();
        kuaidiNumber.setText(getString(R.string.label_kuaidi_number,kuaidi.getNumber()));
        kuaidiStatus.setText(kuaidi.getStatus());
        lastDate.setText(kuaidi.getLastUpdateTime());
        kuaidiContainer.removeAllViews();
        for (int i = 0; i < kuaidi.getItems().size(); i++) {
            KuaidiItem item = kuaidi.getItems().get(i);

            View view = getLayoutInflater().inflate(R.layout.view_kuaidi_info_item, kuaidiContainer, false);

            TextView titleTv = (TextView) view.findViewById(R.id.title);
            TextView timeTv = (TextView) view.findViewById(R.id.time);
            ImageView image = (ImageView) view.findViewById(R.id.image1);
            View topLine = view.findViewById(R.id.topLine);
            View bottomLine = view.findViewById(R.id.bottomLine);
            Space topSpace= (Space) view.findViewById(R.id.topSpace);

            topLine.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
            image.setImageResource(i == 0 ? R.mipmap.ic_kuaidi_item_1 : R.mipmap.ic_kuaidi_item_2);

            int textColor = i ==0 ? R.color.colorAccent : R.color.text_color_lv4;
            timeTv.setTextColor(getResources().getColor(textColor));
            titleTv.setTextColor(getResources().getColor(textColor));
            int height = i==0 ? R.dimen.margin_8 :R.dimen.activity_vertical_margin;
            topSpace.setMinimumHeight(getResources().getDimensionPixelOffset(height));

            titleTv.setText(item.getDesc());
            timeTv.setText(item.getTime());


            kuaidiContainer.addView(view);
        }
    }

}
