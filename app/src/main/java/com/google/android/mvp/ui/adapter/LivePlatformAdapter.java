package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.LiveInfo;

import java.util.List;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * 直播平台列表
 */
public class LivePlatformAdapter extends BaseQuickAdapter<LiveInfo, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public LivePlatformAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_live,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, LiveInfo item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setText(R.id.home_menu_title,!TextUtils.isEmpty(item.getTitle())?item.getTitle():"")
                .setText(R.id.live_number,!TextUtils.isEmpty(item.getNumber())?item.getNumber():"");
        helper.getView(R.id.live_danmu).setVisibility("1".equals(item.getIs_badge())? View.VISIBLE:View.GONE);
        showGgImage(helper, R.id.home_menu_img, item.getImg());


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
