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
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.SizeUtils
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseSubView

/**
 * ================================================
 * 自定义view（仿百度贴吧加载球）
 *
 * Created by NIRVANA on 2017/07/13.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class BaiduLoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseSubView(context, attrs, defStyleAttr) {

    private var path: Path? = null
    private var o: Path? = null
    private var textPaint: Paint? = null
    private var mPaint: Paint? = null
    private var currentPercent: Float = 0.toFloat()
    private var color: Int = 0
    private var text: String? = "贴"

    public override fun setUp(context: Context, attrs: AttributeSet?) {
        mHeight = SizeUtils.dp2px(50f)
        mWidth = SizeUtils.dp2px(50f)
        val array = context.obtainStyledAttributes(attrs, R.styleable.BaiduLoadingView)
        //自定义颜色和文字
        color = array.getColor(R.styleable.BaiduLoadingView_color, Color.rgb(41, 163, 254))
        text = array.getString(R.styleable.BaiduLoadingView_text)
        array.recycle()

        //图形及路径填充画笔
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = color
        mPaint!!.isDither = true
        //文字画笔
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint!!.color = Color.WHITE
        textPaint!!.typeface = Typeface.DEFAULT_BOLD

        //闭合波浪路径
        path = Path()
        //圆形外围路径
        o = Path()

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 1000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            currentPercent = animation.animatedFraction
            invalidate()
        }
        valueAnimator.start()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (MeasureSpec.EXACTLY == widthMode) {
            mWidth = width
        }
        if (MeasureSpec.EXACTLY == heightMode) {
            mHeight = height
        }
        setMeasuredDimension(mWidth, mHeight)
        val textSize = mWidth / 2
        textPaint!!.textSize = textSize.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制波浪上方的文字
        textPaint?.color = color
        drawCenterText(canvas, textPaint, text)
        o?.addCircle((mWidth / 2).toFloat(), (mHeight / 2).toFloat(), (mWidth / 2).toFloat(), Path.Direction.CCW)
        canvas.clipPath(o!!)
        path = getActionPath(currentPercent)
        canvas.drawPath(path!!, mPaint!!)
        canvas.clipPath(path!!)
        textPaint?.color = Color.WHITE
        drawCenterText(canvas, textPaint, text)
    }

    private fun getActionPath(percent: Float): Path {
        val path = Path()
        var x = -mWidth
        //当前x点坐标（根据动画进度水平推移，一个动画周期推移的距离为一个mWidth）
        x += (percent * mWidth).toInt()
        //波形的起点
        path.moveTo(x.toFloat(), (mHeight / 2).toFloat())
        //控制点的相对宽度
        val quadWidth = mWidth / 4
        //控制点的相对高度
        val quadHeight = mHeight / 20 * 3
        //第一个周期
        path.rQuadTo(quadWidth.toFloat(), quadHeight.toFloat(), (quadWidth * 2).toFloat(), 0f)
        path.rQuadTo(quadWidth.toFloat(), (-quadHeight).toFloat(), (quadWidth * 2).toFloat(), 0f)
        //第二个周期
        path.rQuadTo(quadWidth.toFloat(), quadHeight.toFloat(), (quadWidth * 2).toFloat(), 0f)
        path.rQuadTo(quadWidth.toFloat(), (-quadHeight).toFloat(), (quadWidth * 2).toFloat(), 0f)
        //右侧的直线
        path.lineTo((x + mWidth * 2).toFloat(), mHeight.toFloat())
        //下边的直线
        path.lineTo(x.toFloat(), mHeight.toFloat())
        //自动闭合补出左边的直线
        path.close()
        return path
    }

    private fun drawCenterText(canvas: Canvas, textPaint: Paint?, text: String?) {
        val rect = Rect(0, 0, mWidth, mHeight)
        textPaint!!.textAlign = Paint.Align.CENTER

        val fontMetrics = textPaint.fontMetrics
        //文字框最高点距离baseline的距离（负数）
        val top = fontMetrics.top
        //文字框最低点距离baseline的距离（正数）
        val bottom = fontMetrics.bottom
        //调整到正中位置
        val centerY = (rect.centerY().toFloat() - (top / 2) - (bottom / 2))

        canvas.drawText(text!!, rect.centerX().toFloat(), centerY, textPaint)

    }
}
