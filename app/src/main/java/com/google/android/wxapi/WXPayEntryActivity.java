package com.google.android.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.mvp.model.upload.WxPayResult;
import com.google.android.mvp.ui.activity.VipRechargeActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.simple.eventbus.EventBus;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        VipRechargeActivity.mWxApi.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int type = baseResp.getType(); //类型：分享还是登录
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                ArmsUtils.makeText(this, "拒绝授权微信登录");
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "支付取消";
                if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    ArmsUtils.makeText(this, "支付取消");
                }
                ArmsUtils.makeText(this, message);
                break;
            case BaseResp.ErrCode.ERR_OK:
                if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    ArmsUtils.makeText(this, "支付成功");
                    EventBus.getDefault().post(new WxPayResult("支付成功"), EventBusTags.WX_PAY_RESULT);
                }
                break;
            default:
                ArmsUtils.makeText(this, "支付取消");
                break;
        }
        finish();
    }
}
