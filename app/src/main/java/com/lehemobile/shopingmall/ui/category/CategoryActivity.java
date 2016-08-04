package com.lehemobile.shopingmall.ui.category;

import android.view.View;
import android.widget.RadioGroup;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
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
        categoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                showCategoryDetail(radioGroup.getCheckedRadioButtonId());
            }
        });
        loadData();
    }

    private void loadData() {
        //// TODO: 4/8/16 加载类别
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Category category = new Category();
            switch (i) {
                case 0:
                    category.setCategoryName("推荐类别");
                    break;
                case 1:
                    category.setCategoryName("护肤");
                    break;
                case 2:
                    category.setCategoryName("日用");
                    break;
                default:
                    category.setCategoryName("美妆");
                    break;
            }
            category.setCategoryId(i);
            categories.add(category);
        }
        updateCategoryGroupUI(categories);
    }

    private void updateCategoryGroupUI(List<Category> categories) {
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
