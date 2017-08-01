package com.zwq65.unity.ui.swipe_image;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by zwq65 on 2017/08/01
 */

public class ImagePresenter<V extends ImageMvpView> extends BasePresenter<V> implements ImageMvpPresenter<V> {
    @Inject
    public ImagePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void savePicture(WelfareResponse.Image image) {
        Picture picture = new Picture(image.get_id(), image.getCreatedAt(), image.getDesc(),
                image.getSource(), image.getType(), image.getUrl(), image.getWho());
        getDataManager().insertPicture(picture).subscribe(new Action() {
            @Override
            public void run() throws Exception {
                getMvpView().saveSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                getMvpView().saveError();
            }
        });
    }
}
