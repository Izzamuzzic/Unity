package com.zwq65.unity.ui.swipe_image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看大图Activity
 */
public class ImageActivity extends BaseActivity implements ImageMvpView {
    public static final String POSITION = "POSITION";
    public static final String IMAGE_LIST = "IMAGE_LIST";

    int currentPosition, pageSize;//当前显示的大图position
    List<Image> imageList;//图片list

    @BindView(R.id.vp_images)
    ViewPager vpImages;
    @BindView(R.id.tv_current_page)
    TextView tvCurrentPage;//当前页数
    @BindView(R.id.tv_save_image)
    TextView tvSaveImage;

    @Inject
    ImageMvpPresenter<ImageMvpView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithoutInject(R.layout.activity_image);//不绑定toolBar
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        initData();
        initView();
        initViewPager();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentPosition = bundle.getInt(POSITION);
        imageList = bundle.getParcelableArrayList(IMAGE_LIST);
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        pageSize = imageList.size();
    }

    private void initView() {
        setCurrentPage();
    }

    /**
     * 设置当前页数
     */
    private void setCurrentPage() {
        tvCurrentPage.setText(currentPosition + 1 + " / " + pageSize);
    }

    private void initViewPager() {
        vpImages.setOffscreenPageLimit(2);//预加载2个item
        Myadapter mAdapter = new Myadapter();
        vpImages.setAdapter(mAdapter);
        vpImages.setCurrentItem(currentPosition);//设置当前加载的资源为点击进入的图片
        vpImages.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //滑动改变当前页数
                currentPosition = position;
                setCurrentPage();
            }
        });
    }

    @OnClick(R.id.tv_save_image)
    public void onViewClicked() {
        //保存大图
        mPresenter.savePicture(imageList.get(currentPosition));
    }

    @Override
    public void savePictrueWhetherSucceed(Boolean success) {
        if (success) {
            showSuccessAlert("保存成功！");
        } else {
            showErrorAlert("保存失败！");
        }
    }

    /**
     * 显示大图viewpager's adapter
     */
    private class Myadapter extends PagerAdapter {
        LayoutInflater inflater;

        Myadapter() {
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
            //显示大图view
            View view = getLayoutInflater().inflate(R.layout.item_image, container, false);
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
            Glide.with(ImageActivity.this).load(imageList.get(position).getUrl()).into(ivImage);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }


    }
}
