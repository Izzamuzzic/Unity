package com.zwq65.unity.ui.main;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.album.AlbumFragment;
import com.zwq65.unity.ui.base.BaseActivity;
import com.zwq65.unity.ui.base.BaseFragment;
import com.zwq65.unity.ui.personal_center.PersonalCenterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    AlbumFragment albumFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        initDrawer();
    }

    /**
     * 将drawerLayout、toolBar绑定
     */
    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        gotoFragment(new AlbumFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.ll_welfare, R.id.ll_setting, R.id.ll_out, R.id.ll_personal_center})
    public void onViewClicked(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (view.getId()) {
            case R.id.ll_welfare:
                //gank.io福利
                gotoFragment(new AlbumFragment());
                break;
            case R.id.ll_personal_center:
                //个人中心
                gotoFragment(new PersonalCenterFragment());
                break;
            case R.id.ll_setting:
                break;
            case R.id.ll_out:
                break;
        }
    }

    private void gotoFragment(BaseFragment fragment) {
        switchFragment(R.id.fl_main, fragment, fragment.getClass().getSimpleName());

    }

}
