package com.zwq65.unity.ui._custom.recycleview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.blankj.utilcode.util.SizeUtils
import com.zwq65.unity.R
import com.zwq65.unity.ui._custom.view.LoadingView


class LoadingMoreFooter : LinearLayout {
    private var mText: TextView? = null
    private var mLoadingView: LoadingView? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_load_footer, this)
        mText = findViewById(R.id.tv_msg)
        mLoadingView = findViewById(R.id.loadingView)
        mLoadingView!!.setSize(SizeUtils.dp2px(14f))
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setState(state: Int) {
        when (state) {
            STATE_LOADING -> {
                mText!!.text = context.getText(R.string.listview_loading)
                mLoadingView!!.visibility = View.VISIBLE
                visibility = View.VISIBLE
            }
            STATE_COMPLETE -> {
                mText!!.text = context.getText(R.string.listview_loading)
                visibility = View.GONE
            }
            STATE_NOMORE -> {
                mText!!.text = context.getText(R.string.nomore_loading)
                mLoadingView!!.visibility = View.GONE
                visibility = View.VISIBLE
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
