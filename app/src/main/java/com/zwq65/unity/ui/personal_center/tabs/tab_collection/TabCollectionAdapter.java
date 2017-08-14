package com.zwq65.unity.ui.personal_center.tabs.tab_collection;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.jingewenku.abrahamcaijin.commonutil.AppScreenMgr;
import com.zwq65.unity.R;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui.base.base_adapter.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/14
 */

public class TabCollectionAdapter extends BaseRecyclerViewAdapter<Picture, TabCollectionAdapter.ViewHolder> {

    private Context context;
    private int width, height;//item固定尺寸

    TabCollectionAdapter(Context context) {
        this.context = context;
        height = width = AppScreenMgr.getScreenWidth(context) / 3;//3列排序,宽度为屏幕宽度的1/3,高度与宽度保持一致呈正方形
    }

    @Override
    public TabCollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tab_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TabCollectionAdapter.ViewHolder holder, int position) {
        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        Glide.with(context).load(data.get(position).getUrl()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                target.getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(int width, int height) {

                    }
                });
                return false;
            }
        }).into(holder.ivBeauty);
        holder.ivBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                    listener.onClick(data.get(holder.getLayoutPosition()), holder.getLayoutPosition());
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_beauty)
        ImageView ivBeauty;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
