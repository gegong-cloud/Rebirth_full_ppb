package com.google.android.mvp.ui.widget.popwind;

import android.app.Activity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.ui.widget.downtimer.DownTimer;
import com.google.android.mvp.ui.widget.downtimer.DownTimerListener;

import org.simple.eventbus.EventBus;

import razerdp.basepopup.BasePopupWindow;

/**
 * VIP 充值倒计时提示
 */
public class VipPromptPopup extends BasePopupWindow implements View.OnClickListener,DownTimerListener {

    MoneyShow showPop;
    Activity activity;

    TextView textView;

    private AppComponent mAppComponent;
    public VipPromptPopup(Activity context, MoneyShow showPop) {
        super(context);
        this.activity = context;
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(activity);
        }
        setPopupGravity(Gravity.CENTER);
        setPopupWindowFullScreen(false);//不让PopupWindow的蒙层覆盖状态栏
        setAllowDismissWhenTouchOutside(false);//点击popupwindow背景部分不想让popupwindow隐藏
        setBackPressEnable(false);//点击back键不关闭pop
        this.showPop = showPop;
        ((TextView)findViewById(R.id.pay_number)).setText(showPop.getOrderNumber());
        ((TextView)findViewById(R.id.pay_money)).setText(showPop.getMoney());
        textView = ((TextView)findViewById(R.id.pay_confirm));
        beginDownTime();
//        ((TextView)findViewById(R.id.pay_context)).setText(showPop.getRemark());


    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_pay_confirm);
    }

    @Override
    public void onClick(View v) {
    }


    void beginDownTime(){
        DownTimer downTimer = new DownTimer();
        downTimer.setListener(this);
        downTimer.startDown(15 * 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
//        if(3==(millisUntilFinished/1000)){
//            EventBus.getDefault().post("", EventBusTags.SELECT_PAY_RESULT);
//        }
        textView.setText(Html.fromHtml("支付确认中...<font color='#FC488C'>"+(millisUntilFinished/1000)+"</font>秒"));
    }

    @Override
    public void onFinish() {
        EventBus.getDefault().post("", EventBusTags.CAST_PAY_RESULT);
        dismiss();
    }
}
