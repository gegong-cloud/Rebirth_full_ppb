package com.google.android.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.base.App;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.EventBusTags;
import com.google.android.app.MyApplication;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.RegContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.LoginEntity;
import com.google.android.mvp.model.back_entity.OpenInstallEntity;
import com.google.android.mvp.model.upload.LoginUpload;

import org.simple.eventbus.EventBus;

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
public class RegPresenter extends BasePresenter<RegContract.Model, RegContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;


    /**
     * 注册
     */
    public void register(String phone, String verify_code, String passwd, String invitCode) {
        if (!(!TextUtils.isEmpty(phone) && StringUtils.checkPhoneNum(phone))) {
            ArmsUtils.makeText(mApplication, "请输入正确的手机号");
            return;
        }
        if (!(!TextUtils.isEmpty(verify_code) && verify_code.length() >= 4)) {
            ArmsUtils.makeText(mApplication, "请输入验证码");
            return;
        }
        if (!(!TextUtils.isEmpty(passwd) && passwd.length() >= 4)) {
            ArmsUtils.makeText(mApplication, "请输入密码");
            return;
        }
        LoginUpload tokenEntity = new LoginUpload();
        tokenEntity.setUsername(phone);
        tokenEntity.setCode(verify_code);
        tokenEntity.setPassword(passwd);
        tokenEntity.setReferrer(invitCode);
        tokenEntity.setPhonetype("1");
        if (!TextUtils.isEmpty(HbCodeUtils.getDeviceId())) {
            String dataStr = StringUtils.replaceXieGang(HbCodeUtils.getDeviceId());
            OpenInstallEntity.UserIdEntity openInstallEntity = ((App) MyApplication.getsInstance()).getAppComponent().gson().fromJson(dataStr, OpenInstallEntity.UserIdEntity.class);
            if (openInstallEntity != null && !TextUtils.isEmpty(openInstallEntity.getUserId())) {
                tokenEntity.setUserId(openInstallEntity.getUserId());
                Timber.e("lhw--userId=" + openInstallEntity.getUserId());
            } else {
                tokenEntity.setUserId(HbCodeUtils.getDeviceId());
                Timber.e("lhw1--userId=" + HbCodeUtils.getDeviceId());
            }
        } else {
            tokenEntity.setUserId("");
        }
        mModel.register(tokenEntity)
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
                            EventBus.getDefault().post("", EventBusTags.REG_SUCCESS);
                            HbCodeUtils.setToken(commonEntity.getData().getToken());
                            HbCodeUtils.setUserType(commonEntity.getData().getUsertype());
                            ArmsUtils.makeText(mApplication, "注册成功");
                            mRootView.killMyself();
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }


    /**
     * 发送验证码
     */
    public void sendSms(String phone) {
        if (!(!TextUtils.isEmpty(phone) && StringUtils.checkPhoneNum(phone))) {
            ArmsUtils.makeText(mApplication, "请输入正确的手机号");
            return;
        }
        LoginUpload tokenEntity = new LoginUpload();
        tokenEntity.setMobile(phone);
        tokenEntity.setEvent("register");
        mRootView.setCodeBg(true);
        mModel.smsSend(tokenEntity)
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
                            ArmsUtils.makeText(mApplication, "验证码已发送，请注意查收!");
                        } else {
                            mRootView.setCodeBg(false);
                            StringUtils.makeTextCenter(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }

    @Inject
    public RegPresenter(RegContract.Model model, RegContract.View rootView) {
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
