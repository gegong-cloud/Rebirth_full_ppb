package com.google.android.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.MainContract;
import com.google.android.mvp.model.back_entity.BaseUrl;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.back_entity.VersionEntity;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.VersionUpload;
import com.google.android.mvp.ui.widget.popwind.HomePopup;
import com.google.android.mvp.ui.widget.popwind.VersionUpdatePopup;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;


    /**
     *
     * @param activity 检查更新
     */
    public void checkUpdate(Activity activity){
        VersionUpload upload = new VersionUpload();
        upload.setVersion(StringUtils.getLocalVersionName(activity));
        upload.setType("1");
        mModel.checkUpdate(upload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<VersionEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<VersionEntity> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            checkVersionPoup(activity,commonEntity.getData());
                        }
                    }
                });
    }

    void checkVersionPoup(Activity activity, VersionEntity data){
        if("1".equals(data.getIs_update())&&!TextUtils.isEmpty(data.getDownload_url())){
            new VersionUpdatePopup(activity,data.getDownload_url()).showPopupWindow();
        }
    }



    /**
     * 获取广告图片地址
     */
    public void getUrl() {
        mModel.getUrl()
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<BaseUrl>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<BaseUrl>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if(commonEntity.getData().size()>0){
                                HbCodeUtils.setNewUrl(commonEntity.getData());
                                // 可在 App 运行时,随时切换 BaseUrl (指定了 Domain-Name header 的接口)//
                                RetrofitUrlManager.getInstance().putDomain("douban", commonEntity.getData().get(0).getUrl());
                                HbCodeUtils.setNetUrl(commonEntity.getData().get(0).getUrl());
                            }
                        }
                    }
                });

    }


    /**
     * 获取广告图片地址
     */
    public void getServiceUrl() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setName("serviceUrl");
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
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null&&commonEntity.getData().size()>0) {
                            HbCodeUtils.setServiceAdd(commonEntity.getData().get(0));
                        }
                    }
                });

    }


    /**
     *  播放错误日志
     */
    public void logError(String yunboId){
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setYunbo_id(yunboId);
        mModel.logError(homeAdvUpload)
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


    /**
     *
     * @param activity 首页公告
     */
    public void homeConfig(Activity activity){
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setName("topAlert");
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
                            new HomePopup(activity,"",commonEntity.getData().get(0).toString()).showPopupWindow();
                        }
                    }
                });
    }



    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
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
