package com.zwq65.unity.ui.home;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tv_test)
    TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUnBinder(ButterKnife.bind(this));
    }

    @OnClick(R.id.tv_test)
    public void onViewClicked() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1000);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tvTest.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        valueAnimator.start();
    }
}
