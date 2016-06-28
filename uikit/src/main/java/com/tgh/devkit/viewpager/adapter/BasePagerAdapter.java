package com.tgh.devkit.viewpager.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Created by albert on 15/12/30.
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {

    public interface ItemClickListener<T> {
        void onPagerItemClicked(int position, T t);
    }

    protected final Object Lock = new Object();

    protected List<T> data = new ArrayList<>();
    protected Activity context;
    protected ItemClickListener<T> itemClickListener;

    public BasePagerAdapter(Activity context,List<T> data) {
        this.data.addAll(data);
        this.context = context;
    }

    public BasePagerAdapter(Activity context, T[] data) {
        Collections.addAll(this.data,data);
        this.context = context;
    }


    public void reset(List<T> data){
        synchronized (Lock){
            this.data.clear();
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }


    public void reset(T[] data){
        synchronized (Lock){
            this.data.clear();
            Collections.addAll(this.data,data);
        }
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public T getItem(int position){
        return data.isEmpty() ? null:data.get(position);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = newItemView(position,container);
        bindData(view,getItem(position));
        view.setTag(position);
        view.setOnClickListener(innerClickListener);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    private View.OnClickListener innerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            if (itemClickListener!=null){
                itemClickListener.onPagerItemClicked(position,getItem(position));
            }
        }
    };


    protected abstract void bindData(View view, T item);

    protected abstract View newItemView(int position, ViewGroup container);
}
