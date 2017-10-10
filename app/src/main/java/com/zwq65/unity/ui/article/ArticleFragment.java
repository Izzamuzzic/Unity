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

package com.zwq65.unity.ui.article;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._base.adapter.BaseViewPagerAdapter;
import com.zwq65.unity.ui.article.tab.TabArticleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/30
 */
public class ArticleFragment extends BaseFragment<ArticleContract.View, ArticleContract.Presenter<ArticleContract.View>>
        implements ArticleContract.View {

    @BindView(R.id.tab_type)
    TabLayout tabType;
    @BindView(R.id.vp_artcle)
    ViewPager vpArtcle;

    public static final String[] Tabs = new String[]{"Android", "Ios", "前端"};
    private List<Fragment> fragments;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_article;
    }

    @Override
    public void initView() {
        for (String tabStr : Tabs) {
            TabLayout.Tab tab = tabType.newTab();
            tab.setText(tabStr);
            tabType.addTab(tab);
        }
        fragments = new ArrayList<>();
        for (String Tab : Tabs) {
            fragments.add(TabArticleFragment.newInstance(Tab));
        }
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getChildFragmentManager(), fragments, Tabs);
        vpArtcle.setAdapter(adapter);
        vpArtcle.setOffscreenPageLimit(0);
        //将tabLayout与ViewPager绑定
        tabType.setupWithViewPager(vpArtcle);
    }

    @Override
    public void initData(Bundle saveInstanceState) {

    }
}
