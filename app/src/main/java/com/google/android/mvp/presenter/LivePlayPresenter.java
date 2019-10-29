package com.google.android.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.LivePlayContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.LiveInfo;
import com.google.android.mvp.model.upload.LiveUpload;
import com.google.android.mvp.ui.activity.VipRechargeActivity;

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
public class LivePlayPresenter extends BasePresenter<LivePlayContract.Model, LivePlayContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    LiveInfo yunboMov;

    public void loadData(String liveId) {
        LiveUpload homeAdvUpload = new LiveUpload();
        homeAdvUpload.setZhibo_id(liveId);
        mModel.liveView(homeAdvUpload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<LiveInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<LiveInfo> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            yunboMov = commonEntity.getData();
                            mRootView.initAnchorLive(commonEntity.getData());
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }


    /**
     *
     * @param minePlayActivity 跳转充值
     */
    public void castRecharge(Activity minePlayActivity) {
        String typeStr = "";
        if(yunboMov!=null&&!TextUtils.isEmpty(yunboMov.getType())){
            typeStr = yunboMov.getType();
        }
        mRootView.launchActivity(new Intent(minePlayActivity, VipRechargeActivity.class)
                .putExtra("typeStr",typeStr)
        );
    }

    @Inject
    public LivePlayPresenter(LivePlayContract.Model model, LivePlayContract.View rootView) {
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
