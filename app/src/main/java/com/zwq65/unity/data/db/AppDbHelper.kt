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

package com.zwq65.unity.data.db

import com.zwq65.unity.data.db.model.DaoMaster
import com.zwq65.unity.data.db.model.DaoSession
import com.zwq65.unity.data.db.model.Picture
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ================================================
 * Database操作帮助类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Singleton
class AppDbHelper @Inject
internal constructor(dbOpenHelper: DbOpenHelper) : DbHelper {

    private val mDaoSession: DaoSession = DaoMaster(dbOpenHelper.writableDb).newSession()

    override val collectionPictures: Observable<List<Picture>>
        get() = Observable.fromCallable { mDaoSession.pictureDao.loadAll() }

    override fun savePicture(picture: Picture): Observable<Long> {
        return Observable.fromCallable { mDaoSession.pictureDao.insertOrReplace(picture) }
    }

    override fun deletePicture(id: String): Observable<Long> {
        return Observable.fromCallable {
            mDaoSession.pictureDao.deleteByKey(id)
            1L
        }
    }

    override fun isPictureExist(id: String): Observable<Boolean> {
        return Observable.fromCallable {
            val picture = mDaoSession.pictureDao.load(id)
            picture != null
        }
    }
}
