package com.zwq65.unity.ui._custom.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseSubView


/**
 * ================================================
 * 用于显示Loading的[View]，支持颜色和大小的设置。
 *
 * Created by NIRVANA on 2018/3/20
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseSubView(context, attrs, defStyleAttr) {
    private var mSize: Int = 0
    private var mPaintColor: Int = 0
    private var mAnimateValue = 0
    private var mAnimator: ValueAnimator? = null
    private var mPaint: Paint? = null

    private val mUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        mAnimateValue = animation.animatedValue as Int
        invalidate()
    }

    override fun setUp(context: Context, attrs: AttributeSet?) {
        mSize = SizeUtils.dp2px(16f)
        mPaintColor = ContextCompat.getColor(context, R.color.dark_gray)
        initPaint()
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.color = mPaintColor
        mPaint!!.isAntiAlias = true
        mPaint!!.strokeCap = Paint.Cap.ROUND
    }

    fun setColor(color: Int) {
        mPaintColor = color
        mPaint!!.color = color
        invalidate()
    }

    fun setSize(size: Int) {
        mSize = size
        requestLayout()
    }

    fun start() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(0, LINE_COUNT - 1)
            mAnimator!!.addUpdateListener(mUpdateListener)
            mAnimator!!.duration = 600
            mAnimator!!.repeatMode = ValueAnimator.RESTART
            mAnimator!!.repeatCount = ValueAnimator.INFINITE
            mAnimator!!.interpolator = LinearInterpolator()
            mAnimator!!.start()
        } else if (!mAnimator!!.isStarted) {
            mAnimator!!.start()
        }
    }

    fun stop() {
        if (mAnimator != null) {
            mAnimator!!.removeUpdateListener(mUpdateListener)
            mAnimator!!.removeAllUpdateListeners()
            mAnimator!!.cancel()
            mAnimator = null
        }
    }

    private fun drawLoading(canvas: Canvas, rotateDegrees: Int) {
        val width = mSize / 12
        val height = mSize / 6
        mPaint!!.strokeWidth = width.toFloat()

        canvas.rotate(rotateDegrees.toFloat(), (mSize / 2).toFloat(), (mSize / 2).toFloat())
        canvas.translate((mSize / 2).toFloat(), (mSize / 2).toFloat())

        for (i in 0 until LINE_COUNT) {
            canvas.rotate(DEGREE_PER_LINE.toFloat())
            mPaint!!.alpha = (255f * (i + 1) / LINE_COUNT).toInt()
            canvas.translate(0f, (-mSize / 2 + width / 2).toFloat())
            canvas.drawLine(0f, 0f, 0f, height.toFloat(), mPaint!!)
            canvas.translate(0f, (mSize / 2 - width / 2).toFloat())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mSize, mSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        drawLoading(canvas, mAnimateValue * DEGREE_PER_LINE)
        canvas.restoreToCount(saveCount)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            start()
        } else {
            stop()
        }
    }

    companion object {
        private val LINE_COUNT = 12
        private val DEGREE_PER_LINE = 360 / LINE_COUNT
    }

}
