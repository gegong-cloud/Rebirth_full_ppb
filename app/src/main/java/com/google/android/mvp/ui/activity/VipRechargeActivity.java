package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding3.view.RxView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.sunfusheng.marqueeview.MarqueeView;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.sofia.Sofia;
import com.yanzhenjie.sofia.StatusView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.di.component.DaggerVipRechargeComponent;
import com.google.android.di.module.VipRechargeModule;
import com.google.android.mvp.contract.VipRechargeContract;
import com.google.android.mvp.model.back_entity.HornEntity;
import com.google.android.mvp.presenter.VipRechargePresenter;
import com.google.android.mvp.ui.adapter.RechargeMoneyAdapter;
import com.google.android.mvp.ui.adapter.VipRechargeTypeAdapter;
import io.reactivex.functions.Consumer;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * vip充值
 */
public class VipRechargeActivity extends BaseActivity<VipRechargePresenter> implements VipRechargeContract.View {

    @BindView(R.id.status_view)
    StatusView statusView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.self_help_recyclerview)
    RecyclerView selfHelpRecyclerview;
    @BindView(R.id.self_top_recyclerview)
    RecyclerView selfTopRecyclerview;
    @BindView(R.id.rechare_tip)
    TextView rechareTip;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.home_deng_bg)
    LinearLayout homeDengBg;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVipRechargeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .vipRechargeModule(new VipRechargeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_vip_recharge; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0).build();
        materialDialog.setCancelable(false);

        //状态栏
        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("充值VIP");
        if (mPresenter != null) {
            mPresenter.broadcast();
            mPresenter.chargeInterfaceList();
            mPresenter.chargeIndex(getIntent().getStringExtra("typeStr"));
        }
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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscriber(tag = EventBusTags.CARD_PWD_OPEN)
    public void cardPwdOpen(String event) {
        mPresenter.nowOpen(event);
    }

    @OnClick({R.id.toolbar_back, R.id.vip_now_pay, R.id.vip_note_select, R.id.vip_call_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.vip_now_pay://立即支付
                RxView.clicks(view)
                        .throttleFirst(1500, TimeUnit.MILLISECONDS)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                // do something
                                if (mPresenter != null) {
                                    mPresenter.recharge();
                                }
                            }
                        });
                break;
            case R.id.vip_note_select://订单查询
                launchActivity(new Intent(this, RechargeDetailsActivity.class));
                break;
            case R.id.vip_call_service://联系客服
                EventBus.getDefault().post("", EventBusTags.CALL_SERVICE);
                break;
        }
    }


    @Override
    public void showAdapter(RechargeMoneyAdapter rechargeMoneyAdapter) {
        selfTopRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        selfTopRecyclerview.setAdapter(rechargeMoneyAdapter);
    }


    @Override
    public void showAdapter(VipRechargeTypeAdapter vipRechargeTypeAdapter) {
        selfHelpRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        selfHelpRecyclerview.setAdapter(vipRechargeTypeAdapter);
    }

    @Override
    public void regWx(String wxAppID) {
        registerToWX(wxAppID);
    }

    @Override
    public void showTip(boolean isShow) {
        rechareTip.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHorn(List<HornEntity> listData) {
        homeDengBg.setVisibility(View.VISIBLE);
        List<String> showList = new ArrayList<>();
        for (HornEntity horn : listData) {
            showList.add(horn.getInfo());
        }
        marqueeView.startWithList(showList);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (marqueeView != null) {
            marqueeView.startFlipping();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (marqueeView != null) {
            marqueeView.stopFlipping();
        }
    }


    /**
     * 注册微信
     */
    public static IWXAPI mWxApi;

    private void registerToWX(String wxAppID) {
//        if(!TextUtils.isEmpty(HbCodeUtils.getWxAppId())){
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, wxAppID, false);
        // 将该app注册到微信
        mWxApi.registerApp(wxAppID);
//        }
    }
}
