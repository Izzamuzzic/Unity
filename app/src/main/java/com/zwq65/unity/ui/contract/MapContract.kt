package com.zwq65.unity.ui.contract

import com.zwq65.unity.ui._base.BaseContract

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2019/5/29
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
interface MapContract {
    interface View : BaseContract.View

    interface Presenter<V : BaseContract.View> : BaseContract.Presenter<V>
}
