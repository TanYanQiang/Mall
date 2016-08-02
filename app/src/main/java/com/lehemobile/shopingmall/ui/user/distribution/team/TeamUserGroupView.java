package com.lehemobile.shopingmall.ui.user.distribution.team;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.session.TeamUserSession;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 2/8/16.
 */
@EViewGroup(R.layout.view_team_user_group_item)
public class TeamUserGroupView extends RelativeLayout {

    @ViewById
    TextView groupName;
    @ViewById
    TextView groupCount;

    @ViewById
    RelativeLayout container;

    public TeamUserGroupView(Context context) {
        super(context);
    }

    public TeamUserGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamUserGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TeamUserGroupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(TeamUserSession session, boolean isExpanded) {
        groupName.setText(session.getGroupName());
        groupCount.setText(""+session.getUsers().size());
        updateUI(isExpanded);
        if (isExpanded) {
            groupName.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_team_arrow_down, 0, 0, 0);
        } else {
            groupName.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_team_arrow_right, 0, 0, 0);
        }
    }

    public void updateUI(boolean isExpanded) {

        int marginBottom = 0;
        if (!isExpanded) {
            marginBottom = getContext().getResources().getDimensionPixelOffset(R.dimen.margin_8);
        }

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, marginBottom);

        container.setLayoutParams(layoutParams);
    }
}
