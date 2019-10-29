package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.di.component.DaggerCarouselAdComponent;
import com.google.android.di.module.CarouselAdModule;
import com.google.android.mvp.contract.CarouselAdContract;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.presenter.CarouselAdPresenter;
import com.google.android.mvp.ui.widget.downtimer.DownTimer;
import com.google.android.mvp.ui.widget.downtimer.DownTimerListener;
import com.yanzhenjie.sofia.Sofia;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CarouselAdActivity extends BaseActivity<CarouselAdPresenter> implements CarouselAdContract.View, DownTimerListener {

    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.sp_jump_btn)
    Button spJumpBtn;

    /**
     * 倒计时对象
     */
    DownTimer downTimer;

    long currentTime = 0;

    HomeAdv adList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCarouselAdComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .carouselAdModule(new CarouselAdModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_carousel_ad; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Sofia.with(this).invasionStatusBar().statusBarBackground(Color.TRANSPARENT);
        adList = (HomeAdv) getIntent().getSerializableExtra("adList");
        if (adList != null && !TextUtils.isEmpty(adList.getImage())) {

            StringUtils.showImgView(ArmsUtils.obtainAppComponentFromContext(CarouselAdActivity.this),banner,CarouselAdActivity.this,(StringUtils.getBaseUrl()+ adList.getImage()),StringUtils.getStrHashCode(adList.getImage()));

//            ArmsUtils.obtainAppComponentFromContext(CarouselAdActivity.this).imageLoader().loadImage(CarouselAdActivity.this,
//                    ImageConfigImpl
//                            .builder()
//                            .url()
//                            .imageView(banner)
//                            .build());
            spJumpBtn.setVisibility(View.VISIBLE);
            showDownTimer(5);
        } else {
            launchActivity(new Intent(this, MainActivity.class));
            killMyself();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }


    @Override
    protected void onPause() {//暂停
        super.onPause();
        if (downTimer != null && currentTime > 0) {
            downTimer.stopDown();
        }
    }

    @Override
    protected void onResume() {//恢复
        super.onResume();
        if (downTimer != null && currentTime > 0) {
            downTimer.startDown(currentTime);
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        currentTime = millisUntilFinished;
        if (spJumpBtn != null) {
            spJumpBtn.setText("" + (millisUntilFinished / 1000));
        } else {
            Timber.e("lhw-----倒计时对象为空");
        }
    }

    @Override
    public void onFinish() {
        if (spJumpBtn != null) {
            spJumpBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.star_delete));
            spJumpBtn.setTextColor(ContextCompat.getColor(this, R.color.background_transparent));
            spJumpBtn.setText("X");
            castMain();
        }
    }

    /**
     * @param mtime 显示倒计时
     */
    private void showDownTimer(int mtime) {
        downTimer = new DownTimer();
        downTimer.setListener(this);
        downTimer.startDown(mtime * 1000);
    }


    @OnClick({R.id.banner, R.id.sp_jump_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.banner:
                if(adList!=null&&!TextUtils.isEmpty(adList.getLink())){
                    mPresenter.logAd(adList.getId());
                    HbCodeUtils.openBrowser(CarouselAdActivity.this,adList.getLink());
//                    launchActivity(new Intent(this, MainActivity.class));
//                    killMyself();
                }
                break;
            case R.id.sp_jump_btn:
                if ("X".equals(spJumpBtn.getText().toString())) {
                    castMain();
                }
                break;
        }
    }

    /**
     * 跳转主页
     */
    void castMain(){
        launchActivity(new Intent(this, MainActivity.class));
        ArmsUtils.makeText(this, "欢迎回来");
        killMyself();
    }

}
