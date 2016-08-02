package com.lehemobile.shopingmall.ui;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.lehemobile.shopingmall.R;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by  on 17/7/16.
 */
@EActivity(R.layout.activity_setup)
public class SetupActivity extends BaseActivity {
    public static final int WHAT_SETUP = 1;
    @ViewById
    ImageView bg;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_SETUP) {
                MainActivity_.intent(SetupActivity.this).start();
                finish();
            }
        }
    };

    @AfterViews
    void init() {
        String url = "http://s.cn.bing.net/az/hprichbg/rb/WatchmanPeak_ZH-CN11491247109_1920x1080.jpg";
        Picasso.with(this).load(url)
                .into(bg);

        handler.sendEmptyMessageDelayed(WHAT_SETUP, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(WHAT_SETUP);
        }
    }
}
