package com.zwq65.unity.ui.article;

import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

import java.util.List;


/**
 * Created by zwq65 on 2017/08/31
 */

public class TabArticleContract {
    public interface ITabArticleView extends MvpView {
        void refreshData(List<Article> t);

        void showData(List<Article> t);

        void noMoreData();
    }

    public interface ITabArticlePresenter<V extends MvpView> extends MvpPresenter<V> {
        void setType(TabArticleFragment.Type type);

        void init();

        void loadDatas(Boolean isRefresh);
    }
}
