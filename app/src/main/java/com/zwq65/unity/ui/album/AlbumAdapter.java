package com.zwq65.unity.ui.album;

import android.view.View;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;
import com.zwq65.unity.ui._custom.widget.RatioImageView;

import butterknife.BindView;


/**
 * Created by zwq65 on 2017/07/20
 */

class AlbumAdapter extends BaseRecyclerViewAdapter<Image, AlbumAdapter.ViewHolder> {
    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_album;
    }

    @Override
    public ViewHolder getHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<Image> {
        @BindView(R.id.iv_beauty)
        RatioImageView ivBeauty;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void setData(Image data) {
            Glide.with(getContext()).load(data.getUrl()).into(ivBeauty);
        }
    }
}
