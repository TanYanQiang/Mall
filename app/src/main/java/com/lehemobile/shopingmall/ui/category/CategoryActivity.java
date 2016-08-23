package com.lehemobile.shopingmall.ui.category;

import android.widget.RadioGroup;

import com.android.volley.Response;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.CategoryApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.VolleyHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 分类列表
 * Created by tanyq on 31/7/16.
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends BaseActivity {
    @ViewById
    RadioGroup categoryGroup;

    private CategoryDetailFragment fragment = null;

    @AfterViews
    void init() {
        fragment = CategoryDetailFragment_.builder().build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        categoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                showCategoryDetail(radioGroup.getCheckedRadioButtonId());
            }
        });
        loadData();
    }

    private void loadData() {
        showLoading("正在加载数据...");
        BaseRequest<List<Category>> request = CategoryApi.getCategories(0, new Response.Listener<List<Category>>() {
            @Override
            public void onResponse(List<Category> categories) {
                updateCategoryGroupUI(categories);
            }
        }, new AppErrorListener(this));
        VolleyHelper.execute(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancel();
    }

    private void updateCategoryGroupUI(List<Category> categories) {
        dismissLoading();
        categoryGroup.removeAllViews();
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            CategoryItemView view = CategoryItemView_.build(this);
            view.updateUI(category);
            view.setId(category.getCategoryId());
            view.setChecked(i == 0);
            categoryGroup.addView(view);
        }
    }

    private void showCategoryDetail(int categoryId) {
        if (fragment == null) {
            fragment = CategoryDetailFragment_.builder().categoryId(categoryId).build();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } else {
            fragment.loadData(categoryId);
        }
    }
}
