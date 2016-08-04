package com.lehemobile.shopingmall.ui.user.address;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.City;
import com.lehemobile.shopingmall.model.District;
import com.lehemobile.shopingmall.model.Province;
import com.lehemobile.shopingmall.model.Region;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

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

    private Province province;
    private City city;
    private District district;

    @AfterViews
    void init() {
        if (type == TYPE_CHOOSE_PROVINCE) {
            showChooseProvince();
        } else {
            showChooseRegion();
        }
    }


    private void showFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, tag);
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
                Intent intent = new Intent();
                intent.putExtra(AppConfig.Extra, region);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        showFragment(fragment, null);
    }

    private void showChooseProvince() {
        ChooseProvinceFragment fragment = ChooseProvinceFragment_.builder().build();
        fragment.setOnChooseProvinceListener(new ChooseProvinceFragment.OnChooseProvinceListener() {
            @Override
            public void onChooseProvince(Province province) {
                Logger.i("select Province:" + province.getName());
                showChooseCity(province);
            }
        });
        showFragment(fragment, null);
    }

    private void showChooseCity(Province province) {
        this.province = province;
        ChooseCityFragment fragment = ChooseCityFragment_.builder().province(province).build();
        fragment.setOnChooseCityListener(new ChooseCityFragment.OnChooseCityListener() {
            @Override
            public void onChooseCity(City city) {
                Logger.i("select city:" + city.getName());
                showChooseDistrict(city);
            }
        });
        showFragment(fragment, "city");
    }

    private void showChooseDistrict(City city) {
        this.city = city;
        ChooseDistrictFragment fragment = ChooseDistrictFragment_.builder().city(city).build();
        fragment.setOnChooseDistrictListener(new ChooseDistrictFragment.OnChooseDistrictListener() {
            @Override
            public void onChooseDistrict(District district) {
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
