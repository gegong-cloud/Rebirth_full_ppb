package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.mvp.model.back_entity.MoneyShow;

import java.util.List;

/**
 * vip充值方式
 */
public class VipRechargeType1Adapter extends BaseQuickAdapter<MoneyShow, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public VipRechargeType1Adapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_vip_artificial,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, MoneyShow item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setText(R.id.self_help_text,!TextUtils.isEmpty(item.getName())?item.getName():"");
        helper.getView(R.id.vip_line).setVisibility((helper.getPosition()==datas.size()-1)? View.GONE:View.VISIBLE);



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
