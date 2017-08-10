package com.zwq65.unity.ui.personal_center;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwq65.unity.R;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui.base.BaseFragment;
import com.zwq65.unity.utils.FontUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/07
 * 个人中心
 */

public class PersonalCenterFragment extends BaseFragment implements PersonalCenterMvpView {

    @Inject
    PersonalCenterMvpPresenter<PersonalCenterMvpView> mPresenter;

    @BindView(R.id.tv_name)
    TextView tvName;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getCollectionPhoto();
    }

    private void initView() {
        //设置特殊字体
        FontUtils.getInstance().setTypeface(tvName, FontUtils.Font.Roboto_Bold);
    }

    @Override
    public void showCollection(List<Picture> pictures) {
    }

    @Override
    public void onDetach() {
        mPresenter.onDetach();
        super.onDetach();
    }

}
