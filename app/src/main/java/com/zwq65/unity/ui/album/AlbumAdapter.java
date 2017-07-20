package com.zwq65.unity.ui.album;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Beauty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/07/20
 */

class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Beauty> beautyList;

    AlbumAdapter() {
        beautyList = new ArrayList<>();
    }

    void setBeautyList(List<Beauty> beautyList) {
        this.beautyList = beautyList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).ivBeauty.setImageURI(beautyList.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return beautyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_beauty)
        SimpleDraweeView ivBeauty;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
