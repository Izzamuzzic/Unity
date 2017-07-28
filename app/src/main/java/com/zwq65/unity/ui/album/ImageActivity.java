package com.zwq65.unity.ui.album;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends BaseActivity {
    public static final String IMAGE = "IMAGE";

    @BindView(R.id.iv_image)
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHideToolBar(true);//隐藏toolBar，必须在setContentView()前调用
        setContentView(R.layout.activity_image);
        setUnBinder(ButterKnife.bind(this));
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Image image = bundle.getParcelable(IMAGE);
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        } else {
            onError("加载失败，请重试");
        }
    }
}
