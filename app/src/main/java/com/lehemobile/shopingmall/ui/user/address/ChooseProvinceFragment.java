package com.lehemobile.shopingmall.ui.user.address;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Province;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine_;
import com.tgh.devkit.core.utils.IO;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
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
public class ChooseProvinceFragment extends BaseFragment {
    public interface OnChooseProvinceListener {
        void onChooseProvince(Province province);
    }

    @ViewById
    ListView listView;
    @ViewById
    View searchLayout;
    @ViewById
    EditText searchEdit;

    private OnChooseProvinceListener onChooseProvinceListener;

    public void setOnChooseProvinceListener(OnChooseProvinceListener onChooseProvinceListener) {
        this.onChooseProvinceListener = onChooseProvinceListener;
    }

    @AfterViews
    void init() {
        searchLayout.setVisibility(View.VISIBLE);
        loadData();
    }

    private void loadData() {
        InputStream open = null;
        try {
            open = getContext().getAssets().open("province.json");
            String json = new String(IO.read(open));
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            List<Province> datas = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Province province = new Province();
                province.setId(jsonObject1.optInt("province_id"));
                province.setName(jsonObject1.optString("province_name"));

                datas.add(province);
            }
            updateUI(datas);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateUI(List<Province> data) {
        ProvinceAdapter adapter = new ProvinceAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof Province) {
                    if (onChooseProvinceListener != null) {
                        onChooseProvinceListener.onChooseProvince((Province) item);
                    }
                }
            }
        });
    }

    private class ProvinceAdapter extends BaseListAdapter<Province> {

        public ProvinceAdapter(Context context, Collection<? extends Province> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Province item) {
            ListViewSingleLine view = (ListViewSingleLine) convertView;
            view.bindData(item.getName());
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return ListViewSingleLine_.build(context);
        }
    }
}
