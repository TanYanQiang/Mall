package com.lehemobile.shopingmall.ui.goods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lehemobile.shopingmall.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tanyq on 5/8/16.
 */
public class GoodsDetailImageAdapter extends RecyclerView.Adapter<GoodsDetailImageAdapter.ImageHolder> {
    private List<String> data;
    private Context context;

    public GoodsDetailImageAdapter(Context context, List<String> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_goods_detail_image, parent);
        return new ImageHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final ImageHolder holder, int position) {
        String imageUrl = data.get(position);

        Picasso.with(context).load(imageUrl).into(holder.imageView, new Callback.EmptyCallback() {
            @Override
            public void onError() {
                super.onError();
                holder.progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                holder.progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ProgressBar progress;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            progress = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }
}
