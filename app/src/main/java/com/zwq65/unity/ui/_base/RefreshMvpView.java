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

package com.zwq65.unity.ui._base;

import java.util.List;

/**
 * Created by zwq65 on 2017/09/14
 */

public interface RefreshMvpView<T> extends BaseContract.View {

    void refreshData(List<T> list);//刷新数据

    void loadData(List<T> list);//加载数据

    void loadFail(Throwable t);//加载失败

    void noMoreData();//没有数据了
}
