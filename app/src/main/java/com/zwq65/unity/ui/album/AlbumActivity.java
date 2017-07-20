package com.zwq65.unity.ui.album;

import android.os.Bundle;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.base.BaseActivity;

public class AlbumActivity extends BaseActivity implements AlbumMvpView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }
}
