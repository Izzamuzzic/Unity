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

package com.zwq65.unity.ui._custom.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zwq65.unity.R;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/10/24
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class RotateAvatarView extends View {
    private int width, height, bitmapWidth, bitmapHeight, centerX, centerY;
    private Bitmap mAvatar;
    private ObjectAnimator mAnimator;
    /**
     * 旋转角度
     */
    private int degree;
    private static final int ROTATE_DEGREE = 180;
    private Camera mCamera;
    private Paint mPaint;

    @Keep
    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    public RotateAvatarView(Context context) {
        this(context, null);
    }

    public RotateAvatarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateAvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mAvatar = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar);
        bitmapWidth = mAvatar.getWidth();
        bitmapHeight = mAvatar.getHeight();
        mCamera = new Camera();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAnimator = ObjectAnimator.ofInt(this, "degree", 0, ROTATE_DEGREE);
        mAnimator.setDuration(5000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimator.end();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制第一部分view
        canvas.save();
        canvas.clipRect(0, 0, width, centerY);
        int top = centerY - bitmapHeight / 2;
        int left = centerX - bitmapWidth / 2;
        canvas.drawBitmap(mAvatar, left, top, mPaint);
        canvas.restore();

        //绘制第二部分view
        canvas.save();
        if (degree < ROTATE_DEGREE / 2) {
            canvas.clipRect(0, centerY, width, height);
        } else {
            canvas.clipRect(0, 0, width, centerY);
        }
        mCamera.save();
        mCamera.rotateX(degree);
        canvas.translate(centerX, centerY);
        mCamera.applyToCanvas(canvas);
        canvas.translate(-centerX, -centerY);
        mCamera.restore();
        canvas.drawBitmap(mAvatar, left, top, mPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        centerX = width / 2;
        centerY = height / 2;
    }
}
