package com.zwq65.unity.ui._custom.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.blankj.utilcode.util.SizeUtils
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseSubView

/**
 * ================================================
 * 自定义正多边形view(自定义边数量)
 *
 * Created by NIRVANA on 2018/2/9
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class PolygonView @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseSubView(mContext, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint

    override fun setUp(context: Context, attrs: AttributeSet?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.STROKE
        mPaint.color = ContextCompat.getColor(mContext, R.color.colorPrimary)
        mPaint.strokeWidth = SizeUtils.dp2px(2f).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        createPath(3, 80f, canvas)
        createPath(4, 125f, canvas)
        createPath(5, 175f, canvas)
        createPath(6, 225f, canvas)
        createPath(7, 275f, canvas)
        createPath(8, 350f, canvas)
    }

    private fun createPath(sides: Int, radius: Float, canvas: Canvas?) {
        val path = Path()
        val angle = 2.0 * Math.PI / sides
        path.moveTo(
                centerX + (radius * Math.cos(0.0)).toFloat(),
                centerY + (radius * Math.sin(0.0)).toFloat())

        for (i in 1 until sides) {
            path.lineTo(centerX + (radius * Math.cos(angle * i)).toFloat(),
                    centerY + (radius * Math.sin(angle * i)).toFloat())
        }
        path.close()
        canvas?.save()
        canvas?.drawPath(path, mPaint)
        canvas?.rotate(Math.toRadians(angle / 2).toFloat(), centerX.toFloat(), centerY.toFloat())
        canvas?.restore()
    }

}
