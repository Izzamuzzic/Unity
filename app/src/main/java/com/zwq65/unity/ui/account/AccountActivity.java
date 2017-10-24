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

package com.zwq65.unity.ui.account;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseViewActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 个人中心
 * <p>
 * Created by NIRVANA on 2017/08/07
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class AccountActivity extends BaseViewActivity<AccountContract.View, AccountContract.Presenter<AccountContract.View>>
        implements AccountContract.View {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

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
            default:
                break;
        }
    }
}
