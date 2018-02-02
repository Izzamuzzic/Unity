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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseDaggerActivity;
import com.zwq65.unity.ui.contract.AccountContract;

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
public class AccountActivity extends BaseDaggerActivity<AccountContract.View, AccountContract.Presenter<AccountContract.View>>
        implements AccountContract.View {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.iv_1)
    ImageView mIv1;
    @BindView(R.id.iv_2)
    ImageView mIv2;
    @BindView(R.id.iv_3)
    ImageView mIv3;

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
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        //bitmap壓縮之邻近采样（Nearest Neighbour Resampling）
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar, options);
        //bitmap壓縮之双线性采样（Bilinear Resampling）
        //方法一:
        Bitmap bitmap3 = Bitmap.createScaledBitmap(bitmap1, bitmap1.getWidth() / 2, bitmap1.getHeight() / 2, true);
        ////方法二:（直接使用matrix进行缩放）
//        Matrix matrix = new Matrix();
//        matrix.setScale(0.5f, 0.5f);
//        Bitmap bitmap3 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
        mIv1.setImageBitmap(bitmap1);
        mIv2.setImageBitmap(bitmap2);
        mIv3.setImageBitmap(bitmap3);
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
