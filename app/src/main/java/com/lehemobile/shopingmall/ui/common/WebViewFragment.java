package com.lehemobile.shopingmall.ui.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.tgh.devkit.core.utils.DebugLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;


/**
 * webview 页面
 * Created by albert on 11/29/14.
 */
@EFragment(R.layout.activity_webview)
public class WebViewFragment extends BaseFragment {

    @ViewById
    public WebView webView;

    @FragmentArg
    String url;
    @FragmentArg
    String title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @AfterViews
    void init() {

        if (getActivity() != null) {
            getActivity().setTitle(title);
        }

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyChrome("Mall", WebScope.class));
        webView.requestFocus();
        webView.requestFocusFromTouch();
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }

        webView.loadUrl(url);
        DebugLog.i(url);
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }


    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            DebugLog.i("onReceivedError :" + view.getUrl());
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            DebugLog.i("onPageStarted=" + url);
            if (getActivity() != null) {
                getActivity().setProgressBarIndeterminateVisibility(true);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }

            DebugLog.i("onPageFinished URL: " + url);

            Activity activity = getActivity();
            if (activity == null) {
                return;
            }
            activity.setProgressBarIndeterminateVisibility(false);

            activity.setTitle(view.getTitle());

            //TODO 处理 onPageFinished
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            Activity activity = getActivity();
            if (activity == null) {
                return;
            }

        }
    }


    public class MyChrome extends InjectedChromeClient {

        public MyChrome(String injectedName, Class injectedCls) {
            super(injectedName, injectedCls);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (getActivity() != null) {
                getActivity().getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
            }
            if (newProgress == 100) {
                CookieSyncManager.getInstance().sync();
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            DialogUtils.alert(getActivity(), message, android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result.confirm();
                }
            });
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            DialogUtils.alert(getActivity(), null, message,
                    android.R.string.cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            result.cancel();
                        }
                    },
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            result.confirm();
                        }
                    });
            return super.onJsConfirm(view, url, message, result);
        }
    }


}
