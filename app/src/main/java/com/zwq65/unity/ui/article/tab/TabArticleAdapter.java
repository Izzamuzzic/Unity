package com.zwq65.unity.ui.article.tab;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/31
 */

public class TabArticleAdapter<T extends Article> extends BaseRecyclerViewAdapter<T, TabArticleAdapter.ViewHolder> {
    @Inject
    TabArticleAdapter() {
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_article;
    }

    @Override
    public ViewHolder getHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @BindView(R.id.iv_background)
        ImageView ivBackground;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        public void setData(T data) {
            tvTitle.setText(data.getDesc());
            Glide.with(getContext()).load(data.getImage().getUrl()).into(ivBackground);
        }

    }
}
