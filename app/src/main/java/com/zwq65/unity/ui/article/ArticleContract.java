package com.zwq65.unity.ui.article;

import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

/**
 * Created by zwq65 on 2017/08/30
 */
public interface ArticleContract {
    interface View extends MvpView {

    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {

    }
}
