package com.lehemobile.shopingmall.ui.goods;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;

public class GoodsDetailScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Glide.with(this)
                .load("http://a.appsimg.com/upload/merchandise/pdc/035/836/5935820670513836035/0/1804946-6_1440x8000_90.jpg")
                .crossFade()
                .into(imageView);

    }
}
