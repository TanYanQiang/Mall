package com.lehemobile.shopingmall.ui.user.address;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.session.TeamUserSession;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine;
import com.lehemobile.shopingmall.ui.common.ListViewSingleLine_;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.TextWatcherAdapter;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 3/8/16.
 */
@EFragment(R.layout.fragment_choose_region_list)
public class ChooseProvinceFragment extends BaseFragment {
    private ProvinceAdapter adapter;
    private List<Region> provinceData;

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
        if (data == null || data.isEmpty()) {
            listView.setEmptyView(tv_empty);
            return;
        }
        provinceData = data;
        adapter = new ProvinceAdapter(getContext(), data);
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
        initSearch();
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


    private void initSearch() {
        searchLayout.setVisibility(View.VISIBLE);
        searchEdit.setText("");
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        doSearch();
                        break;
                }
                return false;
            }
        });
        searchEdit.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doSearch();
            }
        });
    }

    private void doSearch() {
        InputMethodManager imm = ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);

        String keyword = getInputText(searchEdit);
        if (TextUtils.isEmpty(keyword)) {
            adapter.setData(provinceData);
        } else {
            List<Region> searchResult = searchByName(keyword);
            adapter.setData(searchResult);
        }

    }

    private List<Region> searchByName(String keyword) {

        List<Region> searchResult = new ArrayList<>();
        if (adapter != null) {

            for (int i = 0; i < provinceData.size(); i++) {
                Region region = provinceData.get(i);
                if (region.getName().contains(keyword) || region.getPinyin().contains(keyword)) {
                    searchResult.add(region);
                }

            }
        }
        return searchResult;
    }
}
