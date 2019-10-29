/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link Application.ActivityLifecycleCallbacks} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks/**, DownTimerListener**/ {

//    /**
//     * 这里主要判断用户是否进入后台五分钟
//     */
//    boolean isRunInBackground = false;
//    int appCount = 0;
//    boolean isShowDlag = false;
//    DownTimer downTimer;
//
//    DownTimer downTimer2;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Timber.w(activity + " - onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Timber.w(activity + " - onActivityStarted");
//        appCount++;
//        initDownLoadInfo();
//        if (isRunInBackground) {
//            //应用从后台回到前台 需要做的操作
//            back2App(activity);
//        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Timber.w(activity + " - onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Timber.w(activity + " - onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Timber.w(activity + " - onActivityStopped");
//        appCount--;
//        if (appCount == 0) {
//            //应用进入后台 需要做的操作
//            leaveApp(activity);
//        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Timber.w(activity + " - onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Timber.w(activity + " - onActivityDestroyed");
        //横竖屏切换或配置改变时, Activity 会被重新创建实例, 但 Bundle 中的基础数据会被保存下来,移除该数据是为了保证重新创建的实例可以正常工作
        activity.getIntent().removeExtra("isInitToolbar");
    }
//
//    /**
//     * 从后台回到前台需要执行的逻辑
//     *
//     * @param activity
//     */
//    private void back2App(Activity activity) {
//        isRunInBackground = false;
//        if (isShowDlag) {
//            if(activity instanceof LoginActivity||activity instanceof CarouselAdActivity){
//                return;
//            }
//            updateVersion(activity);
//            isShowDlag = false;
//        }else{
//            if(downTimer!=null){
//                downTimer.stopDown();
//            }
//        }
//    }
//
//    /**
//     * 离开应用 压入后台或者退出应用
//     *
//     * @param activity
//     */
//    private void leaveApp(Activity activity) {
//        int time = 1;//默认一分钟
//        if(HbCodeUtils.getConfig()!=null&&HbCodeUtils.getConfig().getOffline_time()>0){
//            time = HbCodeUtils.getConfig().getOffline_time();
//        }
//        isRunInBackground = true;
//        downTimer = new DownTimer();
//        downTimer.setListener(this);
//        downTimer.startDown((time * 60) * 1000);
//    }
//
//
//    @Override
//    public void onTick(long millisUntilFinished) {
//        Timber.w("进入后台倒计时统计：" + (millisUntilFinished / 1000) + "S");
//    }
//
//    @Override
//    public void onFinish() {
//        isShowDlag = true;
//    }
//
//    private void updateVersion(Activity activity) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle("");
//        builder.setMessage("长时间未操作,请重新启动应用!");//提示内容
//        builder.setCancelable(false);
//        builder.setPositiveButton("重启", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                HbCodeUtils.exitLogin();
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("离开", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ArmsUtils.exitApp();
//                dialog.dismiss();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//    }
//
//    /**
//     * 是否在运行
//     */
//    boolean isRunning = true;
//
//    //由于CountDownTimer有一定的延迟，所以这里设置3400
//    private CountDownTimer countDownTimer;
//    private void initDownLoadInfo(){
//        if(isRunning){//默认运行
//            isRunning = false;//当运行之后不再重复添加倒计时
//            long showTime = 50000;
//            int time = 1;//默认一分钟
//            if(HbCodeUtils.getConfig()!=null&&HbCodeUtils.getConfig().getOffline_time()>0){
//                time = HbCodeUtils.getConfig().getOffline_time();
//            }
//            showTime = time*60*1000;//默认毫秒，数据返回的是一分钟
//            countDownTimer = new CountDownTimer(showTime, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    Timber.w("在线统计倒计时："+(millisUntilFinished/1000)+"S");
//                }
//                @Override
//                public void onFinish() {
//                    EventBus.getDefault().post("unoffline",EventBusTags.USER_UNOFFLINE);
//                    if(countDownTimer!=null){
//                        countDownTimer.cancel();
//                        countDownTimer= null;
//                        isRunning = true;//倒计时完了，重新开启倒计时
//                        initDownLoadInfo();
//                    }
//                }
//            };
//            countDownTimer.start();
//        }
//
//    }


}
