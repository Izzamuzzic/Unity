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

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.Keep
import android.util.AttributeSet
import android.view.View

import com.zwq65.unity.R

/**
 * ================================================
 *
 * Created by NIRVANA on 2017/10/24
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class RotateAvatarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0
    private var centerX: Int = 0
    private var centerY: Int = 0
    private var mAvatar: Bitmap? = null
    private var mAnimator: ObjectAnimator? = null
    /**
     * 旋转角度
     */
    private var degree: Int = 0
    private var mCamera: Camera? = null
    private var mPaint: Paint? = null

    @Keep
    fun setDegree(degree: Int) {
        this.degree = degree
        invalidate()
    }

    init {
        mAvatar = BitmapFactory.decodeResource(resources, R.mipmap.ic_avatar)
        bitmapWidth = mAvatar!!.width
        bitmapHeight = mAvatar!!.height
        mCamera = Camera()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mAnimator = ObjectAnimator.ofInt(this, "degree", 0, ROTATE_DEGREE)
        mAnimator!!.duration = 2500
        mAnimator!!.repeatCount = ValueAnimator.INFINITE
        mAnimator!!.repeatMode = ValueAnimator.REVERSE
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAnimator!!.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAnimator!!.end()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制第一部分view
        canvas.save()
        canvas.clipRect(0, 0, mWidth, centerY)
        val top = centerY - bitmapHeight / 2
        val left = centerX - bitmapWidth / 2
        canvas.drawBitmap(mAvatar!!, left.toFloat(), top.toFloat(), mPaint)
        canvas.restore()

        //绘制第二部分view
        canvas.save()
        if (degree < ROTATE_DEGREE / 2) {
            canvas.clipRect(0, centerY, mWidth, mHeight)
        } else {
            canvas.clipRect(0, 0, mWidth, centerY)
        }
        mCamera!!.save()
        mCamera!!.rotateX(degree.toFloat())
        //第二步:恢复到原来位置
        canvas.translate(centerX.toFloat(), centerY.toFloat())
        mCamera!!.applyToCanvas(canvas)
        //第一步(canvas的变换方法需倒着写):移动到原点进行旋转以达到轴心旋转的目的
        canvas.translate((-centerX).toFloat(), (-centerY).toFloat())
        mCamera!!.restore()
        canvas.drawBitmap(mAvatar!!, left.toFloat(), top.toFloat(), mPaint)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        centerX = mWidth / 2
        centerY = mHeight / 2
    }

    companion object {
        private const val ROTATE_DEGREE = 180
    }
}
