package com.zwq65.unity.ui.swipe_image;

import com.zwq65.unity.ui.base.MvpPresenter;
import com.zwq65.unity.ui.base.MvpView;

/**
 * Created by zwq65 on 2017/07/28
 */

public interface ImageMvpPresenter<V extends MvpView> extends MvpPresenter<V> {
    void loadImages();
}
