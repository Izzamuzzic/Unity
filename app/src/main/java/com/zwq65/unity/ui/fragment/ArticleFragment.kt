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

package com.zwq65.unity.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseFragment
import com.zwq65.unity.ui._base.adapter.BaseFragmentPagerAdapter
import com.zwq65.unity.ui.contract.ArticleContract
import kotlinx.android.synthetic.main.fragment_article.*
import java.util.*

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class ArticleFragment : BaseFragment<ArticleContract.View, ArticleContract.Presenter<ArticleContract.View>>(), ArticleContract.View {

    override val layoutId: Int
        get() = R.layout.fragment_article

    override fun initView() {
        for (tabInt in TABS) {
            val tab = tab_type.newTab()
            tab.setText(tabInt)
            tab.let { tab_type.addTab(it) }
        }
        val fragments = ArrayList<Fragment>()
        val strTabs = arrayOfNulls<String>(TABS.size)
        for (i in TABS.indices) {
            strTabs[i] = getString(TABS[i])
            fragments.add(TabArticleFragment.newInstance(TABS[i]))
        }
        val adapter = BaseFragmentPagerAdapter(childFragmentManager, fragments, strTabs)
        vp_article.adapter = adapter
        vp_article.offscreenPageLimit = 0
        //将tabLayout与ViewPager绑定
        tab_type.setupWithViewPager(vp_article)
    }

    override fun initData(saveInstanceState: Bundle?) {

    }

    companion object {

        val TABS = intArrayOf(TabArticleFragment.ANDROID, TabArticleFragment.IOS, TabArticleFragment.H5)
    }
}
