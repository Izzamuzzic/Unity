package com.zwq65.unity.ui.personal_center;

import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui.base.MvpView;

import java.util.List;

/**
 * Created by zwq65 on 2017/08/07
 */

public interface PersonalCenterMvpView extends MvpView {
    //显示收藏的图片集合
    void showCollection(List<Picture> pictures);

}
