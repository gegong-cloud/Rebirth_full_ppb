package com.google.android.app.utils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class TextConstant {

    public static String RequestSuccess = "0";//当前服务器

    public static int PAGE_SIZE = 20;//页码大小
    public static int PAGE_SIZE_MH = 3;//页码大小


    public  static String APP_DOMAIN_PRE = "http://";
    public  static String APP_DOMAIN1_PRE = "http://";
    public  static String APP_DOMAIN2_PRE = "http://";
    public  static String APP_DOMAIN3_PRE = "http://";
    public  static String APP_SPARE_PRE = "http://";


    public  static String APP_DOMAIN_ROOT = "api";
    public  static String APP_DOMAIN1_ROOT = "api";
    public  static String APP_DOMAIN2_ROOT = "api";
    public  static String APP_DOMAIN3_ROOT = "api";
    public  static String APP_SPARE_ROOT = "api";

    public  static String APP_DOMAIN_MIDDLE= "jufu552";
    public  static String APP_DOMAIN1_MIDDLE= "jufu552";
    public  static String APP_DOMAIN2_MIDDLE = "hpysi";
    public  static String APP_DOMAIN3_MIDDLE = "wf781";
    public  static String APP_SPARE_MIDDLE = "zic4m";


    public  static String APP_DOMAIN_SUFFIX= "com";
    public  static String APP_DOMAIN1_SUFFIX= "xyz";
    public  static String APP_DOMAIN2_SUFFIX = "xyz";
    public  static String APP_DOMAIN3_SUFFIX = "xyz";
    public  static String APP_SPARE_SUFFIX = "xyz";
    //测试服
//    public  static String APP_DOMAIN = "http://test.k0fahc1.top";
    //正式服
    public  static String APP_DOMAIN = APP_DOMAIN_PRE+APP_DOMAIN_ROOT+"."+APP_DOMAIN_MIDDLE+"."+APP_DOMAIN_SUFFIX+"/";
    public  static String APP_DOMAIN1 = APP_DOMAIN1_PRE+APP_DOMAIN1_ROOT+"."+APP_DOMAIN1_MIDDLE+"."+APP_DOMAIN1_SUFFIX+"/";
    public  static String APP_DOMAIN2 = APP_DOMAIN2_PRE+APP_DOMAIN2_ROOT+"."+APP_DOMAIN2_MIDDLE+"."+APP_DOMAIN2_SUFFIX+"/";
    public  static String APP_DOMAIN3 = APP_DOMAIN3_PRE+APP_DOMAIN3_ROOT+"."+APP_DOMAIN3_MIDDLE+"."+APP_DOMAIN3_SUFFIX+"/";



    public static String APP_SPARE =  APP_SPARE_PRE+APP_SPARE_ROOT+"."+APP_SPARE_MIDDLE+"."+APP_SPARE_SUFFIX+"/";;//备用域名


    /**
     * 内置的userid，可更改
     *
     */
//    public static String BUILT_IN_ANDROID = "100008";//默认了为空，取openinstall
//    public static String BUILT_IN_ANDROID = "10";//默认了为空，取openinstall




    public static List<String> API_LIST = new ArrayList<String>();

    public static String getBaseUrl() {
        if (API_LIST.size() <= 0) {
            API_LIST.add(APP_DOMAIN);
            API_LIST.add(APP_DOMAIN1);
            API_LIST.add(APP_DOMAIN2);
            API_LIST.add(APP_DOMAIN3);
            Timber.e("lhw list <= 0");
        }
        Timber.e("lhw list >= 0");
        return API_LIST.get(0);
    }

    public static void addNewBase(String url){
        if(API_LIST!=null){
            if(API_LIST.contains(url)){
                return;
            }else{
                if(url.contains("http")){

                }else{
                    url = "http://"+url;
                }
                API_LIST.add(0,url);
            }
        }
    }

}
