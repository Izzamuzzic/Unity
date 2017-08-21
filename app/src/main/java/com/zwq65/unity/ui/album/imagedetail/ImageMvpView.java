package com.zwq65.unity.ui.album.imagedetail;

import com.zwq65.unity.ui._base.MvpView;

/**
 * Created by zwq65 on 2017/07/28
 */

public interface ImageMvpView extends MvpView {
    void savePictrueWhetherSucceed(Boolean success);//保存图片是否成功

    void collectPictrueWhetherSucceed(Boolean success);//收藏图片是否成功
}
