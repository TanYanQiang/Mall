package com.lehemobile.shopingmall.ui.goods;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by tanyq on 26/7/16.
 */
@EFragment(R.layout.fragment_goods_detail_image)
public class GoodsDetailImageFragment extends BaseFragment {

    @FragmentArg
    Goods goods;
    @ViewById
    LinearLayout container;
    @ViewById
    RecyclerView recyclerView;


    @AfterViews
    void init() {
        List<String> detailImages = goods.getDetailImages();
        if (detailImages == null || detailImages.isEmpty()) return;
        container.removeAllViews();
        for (String imageUrl : detailImages) {
            GoodsDetailImageView imageView = GoodsDetailImageView_.build(getContext());
            imageView.bindData(imageUrl);
            container.addView(imageView);
        }

    }

    private void showRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        List<String> detailImages = goods.getDetailImages();
        if (detailImages == null || detailImages.isEmpty()) return;
        GoodsDetailImageAdapter adapter = new GoodsDetailImageAdapter(getContext(), goods.getDetailImages());
        recyclerView.setAdapter(adapter);
    }

}
