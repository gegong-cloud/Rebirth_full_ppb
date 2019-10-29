package com.google.android.mvp.presenter;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.android.R;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.CloudContract;
import com.google.android.mvp.model.back_entity.CloudSection;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.LabelEntity;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import com.google.android.mvp.ui.activity.CloudListActivity;
import com.google.android.mvp.ui.activity.MinePlayActivity;
import com.google.android.mvp.ui.adapter.CloudCenterAdapter;
import com.google.android.mvp.ui.adapter.CloudTitleAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@FragmentScope
public class CloudPresenter extends BasePresenter<CloudContract.Model, CloudContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    Activity activity;

    /**
     * 顶部标签
     */
    LabelEntity topSelect;
    int topPositon = 0;
    CloudTitleAdapter cloudTitleAdapter;

    List<CloudSection> listData;

    /**
     * 列表数据
     */
    CloudCenterAdapter cloudCenterAdapter;

    public void labelTop(RefreshLayout refreshLayout, Activity activity,boolean showDialog) {
        this.activity = activity;
        mModel.labelTop()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<LabelEntity>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<LabelEntity>> commonEntity) {
                        if(commonEntity!=null&&commonEntity.getData()!=null&&commonEntity.getData().size()>0){
                            showTopAdapter(commonEntity.getData(),refreshLayout,showDialog);
                        }else{
                            loadComplete(refreshLayout);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        loadComplete(refreshLayout);
                    }
                });
    }

    void showTopAdapter(List<LabelEntity> data,RefreshLayout refreshLayout,boolean showDialog){
        if(topPositon!=0&&topPositon<data.size()){
            data.get(topPositon).setSelect(true);
            topSelect = data.get(topPositon);
        }else{
            data.get(0).setSelect(true);
            topSelect = data.get(0);
            topPositon = 0;
        }

        labelList(1,refreshLayout,showDialog);
        cloudTitleAdapter = new CloudTitleAdapter(data,activity);
        mRootView.showAdapter(cloudTitleAdapter);
        cloudTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(topPositon==position){
                    return;
                }
                data.get(topPositon).setSelect(false);
                topSelect = data.get(position);
                topPositon = position;
                data.get(position).setSelect(true);
                labelList(1,refreshLayout,true);
                cloudTitleAdapter.notifyDataSetChanged();
            }
        });
    }



    /**
     * 云播次级标签
     */
    public void labelList(int page,RefreshLayout refreshLayout,boolean showDialog) {
        if(!(topSelect!=null&&!TextUtils.isEmpty(topSelect.getId()))){
            loadComplete(refreshLayout);
            return;
        }
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setCls_id(topSelect.getId());
        homeAdvUpload.setLimit(""+20);
        homeAdvUpload.setPage(""+page);
        mModel.labelList(homeAdvUpload)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if(showDialog){
                            mRootView.showLoading();
                        }
                    }
                })//显示进度条
                .map(new Function<CommonEntity<List<LabelEntity>>, CommonEntity<List<CloudSection>>>() {
                    @Override
                    public CommonEntity<List<CloudSection>> apply(CommonEntity<List<LabelEntity>> listCommonEntity) throws Exception {
                        List<CloudSection> cloudSectionList = null;
                        if(listCommonEntity!=null&&listCommonEntity.getData()!=null&&listCommonEntity.getData().size()>0){
                            cloudSectionList = new ArrayList<>();
                            for(LabelEntity labelEntity:listCommonEntity.getData()){
                                CloudSection cloudSection = new CloudSection(true,labelEntity.getName(),labelEntity.getId());
                                cloudSectionList.add(cloudSection);
                                if(labelEntity.getList()!=null&&labelEntity.getList().size()>0){
                                    for (LabelEntity.LabelList labelList:labelEntity.getList()){
                                        CloudSection cloudSection1 = new CloudSection(labelList);
                                        cloudSectionList.add(cloudSection1);
                                    }
                                }
                            }
                        }
                        CommonEntity commonEntity = new CommonEntity();
                        commonEntity.setCode(listCommonEntity.getCode());
                        commonEntity.setMsg(listCommonEntity.getMsg());
                        commonEntity.setData(cloudSectionList);
                        return commonEntity;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(showDialog) {
                            mRootView.hideLoading();
                        }
                    }
                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<CloudSection>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<CloudSection>> commonEntity) {
                        loadComplete(refreshLayout);
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if (commonEntity.getData() != null && commonEntity.getData().size() > 0) {
//                                allPage = !TextUtils.isEmpty(commonEntity.getMsg())?(StringUtils.getPageAll(Integer.parseInt(commonEntity.getMsg()))):0;
                                showAdapter(commonEntity.getData());
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
    private void showAdapter(List<CloudSection> list) {
        if (list != null && list.size() > 0) {
            listData = list;
            if (cloudCenterAdapter == null) {
                cloudCenterAdapter = new CloudCenterAdapter(list, activity);
                mRootView.showAdapter(cloudCenterAdapter);
                cloudCenterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if(list.get(position).isHeader){
                            mRootView.launchActivity(new Intent(mApplication, CloudListActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("cloudTags",list.get(position).getId())
                            );
                        }else{
                            mRootView.launchActivity(new Intent(mApplication, MinePlayActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("movId",list.get(position).t.getId())
                            );
                        }
                    }
                });

                cloudCenterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
            cloudCenterAdapter = null;
            mRootView.showAdapter(cloudCenterAdapter);
        }
    }


    private void initInfo(List<CloudSection> data) {
        if (loadMore) {
            loadMore = false;
            cloudCenterAdapter.addData(data);
        } else {
            if (cloudCenterAdapter != null) {
                cloudCenterAdapter.replaceData(data);
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
            labelList(page, refreshLayout,true);
        } else {
            loadMore = false;
            refreshLayout.finishLoadmore();
            if (cloudCenterAdapter != null) {
                cloudCenterAdapter.loadMoreEnd();
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
    public void favor(CloudSection homeVipMovList, int position) {
        LabelEntity.LabelList labelList = homeVipMovList.t;
        if("1".equals(labelList.getHas_favor())){
            userFavoDel(labelList.getId(),position);
        }else{
            userFavor(labelList.getId(),position);
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
        if(cloudCenterAdapter!=null&&listData!=null&&listData.size()>0&&position<listData.size()){
            listData.get(position).t.setHas_favor("1".equals(listData.get(position).t.getHas_favor())?"0":"1");
            if(cloudCenterAdapter.getData().size()>position){
                cloudCenterAdapter.notifyItemChanged(position,listData.get(position));
            }
        }
    }





//    /**
//     * 组合数据返回
//     */
//   private  void zipInfo() {
//
//        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
//        homeAdvUpload.setCode("yunbo");
//        Observable.zip(mModel.labelList(), mModel.getAdvList(homeAdvUpload), new BiFunction<CommonEntity<List<LabelEntity>>, CommonEntity<List<HomeAdv>>, CommonEntity<List<LabelEntity>>>() {
//            @Override
//            public CommonEntity<List<LabelEntity>> apply(CommonEntity<List<LabelEntity>> listCommonEntity, CommonEntity<List<HomeAdv>> listCommonEntity2) throws Exception {
//                if (listCommonEntity != null &&listCommonEntity.getData()!=null&&listCommonEntity.getData().size()>0 && listCommonEntity2 != null&&listCommonEntity2.getData()!=null) {
//                    LabelEntity labelEntity = new LabelEntity();
//                    labelEntity.setHomeAdvs(listCommonEntity2.getData());
//                    listCommonEntity.getData().add(1, labelEntity);
//                }
//                return listCommonEntity;
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
//                    }
//                })//显示进度条
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doAfterTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        mRootView.hideLoading();
//                    }
//                })
//                .compose(RxUtils.bindToLifecycle(mRootView))
//                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<LabelEntity>>>(mErrorHandler) {
//                    @Override
//                    public void onNext(@NonNull CommonEntity<List<LabelEntity>> commonEntity) {
//                        if (Api.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
//                            showAdapter(commonEntity.getData());
//                        }
//                    }
//                });
//    }
//
//
//    /**
//     * @param list 显示影视
//     */
//    private void showAdapter(List<LabelEntity> list) {
//        if (list != null && list.size() > 0) {
//            if (cloudTitleAdapter == null) {
//                cloudTitleAdapter = new CloudTitleAdapter(list, mAppManager.getCurrentActivity());
//                mRootView.showAdapter(cloudTitleAdapter);
//            } else {//更新数据
//                if (cloudTitleAdapter != null) {
//                    cloudTitleAdapter.replaceData(list);
//                }
//            }
//        } else {
//            cloudTitleAdapter = null;
//            mRootView.showAdapter(cloudTitleAdapter);
//        }
//    }




    @Inject
    public CloudPresenter(CloudContract.Model model, CloudContract.View rootView) {
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
