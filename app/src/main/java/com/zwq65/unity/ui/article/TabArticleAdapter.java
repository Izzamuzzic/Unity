package com.zwq65.unity.ui.article;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.data.network.retrofit.response.enity.ArticleWithImage;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;
import com.zwq65.unity.utils.FontUtils;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/31
 */

public class TabArticleAdapter extends BaseRecyclerViewAdapter<ArticleWithImage, TabArticleAdapter.ViewHolder> {

    private Context context;

    public TabArticleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FontUtils.getInstance().setTypeface(holder.tvTitle, FontUtils.Font.Roboto_Bold);
        holder.tvTitle.setText(mDataList.get(position).getArticle().getDesc());
        holder.tvTitle.setVisibility(View.INVISIBLE);
        Glide.with(context).load(mDataList.get(position).getImage().getUrl()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.tvTitle.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.ivBackground);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                listener.onClick(mDataList.get(holder.getLayoutPosition()), holder.getLayoutPosition());
            }
        });
    }

    static class ViewHolder extends BaseViewHolder<Article> {
        @BindView(R.id.iv_background)
        ImageView ivBackground;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
        }

    }
}
