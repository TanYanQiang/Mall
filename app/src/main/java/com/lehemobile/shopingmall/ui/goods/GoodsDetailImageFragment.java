package com.lehemobile.shopingmall.ui.goods;

import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 26/7/16.
 */
@EFragment(R.layout.fragment_goods_detail_image)
public class GoodsDetailImageFragment extends BaseFragment {

    @FragmentArg
    Goods goods;
    @ViewById
    ImageView imageView;

    @ViewById
    ProgressBar progress;

    @AfterViews
    void init(){
        Picasso.with(getContext()).load(goods.getDetail()).into(imageView);
    }

}
