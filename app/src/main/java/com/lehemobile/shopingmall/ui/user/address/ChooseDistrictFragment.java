package com.lehemobile.shopingmall.ui.user.address;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.City;
import com.lehemobile.shopingmall.model.District;
import com.lehemobile.shopingmall.model.Province;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine_;
import com.lehemobile.shopingmall.ui.view.ListViewEditTextVIew;
import com.lehemobile.shopingmall.ui.view.ListViewEditTextVIew_;
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
        void onChooseDistrict(District district);
    }

    @ViewById
    ListView listView;
    @ViewById
    View searchLayout;
    @ViewById
    EditText searchEdit;

    @FragmentArg
    City city;

    private OnChooseDistrictListener onChooseDistrictListener;

    public void setOnChooseDistrictListener(OnChooseDistrictListener onChooseDistrictListener) {
        this.onChooseDistrictListener = onChooseDistrictListener;
    }

    @AfterViews
    void init() {
        getActivity().setTitle("选择区");
        searchLayout.setVisibility(View.GONE);
        loadData();
    }

    @OptionsItem(R.id.action_complete)
    void otherDistrict() {
        if (editTextVIew == null) return;
        String otherDistrict = editTextVIew.getEditText();
        if (Strings.isNullOrEmpty(otherDistrict)) return;
        District district = new District();
        district.setId(-1);
        district.setName(otherDistrict);
        if (onChooseDistrictListener != null) {
            onChooseDistrictListener.onChooseDistrict(district);
        }
    }

    private void loadData() {
        InputStream open = null;
        try {
            open = getContext().getAssets().open("district.json");
            String json = new String(IO.read(open));
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            List<District> datas = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                District district = new District();
                district.setId(jsonObject1.optInt("district_id"));
                district.setName(jsonObject1.optString("district_name"));
                datas.add(district);
            }
            updateUI(datas);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateUI(List<District> data) {
        DistrictAdapter adapter = new DistrictAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof District) {
                    if (onChooseDistrictListener != null) {
                        onChooseDistrictListener.onChooseDistrict((District) item);
                    }
                }
            }
        });
        editTextVIew = ListViewEditTextVIew_.build(getActivity());
        listView.addFooterView(editTextVIew);
    }

    private class DistrictAdapter extends BaseListAdapter<District> {

        public DistrictAdapter(Context context, Collection<? extends District> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, District item) {
            ListViewSingleLine view = (ListViewSingleLine) convertView;
            view.bindData(item.getName());
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return ListViewSingleLine_.build(context);
        }
    }
}
