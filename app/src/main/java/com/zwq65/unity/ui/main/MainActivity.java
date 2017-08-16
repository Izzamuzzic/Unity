package com.zwq65.unity.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseActivity;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui.account.AccountActivity;
import com.zwq65.unity.ui.album.AlbumFragment;
import com.zwq65.unity.ui.rxjava.RxjavaFragment;
import com.zwq65.unity.ui.video.RestVideoFragment;
import com.zwq65.unity.utils.FontUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

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
    @BindView(R.id.ll_rxjava)
    LinearLayout llRxjava;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.tv_account_website_address)
    TextView tvAccountWebsiteAddress;
    @BindView(R.id.ll_video)
    LinearLayout llVideo;

    Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        initDrawer();
        addToolbarDoubleClick();
    }

    /**
     * 将drawerLayout、toolBar绑定
     */
    private void initDrawer() {
        //设置字体
        FontUtils.getInstance().setTypeface(tvAccountName, FontUtils.Font.Montserrat_Medium);
        FontUtils.getInstance().setTypeface(tvAccountWebsiteAddress, FontUtils.Font.Montserrat_Medium);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        gotoFragment(new AlbumFragment());
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
                finish();
                System.exit(0);
            }
        }
    }

    @OnClick({R.id.ll_welfare, R.id.ll_personal_center, R.id.ll_video, R.id.ll_rxjava, R.id.ll_setting, R.id.ll_out})
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
            case R.id.ll_rxjava:
                gotoFragment(new RxjavaFragment());
                break;
            case R.id.ll_setting:
                break;
            case R.id.ll_out:
                //退出app
                finish();
                System.exit(0);
                break;
        }
    }

    /**
     * 添加toolBar双击监听事件
     */
    public void addToolbarDoubleClick() {
        //buffer: 定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值
        //判断500ms内，如果接受到2次的点击事件，则视为用户双击操作
        if (getToolbar() != null) {
            disposable = RxView.clicks(getToolbar()).buffer(500, TimeUnit.MILLISECONDS, 2).subscribe(new Consumer<List<Object>>() {
                @Override
                public void accept(@NonNull List<Object> objects) throws Exception {
                    if (objects.size() == 2) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        for (Fragment fragment : fragmentManager.getFragments()) {
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
