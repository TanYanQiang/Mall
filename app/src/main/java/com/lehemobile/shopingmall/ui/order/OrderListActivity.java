package com.lehemobile.shopingmall.ui.order;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.tgh.devkit.viewpager.BaseViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;


/**
 * 订单列表
 * Created by tanyq on 21/7/16.
 */
@EActivity(R.layout.activity_order_list)
public class OrderListActivity extends BaseActivity {
    @Extra
    int type;


    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @ViewById
    BaseViewPager viewPager;

    String[] tabs = {"全部", "待付款", "待发货", "待收货", "已完成", "待评价"};


    @AfterViews
    void init() {
        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(type % tabs.length);
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {


        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return OrderListFragment_.builder().type(position).build();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }


}
