package com.lehemobile.shopingmall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.api.TestApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.orhanobut.logger.Logger;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

        Glide.with(this).load("http://s.cn.bing.net/az/hprichbg/rb/CallunaVulgaris_ZH-CN11090416298_1920x1080.jpg")
                .bitmapTransform(
                        new CropCircleTransformation(this))
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) findViewById(R.id.image));
    }

    private void test() {
        Request request = TestApi.test(new Response.Listener<Void>() {
            @Override
            public void onResponse(Void response) {

            }
        }, new AppErrorListener(this) {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
        VolleyHelper.execute(request, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy");
        VolleyHelper.cancel(this);
    }
}
