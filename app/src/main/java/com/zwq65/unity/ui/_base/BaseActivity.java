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

package com.zwq65.unity.ui._base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zwq65.unity.R;
import com.zwq65.unity.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * ================================================
 * activity基类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public abstract class BaseActivity extends DaggerAppCompatActivity {

    public final String TAG = getClass().getSimpleName();
    private FragmentManager fragmentManager;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
            mUnBinder = ButterKnife.bind(this);
        }
        LogUtils.i(TAG, "onCreate");
    }

    /**
     * @return Resource ID to be inflated
     */
    @LayoutRes
    public abstract int getLayoutId();

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        LogUtils.i(TAG, "onDestroy");
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{permission};
            requestPermissionsSafely(permissions, requestCode);
        }
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    public boolean hasPermission(String permission) {
//        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
//                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
//    }

    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }

    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        //添加intentFlag,如果要启动的activity存在于栈中,将其拉到栈顶,不用重新实例化
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);//add animation
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in_back, R.anim.right_out_back);//add animation
    }

    Fragment currentFragment;

    /**
     * 切换fragment
     * hide当前显示的fragment，若已添加show,否则add
     *
     * @param containerViewId 容器view
     * @param targetFragment  要切换的fragment
     * @param tag             fragment'tag
     */
    public void switchFragment(int containerViewId, Fragment targetFragment, String tag) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment exsitFragment = fragmentManager.findFragmentByTag(tag);
        if (exsitFragment != null) {
            //已添加( ⊙o⊙ ),show()
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.show(exsitFragment).commit();
            currentFragment = exsitFragment;
        } else {
            //还没添加,add()
            if (!targetFragment.isAdded()) {
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.add(containerViewId, targetFragment, tag).commit();
            } else {
                //已添加( ⊙o⊙ ),show()
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.show(targetFragment).commit();
            }
            currentFragment = targetFragment;
        }
    }
}
