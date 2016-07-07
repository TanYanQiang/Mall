package com.lehemobile.shopingmall.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;
import com.tgh.devkit.viewpager.BaseViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

@EActivity(R.layout.activity_main2)
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        navigationView.setNavigationItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //TODO
            LoginActivity_.intent(this).start();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 500));
            String imageUrl = "http://s.cn.bing.net/az/hprichbg/rb/TerracesSunrise_ZH-CN11993554223_1920x1080.jpg";
            if (position % 2 == 0) {
                imageUrl = "http://s.cn.bing.net/az/hprichbg/rb/ReichstagDome_ZH-CN9358724121_1920x1080.jpg";
            }
            Glide.with(getActivity())
                    .load(imageUrl)
                    .bitmapTransform((position % 2 == 0 ? new CropCircleTransformation(getActivity()) : new CropSquareTransformation(getActivity())))
                    .into(imageView);
            return imageView;
        }
    }
}
