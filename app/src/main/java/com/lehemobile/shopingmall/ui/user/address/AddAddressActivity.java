package com.lehemobile.shopingmall.ui.user.address;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.model.Address;
import com.lehemobile.shopingmall.model.City;
import com.lehemobile.shopingmall.model.District;
import com.lehemobile.shopingmall.model.Province;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

/**
 * 添加收货地址
 * Created by  on 22/7/16.
 */
@EActivity(R.layout.activity_add_address)
public class AddAddressActivity extends BaseActivity {
    public static final int REQUEST_CHOOSE_REGION_CODE = 1;
    @Extra
    Address address;

    @ViewById(R.id.name)
    EditText nameEdit;
    @ViewById(R.id.mobile)
    EditText mobileEdit;
    @ViewById(R.id.detailedAddress)
    EditText detailedAddress;

    @ViewById
    TextView addressInfo;

    @AfterViews
    void init() {
        if (address != null) {
            updateUI();
        }
    }

    private void updateUI() {
        if (address == null) return;
        nameEdit.setText(address.getName());
        mobileEdit.setText(address.getMobile());
        detailedAddress.setText(address.getDetailedAddress());
        updateRegion();
    }

    private void updateRegion() {
        addressInfo.setText(address.getRegion());
    }

    @Click(R.id.addressProvince)
    void chooseProvince() { //选择省市区
        ChooseRegionActivity_.intent(this).startForResult(REQUEST_CHOOSE_REGION_CODE);
    }

    @Click(R.id.saveBtn)
    void onSave() {

        String name = getInputText(nameEdit);
        String mobile = getInputText(mobileEdit);
        String fullAddress = getInputText(detailedAddress);

        if (Strings.isNullOrEmpty(name)) {
            showToast("请输入收件人姓名");
            return;
        }
        if (Strings.isNullOrEmpty(mobile)) {
            showToast("请输入收件人电话");
            return;
        }

        CharSequence text = addressInfo.getText();
        if (Strings.isNullOrEmpty(text.toString().trim())) {
            showToast("请输入收件人省市区信息");
            return;
        }

        if (Strings.isNullOrEmpty(fullAddress)) {
            showToast("请输入收件人具体的地址信息");
            return;
        }

        if (address == null) {
            address = new Address();
        }
        address.setName(name);
        address.setMobile(mobile);
        address.setDetailedAddress(fullAddress);
        if (address.getId() == 0) {
            addAddress(address);
        } else {
            editAddress(address);
        }
    }

    private void addAddress(Address address) {
        //TODO 添加收货地
        onFinish(address);
    }

    private void editAddress(Address address) {
        //TODO 编辑收货地

        onFinish(address);

    }

    private void onFinish(Address address) {
        Intent intent = new Intent();
        intent.putExtra(AppConfig.Extra, address);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnActivityResult(REQUEST_CHOOSE_REGION_CODE)
    void onChooseRegionResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Province province = (Province) data.getSerializableExtra("province");
            City city = (City) data.getSerializableExtra("city");
            District district = (District) data.getSerializableExtra("district");
            if (address == null) {
                address = new Address();
            }
            address.setProvince(province);
            address.setCity(city);
            address.setDistrict(district);
            updateRegion();
        }

    }
}
