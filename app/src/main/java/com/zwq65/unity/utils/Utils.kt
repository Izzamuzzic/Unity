package com.zwq65.unity.utils

import android.graphics.Bitmap
import android.os.Environment
import java.io.*

/**
 *================================================
 * 常用的工具函数(包级函数)
 * Created by NIRVANA on 2018/3/13
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */



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

