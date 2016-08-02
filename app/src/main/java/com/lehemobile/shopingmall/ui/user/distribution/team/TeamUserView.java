package com.lehemobile.shopingmall.ui.user.distribution.team;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.view.Picasso.CropCircleTransformation;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 2/8/16.
 */
@EViewGroup(R.layout.view_team_user_item)
public class TeamUserView extends LinearLayout {

    @ViewById
    TextView name;
    @ViewById
    ImageView avatar;

    public TeamUserView(Context context) {
        super(context);
    }

    public TeamUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamUserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TeamUserView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(User user) {

        name.setText(user.getNick());

        if (TextUtils.isEmpty(user.getAvatar())) return;
        Picasso.with(getContext()).load(user.getAvatar())
                .transform(new CropCircleTransformation(getResources().getDimension(R.dimen.avatar_borderWidth), getResources().getColor(R.color.team_avatar_borderColor)))
                .into(avatar);
    }

}
