package com.google.android.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.utils.JumpUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.InviteFriendContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.UserInvite;

import java.io.File;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;

import static com.google.android.app.utils.StringUtils.getBitmapFromInputStream;
import static com.google.android.app.utils.StringUtils.getSavePath;
import static com.google.android.app.utils.StringUtils.getStrHashCode;


@ActivityScope
public class InviteFriendPresenter extends BasePresenter<InviteFriendContract.Model, InviteFriendContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    UserInvite userInvite;

    /**
     * 邀请
     */
    public void userInvite() {
        mModel.userInvite()
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<UserInvite>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<UserInvite> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            userInvite = commonEntity.getData();
                            if(!TextUtils.isEmpty(userInvite.getErweimaUrl())){
                                if(StringUtils.isFlag(userInvite.getErweimaUrl())){
                                    mRootView.showQrImg(userInvite.getErweimaUrl());
                                }else{
                                    downImageToLocation(userInvite.getErweimaUrl());
                                }
                            }
                            mRootView.showInfo(commonEntity.getData());
                        }
                    }
                });

    }


    /**
     * @param fileName
     * @return 下载图片保存到本地
     */
    public void downImageToLocation(String fileName) {
        if(TextUtils.isEmpty(fileName)){
            ArmsUtils.makeText(mApplication,"二维码加载失败,请返回重试!");
            return ;
        }
        String path = getSavePath(mAppManager.getCurrentActivity(), getStrHashCode(fileName));
        File file = new File(path);
        if(file.exists()){
            mRootView.showQrImg(file.getAbsolutePath());
            return;
        }
        String imgUrl = StringUtils.getBaseUrl() +fileName;
        mModel.getImgFile(imgUrl)
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
                        InputStream is = responseBody.byteStream();
                        if(responseBody==null||is==null){
                            return "";
                        }
                        String tempPath = StringUtils.tempBitmap(mAppManager.getCurrentActivity(), getBitmapFromInputStream(is),imgUrl,getStrHashCode(fileName));
                        return tempPath;
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
                        if (!TextUtils.isEmpty(filePath) ) {
                            mRootView.showQrImg(filePath);
                        }else{
                            ArmsUtils.makeText(mApplication,"二维码加载失败,请返回重试!");
                        }
                    }
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        ArmsUtils.makeText(mApplication,"二维码加载失败,请返回重试!");
                    }
                });
    }


    public void copyLink(Activity activity) {
        if(userInvite!=null&&!TextUtils.isEmpty(userInvite.getInvateInfo())){
            JumpUtils.copyUrlToBord(activity,userInvite.getInvateInfo());
        }
    }


    public void shareWx(){
        Intent wechatIntent = new Intent(Intent.ACTION_SEND);
        wechatIntent.setPackage("com.tencent.mm");
        wechatIntent.setType("text/plain");
        wechatIntent.putExtra(Intent.EXTRA_TEXT, userInvite.getInvateInfo());
        mRootView.launchActivity(wechatIntent);
    }

    @Inject
    public InviteFriendPresenter(InviteFriendContract.Model model, InviteFriendContract.View rootView) {
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
