/*
    ShengDao Android Client, DownTimerListener
    Copyright (c) 2014 ShengDao Tech Company Limited
 */
package com.google.android.mvp.ui.widget.downtimer;

/**
 * [倒计时监听类]
 **/
public interface DownTimerListener {

    /**
     * [倒计时每秒方法]<BR>
     * @param millisUntilFinished
     */
    void onTick(long millisUntilFinished);

    /**
     * [倒计时完成方法]<BR>
     */
    void onFinish();
}

