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

package com.zwq65.unity.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui._base.adapter.BaseViewHolder;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class RestVideoAdapter<T extends Video> extends BaseRecyclerViewAdapter<T, RestVideoAdapter.ViewHolder> {

    @Inject
    RestVideoAdapter() {
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_article;
    }

    @Override
    public ViewHolder getHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<T> {
        @BindView(R.id.iv_background)
        ImageView ivBackground;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        public void setData(int position, T data) {
            tvTitle.setText(data.getDesc());
//            FontUtils.getInstance().setTypeface(tvTitle, FontUtils.Font.FZYouH_504L);
            Glide.with(getContext()).load(data.getImage().getUrl()).into(ivBackground);
        }
    }
}
