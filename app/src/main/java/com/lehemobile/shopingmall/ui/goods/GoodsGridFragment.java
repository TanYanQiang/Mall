package com.lehemobile.shopingmall.ui.goods;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.GoodsApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.user.favorite.FavoriteActivity;
import com.lehemobile.shopingmall.utils.VolleyHelper;
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
 * Created by tanyq on 4/8/16.
 */
@EFragment(R.layout.fragment_goods_grid)
public class GoodsGridFragment extends BaseFragment {

    @FragmentArg
    Category category;

    @ViewById
    PullToRefreshGridView gridView;
    @ViewById
    TextView tv_empty;

    @ViewById
    ContentLoadingProgressBar progress;

    private PageListHelper<Goods> pageListHelper;

    @AfterViews
    void init() {

        initGridView();
    }

    void initGridView() {

        pageListHelper = new PageListHelper<Goods>(gridView) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseListAdapter<Goods> newAdapter(List<Goods> data) {
                return new GoodsAdapter(getContext(), data);
            }

            @Override
            public void onItemClicked(int position, Goods goods) {
                //TODO 查看商品详情
                GoodsDetailActivity_.intent(getContext()).goodsId(goods.getId()).start();
            }
        };

        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();
        pageListHelper.setEmptyView(tv_empty);
    }

    private void load(int page, int pageCount) {
        if (page == 1) {
            progress.show();
        }
        BaseRequest<List<Goods>> request = GoodsApi.getGoodsList(category.getCategoryId(), page, pageCount, new Response.Listener<List<Goods>>() {
            @Override
            public void onResponse(List<Goods> response) {
                progress.hide();
                pageListHelper.onLoadSuccess(response);
            }
        }, new AppErrorListener(getActivity()) {
            @Override
            public void onError(int code, String msg) {
                progress.hide();
                super.onError(code, msg);
                pageListHelper.onLoadError(msg);
            }
        });
        VolleyHelper.execute(request);


    }

    public void loadData(Category category) {
        this.category = category;
        initGridView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancel();
    }

    private class GoodsAdapter extends BaseListAdapter<Goods> {

        public GoodsAdapter(Context context, Collection<? extends Goods> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Goods item) {
            GoodsGridItemView view = (GoodsGridItemView) convertView;
            view.updateUI(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return GoodsGridItemView_.build(context);
        }
    }

}
