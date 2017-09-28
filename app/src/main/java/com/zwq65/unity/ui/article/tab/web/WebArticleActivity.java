package com.zwq65.unity.ui.article.tab.web;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseViewActivity;
import com.zwq65.unity.ui.album.image.ImageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WebArticleActivity extends BaseViewActivity<WebArticleContract.View,
        WebArticleContract.Presenter<WebArticleContract.View>> implements WebArticleContract.View {
    public static final String ARTICAL = "ARTICAL";

    Article article;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.iv_title_bg)
    ImageView ivTitleBg;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_article;
    }

    @Override
    public Boolean initBaseTooBar() {
        return false;
    }

    @Override
    public void dealIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        article = bundle.getParcelable(ARTICAL);
    }

    @Override
    public void initView() {
        initToolBar();
        //set header'background image
        if (article != null) {
            Glide.with(this).load(article.getImageUrl()).into(ivTitleBg);
            collapsingToolbarLayout.setTitle(article.getDesc());
            //WebView
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webview.getSettings().setAppCacheEnabled(true);

            webview.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    progressbar.setProgress(newProgress);
                    if (newProgress >= 100) {
                        progressbar.setVisibility(View.GONE);
                    } else {
                        progressbar.setVisibility(View.VISIBLE);
                    }
                }
            });

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webview.loadUrl(article.getUrl());
        }
    }

    @Override
    public void initData() {
    }

    private void initToolBar() {
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        //setup toolbar
        setSupportActionBar(toolbar);
        //添加‘<--’返回功能
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> WebArticleActivity.this.onBackPressed());
        //添加了toolbar，重新设置沉浸栏
        ImmersionBar.with(this).titleBar(toolbar).init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webview != null) {
            webview.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webview != null) {
            webview.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            webview.removeAllViews();
            webview.destroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_collect_bg_image:
                //查看背景图
                gotoContentActivity();
                break;
            case R.id.action_refresh:
                webview.reload();
                break;
            case R.id.action_copy_link:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Url", webview.getUrl());
                clipboardManager.setPrimaryClip(clipData);
                showSuccessAlert(R.string.sucess_msg_copy);
                break;
            case R.id.action_open_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webview.getUrl()));
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void gotoContentActivity() {
        List<Image> images = new ArrayList<>(1);
        Image image = new Image();
        image.setUrl(article.getImageUrl());
        images.add(image);
        Bundle bundle = new Bundle();
        bundle.putInt(ImageActivity.POSITION, 0);
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, (ArrayList<Image>) images);
        openActivity(ImageActivity.class, bundle);
    }
}
