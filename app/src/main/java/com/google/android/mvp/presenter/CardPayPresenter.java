package com.google.android.mvp.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import javax.inject.Inject;

import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.CardPayContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.upload.CardPayUpload;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.google.android.app.utils.RxUtils.bindToLifecycle;


@ActivityScope
public class CardPayPresenter extends BasePresenter<CardPayContract.Model, CardPayContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    public void cardPay(Activity activity, String string) {
        if (TextUtils.isEmpty(string)) {
            StringUtils.makeTextCenter(mApplication, "请输入或者粘贴卡密");
            return;
        }
        CardPayUpload cardPayUpload = new CardPayUpload();
        cardPayUpload.setCardno(string);
        mModel.cardPay(cardPayUpload)
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
                .compose(bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity commonEntity) {
                        new AlertDialog.Builder(activity)
                                .setTitle("充值提示")
                                .setMessage(TextUtils.isEmpty(commonEntity.getMsg())?(TextConstant.RequestSuccess.equals(commonEntity.getCode())?"充值成功":"充值失败"):commonEntity.getMsg())
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())) {
                                            mRootView.killMyself();
                                        }
                                    }
                                })
                                .create().show();
                    }
                });

    }

    @Inject
    public CardPayPresenter(CardPayContract.Model model, CardPayContract.View rootView) {
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
