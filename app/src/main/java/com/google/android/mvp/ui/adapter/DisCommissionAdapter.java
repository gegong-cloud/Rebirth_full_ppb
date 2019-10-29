package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.DisCommission;

import java.util.List;

/**
 * 分销提成
 */
public class DisCommissionAdapter extends BaseQuickAdapter<DisCommission.DisList, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public DisCommissionAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_dis_commission,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, DisCommission.DisList item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(activity);
        }
        helper.setText(R.id.dis_phone,!TextUtils.isEmpty(item.getUsername())? StringUtils.replaceToXing(item.getUsername()):"")
                .setText(R.id.dis_time,!TextUtils.isEmpty(item.getCreatetime())?item.getCreatetime():"")
                .setText(R.id.dis_level,!TextUtils.isEmpty(item.getBonustype())?item.getBonustype():"")
                .setText(R.id.dis_money,!TextUtils.isEmpty(item.getMoney())?item.getMoney():"");


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
