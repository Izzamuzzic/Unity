package com.zwq65.unity.ui.adapter

import android.view.View
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter
import com.zwq65.unity.ui._base.adapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_test.view.*
import javax.inject.Inject

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2018/5/16
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class TestAdapter<T : Any> @Inject
constructor() : BaseRecyclerViewAdapter<T, TestAdapter<T>.ViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_test
    }

    override fun getHolder(v: View, viewType: Int): ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(view: View) : BaseViewHolder<T>(view) {

        override fun setData(position: Int, data: T) {
            itemView.tv_title?.text = data.toString()
        }
    }
}
