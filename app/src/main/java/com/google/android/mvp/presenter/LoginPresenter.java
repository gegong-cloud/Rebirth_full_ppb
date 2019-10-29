package com.google.android.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.base.App;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.MyApplication;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.LoginContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.LoginEntity;
import com.google.android.mvp.model.back_entity.OpenInstallEntity;
import com.google.android.mvp.model.upload.LoginUpload;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import timber.log.Timber;


@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;


    /**
     * 登录
     */
    public void login(String phoneStr,String pwdStr) {
        if(!(!TextUtils.isEmpty(phoneStr)&& StringUtils.checkPhoneNum(phoneStr))){
            ArmsUtils.makeText(mApplication,"请输入正确的手机号");
            return;
        }
        if(TextUtils.isEmpty(pwdStr)){
            ArmsUtils.makeText(mApplication,"请输入密码");
            return;
        }
        LoginUpload loginUpload = new LoginUpload();
        loginUpload.setUsername(phoneStr);
        loginUpload.setPassword(pwdStr);
        loginUpload.setPhonetype("1");
        if(!TextUtils.isEmpty(HbCodeUtils.getDeviceId())){
            String dataStr = StringUtils.replaceXieGang(HbCodeUtils.getDeviceId());
            OpenInstallEntity.UserIdEntity openInstallEntity = ((App) MyApplication.getsInstance()).getAppComponent().gson().fromJson(dataStr, OpenInstallEntity.UserIdEntity.class);
            if(openInstallEntity!=null&&!TextUtils.isEmpty(openInstallEntity.getUserId())){
                loginUpload.setUserId(openInstallEntity.getUserId());
                Timber.e("lhw--userId="+openInstallEntity.getUserId());
            }else{
                loginUpload.setUserId(HbCodeUtils.getDeviceId());
                Timber.e("lhw1--userId="+HbCodeUtils.getDeviceId());
            }
        }else{
            loginUpload.setUserId("");
        }
        mModel.login(loginUpload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<LoginEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<LoginEntity> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            HbCodeUtils.setToken(commonEntity.getData().getToken());
                            HbCodeUtils.setUserType(commonEntity.getData().getUsertype());
                            mRootView.killMyself();
                        }else{
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
                        }
                    }
                });

    }


    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
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
