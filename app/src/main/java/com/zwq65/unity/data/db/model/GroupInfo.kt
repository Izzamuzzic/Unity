package com.zwq65.unity.data.db.model

/**
 *================================================
 *
 * Created by NIRVANA on 2018/5/16
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
data class GroupInfo(var id: Int?, var position: Int?, var mGroupLength: Int, var content: String?) {
    fun isFirstViewInGroup(): Boolean {
        return 0 == position
    }

    fun isLastViewInGroup(): Boolean {
        position?.let {
            return it >= 0 && it == (mGroupLength - 1)
        }
        return false
    }
}