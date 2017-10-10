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

package com.zwq65.unity.ui.test;

import android.os.Bundle;
import android.widget.Button;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/09/13
 */

public class TestFragment extends BaseFragment<TestContract.View, TestContract.Presenter<TestContract.View>> implements TestContract.View {
    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView() {
        btnTest.setOnClickListener(v -> mPresenter.test());
        btnExit.setOnClickListener(v -> getFragmentManager().beginTransaction().remove(this).commit());
    }

    @Override
    public void initData(Bundle saveInstanceState) {

    }

}
