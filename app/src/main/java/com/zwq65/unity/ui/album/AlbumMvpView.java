package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui._base.MvpView;

import java.util.List;

/**
 * Created by zwq65 on 2017/07/19.
 */

public interface AlbumMvpView extends MvpView {

    void refreshImages(List<WelfareResponse.Image> imageList);//刷新图片

    void showImages(List<WelfareResponse.Image> imageList);//加载图片

    void loadError(Throwable t);//加载报错

    void noMoreData();//没有数据了
}
