package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.HomeAdv;

import java.util.List;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * 播放页广告
 */
public class PlayAdvAdapter extends BaseQuickAdapter<HomeAdv, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public PlayAdvAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_gg_img, data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeAdv item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        showGgImage(helper, R.id.home_two_mode_img, StringUtils.getBaseUrl()+item.getImage());
    }

    private void showGgImage(BaseViewHolder helper, int resId, String url) {
        if (!TextUtils.isEmpty(url)) {
            StringUtils.showImgView(mAppComponent,helper.getView(resId),activity,url,getStrHashCode(url));
//            mAppComponent
//                    .imageLoader()
//                    .loadImage(mContext, ImageConfigImpl
//                            .builder()
//                            .imageView(helper.getView(resId))
//                            .url(url)
//                            .build());
        } else {
            helper.setImageResource(resId, R.drawable.ic_launcher_background);
        }
    }




}
