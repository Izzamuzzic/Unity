package com.zwq65.unity.ui.album.imagedetail;

import android.content.Context;

import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

import io.reactivex.Observable;


/**
 * Created by zwq65 on 2017/07/28
 */

public interface ImageMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    /**
     * @param context context
     * @param image   保存图片到本地
     */
    void savePicture(Context context, Image image);

    /**
     * @param image 收藏图片
     */
    void collectPicture(Image image);

    /**
     * @param image 取消收藏图片
     */
    void cancelCollectPicture(Image image);

    /**
     * @param image 图片
     * @return 该图片是否被收藏
     */
    Observable<Boolean> isPictureCollect(Image image);
}
