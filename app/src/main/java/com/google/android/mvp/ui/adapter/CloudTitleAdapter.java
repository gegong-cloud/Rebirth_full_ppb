package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.MyApplication;
import com.google.android.mvp.model.back_entity.LabelEntity;

import java.util.List;

/**
 * 直播平台列表
 */
public class CloudTitleAdapter extends BaseQuickAdapter<LabelEntity, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public CloudTitleAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_cloud_list_title, data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, LabelEntity item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        TextView textView = helper.getView(R.id.cloud_title);
        textView.setTextColor(item.isSelect()?ContextCompat.getColor(MyApplication.getsInstance(),R.color.white):ContextCompat.getColor(MyApplication.getsInstance(),R.color.font_78788E));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP ,item.isSelect()?20:16);
        helper.setText(R.id.cloud_title, !TextUtils.isEmpty(item.getName()) ? item.getName() : "");
    }

}
