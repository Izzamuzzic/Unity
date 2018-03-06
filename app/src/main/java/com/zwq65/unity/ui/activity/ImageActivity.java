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

package com.zwq65.unity.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.gyf.barlibrary.ImmersionBar;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseDaggerActivity;
import com.zwq65.unity.ui.contract.ImageContract;
import com.zwq65.unity.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * ================================================
 * 查看大图
 * <p>
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class ImageActivity extends BaseDaggerActivity<ImageContract.View, ImageContract.Presenter<ImageContract.View>>
        implements ImageContract.View, EasyPermissions.PermissionCallbacks {
    public static final String POSITION = "POSITION";
    public static final String IMAGE_LIST = "IMAGE_LIST";
    private static final int SAVE_MEIZHI = 1;

    @BindView(R.id.vp_images)
    ViewPager vpImages;
    @BindView(R.id.tv_current_page)
    TextView tvCurrentPage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    AppCompatCheckBox cbLove;

    int currentPosition, pageSize;
    List<Image> imageList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    public Boolean initBaseTooBar() {
        return null;
    }

    @Override
    public void dealIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentPosition = bundle.getInt(POSITION);
            imageList = bundle.getParcelableArrayList(IMAGE_LIST);
        }
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        pageSize = imageList.size();
    }

    @Override
    public void initView() {
        initToolbar();
        initViewPager();
        setCurrentPage();
    }

    @Override
    public void initData() {
        //empty
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizhi, menu);
        //找到checkbox设置下样式
        cbLove = (AppCompatCheckBox) menu.findItem(R.id.menu_cb_love).getActionView();
        cbLove.setButtonDrawable(R.drawable.selector_ic_love);
        cbLove.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mPresenter.collectPicture(imageList.get(currentPosition));
            } else {
                mPresenter.cancelCollectPicture(imageList.get(currentPosition));
            }
        });
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
                if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //未获取权限
                    LogUtils.e("未获取权限");
                    List<String> perms = new ArrayList<>();
                    perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                        LogUtils.e("不再提醒");
                        new AppSettingsDialog.Builder(this).build().show();
                    } else {
                        EasyPermissions.requestPermissions(this, "請求存儲權限", SAVE_MEIZHI,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    LogUtils.i("已获取权限");
                    savePictrue();
                }
                requestPermissionsSafely(Manifest.permission.WRITE_EXTERNAL_STORAGE, SAVE_MEIZHI);
                break;
            default:
                break;
        }
        return true;
    }

    private void savePictrue() {
        mPresenter.savePicture(this, imageList.get(currentPosition));
    }

    /**
     * 回调中判断权限是否申请成功
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (requestCode == SAVE_MEIZHI) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                LogUtils.i("SAVE_MEIZHI SUCCESS");
            } else {
                LogUtils.e("SAVE_MEIZHI FAIL");
            }
        }
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
        //改变toolbar标题为图片desc
        if (getSupportActionBar() != null) {
            if (imageList.get(currentPosition).getDesc() != null) {
                getSupportActionBar().setTitle(imageList.get(currentPosition).getDesc());
            }
        }
        //当前页码
        if (pageSize <= 1) {
            tvCurrentPage.setVisibility(View.GONE);
        } else {
            tvCurrentPage.setText(getResources().getString(R.string.placeholder_divider, String.valueOf(currentPosition + 1), String.valueOf(pageSize)));
        }
        //当前图片是否已被用户收藏
        mPresenter.isPictureCollect(imageList.get(currentPosition))
                .compose(bindUntilStopEvent())
                .subscribe(aBoolean -> {
                    if (cbLove != null) {
                        cbLove.setChecked(aBoolean);
                    }
                });
    }

    private void initViewPager() {
        //预加载2个item
        vpImages.setOffscreenPageLimit(2);
        MyAdapter mAdapter = new MyAdapter();
        vpImages.setAdapter(mAdapter);
        //设置当前加载的资源为点击进入的图片
        vpImages.setCurrentItem(currentPosition);
        vpImages.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //滑动改变当前页数
                currentPosition = position;
                setCurrentPage();
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        LogUtils.i("SAVE_MEIZHI SUCCESS!!!!");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        LogUtils.e("SAVE_MEIZHI FAIL!!!");
    }

    /**
     * 显示大图viewpager's adapter
     */
    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (imageList == null) {
                return 0;
            }
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            //显示大图view
            View view = getLayoutInflater().inflate(R.layout.item_image, container, false);
            PhotoView ivImage = view.findViewById(R.id.iv_image);

            final Image image = imageList.get(position);
            Glide.with(ImageActivity.this).load(image.getUrl()).into(ivImage);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
