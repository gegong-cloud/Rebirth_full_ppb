package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.di.component.DaggerOfflineRechargeComponent;
import com.google.android.di.module.OfflineRechargeModule;
import com.google.android.mvp.contract.OfflineRechargeContract;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.presenter.OfflineRechargePresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 线下充值
 */
public class OfflineRechargeActivity extends BaseActivity<OfflineRechargePresenter> implements OfflineRechargeContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recharge_type)
    TextView rechargeType;
    @BindView(R.id.recharge_money)
    TextView rechargeMoney;
    @BindView(R.id.offline_additional_code)
    TextView offlineAdditionalCode;

    MoneyShow showQrcode;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOfflineRechargeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .offlineRechargeModule(new OfflineRechargeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_offline_recharge; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        showQrcode = (MoneyShow) getIntent().getSerializableExtra("showQrcode");
        toolbarTitle.setText("线下充值");
        if(showQrcode!=null){
            rechargeType.setText(("wx".equals(showQrcode.getCode()) ? "微信支付" : "支付宝"));
            rechargeMoney.setText((!TextUtils.isEmpty(showQrcode.getMoney()) ? showQrcode.getMoney() : "")+"元");
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

    @OnClick({R.id.toolbar_back, R.id.recharge_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.recharge_complete:
                launchActivity(new Intent(this,RechargeDetailsActivity.class));
                killMyself();
                break;
        }
    }

}
