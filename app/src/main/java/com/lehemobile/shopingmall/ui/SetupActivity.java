package com.lehemobile.shopingmall.ui;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.lehemobile.shopingmall.R;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by  on 17/7/16.
 */
@EActivity(R.layout.activity_setup)
public class SetupActivity extends BaseActivity {
    public static final int WHAT_SETUP = 1;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_SETUP) {
                startMainActivity();
            }
        }
    };

    @AfterViews
    void init() {

        handler.sendEmptyMessageDelayed(WHAT_SETUP, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(WHAT_SETUP);
        }
    }

    @Click(R.id.goMain)
    void startMainActivity() {
        if (handler != null) {
            handler.removeMessages(WHAT_SETUP);
        }
        MainActivity_.intent(SetupActivity.this).start();
        finish();
    }
}
