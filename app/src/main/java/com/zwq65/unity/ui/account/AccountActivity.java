package com.zwq65.unity.ui.account;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseViewActivity;
import com.zwq65.unity.ui.account.tabs.collect.TabCollectionFragment;
import com.zwq65.unity.ui.account.tabs.local.TabLocalFragment;
import com.zwq65.unity.utils.FontUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zwq65 on 2017/08/07
 * 个人中心
 */

public class AccountActivity extends BaseViewActivity<AccountMvpView, AccountMvpPresenter<AccountMvpView>> implements AccountMvpView {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tl_personal)
    TabLayout tlPersonal;
    @BindView(R.id.vp_personal)
    ViewPager vpPersonal;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    public static final int[] Tabs = new int[]{R.string.collect, R.string.local, R.string.like};

    @Override
    public void injectActivityComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    public Boolean initBaseTooBar() {
        return null;
    }

    @Override
    public void dealIntent(Intent intent) {

    }

    @Override
    public void initView() {
        //设置特殊字体
        FontUtils.getInstance().setTypeface(tvName, FontUtils.Font.Roboto_Bold);

        for (int tabStr : Tabs) {
            TabLayout.Tab tab = tlPersonal.newTab();
            tab.setText(getString(tabStr));
            tlPersonal.addTab(tab);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpPersonal.setAdapter(viewPagerAdapter);
        vpPersonal.setOffscreenPageLimit(0);
        //将tabLayout与ViewPager绑定
        tlPersonal.setupWithViewPager(vpPersonal);
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * ViewPager‘adapter
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(Tabs[position]);
        }

        @Override
        public Fragment getItem(int position) {
            if (getSupportFragmentManager().getFragments() != null) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (0 == position && fragment instanceof TabCollectionFragment) {
                        return fragment;
                    } else if (1 == position && fragment instanceof TabLocalFragment) {
                        return fragment;
                    }
                }
            }
            if (1 == position) {
                return new TabLocalFragment();
            } else {
                return new TabCollectionFragment();
            }
        }

        @Override
        public int getCount() {
            return Tabs.length;
        }
    }

}
