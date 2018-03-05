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

package com.zwq65.unity.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._base.adapter.BaseFragmentPagerAdapter;
import com.zwq65.unity.ui.contract.ArticleContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class ArticleFragment extends BaseFragment<ArticleContract.View, ArticleContract.Presenter<ArticleContract.View>>
        implements ArticleContract.View {

    @BindView(R.id.tab_type)
    TabLayout tabType;
    @BindView(R.id.vp_article)
    ViewPager vpArticle;

    public static final int[] TABS = new int[]{
            TabArticleFragment.Type.android,
            TabArticleFragment.Type.ios,
            TabArticleFragment.Type.h5
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_article;
    }

    @Override
    public void initView() {
        for (int tabInt : TABS) {
            TabLayout.Tab tab = tabType.newTab();
            tab.setText(tabInt);
            tabType.addTab(tab);
        }
        List<Fragment> fragments = new ArrayList<>();
        String[] strTabs = new String[TABS.length];
        for (int i = 0; i < TABS.length; i++) {
            strTabs[i] = getString(TABS[i]);
            fragments.add(TabArticleFragment.newInstance(TABS[i]));
        }
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments, strTabs);
        vpArticle.setAdapter(adapter);
        vpArticle.setOffscreenPageLimit(0);
        //将tabLayout与ViewPager绑定
        tabType.setupWithViewPager(vpArticle);
    }

    @Override
    public void initData(Bundle saveInstanceState) {

    }
}
