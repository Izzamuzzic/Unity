package com.zwq65.unity.ui.account.tabs.local;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jingewenku.abrahamcaijin.commonutil.AppScreenMgr;
import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._custom.widget.RatioImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/14
 */

public class TabLocalAdapter extends BaseRecyclerViewAdapter<File, TabLocalAdapter.ViewHolder> {

    private Context context;
    private int width, height;//item固定尺寸

    TabLocalAdapter(Context context) {
        this.context = context;
        height = width = AppScreenMgr.getScreenWidth(context) / 3;//3列排序,宽度为屏幕宽度的1/3,高度与宽度保持一致呈正方形
    }

    @Override
    public TabLocalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tab_collection, parent, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TabLocalAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Glide.with(context).load(mDataList.get(position)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //设置图片自适应宽高
                holder.ivBeauty.setOriginalSize(resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                return false;
            }
        }).into(holder.ivBeauty);
        holder.ivBeauty.setOnClickListener(v -> {
            if (listener != null) {
                //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                listener.onClick(mDataList.get(holder.getLayoutPosition()), holder.getLayoutPosition());
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_beauty)
        RatioImageView ivBeauty;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
