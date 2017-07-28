package com.zwq65.unity.ui.swipe_image;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends BaseActivity {
    public static final String IMAGE = "IMAGE";
    @BindView(R.id.rv_images)
    RecyclerView rvImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithoutInject(R.layout.activity_image);//不绑定toolBar
        setUnBinder(ButterKnife.bind(this));
        initData();
    }

    private void initData() {
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
