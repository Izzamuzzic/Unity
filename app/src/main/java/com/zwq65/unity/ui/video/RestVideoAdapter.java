package com.zwq65.unity.ui.video;

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
import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;
import com.zwq65.unity.utils.FontUtils;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/15
 */

public class RestVideoAdapter extends BaseRecyclerViewAdapter<VideoWithImage, RestVideoAdapter.ViewHolder> {

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.adapter_rest_video;
    }

    @Override
    public ViewHolder getHolder(View v, int viewType) {
        return null;
    }

    class ViewHolder extends BaseViewHolder<VideoWithImage> {
        @BindView(R.id.iv_beauty)
        ImageView ivBeauty;
        @BindView(R.id.tv_video_title)
        TextView tvVideoTitle;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void setData(VideoWithImage data) {
            FontUtils.getInstance().setTypeface(tvVideoTitle, FontUtils.Font.Roboto_Bold);
            tvVideoTitle.setText(data.getVideo().getDesc());
            tvVideoTitle.setVisibility(View.INVISIBLE);
            Glide.with(getContext()).load(data.getImage().getUrl()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    tvVideoTitle.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(ivBeauty);
        }
    }
}
