package com.lehemobile.shopingmall.ui.user.distribution.team;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.session.TeamUserSession;
import com.lehemobile.shopingmall.ui.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyq on 2/8/16.
 */
@EActivity(R.layout.activity_team_user_list)
public class TeamUserListActivity extends BaseActivity {
    @ViewById
    ExpandableListView listView;
    private List<TeamUserSession> data;

    @AfterViews
    void init() {
        loadData();
    }

    private void loadData() {
        showLoading(R.string.loading);
        data = new ArrayList<>();

        TeamUserSession session1 = new TeamUserSession();
        session1.setGroupName("总监");
        List<User> user1 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setNick("小丸子");
            user.setAvatar("http://img5.imgtn.bdimg.com/it/u=1998714244,2538311333&fm=206&gp=0.jpg");
            user1.add(user);
        }
        session1.setUsers(user1);

        data.add(session1);


        TeamUserSession session2 = new TeamUserSession();
        session2.setGroupName("经理");
        List<User> user2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setNick("流川枫");
            user.setAvatar("http://img3.imgtn.bdimg.com/it/u=3681911204,887928701&fm=206&gp=0.jpg");
            user2.add(user);
        }
        session2.setUsers(user2);

        data.add(session2);

        TeamUserSession session3 = new TeamUserSession();
        session3.setGroupName("主管");
        List<User> user3 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            User user = new User();
            user.setNick("樱木花道");
            user.setAvatar("http://img4.imgtn.bdimg.com/it/u=3821141571,2783513459&fm=206&gp=0.jpg");
            user3.add(user);
        }
        session3.setUsers(user3);

        data.add(session3);


        TeamUserSession session4 = new TeamUserSession();
        session4.setGroupName("意向客户");
        List<User> user4 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setNick("花伦");
            user.setAvatar("http://img4.imgtn.bdimg.com/it/u=1685028048,2784143540&fm=21&gp=0.jpg");
            user4.add(user);
        }
        session4.setUsers(user4);

        data.add(session4);

        updateUI();
    }

    private void updateUI() {
        dismissLoading();
        TeamUserAdapter teamUserAdapter = new TeamUserAdapter(this, data);
        listView.setAdapter(teamUserAdapter);
        listView.expandGroup(0);
        listView.setGroupIndicator(null);
    }

    class TeamUserAdapter extends BaseExpandableListAdapter {
        private List<TeamUserSession> data;
        private Context context;

        public TeamUserAdapter(Context context, List<TeamUserSession> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getGroupCount() {
            return data.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return data.get(groupPosition).getUsers().size();
        }

        @Override
        public TeamUserSession getGroup(int i) {
            return data.get(i);
        }

        @Override
        public User getChild(int groupPosition, int childPosition) {
            return data.get(groupPosition).getUsers().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
            TeamUserGroupView groupView = TeamUserGroupView_.build(context);
            groupView.bindData(getGroup(groupPosition), isExpanded);
            return groupView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
            TeamUserView userView = TeamUserView_.build(context);
            userView.bindData(getChild(groupPosition, childPosition));
            return userView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
