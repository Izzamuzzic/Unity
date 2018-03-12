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

package com.zwq65.unity.ui._custom.recycleview

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter
import java.util.*

/**
 * ================================================
 * 自定义[RecyclerView][ItemTouchHelper]拖拽item监听
 *
 * Created by NIRVANA on 2017/07/31.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class MyItemTouchCallBack(private val adapter: BaseRecyclerViewAdapter<*, *>) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag: Int
        val swipeFlag: Int
        val layoutManager = recyclerView.layoutManager
        if ((layoutManager is GridLayoutManager) or (layoutManager is StaggeredGridLayoutManager)) {
            //上下左右拖拽
            dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            swipeFlag = 0
        } else {
            //LinearLayoutManager
            dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            swipeFlag = ItemTouchHelper.END
        }
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(adapter.getmDataList(), i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(adapter.getmDataList(), i, i - 1)
            }
        }
        recyclerView.adapter.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //侧滑删除
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.END) {
            adapter.getmDataList().removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }
}
