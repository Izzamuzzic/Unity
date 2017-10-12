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

package com.zwq65.unity.ui.test;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;
import com.zwq65.unity.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/13.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class TestPresenter<V extends TestContract.View> extends BasePresenter<V> implements TestContract.Presenter<V> {
    @Inject
    TestPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void test() {
        Observable.interval(1, TimeUnit.SECONDS).subscribe(aLong -> LogUtils.i("test", "---------" + aLong));
    }
}
