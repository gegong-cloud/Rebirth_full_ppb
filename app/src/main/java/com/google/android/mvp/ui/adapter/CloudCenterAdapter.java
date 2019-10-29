package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.CloudSection;
import com.google.android.mvp.model.back_entity.LabelEntity;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * 云播标签展示
 */
public class CloudCenterAdapter extends BaseSectionQuickAdapter<CloudSection, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    boolean hideBottom = false;

    public CloudCenterAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_cloud_list_center, R.layout.adapter_cloud_center_title, data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, CloudSection item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setText(R.id.cloud_title, !TextUtils.isEmpty(item.header) ? item.header : "");
        String gifUrl = "android.resource://" + activity.getPackageName() + "/" + R.raw.show_more;
        showGgImage(helper.getView(R.id.cloud_more),gifUrl);
    }

    @Override
    protected void convert(BaseViewHolder helper, CloudSection item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setImageResource(R.id.cloud_detail_img, R.drawable.cloud_default_img);
        LabelEntity.LabelList labelList = item.t;
        helper.setText(R.id.cloud_detail_text, !TextUtils.isEmpty(labelList.getName()) ? labelList.getName() : "");
        showGgImage(helper, R.id.cloud_detail_img, StringUtils.getBaseUrl() + labelList.getCover());

        //收藏相关
        helper.getView(R.id.my_favor).setVisibility(View.VISIBLE);
        helper.addOnClickListener(R.id.my_favor);
        helper.setImageResource(R.id.my_favor, "1".equals(labelList.getHas_favor()) ? R.drawable.favor_press:R.drawable.favor_nopre);
    }


    private void showGgImage(BaseViewHolder helper, int resId, String url) {
        if (!TextUtils.isEmpty(url)) {
            StringUtils.showImgView(mAppComponent, helper.getView(resId), activity, url, getStrHashCode(url));
        } else {
            helper.setImageResource(resId, R.drawable.cloud_tag_default);
        }
    }

    private void showGgImage(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            mAppComponent
                    .imageLoader()
                    .loadImage(activity, ImageConfigImpl
                            .builder()
                            .imageView(imageView)
                            .url(url)
                            .build());
        }
    }

}
