package com.google.android.mvp.ui.widget.popwind;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.ui.activity.LoginActivity;
import com.google.android.mvp.ui.activity.VipRechargeActivity;

import razerdp.basepopup.BasePopupWindow;

public class MsgPopup extends BasePopupWindow implements View.OnClickListener {


    CommonEntity<JionLiveEntity> contextStr;
    public MsgPopup(Context context,CommonEntity<JionLiveEntity> msgStr) {
        super(context);
        contextStr = msgStr;
        if(msgStr!=null&&!TextUtils.isEmpty(msgStr.getMsg())){
            ((TextView)findViewById(R.id.tv_msg)).setText(msgStr.getMsg());
        }
        setPopupGravity(Gravity.CENTER);
        findViewById(R.id.bt_ok).setOnClickListener(this);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                if("-997".equals(contextStr.getCode())){
                    HbCodeUtils.setToken("");
                    ArmsUtils.startActivity(new Intent(getContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else{
                    ArmsUtils.startActivity(new Intent(getContext(), VipRechargeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                dismiss();
                break;
        }
    }
}
