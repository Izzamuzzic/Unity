package com.zwq65.unity.ui._base;

import java.util.List;

/**
 * Created by zwq65 on 2017/09/14
 */

public interface RefreshMvpView<T> extends MvpView {

    void refreshData(List<T> list);//刷新数据

    void loadData(List<T> list);//加载数据

    void loadFail(Throwable t);//加载失败

    void noMoreData();//没有数据了
}
