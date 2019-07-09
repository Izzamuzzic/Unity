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
import android.graphics.Point
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import com.blankj.utilcode.util.SizeUtils
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseSubView
import kotlin.math.abs
import kotlin.math.sin

/**
 * ================================================
 * 仿掌上英雄联盟的对局能力分布图
 *
 *
 * Created by NIRVANA on 2017/10/19
 * Contact with <zwq651406441></zwq651406441>@gmail.com>
 * ================================================
 */
class LolAbilityView @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseSubView(mContext, attrs, defStyleAttr) {

    private var mPaint: Paint? = null
    private var radiusPaint: Paint? = null
    private var textPaint: Paint? = null
    private var valuePaint: Paint? = null
    private var pathHeptagon: Path? = null
    private val sideNum = 7
    /**
     * 七边形的半径长度
     */
    private var radius: Int = 0
    /**
     * 七边形的内角角度
     */
    private var degree = (360 / sideNum + 0.5).toFloat()
    /**
     * 6个点文字的坐标数组
     */
    private val mPoints = arrayOfNulls<Point>(7)
    /**
     * 6种能力的名称
     */
    private val abilityName = arrayOf("击杀", "生存", "助攻", "物理", "魔法", "防御", "金钱")
    /**
     * 6种能力的分值
     */
    private val abilityValue = floatArrayOf(50f, 70f, 87f, 18f, 46f, 35f, 34f)

    public override fun setUp(context: Context, attrs: AttributeSet?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        radiusPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        valuePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        radiusPaint!!.color = ContextCompat.getColor(context, R.color.heptagon_3)
        textPaint!!.color = ContextCompat.getColor(context, R.color.text_color_dark)
        valuePaint!!.color = ContextCompat.getColor(context, R.color.value_color)
        textPaint!!.textSize = 24f
        radiusPaint!!.strokeWidth = 2f
        radiusPaint!!.style = Paint.Style.STROKE
        valuePaint!!.strokeWidth = 5f
        valuePaint!!.style = Paint.Style.STROKE
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (pathHeptagon == null) {
            pathHeptagon = getPathHeptagon()
        }
        drawText(canvas)
        drawHeptagon(canvas)
        drawHeptagonRadius(canvas)
        drawAbilityPath(canvas)
    }

    /**
     * 绘制能力值分布曲线path
     *
     * @param canvas 画布
     */
    private fun drawAbilityPath(canvas: Canvas) {
        val path = Path()
        for (i in abilityValue.indices) {
            val diff = getDiffrence(mPoints[i]!!, abilityValue[i])
            when (i) {
                0 -> path.moveTo(centerX + diff[0], centerY - diff[1])
                1 -> path.lineTo(centerX + diff[0], centerY - diff[1])
                2, 3 -> path.lineTo(centerX + diff[0], centerY + diff[1])
                4, 5 -> path.lineTo(centerX - diff[0], centerY + diff[1])
                6 -> path.lineTo(centerX - diff[0], centerY - diff[1])
                else -> {
                }
            }
        }
        path.close()
        canvas.drawPath(path, valuePaint!!)
    }

    private fun getDiffrence(distance: Point, value: Float): FloatArray {
        val xx = abs(distance.x - centerX).toFloat()
        val yy = abs(distance.y - centerY).toFloat()
        return floatArrayOf(xx * (value / 100), yy * (value / 100))
    }

    /**
     * 绘制七边形的7条半径
     *
     * @param canvas 画布
     */
    private fun drawHeptagonRadius(canvas: Canvas) {
        canvas.save()
        canvas.rotate(0f, centerX.toFloat(), centerY.toFloat())
        for (j in 0 until sideNum + 1) {
            canvas.drawLine(centerX.toFloat(), centerY.toFloat(), centerX.toFloat(), (centerY - radius).toFloat(), radiusPaint!!)
            //旋转7次，绘制七边形的7条半径
            canvas.rotate(degree, centerX.toFloat(), centerY.toFloat())
        }
        canvas.restore()
    }

    /**
     * 绘制七边形Heptagon
     *
     * @param canvas 画布
     */
    private fun drawHeptagon(canvas: Canvas) {
        canvas.save()
        mPaint!!.color = ContextCompat.getColor(context, R.color.heptagon_4)
        canvas.drawPath(pathHeptagon!!, mPaint!!)
        //缩放canvas，绘制多个同心七边形
        canvas.scale(0.75f, 0.75f, centerX.toFloat(), centerY.toFloat())
        mPaint!!.color = ContextCompat.getColor(context, R.color.heptagon_3)
        canvas.drawPath(pathHeptagon!!, mPaint!!)
        canvas.restore()
        canvas.save()
        canvas.scale(0.5f, 0.5f, centerX.toFloat(), centerY.toFloat())
        mPaint!!.color = ContextCompat.getColor(context, R.color.heptagon_2)
        canvas.drawPath(pathHeptagon!!, mPaint!!)
        canvas.restore()
        canvas.save()
        canvas.scale(0.25f, 0.25f, centerX.toFloat(), centerY.toFloat())
        mPaint!!.color = ContextCompat.getColor(context, R.color.heptagon_1)
        canvas.drawPath(pathHeptagon!!, mPaint!!)
        canvas.restore()
    }

    /**
     * 绘制六个能力text
     *
     * @param canvas 画布
     */
    private fun drawText(canvas: Canvas) {
        val interval = SizeUtils.dp2px(10f).toFloat()
        for (i in mPoints.indices) {
            when (i) {
                0 -> canvas.drawText(abilityName[i], (mPoints[i]!!.x - 1.5 * interval).toFloat(), mPoints[i]!!.y - interval, textPaint!!)
                1 -> canvas.drawText(abilityName[i], mPoints[i]!!.x + interval, mPoints[i]!!.y.toFloat(), textPaint!!)
                2 -> canvas.drawText(abilityName[i], mPoints[i]!!.x + interval, mPoints[i]!!.y + interval, textPaint!!)
                3 -> canvas.drawText(abilityName[i], mPoints[i]!!.x + interval, mPoints[i]!!.y + 2 * interval, textPaint!!)
                4 -> canvas.drawText(abilityName[i], mPoints[i]!!.x - 3 * interval, mPoints[i]!!.y + 2 * interval, textPaint!!)
                5 -> canvas.drawText(abilityName[i], mPoints[i]!!.x - 3 * interval, mPoints[i]!!.y + interval, textPaint!!)
                6 -> canvas.drawText(abilityName[i], mPoints[i]!!.x - 3 * interval, mPoints[i]!!.y.toFloat(), textPaint!!)
                else -> {
                }
            }
        }
    }

    /**
     * @return 七边形的边path
     */
    private fun getPathHeptagon(): Path {
        val path = Path()

        path.moveTo(centerX.toFloat(), (centerY - radius).toFloat())
        mPoints[0] = Point(centerX, centerY - radius)
        //根据已知角度分别计算来绘制七边形的7条边
        val l1 = (sin(Math.toRadians(degree.toDouble())) * radius).toInt()
        val l2 = ((1 - sin(Math.toRadians((90 - degree).toDouble()))) * radius).toInt()
        path.rLineTo(l1.toFloat(), l2.toFloat())
        mPoints[1] = Point(centerX + l1, centerY - radius + l2)
        val l3 = (sin(Math.toRadians(1.5 * degree)) * radius).toInt()
        val l4 = (sin(Math.toRadians(90 - 1.5 * degree)) * radius).toInt()
        path.lineTo((centerX + l3).toFloat(), (centerY + l4).toFloat())
        mPoints[2] = Point(centerX + l3, centerY + l4)
        val l5 = (sin(Math.toRadians(0.5 * degree)) * radius).toInt()
        val l6 = (sin(Math.toRadians(90 - 0.5 * degree)) * radius).toInt()
        path.lineTo((centerX + l5).toFloat(), (centerY + l6).toFloat())
        mPoints[3] = Point(centerX + l5, centerY + l6)
        //对称，所以另外半边将x坐标改为'-'即可
        path.rLineTo((-2 * l5).toFloat(), 0f)
        mPoints[4] = Point(centerX - l5, centerY + l6)
        path.moveTo(centerX.toFloat(), (centerY - radius).toFloat())
        path.rLineTo((-l1).toFloat(), l2.toFloat())
        mPoints[6] = Point(centerX - l1, centerY - radius + l2)
        path.lineTo((centerX - l3).toFloat(), (centerY + l4).toFloat())
        mPoints[5] = Point(centerX - l3, centerY + l4)
        path.lineTo((centerX - l5).toFloat(), (centerY + l6).toFloat())
        return path
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        radius = height / 5
        setMeasuredDimension(width, height)
    }
}
