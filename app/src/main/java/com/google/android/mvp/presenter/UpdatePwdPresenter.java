package com.google.android.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.UpdatePwdContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.upload.LoginUpload;
import com.google.android.mvp.ui.activity.LoginActivity;

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
public class UpdatePwdPresenter extends BasePresenter<UpdatePwdContract.Model, UpdatePwdContract.View> {
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
    public void register(String phone, String verify_code, String passwd) {
        if (!(!TextUtils.isEmpty(phone) && StringUtils.checkPhoneNum(phone))) {
            ArmsUtils.makeText(mApplication, "请输入密码");
            return;
        }
        if (!(!TextUtils.isEmpty(verify_code) && verify_code.length() >= 4)) {
            ArmsUtils.makeText(mApplication, "请输入新密码");
            return;
        }
        if (!(!TextUtils.isEmpty(passwd) && passwd.length() >= 4)) {
            ArmsUtils.makeText(mApplication, "请确认密码");
            return;
        }
        if (!verify_code.equals(passwd)) {
            ArmsUtils.makeText(mApplication, "两次密码不一致");
            return;
        }
        LoginUpload tokenEntity = new LoginUpload();
        tokenEntity.setPwd(phone);
        tokenEntity.setNewpwd(verify_code);
        tokenEntity.setConfirmpwd(passwd);
        mModel.editPwd(tokenEntity)
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
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) ) {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                            HbCodeUtils.setToken("");
                            mRootView.launchActivity(new Intent(mApplication, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            mRootView.killMyself();

                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }



    @Inject
    public UpdatePwdPresenter(UpdatePwdContract.Model model, UpdatePwdContract.View rootView) {
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
