package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

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
 * 历史记录
 */
public class HistoryAdapter extends BaseQuickAdapter<YunboMov, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    boolean showCheck = false;

    public HistoryAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_history, data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, YunboMov item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        helper.setText(R.id.history_title, item.getName())
                .setText(R.id.history_descirption, item.getView_num());
        showGgImage(helper, R.id.history_img, StringUtils.getBaseUrl()+item.getCover());
        ImageView checkBox = helper.getView(R.id.history_checkbox);
        checkBox.setVisibility(showCheck ? View.VISIBLE : View.GONE);
        checkBox.setImageResource(item.isEditFlag()?R.drawable.login_checkbox_true:R.drawable.login_checkbox_false);

    }


    private void showGgImage(BaseViewHolder helper, int resId, String url) {
        if (!TextUtils.isEmpty(url)) {
            StringUtils.showImgView(mAppComponent, helper.getView(resId), activity, url, getStrHashCode(url));
        } else {
            helper.setImageResource(resId, R.drawable.cloud_default_img);
        }
    }

    /**
     *
     * @param flag 是否显示编辑状态
     */
    public void setShowCheck(boolean flag){
        showCheck = flag;
        notifyDataSetChanged();
    }

    public boolean getShowCheck() {
        return showCheck;
    }
}
