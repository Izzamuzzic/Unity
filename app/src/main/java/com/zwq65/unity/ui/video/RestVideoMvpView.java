package com.zwq65.unity.ui.video;

import com.zwq65.unity.data.network.retrofit.response.VideoWithImage;
import com.zwq65.unity.ui._base.MvpView;

import java.util.List;

/**
 * Created by zwq65 on 2017/08/15
 */

public interface RestVideoMvpView extends MvpView{
    void showVideos(List<VideoWithImage> videoWithImages);
}
