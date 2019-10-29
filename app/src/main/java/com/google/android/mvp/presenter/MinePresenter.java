package com.google.android.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.jess.arms.base.App;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.google.android.R;
import com.google.android.app.MyApplication;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.MineContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.OpenInstallEntity;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.upload.LoginUpload;
import com.google.android.mvp.ui.activity.DisCommissionActivity;
import com.google.android.mvp.ui.activity.InviteFriendActivity;
import com.google.android.mvp.ui.activity.LoginActivity;
import com.google.android.mvp.ui.activity.MoneyDetailActivity;
import com.google.android.mvp.ui.activity.MyApprenticeActivity;
import com.google.android.mvp.ui.activity.MyWalletActivity;
import com.google.android.mvp.ui.activity.RechargeDetailsActivity;
import com.google.android.mvp.ui.activity.SetActivity;
import com.google.android.mvp.ui.activity.UpdatePwdActivity;
import com.google.android.mvp.ui.activity.VipRechargeActivity;
import com.google.android.mvp.ui.activity.WebViewActivity;
import com.google.android.mvp.ui.widget.popwind.MsgPopup;

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


@FragmentScope
public class MinePresenter extends BasePresenter<MineContract.Model, MineContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    UserEntity userEntity;

    /**
     * 用户信息
     */
    public void userInfo() {
        LoginUpload loginUpload = new LoginUpload();
        if (!TextUtils.isEmpty(HbCodeUtils.getDeviceId())) {
            String tempStr = HbCodeUtils.getDeviceId();
            if(tempStr.contains("userId")){
                String dataStr = StringUtils.replaceXieGang(tempStr);
                OpenInstallEntity.UserIdEntity openInstallEntity = ((App) MyApplication.getsInstance()).getAppComponent().gson().fromJson(dataStr, OpenInstallEntity.UserIdEntity.class);
                if (openInstallEntity != null && !TextUtils.isEmpty(openInstallEntity.getUserId())) {
                    loginUpload.setSup_user_id(openInstallEntity.getUserId());
                    Timber.e("lhw--userId=" + openInstallEntity.getUserId());
                } else {
                    loginUpload.setSup_user_id(tempStr);
                    Timber.e("lhw1--userId=" + tempStr);
                }
            }else{
                loginUpload.setSup_user_id(tempStr);
                Timber.e("lhw1--userId=" + tempStr);
            }
        } else {
            loginUpload.setSup_user_id("");
//            loginUpload.setSup_user_id(!TextUtils.isEmpty(TextConstant.BUILT_IN_ANDROID) ? TextConstant.BUILT_IN_ANDROID : "");
        }
        mModel.userInfo(loginUpload)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<UserEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<UserEntity> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            userEntity = commonEntity.getData();
                            HbCodeUtils.setUserInfo(userEntity);
                            mRootView.bindTip(userEntity);
                            mRootView.loadUserInfo(commonEntity.getData());
                            if (!TextUtils.isEmpty(commonEntity.getData().getUsertype())) {
                                if ("1".equals(commonEntity.getData().getUsertype())) {
                                    mRootView.isAgent(false);
                                } else {
                                    mRootView.isAgent(true);
                                }
                            } else {
                                mRootView.isAgent(true);
                            }
                        }
                    }
                });

    }

    public void logout() {
        mModel.logout()
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
                            HbCodeUtils.setToken("");
                            mRootView.launchActivity(new Intent(mApplication, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                });

    }


    /**
     * 是否登录的检查
     */
    boolean isLogin = false;

    public void checkUser(int viewId, Activity activity) {
        checkUserResultCast(viewId, activity);
    }

    void checkUserResultCast(int viewId, Activity activity) {
        switch (viewId) {
            case R.id.recharge_details://充值明细
                mRootView.launchActivity(new Intent(activity, RechargeDetailsActivity.class));
                break;
            case R.id.mine_menu1://收支明细
                mRootView.launchActivity(new Intent(activity, MoneyDetailActivity.class));
                break;
            case R.id.mine_menu2://分销提成
                mRootView.launchActivity(new Intent(activity, DisCommissionActivity.class));
                break;
            case R.id.mine_recharge://vip充值
                mRootView.launchActivity(new Intent(activity, VipRechargeActivity.class));
                break;
            case R.id.mine_wallet://我的钱包
                mRootView.launchActivity(new Intent(activity, MyWalletActivity.class));
                break;
            case R.id.mine_team://我的徒弟
                mRootView.launchActivity(new Intent(activity, MyApprenticeActivity.class));
                break;
            case R.id.mine_invite://邀请好友
                mRootView.launchActivity(new Intent(activity, InviteFriendActivity.class));
                break;
            case R.id.mine_pwd://修改密码
                mRootView.launchActivity(new Intent(activity, UpdatePwdActivity.class));
                break;
            case R.id.mine_set:
                mRootView.launchActivity(new Intent(activity, SetActivity.class));
                break;
            case R.id.mine_accagent://加入代理
                String urlLike1 = userEntity==null?"":(userEntity.getMenulink()==null?"":(!TextUtils.isEmpty(userEntity.getMenulink().getDoagent_link())?userEntity.getMenulink().getDoagent_link():""));
                mRootView.launchActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra("titleStr", "加入代理")
                        .putExtra("url",urlLike1)
                );
                break;
            case R.id.mine_makemoney://如何赚钱
                String urlLike2 = userEntity==null?"":(userEntity.getMenulink()==null?"":(!TextUtils.isEmpty(userEntity.getMenulink().getHowmoney_link())?userEntity.getMenulink().getHowmoney_link():""));
                mRootView.launchActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra("titleStr", "如何赚钱")
                        .putExtra("url",urlLike2)
                );
                break;
        }
    }

    private void showPopWindow(CommonEntity<JionLiveEntity> msgStr, Activity activity) {
        if ("-997".equals(msgStr.getCode())) {//这里标识未登录
            isLogin = false;
            new MsgPopup(activity, msgStr).showPopupWindow();
        } else {//这里标识登录了，vip过期了
            isLogin = true;
            new MsgPopup(activity, msgStr).showPopupWindow();
        }
    }


    public UserEntity getUserEntity() {
        return userEntity;
    }

    @Inject
    public MinePresenter(MineContract.Model model, MineContract.View rootView) {
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
