package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.mvp.model.back_entity.MoneyShow;

import java.util.List;

/**
 * 充值金额展示
 */
public class RechargeMoneyAdapter extends BaseQuickAdapter<MoneyShow, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public RechargeMoneyAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_recharge_money, data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, MoneyShow item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        ((TextView)helper.getView(R.id.vip_old_money)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中划线
        helper
                .setText(R.id.vip_name, !TextUtils.isEmpty(item.getName()) ? item.getName() : "")
                .setText(R.id.vip_money, !TextUtils.isEmpty(item.getMoney()) ? item.getMoney() : "")
                .setText(R.id.vip_old_money, !TextUtils.isEmpty(item.getOrigmoney()) ? item.getOrigmoney() : "")
                .setText(R.id.vip_details, !TextUtils.isEmpty(item.getTip()) ? item.getTip() : "")
        ;
        helper.getView(R.id.vip_bg).setBackground(item.isChoose()? ContextCompat.getDrawable(activity,R.drawable.radius_fff1f6_fc4288_6):ContextCompat.getDrawable(activity,R.drawable.radius_ffffff_dddddd_6));
        helper.addOnClickListener(R.id.vip_open);

    }

//    private void showGgImage(BaseViewHolder helper, int resId, String url) {
//        if (!TextUtils.isEmpty(url)) {
//            mAppComponent
//                    .imageLoader()
//                    .loadImage(mContext, ImageConfigImpl
//                            .builder()
//                            .imageView(helper.getView(resId))
//                            .url(url)
//                            .build());
//        } else {
//            helper.setImageResource(resId, R.drawable.ic_launcher_background);
//        }
//    }


}
