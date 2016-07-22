package com.lehemobile.shopingmall.utils.pageList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 16/3/14.
 */
class PageListSession<T> {

    private static final int DEFAULT_PAGE_COUNT = 20;
    private static final int INVALID_TOTAL = -9999;


    private ArrayList<T> data = new ArrayList<>();
    private int page;
    private int pageCount = DEFAULT_PAGE_COUNT;

    private int total = INVALID_TOTAL;
    private boolean isLast;

    public ArrayList<T> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void init() {
        page = 1;
        data.clear();
        isLast = false;
    }

    public void onLoaded(List<T> result) {
        data.addAll(result);
        page++;
        if (total == INVALID_TOTAL) {
            isLast = result.size() < pageCount;
        } else {
            isLast = data.size() >= total;
        }
    }

    public boolean isLast() {
        return isLast;
    }
}
