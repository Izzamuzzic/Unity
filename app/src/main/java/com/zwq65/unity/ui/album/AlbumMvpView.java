package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.MvpView;

import java.util.List;

/**
 * Created by zwq65 on 2017/07/19.
 */

public interface AlbumMvpView extends MvpView {
    void loadImages(List<WelfareResponse.Image> beautyList);//加载图片

    void noMoreData();//没有数据了
}
