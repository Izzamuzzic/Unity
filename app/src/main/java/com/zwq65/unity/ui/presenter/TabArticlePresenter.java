/*
 *    Copyright [2017] [NIRVANA PRIVATE LIMITED]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zwq65.unity.ui.presenter;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.ui._base.BasePresenter;
import com.zwq65.unity.ui.contract.TabArticleContract;
import com.zwq65.unity.ui.fragment.TabArticleFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class TabArticlePresenter<V extends TabArticleContract.View<Article>> extends BasePresenter<V>
        implements TabArticleContract.Presenter<V> {
    private @TabArticleFragment.Type
    int type;
    private int page;
    private boolean isRefresh;

    @Inject
    TabArticlePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void setType(@TabArticleFragment.Type int type) {
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
            case TabArticleFragment.Type.android:
                getDataManager().getAndroidArticles(page, getApiSubscriberCallBack(), getMvpView().bindUntilStopEvent());
                break;
            case TabArticleFragment.Type.ios:
                getDataManager().getIosArticles(page, getApiSubscriberCallBack(), getMvpView().bindUntilStopEvent());
                break;
            case TabArticleFragment.Type.h5:
                getDataManager().getQianduanArticles(page, getApiSubscriberCallBack(), getMvpView().bindUntilStopEvent());
                break;
            default:
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

            @Override
            public void onFailure(String errCode, String errMsg) {
                getMvpView().loadFail(errMsg);
            }
        };
    }

}
