package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.network.retrofit.response.Image;
import com.zwq65.unity.ui._base.MvpView;

import java.util.List;

/**
 * Created by zwq65 on 2017/07/19.
 */

public interface AlbumMvpView extends MvpView {

    void refreshImages(List<Image> imageList);//刷新图片

    void showImages(List<Image> imageList);//加载图片

    void loadFail(Throwable t);//加载报错

    void noMoreData();//没有数据了
}
