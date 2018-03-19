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

package com.zwq65.unity.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import butterknife.OnClick
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseDaggerActivity
import com.zwq65.unity.ui.contract.AccountContract
import kotlinx.android.synthetic.main.activity_account.*

/**
 * ================================================
 * 个人中心
 *
 * Created by NIRVANA on 2017/08/07
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class AccountActivity : BaseDaggerActivity<AccountContract.View, AccountContract.Presenter<AccountContract.View>>(), AccountContract.View {

    override val layoutId: Int
        get() = R.layout.activity_account

    override fun initBaseTooBar(): Boolean {
        return false
    }

    override fun dealIntent(intent: Intent) {

    }

    override fun initView() {

        // 设置参数
        val options = BitmapFactory.Options()
        // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.mipmap.ic_avatar, options)
        val height = options.outHeight
        val width = options.outWidth
        Log.w("TAG", " width: $width height:$height")
        /**
         * 不进行压缩
         */
        val bitmap1 = BitmapFactory.decodeResource(resources, R.mipmap.ic_avatar)

        /**
         * 采样率压缩：根据图片的采样率大小进行压缩
         */
        // 计算好压缩比例后，这次可以去加载原图了
        options.inJustDecodeBounds = false
        //采样率设置:压缩比例为1/2
        options.inSampleSize = 2

        /*
         ALPHA_8表示只有透明度A=8,没有颜色RGB, 1个像素占8位=1字节。
         ARGB_4444表示A=4,R=4,G=4,B=4, 1个像素占4+4+4+4=16位=2字节，ARGB_4444的画质惨不忍睹，所以弃用。
         ARGB_8888表示A=8,R=8,G=8,B=8, 1个像素占8+8+8+8=32位=4字节 。
         RGB_565表示没有透明度A，R=5,G=6,B=5, 1个像素占5+6+5=16位=2字节。
         如果没有透明度A需求，将ARGB_8888改为RGB_565可以降低1个像素所占字节，Bitmap占用内存也就降低
         */
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //bitmap壓縮之邻近采样（Nearest Neighbour Resampling）
        val bitmap2 = BitmapFactory.decodeResource(resources, R.mipmap.ic_avatar, options)

        //bitmap壓縮之双线性采样（Bilinear Resampling）
        //方法一:
        val bitmap3 = Bitmap.createScaledBitmap(bitmap1, bitmap1.width / 2, bitmap1.height / 2, true)
        ////方法二:（直接使用matrix进行缩放）
        //        Matrix matrix = new Matrix();
        //        matrix.setScale(0.5f, 0.5f);
        //        Bitmap bitmap3 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
        iv_1?.setImageBitmap(bitmap1)
        iv_2?.setImageBitmap(bitmap2)
        iv_3?.setImageBitmap(bitmap3)
    }

    override fun initData() {

    }

    @OnClick(R.id.iv_back)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.iv_back -> finish()
            else -> {
            }
        }
    }
}
