package com.zwq65.unity.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2018/5/16
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class TestAdapter<T extends Object> extends BaseRecyclerViewAdapter<T, TestAdapter.ViewHolder> {

    @Inject
    public TestAdapter() {
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_test;
    }

    @Override
    public ViewHolder getHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<T> {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void setData(int position, T data) {
            tvTitle.setText(data.toString());
        }
    }
}
