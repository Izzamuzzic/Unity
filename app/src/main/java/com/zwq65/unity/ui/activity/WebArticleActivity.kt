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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.gyf.barlibrary.ImmersionBar
import com.zwq65.unity.R
import com.zwq65.unity.data.network.retrofit.response.enity.Article
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.ui._base.BaseDaggerActivity
import com.zwq65.unity.ui.contract.WebArticleContract
import kotlinx.android.synthetic.main.activity_web_article.*
import kotlinx.android.synthetic.main.toolbar_content.*
import java.util.*

/**
 * ================================================
 *
 * Created by NIRVANA on 2017/09/04
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class WebArticleActivity : BaseDaggerActivity<WebArticleContract.View, WebArticleContract.Presenter<WebArticleContract.View>>(), WebArticleContract.View {

    internal var article: Article? = null

    override val layoutId: Int
        get() = R.layout.activity_web_article

    override fun initBaseTooBar(): Boolean {
        return false
    }

    override fun dealIntent(intent: Intent) {
        val bundle = intent.extras
        article = bundle?.getParcelable(ARTICLE)
    }

    override fun initView() {
        initToolBar()
        //set header'background image
        if (article != null) {
            Glide.with(this).load(article?.image?.url).into(iv_title_bg)
            collapsingToolbarLayout?.title = article?.desc
            //WebView
            webview?.settings?.javaScriptEnabled = true
            webview?.settings?.domStorageEnabled = true
            webview?.settings?.cacheMode = WebSettings.LOAD_DEFAULT
            webview?.settings?.setAppCacheEnabled(true)

            webview?.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    progressbar?.progress = newProgress
                    if (newProgress >= 100) {
                        progressbar?.visibility = View.GONE
                    } else {
                        progressbar?.visibility = View.VISIBLE
                    }
                }
            }

            webview?.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view?.loadUrl(request?.url?.toString())
                    }
                    return true
                }
            }
            webview?.loadUrl(article?.url)
        }
    }

    override fun initData() {}

    private fun initToolBar() {
        collapsingToolbarLayout?.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white))
        collapsingToolbarLayout?.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))

        //setup toolbar
        setSupportActionBar(toolbar)
        //添加‘<--’返回功能
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener { this@WebArticleActivity.onBackPressed() }
        //添加了toolbar，重新设置沉浸栏
        ImmersionBar.with(this).titleBar(toolbar!!).init()
    }

    override fun onResume() {
        super.onResume()
        webview?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webview?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        webview?.removeAllViews()
        webview?.destroy()
    }

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
            R.id.action_refresh -> webview?.reload()
            R.id.action_copy_link -> {
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Url", webview?.url)
                clipboardManager.primaryClip = clipData
                showMessage(R.string.sucess_msg_copy)
            }
            R.id.action_open_browser -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webview?.url))
                startActivity(intent)
            }
            else -> {
            }
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview?.canGoBack()!!) {
            webview?.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun gotoContentActivity() {
        val images = ArrayList<Image>(1)
        images.add(article?.image!!)
        val bundle = Bundle()
        bundle.putInt(ImageActivity.POSITION, 0)
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, images)
        openActivity(ImageActivity::class.java, bundle)
    }

    companion object {
        const val ARTICLE = "ARTICLE"
    }
}
