package com.google.android.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.google.android.app.MyApplication;
import com.google.android.app.utils.AssetsViewHelper;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.JumpUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.StarUpContract;
import com.google.android.mvp.model.back_entity.BaseUrl;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.OpenInstallEntity;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.LoginUpload;
import com.google.android.mvp.ui.activity.CarouselAdActivity;
import com.google.android.mvp.ui.activity.MainActivity;
import com.jess.arms.base.App;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import okhttp3.ResponseBody;
import timber.log.Timber;
import top.zibin.luban.Luban;

import static com.google.android.app.utils.RxUtils.bindToLifecycle;


@ActivityScope
public class StarUpPresenter extends BasePresenter<StarUpContract.Model, StarUpContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    HomeAdv adList;

    /**
     * 申请权限
     * @param context
     * @param bindData
     */
    public void requestPermissions(Activity context, String bindData) {
        PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {

                                             @Override
                                             public void onRequestPermissionSuccess() {
                                                 readUserId(context,bindData);
                                             }

                                             @Override
                                             public void onRequestPermissionFailure(List<String> permissions) {
                                                 StringUtils.makeTextCenter(mApplication, "权限申请失败，无法访问APP");
                                             }

                                             @Override
                                             public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                                             }
                                         }, mRootView.getRxPermissions(), mErrorHandler,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
//                , Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.CAMERA
        );
    }


    /**
     * 用户信息
     */
    public void userInfo(String userTempId) {
        LoginUpload loginUpload = new LoginUpload();
        if (!TextUtils.isEmpty(HbCodeUtils.getDeviceId())) {
            String tempStr = HbCodeUtils.getDeviceId();
            if (tempStr.contains("userId")) {
                String dataStr = StringUtils.replaceXieGang(tempStr);
                OpenInstallEntity.UserIdEntity openInstallEntity = ((App) MyApplication.getsInstance()).getAppComponent().gson().fromJson(dataStr, OpenInstallEntity.UserIdEntity.class);
                if (openInstallEntity != null && !TextUtils.isEmpty(openInstallEntity.getUserId())) {
                    loginUpload.setSup_user_id(openInstallEntity.getUserId());
                    Timber.e("lhw--userId=" + openInstallEntity.getUserId());
                } else {
                    loginUpload.setSup_user_id(tempStr);
                    Timber.e("lhw1--userId=" + tempStr);
                }
            } else {
                loginUpload.setSup_user_id(tempStr);
                Timber.e("lhw1--userId=" + tempStr);
            }

        } else {
            //传递过来的值 不为空为剪切板的值
            loginUpload.setSup_user_id(!TextUtils.isEmpty(userTempId) ? userTempId : "");
        }
        mModel.userInfo(loginUpload)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<UserEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<UserEntity> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            HbCodeUtils.setUserInfo(commonEntity.getData());
                            HbCodeUtils.setToken(commonEntity.getData().getToken());
                            //查看权限，如果没有则申请权限
                            ad_img();
                        } else {
                            StringUtils.makeTextCenter(mApplication, commonEntity.getMsg());

                        }
                    }
                });

    }


    /**
     * 获取广告图片地址
     */
    public void ad_img() {
        HomeAdvUpload homeAdvUpload = new HomeAdvUpload();
        homeAdvUpload.setCode("start");
        mModel.getAdvList(homeAdvUpload)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
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
                            if (commonEntity.getData().size() > 0) {
                                adList = commonEntity.getData().get(0);
                            }
                            isDownImage();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        isDownImage();
                    }
                });

    }


    /**
     * 请求新的域名地址
     */
    public void getUrl() {
        mModel.getUrl()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<BaseUrl>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<BaseUrl>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if (commonEntity.getData().size() > 0) {
                                if (TextConstant.APP_SPARE.equals(HbCodeUtils.getNetUrl())) {//现在切换的域名为备用域名，建议不用
                                    // 可在 App 运行时,随时切换 BaseUrl (指定了 Domain-Name header 的接口)//
                                    MyApplication.getsInstance().setImgUrl(HbCodeUtils.checkHttp(commonEntity.getData().get(0).getUrl()));
                                    RetrofitUrlManager.getInstance().putDomain("douban", MyApplication.getsInstance().getImgUrl());
//                                    HbCodeUtils.setNetUrl(HbCodeUtils.checkHttp(commonEntity.getData().get(0).getUrl()));
                                }
                                HbCodeUtils.setNewUrl(commonEntity.getData());
                                ad_img();
                            }
                        }
                    }
                });

    }


    /**
     * 下载
     */
    private void isDownImage() {
        if (adList != null && !TextUtils.isEmpty(adList.getImage())) {
            castCarouselAd();
//            if(HbCodeUtils.getAdvEntity()!=null&&!TextUtils.isEmpty(HbCodeUtils.getAdvEntity().getImage())&&HbCodeUtils.getAdvEntity().getImage().equals(adList.getImage())){
//                adList = HbCodeUtils.getAdvEntity();
//                castCarouselAd();
//            }else{
//                downImageToLocation(adList.getImage());
//            }
        } else {
            castCarouselAd();
        }

    }


    /**
     * @param fileName
     * @return 下载图片保存到本地
     */
    private String downImageToLocation(String fileName) {
        mModel.getImgFile(fileName)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })//显示进度条
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        byte[] bys = new byte[24];
                        bys = responseBody.bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                        String tempPath = StringUtils.saveBitmap(mAppManager.getCurrentActivity(), bitmap);
                        return Luban.with(mApplication).load(tempPath).get().get(0).getPath();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull String filePath) {
                        if (!TextUtils.isEmpty(filePath)) {
                            adList.setLocation_url(filePath);
                            HbCodeUtils.setAdvEntity(adList);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        castCarouselAd();
                    }
                });
        return null;
    }


    /**
     * 跳转到广告页
     */
    private void castCarouselAd() {
        if (adList != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("adList", adList);
            mRootView.launchActivity(new Intent(mApplication, CarouselAdActivity.class)
                    .putExtras(bundle)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            mRootView.launchActivity(new Intent(mApplication, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        mRootView.killMyself();
    }

    @Inject
    public StarUpPresenter(StarUpContract.Model model, StarUpContract.View rootView) {
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

    public void readPermiss(Activity context, String bindData) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(context,bindData);
        } else {
            readUserId(context,bindData);
        }
    }

    void readUserId(Activity context, String bindData){
        Observable.just(bindData)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })//显示进度条
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String bindData) throws Exception {
                        if (!TextUtils.isEmpty(HbCodeUtils.getDeviceId())) {//判断 是否存贮在xml 是否有值
                            return HbCodeUtils.getDeviceId();
                        } else {//
//                            String tempId = AssetsViewHelper.width(context).getTempValue();//读取xml文件
                            String tempId = AssetsViewHelper.width(context).getChannel(context);//读取xml文件
                            String userId = TextUtils.isEmpty(tempId) ? bindData : tempId;//
                            Timber.e("lhw_channel=" + tempId);
                            Timber.e("lhw_openinstall=" + bindData);
                            HbCodeUtils.setDeviceId(!TextUtils.isEmpty(userId) ? userId : "");//存xml代理商值 或者 openinstall 值
                            return userId;//如果userId 为空，取剪切板的值
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull String userId) {
                        if (!TextUtils.isEmpty(userId)) {
                            userInfo(userId);
                        } else {
                            userInfo(JumpUtils.getClipboardText(context));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        userInfo("");
                    }
                });
    }
}
