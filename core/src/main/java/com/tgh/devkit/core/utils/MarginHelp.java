package com.tgh.devkit.core.utils;

import android.view.View;
import android.view.ViewGroup;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 *
 * 不能对ListView、GridView等AbsListView的继承类的convertView使用
 * Created by albert on 16/1/8.
 */
public class MarginHelp {

    private MarginHelp(){}

    public static void setMargin(View view, int l, int t, int r, int b) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();
        if (params == null){
            params = new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT);
        }
        params.setMargins(l, t, r, b);
        view.setLayoutParams(params);
    }

    public static void setMarginLeft(View view, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();
        if (params == null){
            params = new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT);
        }
        params.setMargins(margin, params.topMargin, params.rightMargin, params.bottomMargin);
        view.setLayoutParams(params);
    }

    public static void setMarginRight(View view, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();
        if (params == null){
            params = new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT);
        }
        params.setMargins(params.leftMargin, params.topMargin, margin, params.bottomMargin);
        view.setLayoutParams(params);
    }

    public static void setMarginTop(View view, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();
        if (params == null){
            params = new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT);
        }
        params.setMargins(params.leftMargin, margin, params.rightMargin, params.bottomMargin);
        view.setLayoutParams(params);
    }

    public static void setMarginBottom(View view, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();
        if (params == null){
            params = new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT);
        }
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, margin);
        view.setLayoutParams(params);
    }
}
