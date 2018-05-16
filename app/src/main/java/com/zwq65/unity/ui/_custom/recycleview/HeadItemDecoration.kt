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
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.zwq65.unity.App
import com.zwq65.unity.R
import com.zwq65.unity.data.db.model.GroupInfo


/**
 * ================================================
 * 自定义[android.support.v7.widget.RecyclerView.ItemDecoration]粘性头部功能
 *
 * Created by NIRVANA on 2017/07/20.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class HeadItemDecoration : RecyclerView.ItemDecoration() {


    var mGroupInfoCallback: GroupInfoCallback? = null
    private var mHeaderHeight: Int = SizeUtils.dp2px(20f)
    private var mDividerHeight: Int = SizeUtils.dp2px(1f)
    private var mPadding: Int = SizeUtils.dp2px(6f)
    private val mPaint by lazy {
        Paint().apply {
            this.color = ContextCompat.getColor(App.instance!!.applicationContext, R.color.chart_blue)
        }
    }
    private val mTextPaint by lazy {
        Paint().apply {
            this.color = ContextCompat.getColor(App.instance!!.applicationContext, R.color.white)
            this.textSize = SizeUtils.sp2px(16f).toFloat()
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        var groupInfo = mGroupInfoCallback?.getGroupInfo(position)
        //判断是否是同类别的第一个item
        outRect.top = if (groupInfo?.position == 0) mHeaderHeight else mDividerHeight
    }

    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        super.onDraw(c, parent, state)
        val childCount = parent?.childCount
        for (i in 0 until childCount!!) {
            val view = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(view)
            val groupInfo = mGroupInfoCallback?.getGroupInfo(index)
            //只有组内的第一个ItemView之上才绘制
            if (groupInfo?.position == 0) {
                val left = parent.paddingLeft + mPadding
                val top = view.top - (mHeaderHeight + mPadding)
                val right = parent.width - (parent.paddingRight + mPadding)
                val bottom = view.top + mPadding
                c?.drawRect(left.toFloat() - mPadding, top.toFloat(), right.toFloat() + mPadding, bottom.toFloat(), mPaint)
                //绘制Title
                c?.drawText(groupInfo.content, left.toFloat(), bottom.toFloat(), mTextPaint)
            }
        }
    }


    interface GroupInfoCallback {
        fun getGroupInfo(position: Int): GroupInfo
    }
}
