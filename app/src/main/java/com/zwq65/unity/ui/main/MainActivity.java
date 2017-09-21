package com.zwq65.unity.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui.account.AccountActivity;
import com.zwq65.unity.ui.album.AlbumFragment;
import com.zwq65.unity.ui.article.ArticleFragment;
import com.zwq65.unity.ui.test.TestFragment;
import com.zwq65.unity.ui.video.RestVideoFragment;
import com.zwq65.unity.utils.FontUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseViewActivity implements MainMvpView {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_welfare)
    LinearLayout llWelfare;
    @BindView(R.id.ll_personal_center)
    LinearLayout llPersonalCenter;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.ll_out)
    LinearLayout llOut;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.ll_test)
    LinearLayout llTest;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.tv_account_website_address)
    TextView tvAccountWebsiteAddress;
    @BindView(R.id.ll_video)
    LinearLayout llVideo;

    Disposable disposable;
    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    @Override
    public MvpPresenter setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
    }

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
        //设置字体
        FontUtils.getInstance().setTypeface(tvAccountName, FontUtils.Font.Montserrat_Medium);
        FontUtils.getInstance().setTypeface(tvAccountWebsiteAddress, FontUtils.Font.Montserrat_Medium);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //双击退出app
            if (System.currentTimeMillis() - firstClick > 2000) {
                firstClick = System.currentTimeMillis();
                showMessage("再按一次退出");
            } else {
                exitApp();
            }
        }
    }

    @OnClick({R.id.ll_welfare, R.id.ll_personal_center, R.id.ll_video, R.id.ll_test, R.id.ll_setting, R.id.ll_out, R.id.fab})
    public void onViewClicked(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (view.getId()) {
            case R.id.ll_welfare:
                //gank.io福利
                gotoFragment(new AlbumFragment());
                break;
            case R.id.ll_personal_center:
                //个人中心
                openActivity(AccountActivity.class);
                break;
            case R.id.ll_video:
                //休息视频
                gotoFragment(new RestVideoFragment());
                break;
            case R.id.ll_test:
                gotoFragment(new TestFragment());
                break;
            case R.id.ll_setting:
                gotoFragment(new ArticleFragment());
                break;
            case R.id.ll_out:
                //退出app
                exitApp();
                break;
            case R.id.fab:
                //获取应用当前的主题
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        //当前为夜间模式，切换为日间模式
                        mPresenter.setNightMode(false);
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        mPresenter.setNightMode(true);
                        break;
                }
                setDayNightMode(mPresenter.getNightMode());
                //fixme Nougat not showing animation
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();
                break;
            default:
                break;
        }
    }

    /**
     * 设置app主题模式
     *
     * @param nightMode 是否夜间
     */
    public void setDayNightMode(boolean nightMode) {
        if (nightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void exitApp() {
        finish();
        System.exit(0);
    }

    /**
     * 添加toolBar双击监听事件
     */
    public void addToolbarDoubleClick() {
        //buffer: 定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值
        //判断500ms内，如果接受到2次的点击事件，则判定为双击操作
        if (getToolbar() != null) {
            disposable = RxView.clicks(getToolbar()).buffer(500, TimeUnit.MILLISECONDS, 2).subscribe(objects -> {
                if (objects.size() == 2) {
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    fragmentManager1.getFragments().stream()
                            .filter(fragment -> fragment.isVisible() && fragment instanceof BaseFragment)
                            .forEachOrdered(fragment -> ((BaseFragment) fragment).onToolbarClick());
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
