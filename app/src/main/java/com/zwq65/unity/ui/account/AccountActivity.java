package com.zwq65.unity.ui.account;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseViewActivity;
import com.zwq65.unity.utils.FontUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zwq65 on 2017/08/07
 * 个人中心
 */

public class AccountActivity extends BaseViewActivity<AccountContract.View, AccountContract.Presenter<AccountContract.View>>
        implements AccountContract.View {

    @BindView(R.id.tl_personal)
    TabLayout tlPersonal;
    @BindView(R.id.vp_personal)
    ViewPager vpPersonal;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_account_name)
    TextView mTvAccountName;
    @BindView(R.id.tv_account_website_address)
    TextView mTvAccountWebsiteAddress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_account;
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
        FontUtils.getInstance().setTypeface(mTvAccountName, FontUtils.Font.FZYouH_508R);
        FontUtils.getInstance().setTypeface(mTvAccountWebsiteAddress, FontUtils.Font.FZYouH_508R);
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
}
