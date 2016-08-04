package com.lehemobile.shopingmall.ui.user.distribution.team;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.session.TeamUserSession;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.TextWatcherAdapter;

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
    @ViewById
    EditText searchEdit;

    private List<TeamUserSession> teamUserSessions;
    private TeamUserAdapter teamUserAdapter;


    @AfterViews
    void init() {
        initSearch();

        loadData();
    }

    private void initSearch() {
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        doSearch();
                        break;
                }
                return false;
            }
        });
        searchEdit.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doSearch();
            }
        });
    }

    private void doSearch() {
        if (teamUserSessions == null || teamUserSessions.isEmpty()) return;

        String keyword = getInputText(searchEdit);
        if (TextUtils.isEmpty(keyword)) {
            teamUserAdapter.setData(teamUserSessions);
        } else {
            List<TeamUserSession> searchData = searchByName(keyword);
            teamUserAdapter.setData(searchData);
        }

    }

    private List<TeamUserSession> searchByName(String keyword) {

        List<TeamUserSession> result = new ArrayList<>();
        if (teamUserAdapter != null) {

            for (int i = 0; i < teamUserSessions.size(); i++) {
                TeamUserSession session = teamUserSessions.get(i);
                List<User> users = session.getUsers();

                List<User> searchUser = new ArrayList<>();
                for (int j = 0; j < users.size(); j++) {
                    User user = users.get(j);
                    String nick = user.getNick();
                    if (nick.contains(keyword)) {
                        Logger.i("search %s contains %s", keyword, nick);
                        searchUser.add(user);
                    }
                }

                if (!searchUser.isEmpty()) {
                    TeamUserSession resultSession = new TeamUserSession();
                    resultSession.setGroupName(session.getGroupName());
                    resultSession.setUsers(searchUser);
                    result.add(resultSession);
                }
            }
        }
        return result;
    }

    private void loadData() {
        showLoading(R.string.loading);

        //// TODO: 调用接口加载我的小伙伴
        teamUserSessions = new ArrayList<>();

        TeamUserSession session1 = new TeamUserSession();
        session1.setGroupName("总监");
        List<User> user1 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setNick("小丸子" + i);
            user.setAvatar("http://img5.imgtn.bdimg.com/it/u=1998714244,2538311333&fm=206&gp=0.jpg");
            user1.add(user);
        }
        session1.setUsers(user1);

        teamUserSessions.add(session1);


        TeamUserSession session2 = new TeamUserSession();
        session2.setGroupName("经理");
        List<User> user2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setNick("流川枫" + i);
            user.setAvatar("http://img3.imgtn.bdimg.com/it/u=3681911204,887928701&fm=206&gp=0.jpg");
            user2.add(user);
        }
        session2.setUsers(user2);

        teamUserSessions.add(session2);

        TeamUserSession session3 = new TeamUserSession();
        session3.setGroupName("主管");
        List<User> user3 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            User user = new User();
            user.setNick("樱木花道" + i);
            user.setAvatar("http://img4.imgtn.bdimg.com/it/u=3821141571,2783513459&fm=206&gp=0.jpg");
            user3.add(user);
        }
        session3.setUsers(user3);

        teamUserSessions.add(session3);


        TeamUserSession session4 = new TeamUserSession();
        session4.setGroupName("意向客户");
        List<User> user4 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setNick("花伦" + i);
            user.setAvatar("http://img4.imgtn.bdimg.com/it/u=1685028048,2784143540&fm=21&gp=0.jpg");
            user4.add(user);
        }
        session4.setUsers(user4);

        teamUserSessions.add(session4);

        updateUI();
    }

    private void updateUI() {
        dismissLoading();
        teamUserAdapter = new TeamUserAdapter(this, teamUserSessions);
        listView.setAdapter(teamUserAdapter);
        listView.expandGroup(0);
        listView.setGroupIndicator(null);
    }

    class TeamUserAdapter extends BaseExpandableListAdapter {
        private List<TeamUserSession> data;
        private Context context;

        public List<TeamUserSession> getData() {
            return data;
        }

        public void setData(List<TeamUserSession> data) {
            this.data = data;
            notifyDataSetChanged();
        }

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
