package com.zwq65.unity.ui.rxjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.base.BaseFragment;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * Created by zwq65 on 2017/08/10
 * Test the demo of Rxjava
 */

public class RxjavaFragment extends BaseFragment {
    @BindView(R.id.tv_defer)
    Button tvDefer;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_rxjava, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {

    }


    @OnClick(R.id.tv_defer)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_defer:
                defer();
                break;
        }
    }

    /**
     * defer操作符
     */
    private void defer() {
        Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {
                return null;
            }
        });
    }
}
