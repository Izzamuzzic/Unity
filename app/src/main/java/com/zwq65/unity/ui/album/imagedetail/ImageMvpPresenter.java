package com.zwq65.unity.ui.album.imagedetail;

import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui._base.MvpPresenter;

/**
 * Created by zwq65 on 2017/07/28
 */

public interface ImageMvpPresenter<V extends ImageMvpView> extends MvpPresenter<V> {
    void savePicture(WelfareResponse.Image image);
}