package com.zwq65.unity.ui.video;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/15
 */

public class RestVideoAdapter extends BaseRecyclerViewAdapter<Video, RestVideoAdapter.ViewHolder> {

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_article;
    }

    @Override
    public ViewHolder getHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<Video> {
        @BindView(R.id.iv_background)
        ImageView ivBackground;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void setData(Video data) {
            tvTitle.setText(data.getDesc());
//            FontUtils.getInstance().setTypeface(tvTitle, FontUtils.Font.FZYouH_504L);
            Glide.with(getContext()).load(data.getImage().getUrl()).into(ivBackground);
        }
    }
}
