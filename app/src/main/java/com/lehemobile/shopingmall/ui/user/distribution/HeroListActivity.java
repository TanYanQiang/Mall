package com.lehemobile.shopingmall.ui.user.distribution;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.order.OrderDetailActivity_;
import com.lehemobile.shopingmall.ui.order.OrderListItemView;
import com.lehemobile.shopingmall.ui.order.OrderListItemView_;
import com.lehemobile.shopingmall.ui.view.Picasso.CropCircleTransformation;
import com.lehemobile.shopingmall.utils.pageList.PageListHelper;
import com.squareup.picasso.Picasso;
import com.tgh.devkit.core.text.SpannableStringHelper;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 3/8/16.
 */
@EActivity(R.layout.activity_hero_list)
public class HeroListActivity extends BaseActivity {
    @ViewById
    TextView myRank;
    @ViewById
    TextView myCommission;
    @ViewById
    ImageView avatar;
    @ViewById
    PullToRefreshListView listView;


    @AfterViews
    void init() {
        initListView();

        loadData();
    }

    private void loadData() {
        //// TODO: 3/8/16 加载数据

        updateUI();
    }

    private void updateUI() {
        updateSelfUI();

    }

    private void updateSelfUI() {
        User user = ConfigManager.getUser();

        String rank = getString(R.string.label_my_rank, user.getRank());
        new SpannableStringHelper(rank).relativeSize(user.getRank(), 1.3f).attachToTextView(myRank);
        String commission = getString(R.string.label_my_commission, user.getCommission());
        new SpannableStringHelper(commission).relativeSize(user.getCommission(), 1.3f).attachToTextView(myCommission);

        Picasso.with(this).load(user.getAvatar())
                .placeholder(R.mipmap.avatar_default)
                .transform(new CropCircleTransformation(getResources().getDimension(R.dimen.hero_avatar_borderWidth), getResources().getColor(R.color.hero_avatar_borderColor)))
                .into(avatar);

    }

    @Click(R.id.homeAsUpIndicator)
    void onBack() {
        onBackPressed();
    }


    private PageListHelper<User> pageListHelper;

    void initListView() {


        pageListHelper = new PageListHelper<User>(listView) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseListAdapter<User> newAdapter(List<User> data) {
                return new UserAdapter(HeroListActivity.this, data);
            }

            @Override
            public void onItemClicked(int position, User user) {

            }
        };
        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();
    }

    private void load(int page, int pageCount) {
        //TODO 调用接口加载数据
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            User user = new User();
            user.setNick("樱木花道VS流川枫" + i);
            user.setAvatar("http://img3.imgtn.bdimg.com/it/u=3681911204,887928701&fm=206&gp=0.jpg");
            user.setRank(String.valueOf(i + 1));
            user.setCommission(String.valueOf((i + 1) * 10000));
            users.add(user);
        }
        pageListHelper.onLoadSuccess(users);
    }

    private class UserAdapter extends BaseListAdapter<User> {

        public UserAdapter(Context context, Collection<? extends User> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, User item) {
            HeroListItemView view = (HeroListItemView) convertView;
            view.bindData(item);
        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return HeroListItemView_.build(context);
        }
    }
}
