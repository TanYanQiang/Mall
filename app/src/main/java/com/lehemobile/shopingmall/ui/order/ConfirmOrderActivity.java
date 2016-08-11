package com.lehemobile.shopingmall.ui.order;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.Address;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.model.ShoppingSession;
import com.lehemobile.shopingmall.session.ConfirmOrderSession;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.user.address.AddressListsActivity;
import com.lehemobile.shopingmall.ui.user.address.AddressListsActivity_;
import com.tgh.devkit.core.text.SpannableStringHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyq on 7/8/16.
 */
@EActivity(R.layout.activity_confirm_order)
public class ConfirmOrderActivity extends BaseActivity {

    private static final int REQUEST_CHOOSE_ADDRESS_CODE = 1;

    @Extra
    ArrayList<Goods> goodsList;

    @ViewById
    TextView name;
    @ViewById
    TextView mobile;
    @ViewById(R.id.address)
    TextView addressTv;

    @ViewById
    TextView tv_empty;

    @ViewById
    LinearLayout goodsInfo;

    @ViewById
    TextView orderCountPrice;
    @ViewById
    TextView orderNumber;

    @ViewById(R.id.totalPrice)
    TextView totalPriceTv;
    @ViewById
    TextView freight;
    @ViewById
    EditText orderDesc;

    private ConfirmOrderSession session = new ConfirmOrderSession();

    @AfterViews
    void init() {
        Order order = new Order();
        order.setOrderNumber("SH201606231042136362");
        order.setGoodsList(goodsList);

        session.setOrder(order);
        session.setAddress(ConfigManager.getUserDefaultAddress());

        updateGoodsInfo(session.getOrder().getGoodsList());
        updateAddressUI(session.getAddress());

        calculateTotalPrice();

        orderNumber.setText(getString(R.string.label_order_number, order.getOrderNumber()));

        updateFreight(0);
    }


    private void updateGoodsInfo(List<Goods> goodsList) {
        goodsInfo.removeAllViews();
        for (Goods goods : goodsList) {
            ConfirmOrderGoodsItemView view = ConfirmOrderGoodsItemView_.build(this);
            view.bindData(goods);
            view.setOnChangedListener(new ConfirmOrderGoodsItemView.OnChangedListener() {
                @Override
                public void onChanged(Goods goods) {
                    List<Goods> data = session.getOrder().getGoodsList();
                    int indexOf = data.indexOf(goods);
                    data.set(indexOf, goods);
                    session.getOrder().setGoodsList(data);
                    calculateTotalPrice();
                }
            });
            goodsInfo.addView(view);
        }
    }

    private void calculateTotalPrice() {
        List<Goods> data = session.getOrder().getGoodsList();
        double totalPrice = 0;
        int count = 0;
        for (Goods goods : data) {
            totalPrice += goods.getPrice() * goods.getBuyCount();
            count += 1;
        }
        updateTotalPrice(count, totalPrice);
    }

    private void updateTotalPrice(int count, double totalPrice) {
        String price = Strings.doubleTrans(totalPrice);
        String info = getResources().getString(R.string.label_order_count_price, count, price);
        new SpannableStringHelper(info)
                .relativeSize("￥" + price, 1.3f)
                .foregroundColor("￥" + price, getResources().getColor(R.color.text_color_lv1))
                .attachToTextView(orderCountPrice);

        totalPriceTv.setText("￥" + price);
    }

    private void updateFreight(double freightPrice) {
        freight.setText("￥" + Strings.doubleTrans(freightPrice));
    }


    private void updateAddressUI(Address address) {
        if (address == null) {
            name.setVisibility(View.GONE);
            mobile.setVisibility(View.GONE);
            addressTv.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);

        } else {
            session.setAddress(address);

            name.setVisibility(View.VISIBLE);
            mobile.setVisibility(View.VISIBLE);
            addressTv.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);

            name.setText(address.getName());
            mobile.setText(address.getMobile());
            addressTv.setText(address.getFullAddress());
        }
    }

    @Click(R.id.addressView)
    void chooseAddress() {
        //// TODO: 7/8/16 选择收货地址
        AddressListsActivity_.intent(this).type(AddressListsActivity.TYPE_CHOOSE_ADDRESS).startForResult(REQUEST_CHOOSE_ADDRESS_CODE);
    }

    @OnActivityResult(REQUEST_CHOOSE_ADDRESS_CODE)
    void chooseAddressResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Address address = (Address) data.getSerializableExtra(AppConfig.Extra);
            updateAddressUI(address);
        }
    }

    @Click(R.id.submit)
    void submit() {
        if (session.getAddress() == null) {
            showToast("请选择收货地址");
            return;
        }
        Order order = new Order();
        OrderPayActivity_.intent(this).order(order).start();
    }


}
