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
