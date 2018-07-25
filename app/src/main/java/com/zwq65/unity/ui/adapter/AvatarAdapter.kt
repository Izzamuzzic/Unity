package com.zwq65.unity.ui.adapter

import android.view.View
import com.zwq65.unity.R
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter
import com.zwq65.unity.ui._base.adapter.BaseViewHolder
import com.zwq65.unity.utils.loadUrl
import kotlinx.android.synthetic.main.item_avatar.view.*
import javax.inject.Inject

/**
 *================================================
 *
 * Created by NIRVANA on 2018/7/25
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
class AvatarAdapter<T : Image> @Inject
internal constructor() : BaseRecyclerViewAdapter<T, AvatarAdapter<T>.ViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_avatar
    }

    override fun getHolder(v: View, viewType: Int): ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(view: View) : BaseViewHolder<T>(view) {

        override fun setData(position: Int, data: T) {
            itemView.iv_avatar?.loadUrl(data.url)
        }
    }
}
