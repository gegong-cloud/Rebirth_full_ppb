package com.google.android.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import javax.inject.Inject;

import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.TyHistoryContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.model.upload.UserViewUpload;
import com.google.android.mvp.ui.activity.MinePlayActivity;
import com.google.android.mvp.ui.adapter.HistoryAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@FragmentScope
public class TyHistoryPresenter extends BasePresenter<TyHistoryContract.Model, TyHistoryContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    HistoryAdapter historyAdapter;

    //待删除
    List<YunboMov> tempDelList;

    /**
     * 显示的数据
     */
    List<YunboMov> list;

    int type = 1;


    /**
     * 我的喜欢的数据
     */
    public void userView(int page, RefreshLayout refreshLayout) {
        UserViewUpload tokenEntity = new UserViewUpload();
        tokenEntity.setPage("" + page);
        tokenEntity.setLimit(""+TextConstant.PAGE_SIZE);
        tokenEntity.setType(""+type);
        mModel.userView(tokenEntity)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<YunboList>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<YunboList> commonEntity) {
                        loadComplete(refreshLayout);
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if (commonEntity.getData().getList() != null && commonEntity.getData().getList().size() > 0) {
                                allPage = page + 1;
                                showAdapterInfo(commonEntity.getData().getList());
                            } else {
                                loadMore = false;//加载更多没有数据的时候需要把加载更多重置
                                allPage = page;
                            }
                        } else {
                            loadMore = false;//加载更多没有数据的时候需要把加载更多重置
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }

    /**
     * 删除
     */
    public void userViewDel(SmartRefreshLayout smartRefresh) {
        mModel.userViewDel()
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
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            ArmsUtils.makeText(mApplication, !TextUtils.isEmpty(commonEntity.getMsg())?commonEntity.getMsg():"删除成功!");
                            showAdapterInfo(null);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }







    /**
     * @param listdata 显示item
     */
    private void showAdapterInfo(List<YunboMov> listdata) {
        list = listdata;
        if (list != null && list.size() > 0) {
            if (historyAdapter == null) {
                historyAdapter = new HistoryAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(historyAdapter);
                historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        castVipVideo(listdata.get(position));
                    }
                });
            } else {//更新数据
                initInfo(list);
            }
        } else {
            historyAdapter = null;
            mRootView.showAdapter(historyAdapter);
        }
    }

    void castVipVideo(YunboMov yunboMov){
        mRootView.launchActivity(new Intent(mApplication, MinePlayActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("movId",yunboMov.getId())
        );
    }

    private void initInfo(List<YunboMov> data) {
        if (loadMore) {
            loadMore = false;
            historyAdapter.addData(data);
        } else {
            if (historyAdapter != null) {
                historyAdapter.replaceData(data);
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
            userView(page, refreshLayout);
        } else {
            loadMore = false;
            refreshLayout.finishLoadmore();
            if (historyAdapter != null) {
                historyAdapter.loadMoreEnd();
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


    public void setType(int type) {
        this.type = type;
    }


    @Inject
    public TyHistoryPresenter(TyHistoryContract.Model model, TyHistoryContract.View rootView) {
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
