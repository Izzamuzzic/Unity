package com.zwq65.unity.ui.account.tabs.localdata;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;
import com.zwq65.unity.utils.CommonUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/08/22
 */

public class TabLocalPresenter<V extends TabLocalMvpView> extends BasePresenter<V> implements TabLocalMvpPresenter<V> {
    @Inject
    public TabLocalPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getLocalPictures() {
        File file = new File(CommonUtils.getImageStorePath());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            List<File> fileList = Arrays.asList(files);
            getMvpView().showLocalPictures(fileList);
        }

    }
}
