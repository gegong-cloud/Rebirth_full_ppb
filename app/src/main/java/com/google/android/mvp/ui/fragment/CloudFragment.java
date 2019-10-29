package com.google.android.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.sofia.StatusView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.android.R;
import com.google.android.di.component.DaggerCloudComponent;
import com.google.android.di.module.CloudModule;
import com.google.android.mvp.contract.CloudContract;
import com.google.android.mvp.presenter.CloudPresenter;
import com.google.android.mvp.ui.activity.AllSearchActivity;
import com.google.android.mvp.ui.activity.MyHistoryActivity;
import com.google.android.mvp.ui.adapter.CloudCenterAdapter;
import com.google.android.mvp.ui.adapter.CloudTitleAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 云播
 */
public class CloudFragment extends BaseFragment<CloudPresenter> implements CloudContract.View {

    @BindView(R.id.status_view)
    StatusView statusView;
    @BindView(R.id.home_title)
    RecyclerView homeTitle;
    @BindView(R.id.cloud_list_recyclerview)
    RecyclerView cloudListRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    Unbinder unbinder;


    //进度框
    private MaterialDialog materialDialog;

    public static CloudFragment newInstance() {
        CloudFragment fragment = new CloudFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCloudComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cloudModule(new CloudModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cloud, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(getActivity()).content("正在加载...").progress(true, 0).build();
        mPresenter.labelTop(smartRefresh, getActivity(), false);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.labelTop(smartRefresh, getActivity(), true);
            }
        });
        smartRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPresenter.loadMore(smartRefresh);
            }
        });
    }

    /**
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImmersionBar.with(this).destroy();
        unbinder.unbind();
    }


    @Override
    public void showAdapter(CloudTitleAdapter cloudTitleAdapter) {
        homeTitle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        homeTitle.setAdapter(cloudTitleAdapter);
    }

    @Override
    public void showAdapter(CloudCenterAdapter cloudCenterAdapter) {
        cloudListRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        cloudListRecyclerview.setAdapter(cloudCenterAdapter);
    }


    @OnClick({R.id.search_menu, R.id.history_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_menu:
                launchActivity(new Intent(getActivity(), AllSearchActivity.class));
                break;
            case R.id.history_menu:
                launchActivity(new Intent(getActivity(), MyHistoryActivity.class));
                break;
        }
    }
}
