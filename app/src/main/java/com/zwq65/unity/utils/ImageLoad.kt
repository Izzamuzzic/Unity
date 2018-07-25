package com.zwq65.unity.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import java.lang.ref.WeakReference

/**
 *================================================
 * 图片加载函数类
 *
 * Created by NIRVANA on 2018/7/25
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */

/**
 * 加载图片url
 * @param url 图片url
 */
fun ImageView.loadUrl(url: String?) {
    // 具体图片加载实现可以使用第三方框架加载，也可以自己实现，
    var requestOptions = RequestOptions().centerCrop()
            .transform(CenterCrop())
            .format(DecodeFormat.PREFER_RGB_565)
            .priority(Priority.NORMAL)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    Glide.with(context.applicationContext)
            .load(url)
            .apply(requestOptions)
            .into(this)
}

fun load(context: WeakReference<Context>, url: String?, image: ImageView?, transformation: BitmapTransformation) {
    if (image == null) return
    // 具体图片加载逻辑
}

fun load(holder: Int, context: WeakReference<Context>, url: String, image: ImageView?, width: Int, height: Int) {
    if (image == null) return
    // 具体图片加载逻辑
}

fun loadCircle(context: WeakReference<Context>, url: String?, image: ImageView?, width_height: Int) {
    if (image == null) return
    // 具体图片加载逻辑
}

fun loadRound(context: WeakReference<Context>, url: String, image: ImageView?, width: Int, height: Int, round: Int) {
    if (image == null) return
    // 具体图片加载逻辑
}

fun clearCache(context: WeakReference<Context>) {
    // 强制清楚缓存，可以为内存缓存也可以为硬盘缓存
    Glide.get(context.get()!!.applicationContext).clearMemory()
    System.gc()
}
