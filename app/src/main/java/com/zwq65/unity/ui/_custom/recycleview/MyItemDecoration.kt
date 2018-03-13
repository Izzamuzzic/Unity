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

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * ================================================
 * 自定义[android.support.v7.widget.RecyclerView.ItemDecoration]分割线
 *
 * Created by NIRVANA on 2017/07/20.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class MyItemDecoration : RecyclerView.ItemDecoration() {
    /**
     * @param outRect 边界
     * @param view    recyclerView ItemView
     * @param parent  recyclerView
     * @param state   recycler 内部数据管理
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        //设定底部边距为1px
        outRect.set(0, 0, 0, 1)
    }
}