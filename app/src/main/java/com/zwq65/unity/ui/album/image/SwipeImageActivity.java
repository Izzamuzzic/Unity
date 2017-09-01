package com.zwq65.unity.ui.album.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zwq65.unity.ui.album.image.ImageActivity.IMAGE_LIST;
import static com.zwq65.unity.ui.album.image.ImageActivity.POSITION;

public class SwipeImageActivity extends BaseActivity {

    @BindView(R.id.rv_images)
    RecyclerView rvImages;
    int currentPosition;
    List<Image> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_image);
        setUnBinder(ButterKnife.bind(this));
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentPosition = bundle.getInt(POSITION);
        imageList = bundle.getParcelableArrayList(IMAGE_LIST);
    }
}