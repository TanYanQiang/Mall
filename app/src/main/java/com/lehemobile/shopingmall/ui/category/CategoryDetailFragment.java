package com.lehemobile.shopingmall.ui.category;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.CategoryApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.model.CategoryDetailSession;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 4/8/16.
 */
@EFragment(R.layout.fragment_category_detail)
public class CategoryDetailFragment extends BaseFragment {
    @ViewById
    TextView label;

    @ViewById
    GridView gridView;
    @ViewById
    ContentLoadingProgressBar progressBar;

    @FragmentArg
    int categoryId;

    @AfterViews
    void init() {

    }


    public void loadData(int categoryId) {
        progressBar.setVisibility(View.VISIBLE);

        BaseRequest<List<Category>> request = CategoryApi.getCategories(categoryId, new Response.Listener<List<Category>>() {
            @Override
            public void onResponse(List<Category> response) {
                CategoryDetailSession session = new CategoryDetailSession();
                session.setDetails(response);
                updateUI(session.getDetails());
                updateTips(session.getTips());
            }
        }, new AppErrorListener(getContext()) {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
        VolleyHelper.execute(request);
    }

    private void updateTips(String tips) {
        label.setVisibility(Strings.isNullOrEmpty(tips) ? View.GONE : View.VISIBLE);
        label.setText(tips);
    }

    private void updateUI(List<Category> details) {
        progressBar.setVisibility(View.GONE);
        final CategoryDetailAdapter adapter = new CategoryDetailAdapter(getContext(), details);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof Category) {
                    //TODO 查看类别下面的商品列表
                    Logger.i("查看类别下面的商品列表:" + ((Category) item).getCategoryName());
                    CategoryGoodsListActivity_.intent(getContext()).category((Category) item).start();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancel();
    }

    private class CategoryDetailAdapter extends BaseListAdapter<Category> {

        public CategoryDetailAdapter(Context context, Collection<? extends Category> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Category item) {
            CategoryDetailItemView view = (CategoryDetailItemView) convertView;
            view.updateUI(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return CategoryDetailItemView_.build(context);
        }
    }
}
