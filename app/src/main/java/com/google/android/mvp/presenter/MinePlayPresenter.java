package com.google.android.mvp.presenter;

import android.app.Activity;
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

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import com.google.android.app.EventBusTags;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.MinePlayContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import com.google.android.mvp.ui.activity.MinePlayActivity;
import com.google.android.mvp.ui.activity.VipRechargeActivity;
import com.google.android.mvp.ui.adapter.HistoryAdapter;
import com.google.android.mvp.ui.adapter.PlayAdvAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class MinePlayPresenter extends BasePresenter<MinePlayContract.Model, MinePlayContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    PlayAdvAdapter playAdvAdapter;
    YunboMov yunboMov;//影视数据

    HistoryAdapter historyAdapter;

    /**
     * 广告
     */
    public void yunboView(String movId) {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setYunbo_id(movId);
        mModel.yunboView(homeAdvUpload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<YunboMov>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<YunboMov> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            yunboMov = commonEntity.getData();
                            mRootView.initVedio(commonEntity.getData());
                        }
                    }
                });

    }

    /**
     * 广告
     */
    public void getAdvList() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setCode("video");
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
                            showAdapter(commonEntity.getData());

                        }
                    }
                });

    }

    /**
     * @param list 显示影视
     */
    private void showAdapter(List<HomeAdv> list) {
        if (list != null && list.size() > 0) {
            if (playAdvAdapter == null) {
                playAdvAdapter = new PlayAdvAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(playAdvAdapter);
                playAdvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (list != null && position < list.size() && list.get(position) != null && !TextUtils.isEmpty(list.get(position).getLink())) {
                            EventBus.getDefault().post("", EventBusTags.CLICK_AD);
                            HbCodeUtils.openBrowser(mAppManager.getCurrentActivity(), list.get(position).getLink());
                        }
                    }
                });

            } else {//更新数据
                if (playAdvAdapter != null) {
                    playAdvAdapter.replaceData(list);
                }
            }
        } else {
            playAdvAdapter = null;
            mRootView.showAdapter(playAdvAdapter);
        }
    }


    /**
     * @param minePlayActivity 跳转充值
     */
    public void castRecharge(Activity minePlayActivity) {
        String typeStr = "";
        if (yunboMov != null && !TextUtils.isEmpty(yunboMov.getType())) {
            typeStr = yunboMov.getType();
        }
        mRootView.launchActivity(new Intent(minePlayActivity, VipRechargeActivity.class)
                .putExtra("typeStr", typeStr)
        );
    }


    /**
     * 猜你喜欢
     */
    public void movSimilar(String movieEntity) {
        UserDeleteUpload tokenEntity = new UserDeleteUpload();
        tokenEntity.setYunbo_id(movieEntity);
        mModel.movSimilar(tokenEntity)
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
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            showAdapterInfo(commonEntity.getData().getList());
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }

    /**
     * @param list 显示item
     */
    private void showAdapterInfo(List<YunboMov> list) {
        if (list != null && list.size() > 0) {
            if (historyAdapter == null) {
                historyAdapter = new HistoryAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(historyAdapter);
                historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        castVipVideo(list.get(position));
                    }
                });
            } else {//更新数据
                if (historyAdapter != null) {
                    historyAdapter.replaceData(list);
                }
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



    /**
     * 收藏关系
     */
    public void favor() {
        if(yunboMov!=null&&!TextUtils.isEmpty(yunboMov.getId())){
            if("1".equals(yunboMov.getHas_favor())){
                userFavoDel(yunboMov.getId());
            }else{
                userFavor(yunboMov.getId());
            }
        }
    }

    /**
     * 收藏
     */
    public void userFavor(String movId){
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
                            yunboMov.setHas_favor("1");//已经收藏
                            mRootView.updateFavoUI(true);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }

    /**
     * 删除
     */
    public void userFavoDel(String movId){
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
                            yunboMov.setHas_favor("0");//取消收藏
                            mRootView.updateFavoUI(false);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }



    @Inject
    public MinePlayPresenter(MinePlayContract.Model model, MinePlayContract.View rootView) {
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
