package com.lehemobile.shopingmall.ui.user.address;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.event.ChooseRegionEvent;
import com.lehemobile.shopingmall.model.Region;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import de.greenrobot.event.EventBus;

/**
 * 选择省、市、区、县
 * Created by tanyq on 23/7/16.
 */
@EActivity(R.layout.activity_choose_region)
public class ChooseRegionActivity extends BaseActivity {
    public static final int TYPE_CHOOSE_PROVINCE = 0;
    public static final int TYPE_CHOOSE_REGION = 1;

    @Extra
    int type;

    private Region province;
    private Region city;
    private Region district;

    @AfterViews
    void init() {
        if (type == TYPE_CHOOSE_PROVINCE) {
            setTitle("选择地区");
            showChooseProvince();
        } else {
            setTitle("选择收货地区");
            showChooseRegion();
        }
    }


    private void showFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        if (!TextUtils.isEmpty(tag)) {
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        fragmentTransaction.commit();
    }

    private void showChooseRegion() {
        ChooseRegionFragment fragment = ChooseRegionFragment_.builder().build();
        fragment.setOnChooseRegionListener(new ChooseRegionFragment.OnChooseRegionListener() {
            @Override
            public void onChooseRegion(Region region) {
                //保存
                ConfigManager.setRegion(region);
                EventBus.getDefault().post(new ChooseRegionEvent());
                finish();
            }
        });
        showFragment(fragment, null);
    }

    private void showChooseProvince() {
        ChooseProvinceFragment fragment = ChooseProvinceFragment_.builder().build();
        fragment.setOnChooseProvinceListener(new ChooseProvinceFragment.OnChooseProvinceListener() {
            @Override
            public void onChooseProvince(Region province) {
                Logger.i("select Province:" + province.getName());
                showChooseCity(province);
            }
        });
        showFragment(fragment, null);
    }

    private void showChooseCity(Region province) {
        this.province = province;
        ChooseCityFragment fragment = ChooseCityFragment_.builder().province(province).build();
        fragment.setOnChooseCityListener(new ChooseCityFragment.OnChooseCityListener() {
            @Override
            public void onChooseCity(Region city) {
                Logger.i("select city:" + city.getName());
                showChooseDistrict(city);
            }
        });
        showFragment(fragment, "city");
    }

    private void showChooseDistrict(Region city) {
        this.city = city;
        ChooseDistrictFragment fragment = ChooseDistrictFragment_.builder().city(city).build();
        fragment.setOnChooseDistrictListener(new ChooseDistrictFragment.OnChooseDistrictListener() {
            @Override
            public void onChooseDistrict(Region district) {
                ChooseRegionActivity.this.district = district;
                onComplete();
            }
        });
        showFragment(fragment, "district");
    }

    private void onComplete() {
        Intent intent = new Intent();
        intent.putExtra("province", province);
        intent.putExtra("city", city);
        intent.putExtra("district", district);
        setResult(RESULT_OK, intent);
        finish();
    }
}
