package com.google.android.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.SetContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.upload.AccountUpload;

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
public class SetPresenter extends BasePresenter<SetContract.Model, SetContract.View> {
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
     * @param payali
     * @param bankName
     * @param bankPeople
     * @param bankNo
     * @param bankPhone
     */
    public void updateUserBank(String payali, String bankName, String bankPeople, String bankNo, String bankPhone) {
        if(TextUtils.isEmpty(payali)&&TextUtils.isEmpty(bankName)){
            ArmsUtils.makeText(mApplication,"请输入支付宝账号或银行卡信息");
            return;
        }

        if(!TextUtils.isEmpty(bankName)){
            if(TextUtils.isEmpty(bankPeople)){
                ArmsUtils.makeText(mApplication,"请输入持卡人姓名");
                return;
            }
            if(TextUtils.isEmpty(bankNo)){
                ArmsUtils.makeText(mApplication,"请输入银行卡号");
                return;
            }
        }
        AccountUpload accountUpload = new AccountUpload();
        accountUpload.setAlipaycode(payali);
        accountUpload.setBankname(bankName);
        accountUpload.setAccountname(bankPeople);
        accountUpload.setAccountno(bankNo);
        accountUpload.setAccountphone(bankPhone);
        mModel.updateUserBank(accountUpload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())) {
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
                            EventBus.getDefault().post("", EventBusTags.ADD_ACCOUNT);
                            mRootView.killMyself();
                        }else{
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
                        }
                    }
                });

    }


    @Inject
    public SetPresenter(SetContract.Model model, SetContract.View rootView) {
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
