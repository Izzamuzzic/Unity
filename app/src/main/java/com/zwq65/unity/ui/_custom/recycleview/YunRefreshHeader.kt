package com.zwq65.unity.ui._custom.recycleview

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.zwq65.unity.R

class YunRefreshHeader @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(mContext, attrs, defStyleAttr), BaseRefreshHeader {
    private var msg: TextView? = null
    var state = BaseRefreshHeader.STATE_NORMAL
        private set(state) {
            if (state == this.state) {
                return
            }
            when (state) {
                BaseRefreshHeader.STATE_NORMAL ->

                    msg!!.setText(R.string.listview_header_hint_normal)
                BaseRefreshHeader.STATE_RELEASE_TO_REFRESH -> msg!!.setText(R.string.listview_header_hint_release)
                BaseRefreshHeader.STATE_REFRESHING -> msg!!.setText(R.string.refreshing)
                BaseRefreshHeader.STATE_DONE -> msg!!.setText(R.string.refresh_done)
            }
            field = state
        }
    private var mMeasuredHeight: Int = 0
    private var mContainer: LinearLayout? = null

    override val visibleHeight: Int
        get() = mContainer!!.height

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_refresh_header, this)
        msg = findViewById(R.id.msg)
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mMeasuredHeight = measuredHeight
        gravity = Gravity.CENTER_HORIZONTAL
        mContainer = findViewById(R.id.container)
        mContainer!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
        this.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    override fun onMove(delta: Float) {
        if (visibleHeight > 0 || delta > 0) {
            setVisiableHeight(delta.toInt() + visibleHeight)
            // 未处于刷新状态，更新箭头
            if (state <= BaseRefreshHeader.STATE_RELEASE_TO_REFRESH) {
                if (visibleHeight > mMeasuredHeight) {
                    state = BaseRefreshHeader.STATE_RELEASE_TO_REFRESH
                } else {
                    state = BaseRefreshHeader.STATE_NORMAL
                }
            }
        }
    }

    override fun releaseAction(): Boolean {
        var isOnRefresh = false
        val height = visibleHeight
        if (height == 0)
        // not visible.
            isOnRefresh = false

        if (visibleHeight > mMeasuredHeight && state < BaseRefreshHeader.STATE_REFRESHING) {
            state = BaseRefreshHeader.STATE_REFRESHING
            isOnRefresh = true
        }
        // refreshing and header isn't shown fully. do nothing.
        if (state == BaseRefreshHeader.STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;
        }
        var destHeight = 0 // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (state == BaseRefreshHeader.STATE_REFRESHING) {
            destHeight = mMeasuredHeight
        }
        smoothScrollTo(destHeight)

        return isOnRefresh
    }

    override fun refreshComplete() {
        state = BaseRefreshHeader.STATE_DONE
        Handler().postDelayed({ reset() }, 500)
    }

    fun reset() {
        smoothScrollTo(0)
        state = BaseRefreshHeader.STATE_NORMAL
    }

    private fun smoothScrollTo(destHeight: Int) {
        val animator = ValueAnimator.ofInt(visibleHeight, destHeight)
        animator.setDuration(300).start()
        animator.addUpdateListener { animation -> setVisiableHeight(animation.animatedValue as Int) }
        animator.start()
    }

    private fun setVisiableHeight(height: Int) {
        var height = height
        if (height < 0) {
            height = 0
        }
        //       `
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        lp.height = height
        mContainer!!.layoutParams = lp
    }
}
