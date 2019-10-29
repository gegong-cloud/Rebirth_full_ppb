package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.di.component.DaggerMyWalletComponent;
import com.google.android.di.module.MyWalletModule;
import com.google.android.mvp.contract.MyWalletContract;
import com.google.android.mvp.model.back_entity.AccountDetail;
import com.google.android.mvp.model.back_entity.AccountReceipt;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.presenter.MyWalletPresenter;
import com.yanzhenjie.sofia.Sofia;
import com.yanzhenjie.sofia.StatusView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 我的钱包
 */
public class MyWalletActivity extends BaseActivity<MyWalletPresenter> implements MyWalletContract.View {

    @BindView(R.id.status_view)
    StatusView statusView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.accout_money)
    TextView accoutMoney;
    @BindView(R.id.income_money)
    TextView incomeMoney;
    @BindView(R.id.withdrawed_money)
    TextView withdrawedMoney;
    @BindView(R.id.bank_logo)
    ImageView bankLogo;
    @BindView(R.id.bank_name)
    TextView bankName;
    @BindView(R.id.bank_number)
    TextView bankNumber;
    @BindView(R.id.choose_accout_title)
    TextView chooseAccoutTitle;
    @BindView(R.id.input_money)
    EditText inputMoney;
    @BindView(R.id.available_money)
    TextView availableMoney;
    @BindView(R.id.my_wallet_prompt)
    TextView myWalletPrompt;

    /**
     * 我的账户信息
     */
    AccountDetail.AccountInfo accountInfo;

    /**
     * 用户提现收款账号
     */
    AccountReceipt accountReceipt;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMyWalletComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myWalletModule(new MyWalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_my_wallet; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0).build();
        //状态栏
        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("我的钱包");
        if(mPresenter!=null){
            mPresenter.userIndex();
            mPresenter.getConfig();
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

    @Subscriber(tag = EventBusTags.ACCOUNT_CHOOSE)
    public void chooseAccount(AccountReceipt newAccountReceipt){
        accountReceipt = newAccountReceipt;
        if(newAccountReceipt !=null){
            chooseAccoutTitle.setVisibility(View.GONE);
            bankLogo.setVisibility(View.VISIBLE);
            bankName.setVisibility(View.VISIBLE);
            bankNumber.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(newAccountReceipt.getAlipaycode())){
                bankLogo.setImageResource(R.drawable.pay_logo);
                bankName.setText("支付宝");
                bankNumber.setText("支付宝账号:"+newAccountReceipt.getAlipaycode());
            }
            if(!TextUtils.isEmpty(newAccountReceipt.getBankname())&&!TextUtils.isEmpty(newAccountReceipt.getAccountno())){
                bankLogo.setImageResource(R.drawable.bank_logo);
                bankName.setText(newAccountReceipt.getBankname());
                bankNumber.setText("尾号"+ StringUtils.getNoLast(newAccountReceipt.getAccountno()));
            }
        }

    }

    @OnClick({R.id.toolbar_back, R.id.choose_accout, R.id.wihtdarw_all, R.id.need_withdraw, R.id.make_money})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.choose_accout:
                launchActivity(new Intent(this, ChooseAccoutActivity.class));
                break;
            case R.id.wihtdarw_all://全部提现
                if(accountInfo!=null&&!TextUtils.isEmpty(accountInfo.getBalance())){
                    inputMoney.setText(accountInfo.getBalance());
                }
                break;
            case R.id.need_withdraw://我要提现
                mPresenter.withDrowApply(inputMoney.getText().toString(),accountReceipt);
                break;
            case R.id.make_money:
                if(HbCodeUtils.getUserInfo()!=null){
                    UserEntity userEntity = HbCodeUtils.getUserInfo();
                    String urlLike2 = userEntity==null?"":(userEntity.getMenulink()==null?"":(!TextUtils.isEmpty(userEntity.getMenulink().getHowmoney_link())?userEntity.getMenulink().getHowmoney_link():""));
                    launchActivity(new Intent(MyWalletActivity.this,WebViewActivity.class)
                            .putExtra("titleStr","我要赚钱")
                            .putExtra("url",urlLike2)
                    );
                }
                break;
        }
    }

    @Override
    public void setScrollText(ScrollMsg scrollText) {
        if (scrollText != null && !TextUtils.isEmpty(scrollText.getValue())) {
            myWalletPrompt.setText(scrollText.getValue());
        }
    }

    @Override
    public void wihtdarw_all(String allMoney) {
        inputMoney.setText(allMoney);
    }

    @Override
    public void loadAccountInfo(AccountDetail accountDetail) {//accoutMoney
        if(accountDetail!=null&&accountDetail.getAccountInfo()!=null&&accountDetail.getAccountInfo().size()>0){
            for(AccountDetail.AccountInfo accountInfo1:accountDetail.getAccountInfo()){
                if("2".equals(accountInfo1.getAccounttype())){
                    accountInfo = accountInfo1;
                    break;
                }
            }
            if(accountInfo!=null){
                accoutMoney.setText(!TextUtils.isEmpty(accountInfo.getBalance())?accountInfo.getBalance():"0.00");
                incomeMoney.setText(!TextUtils.isEmpty(accountInfo.getGainsum())?accountInfo.getGainsum():"0.00");
                withdrawedMoney.setText(!TextUtils.isEmpty(accountInfo.getWithdrowsum())?accountInfo.getWithdrowsum():"0.00");
                availableMoney.setText("可用余额"+(!TextUtils.isEmpty(accountInfo.getBalance())?accountInfo.getBalance():"0.00")+"元");
            }

        }
    }
}
