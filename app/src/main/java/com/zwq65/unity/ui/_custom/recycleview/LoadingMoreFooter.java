package com.zwq65.unity.ui._custom.recycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.zwq65.unity.R;
import com.zwq65.unity.ui._custom.view.LoadingView;


public class LoadingMoreFooter extends LinearLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private TextView mText;
    private LoadingView mLoadingView;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_load_footer, this);
        mText = findViewById(R.id.tv_msg);
        mLoadingView = findViewById(R.id.loadingView);
        mLoadingView.setSize(SizeUtils.dp2px(14));
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mText.setText(getContext().getText(R.string.listview_loading));
                mLoadingView.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(getContext().getText(R.string.listview_loading));
                setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(getContext().getText(R.string.nomore_loading));
                mLoadingView.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void reSet() {
        this.setVisibility(GONE);
    }
}
