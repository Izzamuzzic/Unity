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
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.gyf.barlibrary.ImmersionBar
import com.zwq65.unity.R
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.data.network.retrofit.response.enity.Video
import com.zwq65.unity.ui._base.BaseDaggerActivity
import com.zwq65.unity.ui.contract.WatchContract
import kotlinx.android.synthetic.main.toolbar_content.*
import java.util.*

/**
 * ================================================
 * 视频播放
 *
 * Created by NIRVANA on 2017/06/29.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class WatchActivity : BaseDaggerActivity<WatchContract.View, WatchContract.Presenter<WatchContract.View>>(), WatchContract.View {

    internal var video: Video? = null

    override val layoutId: Int
        get() = R.layout.activity_watch

    override fun initBaseTooBar(): Boolean {
        return false
    }

    override fun dealIntent(intent: Intent) {
        val bundle = intent.extras
        video = bundle?.getParcelable(VIDEO)
    }

    override fun initView() {
        //set header'background image
        if (video != null) {
            Glide.with(this).load(video?.image?.url).into(iv_title_bg!!)
            collapsingToolbarLayout?.title = video?.desc
        }
        collapsingToolbarLayout?.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white))
        collapsingToolbarLayout?.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
        //setup toolbar
        setSupportActionBar(toolbar)
        //添加‘<--’返回功能
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener { v -> this@WatchActivity.onBackPressed() }
        //添加了toolbar，重新设置沉浸栏
        ImmersionBar.with(this).titleBar(toolbar!!).init()
    }

    override fun initData() {}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_collect_bg_image ->
                //查看背景图
                gotoContentActivity()
            else -> {
            }
        }
        return true
    }

    private fun gotoContentActivity() {
        val images = ArrayList<Image>(1)
        images.add(video?.image!!)
        val bundle = Bundle()
        bundle.putInt(ImageActivity.POSITION, 0)
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, images)
        openActivity(ImageActivity::class.java, bundle)
    }

    companion object {
        //intent'key value
        const val VIDEO = "VIDEO"
    }

}
