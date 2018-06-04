package com.zwq65.unity.ui._custom.recycleview


internal interface BaseRefreshHeader {

    val visibleHeight: Int

    fun onMove(delta: Float)

    fun releaseAction(): Boolean

    fun refreshComplete()

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_RELEASE_TO_REFRESH = 1
        const val STATE_REFRESHING = 2
        const val STATE_DONE = 3
    }
}
