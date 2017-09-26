package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;
import com.zwq65.unity.ui._base.RefreshMvpView;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface AlbumContract {
    interface View<T extends Image> extends RefreshMvpView<T> {

    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {
        void init();

        /**
         * 加载图片资源
         *
         * @param isRefresh 是否为刷新操作
         */
        void loadImages(Boolean isRefresh);

    }
}
