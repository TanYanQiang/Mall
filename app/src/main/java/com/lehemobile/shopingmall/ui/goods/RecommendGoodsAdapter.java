package com.lehemobile.shopingmall.ui.goods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.ui.view.Picasso.PicassoHelper;
import com.lehemobile.shopingmall.ui.view.Picasso.RoundedCornersTransformation;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tanyq on 5/8/16.
 */
public class RecommendGoodsAdapter extends RecyclerView.Adapter<RecommendGoodsAdapter.GoodsViewHolder> {

    private List<Goods> data;
    private Context context;

    public RecommendGoodsAdapter(Context context, List<Goods> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recommend_goods, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public void onBindViewHolder(GoodsViewHolder holder, int position) {
        Goods goods = data.get(position);
        holder.goodsPrice.setText("￥" + goods.getPriceString());
        PicassoHelper.showGoodsThumb(context,goods.getThumbnail(),holder.goodsThumb);
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        public ImageView goodsThumb;
        public TextView goodsPrice;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            goodsThumb = (ImageView) itemView.findViewById(R.id.goodsThumb);
            goodsPrice = (TextView) itemView.findViewById(R.id.goodsPrice);
            goodsThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Goods goods = data.get(getLayoutPosition());
                    Logger.i("推荐商品：" + goods.getName());
                    GoodsDetailActivity_.intent(context).goodsId(goods.getId()).start();
                }
            });
        }
    }
}
