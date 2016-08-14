package com.lehemobile.shopingmall.ui.user.address;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import org.androidannotations.annotations.ViewById;

import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 3/8/16.
 */
@EFragment(R.layout.fragment_choose_region_list)
public class ChooseProvinceFragment extends BaseFragment {
    public interface OnChooseProvinceListener {
        void onChooseProvince(Region province);
    }

    @ViewById
    ListView listView;
    @ViewById
    View searchLayout;
    @ViewById
    EditText searchEdit;
    @ViewById
    TextView tv_empty;

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
        showLoading(R.string.loading);

        Request<List<Region>> request = AddressApi.getRegion(1, AddressApi.LEVEL_PROVINCE, new Response.Listener<List<Region>>() {
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
        if (data == null) {
            listView.setEmptyView(tv_empty);
            return;
        }
        ProvinceAdapter adapter = new ProvinceAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getAdapter().getItem(i);
                if (item instanceof Region) {
                    if (onChooseProvinceListener != null) {
                        onChooseProvinceListener.onChooseProvince((Region) item);
                    }
                }
            }
        });
    }

    private class ProvinceAdapter extends BaseListAdapter<Region> {

        public ProvinceAdapter(Context context, Collection<? extends Region> data) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancel(this);
    }
}
