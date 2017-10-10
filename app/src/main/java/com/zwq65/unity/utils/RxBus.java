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

package com.zwq65.unity.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/29
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class RxBus<T> {
    private static RxBus mRxBus;

    public static RxBus getInstance() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }

    private PublishSubject<T> bus = PublishSubject.create();

    public void send(T t) {
        bus.onNext(t);
    }

    public Observable<T> toObservable() {
        return bus;
    }

}
