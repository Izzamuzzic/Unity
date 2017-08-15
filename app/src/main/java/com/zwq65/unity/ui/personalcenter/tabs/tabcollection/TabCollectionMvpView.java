package com.zwq65.unity.ui.personalcenter.tabs.tabcollection;

import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui.base.MvpView;

import java.util.List;

/**
 * Created by zwq65 on 2017/08/14
 */

public interface TabCollectionMvpView extends MvpView {
    void showCollectionPictures(List<Picture> pictures);
}
