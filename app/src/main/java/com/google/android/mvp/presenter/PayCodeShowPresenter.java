package com.google.android.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.PayCodeShowContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.PaySuccessResult;
import com.google.android.mvp.model.upload.TokenEntity;

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
public class PayCodeShowPresenter extends BasePresenter<PayCodeShowContract.Model, PayCodeShowContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;



    /**
     * 支付结果查询
     * @param chargeid
     */
    public void rechargeQuery(String chargeid){
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChargeid(chargeid);
        mModel.rechargeQuery(tokenEntity)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<PaySuccessResult>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<PaySuccessResult> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())&&commonEntity.getData()!=null) {
//                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                            String resultStr ="金额="+ commonEntity.getData().getBalance()+"日期="+commonEntity.getData().getDays();
                            mRootView.showPayResult(resultStr);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }


    @Inject
    public PayCodeShowPresenter(PayCodeShowContract.Model model, PayCodeShowContract.View rootView) {
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
