package com.lehemobile.shopingmall.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.common.NavigationView;
import com.tgh.devkit.viewpager.BaseViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @ViewById
    Toolbar toolbar;

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
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setOnNavigationItemSelectedListener(this);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
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

    private class SectionPagerAdapter extends FragmentPagerAdapter {


        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "中文 " + position;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        public static PlaceholderFragment newInstance(int position) {
            PlaceholderFragment placeholderFragment = new PlaceholderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            placeholderFragment.setArguments(bundle);
            return placeholderFragment;
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int position = getArguments().getInt("position");


            ImageView imageView = new ImageView(getActivity());

            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            String imageUrl = "http://s.cn.bing.net/az/hprichbg/rb/TerracesSunrise_ZH-CN11993554223_1920x1080.jpg";
            if (position % 2 == 0) {
                imageUrl = "http://a.vpimg4.com/upload/merchandise/pdc/411/873/1033652364713873411/2/8712400110020-6.jpg";
            }
            Glide.with(getActivity())
                    .load(imageUrl)
//                    .bitmapTransform((position % 2 == 0 ? new CropCircleTransformation(getActivity()) : new CropSquareTransformation(getActivity())))
                    .into(imageView);
            return imageView;
        }
    }
}
