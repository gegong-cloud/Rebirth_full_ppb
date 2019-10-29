package com.google.android.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.google.android.app.EventBusTags;
import com.google.android.app.MyApplication;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.VipRechargeContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HornEntity;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.model.back_entity.PayMoneyResult;
import com.google.android.mvp.model.back_entity.PayResult;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.back_entity.WeChatPay;
import com.google.android.mvp.model.upload.TokenEntity;
import com.google.android.mvp.ui.activity.CardPayActivity;
import com.google.android.mvp.ui.activity.OfflineRechargeActivity;
import com.google.android.mvp.ui.activity.VipQrcodeActivity;
import com.google.android.mvp.ui.activity.VipRechargeActivity;
import com.google.android.mvp.ui.adapter.RechargeMoneyAdapter;
import com.google.android.mvp.ui.adapter.VipRechargeTypeAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.google.android.app.utils.RxUtils.bindToLifecycle;


@ActivityScope
public class VipRechargePresenter extends BasePresenter<VipRechargeContract.Model, VipRechargeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;


    MoneyShow moneyChoose;
    RechargeMoneyAdapter rechargeMoneyAdapter;
    int moneyPostion = 0;


    List<MoneyShow> moneyCardList;

    /**
     * 自助支付
     */
    VipRechargeTypeAdapter vipRechargeTypeAdapter;
    List<MoneyShow> selfList;

    /**
     * 默认选中一个支付方式
     */
    int choosePositon = 0;
    List<MoneyShow> listData;
    MoneyShow payTypeShow;//支付方式


    /**
     * 用于显示支付结果
     */
    MoneyShow showPop;


    /**
     * 充值小喇叭
     */
    public void broadcast() {
        mModel.broadcast()
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<HornEntity>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<HornEntity>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null&&commonEntity.getData().size()>0) {
                            mRootView.showHorn(commonEntity.getData());
                        }else{

                        }
                    }
                });

    }

    /**
     * 1.1	支付方式列表
     */
    public void chargeInterfaceList() {
        mModel.chargeInterfaceList()
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<MoneyShow>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<MoneyShow>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            if(commonEntity.getData().size()>0){
                                for (MoneyShow moneyShow:commonEntity.getData()){
                                    if("2".equals(moneyShow.getPaytype())&&"wx".equals(moneyShow.getCode())&&!TextUtils.isEmpty(moneyShow.getImage())){
                                        mRootView.regWx(moneyShow.getImage());
                                        break;
                                    }
                                }
                            }
                            bindInfo(commonEntity.getData());
                        }
                    }
                });

    }

    void bindInfo(List<MoneyShow> data) {
        if (selfList == null) {
            selfList = new ArrayList<>();
        }
        selfList = data;
        showAdapter(selfList);

    }

    /**
     * @param list 支付方式
     */
    private void showAdapter(List<MoneyShow> list) {
        if (list != null && list.size() > 0) {
            listData = list;
            list.get(0).setChoose(true);
            choosePositon = 0;
            if (vipRechargeTypeAdapter == null) {
                vipRechargeTypeAdapter = new VipRechargeTypeAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(vipRechargeTypeAdapter);
                vipRechargeTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (choosePositon == position) {
                            return;
                        } else {
                            list.get(choosePositon).setChoose(false);
                            list.get(position).setChoose(true);
                            choosePositon = position;
                            vipRechargeTypeAdapter.notifyDataSetChanged();
                        }
                    }
                });

            } else {//更新数据
                if (vipRechargeTypeAdapter != null) {
                    vipRechargeTypeAdapter.replaceData(list);
                }
            }
        } else {
            vipRechargeTypeAdapter = null;
            mRootView.showAdapter(vipRechargeTypeAdapter);
        }
    }


    /**
     * 1.1	1.2	支付金额列表
     * @param typeStr
     */
    public void chargeIndex(String typeStr) {
        TokenEntity tokenEntity = new TokenEntity();
        if(!TextUtils.isEmpty(typeStr)){
            tokenEntity.setType(typeStr);
        }
        mModel.chargeIndex(tokenEntity)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<List<MoneyShow>>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<List<MoneyShow>> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            moneyCardList = commonEntity.getData();
                            mRootView.showTip(true);
                            showTagAdapter(commonEntity.getData());
                        }
                    }
                });

    }

    /**
     * @param list 显示卡金额
     */
    private void showTagAdapter(List<MoneyShow> list) {
        if (list != null && list.size() > 0) {
            moneyPostion = 0;
            list.get(0).setChoose(true);
            moneyChoose = list.get(0);
            if (rechargeMoneyAdapter == null) {
                rechargeMoneyAdapter = new RechargeMoneyAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(rechargeMoneyAdapter);
                rechargeMoneyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (moneyPostion == position) {
                            return;
                        } else {
                            list.get(moneyPostion).setChoose(false);
                            list.get(position).setChoose(true);
                            moneyPostion = position;
                            moneyChoose = list.get(position);
                            rechargeMoneyAdapter.notifyDataSetChanged();
                        }
                    }
                });
                rechargeMoneyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        if (moneyPostion == position) {
                        } else {
                            list.get(moneyPostion).setChoose(false);
                            list.get(position).setChoose(true);
                            moneyPostion = position;
                            moneyChoose = list.get(position);
                            rechargeMoneyAdapter.notifyDataSetChanged();
                        }
                        RxView.clicks(view)
                                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        // do something
                                            recharge();
                                    }
                                });

                    }
                });

            } else {//更新数据
                if (rechargeMoneyAdapter != null) {
                    rechargeMoneyAdapter.replaceData(list);
                }
            }
        } else {
            rechargeMoneyAdapter = null;
            mRootView.showAdapter(rechargeMoneyAdapter);
        }
    }

    /**
     * @param position 用户选中的
     */
    public void userChoose(int position) {
        if (moneyCardList != null && moneyCardList.size() > position) {
            moneyChoose = moneyCardList.get(position);
        }
    }

    /**
     * 预支付
     */
    public void recharge() {

        if (listData != null && choosePositon < listData.size()) {
            payTypeShow = listData.get(choosePositon);
            if (!(payTypeShow != null && !TextUtils.isEmpty(payTypeShow.getId()))) {
                ArmsUtils.makeText(mApplication, "请选择支付方式");
                return;
            }
        }
        if (!(moneyChoose != null && !TextUtils.isEmpty(moneyChoose.getId()))) {
            ArmsUtils.makeText(mApplication, "请选择金额");
            return;
        }
        if(payTypeShow==null||moneyChoose==null){
            return;
        }
        showPayPop(payTypeShow, moneyChoose);
    }


    void showNet(MoneyShow payType, MoneyShow moneyChoose) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setId(moneyChoose.getId());
        tokenEntity.setInterfaceid(payType.getId());
        mModel.recharge(tokenEntity)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<PayMoneyResult>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<PayMoneyResult> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            reChargeResult(payType, commonEntity.getData(), moneyChoose);
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }



    /**
     * 预支付结果处理
     *
     * @param payType
     * @param data
     */
    void reChargeResult(MoneyShow payType, PayMoneyResult data, MoneyShow moneyChoose) {
        showPop = new MoneyShow();
        showPop.setName(payType.getName());//支付方式名称
        showPop.setCode(payType.getCode());//wx or ali
        showPop.setPaytype(payType.getPaytype());//支付类型
        showPop.setRemark(payType.getRemark());//支付方式备注
        showPop.setUrl(data.getUrl());//二维码地址
        showPop.setMoney(data.getMoney());//支付金额
        showPop.setOrderNumber(data.getChargeid());//订单号
        showPop.setAccountno(payType.getAccountno());//客服账号
        showPop.setCardName(moneyChoose.getName());//卡
        showPop.setAccountname(payType.getAccountname());
        if (payType != null && !TextUtils.isEmpty(payType.getPaytype())) {
            switch (payType.getPaytype()) {//VipQrcodeActivity
                case "1"://二维码扫码

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("showQrcode", showPop);
                    mRootView.launchActivity(new Intent(mApplication, VipQrcodeActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtras(bundle)
                    );
                    break;
                case "2"://客服端支付
                    if (!TextUtils.isEmpty(payType.getCode()) && data != null) {//
                        if ("alipay".equals(payType.getCode()) && !TextUtils.isEmpty(data.getPayurl())) {//支付宝客服端
//                            payV2("alipay_sdk=alipay-sdk-php-20180705&app_id=2019030763472584&biz_content=%7B%22body%22%3A%22%5Cu5145%5Cu503c%22%2C%22subject%22%3A%22%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%222019031110002220824335%22%2C%22total_amount%22%3A%22200.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpay.mw238.com%2Faliback&sign_type=RSA2&timestamp=2019-03-11+20%3A43%3A35&version=1.0&sign=NxHgJMd4O6wAnTuOFFCT2LTOFmnsIrAy%2F4%2BKy1jcWFiYhMymBZ2pnzE9371b46KN3mVVsYoPy3kiBju8vDalV36kNYuGUDJKwtzOqpjGwT8f6c2lMtPJO3O9Dm9KhwccUvaJWrTX7uTb%2Bz9qkzwRGwk8WvcovxLMZRHDfPx2%2BBl9CkYOt5fWKwctWA%2BvLY6P913%2B3BCPaKJf7SgjXK3YMjJH%2BJUxNXshS4MsiVZdhRMZXDRczi51tTQpCRqqfqjTTB0QHXd%2BRJ7eBepjGs2HtpbAy3JCfngPb%2BWtrLk5pvh0ICjPpUbUy6UPa7hKSPYEYIq%2Fp175Xd12KwrJKBJFrw%3D%3D");
                            payV2(data.getPayurl());
                        } else if ("wx".equals(payType.getCode())) {//微信客服端
                            if(!TextUtils.isEmpty(data.getPayurl())){
                                weChatPay(data.getPayurl());
                            }
                        }
                    }
                    break;
                case "3"://3-url地址跳转
                    //url 支付
                    if (!(TextUtils.isEmpty(data.getPayurl())&&TextUtils.isEmpty(data.getUrl()))) {
                        HbCodeUtils.openBrowser(mApplication,TextUtils.isEmpty(data.getPayurl())?(TextUtils.isEmpty(data.getUrl())?"http://www.baidu.com":data.getUrl()):data.getPayurl());
//                        mRootView.launchActivity(new Intent(mApplication, WebViewActivity.class)
//                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                .putExtra("titleStr", "VIP充值")
//                                .putExtra("payType", payType.getName())
//                                .putExtra("url", TextUtils.isEmpty(data.getPayurl())?(TextUtils.isEmpty(data.getUrl())?"http://www.baidu.com":data.getUrl()):data.getPayurl())
//                        );
                    }else{
                        ArmsUtils.makeText(mApplication,"支付地址不存在!");
                    }
                    break;
            }
        }
    }

    /**
     * @param payType     支付方式
     * @param moneyChoose //支付金额
     */
    void showPayPop(MoneyShow payType, MoneyShow moneyChoose) {
        if (payType != null && !TextUtils.isEmpty(payType.getPaytype())) {
            switch (payType.getPaytype()) {
                case "1"://二维码扫码
                    showNet(payType, moneyChoose);
                    break;
                case "2"://2-客户端 先条用预支付获取支付信息
                    showNet(payType, moneyChoose);
                    break;
                case "3"://3-url地址跳转
                    showNet(payType, moneyChoose);
                    break;
                case "4"://卡密支付
                    if("service".equals(payType.getCode())){//官方客服
                        EventBus.getDefault().post("", EventBusTags.CALL_SERVICE);
                    }else{//卡密支付
                        mRootView.launchActivity(new Intent(mApplication,CardPayActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        );
                    }
                    break;
            }
        }
    }

    /**
     * 购买卡密
     */
    public void buyCardPwd() {
        if (listData != null && listData.size() > 0) {
            MoneyShow moneyShowTemp = null;
            for (MoneyShow ms : listData) {
                if ("4".equals(ms.getPaytype())) {
                    moneyShowTemp = ms;
                    break;
                }
            }
            if (moneyShowTemp == null) {
                ArmsUtils.makeText(mApplication, "该业务未开通,请选择其他方式购买");
                return;
            }
            showPayPop(moneyShowTemp, null);
        }
    }


    /**
     * @param cardPwdStr 卡密开通
     */
    public void nowOpen(String cardPwdStr) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setCardno(cardPwdStr);
        mModel.cardRecharge(tokenEntity)
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
                .subscribe(new ErrorHandleSubscriber<CommonEntity<UserEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<UserEntity> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode()) && commonEntity.getData() != null) {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }


    /**
     * 支付宝支付业务示例
     */
    public void payV2(String orderInfo) {
        if (TextUtils.isEmpty(orderInfo)) {
            ArmsUtils.makeText(mApplication, "支付失败,请返回重试！");
            return;
        }
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mAppManager.getCurrentActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }


    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ArmsUtils.makeText(mApplication, "支付成功!");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("showQrcode",showPop);
                        mRootView.launchActivity(new Intent(mApplication, OfflineRechargeActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtras(bundle)
                        );
                        mRootView.killMyself();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ArmsUtils.makeText(mApplication, "" + (!TextUtils.isEmpty(resultInfo)?resultInfo:"支付取消"));
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    /**
     * @param payUrl 调用微信支付
     */
    private void weChatPay(String payUrl) {
        WeChatPay weChatPay = (MyApplication.getsInstance()).getAppComponent().gson().fromJson(StringUtils.replaceXieGang(payUrl), WeChatPay.class);
        Observable.just(weChatPay)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<WeChatPay, Boolean>() {

                    @Override
                    public Boolean apply(WeChatPay weChatPay) throws Exception {
                        PayReq request = new PayReq(); //调起微信APP的对象
                        //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
                        request.appId = weChatPay.getAppid();
                        request.partnerId = weChatPay.getPartnerid();
                        request.prepayId = weChatPay.getPrepayid();
                        request.packageValue = weChatPay.getPackagestr();
                        request.nonceStr = weChatPay.getNoncestr();
                        request.timeStamp = weChatPay.getTimestamp();
                        request.sign = weChatPay.getSign();
                        return VipRechargeActivity.mWxApi.sendReq(request);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Boolean>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull Boolean commonEntity) {
                    }
                });
    }

    @Inject
    public VipRechargePresenter(VipRechargeContract.Model model, VipRechargeContract.View rootView) {
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
