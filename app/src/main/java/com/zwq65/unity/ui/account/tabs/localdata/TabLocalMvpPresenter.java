package com.zwq65.unity.ui.account.tabs.localdata;

import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by zwq65 on 2017/08/22
 */

public interface TabLocalMvpPresenter<V extends MvpView> extends MvpPresenter<V> {
    Observable<List<File>> getLocalPictures();
}
