package com.zwq65.unity.ui.album.image;

import android.content.Context;

import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseContract;

import io.reactivex.Observable;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface ImageContract {
    interface View extends BaseContract.View {

    }

    interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {
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
}
