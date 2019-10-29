package com.google.android.mvp.ui.widget.popwind;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.JumpUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import razerdp.basepopup.BasePopupWindow;

/**
 * 卡密支付开通vip
 */
public class CardPwdOpenPopup extends BasePopupWindow implements View.OnClickListener {

    Activity activity;

    EditText editText;

    private AppComponent mAppComponent;

    /**
     * 粘贴类容
     */
    String pasteStr = "";

    public CardPwdOpenPopup(Activity context) {
        super(context);
        this.activity = context;
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(activity);
        }
        setPopupGravity(Gravity.CENTER);
//        setPopupWindowFullScreen(false);//不让PopupWindow的蒙层覆盖状态栏
//        setAllowDismissWhenTouchOutside(false);//点击popupwindow背景部分不想让popupwindow隐藏
        setBackPressEnable(false);//点击back键不关闭pop
        findViewById(R.id.vip_now_open).setOnClickListener(this);
        findViewById(R.id.online_close).setOnClickListener(this);
        editText = (EditText) findViewById(R.id.input_card_pwd);
        setAutoShowInputMethod(editText,true);
//        ((TextView)findViewById(R.id.pay_context)).setText(showPop.getRemark());
        editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPaste();
                return false;
            }
        });

    }

    void showPaste(){
        pasteStr = JumpUtils.getClipboardText(activity);
        if(TextUtils.isEmpty(pasteStr)){
            return;
        }
        new PastePopup(activity).showPopupWindow(findViewById(R.id.input_card_paste));
    }


    @Override
    public View onCreateContentView() {
        EventBus.getDefault().register(this);
        return createPopupById(R.layout.popup_now_open);
    }

    @Override
    public void onDismiss() {
        EventBus.getDefault().unregister(this);
        super.onDismiss();
    }

    @Subscriber(tag = EventBusTags.PASTE_TEXT)
    public void setPaste(String event){
        editText.setText(!TextUtils.isEmpty(pasteStr)?pasteStr:"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vip_now_open:
                nowOpen();
                break;
            case R.id.online_close:
                dismiss();
                break;
        }
    }

    void nowOpen() {
        if(TextUtils.isEmpty(editText.getText())){
            ArmsUtils.makeText(activity,"请输入您的卡密");
            return;
        }
        EventBus.getDefault().post(editText.getText().toString(),EventBusTags.CARD_PWD_OPEN);
        dismiss();
    }

}
