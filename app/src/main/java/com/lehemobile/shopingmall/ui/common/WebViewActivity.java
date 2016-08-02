package com.lehemobile.shopingmall.ui.common;

import android.content.Context;

/**
 * webview 页面
 * Created by albert on 11/29/14.
 */
public class WebViewActivity extends BaseWebViewActivity {

    public static IntentBuilder_ intent(Context context) {
        return new IntentBuilder_(context, WebViewActivity.class);
    }

    public static IntentBuilder_ intent(android.app.Fragment fragment) {
        return new IntentBuilder_(fragment, WebViewActivity.class);
    }

    public static IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new IntentBuilder_(supportFragment, WebViewActivity.class);
    }

}
