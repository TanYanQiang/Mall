package com.lehemobile.shopingmall.ui.goods;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.BaseFragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

/**
 * Created by tanyq on 26/7/16.
 */
@EFragment(R.layout.fragment_goods_detail_comment_list)
public class GoodsDetailCommentListFragment extends BaseFragment {
    @FragmentArg
    Goods goods;
}
