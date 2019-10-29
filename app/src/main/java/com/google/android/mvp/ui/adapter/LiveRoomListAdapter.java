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
import com.google.android.mvp.model.back_entity.LiveInfo;

import java.util.List;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * 单个平台房间列表
 */
public class LiveRoomListAdapter extends BaseQuickAdapter<LiveInfo, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public LiveRoomListAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_live_detail,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, LiveInfo item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setText(R.id.live_detail_text,!TextUtils.isEmpty(item.getTitle())?item.getTitle():"")
                .setText(R.id.tv_room_looker,helper.getPosition()+500+((int)(Math.random()*500))+"");
        showGgImage(helper, R.id.live_detail_img, item.getImg());


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
