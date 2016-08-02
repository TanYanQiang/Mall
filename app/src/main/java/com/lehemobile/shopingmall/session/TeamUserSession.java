package com.lehemobile.shopingmall.session;

import com.lehemobile.shopingmall.model.User;

import java.util.List;

/**
 * Created by tanyq on 2/8/16.
 */
public class TeamUserSession {

    private String groupName;
    private List<User> users;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
