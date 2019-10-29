package com.google.android.mvp.ui.widget.popwind;

import android.app.Activity;
import android.view.View;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;

import org.simple.eventbus.EventBus;

import razerdp.basepopup.BasePopupWindow;

/**
 * 粘贴
 */
public class PastePopup extends BasePopupWindow implements View.OnClickListener {

    Activity activity;


    private AppComponent mAppComponent;

    public PastePopup(Activity context) {
        super(context);
        this.activity = context;
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(activity);
        }
        setBackground(0);
//        setPopupGravity(Gravity.CENTER);
        setBackPressEnable(false);//点击back键不关闭pop
        findViewById(R.id.popup_paste_text).setOnClickListener(this);

    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_paste);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_paste_text:
                EventBus.getDefault().post("",EventBusTags.PASTE_TEXT);
                dismiss();
                break;
        }
    }


}
