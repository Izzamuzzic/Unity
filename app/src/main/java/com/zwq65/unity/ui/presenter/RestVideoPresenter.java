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
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.ui._base.BasePresenter;
import com.zwq65.unity.ui.contract.RestVideoContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class RestVideoPresenter<V extends RestVideoContract.View<Video>> extends BasePresenter<V>
        implements RestVideoContract.Presenter<V> {
    private int page;

    @Inject
    RestVideoPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void init() {
        page = 1;
        loadVideos(true);
    }

    @Override
    public void loadVideos(final Boolean isRefresh) {
        getCompositeDisposable().add(
                getDataManager().getVideosAndIMages(page, new ApiSubscriberCallBack<List<Video>>() {
                    @Override
                    public void onSuccess(List<Video> videoWithImages) {
                        if (videoWithImages != null && videoWithImages.size() > 0) {
                            page++;
                            if (isRefresh) {
                                getMvpView().refreshData(videoWithImages);
                            } else {
                                getMvpView().loadData(videoWithImages);
                            }
                        } else {
                            getMvpView().noMoreData();
                        }
                    }
                }, new ApiErrorCallBack<Throwable>() {
                    @Override
                    public void onFailure(Throwable t) {
                        getMvpView().loadFail(t);
                    }
                }));
    }
}
