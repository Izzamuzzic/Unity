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

package com.zwq65.unity.ui.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.zwq65.unity.R
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter
import com.zwq65.unity.ui._base.adapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_album.view.*
import javax.inject.Inject

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/07/20
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class AlbumAdapter<T : Image> @Inject
internal constructor() : BaseRecyclerViewAdapter<T, AlbumAdapter<T>.ViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_album
    }

    override fun getHolder(v: View, viewType: Int): ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(view: View) : BaseViewHolder<T>(view) {

        override fun setData(position: Int, data: T) {
            Glide.with(context)
                    .load(data.url)
                    .into(itemView.iv_beauty)
        }
    }
}
