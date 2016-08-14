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
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 3/8/16.
 */
@EFragment(R.layout.fragment_choose_region_list)
public class ChooseCityFragment extends BaseFragment {
    public interface OnChooseCityListener {
        void onChooseCity(Region city);
    }

    @ViewById
    ListView listView;
    @ViewById
    View searchLayout;
    @ViewById
    EditText searchEdit;

    @FragmentArg
    Region province;

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
        showLoading(R.string.loading);

        Request<List<Region>> request = AddressApi.getRegion(province.getId(), AddressApi.LEVEL_CITY, new Response.Listener<List<Region>>() {
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
        CityAdapter adapter = new CityAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof Region) {
                    if (onChooseCityListener != null) {
                        onChooseCityListener.onChooseCity((Region) item);
                    }
                }
            }
        });
    }

    private class CityAdapter extends BaseListAdapter<Region> {

        public CityAdapter(Context context, Collection<? extends Region> data) {
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
