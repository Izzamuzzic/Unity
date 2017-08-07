package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/07
 */

public class AlbumFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_album, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return null;
    }

    @Override
    public void initData(Bundle saveInstanceState) {

    }
}
