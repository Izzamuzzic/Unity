package com.zwq65.unity.ui.article;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/30
 */
public class ArticleFragment extends BaseFragment implements ArticleContract.IArticleView {

    @Inject
    ArticleContract.IArticlePresenter<ArticleContract.IArticleView> mPresenter;
    @BindView(R.id.tab_type)
    TabLayout tabType;
    @BindView(R.id.vp_artcle)
    ViewPager vpArtcle;

    public static final int[] Tabs = new int[]{R.string.android, R.string.ios, R.string.qianduan};

    private void initView() {
        for (int tabStr : Tabs) {
            TabLayout.Tab tab = tabType.newTab();
            tab.setText(getString(tabStr));
            tabType.addTab(tab);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        vpArtcle.setAdapter(viewPagerAdapter);
        vpArtcle.setOffscreenPageLimit(0);
        //将tabLayout与ViewPager绑定
        tabType.setupWithViewPager(vpArtcle);
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        setmPresenter(mPresenter);
        initView();
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {

    }

    /**
     * ViewPager‘adapter
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(Tabs[position]);
        }

        @Override
        public Fragment getItem(int position) {
            LogUtils.e(TAG, "position:" + position);
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof TabArticleFragment &&
                            Tabs[position] == ((TabArticleFragment) fragment).mType.getValue()) {
                        LogUtils.e(TAG, "instanceof");
                        return fragment;
                    }
                }
            }
            LogUtils.e(TAG, "newInstance");
            return TabArticleFragment.newInstance(Tabs[position]);
        }

        @Override
        public int getCount() {
            return Tabs.length;
        }
    }
}
