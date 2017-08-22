package com.zwq65.unity.ui.album.imagedetail;

import android.content.Context;

import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui._base.MvpPresenter;

import io.reactivex.Observable;


/**
 * Created by zwq65 on 2017/07/28
 */

public interface ImageMvpPresenter<V extends ImageMvpView> extends MvpPresenter<V> {
    //保存图片到本地
    void savePicture(Context context, Image image);

    //收藏图片
    void collectPicture(Image image);

    //取消收藏图片
    void cancelCollectPicture(Image image);

    Observable<Boolean> isPictureCollect(Image image);
}
