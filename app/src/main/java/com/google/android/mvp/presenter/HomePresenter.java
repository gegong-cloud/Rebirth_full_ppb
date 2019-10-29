package com.google.android.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.HomeContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.HomeVipMov;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.ui.activity.LiveListActivity;
import com.google.android.mvp.ui.activity.VipVideoActivity;
import com.google.android.mvp.ui.widget.popwind.MsgPopup;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@FragmentScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
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



    public void initInfo(RefreshLayout refreshLayout) {
//        vipCate();
//        getAdvListBottom();
//        getAdvListSlides();//banner
//        getAdvListFloat();//浮动广告
//        getConfig();//首页动态公告
        zipVipCateAndBottomAdv();//recyclerview 数据
        loadComplete(refreshLayout);
    }


    private void zipVipCateAndBottomAdv(){
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setCode("firstBottomBanner");
        Observable.zip(mModel.vipCate(), mModel.getAdvList(homeAdvUpload), new BiFunction<CommonEntity<List<HomeVipMov>>, CommonEntity<List<HomeAdv>>, CommonEntity<List<HomeVipMov>>>() {
            @Override
            public CommonEntity<List<HomeVipMov>> apply(CommonEntity<List<HomeVipMov>> listCommonEntity, CommonEntity<List<HomeAdv>> listCommonEntity2) throws Exception {
                if(listCommonEntity!=null&&listCommonEntity.getData()!=null&&listCommonEntity.getData().size()>0&&listCommonEntity2!=null&&listCommonEntity2.getData()!=null){
                    HomeVipMov homeVipMov = new HomeVipMov();
                    homeVipMov.setBottomAdv(listCommonEntity2.getData());
                    listCommonEntity.getData().add(1,homeVipMov);

                }
                return listCommonEntity;
            }
        })
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
//                    }
//                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .doAfterTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        mRootView.hideLoading();
//                    }
//                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<HomeVipMov>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<HomeVipMov>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            showAdapter(commonEntity.getData());
                        }
                    }
                });
    }



    /**
     * @param list 显示影视
     */
    private void showAdapter(List<HomeVipMov> list) {
        mRootView.showAdapter(list);
    }





    /**
     * banner
     */
    private void getAdvListSlides() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setCode("slides");
        mModel.getAdvList(homeAdvUpload)
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
//                    }
//                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .doAfterTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        mRootView.hideLoading();
//                    }
//                })
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
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
//                    }
//                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .doAfterTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        mRootView.hideLoading();
//                    }
//                })
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
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
//                    }
//                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .doAfterTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        mRootView.hideLoading();
//                    }
//                })
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

    public void floatClick(Activity activity){
        if(homeAdvFloat!=null&&!TextUtils.isEmpty(homeAdvFloat.getLink())){
            HbCodeUtils.openBrowser(activity,homeAdvFloat.getLink());
        }
    }


    public void checkUser(HomeVipMov.HomeVipMovList liveInfo) {
//        mModel.checkUser()
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
//                .subscribe(new ErrorHandleSubscriber<CommonEntity<JionLiveEntity>>(mErrorHandler) {
//                    @Override
//                    public void onNext(@NonNull CommonEntity<JionLiveEntity> commonEntity) {
//                        if (Api.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
//                            castLivingRoom(liveInfo);
//                        } else {//没有会员，或者会员已过期
//                            showPopWindow(commonEntity);
//                        }
//                    }
//
//                });

        castLivingRoom(liveInfo);
    }


    private  void showPopWindow(CommonEntity<JionLiveEntity> msgStr){
        new MsgPopup(mAppManager.getCurrentActivity(),msgStr).showPopupWindow();
    }

    private void castLivingRoom(HomeVipMov.HomeVipMovList liveInfo) {
        mRootView.launchActivity(new Intent(mApplication, VipVideoActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("baseUrl", liveInfo.getLink())
                .putExtra("vip_cate_id", liveInfo.getId())
        );
    }


    /**
     * 获取直播列表
     */
    public void liveIndex() {
        mModel.liveIndex()
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
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if(commonEntity.getData().getLists()!=null&&commonEntity.getData().getLists().size()>0){
                                Random random = new Random();
                                int position = random.nextInt(commonEntity.getData().getLists().size());
                                position = position<commonEntity.getData().getLists().size()?position:0;
                                mRootView.launchActivity(new Intent(mApplication, LiveListActivity.class)
                                        .putExtra("title", commonEntity.getData().getLists().get(position).getTitle())
                                        .putExtra("name", commonEntity.getData().getLists().get(position).getName())
                                        .putExtra("img", commonEntity.getData().getLists().get(position).getImg())
                                        .putExtra("number", commonEntity.getData().getLists().get(position).getNumber())
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                );
                            }
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }


    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView) {
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
