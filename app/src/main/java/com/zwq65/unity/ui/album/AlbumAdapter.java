package com.zwq65.unity.ui.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jingewenku.abrahamcaijin.commonutil.AppScreenMgr;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui.base.base_adapter.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    public void onBindViewHolder(final AlbumAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getUrl()).into(holder.ivBeauty);
        holder.ivBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //使用getLayoutPosition(),为了保证动态添加和删除时position值的正确性.
                    listener.onClick(data.get(holder.getLayoutPosition()), holder.getLayoutPosition());
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_beauty)
        ImageView ivBeauty;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
