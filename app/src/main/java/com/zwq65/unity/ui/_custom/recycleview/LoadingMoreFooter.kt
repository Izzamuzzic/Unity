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

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.zwq65.unity.R

/**
 * ================================================
 * [RecyclerView]加载更多的footView
 *
 * Created by NIRVANA on 2017/07/28.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LoadingMoreFooter : LinearLayout {
    private var mText: TextView? = null
    private var mAnimationDrawable: AnimationDrawable? = null
    private var mIvProgress: ImageView? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.yun_refresh_footer, this)
        mText = findViewById<View>(R.id.msg) as TextView
        mIvProgress = findViewById<View>(R.id.iv_progress) as ImageView
        mAnimationDrawable = mIvProgress?.drawable as AnimationDrawable
        if (!mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.start()
        }
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setState(state: Int) {
        when (state) {
            STATE_LOADING -> {
                if (!mAnimationDrawable?.isRunning!!) {
                    mAnimationDrawable?.start()
                }
                mIvProgress?.visibility = View.VISIBLE
                mText?.text = context.getText(R.string.listview_loading)
                this.visibility = View.VISIBLE
            }
            STATE_COMPLETE -> {
                if (mAnimationDrawable?.isRunning!!) {
                    mAnimationDrawable?.stop()
                }
                mText?.text = context.getText(R.string.listview_loading)
                this.visibility = View.GONE
            }
            STATE_NOMORE -> {
                if (mAnimationDrawable?.isRunning!!) {
                    mAnimationDrawable?.stop()
                }
                mText?.text = context.getText(R.string.nomore_loading)
                mIvProgress?.visibility = View.GONE
                this.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }

    fun reSet() {
        this.visibility = View.GONE
    }

    companion object {
        const val STATE_LOADING = 0
        const val STATE_COMPLETE = 1
        const val STATE_NOMORE = 2
    }
}
