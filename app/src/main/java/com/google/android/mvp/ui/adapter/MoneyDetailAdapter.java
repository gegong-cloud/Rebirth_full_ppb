package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.mvp.model.back_entity.MoneyTakeDetail;

import java.util.List;

/**
 * 收支明细
 */
public class MoneyDetailAdapter extends BaseQuickAdapter<MoneyTakeDetail, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public MoneyDetailAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_money_detail,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, MoneyTakeDetail item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        String moneyNumber = "";
        int colorId = R.color.white;
        if("提现".equals(item.getType())){
            moneyNumber = "-" + item.getMoney();
            colorId = R.color.white;
        }else if("返佣".equals(item.getType())){
            moneyNumber = "+" + item.getMoney();
            colorId = R.color.radius_fc4288;
        }else{
            moneyNumber = item.getMoney();
            colorId = R.color.white;
        }

        helper.setText(R.id.money_title,(!TextUtils.isEmpty(item.getType())?item.getType():"")
                +(!TextUtils.isEmpty(item.getRemark())?item.getRemark():""))
                .setText(R.id.money_time,!TextUtils.isEmpty(item.getCreatetime())?item.getCreatetime():"")
                .setText(R.id.money_number,moneyNumber);
        ((TextView)helper.getView(R.id.money_number)).setTextColor(ContextCompat.getColor(activity,colorId));


    }

    private void showGgImage(BaseViewHolder helper, int resId, String url) {
        if (!TextUtils.isEmpty(url)) {
            mAppComponent
                    .imageLoader()
                    .loadImage(mContext, ImageConfigImpl
                            .builder()
                            .imageView(helper.getView(resId))
                            .url(url)
                            .build());
        } else {
            helper.setImageResource(resId, R.drawable.ic_launcher_background);
        }
    }



}
