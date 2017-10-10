package com.zwq65.unity.ui._base;

import android.support.annotation.StringRes;

/**
 * ================================================
 * <p> BaseContract
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface BaseContract {
    /**
     * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
     * pattern must implement. Generally this interface will be extended by a more specific interface
     * that then usually will be implemented by an Activity or Fragment.
     */
    interface View {

        void showLoading();

        void hideLoading();

        void showMessage(@StringRes int resId);

        void showMessage(String message);

        void showError(@StringRes int resId);

        void showError(String message);
    }

    /**
     * Every presenter in the app must either implement this interface or extend BasePresenter
     * indicating the MvpView type that wants to be attached with.
     */
    interface Presenter<V extends View> {

        void onAttach(V mvpView);

        void onDetach();

        boolean isViewAttached();
    }

}
