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

package com.zwq65.unity.ui._custom.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseSubView

/**
 * ================================================
 * 波浪view
 *
 * Created by NIRVANA on 2017/03/06
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class WaveBezierView : BaseSubView {

    private var mPaint: Paint? = null
    private var mPaint2: Paint? = null
    private var mPath: Path? = null
    private var mPath2: Path? = null
    private var currPercent: Float = 0.toFloat()
    private var mScreenHeight: Int = 0
    private var mScreenWidth: Int = 0
    private var animator: ValueAnimator? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    public override fun setUp(context: Context, attrs: AttributeSet?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.style = Paint.Style.FILL_AND_STROKE

        mPaint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint2?.style = Paint.Style.FILL_AND_STROKE

        val shader1 = LinearGradient(0f, 0f, 100f, 100f, ContextCompat.getColor(context, R.color.wave2),
                ContextCompat.getColor(context, R.color.wave1), Shader.TileMode.CLAMP)
        mPaint?.shader = shader1
        mPaint2?.shader = shader1

        startAnim()
    }

    private fun startAnim() {
        animator = ValueAnimator.ofFloat(0f, 1f)
        animator?.duration = 4000
        animator?.repeatCount = ValueAnimator.INFINITE
        animator?.interpolator = LinearInterpolator()
        animator?.addUpdateListener { animation ->
            currPercent = animation.animatedValue as Float
            invalidate()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.end()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mScreenHeight = h
        mScreenWidth = w
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath = getWavePath1(currPercent)
        canvas.drawPath(mPath!!, mPaint!!)
        mPath2 = getWavePath2(currPercent)
        canvas.drawPath(mPath2!!, mPaint2!!)

    }

    private fun getWavePath1(percent: Float): Path {
        val path = Path()
        var x = -mScreenWidth
        //当前x点坐标（根据动画进度水平推移，一个动画周期推移的距离为一个mWidth）
        x += (percent * mScreenWidth).toInt()
        //设置起点
        path.moveTo(x.toFloat(), (mScreenHeight / 3).toFloat())
        //控制点的相对宽度
        val quadWidth = mScreenWidth / 4
        //控制点的相对高度
        val quadHeight = mScreenHeight / 5
        //相对起点绘制贝塞尔曲线（rQuadTo():相对path起点坐标绘制）
        //第一个周期
        path.rQuadTo(quadWidth.toFloat(), quadHeight.toFloat(), (2 * quadWidth).toFloat(), 0f)
        path.rQuadTo(quadWidth.toFloat(), (-quadHeight).toFloat(), (2 * quadWidth).toFloat(), 0f)
        //第二个周期
        path.rQuadTo(quadWidth.toFloat(), quadHeight.toFloat(), (2 * quadWidth).toFloat(), 0f)
        path.rQuadTo(quadWidth.toFloat(), (-quadHeight).toFloat(), (2 * quadWidth).toFloat(), 0f)
        //右侧的直线
        path.lineTo((x + mScreenWidth * 2).toFloat(), mScreenHeight.toFloat())
        //下边的直线
        path.lineTo(x.toFloat(), mScreenHeight.toFloat())
        //自动闭合补出左边的直线
        path.close()
        return path
    }

    private fun getWavePath2(percent: Float): Path {
        val path = Path()
        var x = -2 * mScreenWidth
        //当前x点坐标（根据动画进度水平推移，一个动画周期推移的距离为一个mWidth）
        x += (percent * 2f * mScreenWidth.toFloat()).toInt()
        //设置起点
        path.moveTo(x.toFloat(), (mScreenHeight / 3).toFloat())
        //控制点的相对宽度
        val quadWidth = mScreenWidth / 2
        //控制点的相对高度
        val quadHeight = mScreenHeight / 6
        //相对起点绘制贝塞尔曲线（rQuadTo():相对path起点坐标绘制）
        //第一个周期
        path.rQuadTo(quadWidth.toFloat(), (-quadHeight).toFloat(), (2 * quadWidth).toFloat(), 0f)
        path.rQuadTo(quadWidth.toFloat(), quadHeight.toFloat(), (2 * quadWidth).toFloat(), 0f)
        //第二个周期
        path.rQuadTo(quadWidth.toFloat(), (-quadHeight).toFloat(), (2 * quadWidth).toFloat(), 0f)
        //右侧的直线
        path.lineTo((x + mScreenWidth * 3).toFloat(), mScreenHeight.toFloat())
        //下边的直线
        path.lineTo(x.toFloat(), mScreenHeight.toFloat())
        //自动闭合补出左边的直线
        path.close()
        return path
    }
}