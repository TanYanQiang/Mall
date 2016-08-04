package com.lehemobile.shopingmall.ui.category;

import android.support.design.widget.TabLayout;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.goods.GoodsGridFragment;
import com.lehemobile.shopingmall.ui.goods.GoodsGridFragment_;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyq on 4/8/16.
 */
@EActivity(R.layout.activity_category_goods_list)
public class CategoryGoodsListActivity extends BaseActivity {
    @ViewById
    TabLayout tabs;

    @Extra
    Category category;
    private GoodsGridFragment gridFragment;

    @AfterViews
    void init() {
        setTitle(category.getCategoryName());

        loadCategories();

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Logger.i("onTabSelected tab:" + tab.getText());
                Object tag = tab.getTag();
                if (tag != null && tag instanceof Category) {
                    loadGoods((Category) tag);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Category category = new Category();
            category.setCategoryName("面膜");
            categories.add(category);
        }
        updateCategoryTabs(categories);
    }

    private void updateCategoryTabs(List<Category> categories) {

        Category allCategory = new Category();
        allCategory.setCategoryId(-1);
        allCategory.setCategoryName("全部分类");
        categories.add(0, allCategory);

        for (Category category : categories) {
            TabLayout.Tab tab = tabs.newTab().setText(category.getCategoryName()).setTag(category);
            tabs.addTab(tab);
        }
        loadGoods(allCategory);
    }

    private void loadGoods(Category category) {
        if (gridFragment == null) {
            gridFragment = GoodsGridFragment_.builder().category(category).build();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, gridFragment)
                    .commit();
        } else {
            gridFragment.loadData(category);
        }

    }

}
