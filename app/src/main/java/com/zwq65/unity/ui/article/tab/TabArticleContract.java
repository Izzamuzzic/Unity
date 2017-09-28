package com.zwq65.unity.ui.article.tab;

import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.ui._base.BaseContract;
import com.zwq65.unity.ui._base.RefreshMvpView;


/**
 * Created by zwq65 on 2017/08/31
 */

public class TabArticleContract {
    public interface View<T extends Article> extends RefreshMvpView<T> {
    }

    public interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {
        void setType(TabArticleFragment.Type type);

        void init();

        void loadDatas(Boolean isRefresh);
    }
}
