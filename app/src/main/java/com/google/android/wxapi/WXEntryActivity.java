package com.google.android.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.mvp.ui.activity.VipRechargeActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";
    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        //这句没有写,是不能执行回调的方法的
        VipRechargeActivity.mWxApi.handleIntent(getIntent(), this);
    }


    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i(TAG, "onResp:------>");
        Log.i(TAG, "error_code:---->" + baseResp.errCode);
        int type = baseResp.getType(); //类型：分享还是登录
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                ArmsUtils.makeText(this, "拒绝授权微信登录");
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                } else if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    ArmsUtils.makeText(this, "支付取消");
                }
                ArmsUtils.makeText(this, message);
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) baseResp).code;
                    Log.i(TAG, "code:------>" + code);
//                    EventBus.getDefault().post(new WxLogin(code), EventBusTags.WX_LOGIN_CODE);

//                    ArmsUtils.makeText(this, "成功获取微信登录code："+code);
                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
//                    WXLoginUtils().getWXLoginResult(code, this);

                } else if (type == RETURN_MSG_TYPE_SHARE) {
//                    EventBus.getDefault().post(String.valueOf(""), EventBusTags.SHARE_TASK);
                    ArmsUtils.makeText(this, "微信分享成功");
                } else if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    ArmsUtils.makeText(this, "支付成功");
                }

                break;
        }
        finish();

    }


}
