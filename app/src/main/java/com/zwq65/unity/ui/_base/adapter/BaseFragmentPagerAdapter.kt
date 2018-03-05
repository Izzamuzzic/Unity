/*
 *    Copyright [2017] [NIRVANA PRIVATE LIMITED]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.zwq65.unity.ui._base.adapter

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

/**
 * ================================================
 * 基类 [FragmentPagerAdapter]
 *
 *
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class BaseFragmentPagerAdapter : FragmentPagerAdapter {
    private var mList: List<Fragment>? = null
    private var mTitles: Array<CharSequence>? = null

    constructor(fragmentManager: FragmentManager, list: List<Fragment>) : super(fragmentManager) {
        this.mList = list
    }

    constructor(fragmentManager: FragmentManager, list: List<Fragment>, titles: Array<CharSequence>) : super(fragmentManager) {
        this.mList = list
        this.mTitles = titles
    }

    override fun getItem(position: Int): Fragment {
        return mList!![position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (mTitles?.size!! > position) {
            mTitles!![position]
        } else super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return mList!!.size
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val f = super.instantiateItem(container, position) as Fragment
        val view = f.view
        if (view != null) {
            container.addView(view)
        }
        return f
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = mList!![position].view
        if (view != null)
            container.removeView(view)
    }
}
