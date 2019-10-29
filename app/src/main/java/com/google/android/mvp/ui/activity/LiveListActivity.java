package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.google.android.R;
import com.google.android.di.component.DaggerLiveListComponent;
import com.google.android.di.module.LiveListModule;
import com.google.android.mvp.contract.LiveListContract;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.presenter.LiveListPresenter;
import com.google.android.mvp.ui.adapter.LiveRoomListAdapter;
import com.google.android.mvp.ui.adapter.RecyclerItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 单个直播平台主播列表
 */
public class LiveListActivity extends BaseActivity<LiveListPresenter> implements LiveListContract.View {

    @BindView(R.id.live_number)
    TextView liveNumber;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.live_recyclerview)
    RecyclerView liveRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;

    String platFormName = "";

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLiveListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .liveListModule(new LiveListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_live_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        platFormName = getIntent().getStringExtra("name");
        if(mPresenter!=null){
            mPresenter.liveAnchors(platFormName,smartRefresh);
        }
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.liveAnchors(platFormName,refreshLayout);
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
    public void showTilte(LiveEntity liveEntity) {
        toolbarTitle.setText(!TextUtils.isEmpty(liveEntity.getTitle()) ? liveEntity.getTitle() : "直播视频");
        //font_fc488c
        String numberStr = !TextUtils.isEmpty(liveEntity.getCount()) ? liveEntity.getCount() : "0";
        liveNumber.setText(Html.fromHtml("主播数:<font color='#FC488C'>"+numberStr+"</font>"));
    }

    @Override
    public void showAdapter(LiveRoomListAdapter livePlatformAdapter) {
        liveRecyclerview.setAdapter(livePlatformAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        liveRecyclerview.setLayoutManager(gridLayoutManager);
        liveRecyclerview.addItemDecoration(new RecyclerItemDecoration(15, 3));
    }
}
