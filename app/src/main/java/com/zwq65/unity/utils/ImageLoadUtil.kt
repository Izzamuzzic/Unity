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
import com.bumptech.glide.request.target.DrawableImageViewTarget
import java.lang.ref.WeakReference

/**
 *================================================
 *
 * Created by NIRVANA on 2018/3/19
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
class ImageLoadUtil {
    open fun load(context: WeakReference<Context>, url: String?, image: ImageView?) {
        if (image == null) return
        // 具体图片加载实现可以使用第三方框架加载，也可以自己实现
        var requestOptions = RequestOptions().centerCrop()
                .transform(CenterCrop())
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.LOW)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        Glide.with(context.get()!!.applicationContext)
                .load(url)
                .apply(requestOptions)
                .into(object : DrawableImageViewTarget(image) {
                })
    }

    open fun load(context: WeakReference<Context>, url: String?, image: ImageView?, transformation: BitmapTransformation) {
        if (image == null) return
        // 具体图片加载逻辑
    }

    open fun load(holder: Int, context: WeakReference<Context>, url: String, image: ImageView?, width: Int, height: Int) {
        if (image == null) return
        // 具体图片加载逻辑
    }

    open fun loadCircle(context: WeakReference<Context>, url: String?, image: ImageView?, width_height: Int) {
        if (image == null) return
        // 具体图片加载逻辑
    }

    open fun loadRound(context: WeakReference<Context>, url: String, image: ImageView?, width: Int, height: Int, round: Int) {
        if (image == null) return
        // 具体图片加载逻辑
    }

    open fun clearCache(context: WeakReference<Context>) {
        // 强制清楚缓存，可以为内存缓存也可以为硬盘缓存
        Glide.get(context.get()!!.applicationContext).clearMemory()
        System.gc()
    }
}