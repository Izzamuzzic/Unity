package com.zwq65.unity.ui.swipe_image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 查看大图Activity
 */
public class ImageActivity extends BaseActivity {
    public static final String POSITION = "POSITION";
    public static final String IMAGE_LIST = "IMAGE_LIST";
    @BindView(R.id.vp_images)
    ViewPager vpImages;
    int currentPosition;
    List<Image> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithoutInject(R.layout.activity_image);//不绑定toolBar
        setUnBinder(ButterKnife.bind(this));
        initData();
        initViewPager();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentPosition = bundle.getInt(POSITION);
        imageList = bundle.getParcelableArrayList(IMAGE_LIST);
    }

    private void initViewPager() {
        vpImages.setOffscreenPageLimit(2);
        Myadapter mAdapter = new Myadapter(this);
        vpImages.setAdapter(mAdapter);
        vpImages.setCurrentItem(currentPosition);//设置当前加载的资源为点击进入的图片
    }

    private class Myadapter extends PagerAdapter {
        LayoutInflater inflater;
        Context context;

        Myadapter(Context context) {
            this.context = context;
            this.inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            if (imageList == null) {
                return 0;
            }
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.item_image, container, false);
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
            Glide.with(context).load(imageList.get(position).getUrl()).into(ivImage);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

//    float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                x1 = ev.getRawX();
//                y1 = ev.getRawY();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = ev.getRawX();
//                y2 = ev.getRawY();
//                if (Math.abs(y2 - y1) < 150) {//左滑
//                    if (x2 - x1 > 200) {
//                        finish();
//                    } else if (x2 - x1 < -200) {//右滑
//
//                    }
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
