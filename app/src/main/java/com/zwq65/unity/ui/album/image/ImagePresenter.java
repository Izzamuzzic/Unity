package com.zwq65.unity.ui.album.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.jingewenku.abrahamcaijin.commonutil.AppFileMgr;
import com.zwq65.unity.R;
import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BasePresenter;
import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.LogUtils;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zwq65 on 2017/08/01
 */

public class ImagePresenter<V extends ImageMvpView> extends BasePresenter<V> implements ImageMvpPresenter<V> {
    @Inject
    ImagePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void savePicture(final Context context, Image image) {
        getMvpView().showLoading();
        Observable.just(image)
                .observeOn(Schedulers.io())
                .map(new Function<Image, Bitmap>() {
                    Bitmap bitmap;

                    @Override
                    public Bitmap apply(@NonNull Image image) throws Exception {
                        try {
                            bitmap = Glide.with(context).asBitmap().load(image.getUrl()).submit().get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        String path = CommonUtils.getImageStorePath();
                        AppFileMgr.saveFileToSdcard(bitmap, path + image.get_id() + ".jpg");
                        return bitmap;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    getMvpView().hideLoading();
                    getMvpView().showSuccessAlert(R.string.success_msg_save);
                }, throwable -> {
                    LogUtils.e(throwable.toString());
                    getMvpView().hideLoading();
                    getMvpView().showSuccessAlert(R.string.error_msg_save_fail);
                });
    }

    @Override
    public void collectPicture(Image image) {
        isPictureCollect(image).filter(aBoolean -> !aBoolean).flatMap(new Function<Boolean, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(@NonNull Boolean aBoolean) throws Exception {
                Picture picture = new Picture(image.get_id(), image.getCreatedAt(), image.getDesc(),
                        image.getSource(), image.getType(), image.getUrl(), image.getWho());
                //显示加载框
                getMvpView().showLoading();
                return getDataManager().insertPicture(picture);
            }
        }).subscribe(aLong -> {
            getMvpView().showSuccessAlert(R.string.success_msg_collect);
            getMvpView().hideLoading();
        }, throwable -> {
            LogUtils.e(throwable.toString());
            getMvpView().hideLoading();
            getMvpView().showErrorAlert(R.string.error_msg_collect_fail);
        });
    }

    @Override
    public void cancelCollectPicture(Image image) {
        getMvpView().showLoading();
        getDataManager().deletePicture(image.get_id()).subscribe(aLong -> getMvpView().hideLoading(), throwable -> {
            getMvpView().hideLoading();
            getMvpView().showErrorAlert(R.string.error_msg_cancel_collect_fail);
        });
    }

    @Override
    public Observable<Boolean> isPictureCollect(Image image) {
        return getDataManager().isPictureExist(image.get_id());
    }
}
