package com.zwq65.unity.utils.listener

import android.support.annotation.IntDef
import android.support.design.widget.AppBarLayout
import com.blankj.utilcode.util.LogUtils
import com.zwq65.unity.utils.listener.AppBarStateChangeListener.AppBarStatus.COLLAPSED
import com.zwq65.unity.utils.listener.AppBarStateChangeListener.AppBarStatus.EXPANDED
import com.zwq65.unity.utils.listener.AppBarStateChangeListener.AppBarStatus.IDLE

/**
 * ================================================
 * Interface definition for a callback to be invoked when an {@link AppBarLayout}'s vertical
 * offset changes.
 * <p>
 * Created by NIRVANA on 2018/3/20
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
    private var mCurrentState = IDLE

    /**
     * Called when the [AppBarLayout]'s layout offset has been changed. This allows
     * child views to implement custom behavior based on the offset (for instance pinning a
     * view at a certain y value).
     *
     * @param appBarLayout   the [AppBarLayout] which offset has changed
     * @param verticalOffset the vertical offset for the parent [AppBarLayout], in px
     */
    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        LogUtils.i("onOffsetChanged", verticalOffset.toString() + "")
        when {
            verticalOffset == 0 -> {
                if (mCurrentState != EXPANDED) {
                    onStateChanged(appBarLayout, EXPANDED)
                }
                mCurrentState = EXPANDED
            }
            Math.abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != COLLAPSED) {
                    onStateChanged(appBarLayout, COLLAPSED)
                }
                mCurrentState = COLLAPSED
            }
            else -> {
                if (mCurrentState != IDLE) {
                    onStateChanged(appBarLayout, IDLE)
                }
                mCurrentState = IDLE
            }
        }
    }

    /**
     * @param appBarLayout [AppBarLayout]
     * @param state        [AppBarState]
     */
    abstract fun onStateChanged(appBarLayout: AppBarLayout, @AppBarState state: Int)

    @IntDef(EXPANDED, COLLAPSED, IDLE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class AppBarState

    object AppBarStatus {
        //展开状态
        const val EXPANDED = 0
        //折叠状态
        const val COLLAPSED = 1
        //动画状态
        const val IDLE = 2
    }
}

