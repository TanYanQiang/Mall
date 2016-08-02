package com.lehemobile.shopingmall.ui.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.webkit.WebView;


import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.BaseActivity;

import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.builder.PostActivityStarter;


/**
 * Created by tanyq on 25/5/15.
 */
public abstract class BaseWebViewActivity extends BaseActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_SHOW_SHARE = "showShare";
    public static final String EXTRA_HAS_MENU = "hasMenu";
    WebViewFragment webViewFragment;
    private String title;
    private boolean showShare;
    private String url;
    private boolean hasMenu = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_container);
        injectExtras();

        initViews();
    }

    private void injectExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(EXTRA_TITLE)) {
                title = extras.getString(EXTRA_TITLE);
            }
            if (extras.containsKey(EXTRA_URL)) {
                url = extras.getString(EXTRA_URL);
            }
            if (extras.containsKey(EXTRA_SHOW_SHARE)) {
                showShare = extras.getBoolean(EXTRA_SHOW_SHARE);
            }
            if (extras.containsKey(EXTRA_HAS_MENU)) {
                hasMenu = extras.getBoolean(EXTRA_HAS_MENU);
            }
        }
    }

    void initViews() {
        webViewFragment = WebViewFragment_.builder().title(title).url(url).build();
        getSupportFragmentManager().beginTransaction().add(R.id.container, webViewFragment).commit();
    }
//
//    @Override
//    public boolean handleBackPresse() {
//        if (webViewFragment != null) {
//            WebView webView = webViewFragment.webView;
//            if (webView != null && webView.canGoBack()) {
//                webView.goBack();
//                return true;
//            }
//        }
//        return super.handleBackPresse();
//    }

    public static IntentBuilder_ intent(Context context, Class clazz) {
        return new IntentBuilder_(context, clazz);
    }

    public static IntentBuilder_ intent(android.app.Fragment fragment, Class clazz) {
        return new IntentBuilder_(fragment, clazz);
    }

    public static IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment, Class clazz) {
        return new IntentBuilder_(supportFragment, clazz);
    }


    public static class IntentBuilder_
            extends ActivityIntentBuilder<IntentBuilder_> {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context, Class clazz) {
            super(context, clazz);
        }

        public IntentBuilder_(android.app.Fragment fragment, Class clazz) {
            super(fragment.getActivity(), clazz);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment, Class clazz) {
            super(fragment.getActivity(), clazz);
            fragmentSupport_ = fragment;
        }

        @Override
        public PostActivityStarter startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        fragment_.startActivityForResult(intent, requestCode, lastOptions);
                    } else {
                        fragment_.startActivityForResult(intent, requestCode);
                    }
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            context.startActivity(intent, lastOptions);
                        } else {
                            context.startActivity(intent);
                        }
                    }
                }
            }
            return new PostActivityStarter(context);
        }

        public IntentBuilder_ url(String url) {
            return super.extra(EXTRA_URL, url);
        }

        public IntentBuilder_ showShare(boolean showShare) {
            return super.extra(EXTRA_SHOW_SHARE, showShare);
        }

        public IntentBuilder_ hasMenu(boolean hasMenu) {
            return super.extra(EXTRA_HAS_MENU, hasMenu);
        }

        public IntentBuilder_ title(String title) {
            return super.extra(EXTRA_TITLE, title);
        }

    }
}
