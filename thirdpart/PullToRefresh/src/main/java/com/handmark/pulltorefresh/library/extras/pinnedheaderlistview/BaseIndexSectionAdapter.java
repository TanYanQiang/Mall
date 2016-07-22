package com.handmark.pulltorefresh.library.extras.pinnedheaderlistview;

import android.app.Activity;
import android.support.v4.util.ArrayMap;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * Created by albert on 15/12/30.
 */
public abstract class BaseIndexSectionAdapter<T extends Indexable> extends SectionedBaseAdapter {

    protected Activity context;
    protected final List<T> rawData;
    protected Comparator<String> keyOrder;
    protected ArrayList<Pair<String, List<T>>> data;
    protected ArrayMap<String, Integer> indexs;
    protected String[] keys;

    public BaseIndexSectionAdapter(Activity context, List<T> rawData) {
        this(context, rawData, String.CASE_INSENSITIVE_ORDER);
    }

    public BaseIndexSectionAdapter(Activity context, List<T> rawData, Comparator<String> keyOrder ) {
        this.context = context;
        this.rawData = rawData;
        this.keyOrder = keyOrder;


        if (rawData == null || rawData.isEmpty()){
            return;
        }

        prepareData();

    }

    protected void prepareData() {
        Map<String,List<T>> items = new TreeMap<>(keyOrder);
        for (T t : rawData){
            String index = t.getIndex();
            if (items.containsKey(index)){
                List<T> list = items.get(index);
                list.add(t);
                items.put(index,list);
            }else {
                List<T> list = new ArrayList<>();
                list.add(t);
                items.put(index,list);
            }
        }

        data = new ArrayList<>();
        for (Map.Entry<String,List<T>> item: items.entrySet()){
            data.add(new Pair<>(item.getKey(), item.getValue()));
        }

        indexs = new ArrayMap<>(data.size());
        keys = new String[data.size()];

        ArrayList<Integer> temp = new ArrayList<>();

        for(int i = 0, size = data.size();i<size;i++){
            Pair<String, List<T>> item = data.get(i);
            keys[i] = item.first;
            List<T> second = item.second;
            int index;
            if (i == 0){
                index = 0;
            }else {
                index = temp.get(i-1);
            }
            indexs.put(item.first,index);
            index = index + second.size() + 1;
            temp.add(index);
        }
    }


    @Override
    public T getItem(int section, int position) {
        if (data == null){
            return null;
        }

        Pair<String, List<T>> listPair = data.get(section);
        return listPair.second.get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getCountForSection(int section) {
        return data == null ? 0 : data.get(section).second.size();
    }

    public String[] getKeys() {
        return keys;
    }

    public int getKeyIndex(String key){
        if (indexs == null || indexs.size() == 0){
            return 0;
        }
        return indexs.get(key);
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = newItemView(section,parent);
        }
        bindItemData(convertView, getItem(section, position));
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = newSectionView(section, parent);
        }
        bindSectionData(convertView,data.get(section).first);
        return convertView;
    }

    protected abstract void bindSectionData(View convertView, String data);

    protected abstract View newSectionView(int section, ViewGroup parent);

    protected abstract void bindItemData(View convertView, T item);

    protected abstract View newItemView(int section, ViewGroup parent);
}
