package com.lehemobile.shopingmall.ui.category;

import android.support.design.widget.TabLayout;

import com.android.volley.Response;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.CategoryApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.Category;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.goods.GoodsGridFragment;
import com.lehemobile.shopingmall.ui.goods.GoodsGridFragment_;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.DebugLog;

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

        gridFragment = GoodsGridFragment_.builder().build();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, gridFragment)
                .commit();


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
        showLoading("正在加载数据....");
        BaseRequest<List<Category>> request = CategoryApi.getCategories(category.getCategoryId(), new Response.Listener<List<Category>>() {
            @Override
            public void onResponse(List<Category> categories) {
                updateCategoryTabs(categories);
                dismissLoading();
            }
        }, new AppErrorListener(this));

        VolleyHelper.execute(request);

    }

    private void updateCategoryTabs(List<Category> categories) {

        Category allCategory = new Category();
        allCategory.setCategoryId(category.getCategoryId());
        allCategory.setCategoryName("全部分类");
        categories.add(0, allCategory);

        for (Category category : categories) {
            TabLayout.Tab tab = tabs.newTab().setText(category.getCategoryName()).setTag(category);
            tabs.addTab(tab);
        }
    }

    private void loadGoods(Category category) {
        if (gridFragment == null) {
            gridFragment = GoodsGridFragment_.builder().build();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, gridFragment)
                    .commit();
        }
        boolean added = gridFragment.isAdded();
        DebugLog.i("gridFragment.isAdded():" + added);
        if (gridFragment.isAdded()) {
            gridFragment.loadData(category);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gridFragment = null;
    }
}
