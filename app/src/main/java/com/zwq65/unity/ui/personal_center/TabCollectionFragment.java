package com.zwq65.unity.ui.personal_center;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/11
 */
public class TabCollectionFragment extends BaseFragment {

    public final static String TYPE = "TYPE";
    public Type type;
    @BindView(R.id.rl_collection)
    RecyclerView rlCollection;

    public enum Type {collection, like, release}

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_tab_collection, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        int position = getArguments().getInt(TYPE);
        type = getType(position);
        initView();
    }

    private void initView() {
    }

    private Type getType(int position) {
        Type type;
        switch (position) {
            case 0:
                type = Type.collection;
                break;
            case 1:
                type = Type.like;
                break;
            case 2:
                type = Type.release;
                break;
            default:
                type = Type.collection;
                break;
        }
        return type;
    }
}
