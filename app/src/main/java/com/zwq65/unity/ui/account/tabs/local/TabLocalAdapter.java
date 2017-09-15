package com.zwq65.unity.ui.account.tabs.local;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;
import com.zwq65.unity.ui._custom.widget.RatioImageView;

import java.io.File;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/14
 */

public class TabLocalAdapter extends BaseRecyclerViewAdapter<File, TabLocalAdapter.ViewHolder> {
    @Override
    public ViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<File> {
        @BindView(R.id.iv_beauty)
        RatioImageView ivBeauty;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.adapter_tab_collection);
            ivBeauty = $(R.id.iv_beauty);
        }

        @Override
        public void setData(File data) {
            Glide.with(getContext()).load(data).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    //设置图片自适应宽高
                    ivBeauty.setOriginalSize(resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                    return false;
                }
            }).into(ivBeauty);
            ivBeauty.setOnClickListener(v -> {
                if (listener != null) {
                    //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                    listener.onClick(mDataList.get(getLayoutPosition()), getLayoutPosition());
                }
            });
        }
    }
}
