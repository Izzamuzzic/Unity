package com.zwq65.unity.ui.video;

import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;
import com.zwq65.unity.ui._base.BaseContract;
import com.zwq65.unity.ui._base.RefreshMvpView;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface RestVideoContract {
    interface View<T extends VideoWithImage> extends RefreshMvpView<T> {

    }

    interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {
        void init();

        /**
         * 加载视频资源
         *
         * @param isRefresh 是否为刷新操作
         */
        void loadVideos(Boolean isRefresh);
    }
}
