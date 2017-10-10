package com.zwq65.unity.ui.setting;

import android.content.Intent;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseViewActivity;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/10/10
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class SettingActivity extends BaseViewActivity<SettingContract.View,
        SettingContract.Presenter<SettingContract.View>> implements SettingContract.View {

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
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

    }

    @Override
    public void initData() {

    }
}
