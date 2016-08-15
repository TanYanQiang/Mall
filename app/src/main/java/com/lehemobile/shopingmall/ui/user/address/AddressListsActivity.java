package com.lehemobile.shopingmall.ui.user.address;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.Address;
import com.lehemobile.shopingmall.model.Region;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.pageList.PageListHelper;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 收货地址 列表
 * Created by   on 22/7/16.
 */
@EActivity(R.layout.activity_address_list)
public class AddressListsActivity extends BaseActivity {

    public static final int TYPE_CHOOSE_ADDRESS = 1;
    public static final int TYPE_MANAGER_ADDRESS = 0;

    @Extra
    int type;


    public static final int REQUEST_ADD_CODE = 1;
    public static final int REQUEST_EDIT_CODE = 2;
    @ViewById
    PullToRefreshListView listView;
    @ViewById
    TextView tv_empty;
    private PageListHelper<Address> pageListHelper;

    @AfterViews
    void init() {
        setTitle(type == TYPE_CHOOSE_ADDRESS ? R.string.title_activity_ChooseAddressList : R.string.title_activity_AddressList);
        pageListHelper = new PageListHelper<Address>(listView) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseListAdapter<Address> newAdapter(List<Address> data) {
                return new AddressAdapter(AddressListsActivity.this, data);
            }

            @Override
            public void onItemClicked(int position, Address address) {
                if (type == TYPE_CHOOSE_ADDRESS) {
                    Intent intent = new Intent();
                    intent.putExtra(AppConfig.Extra, address);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
        pageListHelper.setEmptyView(tv_empty);
        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();


    }

    @Click(R.id.action_add_address)
    void addAddress() {
        Logger.i("新增收货地址");
        AddAddressActivity_.intent(this).startForResult(REQUEST_ADD_CODE);
    }


    private void deleteAddress(Address address) {
        //TODO 删除
        Logger.i("删除:" + address.getDetailedAddress());

        //
        ArrayList<Address> data = pageListHelper.getData();
        data.remove(address);
        pageListHelper.getAdapter().setData(data);

    }

    private void load(int page, int pageCount) {
        //TODO 调用接口加载数据
        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Address address = new Address();
            address.setId(i + 1);
            address.setName("谭燕强");
            address.setMobile("186****036" + i);
            Region province = new Region();
            province.setName("北京市");
            address.setProvince(province);
            Region city = new Region();
            city.setName("北京市");
            address.setCity(city);
            Region district = new Region();
            district.setName("朝阳区");
            address.setDistrict(district);
            address.setDetailedAddress("安定路39号长新大厦1105");
            address.setDefault(i == 0);
            addressList.add(address);
        }
        pageListHelper.onLoadSuccess(addressList);

    }

    private class AddressAdapter extends BaseListAdapter<Address> {

        public AddressAdapter(Context context, Collection<? extends Address> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Address item) {
            AddressListItemView view = (AddressListItemView) convertView;
            view.setOnAddressListListener(new AddressListItemView.OnAddressListListener() {
                @Override
                public void onEidtClickListener(Address address) {
                    //TODO 编辑
                    AddAddressActivity_.intent(AddressListsActivity.this).address(address).startForResult(REQUEST_EDIT_CODE);
                }

                @Override
                public void onCheckSelectdListener(Address address) {
                    Logger.i("check Selected:" + address.getDetailedAddress());

                    ArrayList<Address> data = pageListHelper.getData();
                    for (Address ad : data) {
                        ad.setDefault(ad.equals(address));
                        if (ad.isDefault()) {
                            ConfigManager.setUserDefaultAddress(ad);
                        }
                    }
                    pageListHelper.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onDeleteClickListener(Address address) {
                    deleteAddress(address);
                }
            });
            view.bindData(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return AddressListItemView_.build(context);
        }
    }

    @OnActivityResult(REQUEST_ADD_CODE)
    void onAddResult(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Address address = (Address) data.getSerializableExtra(AppConfig.Extra);
        ArrayList<Address> lists = pageListHelper.getData();
        lists.add(address);
        pageListHelper.getAdapter().setData(lists);
    }

    @OnActivityResult(REQUEST_EDIT_CODE)
    void onEditResult(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Address address = (Address) data.getSerializableExtra(AppConfig.Extra);
        ArrayList<Address> lists = pageListHelper.getData();
        int indexOf = lists.indexOf(address);
        Logger.i("edit indexOf:" + indexOf);
        if (indexOf >= 0 && indexOf < pageListHelper.getData().size()) {
            lists.set(indexOf, address);
            pageListHelper.getAdapter().setData(lists);
        }
    }
}
