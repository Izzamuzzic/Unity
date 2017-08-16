package com.zwq65.unity.ui.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.VideoWithImage;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.utils.FontUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/15
 */

public class RestVideoAdapter extends BaseRecyclerViewAdapter<VideoWithImage, RestVideoAdapter.ViewHolder> {
    private Context context;

    public RestVideoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RestVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_rest_video, parent, false);
        return new RestVideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FontUtils.getInstance().setTypeface(holder.tvVideoTitle, FontUtils.Font.Roboto_Bold);
        holder.tvVideoTitle.setText(data.get(position).getVideo().getDesc());
        holder.tvVideoTitle.setVisibility(View.INVISIBLE);
        Glide.with(context).load(data.get(position).getImage().getUrl()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.tvVideoTitle.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.ivBeauty);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                    listener.onClick(data.get(holder.getLayoutPosition()), holder.getLayoutPosition());
                }
            }
        });
        //添加动画
        setAnimation(holder.itemView, position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_beauty)
        ImageView ivBeauty;
        @BindView(R.id.tv_video_title)
        TextView tvVideoTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
