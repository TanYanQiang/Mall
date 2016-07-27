package com.lehemobile.shopingmall.ui.goods;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Comment;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.utils.pageList.PageListHelper;
import com.tgh.devkit.list.adapter.BaseListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tanyq on 26/7/16.
 */
@EFragment(R.layout.fragment_goods_detail_comment_list)
public class GoodsDetailCommentListFragment extends BaseFragment {
    @FragmentArg
    Goods goods;

    @ViewById
    PullToRefreshListView listView;

    @ViewById
    TextView tv_empty;

    private PageListHelper<Comment> pageListHelper;

    @AfterViews
    void init() {
        pageListHelper = new PageListHelper<Comment>(listView) {
            @Override
            public void loadData(int page, int pageCount) {
                load(page, pageCount);
            }

            @Override
            public BaseAdapter newAdapter(List<Comment> data) {
                return new CommentAdapter(getActivity(), data);
            }

            @Override
            public void onItemClicked(int position, Comment comment) {//TODO 查看商品详情
            }
        };
        pageListHelper.setEmptyView(tv_empty);
        pageListHelper.setInitMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pageListHelper.initStart();

    }

    private void load(int page, int pageCount) {
        //TODO 调用接口加载数据
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = new Comment();
            comment.setId(i);
            comment.setUserId(1);
            comment.setUserNick("强");
            comment.setStar(3.0f);
            comment.setContent("这个产品很好。。。。");
        }
        pageListHelper.onLoadSuccess(comments);

    }

    private class CommentAdapter extends BaseListAdapter<Comment> {

        public CommentAdapter(Context context, Collection<? extends Comment> data) {
            super(context, data);
        }

        @Override
        public void bindData(int position, View convertView, Comment item) {

        }

        @Override
        public View newItemView(int type, ViewGroup parent) {
            return null;
        }
    }
}
