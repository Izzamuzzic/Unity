package com.zwq65.unity.ui.article;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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
import com.zwq65.unity.data.network.retrofit.response.enity.ArticleWithImage;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;
import com.zwq65.unity.utils.FontUtils;

/**
 * Created by zwq65 on 2017/08/31
 */

public class TabArticleAdapter extends BaseRecyclerViewAdapter<ArticleWithImage, TabArticleAdapter.ViewHolder> {

    @Override
    public ViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<ArticleWithImage> {
        ImageView ivBackground;
        TextView tvTitle;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.adapter_article);
            ivBackground = $(R.id.iv_background);
            tvTitle = $(R.id.tv_title);
        }

        @Override
        public void setData(ArticleWithImage data) {
            FontUtils.getInstance().setTypeface(tvTitle, FontUtils.Font.Roboto_Bold);
            tvTitle.setText(data.getArticle().getDesc());
            tvTitle.setVisibility(View.INVISIBLE);
            Glide.with(getContext()).load(data.getImage().getUrl()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    tvTitle.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(ivBackground);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                    listener.onClick(mDataList.get(getLayoutPosition()), getLayoutPosition());
                }
            });
        }

    }
}
