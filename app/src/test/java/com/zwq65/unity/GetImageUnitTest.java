package com.zwq65.unity;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;

import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import static junit.framework.Assert.assertTrue;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/11/6
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class GetImageUnitTest {
    @Inject
    DataManager mDataManager;

    @Test
    public void getImages() {
        mDataManager.get20Images(1, new ApiSubscriberCallBack<GankApiResponse<List<Image>>>() {
            @Override
            public void onSuccess(GankApiResponse<List<Image>> listGankApiResponse) {
                assertTrue(listGankApiResponse.isError());
            }
        }, new ApiErrorCallBack<Throwable>() {
            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
