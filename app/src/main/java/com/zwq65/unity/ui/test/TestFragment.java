package com.zwq65.unity.ui.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._base.MvpPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zwq65 on 2017/09/13
 */

public class TestFragment extends BaseFragment implements TestMvpView {

    @Inject
    TestMvpPresenter<TestMvpView> mPresenter;
    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    public MvpPresenter setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
    }

    @Override
    public View inflateLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public Unbinder setUnBinder(View view) {
        return ButterKnife.bind(this, view);
    }

    @Override
    public void initView() {
        btnTest.setOnClickListener(v -> {
            mPresenter.test();
        });
        btnExit.setOnClickListener(v -> getFragmentManager().beginTransaction().remove(this).commit());
    }

    @Override
    public void initData(Bundle saveInstanceState) {

    }

}
