package com.zwq65.unity.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.support.annotation.ColorInt
import android.support.annotation.IdRes
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.blankj.utilcode.util.BarUtils
import com.zwq65.unity.R
import java.io.*
import java.lang.reflect.Field

/**
 *================================================
 * 常用的工具函数(包级函数)
 * Created by NIRVANA on 2018/3/13
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */

fun <T : View> bind(root: View, @IdRes res: Int): T {
    @Suppress("UNCHECKED_CAST")
    return root.findViewById(res) as T
}

/**
 * 实现沉浸式状态栏
 *
 * @param isDark         是否沉浸状态栏为深色
 * @param statusBarColor 状态栏颜色
 */
fun setTransStatusBar(activity: Activity, isDark: Boolean, @ColorInt statusBarColor: Int) {
    val mWindow = activity.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (!isDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Android6.0以上系统设置浅色状态栏文字适应
                mWindow.statusBarColor = statusBarColor
                mWindow.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //Android6.0以下系统无法应用浅色状态栏，所以改成灰色背景；
                mWindow.statusBarColor = ContextCompat.getColor(activity, R.color.iron)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Android6.0以上系统恢复默认状态栏文字适应
                mWindow.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE
            }
            mWindow.statusBarColor = statusBarColor
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //android4.4
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val systemContent = activity.findViewById(android.R.id.content) as ViewGroup
        val statusBarView = View(activity)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight())
        if (!isDark) {
            //Android6.0以下系统无法应用浅色状态栏，所以改成灰色背景；
            statusBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.iron))
        } else {
            statusBarView.setBackgroundColor(statusBarColor)
        }
        systemContent.getChildAt(0).fitsSystemWindows = true
        systemContent.addView(statusBarView, 0, lp)
    }
}

/**
 * 设置界面全屏在状态栏底部开始显示，透明状态栏
 */
fun setBarFullScreen(activity: Activity, isLightbar: Boolean = false) {
    val window = activity.window

    //sdk > 21
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // 部分机型的statusBar会有半透明的黑色背景
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (isLightbar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    } else {
        //sdk 19
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val contentView = window.decorView.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
        contentView.getChildAt(0).fitsSystemWindows = false
    }
}

/**
 * 设置[TabLayout]下划线宽度
 *
 * @param tabs
 * @param leftDip
 * @param rightDip
 */
fun setIndicator(tabs: TabLayout, leftDip: Int, rightDip: Int) {
    val tabLayout = tabs.javaClass
    var tabStrip: Field? = null
    try {
        tabStrip = tabLayout.getDeclaredField("mTabStrip")
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }

    if (tabStrip != null) {
        tabStrip.isAccessible = true
        var llTab: LinearLayout? = null
        try {
            llTab = tabStrip.get(tabs) as LinearLayout
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), Resources.getSystem().displayMetrics).toInt()
        val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), Resources.getSystem().displayMetrics).toInt()

        if (llTab != null) {
            for (i in 0 until llTab.childCount) {
                val child = llTab.getChildAt(i)
                child.setPadding(0, 0, 0, 0)
                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                params.leftMargin = left
                params.rightMargin = right
                child.layoutParams = params
                child.invalidate()
            }
        }
    }
}

var sNoncompatDensity = 0f
var sNoncompatScaledDensity = 0f

/**
 * https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
 * 今日头条屏幕适配方案
 * 在 Activity#onCreate 方法中调用下
 * 以下代码以设计图宽360dp去适配的，如果要以高维度适配，可以再扩展下代码即可
 * @param widthDp 设计图屏幕宽度dp值
 */
fun setCustomDensity(activity: Activity, application: Application, widthDp: Float) {
    val appDisplayMetrics = application.resources.displayMetrics
    if (sNoncompatDensity == 0F) {
        sNoncompatDensity = appDisplayMetrics.density
        sNoncompatScaledDensity = appDisplayMetrics.scaledDensity
        application.registerComponentCallbacks(object : ComponentCallbacks {
            override fun onLowMemory() {
            }

            override fun onConfigurationChanged(newConfig: Configuration?) {
                if (newConfig != null && newConfig.fontScale > 0) {
                    sNoncompatScaledDensity = application.resources.displayMetrics.scaledDensity
                }
            }
        })
    }
    val targetDensity: Float = (appDisplayMetrics.widthPixels.toFloat() / widthDp)
    val targetScaledDensity: Float = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity)
    val targetDensityDpi: Int = (160 * targetDensity).toInt()

//    appDisplayMetrics.density = targetDensity
//    appDisplayMetrics.scaledDensity = targetScaledDensity
//    appDisplayMetrics.densityDpi = targetDensityDpi

    val activityDisplayMetrics: DisplayMetrics = activity.resources.displayMetrics
    activityDisplayMetrics.density = targetDensity
    activityDisplayMetrics.scaledDensity = targetScaledDensity
    activityDisplayMetrics.densityDpi = targetDensityDpi
}

/**
 * 用于取得recycleView当前最大的position以判断是否许需要加载
 *
 * @param lastPositions recycleView底部的position数组
 * @return 最大的position
 */
fun findMax(lastPositions: IntArray): Int {
    return lastPositions.max()
            ?: lastPositions[0]
}

/**
 * 将File写入到指定路径下
 * @param bitmap
 * @param path
 * @return void
 */
fun saveFileToSdcard(bitmap: Bitmap?, path: String) {
    val file = File(path)
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    if (!file.exists()) {
        val outputStream: FileOutputStream
        var array: ByteArray? = null
        try {
            outputStream = FileOutputStream(file)
            if (null != bitmap) {
                val os = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                array = os.toByteArray()
                os.close()
            }
            outputStream.write(array!!)
            outputStream.flush()
            outputStream.close()
            LogUtils.i("AppFileMgr-->>saveFileToSdcard-->>bitmap:", bitmap!!.toString())
            LogUtils.i("AppFileMgr-->>saveFileToSdcard-->>path:", path)
            LogUtils.i("AppFileMgr-->>saveFileToSdcard:", "将File写入到指定路径下成功！")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            LogUtils.e("AppFileMgr-->>saveFileToSdcard:", "将File写入到指定路径下失败！" + e.message)
        } catch (e1: IOException) {
            e1.printStackTrace()
            LogUtils.e("AppFileMgr-->>saveFileToSdcard:", "将File写入到指定路径下失败！" + e1.message)
        }

    }
}

/**
 * 获取手机内存AbsolutePath存储路径
 * @return String
 */
fun getDataAbsolutePath(): String {
    return Environment.getDataDirectory().absolutePath + File.separator
}


/**
 * 检查SDCard是否可用，是否存在
 * @return boolean
 */
fun getSdCardIsEnable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

/**
 * 获取SdCard的Path路径
 * @return String
 */
fun getSdCardPath(): String {
    return Environment.getExternalStorageDirectory().path + File.separator
}
