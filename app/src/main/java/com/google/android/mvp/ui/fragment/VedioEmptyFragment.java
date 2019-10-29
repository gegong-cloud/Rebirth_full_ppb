package com.google.android.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.google.android.di.component.DaggerVedioEmptyComponent;
import com.google.android.di.module.VedioEmptyModule;
import com.google.android.mvp.contract.VedioEmptyContract;
import com.google.android.mvp.presenter.VedioEmptyPresenter;

import com.google.android.R;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class VedioEmptyFragment extends BaseFragment<VedioEmptyPresenter> implements VedioEmptyContract.View {

    public static VedioEmptyFragment newInstance() {
        VedioEmptyFragment fragment = new VedioEmptyFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerVedioEmptyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .vedioEmptyModule(new VedioEmptyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vedio_empty, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    /**
     *a
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

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

    }
}
