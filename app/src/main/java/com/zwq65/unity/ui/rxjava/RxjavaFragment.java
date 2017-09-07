package com.zwq65.unity.ui.rxjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/10
 * Test the demo of Rxjava
 */

public class RxjavaFragment extends BaseFragment {

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_rxjava, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {

    }

}
