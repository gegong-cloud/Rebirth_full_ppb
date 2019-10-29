package com.google.android.mvp.presenter;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.VipQrcodeContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.model.back_entity.PaySuccessResult;
import com.google.android.mvp.model.upload.TokenEntity;
import com.google.android.mvp.ui.widget.popwind.VipPromptPopup;

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
import top.zibin.luban.Luban;


@ActivityScope
public class VipQrcodePresenter extends BasePresenter<VipQrcodeContract.Model, VipQrcodeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    VipPromptPopup vipPromptPopup;
    MoneyShow showQrcode;

    public void queryResult(MoneyShow chargeid) {
        showQrcode = chargeid;
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChargeid(showQrcode.getOrderNumber());
        mModel.rechargeQuery(tokenEntity)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<PaySuccessResult>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<PaySuccessResult> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())) {
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
                            hide();
                        }else{
                            vipPromptPopup =  new VipPromptPopup(mAppManager.getCurrentActivity(), showQrcode);
                            vipPromptPopup.showPopupWindow();
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
                        }
                    }
                });

    }


    /**
     *
     * @param chargeid 解锁
     */
    public void unlockMoney(MoneyShow chargeid) {
        showQrcode = chargeid;
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChargeid(showQrcode.getOrderNumber());
        mModel.unlockMoney(tokenEntity)
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
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())) {
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
                            mRootView.killMyself();
                        }else{
                            ArmsUtils.makeText(mApplication,commonEntity.getMsg());
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
        mModel.getImgFile(StringUtils.getBaseUrl() +fileName)
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


    void hide(){
        if(vipPromptPopup!=null&&vipPromptPopup.isShowing()){
            vipPromptPopup.dismiss();
        }
        mRootView.complete();
    }


    @Inject
    public VipQrcodePresenter(VipQrcodeContract.Model model, VipQrcodeContract.View rootView) {
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
