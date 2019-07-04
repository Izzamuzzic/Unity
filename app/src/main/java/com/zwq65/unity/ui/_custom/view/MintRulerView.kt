/*
 *    Copyright  2017  NIRVANA
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

package com.zwq65.unity.ui._custom.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.Scroller
import com.blankj.utilcode.util.SizeUtils
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseSubView
import kotlin.math.abs


/**
 * ================================================
 * 自定义测量尺view
 *
 * Created by NIRVANA on 2017/11/02
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class MintRulerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseSubView(context, attrs, defStyleAttr) {
    /**
     * 绘制中刻度的paint
     */
    private var mMediumPaint: Paint? = null
    /**
     * 绘制小刻度和数字的paint
     */
    private var mTinyPaint: Paint? = null
    /**
     * 绘制小三角形的paint
     */
    private var mTrianglePaint: Paint? = null
    /**
     * 三角形path
     */
    private var mTrianglePath: Path? = null
    /**
     * 中刻度的绘制高度
     */
    private var mMediumHeight: Float = 0.toFloat()
    /**
     * 小刻度的绘制高度
     */
    private var mTinyHeight: Float = 0.toFloat()
    /**
     * 三角形的绘制高度
     */
    private var mTriangleHeight: Float = 0.toFloat()
    /**
     * 数字的绘制高度
     */
    private var mTextHeight: Float = 0.toFloat()
    /**
     * 两个小刻度间的长度
     */
    private var mScale: Int = 0
    /**
     * 尺子的最大最小数值
     */
    private var mMinValue: Float = 0.toFloat()
    private var mMaxValue: Float = 0.toFloat()

    /**
     * 尺子正中的刻度值
     */
    private var mMiddleValue: Float = 0.toFloat()

    private var moveX: Float = 0.toFloat()

    private var lastX: Float = 0.toFloat()

    /**
     * 滑动帮助类
     */
    private var mScroller: Scroller? = null

    /**
     * 速度追踪器
     */
    private var mVelocityTracker: VelocityTracker? = null

    private fun setMiddleValue(middleValue: Float) {
        mMiddleValue = middleValue
    }

    public override fun setUp(context: Context, attrs: AttributeSet?) {

        mMinValue = 0f
        mMaxValue = 100f

        //定义长宽值
        val mediumWidth = SizeUtils.dp2px(2f)
        mMediumHeight = SizeUtils.dp2px(35f).toFloat()
        val tinyWidth = SizeUtils.dp2px(1f)
        mTinyHeight = SizeUtils.dp2px(20f).toFloat()
        mTriangleHeight = SizeUtils.dp2px(9f).toFloat()

        mScale = SizeUtils.dp2px(10f)

        mMediumPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mMediumPaint!!.color = ContextCompat.getColor(mContext, R.color.white)
        mMediumPaint!!.style = Paint.Style.STROKE
        mMediumPaint!!.strokeWidth = mediumWidth.toFloat()

        mTinyPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTinyPaint!!.color = ContextCompat.getColor(mContext, R.color.white)
        mTinyPaint!!.textSize = SizeUtils.sp2px(14f).toFloat()
        mTinyPaint!!.style = Paint.Style.STROKE
        mTinyPaint!!.strokeWidth = tinyWidth.toFloat()

        mTextHeight = mTinyPaint!!.fontSpacing

        mTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTrianglePaint!!.color = ContextCompat.getColor(mContext, R.color.white)
        mTrianglePaint!!.style = Paint.Style.FILL_AND_STROKE

        mTrianglePath = Path()

        mScroller = Scroller(mContext)
        setMiddleValue(50f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制背景颜色
        canvas.drawColor(ContextCompat.getColor(mContext, R.color.medium_scale))
        //绘制尺子的大小刻度和数字
        drawScales(canvas)
        //绘制三角形(作为测量的目标值)
        canvas.drawPath(mTrianglePath!!, mTrianglePaint!!)
    }

    /**
     * 绘制尺子的大小刻度和数字
     *
     * @param canvas canvas
     */
    private fun drawScales(canvas: Canvas) {
        //        //滑动必须在规定范围内
        //        if (mMiddleValue + moveX > mMinValue && mMiddleValue + moveX < mMaxValue) {
        //            //改变移动后的正中刻度值
        //            mMiddleValue += moveX;
        //        }
        canvas.save()
        canvas.translate(moveX, 0f)
        //先绘制后半段
        canvas.drawLine(centerX.toFloat(), 0f, centerX.toFloat(), mMediumHeight, mMediumPaint!!)
        var i = 1
        while (i < mMaxValue) {
            if (i % 10 == 0) {
                canvas.drawLine((centerX + mScale * i).toFloat(), 0f, (centerX + mScale * i).toFloat(), mMediumHeight, mMediumPaint!!)
                //绘制大刻度下的数字(调整位置使数字居中于刻度显示)
                canvas.drawText(i.toString(), centerX + mScale * i - mTinyPaint!!.measureText(i.toString()) / 2,
                        mMediumHeight + 2 * mTextHeight, mTinyPaint!!)
            } else {
                canvas.drawLine((centerX + mScale * i).toFloat(), 0f, (centerX + mScale * i).toFloat(), mTinyHeight, mTinyPaint!!)
            }
            i++
        }
        canvas.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //path加上一个三角形
        mTrianglePath!!.moveTo(centerX - mTriangleHeight, 0f)
        mTrianglePath!!.lineTo(centerX.toFloat(), mTriangleHeight)
        mTrianglePath!!.lineTo(centerX + mTriangleHeight, 0f)
        mTrianglePath!!.close()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        if (MeasureSpec.EXACTLY != heightMode) {
            //默认高度为80dp，宽度默认为屏幕宽度
            height = SizeUtils.dp2px(80f)
        }
        setMeasuredDimension(width, height)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //初始化mVelocityTracker速度追踪器
        mVelocityTracker = VelocityTracker.obtain()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //回收mVelocityTracker速度追踪器
        mVelocityTracker!!.clear()
        mVelocityTracker!!.recycle()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x

        when (event.action) {
            MotionEvent.ACTION_DOWN -> lastX = x
            MotionEvent.ACTION_MOVE -> {
                moveX = x - lastX
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker!!.addMovement(event)
                calculateVelocity()
                return false
            }
            else -> return false
        }
        return true
    }

    /**
     * 计算手势抬起时的速度
     */
    private fun calculateVelocity() {
        mVelocityTracker!!.computeCurrentVelocity(1000)
        //计算水平方向的速度（单位秒）
        val xVelocity = mVelocityTracker!!.xVelocity
        //Minimum velocity to initiate a fling, as measured in pixels per second.
        val minFlingVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity.toFloat()
        //大于这个值才会被认为是fling
        if (abs(xVelocity) > minFlingVelocity) {
            mScroller!!.fling(0, 0, xVelocity.toInt(), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0)
            invalidate()
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        //返回true表示滑动还没有结束
        if (mScroller!!.computeScrollOffset()) {
            if (mScroller!!.currX == mScroller!!.finalX) {
            }
        }
    }
}
