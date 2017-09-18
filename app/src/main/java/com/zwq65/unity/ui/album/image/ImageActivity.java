package com.zwq65.unity.ui.album.image;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gyf.barlibrary.ImmersionBar;
import com.tuyenmonkey.mkloader.MKLoader;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseViewActivity;
import com.zwq65.unity.ui._custom.other.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;

/**
 * 查看大图Activity
 */
public class ImageActivity extends BaseViewActivity<ImageMvpView, ImageMvpPresenter<ImageMvpView>> implements ImageMvpView {
    public static final String POSITION = "POSITION";
    public static final String IMAGE_LIST = "IMAGE_LIST";
    private static final int SAVE_MEIZHI = 1;


    int currentPosition, pageSize;//当前显示的大图position
    List<Image> imageList;//图片list
    @Inject
    ImageMvpPresenter<ImageMvpView> mPresenter;

    @BindView(R.id.vp_images)
    ViewPager vpImages;
    @BindView(R.id.tv_current_page)
    TextView tvCurrentPage;//当前页数
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AppCompatCheckBox cbLove;

    @Override
    public ImageMvpPresenter<ImageMvpView> setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    public Boolean initBaseTooBar() {
        return null;
    }

    @Override
    public Unbinder setUnBinder() {
        return ButterKnife.bind(this);
    }

    @Override
    public void dealIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        currentPosition = bundle.getInt(POSITION);
        imageList = bundle.getParcelableArrayList(IMAGE_LIST);
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        pageSize = imageList.size();
    }

    @Override
    public void initView() {
        initToolbar();
        setCurrentPage();
        initViewPager();
    }

    @Override
    public void initData() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizhi, menu);
        //找到checkbox设置下样式
        cbLove = (AppCompatCheckBox) menu.findItem(R.id.menu_cb_love).getActionView();
        cbLove.setButtonDrawable(R.drawable.selector_ic_love);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save:
                //保存大图
                requestPermissionsSafely(Manifest.permission.WRITE_EXTERNAL_STORAGE, SAVE_MEIZHI);//请求获取权限
                mPresenter.savePicture(this, imageList.get(currentPosition));
                break;
            case R.id.menu_cb_love:
                //收藏、取消收藏 图片
                if (cbLove.isChecked()) {
                    mPresenter.collectPicture(imageList.get(currentPosition));
                } else {
                    mPresenter.cancelCollectPicture(imageList.get(currentPosition));
                }
                break;
        }
        return true;
    }

    private void initToolbar() {
        //setup toolbar
        setSupportActionBar(toolbar);
        //添加‘<--’返回功能
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp);
        }
        //添加了toolbar，重新设置沉浸式状态栏
        ImmersionBar.with(this).titleBar(toolbar).init();
    }

    /**
     * 设置当前页数
     */
    private void setCurrentPage() {
        if (pageSize <= 1) {
            tvCurrentPage.setVisibility(View.GONE);
            return;
        }
        tvCurrentPage.setText(currentPosition + 1 + " / " + pageSize);
    }

    private void initViewPager() {
        vpImages.setOffscreenPageLimit(2);//预加载2个item
        Myadapter mAdapter = new Myadapter();
        vpImages.setAdapter(mAdapter);
        vpImages.setCurrentItem(currentPosition);//设置当前加载的资源为点击进入的图片
        vpImages.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //滑动改变当前页数
                currentPosition = position;
                setCurrentPage();

                //当前图片是否已被用户收藏
                mPresenter.isPictureCollect(imageList.get(currentPosition)).subscribe(aBoolean -> {
                    if (cbLove != null) {
                        cbLove.setChecked(aBoolean);
                    }
                });
                //改变toolbar标题
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(imageList.get(currentPosition).getDesc());
                }
            }
        });
    }

    /**
     * 显示大图viewpager's adapter
     */
    private class Myadapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (imageList == null) {
                return 0;
            }
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            //显示大图view
            View view = getLayoutInflater().inflate(R.layout.item_image, container, false);
            PhotoView ivImage = (PhotoView) view.findViewById(R.id.iv_image);

            final Image image = imageList.get(position);
            final MKLoader pbLoader = (MKLoader) view.findViewById(R.id.pb_loader);
            Glide.with(ImageActivity.this).load(image.getUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            showMessage(R.string.error_msg_load_fail);
                            pbLoader.setVisibility(GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pbLoader.setVisibility(GONE);
                            return false;
                        }
                    }).into(ivImage);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
