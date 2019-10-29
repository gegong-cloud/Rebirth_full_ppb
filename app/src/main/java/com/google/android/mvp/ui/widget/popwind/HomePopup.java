package com.google.android.mvp.ui.widget.popwind;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 首页提示
 */
public class HomePopup extends BasePopupWindow implements View.OnClickListener {



    public HomePopup(Context context, String titleStr, String contentStr) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        setPopupWindowFullScreen(false);//不让PopupWindow的蒙层覆盖状态栏
//        setAllowDismissWhenTouchOutside(false);//点击popupwindow背景部分不想让popupwindow隐藏
//        setBackPressEnable(false);//点击back键不关闭pop
        ((TextView)findViewById(R.id.home_tip_title)).setText(!TextUtils.isEmpty(titleStr)?titleStr:"公告");
        ((TextView)findViewById(R.id.home_tip_content)).setText(contentStr);
        findViewById(R.id.home_tip_cancel).setOnClickListener(this);


    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_home_tip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_tip_cancel:
                dismiss();
                break;
        }
    }


}
