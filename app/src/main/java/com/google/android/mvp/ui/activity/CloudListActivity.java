package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.google.android.R;
import com.google.android.di.component.DaggerCloudListComponent;
import com.google.android.di.module.CloudListModule;
import com.google.android.mvp.contract.CloudListContract;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.presenter.CloudListPresenter;
import com.google.android.mvp.ui.adapter.CloudListAdapter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 单个云播平台视频列表
 */
public class CloudListActivity extends BaseActivity<CloudListPresenter> implements CloudListContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.cloud_list_recyclerview)
    RecyclerView cloudListRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.cloud_title)
    TextView cloudTitle;
    @BindView(R.id.yunbo_tag)
    LinearLayout yunboTag;

    String labelTagStr = "";//标签列表

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCloudListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cloudListModule(new CloudListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_cloud_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("云播");
        labelTagStr = getIntent().getStringExtra("cloudTags");
        mPresenter.yunboIndex(1, smartRefresh, labelTagStr);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.yunboIndex(1, smartRefresh, labelTagStr);
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

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        killMyself();
    }

    @Override
    public void showAdapter(CloudListAdapter cloudListAdapter) {
        cloudListRecyclerview.setAdapter(cloudListAdapter);
        cloudListRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public void showTitle(YunboList yunboList) {
        yunboTag.setVisibility(!TextUtils.isEmpty(yunboList.getName()) ? View.VISIBLE:View.GONE);
        cloudTitle.setText(!TextUtils.isEmpty(yunboList.getName()) ? yunboList.getName() : "");
    }
}
