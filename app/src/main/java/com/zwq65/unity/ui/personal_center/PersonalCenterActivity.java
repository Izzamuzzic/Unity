package com.zwq65.unity.ui.personal_center;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.base.BaseActivity;
import com.zwq65.unity.utils.FontUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zwq65 on 2017/08/07
 * 个人中心
 */

public class PersonalCenterActivity extends BaseActivity implements PersonalCenterMvpView {

    @Inject
    PersonalCenterMvpPresenter<PersonalCenterMvpView> mPresenter;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tl_personal)
    TabLayout tlPersonal;
    @BindView(R.id.vp_personal)
    ViewPager vpPersonal;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithoutInject(R.layout.activity_personal_center);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    private void initView() {
        //设置特殊字体
        FontUtils.getInstance().setTypeface(tvName, FontUtils.Font.Roboto_Bold);
        tlPersonal.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        vpPersonal.setOffscreenPageLimit(0);
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        vpPersonal.setAdapter(viewPagerAdapter);
//        //将tabLayout与ViewPager绑定
//        tlPersonal.setupWithViewPager(vpPersonal);
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
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return tlPersonal.getTabCount();
        }
    }


}
