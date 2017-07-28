package com.zwq65.unity.ui.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/07/20
 */

class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Image> imageList;
    private OnItemClickListener onItemClickListener;

    AlbumAdapter(Context context) {
        this.context = context;
        imageList = new ArrayList<>();
    }

    void initImageList(List<Image> imageList) {
        this.imageList.clear();
        this.imageList.addAll(imageList);
        notifyDataSetChanged();
    }

    void addImageList(List<Image> imageList) {
        this.imageList.addAll(imageList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Image getPosition(int position) {
        if (position > imageList.size()) {
            return null;
        }
        return imageList.get(position);
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(imageList.get(position).getUrl()).into(holder.ivBeauty);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_beauty)
        ImageView ivBeauty;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ivBeauty.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

}
