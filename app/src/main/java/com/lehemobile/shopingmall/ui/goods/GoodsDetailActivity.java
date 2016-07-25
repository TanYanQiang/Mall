package com.lehemobile.shopingmall.ui.goods;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.BaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by tanyq on 24/7/16.
 */
@EActivity(R.layout.activity_goods_detail)
public class GoodsDetailActivity extends BaseActivity {
    @Extra
    int goodsId;
}
