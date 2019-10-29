package com.google.android.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.ChooseAccoutContract;
import com.google.android.mvp.model.back_entity.AccountReceipt;
import com.google.android.mvp.model.back_entity.CommonEntity;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ChooseAccoutPresenter extends BasePresenter<ChooseAccoutContract.Model, ChooseAccoutContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    /**
     * 账户信息
     */
    AccountReceipt accountReceipt;
    /**
     * 账户信息
     */
    public void getUserBank() {
        mModel.getUserBank()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<AccountReceipt>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<AccountReceipt> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            accountReceipt = commonEntity.getData();
                            mRootView.showAccountInfo(commonEntity.getData());
                        }
                    }
                });

    }

    public void chooseAlipay() {
        if(accountReceipt!=null&&!TextUtils.isEmpty(accountReceipt.getAlipaycode())){
            AccountReceipt newAccountReceipt = new AccountReceipt();
            newAccountReceipt.setAlipaycode(accountReceipt.getAlipaycode());
            EventBus.getDefault().post(newAccountReceipt, EventBusTags.ACCOUNT_CHOOSE);
            mRootView.killMyself();
        }
    }

    public void chooseBank() {
        if(accountReceipt!=null&&!TextUtils.isEmpty(accountReceipt.getBankname())&&!TextUtils.isEmpty(accountReceipt.getAccountno())){
            AccountReceipt newAccountReceipt = new AccountReceipt();
            newAccountReceipt.setBankname(accountReceipt.getBankname());
            newAccountReceipt.setAccountno(accountReceipt.getAccountno());
            EventBus.getDefault().post(newAccountReceipt, EventBusTags.ACCOUNT_CHOOSE);
            mRootView.killMyself();
        }
    }

    @Inject
    public ChooseAccoutPresenter(ChooseAccoutContract.Model model, ChooseAccoutContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }


}
