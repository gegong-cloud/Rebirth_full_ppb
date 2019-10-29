package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.di.component.DaggerStarUpComponent;
import com.google.android.di.module.StarUpModule;
import com.google.android.mvp.contract.StarUpContract;
import com.google.android.mvp.presenter.StarUpPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class StarUpActivity extends BaseActivity<StarUpPresenter> implements StarUpContract.View {

    //权限申请
    private RxPermissions mRxPermissions;

    /**
     * 定时器
     */
    Timer timer;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerStarUpComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .starUpModule(new StarUpModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_star_up; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        if(TextUtils.isEmpty(HbCodeUtils.getAndroidDevice())){
//            HbCodeUtils.setAndroidDevice(StringUtils.buildDeviceUUID(this));
//        }
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0)
                .backgroundColorRes(R.color.tran_60_black)
                .contentColor(ContextCompat.getColor(this, R.color.white))
                .build();
        materialDialog.setCancelable(false);
        if (!TextUtils.isEmpty(HbCodeUtils.getNetUrl())) {//api地址判断，重新开始
            HbCodeUtils.setNetUrl("");
        }
//        if(TextUtils.isEmpty(HbCodeUtils.getDeviceId())){
//            initTimer();
//            initOpenInstall();
//        }else{
        if (mPresenter != null) {//回调了之后才调用网络
            mPresenter.readPermiss(this, "");
        }
//        }


    }

    private void initOpenInstall() {
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
//                if(TextUtils.isEmpty(TextConstant.BUILT_IN_ANDROID)){//判断内置是否为空，为空保存openinstall userid
//                    HbCodeUtils.setDeviceId(bindData);
//                }
                if (mPresenter != null) {
                    mPresenter.readPermiss(StarUpActivity.this, bindData);
                }
                Timber.e("lhw openinstall = " + appData.toString());
                if (timer != null) {
                    Timber.e("lhw timer cancel ");
                    timer.cancel();
                }
//                if(mPresenter!=null){//回调了之后才调用网络
//                    mPresenter.userInfo();
//                }
            }
        });

    }

    @Override
    public void showLoading() {
        materialDialog.show();
    }

    @Override
    public void hideLoading() {
        materialDialog.cancel();
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
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void setMaterContent(String materContent) {
        if (materialDialog != null) {
            materialDialog.setContent(materContent);
        }
    }

    @Subscriber(tag = EventBusTags.NET_WORK_URL)
    public void getNowUrl(String urlStr) {
        if (mPresenter != null) {
            setMaterContent("正在选择加速通道...");
            mPresenter.userInfo("");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //获取唤醒参数
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        wakeUpAdapter = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }

    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();
            Timber.d("lhw = " + appData.toString());
        }
    };


    /**
     * 延时3秒启动
     */
    private void initTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Timber.e("lhw timer begin");
                if (mPresenter != null) {//回调了之后才调用网络
                    mPresenter.readPermiss(StarUpActivity.this, "");
                }
            }
        }, 3000);
    }


}
