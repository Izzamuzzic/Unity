package com.zwq65.unity.ui.album;

import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;
import com.zwq65.unity.ui._custom.widget.RatioImageView;
import com.zwq65.unity.utils.LogUtils;


/**
 * Created by zwq65 on 2017/07/20
 */

class AlbumAdapter extends BaseRecyclerViewAdapter<Image, AlbumAdapter.ViewHolder> {

    @Override
    public ViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<Image> {
        RatioImageView ivBeauty;

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.adapter_album);
            ivBeauty = $(R.id.iv_beauty);
        }

        @Override
        public void setData(Image data) {
            LogUtils.i(TAG, "data.getUrl():" + data.getUrl());
            Glide.with(getContext()).load(data.getUrl()).into(ivBeauty);
        }
    }
}
