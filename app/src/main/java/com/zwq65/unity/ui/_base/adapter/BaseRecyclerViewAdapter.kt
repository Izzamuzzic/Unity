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

package com.zwq65.unity.ui._base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zwq65.unity.R
import java.util.*

/**
 * ================================================
 * RecyclerView.ViewHolder基类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
abstract class BaseRecyclerViewAdapter<T, V : BaseViewHolder<T>> : RecyclerView.Adapter<V>() {

    /**
     * 标记使用动画的final item'position，加载过的item不用动画
     */
    private var lastPosition = -1
    private val mDataList = ArrayList<T>()
    private var listener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        val viewHolder = getHolder(view, viewType)
        //itemView 的点击事件
        if (listener != null) {
            viewHolder.itemView.setOnClickListener { listener!!.onClick(mDataList[viewHolder.adapterPosition], viewHolder.adapterPosition) }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        //添加动画
        setAnimation(holder.itemView, position)
        //绘制数据ui
        getItem(position)?.let { holder.setData(position, it) }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    val TYPE_FOOTER = 2
                    return if (getItemViewType(position) == TYPE_FOOTER) manager.spanCount else 1
                }
            }
        }
    }

    /**
     * 提供Item的布局
     *
     * @param viewType viewType
     * @return resLayoutId
     */
    abstract fun getLayoutId(viewType: Int): Int

    /**
     * 子类实现提供holder
     *
     * @param v        ViewHolder'view
     * @param viewType viewType
     * @return V
     */
    abstract fun getHolder(v: View, viewType: Int): V

    fun getmDataList(): ArrayList<T> {
        return mDataList
    }

    private fun getItem(position: Int): T? {
        return if (position >= 0 && position < mDataList.size) mDataList[position] else null
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    /**
     * 添加一条记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    @JvmOverloads
    fun addItem(data: T, position: Int = mDataList.size) {
        if (position >= 0 && position <= mDataList.size) {
            mDataList.add(position, data)
            notifyItemInserted(position)
        }
    }

    /**
     * 批量添加记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    fun addItems(data: List<T>?, position: Int = mDataList.size) {
        if (position >= 0 && position <= mDataList.size && data != null && data.isNotEmpty()) {
            mDataList.addAll(position, data)
            notifyItemRangeChanged(position, data.size)
        }
    }

    /**
     * 移除某一条记录
     *
     * @param position 移除数据的position
     */
    fun removeItem(position: Int) {
        if (position >= 0 && position < mDataList.size) {
            mDataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * 移除所有记录
     */
    fun clearItems() {
        val size = mDataList.size
        if (size > 0) {
            mDataList.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        this.listener = listener
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_right)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun onViewDetachedFromWindow(holder: V) {
        super.onViewDetachedFromWindow(holder)
        //移除动画,节省资源
        holder.itemView.clearAnimation()
    }

    interface OnItemClickListener<T> {
        fun onClick(t: T, position: Int)
    }
}
