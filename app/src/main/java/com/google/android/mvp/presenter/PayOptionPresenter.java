package com.google.android.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.PayOptionContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.model.back_entity.PayMoneyResult;
import com.google.android.mvp.model.upload.TokenEntity;
import com.google.android.mvp.ui.activity.PayCodeShowActivity;
import com.google.android.mvp.ui.adapter.MoneyShowAdapter;

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
public class PayOptionPresenter extends BasePresenter<PayOptionContract.Model, PayOptionContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    MoneyShowAdapter moneyShowAdapter;


    /**
     *  列表显示
     */
    public void chargeIndex(){
        TokenEntity tokenEntity = new TokenEntity();
        mModel.chargeIndex(tokenEntity)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<MoneyShow>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<MoneyShow>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())&&commonEntity.getData()!=null) {
                            showAdapterInfo(commonEntity.getData());
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }





    /**
     *  预支付
     */
    public void recharge(String moneyStr){
        if(TextUtils.isEmpty(moneyStr)){
            ArmsUtils.makeText(mApplication,"请选择金额");
            return;
        }
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setCode("wx");
        tokenEntity.setMoney(moneyStr);
        mModel.recharge(tokenEntity)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<PayMoneyResult>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<PayMoneyResult> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())&&commonEntity.getData()!=null) {
                            Bundle bundle = new Bundle();bundle.putSerializable("payCode",commonEntity.getData());
                            mRootView.launchActivity(new Intent(mApplication,PayCodeShowActivity.class)
                                    .putExtras(bundle)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            );
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }



    /**
     * @param list 显示item
     */
    private void showAdapterInfo(List<MoneyShow> list) {
        if (list != null && list.size() > 0) {
            if (moneyShowAdapter == null) {
                moneyShowAdapter = new MoneyShowAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(moneyShowAdapter);
            } else {//更新数据
                if (moneyShowAdapter != null) {
                    moneyShowAdapter.replaceData(list);
                }
            }
        } else {
            moneyShowAdapter = null;
            mRootView.showAdapter(moneyShowAdapter);
        }
    }

    @Inject
    public PayOptionPresenter(PayOptionContract.Model model, PayOptionContract.View rootView) {
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
