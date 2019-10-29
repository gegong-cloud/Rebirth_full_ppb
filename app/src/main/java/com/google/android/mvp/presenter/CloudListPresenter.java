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
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import com.google.android.R;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.CloudListContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import com.google.android.mvp.ui.activity.MinePlayActivity;
import com.google.android.mvp.ui.adapter.CloudListAdapter;
import com.google.android.mvp.ui.widget.popwind.MsgPopup;

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
public class CloudListPresenter extends BasePresenter<CloudListContract.Model, CloudListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    List<YunboMov> listData;

    CloudListAdapter cloudListAdapter;
    String tagStr = "";



    /**
     * top标签
     */
    public void yunboIndex(int page,RefreshLayout refreshLayout,String clsList) {
        tagStr = clsList;
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setPage(""+page);
        homeAdvUpload.setLimit(""+ TextConstant.PAGE_SIZE);
        homeAdvUpload.setCls_id(clsList);
        mModel.yunboIndex(homeAdvUpload)
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
                            if (commonEntity.getData() != null&&commonEntity.getData().getList()!=null && commonEntity.getData().getList().size() > 0) {
                                mRootView.showTitle(commonEntity.getData());
//                                allPage = !TextUtils.isEmpty(commonEntity.getMsg())?(StringUtils.getPageAll(Integer.parseInt(commonEntity.getMsg()))):0;
                                showAdapter(commonEntity.getData().getList());
                                allPage++;
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
    private void showAdapter(List<YunboMov> list) {
        if (list != null && list.size() > 0) {
            listData = list;
            if (cloudListAdapter == null) {
                cloudListAdapter = new CloudListAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(cloudListAdapter);
                cloudListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        castVipVideo(list.get(position));
                    }
                });

                cloudListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        if(R.id.my_favor==view.getId()){
                            favor(list.get(position),position);
                        }
                    }
                });

            } else {//更新数据
                initInfo(list);
            }
        } else {
            cloudListAdapter = null;
            mRootView.showAdapter(cloudListAdapter);
        }
    }


    private void checkUser(YunboMov liveInfo) {
        mModel.checkUser()
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
                            castVipVideo(liveInfo);
                        } else {//没有会员，或者会员已过期
                            showPopWindow(commonEntity);
                        }
                    }

                });

    }

    private  void showPopWindow(CommonEntity<JionLiveEntity> msgStr){
        new MsgPopup(mAppManager.getCurrentActivity(),msgStr).showPopupWindow();
    }

    void castVipVideo(YunboMov yunboMov){
//        Bundle bundle = new Bundle();bundle.putSerializable("yunboMov",yunboMov);
        mRootView.launchActivity(new Intent(mApplication, MinePlayActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("movId",yunboMov.getId())
//                .putExtras(bundle)
        );
    }




    private void initInfo(List<YunboMov> data) {
        if (loadMore) {
            loadMore = false;
            cloudListAdapter.addData(data);
        } else {
            if (cloudListAdapter != null) {
                cloudListAdapter.replaceData(data);
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
            yunboIndex(page, refreshLayout,tagStr);
        } else {
            loadMore = false;
            refreshLayout.finishLoadmore();
            if (cloudListAdapter != null) {
                cloudListAdapter.loadMoreEnd();
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
    public void favor(YunboMov homeVipMovList, int position) {
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
                            updateFavoUI(position);
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
                            updateFavoUI(position);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }

    public void updateFavoUI(int position) {
        if(cloudListAdapter!=null&&listData!=null&&listData.size()>0&&position<listData.size()){
            listData.get(position).setHas_favor("1".equals(listData.get(position).getHas_favor())?"0":"1");
            if(cloudListAdapter.getData().size()>position){
                cloudListAdapter.notifyItemChanged(position,listData.get(position));
            }
        }
    }

    @Inject
    public CloudListPresenter(CloudListContract.Model model, CloudListContract.View rootView) {
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
