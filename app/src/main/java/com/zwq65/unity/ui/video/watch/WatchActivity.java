package com.zwq65.unity.ui.video.watch;

import android.os.Bundle;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseActivity;

public class WatchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithoutInject(R.layout.activity_watch);
    }
}
