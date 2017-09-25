package com.zwq65.unity.ui.album.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseViewActivity;

import java.util.List;

import butterknife.BindView;

import static com.zwq65.unity.ui.album.image.ImageActivity.IMAGE_LIST;
import static com.zwq65.unity.ui.album.image.ImageActivity.POSITION;

public class SwipeImageActivity extends BaseViewActivity {

    @BindView(R.id.rv_images)
    RecyclerView rvImages;
    int currentPosition;
    List<Image> imageList;


    @Override
    public void injectComponent() {
        //no need
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_swipe_image;
    }

    @Override
    public Boolean initBaseTooBar() {
        return null;
    }

    @Override
    public void dealIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        currentPosition = bundle.getInt(POSITION);
        imageList = bundle.getParcelableArrayList(IMAGE_LIST);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
