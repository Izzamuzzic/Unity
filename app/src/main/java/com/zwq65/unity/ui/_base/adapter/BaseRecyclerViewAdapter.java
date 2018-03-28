/*
 *    Copyright [2017] [NIRVANA PRIVATE LIMITED]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zwq65.unity.ui._base.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zwq65.unity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * RecyclerView.ViewHolder基类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public abstract class BaseRecyclerViewAdapter<T, V extends BaseViewHolder> extends RecyclerView.Adapter<V> {

    public String TAG = getClass().getSimpleName();

    /**
     * 标记使用动画的final item'position，加载过的item不用动画
     */
    private int lastPosition = -1;
    private List<T> mDataList = new ArrayList<>();
    private OnItemClickListener<T> listener;

    @Override
    public final V onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        V viewHolder = getHolder(view, viewType);
        //itemView 的点击事件
        if (listener != null) {
            viewHolder.itemView.setOnClickListener(v -> listener.onClick(mDataList.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition()));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        //添加动画
        setAnimation(holder.itemView, position);
        //绘制数据ui
        holder.setData(position,getItem(position));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    int TYPE_FOOTER = 2;
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 提供Item的布局
     *
     * @param viewType viewType
     * @return resLayoutId
     */
    public abstract int getLayoutId(int viewType);

    /**
     * 子类实现提供holder
     *
     * @param v        ViewHolder'view
     * @param viewType viewType
     * @return V
     */
    public abstract V getHolder(View v, int viewType);

    public List<T> getmDataList() {
        return mDataList;
    }

    public T getItem(int position) {
        return (position >= 0 && position < mDataList.size()) ? mDataList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 添加一条记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    public void addItem(T data, int position) {
        if (position >= 0 && position <= mDataList.size()) {
            mDataList.add(position, data);
            notifyItemInserted(position);
        }
    }

    /**
     * 添加一条记录
     *
     * @param data 需要加入的数据结构
     */
    public void addItem(T data) {
        addItem(data, mDataList.size());
    }

    /**
     * 批量添加记录
     *
     * @param data 需要加入的数据结构
     */
    public void addItems(List<T> data) {
        addItems(data, mDataList.size());
    }

    /**
     * 批量添加记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    public void addItems(List<T> data, int position) {
        if (position >= 0 && position <= mDataList.size() && data != null && data.size() > 0) {
            mDataList.addAll(position, data);
            notifyItemRangeChanged(position, data.size());
        }
    }

    /**
     * 移除某一条记录
     *
     * @param position 移除数据的position
     */
    public void removeItem(int position) {
        if (position >= 0 && position < mDataList.size()) {
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 移除所有记录
     */
    public void clearItems() {
        int size = mDataList.size();
        if (size > 0) {
            mDataList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(V holder) {
        super.onViewDetachedFromWindow(holder);
        //移除动画,节省资源
        holder.itemView.clearAnimation();
    }

    public interface OnItemClickListener<T> {
        void onClick(T t, int position);
    }
}
