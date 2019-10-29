package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.di.component.DaggerPayOptionComponent;
import com.google.android.di.module.PayOptionModule;
import com.google.android.mvp.contract.PayOptionContract;
import com.google.android.mvp.presenter.PayOptionPresenter;
import com.google.android.mvp.ui.adapter.MoneyShowAdapter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 支付界面选择 已弃用
 */
public class PayOptionActivity extends BaseActivity<PayOptionPresenter> implements PayOptionContract.View {

    @BindView(R.id.pay_input)
    EditText payInput;
    @BindView(R.id.pay_recyclerview)
    RecyclerView payRecyclerview;
    @BindView(R.id.pay_submit)
    Button paySubmit;
    @BindView(R.id.pay_result)
    TextView payResult;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPayOptionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .payOptionModule(new PayOptionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pay_option; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.chargeIndex();
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
    public void showAdapter(MoneyShowAdapter moneyShowAdapter) {
        payRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        payRecyclerview.setAdapter(moneyShowAdapter);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.pay_submit)
    public void onViewClicked() {
        mPresenter.recharge(payInput.getText().toString());
    }
}
