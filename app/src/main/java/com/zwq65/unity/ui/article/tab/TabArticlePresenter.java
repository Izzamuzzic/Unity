package com.zwq65.unity.ui.article.tab;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.ui._base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/08/31
 */

public class TabArticlePresenter<V extends TabArticleContract.View<Article>> extends BasePresenter<V>
        implements TabArticleContract.Presenter<V> {
    private TabArticleFragment.Type type;
    private int page;
    private boolean isRefresh;

    @Inject
    TabArticlePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void setType(TabArticleFragment.Type type) {
        this.type = type;
    }

    @Override
    public void init() {
        page = 1;
        loadDatas(true);
    }

    @Override
    public void loadDatas(Boolean isRefresh) {
        this.isRefresh = isRefresh;
        switch (type) {
            case Android:
                getCompositeDisposable().add(getDataManager().getAndroidArticles(page, getApiSubscriberCallBack(),
                        getApiErrorCallBack()));
                break;
            case Ios:
                getCompositeDisposable().add(getDataManager().getIosArticles(page, getApiSubscriberCallBack(),
                        getApiErrorCallBack()));
                break;
            case Qianduan:
                getCompositeDisposable().add(getDataManager().getQianduanArticles(page, getApiSubscriberCallBack(),
                        getApiErrorCallBack()));
                break;
        }
    }

    private ApiSubscriberCallBack<List<Article>> getApiSubscriberCallBack() {
        return new ApiSubscriberCallBack<List<Article>>() {
            @Override
            public void onSuccess(List<Article> data) {
                if (data != null) {
                    page++;
                    if (isRefresh) {
                        getMvpView().refreshData(data);
                    } else {
                        getMvpView().loadData(data);
                    }
                } else {
                    getMvpView().noMoreData();
                }
            }
        };
    }

    private ApiErrorCallBack<Throwable> getApiErrorCallBack() {
        return new ApiErrorCallBack<Throwable>() {
            @Override
            public void onFailure(Throwable t) {
                getMvpView().loadFail(t);
            }
        };
    }

}
