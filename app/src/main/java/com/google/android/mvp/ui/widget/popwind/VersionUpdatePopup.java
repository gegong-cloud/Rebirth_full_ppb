package com.google.android.mvp.ui.widget.popwind;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.google.android.R;
import com.google.android.app.utils.HbCodeUtils;

import razerdp.basepopup.BasePopupWindow;

/**
 * VIP 充值倒计时提示
 */
public class VersionUpdatePopup extends BasePopupWindow implements View.OnClickListener{

    Activity activity;

    String updateUrl;

    public VersionUpdatePopup(Activity context, String downUrl) {
        super(context);
        this.activity = context;
        updateUrl = downUrl;
        setPopupGravity(Gravity.CENTER);
        setPopupWindowFullScreen(false);//不让PopupWindow的蒙层覆盖状态栏
        setAllowDismissWhenTouchOutside(false);//点击popupwindow背景部分不想让popupwindow隐藏
        setBackPressEnable(false);//点击back键不关闭pop
        findViewById(R.id.pay_confirm).setOnClickListener(this);


    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_version_update);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_confirm:
                HbCodeUtils.openBrowser(activity,updateUrl);
//                dismiss();
//                ArmsUtils.exitApp();
                break;
        }
    }


}
