package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.sofia.Sofia;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.R;
import com.google.android.di.component.DaggerMyLikeMovieComponent;
import com.google.android.di.module.MyLikeMovieModule;
import com.google.android.mvp.contract.MyLikeMovieContract;
import com.google.android.mvp.presenter.MyLikeMoviePresenter;
import com.google.android.mvp.ui.adapter.HistoryAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyLikeMovieActivity extends BaseActivity<MyLikeMoviePresenter> implements MyLikeMovieContract.View {

    @BindView(R.id.toolbar_right)
    TextView toolbarRight;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.mylove_recyclerview)
    RecyclerView myloveRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.history_bottom_all)
    TextView historyBottomAll;
    @BindView(R.id.history_bottom)
    LinearLayout historyBottom;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMyLikeMovieComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myLikeMovieModule(new MyLikeMovieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_my_like_movie; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        toolbarRight.setVisibility(View.VISIBLE);
        toolbarRight.setText("编辑");
        toolbarTitle.setText("我的喜欢");
        toolbarRight.setTextColor(ContextCompat.getColor(this,R.color.white));
        mPresenter.userFavo(1, smartRefresh);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.userFavo(1, refreshLayout);
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
    }


    @Override
    public void editShow(boolean isShow) {
        historyBottom.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAdapter(HistoryAdapter historyAdapter) {
        myloveRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        myloveRecyclerview.setAdapter(historyAdapter);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_right, R.id.history_bottom_all, R.id.history_bottom_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.toolbar_right:
                if("编辑".equals(toolbarRight.getText().toString().trim())){
                    toolbarRight.setText("完成");
                    mPresenter.showEdit(true);
                }else{
                    toolbarRight.setText("编辑");
                    mPresenter.showEdit(false);
                }
                break;
            case R.id.history_bottom_all:
                if("全选".equals(historyBottomAll.getText().toString().trim())){
                    historyBottomAll.setText("全部取消");
                    mPresenter.addShowItemAll(true);
                }else{
                    historyBottomAll.setText("全选");
                    mPresenter.addShowItemAll(false);
                }
                break;
            case R.id.history_bottom_del:
                mPresenter.userFavoDel(smartRefresh);
                break;
        }
    }

}
