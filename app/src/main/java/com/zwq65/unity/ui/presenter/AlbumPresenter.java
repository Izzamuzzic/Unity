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
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BasePresenter;
import com.zwq65.unity.ui.contract.AlbumContract;

import java.util.List;

import javax.inject.Inject;


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/07/20
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class AlbumPresenter<V extends AlbumContract.View<Image>> extends BasePresenter<V> implements AlbumContract.Presenter<V> {
    private int page;

    @Inject
    AlbumPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void init() {
        page = 1;
        loadImages(true);
    }

    @Override
    public void loadImages(final Boolean isRefresh) {
        getDataManager().get20Images(page, new ApiSubscriberCallBack<GankApiResponse<List<Image>>>() {
            @Override
            public void onSuccess(GankApiResponse<List<Image>> welfareResponse) {
                if (welfareResponse != null && welfareResponse.getData() != null) {
                    page++;
                    if (isRefresh) {
                        getMvpView().refreshData(welfareResponse.getData());
                    } else {
                        getMvpView().loadData(welfareResponse.getData());
                    }
                } else {
                    getMvpView().noMoreData();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg) {
                getMvpView().loadFail(errMsg);
            }
        }, getMvpView().bindUntilStopEvent());
    }
}
