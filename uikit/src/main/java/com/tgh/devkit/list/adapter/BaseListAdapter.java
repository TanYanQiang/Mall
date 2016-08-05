package com.tgh.devkit.list.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Created by albert on 15/12/29.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    private final Object Lock = new Object();
    protected Context context;
    protected List<T> data = new ArrayList<>();

    public BaseListAdapter(Context context, Collection<? extends T> data) {
        this.context = context;
        this.data.addAll(data);
    }

    public BaseListAdapter(Context context, T[] data) {
        this.context = context;
        Collections.addAll(this.data,data);
    }

    public BaseListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.isEmpty()? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(T object) {
        synchronized (Lock) {
            data.add(object);
        }
        notifyDataSetChanged();
    }
    public void setData(Collection<? extends T> collection){
        this.data = (List<T>) collection;
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        synchronized (Lock) {
            data.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void addAll(T[] items) {
        synchronized (Lock) {
            Collections.addAll(this.data,items);
        }
        notifyDataSetChanged();
    }

    public void reset(Collection<? extends T> collection){
        synchronized (Lock) {
            data.clear();
            data.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void reset(T[] items){
        synchronized (Lock) {
            data.clear();
            Collections.addAll(this.data,items);
        }
        notifyDataSetChanged();
    }

    public void insert(T object, int index) {
        synchronized (Lock) {
            data.add(index, object);
        }
        notifyDataSetChanged();
    }

    public void remove(T object) {
        synchronized (Lock) {
            data.remove(object);
        }
        notifyDataSetChanged();
    }


    public void clear() {
        synchronized (Lock) {
            data.clear();
        }
        notifyDataSetChanged();
    }


    public void sort(Comparator<? super T> comparator) {
        synchronized (Lock) {
            Collections.sort(data, comparator);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = newItemView(getItemViewType(position), parent);
        }
        bindData(position, convertView, getItem(position));
        return convertView;
    }

    public abstract void bindData(int position, View convertView, T item);

    public abstract View newItemView(int type, ViewGroup parent);

    public List<T> getData() {
        return data;
    }
}
