package com.zwq65.unity.ui._custom.layout

/**
 * ================================================
 *
 * Created by NIRVANA on 2019/07/09.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
interface OnBounceDistanceChangeListener {

    fun onDistanceChange(distance: Int, direction: Int)

    fun onFingerUp(distance: Int, direction: Int)

    companion object {
        val DIRECTION_LEFT = 1
        val DIRECTION_RIGHT = 2
        val DIRECTION_UP = 3
        val DIRECTION_DOWN = 4
    }
}
