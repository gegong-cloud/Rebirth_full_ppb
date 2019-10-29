package com.google.android.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.MyWalletContract;
import com.google.android.mvp.model.back_entity.AccountDetail;
import com.google.android.mvp.model.back_entity.AccountReceipt;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.WithDrowAppUpload;

import java.util.List;

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
public class MyWalletPresenter extends BasePresenter<MyWalletContract.Model, MyWalletContract.View> {
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
    public void userIndex() {
        mModel.userIndex()
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<AccountDetail>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<AccountDetail> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            mRootView.loadAccountInfo(commonEntity.getData());
                        }
                    }
                });

    }

    /**
     * 公告信息
     */
    public void getConfig() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setName("bolletinfo");
        mModel.getConfig(homeAdvUpload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<ScrollMsg>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<ScrollMsg>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if(commonEntity.getData().size()>0){
                                mRootView.setScrollText(commonEntity.getData().get(0));
                            }
                        }
                    }
                });

    }

    /**
     * 提现
     * @param moneyStr
     * @param accountReceipt
     */
    public void withDrowApply(String moneyStr, AccountReceipt accountReceipt) {
        if(accountReceipt==null){
            ArmsUtils.makeText(mApplication,"请选择到账账号");
            return;
        }
        if(TextUtils.isEmpty(moneyStr)){
            ArmsUtils.makeText(mApplication,"请输入提现金额");
            return;
        }
        WithDrowAppUpload withDrowAppUpload = new WithDrowAppUpload();
        withDrowAppUpload.setMoney(moneyStr);
        withDrowAppUpload.setAlipaycode(!TextUtils.isEmpty(accountReceipt.getAlipaycode())?accountReceipt.getAlipaycode():"");
        withDrowAppUpload.setWxcode(!TextUtils.isEmpty(accountReceipt.getWxcode())?accountReceipt.getWxcode():"");
        withDrowAppUpload.setBankname(!TextUtils.isEmpty(accountReceipt.getBankname())?accountReceipt.getBankname():"");
        withDrowAppUpload.setAccountno(!TextUtils.isEmpty(accountReceipt.getAccountno())?accountReceipt.getAccountno():"");
        mModel.withDrowApply(withDrowAppUpload)
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
                            userIndex();
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
                        }else{
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
                        }
                    }
                });

    }


    @Inject
    public MyWalletPresenter(MyWalletContract.Model model, MyWalletContract.View rootView) {
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
