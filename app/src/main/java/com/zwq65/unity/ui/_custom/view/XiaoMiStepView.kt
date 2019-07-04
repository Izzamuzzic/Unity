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

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.annotation.Keep
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.SizeUtils
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseSubView
import java.math.BigDecimal

/**
 * ================================================
 * 仿小米运动app步数纪录圆环
 *
 * Created by NIRVANA on 2017/10/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class XiaoMiStepView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseSubView(context, attrs, defStyleAttr) {

    /**
     * 当前用户的步数
     */
    private var mCurrentCount: Int = 0

    /**
     * 用户每日目标的步数
     */
    private var mTargetCount: Int = 0

    private var mBigTextWidth: Float = 0.toFloat()
    private var mBigTextHeight: Float = 0.toFloat()
    private var mTinyTextStartWidth: Float = 0.toFloat()
    private var mTinyTextHeight: Float = 0.toFloat()

    /**
     * 圆环上点和原点的距离,以此来持续纪录运动点的坐标及运动趋势
     */
    private var degree: Float = 0.toFloat()

    /**
     * 主圆环的宽度
     */
    private var mCircleWidth: Float = 0.toFloat()

    /**
     * 主圆环的半径长度
     */
    private var mRadius: Float = 0.toFloat()

    /**
     * 游标点的坐标
     */
    private var cursorPos: FloatArray? = null

    /**
     * 偏移点的趋势(与圆环的正切值)
     */
    private var tan: FloatArray? = null

    private var mPaint: Paint? = null
    private var mCursorPaint: Paint? = null
    private var mCursorMovePaint: Paint? = null
    private var mOffsetPaint1: Paint? = null
    private var mOffsetPaint2: Paint? = null
    private var mOffsetPaint3: Paint? = null
    private var mBigTextPaint: Paint? = null
    private var mTinyTextPaint: Paint? = null

    private var mCirclePath: Path? = null
    private var mCursorCirclePath: Path? = null
    private var mCursorMovePath: Path? = null
    private var mOffsetPath1: Path? = null
    private var mOffsetPath2: Path? = null
    private var mOffsetPath3: Path? = null
    private var mClipPath: Path? = null

    private var mWatchBitmap: Bitmap? = null

    private var mColorWhite: Int = 0
    private var mColorTransWhite: Int = 0

    private var mObjectAnimator: ObjectAnimator? = null

    private var mMatrix: Matrix? = null

    @Keep
    fun setDegree(degree: Float) {
        this.degree = degree
        invalidate()
    }

    public override fun setUp(context: Context, attrs: AttributeSet?) {

        mCurrentCount = 1314
        mTargetCount = 8000
        mCircleWidth = SizeUtils.dp2px(14f).toFloat()

        mColorWhite = ContextCompat.getColor(mContext, R.color.xiaomi_white)
        mColorTransWhite = ContextCompat.getColor(mContext, R.color.xiaomi_white_trans)

        cursorPos = FloatArray(2)
        tan = FloatArray(2)

        mCirclePath = Path()
        mCursorCirclePath = Path()
        mCursorMovePath = Path()
        mOffsetPath1 = Path()
        mOffsetPath2 = Path()
        mOffsetPath3 = Path()
        mClipPath = Path()

        mMatrix = Matrix()

        mWatchBitmap = BitmapFactory.decodeResource(resources, R.mipmap.icon_headview_watch)

        //初始化paint
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = mCircleWidth
        mPaint!!.color = mColorWhite

        mCursorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCursorPaint!!.style = Paint.Style.STROKE
        mCursorPaint!!.strokeWidth = SizeUtils.dp2px(2f).toFloat()
        mCursorPaint!!.pathEffect = DashPathEffect(floatArrayOf(3f, 3f), 0f)
        mCursorPaint!!.color = mColorTransWhite

        mCursorMovePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCursorMovePaint!!.style = Paint.Style.STROKE
        mCursorMovePaint!!.strokeWidth = SizeUtils.dp2px(3f).toFloat()
        mCursorMovePaint!!.color = mColorWhite

        mBigTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBigTextPaint!!.isFakeBoldText = true
        mBigTextPaint!!.strokeCap = Paint.Cap.ROUND
        mBigTextPaint!!.strokeWidth = SizeUtils.dp2px(8f).toFloat()
        mBigTextPaint!!.textSize = SizeUtils.sp2px(50f).toFloat()
        mBigTextPaint!!.color = mColorWhite

        mTinyTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTinyTextPaint!!.textSize = SizeUtils.sp2px(12f).toFloat()
        mTinyTextPaint!!.color = ContextCompat.getColor(mContext, R.color.xiaomi_white_tiny_text)

        mOffsetPaint1 = Paint(Paint.ANTI_ALIAS_FLAG)
        mOffsetPaint1!!.style = Paint.Style.FILL_AND_STROKE
        mOffsetPaint1!!.strokeWidth = SizeUtils.dp2px(1f).toFloat()
        mOffsetPaint1!!.color = ContextCompat.getColor(mContext, R.color.xiaomi_white_offset_trans_1)

        mOffsetPaint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        mOffsetPaint2!!.style = Paint.Style.FILL_AND_STROKE
        mOffsetPaint2!!.strokeWidth = SizeUtils.dp2px(1f).toFloat()
        mOffsetPaint2!!.color = ContextCompat.getColor(mContext, R.color.xiaomi_white_offset_trans_2)

        mOffsetPaint3 = Paint(Paint.ANTI_ALIAS_FLAG)
        mOffsetPaint3!!.style = Paint.Style.FILL_AND_STROKE
        mOffsetPaint3!!.strokeWidth = SizeUtils.dp2px(1f).toFloat()
        mOffsetPaint3!!.color = ContextCompat.getColor(mContext, R.color.xiaomi_white_offset_trans_3)

        //计算居中对齐时需要用到的文字宽高度
        mBigTextWidth = mBigTextPaint!!.measureText(mCurrentCount.toString())
        //分隔符居中,所以偏移 第一个文字到"|"的长度
        val text = getText(mCurrentCount)
        val rect = Rect()
        mTinyTextPaint!!.getTextBounds(text, 0, text.indexOf("|"), rect)
        mTinyTextStartWidth = (rect.right - rect.left).toFloat()
        mBigTextHeight = mBigTextPaint!!.fontSpacing
        mTinyTextHeight = mTinyTextPaint!!.fontSpacing

        //设置背景
        background = ContextCompat.getDrawable(mContext, R.mipmap.bg_step_law)

    }

    /**
     * path添加图形
     */
    private fun setUpPath() {
        mRadius = (mWidth / 3).toFloat()
        /*
         * 添加主圆环部分
         */
        mCirclePath!!.addCircle(0f, 0f, mRadius, Path.Direction.CW)

        /*
         * 添加游标圆环部分
         */
        mCursorCirclePath!!.addCircle(0f, 0f, mRadius - mCircleWidth, Path.Direction.CW)
        val cursorPathMeasure = PathMeasure(mCursorCirclePath, true)
        //测量游标圆环path的长度
        val cursorlength = cursorPathMeasure.length
        //游标点和原点的圆弧长度
        var cursorRate = BigDecimal(mCurrentCount).multiply(BigDecimal(cursorlength.toInt()))
        cursorRate = cursorRate.divide(BigDecimal(mTargetCount), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP)
        //计算游标点的坐标
        cursorPathMeasure.getPosTan(cursorRate.toFloat(), cursorPos, tan)
        //得到原点到游标点的路径path
        cursorPathMeasure.getSegment(0f, cursorRate.toFloat(), mCursorMovePath, true)

        /*
         * 添加3个偏移的圆弧部分
         */
        val offset1 = 8
        val offset2 = 2 * offset1
        val offset3 = 3 * offset1
        //圆弧的半径(-2为微调数值使之与主圆环圆滑兼容)
        val offsetRadius = mRadius + mCircleWidth / 2 - 2
        val rectF1 = RectF(-(offsetRadius + offset1), -offsetRadius, offsetRadius + offset1, offsetRadius)
        mOffsetPath1!!.addArc(rectF1, -90f, 180f)
        val rectF2 = RectF(-(offsetRadius + offset2), -offsetRadius, offsetRadius + offset2, offsetRadius)
        mOffsetPath2!!.addArc(rectF2, -90f, 180f)
        val rectF3 = RectF(-(offsetRadius + offset3), -offsetRadius, offsetRadius + offset3, offsetRadius)
        mOffsetPath3!!.addArc(rectF3, -90f, 180f)

        //添加裁切path
        mClipPath!!.fillType = Path.FillType.INVERSE_EVEN_ODD
        //裁切path圆的半径(+2为微调数值使之与主圆环圆滑兼容,与上边的-2对应。。)
        mClipPath!!.addCircle(0f, 0f, offsetRadius + 2, Path.Direction.CW)
    }

    /**
     * 为Paint添加渐变着色器
     */
    private fun setPaintSHader() {
        //添加LinearGradient‘Shader,主圆环的渐变效果，默认从顶点到底点渐变
        val shader = LinearGradient(mRadius, 0f, -mRadius, 0f, mColorWhite, mColorTransWhite, Shader.TileMode.CLAMP)
        mPaint!!.shader = shader

        //圆弧添加颜色渐变(半透明白到全透明)
        val gradient = SweepGradient(0f, 0f, mColorWhite, ContextCompat.getColor(mContext, R.color.transparent))
        //SweepGradient默认从正右方向开始渐变,使用setLocalMatrix(mMatrix)旋转到我们想要渐变的位置
        mMatrix!!.reset()
        mMatrix!!.setRotate(180f, 0f, 0f)
        gradient.setLocalMatrix(mMatrix)
        mOffsetPaint1!!.shader = gradient
        mOffsetPaint2!!.shader = gradient
        mOffsetPaint3!!.shader = gradient
    }

    /**
     * 开始加载动画
     */
    private fun setUpAnimator() {
        mObjectAnimator = ObjectAnimator.ofFloat(this, "degree", 0f, 360f)
        mObjectAnimator!!.duration = DURATION.toLong()
        mObjectAnimator!!.repeatCount = ValueAnimator.INFINITE
        //添加匀速插值器
        mObjectAnimator!!.interpolator = LinearInterpolator()
        mObjectAnimator!!.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //确认view的宽高后，path添加图形
        setUpPath()
        //为Paint添加渐变着色器
        setPaintSHader()
        //开始加载动画
        setUpAnimator()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制步数、行走距离和消耗千卡文字
        drawText(canvas)
        //绘制主圆环和若干个偏移且渐变的圆弧
        drawRing(canvas)
        //绘制游标的圆环和相应进度
        drawCursor(canvas)
    }

    /**
     * 绘制步数、行走距离和消耗千卡文字
     *
     * @param canvas canvas
     */
    private fun drawText(canvas: Canvas) {
        //绘制大号字体的步数(微调位置)
        canvas.drawText(mCurrentCount.toString(), centerX - mBigTextWidth / 2,
                centerY + mBigTextHeight / 4, mBigTextPaint!!)

        //绘制小号字体的步数和千卡消耗(微调位置)
        canvas.drawText(getText(mCurrentCount), centerX - mTinyTextStartWidth,
                centerY.toFloat() + mBigTextHeight / 4 + mTinyTextHeight * 3 / 2, mTinyTextPaint!!)

        //在文字的正下方绘制一个时钟小图标
        canvas.drawBitmap(mWatchBitmap!!, (centerX - mWatchBitmap!!.width / 2).toFloat(),
                centerY.toFloat() + mBigTextHeight / 4 + mTinyTextHeight * 3 / 2 + mWatchBitmap!!.height.toFloat(), mTinyTextPaint)
    }

    /**
     * 绘制主圆环和若干个偏移且渐变的圆弧
     *
     * @param canvas canvas
     */
    private fun drawRing(canvas: Canvas) {
        canvas.save()
        mMatrix!!.reset()
        //因为圆是从1/4圆弧为原点,所以旋转-90度,游标圆以顶点为原点进行绘制
        mMatrix!!.setRotate(-90 + degree)
        canvas.translate(centerX.toFloat(), centerY.toFloat())
        //用Canvas当前的变换矩阵和Matrix相乘，即基于Canvas当前的变换，叠加上Matrix中的变换。
        canvas.concat(mMatrix)

        //绘制主圆环
        canvas.drawPath(mCirclePath!!, mPaint!!)

        //绘制主圆环外偏移的三个圆弧
        canvas.clipPath(mClipPath!!)
        canvas.drawPath(mOffsetPath3!!, mOffsetPaint3!!)
        canvas.drawPath(mOffsetPath2!!, mOffsetPaint2!!)
        canvas.drawPath(mOffsetPath1!!, mOffsetPaint1!!)

        canvas.restore()
    }

    /**
     * 绘制游标的圆环和相应进度
     *
     * @param canvas canvas
     */
    private fun drawCursor(canvas: Canvas) {
        canvas.save()
        mMatrix!!.reset()
        //因为圆是从1/4圆弧为原点,所以旋转-90度,游标圆以顶点为原点进行绘制
        mMatrix!!.setRotate(-90f)
        canvas.translate(centerX.toFloat(), centerY.toFloat())
        //用Canvas当前的变换矩阵和Matrix相乘，即基于Canvas当前的变换，叠加上Matrix中的变换。
        canvas.concat(mMatrix)
        //绘制游标圆环
        canvas.drawPath(mCursorCirclePath!!, mCursorPaint!!)
        //绘制游标当前点相对的位置
        canvas.drawPoint(cursorPos!![0], cursorPos!![1], mBigTextPaint!!)
        //绘制原点到游标的path路径
        canvas.drawPath(mCursorMovePath!!, mCursorMovePaint!!)
        canvas.restore()
    }

    /**
     * 假设每步行1500步,行走1000m;假设每行走x米，消耗3*x千卡的能量
     *
     * @param currentCount 当前用户的行走步数
     * @return 要绘制的行走距离和消耗千卡字符串
     */
    private fun getText(currentCount: Int): String {
        return (resources.getString(R.string.km, BigDecimal(currentCount)
                .divide(BigDecimal("1500"), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                + resources.getString(R.string.separator)
                + resources.getString(R.string.kcal, (3 * currentCount).toString()))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        //默认宽高一致
        setMeasuredDimension(width, width)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mObjectAnimator != null && mObjectAnimator!!.isRunning) {
            mObjectAnimator!!.end()
        }
    }

    companion object {

        /**
         * 圆环运动一周所用的时间
         */
        private const val DURATION = 8000
    }

}
