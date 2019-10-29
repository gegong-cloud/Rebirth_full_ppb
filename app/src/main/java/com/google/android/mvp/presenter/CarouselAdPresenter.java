package com.google.android.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.google.android.app.utils.RxUtils;
import com.google.android.mvp.contract.CarouselAdContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.upload.HomeAdvUpload;


@ActivityScope
public class CarouselAdPresenter extends BasePresenter<CarouselAdContract.Model, CarouselAdContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    /**
     *  广告点击
     */
    public void logAd(String adId){
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setAd_id(adId);
        mModel.logAd(homeAdvUpload)
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
                    }
                });
    }

    @Inject
    public CarouselAdPresenter(CarouselAdContract.Model model, CarouselAdContract.View rootView) {
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
