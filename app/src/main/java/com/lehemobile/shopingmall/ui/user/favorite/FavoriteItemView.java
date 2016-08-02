package com.lehemobile.shopingmall.ui.user.favorite;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.event.FavoriteEvent;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.view.Picasso.RoundedCornersTransformation;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Created by tanyq on 23/7/16.
 */
@EViewGroup(R.layout.view_favorite_list_item)
public class FavoriteItemView extends RelativeLayout {

    @ViewById
    ImageView orderThumb;
    @ViewById
    TextView goodsPrice;
    @ViewById
    TextView goodsName;
    private Goods goods;

    public FavoriteItemView(Context context) {
        super(context);
    }

    public FavoriteItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavoriteItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindData(Goods goods) {
        this.goods = goods;
        goodsName.setText(goods.getName());
        goodsPrice.setText(getResources().getString(R.string.label_order_price, goods.getPrice()));

        Picasso.with(getContext()).load(goods.getThumbnail())
                .resizeDimen(R.dimen.goods_thumb_width, R.dimen.goods_thumb_height)
                .transform(new RoundedCornersTransformation(getResources().getDimensionPixelOffset(R.dimen.corners_small),
                        0,
                        getResources().getDimensionPixelOffset(R.dimen.goods_thumb_border_width),
                        getResources().getColor(R.color.goods_thumb_borderColor)))
                .into(orderThumb);
    }

    @Click(R.id.favoriteBtn)
    void cancelFavorite() {
        DialogUtils.alert((Activity) getContext(), "取消收藏", "确认取消收藏该商品吗?", android.R.string.cancel, null, android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 取消收藏
                cancelFavoriteSuccess();
            }
        });


    }

    private void cancelFavoriteSuccess() {
        Toast.makeText(getContext(), "取消成功", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new FavoriteEvent(false, goods));
    }
}
