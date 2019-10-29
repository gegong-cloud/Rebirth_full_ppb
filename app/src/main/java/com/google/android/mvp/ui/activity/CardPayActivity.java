package com.google.android.mvp.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.yanzhenjie.sofia.Sofia;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.R;
import com.google.android.app.utils.JumpUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.di.component.DaggerCardPayComponent;
import com.google.android.di.module.CardPayModule;
import com.google.android.mvp.contract.CardPayContract;
import com.google.android.mvp.presenter.CardPayPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CardPayActivity extends BaseActivity<CardPayPresenter> implements CardPayContract.View {

    @BindView(R.id.reg_pwd_old)
    EditText regPwdOld;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCardPayComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cardPayModule(new CardPayModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_card_pay; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);


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
    protected void onResume() {
        super.onResume();
        initDialog();
    }

    @OnClick({R.id.toolbar_back, R.id.reg_paste, R.id.reg_reg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.reg_paste:
                clickPaste();
                break;
            case R.id.reg_reg:
                if(mPresenter!=null){
                    mPresenter.cardPay(this,regPwdOld.getText().toString());
                }
                break;
        }
    }

    void initDialog(){
        new AlertDialog.Builder(this)
                .setTitle("粘贴提示")
                .setCancelable(false)
                .setMessage("是否粘贴卡密到输入框?")
                .setPositiveButton("粘贴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clickPaste();
                    }
                })
                .setNegativeButton("取消",null)
                .create().show();
    }

    @Override
    public void clickPaste() {
        String pasteStr = JumpUtils.getClipboardText(this);
        if(regPwdOld!=null&&!TextUtils.isEmpty(pasteStr)){
            regPwdOld.setText(pasteStr);
        }else{
            StringUtils.makeTextCenter(this,"粘贴板没有内容！");
        }
    }
}
