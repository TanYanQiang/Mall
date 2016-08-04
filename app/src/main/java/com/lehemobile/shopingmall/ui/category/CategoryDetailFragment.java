package com.lehemobile.shopingmall.ui.category;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.CategoryDetail;
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
        List<CategoryDetail> details = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CategoryDetail categoryDetail = new CategoryDetail();
            categoryDetail.setId(i);
            categoryDetail.setName(categoryId + "美容" + i);
            categoryDetail.setImageUrl("http://a.vpimg4.com/upload/category/2016/06/14/111/aabfc582-3b25-4f8a-9951-bbc516b8fe90.jpg");
            details.add(categoryDetail);
        }
        session.setDetails(details);

        updateUI(session.getDetails());
        updateTips(session.getTips());
    }

    private void updateTips(String tips) {
        label.setVisibility(Strings.isNullOrEmpty(tips) ? View.GONE : View.VISIBLE);
        label.setText(tips);
    }

    private void updateUI(List<CategoryDetail> details) {
        progressBar.setVisibility(View.GONE);
        final CategoryDetailAdapter adapter = new CategoryDetailAdapter(getContext(), details);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof CategoryDetail) {
                    //TODO 查看类别下面的商品列表
                    Logger.i("查看类别下面的商品列表:" + ((CategoryDetail) item).getName());
                }
            }
        });
    }

    private class CategoryDetailAdapter extends BaseListAdapter<CategoryDetail> {

        public CategoryDetailAdapter(Context context, Collection<? extends CategoryDetail> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, CategoryDetail item) {
            CategoryDetailItemView view = (CategoryDetailItemView) convertView;
            view.updateUI(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return CategoryDetailItemView_.build(context);
        }
    }
}
