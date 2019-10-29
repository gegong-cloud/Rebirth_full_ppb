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
import com.google.android.di.component.DaggerSetComponent;
import com.google.android.di.module.SetModule;
import com.google.android.mvp.contract.SetContract;
import com.google.android.mvp.presenter.SetPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 设置
 */
public class SetActivity extends BaseActivity<SetPresenter> implements SetContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.pay_accout)
    EditText payAccout;
    @BindView(R.id.card_name)
    EditText cardName;
    @BindView(R.id.card_number)
    EditText cardNumber;
    @BindView(R.id.bank_phone)
    EditText bankPhone;
    @BindView(R.id.bank_name)
    EditText bankName;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSetComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .setModule(new SetModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_set; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("设置");
        materialDialog = new MaterialDialog.Builder(this).content("正在保存账号...").progress(true, 0).build();
    }

    @Override
    public void showLoading() {
        materialDialog.show();
    }

    @Override
    public void hideLoading() {
        materialDialog.dismiss();
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
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    @OnClick({R.id.toolbar_back, R.id.pwd_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.pwd_ok:
                if (mPresenter != null) {
                    mPresenter.updateUserBank(payAccout.getText().toString(),bankName.getText().toString(),cardName.getText().toString(),cardNumber.getText().toString(),bankPhone.getText().toString());
                }
                break;
        }
    }

}
