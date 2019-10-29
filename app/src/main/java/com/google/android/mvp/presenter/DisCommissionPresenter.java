package com.google.android.mvp.presenter;

import android.app.Application;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.DisCommissionContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.DisCommission;
import com.google.android.mvp.ui.adapter.DisCommissionAdapter;

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
public class DisCommissionPresenter extends BasePresenter<DisCommissionContract.Model, DisCommissionContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    DisCommissionAdapter disCommissionAdapter;

    public void initInfo(RefreshLayout refreshLayout) {
        bonusIndex();
        loadComplete(refreshLayout);
    }

    /**
     * vip影视
     */
    private void bonusIndex() {
        mModel.bonusIndex()
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<DisCommission>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<DisCommission> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            mRootView.showTitle(commonEntity.getData());
                            if(commonEntity.getData().getList()!=null){
                                showAdapter(commonEntity.getData().getList());
                            }

                        }
                    }
                });

    }

    /**
     * @param list 显示影视
     */
    private void showAdapter(List<DisCommission.DisList> list) {
        if (list != null && list.size() > 0) {
            if (disCommissionAdapter == null) {
                disCommissionAdapter = new DisCommissionAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(disCommissionAdapter);
                disCommissionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    }
                });

            } else {//更新数据
                if (disCommissionAdapter != null) {
                    disCommissionAdapter.replaceData(list);
                }
            }
        } else {
            disCommissionAdapter = null;
            mRootView.showAdapter(disCommissionAdapter);
        }
    }

    @Inject
    public DisCommissionPresenter(DisCommissionContract.Model model, DisCommissionContract.View rootView) {
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
