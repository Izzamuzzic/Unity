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

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * ================================================
 * 自定义[RecyclerView.ItemDecoration]分割线
 *
 * Created by NIRVANA on 2017/07/20.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class MyItemDecoration : RecyclerView.ItemDecoration() {
    private var mDividerHeight: Float? = null
    private var mPaint: Paint = Paint()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("#D7DDE7")
    }

    /**
     * @param outRect 边界
     * @param view    recyclerView ItemView
     * @param parent  recyclerView
     * @param state   recycler 内部数据管理
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        //如果不是第一个，则设置top的值。
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = 1
            mDividerHeight = 1f
//            outRect.offset(dp2px(15f), 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(view)
            //第一个ItemView不需要绘制
            if (index == 0) {
                continue
            }
            val dividerTop = view.top - (mDividerHeight ?: 0f)
            val dividerLeft = view.paddingStart
            val dividerBottom = view.top
            val dividerRight = parent.width - view.paddingEnd

            c.drawRect(dividerLeft.toFloat(), dividerTop, dividerRight.toFloat(), dividerBottom.toFloat(), mPaint)
        }

    }

}
