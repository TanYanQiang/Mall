package com.lehemobile.shopingmall.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.Region;
import com.lehemobile.shopingmall.ui.common.NavigationView;
import com.lehemobile.shopingmall.ui.main.MainGoodsFragment_;
import com.lehemobile.shopingmall.ui.main.NewTodayGoodsFragment_;
import com.lehemobile.shopingmall.ui.user.address.ChooseRegionActivity;
import com.lehemobile.shopingmall.ui.user.address.ChooseRegionActivity_;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.viewpager.BaseViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CHOOSE_REGION_CODE = 1;
    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView region;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;


    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @ViewById
    BaseViewPager viewPager;

    @AfterViews
    void init() {

        initActionBar(toolbar);
        updateRegionUI();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setOnNavigationItemSelectedListener(this);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void updateRegionUI() {
        Region region = ConfigManager.getRegion();
        String regionName = "北京";
        if (region != null) {
            regionName = region.getName();
        }
        this.region.setText(regionName);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


    @Click(R.id.region)
    void chooseRegion() {
        Logger.i("click choose chooseRegion");
        ChooseRegionActivity_.intent(this)
                .type(ChooseRegionActivity.TYPE_CHOOSE_REGION)
                .startForResult(REQUEST_CHOOSE_REGION_CODE);
    }

    @OptionsItem(R.id.action_search)
    void doSearch() {
        Logger.i(" click search ");
    }

    String[] titles = new String[]{"今日上线", "化妆", "护肤", "美体", "即将上线"};

    private class SectionPagerAdapter extends FragmentStatePagerAdapter {


        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (position == 0) {
                return NewTodayGoodsFragment_.builder().build();
            } else {
                return MainGoodsFragment_.builder().categoryId(position).build();
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @OnActivityResult(REQUEST_CHOOSE_REGION_CODE)
    void chooseRegionResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            updateRegionUI();
        }
    }
}
