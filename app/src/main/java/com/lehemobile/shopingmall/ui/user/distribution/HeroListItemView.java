package com.lehemobile.shopingmall.ui.user.distribution;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.User;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 3/8/16.
 */
@EViewGroup(R.layout.view_hero_list_item)
public class HeroListItemView extends LinearLayout {

    @ViewById
    TextView ranking;
    @ViewById
    TextView nick;
    @ViewById
    TextView commission;

    @ViewById
    ImageView champion;

    public HeroListItemView(Context context) {
        super(context);
    }

    public HeroListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeroListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeroListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(User user) {

        commission.setTextColor(getResources().getColor(R.color.text_color_lv2));
        nick.setTextColor(getResources().getColor(R.color.text_color_lv2));
        ranking.setTextColor(getResources().getColor(R.color.text_color_lv2));


        commission.setText(user.getCommission());
        nick.setText(user.getNick());

        if (user.getRank().equals("1")) {
            ranking.setVisibility(GONE);
            champion.setVisibility(VISIBLE);
        } else {
            ranking.setVisibility(VISIBLE);
            champion.setVisibility(GONE);
            ranking.setText(user.getRank());
        }


    }
}
