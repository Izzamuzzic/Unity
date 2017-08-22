package com.zwq65.unity.ui.account.tabs.collection;

import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui._base.MvpView;

import java.util.List;

/**
 * Created by zwq65 on 2017/08/14
 */

public interface TabCollectionMvpView extends MvpView {
    void showCollectionPictures(List<Picture> pictures);
}
