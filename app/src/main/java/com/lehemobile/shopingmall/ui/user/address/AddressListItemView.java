package com.lehemobile.shopingmall.ui.user.address;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Address;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.orhanobut.logger.Logger;

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

        void onDeleteClickListener(Address address);
    }


    @ViewById
    CheckBox isDefault;

    @ViewById
    TextView name;

    @ViewById
    TextView mobile;


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

        isDefault.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = isDefault.isChecked();
                if (!checked) {
                    isDefault.setChecked(true);
                    return;
                }
                isDefault.setChecked(checked);
                if (onAddressListListener != null) {
                    address.setDefault(checked);
                    Logger.i("address " + address.getId() + ", default:" + checked);
                    //TODO 调用接口设置默认值

                    onAddressListListener.onCheckSelectdListener(address);
                }
            }
        });
    }

    public void bindData(Address address) {
        this.address = address;


        isDefault.setChecked(address.isDefault());

        name.setText(address.getName());
        mobile.setText(address.getMobile());

        String stringBuffer = address.getProvince() +
                address.getCity() +
                address.getArea() +
                address.getFullAddress();

        addressTv.setText(stringBuffer);

    }

    @Click(R.id.editBtn)
    void onEdit() {
        if (onAddressListListener != null) {
            onAddressListListener.onEidtClickListener(address);
        }
    }

    @Click(R.id.deleteBtn)
    void onDelete() {

        DialogUtils.alert((Activity) getContext(), "", "确定要删除该地址?", android.R.string.cancel, null,
                android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onAddressListListener != null) {
                            onAddressListListener.onDeleteClickListener(address);
                        }
                    }
                });


    }
}
