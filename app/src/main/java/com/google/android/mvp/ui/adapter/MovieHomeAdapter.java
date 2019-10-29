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
import com.google.android.mvp.model.back_entity.HomeVipMov;

import java.util.List;

/**
 * 新首页的影视
 */
public class MovieHomeAdapter extends BaseQuickAdapter<HomeVipMov.HomeVipMovList, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public MovieHomeAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_home_movie,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeVipMov.HomeVipMovList item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setText(R.id.home_menu_title,!TextUtils.isEmpty(item.getTitle())?item.getTitle():"");
        showGgImage(helper, R.id.home_menu_img, StringUtils.getBaseUrl()+item.getImage());


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
