package com.google.android.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.app.utils.sv.DeviceInformationHelper;
import com.google.android.mvp.contract.LiveListContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.model.back_entity.LiveInfo;
import com.google.android.mvp.model.upload.LiveUpload;
import com.google.android.mvp.ui.activity.LivePlayActivity;
import com.google.android.mvp.ui.adapter.LiveRoomListAdapter;
import com.google.android.mvp.ui.widget.popwind.MsgPopup;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;

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
public class LiveListPresenter extends BasePresenter<LiveListContract.Model, LiveListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    LiveRoomListAdapter livePlatformAdapter;


    /**
     * 登录
     */
    public void liveAnchors(String name, RefreshLayout refreshLayout) {
        LiveUpload loginUpload = new LiveUpload();
        loginUpload.setName(name);
        mModel.liveAnchors(loginUpload)
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
                        loadComplete(refreshLayout);
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            mRootView.showTilte(commonEntity.getData());
                            showLivev(commonEntity.getData().getLists());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        loadComplete(refreshLayout);
                    }
                });

    }


    /**
     * @param list 显示直播平台列表
     */
    private void showLivev(ArrayList<LiveInfo> list) {
        if (list != null && list.size() > 0) {
            if (livePlatformAdapter == null) {
                livePlatformAdapter = new LiveRoomListAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(livePlatformAdapter);
                livePlatformAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        if (TextUtils.isEmpty(HbCodeUtils.getToken())) {
//                            mRootView.launchActivity(new Intent(mApplication, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                            return;
//                        }
                        castLivingRoom(list.get(position));
//                        checkUser(list.get(position));
                    }
                });

            } else {//更新数据
                if (livePlatformAdapter != null) {
                    livePlatformAdapter.replaceData(list);
                }
            }
        } else {
            livePlatformAdapter = null;
            mRootView.showAdapter(livePlatformAdapter);
        }
    }


    private void checkUser(LiveInfo liveInfo) {
        mModel.checkUserSpecial(!TextUtils.isEmpty(HbCodeUtils.getAndroidDevice())?HbCodeUtils.getAndroidDevice(): DeviceInformationHelper.imei2(mAppManager.getCurrentActivity()))
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<JionLiveEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<JionLiveEntity> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
//                            castLivingRoom(liveInfo, commonEntity.getData());
                        } else {//没有会员，或者会员已过期
                            showPopWindow(commonEntity);
                        }
                    }

                });

    }


    private  void showPopWindow(CommonEntity<JionLiveEntity> msgStr){
        new MsgPopup(mAppManager.getCurrentActivity(),msgStr).showPopupWindow();
    }

    private void castLivingRoom(LiveInfo liveInfo) {
        mRootView.launchActivity(new Intent(mApplication, LivePlayActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("liveId", liveInfo.getId())
        );
    }


    @Inject
    public LiveListPresenter(LiveListContract.Model model, LiveListContract.View rootView) {
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
