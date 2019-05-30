package com.zwq65.unity.ui.presenter

import com.zwq65.unity.data.DataManager
import com.zwq65.unity.ui._base.BasePresenter
import com.zwq65.unity.ui.contract.MapContract
import javax.inject.Inject

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2019/5/29
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class MapPresenter<V : MapContract.View> @Inject
internal constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), MapContract.Presenter<V>
