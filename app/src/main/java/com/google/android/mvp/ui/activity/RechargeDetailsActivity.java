package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.di.component.DaggerRechargeDetailsComponent;
import com.google.android.di.module.RechargeDetailsModule;
import com.google.android.mvp.contract.RechargeDetailsContract;
import com.google.android.mvp.presenter.RechargeDetailsPresenter;
import com.google.android.mvp.ui.adapter.RechargeDetailsAdapter;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 充值明细
 */
public class RechargeDetailsActivity extends BaseActivity<RechargeDetailsPresenter> implements RechargeDetailsContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.money_recyclerview)
    RecyclerView moneyRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.recharege_title)
    TextView recharegeTitle;
    @BindView(R.id.recharege_details)
    TextView recharegeDetails;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRechargeDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .rechargeDetailsModule(new RechargeDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_recharge_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setText("充值记录");
        recharegeDetails.setText(Html.fromHtml("支付完成未生效?<font color='#FC488C'><u>在线客服检查</u></font>"));
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0).build();
        mPresenter.chargeLog(1, smartRefresh);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.chargeLog(1, refreshLayout);
            }
        });
        smartRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPresenter.loadMore(smartRefresh);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ImmersionBar.with(this).init();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
    }


    @Override
    public void showTitle(String titleStr) {
        recharegeTitle.setText(titleStr);
    }

    @Override
    public void showAdapter(RechargeDetailsAdapter rechargeDetailsAdapter) {
        moneyRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        moneyRecyclerview.setAdapter(rechargeDetailsAdapter);
    }

    @OnClick({R.id.toolbar_back, R.id.recharege_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.recharege_details:
                EventBus.getDefault().post("service", EventBusTags.CALL_SERVICE);
                break;
        }
    }

}
