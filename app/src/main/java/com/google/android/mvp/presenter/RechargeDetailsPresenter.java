package com.google.android.mvp.presenter;

import android.app.Application;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.RechargeDetailsContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.RechargeDetails;
import com.google.android.mvp.model.upload.AccountUpload;
import com.google.android.mvp.ui.adapter.RechargeDetailsAdapter;

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
public class RechargeDetailsPresenter extends BasePresenter<RechargeDetailsContract.Model, RechargeDetailsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    RechargeDetailsAdapter rechargeDetailsAdapter;

    /**
     * 充值记录
     */
    public void chargeLog(int page, RefreshLayout refreshLayout) {
        AccountUpload accountUpload = new AccountUpload();
        accountUpload.setLimit(""+20);
        accountUpload.setPage(""+page);
        mModel.chargeLog(accountUpload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<RechargeDetails>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<RechargeDetails>> commonEntity) {
                        loadComplete(refreshLayout);
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            String titleStr = "您最近的充值记录共"+commonEntity.getMsg()+"笔";
                            mRootView.showTitle(titleStr);
                            if (commonEntity.getData() != null && commonEntity.getData().size() > 0) {
//                                allPage = !TextUtils.isEmpty(commonEntity.getMsg())?(StringUtils.getPageAll(Integer.parseInt(commonEntity.getMsg()))):0;
                                allPage++;
                                showAdapter(commonEntity.getData());
                            }else{
                                allPage = 0;
                            }

                        }else{
                            loadMore = false;//加载更多没有数据的时候需要把加载更多重置
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        loadComplete(refreshLayout);
                        loadMore = false;//加载更多没有数据的时候需要把加载更多重置
                    }
                });

    }

    /**
     * @param list 显示影视
     */
    private void showAdapter(List<RechargeDetails> list) {
        if (list != null && list.size() > 0) {



            if (rechargeDetailsAdapter == null) {
                rechargeDetailsAdapter = new RechargeDetailsAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(rechargeDetailsAdapter);
                rechargeDetailsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    }
                });

            } else {//更新数据
                initInfo(list);
            }
        } else {
            rechargeDetailsAdapter = null;
            mRootView.showAdapter(rechargeDetailsAdapter);
        }
    }


    private void initInfo(List<RechargeDetails> data) {
        if (loadMore) {
            loadMore = false;
            rechargeDetailsAdapter.addData(data);
        } else {
            if (rechargeDetailsAdapter != null) {
                rechargeDetailsAdapter.replaceData(data);
            }
        }
    }


    int page = 1;//分页
    int allPage = 1;//总的页数
    boolean loadMore = false;//是否加载更多


    /**
     * @param refreshLayout 加载更多
     */
    public void loadMore(RefreshLayout refreshLayout) {
        if (page < allPage) {
            loadMore = true;
            page++;
            chargeLog(page, refreshLayout);
        } else {
            loadMore = false;
            refreshLayout.finishLoadmore();
            if (rechargeDetailsAdapter != null) {
                rechargeDetailsAdapter.loadMoreEnd();
            }
        }
    }

    private void loadComplete(RefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            if (loadMore) {//是不是加载更多
                refreshLayout.finishLoadmore();
            } else {
                page = 1;//如果是刷新，页码归一
                refreshLayout.finishRefresh();
            }
        }
    }



    @Inject
    public RechargeDetailsPresenter(RechargeDetailsContract.Model model, RechargeDetailsContract.View rootView) {
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
