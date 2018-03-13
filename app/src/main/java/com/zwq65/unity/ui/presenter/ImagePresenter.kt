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

package com.zwq65.unity.ui.presenter

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.zwq65.unity.R
import com.zwq65.unity.data.DataManager
import com.zwq65.unity.data.db.model.Picture
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.ui._base.BasePresenter
import com.zwq65.unity.ui.contract.ImageContract
import com.zwq65.unity.utils.CommonUtils
import com.zwq65.unity.utils.LogUtils
import com.zwq65.unity.utils.saveFileToSdcard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutionException
import javax.inject.Inject

/**
 * ================================================
 *
 * Created by NIRVANA on 2017/08/01
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class ImagePresenter<V : ImageContract.View> @Inject
internal constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), ImageContract.Presenter<V> {

    override fun savePicture(context: Context, image: Image) {
        mvpView?.showLoading()
        Observable.just(image)
                .subscribeOn(Schedulers.io())
                .compose(mvpView?.bindUntilStopEvent())
                .map(object : Function<Image, Bitmap> {
                    internal lateinit var bitmap: Bitmap

                    @Throws(Exception::class)
                    override fun apply(@NonNull image: Image): Bitmap {
                        try {
                            bitmap = Glide.with(context).asBitmap().load(image.url).submit().get()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } catch (e: ExecutionException) {
                            e.printStackTrace()
                        }

                        val path = CommonUtils.getImageStorePath()
                        saveFileToSdcard(bitmap, path + image._id + ".jpg")

                        return bitmap
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _ ->
                    mvpView?.hideLoading()
                    mvpView?.showMessage(R.string.success_msg_save)
                }) { throwable ->
                    LogUtils.e(throwable.toString())
                    mvpView?.hideLoading()
                    mvpView?.showError(R.string.error_msg_save_fail)
                }
    }

    override fun collectPicture(image: Image) {
        isPictureCollect(image)
                .compose(mvpView?.bindUntilStopEvent())
                .filter { aBoolean -> !aBoolean }
                .flatMap { _ ->
                    val picture = Picture(image._id, image.createdAt, image.desc,
                            image.source, image.type, image.url, image.who)
                    //显示加载框
                    mvpView?.showLoading()
                    dataManager.savePicture(picture)
                }.subscribe({ _ ->
                    mvpView?.showMessage(R.string.success_msg_collect)
                    mvpView?.hideLoading()
                }) { throwable ->
                    LogUtils.e(throwable.toString())
                    mvpView?.hideLoading()
                    mvpView?.showError(R.string.error_msg_collect_fail)
                }
    }

    override fun cancelCollectPicture(image: Image) {
        mvpView?.showLoading()
        dataManager.deletePicture(image._id)
                .compose(mvpView?.bindUntilStopEvent())
                .subscribe({ mvpView?.hideLoading() }) {
                    mvpView?.hideLoading()
                    mvpView?.showError(R.string.error_msg_cancel_collect_fail)
                }
    }

    override fun isPictureCollect(image: Image): Observable<Boolean> {
        return dataManager.isPictureExist(image._id)
    }
}
