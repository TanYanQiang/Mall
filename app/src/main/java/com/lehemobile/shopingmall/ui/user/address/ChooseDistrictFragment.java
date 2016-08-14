package com.lehemobile.shopingmall.ui.user.address;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.AddressApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.model.Region;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine_;
import com.lehemobile.shopingmall.ui.view.ListViewEditTextVIew;
import com.lehemobile.shopingmall.ui.view.ListViewEditTextVIew_;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.tgh.devkit.core.utils.IO;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 3/8/16.
 */
@EFragment(R.layout.fragment_choose_region_list)
@OptionsMenu(R.menu.menu_complete)
public class ChooseDistrictFragment extends BaseFragment {

    private ListViewEditTextVIew editTextVIew;

    public interface OnChooseDistrictListener {
        void onChooseDistrict(Region district);
    }

    @ViewById
    ListView listView;
    @ViewById
    View searchLayout;
    @ViewById
    EditText searchEdit;

    @FragmentArg
    Region city;

    private OnChooseDistrictListener onChooseDistrictListener;

    public void setOnChooseDistrictListener(OnChooseDistrictListener onChooseDistrictListener) {
        this.onChooseDistrictListener = onChooseDistrictListener;
    }

    @AfterViews
    void init() {
        searchLayout.setVisibility(View.GONE);
        loadData();
    }

    @OptionsItem(R.id.action_complete)
    void otherDistrict() {
        if (editTextVIew == null) return;
        String otherDistrict = editTextVIew.getEditText();
        if (Strings.isNullOrEmpty(otherDistrict)) return;
        Region district = new Region();
        district.setId(-1);
        district.setName(otherDistrict);
        if (onChooseDistrictListener != null) {
            onChooseDistrictListener.onChooseDistrict(district);
        }
    }

    private void loadData() {
        Request<List<Region>> request = AddressApi.getRegion(city.getId(), AddressApi.LEVEL_DISTRICT, new Response.Listener<List<Region>>() {
            @Override
            public void onResponse(List<Region> response) {
                dismissLoading();
                updateUI(response);
            }
        }, new AppErrorListener(getContext()) {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
        VolleyHelper.execute(request);

    }

    private void updateUI(List<Region> data) {
        DistrictAdapter adapter = new DistrictAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof Region) {
                    if (onChooseDistrictListener != null) {
                        onChooseDistrictListener.onChooseDistrict((Region) item);
                    }
                }
            }
        });
        editTextVIew = ListViewEditTextVIew_.build(getActivity());
        listView.addFooterView(editTextVIew);
    }

    private class DistrictAdapter extends BaseListAdapter<Region> {

        public DistrictAdapter(Context context, Collection<? extends Region> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Region item) {
            ListViewSingleLine view = (ListViewSingleLine) convertView;
            view.bindData(item.getName());
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return ListViewSingleLine_.build(context);
        }
    }
}
