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

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._base.BaseViewActivity;
import com.zwq65.unity.ui.fragment.AlbumFragment;
import com.zwq65.unity.ui.fragment.ArticleFragment;
import com.zwq65.unity.ui.contract.MainContract;
import com.zwq65.unity.ui.fragment.RestVideoFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/06/29.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class MainActivity extends BaseViewActivity<MainContract.View, MainContract.Presenter<MainContract.View>>
        implements MainContract.View {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_welfare)
    LinearLayout llWelfare;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.ll_out)
    LinearLayout llOut;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.ll_news)
    LinearLayout llNews;
    @BindView(R.id.ll_video)
    LinearLayout llVideo;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;

    Disposable disposable;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Boolean initBaseTooBar() {
        return true;
    }

    @Override
    public void dealIntent(Intent intent) {

    }

    @Override
    public void initView() {
        //将drawerLayout、toolBar绑定
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        gotoFragment(new AlbumFragment());
        addToolbarDoubleClick();
    }

    @Override
    public void initData() {

    }

    private long firstClick;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //双击退出app
            if (System.currentTimeMillis() - firstClick > 2000) {
                firstClick = System.currentTimeMillis();
                showMessage("再按一次退出");
            } else {
                super.onBackPressed();
            }
        }
    }

    @OnClick({R.id.iv_avatar, R.id.tv_account_name, R.id.ll_welfare, R.id.ll_video, R.id.ll_news, R.id.ll_setting, R.id.ll_out, R.id.fab})
    public void onViewClicked(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (view.getId()) {
            case R.id.ll_welfare:
                //gank.io福利
                gotoFragment(new AlbumFragment());
                break;
            case R.id.iv_avatar:
            case R.id.tv_account_name:
                //个人中心
                openActivity(AccountActivity.class);
                break;
            case R.id.ll_news:
                gotoFragment(new ArticleFragment());
                break;
            case R.id.ll_video:
                //休息视频
                gotoFragment(new RestVideoFragment());
                break;
            case R.id.ll_setting:
                showError("开发中...＜(▰˘◡˘▰)");
                break;
            case R.id.ll_out:
                onBackPressed();
                break;
            case R.id.fab:
                setDayNightMode();
                break;
            default:
                break;
        }
    }

    /**
     * 设置白天/黑夜主题
     * TODO: 2017/10/24 有bug,找机会修复(｡◕ˇ∀ˇ◕)
     */
    private void setDayNightMode() {
        //获取应用当前的主题
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                //当前为夜间模式，切换为日间模式
                mPresenter.setNightMode(false);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                mPresenter.setNightMode(true);
                break;
            default:
                break;
        }
        setDayNightMode(mPresenter.getNightMode());
        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
        recreate();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

            }
        }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 设置app主题模式
     *
     * @param nightMode 是否夜间
     */
    public void setDayNightMode(boolean nightMode) {
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * 添加toolBar双击监听事件
     */
    public void addToolbarDoubleClick() {
        //buffer: 定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值
        //判断500ms内，如果接受到2次的点击事件，则视为用户双击操作
        if (getToolbar() != null) {
            disposable = RxView.clicks(getToolbar()).buffer(500, TimeUnit.MILLISECONDS, 2).subscribe(objects -> {
                if (objects.size() == 2) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    List<Fragment> fragmentList = fragmentManager.getFragments();
                    if (fragmentList != null && fragmentList.size() > 0) {
                        for (Fragment fragment : fragmentList) {
                            if (fragment.isVisible() && fragment instanceof BaseFragment) {
                                ((BaseFragment) fragment).onToolbarClick();
                            }
                        }
                    }
                }
            });
        }
    }

    private void gotoFragment(BaseFragment fragment) {
        switchFragment(R.id.fl_main, fragment, fragment.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
