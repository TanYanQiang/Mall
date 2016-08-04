package com.lehemobile.shopingmall.utils.pageList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 16/3/14.
 */
public abstract class PageListHelper<T> {

    private final PullToRefreshAdapterViewBase pageListView;


    private PageListSession<T> session;
    private View v_empty;
    private BaseListAdapter<T> adapter;

    public PageListHelper(PullToRefreshAdapterViewBase pageListView) {
        this.pageListView = pageListView;
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        session = new PageListSession<>();
        pageListView.setMode(initMode);
        pageListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                initStart();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                loadData(session.getPage(), session.getPageCount());
            }
        });
        pageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T item = (T) parent.getAdapter().getItem(position);
                onItemClicked(position, item);
            }
        });
    }

    private PullToRefreshAdapterViewBase.Mode initMode = PullToRefreshBase.Mode.BOTH;

    public PageListHelper setInitMode(PullToRefreshAdapterViewBase.Mode initMode) {
        this.initMode = initMode;
        pageListView.setMode(initMode);
        return this;
    }

    public void onLoadSuccess(List<T> result) {
        pageListView.onRefreshComplete();

        session.onLoaded(result);
        notifyDataSetChanged(result);

        if (session.isLast()) {
            if (initMode == PullToRefreshBase.Mode.BOTH) {
                pageListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            } else {
                pageListView.setMode(PullToRefreshBase.Mode.DISABLED);
            }
        }

        if (session.getData().isEmpty() && v_empty != null) {
            v_empty.setVisibility(View.VISIBLE);
        }
    }

    public void onLoadError(String errorMsg) {
        pageListView.onRefreshComplete();
    }

    public void initStart() {
        pageListView.setMode(initMode);
        session.init();
        loadData(session.getPage(), session.getPageCount());
    }

    public ArrayList<T> getData() {
        return session.getData();
    }

    public BaseListAdapter<T> getAdapter() {
        return adapter;
    }


    public void notifyDataSetChanged(List<T> result) {
        if(adapter == null){
            adapter = newAdapter(result);
            pageListView.setAdapter(adapter);
        }else{
            adapter.addAll(result);
        }
        adapter.notifyDataSetChanged();
//
//        View view = pageListView.getRefreshableView();
//        if (view instanceof ListView) {
//            ListView listView = (ListView) view;
//            int position = listView.getFirstVisiblePosition();
//            pageListView.setAdapter(adapter);
//            listView.setSelection(position);
//        } else {
//            pageListView.setAdapter(adapter);
//        }
    }

    public abstract void loadData(int page, int pageCount);

    public abstract BaseListAdapter<T> newAdapter(List<T> data);

    public abstract void onItemClicked(int position, T t);

    public void setEmptyView(View empty) {
        if (empty == null) {
            return;
        }
        v_empty = empty;
        v_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_empty.setVisibility(View.GONE);
                initStart();
            }
        });
    }
}
