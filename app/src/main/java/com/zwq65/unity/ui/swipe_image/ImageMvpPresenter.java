package com.zwq65.unity.ui.swipe_image;

import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.MvpPresenter;

/**
 * Created by zwq65 on 2017/07/28
 */

public interface ImageMvpPresenter<V extends ImageMvpView> extends MvpPresenter<V> {
    void savePicture(WelfareResponse.Image image);
}
