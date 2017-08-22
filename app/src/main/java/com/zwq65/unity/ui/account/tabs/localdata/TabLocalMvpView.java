package com.zwq65.unity.ui.account.tabs.localdata;

import com.zwq65.unity.ui._base.MvpView;

import java.io.File;
import java.util.List;

/**
 * Created by zwq65 on 2017/08/22
 */

public interface TabLocalMvpView extends MvpView{
    void showLocalPictures(List<File> pictures);
}
