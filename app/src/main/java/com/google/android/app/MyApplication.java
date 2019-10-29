package com.google.android.app;


import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.jess.arms.base.App;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.base.delegate.AppDelegate;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.component.AppComponent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.android.app.utils.TextConstant;
import io.fabric.sdk.android.Fabric;


/**
 * Created by Administrator on 2017/11/22.
 */

public class MyApplication extends MultiDexApplication implements App {

//    static {//static 代码段可以防止内存泄露
//        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
//            @Override
//            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(Color.CYAN, android.R.color.primary_text_dark);//全局设置主题颜色
////                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
//                return new MaterialHeader(context) ;
//            }
//        });
//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
//            }
//        });
//    }
    private String imgUrl = TextConstant.APP_DOMAIN;

    private AppLifecycles mAppDelegate;


    private String UUID = "";

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (isMainProcess()) {//初始化openinstall
//            OpenInstall.init(this);
//        }
        this.mAppDelegate.onCreate(this);
        //BUG 监听插件
//        initBugly();
        Fabric.with(this, new Crashlytics());
    }




//    private void initBugly() {
//        Context context = getApplicationContext();
//// 获取当前包名
//        String packageName = context.getPackageName();
//// 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
//// 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//// 初始化Bugly
////        CrashReport.initCrashReport(context, "382001d844", false, strategy);//正式服
//        CrashReport.initCrashReport(context, "9d2b339f32", false, strategy);//测试服
//    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(this);
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return ((App) mAppDelegate).getAppComponent();
    }


    public static MyApplication sInstance;


    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static void setsInstance(MyApplication sInstance) {
        MyApplication.sInstance = sInstance;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }



    private boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public AppLifecycles getmAppDelegate() {
        return mAppDelegate;
    }

    public void setmAppDelegate(AppLifecycles mAppDelegate) {
        this.mAppDelegate = mAppDelegate;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
