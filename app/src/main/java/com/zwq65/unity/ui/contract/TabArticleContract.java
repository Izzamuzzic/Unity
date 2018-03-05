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

import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.ui._base.BaseContract;
import com.zwq65.unity.ui._base.RefreshMvpView;
import com.zwq65.unity.ui.fragment.TabArticleFragment;


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class TabArticleContract {
    public interface View<T extends Article> extends RefreshMvpView<T> {
    }

    public interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {
        void setType(@TabArticleFragment.Type int type);

        void init();

        void loadDatas(Boolean isRefresh);
    }
}
