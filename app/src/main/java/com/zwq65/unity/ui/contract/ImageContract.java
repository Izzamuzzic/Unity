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

package com.zwq65.unity.ui.contract;

import android.content.Context;

import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseContract;

import io.reactivex.Observable;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface ImageContract {
    interface View extends BaseContract.View {

    }

    interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {
        /**
         * 保存图片到本地
         *
         * @param context context
         * @param image   Image
         */
        void savePicture(Context context, Image image);

        /**
         * 收藏图片
         *
         * @param image Image
         */
        void collectPicture(Image image);

        /**
         * 取消收藏图片
         *
         * @param image Image
         */
        void cancelCollectPicture(Image image);

        /**
         * 该图片是否被收藏
         *
         * @param image Image
         * @return 该图片是否被收藏
         */
        Observable<Boolean> isPictureCollect(Image image);

    }
}
