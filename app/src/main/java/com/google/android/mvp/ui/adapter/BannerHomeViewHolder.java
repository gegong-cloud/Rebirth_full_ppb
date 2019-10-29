package com.google.android.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * 首页banner
 */
public class BannerHomeViewHolder implements MZViewHolder<HomeAdv> {
    private ImageView mImageView;

    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        return  view;
    }

    @Override
    public void onBind(Context context, int i, HomeAdv bannerInfo) {
        if(bannerInfo!=null&&!TextUtils.isEmpty(bannerInfo.getImage())){
            StringUtils.showImgView(ArmsUtils.obtainAppComponentFromContext(context),mImageView,context,bannerInfo.getImage(),getStrHashCode(bannerInfo.getImage()));
//            Glide.with(context).load(StringUtils.getBaseUrl()+bannerInfo.getImage()).into(mImageView);
        }else{
            mImageView.setImageResource(R.drawable.banner_default_img);
        }
        // 数据绑定
//        mImageView.setImageResource(homeBanner.getResId());
    }

}
