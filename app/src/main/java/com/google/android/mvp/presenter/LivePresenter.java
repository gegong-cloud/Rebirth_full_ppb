package com.google.android.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.LiveContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.model.back_entity.LiveInfo;
import com.google.android.mvp.model.upload.HomeAdvUpload;

import java.util.ArrayList;
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


@FragmentScope
public class LivePresenter extends BasePresenter<LiveContract.Model, LiveContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    /**
     * banner广告
     */
    List<HomeAdv> adHomeEntity;


    String countTitle = "";




    public void initInfo(RefreshLayout refreshLayout) {
        bannerList();
        liveIndex();
        loadComplete(refreshLayout);
    }


    /**
     * banner广告
     */
    private void bannerList() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setCode("zhibo");
        mModel.getAdvList(homeAdvUpload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<HomeAdv>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<HomeAdv>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            adHomeEntity = commonEntity.getData();
                            mRootView.setBannerInfo(commonEntity.getData());
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }

    private void liveIndex() {
        mModel.liveIndex()
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<LiveEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<LiveEntity> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            countTitle = "共" + (!TextUtils.isEmpty(commonEntity.getData().getCount()) ? commonEntity.getData().getCount() : "0") + "个";
                            mRootView.setCountTitle(countTitle);
                            showLivev(commonEntity.getData().getLists());
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }


    /**
     * @param list 显示直播平台列表
     */
    private void showLivev(ArrayList<LiveInfo> list) {
        if (list != null && list.size() > 0) {
            mRootView.showAdapter(list);
        }else{
            mRootView.showAdapter(null);
        }
    }








    @Inject
    public LivePresenter(LiveContract.Model model, LiveContract.View rootView) {
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

    private void loadComplete(RefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
    }
}
