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

import android.animation.AnimatorSet
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.*
import android.support.annotation.Keep
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.blankj.utilcode.util.SizeUtils
import com.jakewharton.rxbinding2.view.RxView
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseSubView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * ================================================
 * 仿即刻大拇指点赞/取消点赞
 *
 * Created by NIRVANA on 2017/10/23
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class ThumbView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseSubView(context, attrs, defStyleAttr), View.OnClickListener {
    /**
     * view的长宽大小
     */
    private var mTextPaint: Paint? = null
    private var mCirclePaint: Paint? = null
    /**
     * 文字绘制原点坐标
     */
    private var mTextPoint: Point? = null
    /**
     * bitmap的padding值
     */
    private var mBitmapPadding: Int = 0

    /**
     * 字体大小
     */
    private val mTextSize = 15

    /**
     * 当前点赞数目、点赞时+1的数目
     */
    private var mCount: Int = 0
    private var mBackCount: Int = 0

    /**
     * 是否为点赞状态
     */
    private var isThumb: Boolean = false

    /**
     * 是否第一次绘制，以此来避免多次绘制浪费资源
     */
    private var isInit: Boolean = false

    /**
     * Bitmap资源文件
     */
    private var unThumbBitmap: Bitmap? = null
    private var thumbBitmap: Bitmap? = null
    private var shiningBitmap: Bitmap? = null

    /**
     * 动画刷新的文字滚动距离
     */
    private var translate: Float = 0.toFloat()

    /**
     * 动画圆圈波动的半径长度
     */
    private var radius: Float = 0.toFloat()

    private var defaultRadius: Float = 0.toFloat()

    /**
     * 动画刷新大拇指的缩放大小
     */
    private var scale = 1f

    private var animatorSet: AnimatorSet? = null

    /**
     * @return 该view的最小宽度
     */
    private val contentWidth: Int
        get() = (thumbBitmap!!.width.toFloat() + mBitmapPadding.toFloat() + mTextPaint!!.measureText(mCount.toString()) + paddingStart.toFloat() + paddingEnd.toFloat()).toInt()

    /**
     * @return 该view的最小高度
     */
    private val contentHeight: Int
        get() = max(thumbBitmap!!.height + shiningBitmap!!.height, SizeUtils.sp2px(mTextSize.toFloat())) + paddingTop + paddingBottom

    fun setCount(count: Int) {
        mCount = count
        mBackCount = count + 1
    }

    @Keep
    fun setTranslate(translate: Float) {
        this.translate = translate
        invalidate()
    }

    fun setScale(scale: Float) {
        this.scale = scale
    }

    @Keep
    fun setRadius(radius: Float) {
        this.radius = radius
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //释放资源
        unThumbBitmap!!.recycle()
        thumbBitmap!!.recycle()
        shiningBitmap!!.recycle()
    }

    public override fun setUp(context: Context, attrs: AttributeSet?) {
        //init paint
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.color = ContextCompat.getColor(mContext, R.color.thumb_gray)
        mTextPaint!!.style = Paint.Style.STROKE
        mTextPaint!!.textSize = SizeUtils.sp2px(mTextSize.toFloat()).toFloat()

        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCirclePaint!!.style = Paint.Style.STROKE
        mCirclePaint!!.color = ContextCompat.getColor(mContext, R.color.thumb_red)
        mCirclePaint!!.strokeWidth = SizeUtils.dp2px(1f).toFloat()

        //init bitmap
        unThumbBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_messages_like_unselected)
        thumbBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_messages_like_selected)
        shiningBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_messages_like_selected_shining)
        mBitmapPadding = SizeUtils.dp2px(2f)

        mTextPoint = Point()
        mTextPoint!!.set(unThumbBitmap!!.width + mBitmapPadding, thumbBitmap!!.height + shiningBitmap!!.height / 2
                - (thumbBitmap!!.height - SizeUtils.sp2px(mTextSize.toFloat())) / 2)
        //init valueAnimator
        //文字滚动动画
        val transAnimator = ObjectAnimator.ofFloat(this, "translate", 0f, mTextPoint!!.y.toFloat())

        //+5微调半径的大小
        defaultRadius = (mTextPoint!!.x / 2 + 5).toFloat()
        //点击圆圈波动动画
        val radiusAnimator = ObjectAnimator.ofFloat(this, "radius", 0f, defaultRadius)
        radiusAnimator.interpolator = AccelerateDecelerateInterpolator()
        //拇指缩放动画
        val keyframe1 = Keyframe.ofFloat(0f, 1.0f)
        val keyframe2 = Keyframe.ofFloat(0.5f, 0.9f)
        val keyframe3 = Keyframe.ofFloat(1f, 1.0f)
        val holder = PropertyValuesHolder.ofKeyframe("scale", keyframe1, keyframe2, keyframe3)
        val scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(this, holder)
        animatorSet = AnimatorSet()
        animatorSet!!.playTogether(transAnimator, scaleAnimator, radiusAnimator)
        animatorSet!!.duration = DURATION.toLong()

        //set OnClickListener
        setOnClickListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //刷新界面完成点赞效果动画
        drawText(canvas)
        drawThumb(canvas)
        drawCircle(canvas)
        //绘制初始状态时的view(init在animate之后调用，避免被animate绘制覆盖)
        firstDraw(canvas)
    }

    /**
     * 文字滚动和颜色改变动画
     *
     * @param canvas canvas
     */
    private fun drawText(canvas: Canvas) {
        canvas.save()
        if (isThumb) {
            //点赞，数字动画方向为从上往下
            canvas.drawText(mCount.toString(), mTextPoint!!.x.toFloat(), mTextPoint!!.y + translate, mTextPaint!!)
            canvas.drawText(mBackCount.toString(), mTextPoint!!.x.toFloat(), translate, mTextPaint!!)
        } else if (isInit) {
            //取消点赞，数字动画方向为从下往上
            canvas.drawText(mBackCount.toString(), mTextPoint!!.x.toFloat(), mTextPoint!!.y - translate, mTextPaint!!)
            canvas.drawText(mCount.toString(), mTextPoint!!.x.toFloat(), 2 * mTextPoint!!.y - translate, mTextPaint!!)
        }
        canvas.restore()
    }

    /**
     * 点赞时产生圆圈的波动动画
     *
     * @param canvas canvas
     */
    private fun drawCircle(canvas: Canvas) {
        //点赞时才绘制该动画
        if (isThumb) {
            if (radius != defaultRadius && radius > defaultRadius / 2) {
                //动画未结束
                canvas.drawCircle(((unThumbBitmap!!.width + mBitmapPadding) / 2).toFloat(), (mHeight / 2).toFloat(), radius, mCirclePaint!!)
            }

        }
    }

    /**
     * 完成大拇指缩放动画,彩虹线的显示隐藏动画
     *
     * @param canvas canvas
     */
    private fun drawThumb(canvas: Canvas) {
        canvas.save()
        canvas.scale(scale, scale, (thumbBitmap!!.width / 2).toFloat(), ((shiningBitmap!!.height / 2 + thumbBitmap!!.height) / 2).toFloat())
        if (isThumb) {
            canvas.drawBitmap(thumbBitmap!!, 0f, (shiningBitmap!!.height / 2).toFloat(), mCirclePaint)
            canvas.drawBitmap(shiningBitmap!!, mBitmapPadding.toFloat(), 0f, mCirclePaint)
        } else {
            canvas.drawBitmap(unThumbBitmap!!, 0f, (shiningBitmap!!.height / 2).toFloat(), mCirclePaint)
        }
        canvas.restore()
    }

    /**
     * 第一次绘制界面(绘制2部分:大拇指、点赞数字)
     *
     * @param canvas canvas
     */
    private fun firstDraw(canvas: Canvas) {
        if (isInit) {
            return
        }
        isInit = true
        //绘制点赞数字
        canvas.drawText(mCount.toString(), mTextPoint!!.x.toFloat(), mTextPoint!!.y.toFloat(), mTextPaint!!)
        //绘制大拇指
        canvas.drawBitmap(unThumbBitmap!!, 0f, (shiningBitmap!!.height / 2).toFloat(), mCirclePaint)
    }

    override fun onClick(v: View) {
        //点击改变拇指点赞状态(throttleFirst:500ms内只处理一次点击事件)
        RxView.clicks(this).throttleFirst(DURATION.toLong(), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { o ->
                    isThumb = !isThumb
                    animatorSet!!.start()
                }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        when (widthMode) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> mWidth = contentWidth
            MeasureSpec.EXACTLY -> mWidth = max(width, contentWidth)
            else -> {
            }
        }
        when (heightMode) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> mHeight = contentHeight
            MeasureSpec.EXACTLY -> mHeight = max(height, contentHeight)
            else -> {
            }
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    companion object {

        /**
         * 动画时长
         */
        private const val DURATION = 300
    }
}
