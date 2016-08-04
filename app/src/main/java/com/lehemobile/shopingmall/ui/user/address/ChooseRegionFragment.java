package com.lehemobile.shopingmall.ui.user.address;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Province;
import com.lehemobile.shopingmall.model.Region;
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
 * 选择我的区域
 * Created by tanyq on 3/8/16.
 */
@EFragment(R.layout.fragment_choose_region_list)
public class ChooseRegionFragment extends BaseFragment {
    public interface OnChooseRegionListener {
        void onChooseRegion(Region region);
    }

    @ViewById
    ListView listView;
    @ViewById
    View searchLayout;
    @ViewById
    EditText searchEdit;

    private OnChooseRegionListener onChooseRegionListener;

    public void setOnChooseRegionListener(OnChooseRegionListener onChooseRegionListener) {
        this.onChooseRegionListener = onChooseRegionListener;
    }

    @AfterViews
    void init() {
        getActivity().setTitle("选择收货地区");
        searchLayout.setVisibility(View.GONE);
        loadData();
    }

    private void loadData() {
        InputStream open = null;
        try {
            open = getContext().getAssets().open("province.json");
            String json = new String(IO.read(open));
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            List<Region> datas = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Region region = new Region();
                region.setId(jsonObject1.optString("province_id"));
                region.setName(jsonObject1.optString("province_name"));

                datas.add(region);
            }
            updateUI(datas);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateUI(List<Region> data) {
        RegionAdapter adapter = new RegionAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof Region) {
                    if (onChooseRegionListener != null) {
                        onChooseRegionListener.onChooseRegion((Region) item);
                    }
                }
            }
        });
    }

    private class RegionAdapter extends BaseListAdapter<Region> {

        public RegionAdapter(Context context, Collection<? extends Region> data) {
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
