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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;


/**
 * M为这个itemView对应的model。
 * 使用RecyclerArrayAdapter就一定要用这个ViewHolder。
 * 这个ViewHolder将ItemView与Adapter解耦。
 * 推荐子类继承第二个构造函数。并将子类的构造函数设为一个ViewGroup parent。
 * 然后这个ViewHolder就完全独立。adapter在new的时候只需将parentView传进来。View的生成与管理由ViewHolder执行。
 * 实现setData来实现UI修改。Adapter会在onCreateViewHolder里自动调用。
 * <p>
 * 在一些特殊情况下，只能在setData里设置监听。
 */
abstract public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public abstract void setData(T data);

    protected Context getContext() {
        return itemView.getContext();
    }

}