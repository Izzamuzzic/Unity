package com.zwq65.unity.utils.listener;

import android.support.annotation.IntDef;
import android.support.design.widget.AppBarLayout;

import com.blankj.utilcode.util.LogUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.zwq65.unity.utils.listener.AppBarStateChangeListener.AppBarState.COLLAPSED;
import static com.zwq65.unity.utils.listener.AppBarStateChangeListener.AppBarState.EXPANDED;
import static com.zwq65.unity.utils.listener.AppBarStateChangeListener.AppBarState.IDLE;


/**
 * ================================================
 * Interface definition for a callback to be invoked when an {@link AppBarLayout}'s vertical
 * offset changes.
 * <p>
 * Created by NIRVANA on 2018/3/20
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    private int mCurrentState = IDLE;

    /**
     * Called when the {@link AppBarLayout}'s layout offset has been changed. This allows
     * child views to implement custom behavior based on the offset (for instance pinning a
     * view at a certain y value).
     *
     * @param appBarLayout   the {@link AppBarLayout} which offset has changed
     * @param verticalOffset the vertical offset for the parent {@link AppBarLayout}, in px
     */
    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        LogUtils.i("onOffsetChanged", verticalOffset + "");
        if (verticalOffset == 0) {
            if (mCurrentState != EXPANDED) {
                onStateChanged(appBarLayout, EXPANDED);
            }
            mCurrentState = EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != COLLAPSED) {
                onStateChanged(appBarLayout, COLLAPSED);
            }
            mCurrentState = COLLAPSED;
        } else {
            if (mCurrentState != IDLE) {
                onStateChanged(appBarLayout, IDLE);
            }
            mCurrentState = IDLE;
        }
    }

    /**
     * @param appBarLayout {@link AppBarLayout}
     * @param state        {@link AppBarState}
     */
    public abstract void onStateChanged(AppBarLayout appBarLayout, @AppBarState int state);

    @IntDef({
            //展开状态
            EXPANDED,
            //折叠状态
            COLLAPSED,
            IDLE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppBarState {
        int EXPANDED = -1;
        int COLLAPSED = 0;
        int IDLE = 1;
    }
}

