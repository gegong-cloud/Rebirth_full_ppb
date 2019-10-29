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
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.MyTeam;

import java.util.List;

/**
 * 我的团队/我的徒弟
 */
public class MyApprenticeAdapter extends BaseQuickAdapter<MyTeam, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public MyApprenticeAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_my_apprentice,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyTeam item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        int colorId = R.color.font_666666;
        String moneyNumber = "已激活";
        if("1".equals(item.getActive())){
            moneyNumber = "已激活";
            colorId = R.color.radius_fc4288;
        }else{
            colorId = R.color.font_666666;
            moneyNumber = "未激活";
        }
        helper.setText(R.id.dis_phone,!TextUtils.isEmpty(item.getUsername())?(StringUtils.replaceToXing(item.getUsername())):"")
                .setText(R.id.dis_time,!TextUtils.isEmpty(item.getEnd_time())?item.getEnd_time():"")
                .setText(R.id.dis_status,moneyNumber);
        ((TextView)helper.getView(R.id.dis_status)).setTextColor(ContextCompat.getColor(activity,colorId));

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
