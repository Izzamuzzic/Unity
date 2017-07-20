package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.MvpView;

/**
 * Created by zwq65 on 2017/07/19.
 */

public interface AlbumMvpView extends MvpView {
    void loadBeatys(WelfareResponse welfareResponse);
}
