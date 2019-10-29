package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.di.component.DaggerChooseAccoutComponent;
import com.google.android.di.module.ChooseAccoutModule;
import com.google.android.mvp.contract.ChooseAccoutContract;
import com.google.android.mvp.model.back_entity.AccountReceipt;
import com.google.android.mvp.presenter.ChooseAccoutPresenter;
import com.yanzhenjie.sofia.Sofia;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 选择账号
 */
public class ChooseAccoutActivity extends BaseActivity<ChooseAccoutPresenter> implements ChooseAccoutContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.alipay_number)
    TextView alipayNumber;
    @BindView(R.id.alipay_choose)
    RelativeLayout alipayChoose;
    @BindView(R.id.bank_name)
    TextView bankName;
    @BindView(R.id.bank_number)
    TextView bankNumber;
    @BindView(R.id.bank_choose)
    RelativeLayout bankChoose;

    //进度框
    private MaterialDialog materialDialog;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChooseAccoutComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .chooseAccoutModule(new ChooseAccoutModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_choose_accout; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
        Sofia.with(this)
                .statusBarDarkFont()//深色字体
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("选择账号");
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0).build();
        if(mPresenter!=null){
            mPresenter.getUserBank();
        }
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


    @Subscriber(tag = EventBusTags.ADD_ACCOUNT)
    public void refAccount(String event){
        if(mPresenter!=null){
           mPresenter.getUserBank();
        }
    }

    @OnClick({R.id.toolbar_back, R.id.alipay_choose, R.id.bank_choose, R.id.account_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.alipay_choose:
                mPresenter.chooseAlipay();
                break;
            case R.id.bank_choose:
                mPresenter.chooseBank();
                break;
            case R.id.account_add:
                launchActivity(new Intent(this, SetActivity.class));
                break;
        }
    }

    @Override
    public void showAccountInfo(AccountReceipt accountReceipt) {
        if (accountReceipt != null) {
            if (!TextUtils.isEmpty(accountReceipt.getAlipaycode())) {
                alipayChoose.setVisibility(View.VISIBLE);
                alipayNumber.setText("支付宝账号:"+accountReceipt.getAlipaycode());
            }
            if (!TextUtils.isEmpty(accountReceipt.getBankname())&&!TextUtils.isEmpty(accountReceipt.getAccountno())) {
                bankChoose.setVisibility(View.VISIBLE);
                bankName.setText(accountReceipt.getBankname());
                bankNumber.setText("银行卡账号:"+accountReceipt.getAccountno());
            }
        }
    }


}
