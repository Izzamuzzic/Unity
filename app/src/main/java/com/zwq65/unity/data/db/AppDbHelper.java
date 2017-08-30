/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.zwq65.unity.data.db;

import com.zwq65.unity.data.db.model.DaoMaster;
import com.zwq65.unity.data.db.model.DaoSession;
import com.zwq65.unity.data.db.model.Picture;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;


/**
 * Created by janisharali on 08/12/16.
 * databese操作帮助类
 */

@Singleton
public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Observable<Long> insertPicture(final Picture picture) {
        return Observable.fromCallable(() -> mDaoSession.getPictureDao().insertOrReplace(picture));
    }

    @Override
    public Observable<Long> deletePicture(final String id) {
        return Observable.fromCallable(() -> {
            mDaoSession.getPictureDao().deleteByKey(id);
            return 1L;
        });
    }

    @Override
    public Observable<Boolean> isPictureExist(final String id) {
        return Observable.fromCallable(() -> {
            Picture picture = mDaoSession.getPictureDao().load(id);
            return picture != null;
        });
    }

    @Override
    public Observable<List<Picture>> getCollectionPictures() {
        return Observable.fromCallable(() -> mDaoSession.getPictureDao().loadAll());
    }
}
