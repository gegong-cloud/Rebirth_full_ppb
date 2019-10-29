package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.di.component.DaggerLoginComponent;
import com.google.android.di.module.LoginModule;
import com.google.android.mvp.contract.LoginContract;
import com.google.android.mvp.presenter.LoginPresenter;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_pwd)
    EditText loginPwd;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("登录");
        materialDialog = new MaterialDialog.Builder(this).content("正在登陆").progress(true, 0).build();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).init();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscriber(tag = EventBusTags.REG_SUCCESS)
    public void regSuccess(String event){
        killMyself();
    }

    @OnClick({R.id.toolbar_back, R.id.login_ok, R.id.login_reg, R.id.login_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.login_ok:
                mPresenter.login(loginPhone.getText().toString(),loginPwd.getText().toString());
                break;
            case R.id.login_reg:
                launchActivity(new Intent(this,RegActivity.class));
                break;
            case R.id.login_forget:
                launchActivity(new Intent(this,ForgetPwdActivity.class));
                break;
        }
    }

}
