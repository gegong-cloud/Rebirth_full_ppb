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
import com.google.android.mvp.contract.MoneyDetailContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.MoneyTakeDetail;
import com.google.android.mvp.ui.adapter.MoneyDetailAdapter;

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
public class MoneyDetailPresenter extends BasePresenter<MoneyDetailContract.Model, MoneyDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    MoneyDetailAdapter moneyDetailAdapter;


    public void initInfo(RefreshLayout refreshLayout) {
        accountLog();
        loadComplete(refreshLayout);
    }


    /**
     * vip影视
     */
    private void accountLog() {
        mModel.accountLog()
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<MoneyTakeDetail>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<MoneyTakeDetail>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            showAdapter(commonEntity.getData());
                        }
                    }
                });

    }

    /**
     * @param list 显示影视
     */
    private void showAdapter(List<MoneyTakeDetail> list) {
        if (list != null && list.size() > 0) {
            if (moneyDetailAdapter == null) {
                moneyDetailAdapter = new MoneyDetailAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(moneyDetailAdapter);
                moneyDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    }
                });

            } else {//更新数据
                if (moneyDetailAdapter != null) {
                    moneyDetailAdapter.replaceData(list);
                }
            }
        } else {
            moneyDetailAdapter = null;
            mRootView.showAdapter(moneyDetailAdapter);
        }
    }


    @Inject
    public MoneyDetailPresenter(MoneyDetailContract.Model model, MoneyDetailContract.View rootView) {
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
