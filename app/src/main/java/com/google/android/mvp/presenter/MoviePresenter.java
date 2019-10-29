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
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import javax.inject.Inject;

import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.MovieContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.HomeVipMov;
import com.google.android.mvp.model.back_entity.LabelEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import com.google.android.mvp.ui.activity.CloudListActivity;
import com.google.android.mvp.ui.activity.LiveListActivity;
import com.google.android.mvp.ui.activity.LivePlayActivity;
import com.google.android.mvp.ui.activity.MinePlayActivity;
import com.google.android.mvp.ui.adapter.CloudTitleAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@FragmentScope
public class MoviePresenter extends BasePresenter<MovieContract.Model, MovieContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    /**
     * 悬浮广告链接
     */
    HomeAdv homeAdvFloat;


    /**
     * 首页top数据
     */
    CloudTitleAdapter cloudTitleAdapter;
    /**
     * 初始化首页top数据
     */
    LabelEntity topSelect;
    int topPositon = 0;


    /**
     * @param labelEntities 显示top数据
     */
    public void loadTop(List<LabelEntity> labelEntities, RefreshLayout refreshLayout) {
        topSelect = labelEntities.get(0);
        topPositon = 0;
        initInfo(1, refreshLayout);
        cloudTitleAdapter = new CloudTitleAdapter(labelEntities, mAppManager.getCurrentActivity());
        mRootView.showAdapterTop(cloudTitleAdapter);
        cloudTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (topPositon == position) {
                    return;
                }
                labelEntities.get(topPositon).setSelect(false);
                topSelect = labelEntities.get(position);
                topPositon = position;
                labelEntities.get(position).setSelect(true);
                mRootView.showCenterAdapter(true);
                homeIndex(1, refreshLayout, true);
                cloudTitleAdapter.notifyDataSetChanged();
            }
        });
    }


    public void initInfo(int page, RefreshLayout refreshLayout) {
        getAdvListSlides();//banner
        getAdvListFloat();//浮动广告
        getConfig();//首页动态公告
        homeIndex(page, refreshLayout, true);//recyclerview 数据
    }

    /**
     * banner
     */
    private void getAdvListSlides() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setCode("slides");
        mModel.getAdvList(homeAdvUpload)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<HomeAdv>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<HomeAdv>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            mRootView.setBannerInfo(commonEntity.getData());
                        }
                    }
                });

    }

    /**
     * 获取钱包首页的的温馨提示
     */
    private void getConfig() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setName("topAlert");
        mModel.getConfig(homeAdvUpload)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<ScrollMsg>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<ScrollMsg>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            mRootView.showDeng(commonEntity.getData());
                        }
                    }
                });

    }


    /**
     * 悬浮广告 firstFlout
     */
    private void getAdvListFloat() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setCode("firstFlout");
        mModel.getAdvList(homeAdvUpload)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<HomeAdv>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<HomeAdv>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if (commonEntity.getData().size() > 0) {
                                homeAdvFloat = commonEntity.getData().get(0);
                                mRootView.showFloat(homeAdvFloat);
                            }
                        }
                    }
                });

    }

    /**
     * vip影视
     */
    public void homeIndex(int page, RefreshLayout refreshLayout, boolean showDialog) {
        if (!(topSelect != null && !TextUtils.isEmpty(topSelect.getId()))) {
            loadComplete(refreshLayout);
            return;
        }
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setLimit("" + 20);
        homeAdvUpload.setPage("" + page);
        homeAdvUpload.setType("" + topSelect.getId());
        mModel.homeIndex(homeAdvUpload)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (showDialog) {
                            mRootView.showLoading();
                        }
                    }
                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (showDialog) {
                            mRootView.hideLoading();
                        }
                    }
                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<HomeVipMov.HomeVipMovList>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<HomeVipMov.HomeVipMovList>> commonEntity) {
                        loadComplete(refreshLayout);
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if (commonEntity.getData() != null && commonEntity.getData().size() > 0) {
//                                allPage = !TextUtils.isEmpty(commonEntity.getMsg()) ? (StringUtils.getPageAll(Integer.parseInt(commonEntity.getMsg()))) : 0;
                                allPage++;
                                showAdapter(commonEntity.getData());
                            } else {
                                allPage = 0;
                            }

                        } else {
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
    private void showAdapter(List<HomeVipMov.HomeVipMovList> list) {
        if (list != null && list.size() > 0) {
            initInfo(list);
        } else {
            mRootView.showAdapter(null,false);
        }
    }


   public void castItemClick(HomeVipMov.HomeVipMovList homeVipMov) {
        if (!TextUtils.isEmpty(homeVipMov.getLink()) && homeVipMov.getLink().startsWith("zhibo")) {
            String[] yunboIds = homeVipMov.getLink().split("zhibo_id=");//主播
            String[] clsIds = homeVipMov.getLink().split("pingtai_id=");//平台
            if (yunboIds != null && yunboIds.length >= 2) {
                mRootView.launchActivity(new Intent(mApplication, LivePlayActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("liveId", yunboIds[1])
                );
            } else if (clsIds != null && clsIds.length >= 2) {
                mRootView.launchActivity(new Intent(mApplication, LiveListActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("name", clsIds[1])
                );
            }
        } else if (!TextUtils.isEmpty(homeVipMov.getLink()) && homeVipMov.getLink().startsWith("yunbo")) {
            String[] yunboIds = homeVipMov.getLink().split("yunbo_id=");
            String[] clsIds = homeVipMov.getLink().split("cls_id=");
            if (yunboIds != null && yunboIds.length >= 2) {
                mRootView.launchActivity(new Intent(mApplication, MinePlayActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("movId", yunboIds[1])
                );
            } else if (clsIds != null && clsIds.length >= 2) {
                mRootView.launchActivity(new Intent(mApplication, CloudListActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("cloudTags", clsIds[1])

                );
            }
        }
    }

    private void initInfo(List<HomeVipMov.HomeVipMovList> data) {
        mRootView.showAdapter(data,loadMore);
        if (loadMore) {
            loadMore = false;
        }
        mRootView.showCenterAdapter(true);
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
            homeIndex(page, refreshLayout, true);
        } else {
            loadMore = false;
            refreshLayout.finishLoadmore();
            if (mRootView.getHomeAdapter() != null) {
                mRootView.getHomeAdapter().loadMoreEnd();
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

    /**
     * 收藏关系
     * @param homeVipMovList
     */
    public void favor(HomeVipMov.HomeVipMovList homeVipMovList,int position) {
        if("1".equals(homeVipMovList.getHas_favor())){
            userFavoDel(homeVipMovList.getId(),position);
        }else{
            userFavor(homeVipMovList.getId(),position);
        }
    }

    /**
     * 收藏
     */
    public void userFavor(String movId,int position){
        UserDeleteUpload tokenEntity = new UserDeleteUpload();
        tokenEntity.setYunbo_id(movId);
        mModel.userfavor(tokenEntity)
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
                            ArmsUtils.makeText(mApplication, !TextUtils.isEmpty(commonEntity.getMsg())?commonEntity.getMsg():"收藏成功!");
                            mRootView.updateFavoUI(position);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }

    /**
     * 删除
     */
    public void userFavoDel(String movId,int position){
        String[] movs_id = new String[1];
        movs_id[0] = movId;
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
                            ArmsUtils.makeText(mApplication, !TextUtils.isEmpty(commonEntity.getMsg())?commonEntity.getMsg():"已取消收藏!");
                            mRootView.updateFavoUI(position);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }


    @Inject
    public MoviePresenter(MovieContract.Model model, MovieContract.View rootView) {
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
