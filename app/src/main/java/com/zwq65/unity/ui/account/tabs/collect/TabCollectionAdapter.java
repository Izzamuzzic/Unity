package com.zwq65.unity.ui.account.tabs.collect;

import android.view.View;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;
import com.zwq65.unity.ui._custom.widget.RatioImageView;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/14
 */

public class TabCollectionAdapter extends BaseRecyclerViewAdapter<Picture, TabCollectionAdapter.ViewHolder> {

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_tab_collection;
    }

    @Override
    public ViewHolder getHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<Picture> {
        @BindView(R.id.iv_beauty)
        RatioImageView ivBeauty;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void setData(Picture data) {
            Glide.with(getContext()).load(data.getUrl()).into(ivBeauty);
        }
    }
}
