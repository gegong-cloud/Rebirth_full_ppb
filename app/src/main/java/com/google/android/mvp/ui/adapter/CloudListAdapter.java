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
import com.google.android.mvp.model.back_entity.YunboMov;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * 云播标签对应的影片列表
 */
public class CloudListAdapter extends BaseQuickAdapter<YunboMov, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public CloudListAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_cloud_detail,data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, YunboMov item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setImageResource(R.id.cloud_detail_img,R.drawable.cloud_default_img);
        helper.setText(R.id.cloud_detail_text,!TextUtils.isEmpty(item.getName())?item.getName():"");
        showGgImage(helper, R.id.cloud_detail_img, item.getCover());

        //收藏相关
        helper.getView(R.id.my_favor).setVisibility(View.VISIBLE);
        helper.addOnClickListener(R.id.my_favor);
        helper.setImageResource(R.id.my_favor, "1".equals(item.getHas_favor()) ?R.drawable.favor_press:R.drawable.favor_nopre);
    }

    private void showGgImage(BaseViewHolder helper, int resId, String url) {
        if (!TextUtils.isEmpty(url)) {
            StringUtils.showImgView(mAppComponent,helper.getView(resId),activity,url,getStrHashCode(url));
//            Glide.with(activity).load(url).into(new SimpleTarget<Drawable>() {
//                @Override
//                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        helper.setImageDrawable(resId, resource);
//                }
//
//                @Override
//                public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                    super.onLoadFailed(errorDrawable);
//                    helper.setImageResource(resId, R.drawable.cloud_default_img);
//                }
//
//                @Override
//                public void onLoadStarted(@Nullable Drawable placeholder) {
//                    super.onLoadStarted(placeholder);
//                    helper.setImageResource(resId, R.drawable.cloud_default_img);
//                }
//            });
        } else {
            helper.setImageResource(resId, R.drawable.cloud_default_img);
        }
    }



}
