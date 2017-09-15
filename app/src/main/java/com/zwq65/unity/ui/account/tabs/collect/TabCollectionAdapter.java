package com.zwq65.unity.ui.account.tabs.collect;

import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;
import com.zwq65.unity.ui._custom.widget.RatioImageView;

/**
 * Created by zwq65 on 2017/08/14
 */

public class TabCollectionAdapter extends BaseRecyclerViewAdapter<Picture, TabCollectionAdapter.ViewHolder> {

    @Override
    public ViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<Picture> {
        RatioImageView ivBeauty;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.adapter_tab_collection);
            ivBeauty = $(R.id.iv_beauty);
        }

        @Override
        public void setData(Picture data) {
            Glide.with(getContext()).load(data.getUrl()).into(ivBeauty);
            ivBeauty.setOnClickListener(v -> {
                if (listener != null) {
                    //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                    listener.onClick(mDataList.get(getLayoutPosition()), getLayoutPosition());
                }
            });
        }
    }
}
