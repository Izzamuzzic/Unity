package com.zwq65.unity.ui.article;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
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

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/31
 */

public class TabArticleAdapter extends BaseRecyclerViewAdapter<ArticleWithImage, TabArticleAdapter.ViewHolder> {

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_article;
    }

    @Override
    public ViewHolder getHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    static class ViewHolder extends BaseViewHolder<ArticleWithImage> {
        @BindView(R.id.iv_background)
        ImageView ivBackground;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
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
        }

    }
}
