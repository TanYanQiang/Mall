package com.lehemobile.shopingmall.ui.category;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.model.CategoryDetailSession;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.Strings;
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
        loadData(categoryId);
    }


    public void loadData(int categoryId) {
        progressBar.setVisibility(View.VISIBLE);

        //// TODO: 4/8/16  加载数据
        CategoryDetailSession session = new CategoryDetailSession();
        session.setTips("为你推荐");
        List<Category> details = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = new Category();
            category.setCategoryId(i);
            category.setCategoryName(categoryId + "美容" + i);
            category.setCategoryImage("http://a.vpimg3.com/upload/merchandise/pdcvis/2016/07/21/40/b9ee3af9-b518-4f9f-8ea7-c04163189051.jpg");
            details.add(category);
        }
        session.setDetails(details);

        updateUI(session.getDetails());
        updateTips(session.getTips());
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
