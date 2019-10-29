package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.mvp.model.back_entity.RechargeDetails;

import java.util.List;

/**
 * 充值记录
 */
public class RechargeDetailsAdapter extends BaseQuickAdapter<RechargeDetails, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public RechargeDetailsAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_recharge_detail, data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeDetails item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setText(R.id.money_title, !TextUtils.isEmpty(item.getDesc()) ? item.getDesc() : "")
                .setText(R.id.money_time, !TextUtils.isEmpty(item.getCreatetime()) ? item.getCreatetime() : "")
                .setText(R.id.money_number, !TextUtils.isEmpty(item.getBalance()) ? item.getBalance()+"元" : "");
//        ((TextView)helper.getView(R.id.money_number)).setTextColor(ContextCompat.getColor(activity,colorId));

    }

    String getStatusStr(String status) {
        String statusStr = "新申请";
        switch (status) {
            case "1":
                statusStr = "新申请";
                break;
            case "2":
                statusStr = "支付成功";
                break;
            case "3":
                statusStr = "支付失败";
                break;
            default:
                statusStr = "新申请";
                break;
        }
        return statusStr;
    }


}
