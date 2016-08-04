package com.lehemobile.shopingmall.ui.user.address;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.City;
import com.lehemobile.shopingmall.model.Province;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine_;
import com.tgh.devkit.core.utils.IO;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
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
public class ChooseCityFragment extends BaseFragment {
    public interface OnChooseCityListener {
        void onChooseCity(City city);
    }

    @ViewById
    ListView listView;
    @ViewById
    View searchLayout;
    @ViewById
    EditText searchEdit;

    @FragmentArg
    Province province;

    private OnChooseCityListener onChooseCityListener;

    public void setOnChooseCityListener(OnChooseCityListener onChooseCityListener) {
        this.onChooseCityListener = onChooseCityListener;
    }

    @AfterViews
    void init() {
        searchLayout.setVisibility(View.GONE);
        loadData();
    }

    private void loadData() {
        InputStream open = null;
        try {
            open = getContext().getAssets().open("city.json");
            String json = new String(IO.read(open));
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            List<City> datas = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                City city = new City();
                city.setId(jsonObject1.optInt("city_id"));
                city.setName(jsonObject1.optString("city_name"));

                datas.add(city);
            }
            updateUI(datas);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateUI(List<City> data) {
        CityAdapter adapter = new CityAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof City) {
                    if (onChooseCityListener != null) {
                        onChooseCityListener.onChooseCity((City) item);
                    }
                }
            }
        });
    }

    private class CityAdapter extends BaseListAdapter<City> {

        public CityAdapter(Context context, Collection<? extends City> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, City item) {
            ListViewSingleLine view = (ListViewSingleLine) convertView;
            view.bindData(item.getName());
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return ListViewSingleLine_.build(context);
        }
    }
}
