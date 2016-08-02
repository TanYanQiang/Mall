package com.lehemobile.shopingmall.ui.common;

import android.app.Activity;
import android.webkit.WebView;


/**
 * Created by albert on 16/3/29.
 */
public class WebScope {


    public static void closePage(WebView webView) {
        Activity activity = (Activity) webView.getContext();
        activity.finish();
    }


    public static void pageBack(WebView webView) {
        webView.goBack();
    }

    public static void pageForward(WebView webView) {
        webView.goForward();
    }

    public static void loadImage(WebView webView, String imageUrl) {

    }

    public static void doShare(WebView webView, String content, String imageUrl) {

    }

    public static void goShare(WebView webView, String title, String desc, String url, String img_url) {

    }
}
