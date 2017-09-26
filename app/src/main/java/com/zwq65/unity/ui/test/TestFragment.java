package com.zwq65.unity.ui.test;

import android.os.Bundle;
import android.widget.Button;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/09/13
 */

public class TestFragment<V extends TestContract.View> extends BaseFragment<V, TestContract.Presenter<V>> implements TestContract.View {
    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

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
