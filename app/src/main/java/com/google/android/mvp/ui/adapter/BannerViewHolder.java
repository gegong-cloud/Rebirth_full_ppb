package com.google.android.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.BannerInfo;
import com.zhouwei.mzbanner.holder.MZViewHolder;

/**
 * 首页banner
 */
public class BannerViewHolder implements MZViewHolder<BannerInfo> {
    private ImageView mImageView;
    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        return  view;
    }

    @Override
    public void onBind(Context context, int i, BannerInfo bannerInfo) {
        Glide.with(context).load(StringUtils.getBaseUrl()+bannerInfo.getImage()).into(mImageView);
        // 数据绑定
//        mImageView.setImageResource(homeBanner.getResId());
    }

}
