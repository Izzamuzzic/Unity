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

package com.zwq65.unity.ui._base

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.zwq65.unity.R
import com.zwq65.unity.utils.LogUtils

/**
 * ================================================
 * activity基类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
abstract class BaseActivity : RxAppCompatActivity() {

    val TAG = javaClass.simpleName
    private var fragmentManager: FragmentManager? = null

    /**
     * 获取Activity的视图资源id
     *
     * @return Resource ID to be inflated
     */
    @get:LayoutRes
    abstract val layoutId: Int

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = layoutId
        if (layoutId != 0) {
            setContentView(layoutId)
        }
        LogUtils.i(TAG, "onCreate")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.i(TAG, "onPause")
    }

    override fun onDestroy() {
        LogUtils.i(TAG, "onDestroy")
        super.onDestroy()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permission: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(permission)
            requestPermissionsSafely(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @JvmOverloads
    fun openActivity(cls: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, cls)
        //添加intentFlag,如果要启动的activity存在于栈中,将其拉到栈顶,不用重新实例化
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        //add animation
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //add animation
        overridePendingTransition(R.anim.right_in_back, R.anim.right_out_back)
    }

    /**
     * 切换fragment
     * hide当前显示的fragment，若已添加show,否则add
     *
     * @param containerViewId 容器view
     * @param targetFragment  要切换的fragment
     * @param tag             fragment'tag
     */
    fun switchFragment(containerViewId: Int, targetFragment: Fragment, tag: String) {
        if (fragmentManager == null) {
            fragmentManager = supportFragmentManager
        }
        val transaction = fragmentManager!!.beginTransaction()
        val existFragment = fragmentManager!!.findFragmentByTag(tag)
        if (existFragment != null) {
            //已添加( ⊙o⊙ ),show()
            currentFragment?.let { transaction.hide(it) }
            transaction.show(existFragment).commit()
            currentFragment = existFragment
        } else {
            //还没添加,add()
            if (!targetFragment.isAdded) {
                currentFragment?.let { transaction.hide(it) }
                transaction.add(containerViewId, targetFragment, tag).commit()
            } else {
                //已添加( ⊙o⊙ ),show()
                currentFragment?.let { transaction.hide(it) }
                transaction.show(targetFragment).commit()
            }
            currentFragment = targetFragment
        }
    }
}
