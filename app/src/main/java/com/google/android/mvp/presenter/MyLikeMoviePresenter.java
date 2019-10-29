package com.google.android.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.MyLikeMovieContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.model.upload.UserDeleteUpload;
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


@ActivityScope
public class MyLikeMoviePresenter extends BasePresenter<MyLikeMovieContract.Model, MyLikeMovieContract.View> {
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


    /**
     * 我的喜欢的数据
     */
    public void userFavo(int page,RefreshLayout refreshLayout){
        UserViewUpload tokenEntity = new UserViewUpload();
        tokenEntity.setPage(""+page);
        tokenEntity.setLimit(""+TextConstant.PAGE_SIZE);
        mModel.userFavo(tokenEntity)
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
     * @param smartRefresh
     */
    public void userFavoDel(SmartRefreshLayout smartRefresh){
        if(!(tempDelList!=null&&tempDelList.size()>0)){
            return;
        }
        String[] movs_id = new String[tempDelList.size()];
        for(int i=0;i<tempDelList.size();i++){
            movs_id[i] = tempDelList.get(i).getId();
        }
        UserDeleteUpload tokenEntity = new UserDeleteUpload();
        tokenEntity.setYunbo_ids(movs_id);
        mModel.userFavoDel(tokenEntity)
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
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())&&commonEntity.getData()!=null) {
                            tempDelList.clear();
                            ArmsUtils.makeText(mApplication, !TextUtils.isEmpty(commonEntity.getMsg())?commonEntity.getMsg():"删除成功!");
                            userFavo(1,smartRefresh);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }




    /**
     * @param event 显示是否可编辑
     */
    public void showEdit(boolean event) {
        if(!event){//这里要全部取消
            addShowItemAll(!event);
        }
        mRootView.editShow(event);
        if (historyAdapter != null) {
            historyAdapter.setShowCheck(event);
        }

    }


    public void addShowItem(int position) {
        if (tempDelList == null) {
            tempDelList = new ArrayList<>();
        }
        if (list != null && list.size() > 0) {
            YunboMov movieEntity = list.get(position);
            boolean isChoose = false;
            if (movieEntity.isEditFlag()) {
                isChoose = false;
                movieEntity.setEditFlag(false);
            } else {
                isChoose = true;
                movieEntity.setEditFlag(true);
            }
            if (isChoose) {//选中操作
                if (tempDelList.contains(movieEntity)) {
                } else {
                    tempDelList.add(movieEntity);
                }
            } else {//取消选中
                if (tempDelList.contains(movieEntity)) {
                    tempDelList.remove(movieEntity);
                }
            }
            list.remove(position);
            list.add(position, movieEntity);
            if (historyAdapter != null) {
                historyAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * @param isAdd 全选或者全部取消
     */
    public void addShowItemAll(boolean isAdd) {
        if (tempDelList == null) {
            tempDelList = new ArrayList<>();
        }
        if (list != null && list.size() > 0) {
            tempDelList.clear();//全部取消
            for (YunboMov movieEntity : list) {
                movieEntity.setEditFlag(isAdd);
                if (isAdd) {//全选
                    tempDelList.add(movieEntity);
                }
            }
            if (historyAdapter != null) {
                historyAdapter.notifyDataSetChanged();
            }
        }

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
                        if (historyAdapter.getShowCheck()) {//编辑状态
                            addShowItem(position);
                        } else {//跳转
                            castVipVideo(listdata.get(position));
                        }

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
            userFavo(page, refreshLayout);
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

    void castVipVideo(YunboMov yunboMov){
        mRootView.launchActivity(new Intent(mApplication, MinePlayActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("movId",yunboMov.getId())
        );
    }


    @Inject
    public MyLikeMoviePresenter(MyLikeMovieContract.Model model, MyLikeMovieContract.View rootView) {
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
