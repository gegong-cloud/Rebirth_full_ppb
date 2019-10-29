package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.google.android.R;
import com.google.android.di.component.DaggerDisCommissionComponent;
import com.google.android.di.module.DisCommissionModule;
import com.google.android.mvp.contract.DisCommissionContract;
import com.google.android.mvp.model.back_entity.DisCommission;
import com.google.android.mvp.presenter.DisCommissionPresenter;
import com.google.android.mvp.ui.adapter.DisCommissionAdapter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 分销提成
 */
public class DisCommissionActivity extends BaseActivity<DisCommissionPresenter> implements DisCommissionContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.dis_title)
    TextView disTitle;
    @BindView(R.id.money_recyclerview)
    RecyclerView moneyRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.toolbar_right)
    TextView toolbarRight;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDisCommissionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .disCommissionModule(new DisCommissionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_dis_commission; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("分销提成");
        toolbarRight.setText("提现");
        toolbarRight.setVisibility(View.VISIBLE);
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0).build();
        mPresenter.initInfo(smartRefresh);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.initInfo(refreshLayout);
            }
        });
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


    @Override
    public void showTitle(DisCommission disCommission) {
        String urlStr = "您有" + (!TextUtils.isEmpty(disCommission.getUsernum()) ? disCommission.getUsernum() : "0") + "位下级充值 共获得" + (!TextUtils.isEmpty(disCommission.getMoney()) ? disCommission.getMoney() : "0") + "元";
        disTitle.setText(urlStr);
    }

    @Override
    public void showAdapter(DisCommissionAdapter disCommissionAdapter) {
        moneyRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        moneyRecyclerview.setAdapter(disCommissionAdapter);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.toolbar_right:
                launchActivity(new Intent(this,MyWalletActivity.class));
                break;
        }
    }

}
