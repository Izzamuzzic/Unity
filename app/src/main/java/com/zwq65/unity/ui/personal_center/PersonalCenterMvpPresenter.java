package com.zwq65.unity.ui.personal_center;

import com.zwq65.unity.ui.base.MvpPresenter;
import com.zwq65.unity.ui.base.MvpView;

/**
 * Created by zwq65 on 2017/08/07
 */

public interface PersonalCenterMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    //获取收藏的图片集合
    void getCollectionPhoto();

}
