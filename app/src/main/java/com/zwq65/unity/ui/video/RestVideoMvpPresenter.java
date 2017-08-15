package com.zwq65.unity.ui.video;

import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

/**
 * Created by zwq65 on 2017/08/15
 */

public interface RestVideoMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void init();


    /**
     * 加载视频资源
     *
     * @param isRefresh 是否为刷新操作
     */
    void loadVideos(Boolean isRefresh);
}
