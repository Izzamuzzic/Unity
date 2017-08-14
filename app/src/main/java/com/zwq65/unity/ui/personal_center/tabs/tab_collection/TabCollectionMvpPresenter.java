package com.zwq65.unity.ui.personal_center.tabs.tab_collection;

import com.zwq65.unity.ui.base.MvpPresenter;
import com.zwq65.unity.ui.base.MvpView;

/**
 * Created by zwq65 on 2017/08/14
 */

public interface TabCollectionMvpPresenter<V extends MvpView> extends MvpPresenter<V> {
    void getCollectionPictures();
}
