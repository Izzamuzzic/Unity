package com.zwq65.unity.ui.album.imagedetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zwq65.unity.R;
import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui._base.BasePresenter;
import com.zwq65.unity.utils.AppFileMgr;
import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.LogUtils;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
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
                            bitmap = Glide.with(context).asBitmap().load(image.getUrl()).listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).submit().get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        String path = CommonUtils.getImageStorePath();
                        AppFileMgr.saveFileToSdcard(bitmap, path + image.get_id() + ".jpg");
                        return bitmap;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        getMvpView().hideLoading();
                        getMvpView().showSuccessAlert(R.string.save_success);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable.toString());
                        getMvpView().hideLoading();
                        getMvpView().showSuccessAlert(R.string.save_fail);
                    }
                });
    }

    @Override
    public void collectPicture(Image image) {
        getMvpView().showLoading();
        Picture picture = new Picture(image.get_id(), image.getCreatedAt(), image.getDesc(),
                image.getSource(), image.getType(), image.getUrl(), image.getWho());
        getDataManager().insertPicture(picture).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                getMvpView().hideLoading();
                getMvpView().showSuccessAlert(R.string.collect_success);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LogUtils.e(throwable.toString());
                getMvpView().hideLoading();
                getMvpView().showErrorAlert(R.string.collect_fail);
            }
        });
    }

    @Override
    public void cancelCollectPicture(Image image) {
        getMvpView().showLoading();
        getDataManager().deletePicture(image.get_id()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                getMvpView().hideLoading();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                getMvpView().hideLoading();
                getMvpView().showErrorAlert(R.string.cancel_collect_fail);
            }
        });
    }

    @Override
    public Observable<Boolean> isPictureCollect(Image image) {
        return getDataManager().isPictureExist(image.get_id());
    }
}
