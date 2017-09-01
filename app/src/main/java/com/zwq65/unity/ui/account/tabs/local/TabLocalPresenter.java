package com.zwq65.unity.ui.account.tabs.local;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;
import com.zwq65.unity.utils.CommonUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zwq65 on 2017/08/22
 */

public class TabLocalPresenter<V extends TabLocalMvpView> extends BasePresenter<V> implements TabLocalMvpPresenter<V> {
    @Inject
    TabLocalPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public Observable<List<File>> getLocalPictures() {
        return Observable.just(CommonUtils.getImageStorePath())
                .observeOn(Schedulers.io())
                .map(File::new)
                .map(file -> {
                    if (file.exists() && file.isDirectory() && file.list().length > 0) {
                        return file.listFiles();
                    }
                    // TODO: 2017/08/23 一定不能返null值
                    return new File[0];
                })
                .map(Arrays::asList)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
