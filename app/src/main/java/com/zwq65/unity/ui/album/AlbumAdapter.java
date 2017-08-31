package com.zwq65.unity.ui.album;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jingewenku.abrahamcaijin.commonutil.AppScreenMgr;
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

    private Context context;

    AlbumAdapter(Context context) {
        this.context = context;
    }

    int getWidth() {
        int screenWidth = AppScreenMgr.getScreenWidth(context);
        return (screenWidth) / 2;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlbumAdapter.ViewHolder holder, final int position) {

        Glide.with(context).load(mDataList.get(position).getUrl()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //设置图片自适应宽高
                holder.ivBeauty.setOriginalSize(resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                return false;
            }
        }).into(holder.ivBeauty);
        holder.ivBeauty.setOnClickListener(v -> {
            if (listener != null) {
                //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                listener.onClick(mDataList.get(holder.getLayoutPosition()), holder.getLayoutPosition());
            }
        });
        //添加动画
        setAnimation(holder.itemView, position);
    }

    class ViewHolder extends BaseViewHolder<Image> {
        @BindView(R.id.iv_beauty)
        RatioImageView ivBeauty;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindViewData(Image data) {

        }
    }
}
