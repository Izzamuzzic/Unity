package com.zwq65.unity.ui._custom.layout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewParent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.abs

/**
 * Created by zhaowenqiang.
 * Desc: "description info"
 * Date: 2019/7/8
 * Time: 14:25
 */
class ReBoundConstraintLayout @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ConstraintLayout(mContext, attrs, defStyleAttr) {
    private var mInnerView: View? = null
    private var mDownX: Int = 0
    private var mDownY: Int = 0
    private var isIntercept: Boolean = false
    private var mResistance: Float = 3f
    private var mDuration: Long = 300
    private val mTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var mInterpolator: Interpolator? = null
    private var mResetDistance: Int = 0
    private var isNeedReset: Boolean = false

    init {
        mInterpolator = AccelerateDecelerateInterpolator()
        mResetDistance = Integer.MAX_VALUE
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mInnerView?.clearAnimation()
                mDownX = ev.x.toInt()
                mDownY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val difX = (ev.x - mDownX).toInt()
                val difY = (ev.y - mDownY).toInt()

                if (abs(difY) > mTouchSlop && abs(difY) > abs(difX)) {
                    var parent: ViewParent? = parent
                    while (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true)
                        parent = parent.parent
                        isIntercept = true
                    }
                    if (!mInnerView?.canScrollVertically(-1)!! && difY > 0) {
                        //下拉到边界
                        return true
                    }
                    if (!mInnerView?.canScrollVertically(1)!! && difY < 0) {
                        //上拉到边界
                        return true
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isIntercept) {
                    var parent: ViewParent? = parent
                    while (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false)
                        parent = parent.parent
                    }
                }
                isIntercept = false
                mDownX = 0
                mDownY = 0
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                val difY = ((event.y - mDownY) / mResistance).toInt()
                var isRebound = false
                if (!mInnerView?.canScrollVertically(-1)!! && difY > 0) {
                    //下拉到边界
                    isRebound = true
                } else if (!mInnerView?.canScrollVertically(1)!! && difY < 0) {
                    //上拉到边界
                    isRebound = true
                }
                if (isRebound) {
                    mInnerView?.translationY = difY.toFloat()
//                    if (onBounceDistanceChangeListener != null) {
//                        onBounceDistanceChangeListener.onDistanceChange(abs(difY), if (difY > 0)
//                            OnBounceDistanceChangeListener.DIRECTION_DOWN
//                        else
//                            OnBounceDistanceChangeListener.DIRECTION_UP)
//                    }
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                val difY = mInnerView?.translationY!!.toInt()
                if (difY != 0) {
                    if (abs(difY) <= mResetDistance || isNeedReset) {
                        mInnerView?.animate()!!.translationY(0f).setDuration(mDuration).interpolator = mInterpolator
                    }
//                    if (onBounceDistanceChangeListener != null) {
//                        onBounceDistanceChangeListener.onFingerUp(Math.abs(difY), if (difY > 0)
//                            OnBounceDistanceChangeListener.DIRECTION_DOWN
//                        else
//                            OnBounceDistanceChangeListener.DIRECTION_UP)
//                    }
                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            mInnerView = getChildAt(0)
        } else {
            throw IllegalArgumentException("it must have mInnerView")
        }
    }

}