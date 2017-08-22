package com.zwq65.unity.ui.account.tabs.collection;

import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

/**
 * Created by zwq65 on 2017/08/14
 */

public interface TabCollectionMvpPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getCollectionPictures();
}
