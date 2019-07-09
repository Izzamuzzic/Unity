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

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.gyf.barlibrary.ImmersionBar
import com.zwq65.unity.R
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.ui._base.BaseDaggerActivity
import com.zwq65.unity.ui.contract.ImageContract
import com.zwq65.unity.utils.LogUtils
import kotlinx.android.synthetic.main.activity_image.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * ================================================
 * 查看大图
 *
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class ImageActivity : BaseDaggerActivity<ImageContract.View, ImageContract.Presenter<ImageContract.View>>(), ImageContract.View, EasyPermissions.PermissionCallbacks {

    private var cbLove: AppCompatCheckBox? = null

    internal var currentPosition: Int = 0
    private var pageSize: Int = 0
    internal var imageList: List<Image> = ArrayList()

    override val layoutId: Int
        get() = R.layout.activity_image

    override fun initBaseTooBar(): Boolean {
        return false
    }

    override fun dealIntent(intent: Intent) {
        val bundle = intent.extras
        if (bundle != null) {
            currentPosition = bundle.getInt(POSITION)
            imageList = bundle.getParcelableArrayList(IMAGE_LIST)
        }
        pageSize = imageList.size
    }

    override fun initView() {
        initToolbar()
        initViewPager()
        setCurrentPage()
    }

    override fun initData() {
        //empty
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_meizhi, menu)
        //找到checkbox设置下样式
        cbLove = menu.findItem(R.id.menu_cb_love).actionView as AppCompatCheckBox
        cbLove?.setButtonDrawable(R.drawable.selector_ic_love)
        cbLove?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mPresenter.collectPicture(imageList[currentPosition])
            } else {
                mPresenter.cancelCollectPicture(imageList[currentPosition])
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_save -> {
                //保存大图
                if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //未获取权限
                    LogUtils.e("未获取权限")
                    val perms = ArrayList<String>()
                    perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                        LogUtils.e("不再提醒")
                        AppSettingsDialog.Builder(this).build().show()
                    } else {
                        EasyPermissions.requestPermissions(this, "請求存儲權限", SAVE_MEIZHI,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                } else {
                    LogUtils.i("已获取权限")
                    savePicture()
                }
                requestPermissionsSafely(Manifest.permission.WRITE_EXTERNAL_STORAGE, SAVE_MEIZHI)
            }
            else -> {
            }
        }
        return true
    }

    private fun savePicture() {
        mPresenter.savePicture(this, imageList[currentPosition])
    }

    /**
     * 回调中判断权限是否申请成功
     *
     * @param requestCode  The request code passed in [.requestPermissions].
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [android.content.pm.PackageManager.PERMISSION_GRANTED]
     * or [android.content.pm.PackageManager.PERMISSION_DENIED]. Never null.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        if (requestCode == SAVE_MEIZHI) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                LogUtils.i("SAVE SUCCESS")
            } else {
                LogUtils.e("SAVE FAIL")
            }
        }
    }

    private fun initToolbar() {
        //setup toolbar
        setSupportActionBar(toolbar)
        //添加‘<--’返回功能
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp)
        //添加了toolbar，重新设置沉浸式状态栏
        ImmersionBar.with(this).titleBar(toolbar!!).init()
    }

    /**
     * 设置当前页数
     */
    private fun setCurrentPage() {
        //改变toolbar标题为图片desc
        if (supportActionBar != null) {
            if (imageList[currentPosition].desc != null) {
                supportActionBar?.title = imageList[currentPosition].desc
            }
        }
        //当前页码
        if (pageSize <= 1) {
            tv_current_page?.visibility = View.GONE
        } else {
            tv_current_page?.text = resources.getString(R.string.placeholder_divider, (currentPosition + 1).toString(), pageSize.toString())
        }
        //当前图片是否已被用户收藏
        mPresenter.isPictureCollect(imageList[currentPosition])
                .compose(bindUntilStopEvent())
                .subscribe { aBoolean ->
                    cbLove?.isChecked = aBoolean
                }
    }

    private fun initViewPager() {
        //预加载2个item
        vp_images?.offscreenPageLimit = 2
        val mAdapter = MyAdapter()
        vp_images?.adapter = mAdapter
        //设置当前加载的资源为点击进入的图片
        vp_images?.currentItem = currentPosition
        vp_images?.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                //滑动改变当前页数
                currentPosition = position
                setCurrentPage()
            }
        })
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        LogUtils.i("SAVE SUCCESS!!!!")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        LogUtils.e("SAVE FAIL!!!")
    }

    /**
     * 显示大图viewpager's adapter
     */
    private inner class MyAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return imageList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            //显示大图view
            val view = layoutInflater.inflate(R.layout.item_image, container, false)
            val ivImage = view.findViewById<PhotoView>(R.id.iv_image)

            val image = imageList[position]
            Glide.with(this@ImageActivity).load(image.url).into(ivImage)
            container.addView(view, 0)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    companion object {
        const val POSITION = "POSITION"
        const val IMAGE_LIST = "IMAGE_LIST"
        private const val SAVE_MEIZHI = 1
    }
}
