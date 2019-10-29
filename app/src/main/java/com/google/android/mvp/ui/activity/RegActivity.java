package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.di.component.DaggerRegComponent;
import com.google.android.di.module.RegModule;
import com.google.android.mvp.contract.RegContract;
import com.google.android.mvp.presenter.RegPresenter;
import com.google.android.mvp.ui.widget.downtimer.DownTimer;
import com.google.android.mvp.ui.widget.downtimer.DownTimerListener;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 注册
 */
public class RegActivity extends BaseActivity<RegPresenter> implements RegContract.View,DownTimerListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.reg_invite_code)
    EditText regInviteCode;
    @BindView(R.id.reg_phone)
    EditText regPhone;
    @BindView(R.id.reg_code)
    EditText regCode;
    @BindView(R.id.reg_get_code)
    TextView regGetCode;
    @BindView(R.id.pwd_new_pwd)
    EditText pwdNewPwd;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .regModule(new RegModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_reg; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("注册");
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
        ImmersionBar.with(this).init();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
    }


    @Override
    protected void onDestroy() {
        if(downTimer!=null){
            downTimer.stopDown();
            downTimer = null;
        }
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    @OnClick({R.id.toolbar_back, R.id.reg_get_code, R.id.reg_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.reg_get_code:
                mPresenter.sendSms(regPhone.getText().toString().trim());
                break;
            case R.id.reg_ok:
                mPresenter.register(regPhone.getText().toString().trim(), regCode.getText().toString(), pwdNewPwd.getText().toString().trim(),regInviteCode.getText().toString().trim());
                break;
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(regGetCode!=null){
            regGetCode.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            regGetCode.setClickable(false);
//            regGetCode.setBackground(null);
        }
    }

    @Override
    public void onFinish() {
        setCodeBg(false);
    }

    DownTimer downTimer;
    @Override
    public void setCodeBg(boolean isGet) {
        if (isGet) {
            downTimer = new DownTimer();
            downTimer.setListener(this);
            downTimer.startDown(60 * 1000);
        } else {
            if(regGetCode!=null){
                regGetCode.setText("获取");
                regGetCode.setClickable(true);
//                regGetCode.setBackground(null);
            }
        }
    }


}
