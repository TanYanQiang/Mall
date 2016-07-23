package com.lehemobile.shopingmall.ui.user.address;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Address;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 23/7/16.
 */
@EViewGroup(R.layout.view_address_list_item)
public class AddressListItemView extends RelativeLayout {

    public interface OnAddressListListener {
        void onEidtClickListener(Address address);

        void onCheckSelectdListener(Address address);
    }


    @ViewById
    CheckBox checkbox;

    @ViewById
    TextView name;

    @ViewById
    TextView mobile;

    @ViewById
    TextView isDefault;

    @ViewById(R.id.address)
    TextView addressTv;

    private OnAddressListListener onAddressListListener;

    public void setOnAddressListListener(OnAddressListListener onAddressListListener) {
        this.onAddressListListener = onAddressListListener;
    }

    private Address address;

    public AddressListItemView(Context context) {
        super(context);
    }

    public AddressListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddressListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddressListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @AfterViews
    void init() {
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (onAddressListListener != null) {
                    address.setDefault(b);
                    onAddressListListener.onCheckSelectdListener(address);
                }
            }
        });
    }

    public void bindData(Address address) {
        this.address = address;

        isDefault.setVisibility(address.isDefault() ? VISIBLE : GONE);
        checkbox.setChecked(address.isDefault());

        name.setText(address.getName());
        mobile.setText(address.getMobile());

        String stringBuffer = address.getProvince() +
                address.getCity() +
                address.getArea() +
                address.getFullAddress();

        addressTv.setText(stringBuffer);

    }

    @Click(R.id.edit)
    void onEdit() {
        if (onAddressListListener != null) {
            onAddressListListener.onEidtClickListener(address);
        }
    }
}
