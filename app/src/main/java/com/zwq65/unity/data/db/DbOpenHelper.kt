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

import android.content.Context

import com.zwq65.unity.data.db.model.DaoMaster
import com.zwq65.unity.di.ApplicationContext
import com.zwq65.unity.di.DatabaseInfo

import org.greenrobot.greendao.database.Database

import javax.inject.Inject
import javax.inject.Singleton

/**
 * ================================================
 * 本地数据库帮助类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Singleton
class DbOpenHelper @Inject
constructor(@ApplicationContext context: Context, @DatabaseInfo name: String) : DaoMaster.OpenHelper(context, name) {

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)
        //        LogUtils.d("DEBUG", "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);
        //        switch (oldVersion) {
        //            case 1:
        //            case 2:
        //                db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN "
        //                        + "sex" + " TEXT DEFAULT 'MAN'");
        //                db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN "
        //                        + "sex" + " TEXT DEFAULT 'MAN'");
        //        }
    }
}