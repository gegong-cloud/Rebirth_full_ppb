package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.HomeVipMov;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * 影视adapter
 */
public class HomeMovAdapter extends BaseQuickAdapter<HomeVipMov.HomeVipMovList, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public HomeMovAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_home_movie,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeVipMov.HomeVipMovList item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setImageResource(R.id.movie_img,R.drawable.cloud_default_img);
        helper
                .setText(R.id.movie_title,!TextUtils.isEmpty(item.getTitle())?item.getTitle():"")
                .setText(R.id.movie_play_number,!TextUtils.isEmpty(item.getView_num())?item.getView_num():"")
                .setText(R.id.movie_time,!TextUtils.isEmpty(item.getTip())?item.getTip():"")
        ;
        showGgImage(helper, R.id.movie_img, StringUtils.getBaseUrl()+item.getImage());

        //收藏相关
        if (!TextUtils.isEmpty(item.getLink()) && item.getLink().startsWith("yunbo")) {
            String[] yunboIds = item.getLink().split("yunbo_id=");
            if (yunboIds != null && yunboIds.length >= 2) {//这里显示收藏
                helper.getView(R.id.my_favor).setVisibility(View.VISIBLE);
                helper.addOnClickListener(R.id.my_favor);
                helper.setImageResource(R.id.my_favor,"1".equals(item.getHas_favor())?R.drawable.favor_press:R.drawable.favor_nopre);
            }else{//不显示收藏
                helper.getView(R.id.my_favor).setVisibility(View.GONE);
            }
        }else{
            helper.getView(R.id.my_favor).setVisibility(View.GONE);
        }

    }

    private void showGgImage(BaseViewHolder helper, int resId, String url) {
        if (!TextUtils.isEmpty(url)) {
            StringUtils.showImgView(mAppComponent,helper.getView(resId),activity,url,getStrHashCode(url));
        } else {
            helper.setImageResource(resId, R.drawable.cloud_default_img);
        }
    }



}
