package com.zwq65.unity.ui.album.imagedetail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tuyenmonkey.mkloader.MKLoader;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui._base.BaseActivity;
import com.zwq65.unity.ui.custom.other.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * 查看大图Activity
 */
public class ImageActivity extends BaseActivity implements ImageMvpView {
    public static final String POSITION = "POSITION";
    public static final String IMAGE_LIST = "IMAGE_LIST";
    public static final String COLLECTIONS = "COLLECTIONS";

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
        mPresenter.savePicture(this, imageList.get(currentPosition));
    }

    @Override
    public void savePictrueWhetherSucceed(Boolean success) {
        if(success){
           showSuccessAlert(R.string.save_success);
        }else{
            showSuccessAlert(R.string.save_fail);
        }
    }

    @Override
    public void collectPictrueWhetherSucceed(Boolean success) {
        if (success) {
            showSuccessAlert(R.string.collect_success);
        } else {
            showErrorAlert(R.string.collect_fail);
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
        public Object instantiateItem(ViewGroup container, final int position) {
            //显示大图view
            View view = getLayoutInflater().inflate(R.layout.item_image, container, false);
            PhotoView ivImage = (PhotoView) view.findViewById(R.id.iv_image);
            CheckBox cbLove = (CheckBox) view.findViewById(R.id.cb_love);

            final Image image = imageList.get(position);

            cbLove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mPresenter.collectPicture(image);
                    } else {
                        mPresenter.cancelCollectPicture(image);
                    }
                }
            });
            final MKLoader pbLoader = (MKLoader) view.findViewById(R.id.pb_loader);
            Glide.with(ImageActivity.this).load(image.getUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            showMessage(R.string.load_fail);
                            pbLoader.setVisibility(GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pbLoader.setVisibility(GONE);
                            return false;
                        }
                    }).into(ivImage);
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
