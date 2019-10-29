package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.di.component.DaggerPayCodeShowComponent;
import com.google.android.di.module.PayCodeShowModule;
import com.google.android.mvp.contract.PayCodeShowContract;
import com.google.android.mvp.model.back_entity.PayMoneyResult;
import com.google.android.mvp.presenter.PayCodeShowPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 显示二维码提供给用户支付
 */
public class PayCodeShowActivity extends BaseActivity<PayCodeShowPresenter> implements PayCodeShowContract.View {

    @BindView(R.id.pay_code)
    ImageView payCode;
    @BindView(R.id.pay_result)
    TextView payResult;

    PayMoneyResult payMoneyResult;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPayCodeShowComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .payCodeShowModule(new PayCodeShowModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pay_code_show; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        payMoneyResult = (PayMoneyResult) getIntent().getSerializableExtra("payCode");
        if(payMoneyResult!=null&&!TextUtils.isEmpty(payMoneyResult.getUrl())){
            ArmsUtils.obtainAppComponentFromContext(this).imageLoader().loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .url(StringUtils.getBaseUrl()+payMoneyResult.getUrl())
                            .imageView(payCode)
                            .build());
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
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void showPayResult(String titleStr) {
        payResult.setText(titleStr);
    }
}
