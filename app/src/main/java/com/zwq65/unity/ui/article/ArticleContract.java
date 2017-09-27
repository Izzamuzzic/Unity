package com.zwq65.unity.ui.article;

import com.zwq65.unity.ui._base.BaseContract;

/**
 * Created by zwq65 on 2017/08/30
 */
public interface ArticleContract {
    interface View extends BaseContract.View {

    }

    interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {

    }
}
