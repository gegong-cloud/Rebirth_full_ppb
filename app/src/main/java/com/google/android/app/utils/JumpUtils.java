package com.google.android.app.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.jess.arms.utils.ArmsUtils;

import timber.log.Timber;

public class JumpUtils {




    /**
     *
     * @param activity
     * @param url 点击广告下载的时候要把链接复制到剪切板
     */
    public static void copyUrlToBord(Activity activity, String url) {
        if (activity != null) {
            if (!TextUtils.isEmpty(url)) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", url);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ArmsUtils.makeText(activity, "推广链接已复制到粘贴板,快去分享吧");
            }
        }
    }


    /**
     *
     * @param activity 分享推广链接
     */
    public static void share(Activity activity) {
        if (activity != null) {
            if (!TextUtils.isEmpty(HbCodeUtils.getSharePath())) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", HbCodeUtils.getSharePath());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ArmsUtils.makeText(activity, "推广链接已复制到粘贴板,快去分享吧");
            }
        }
    }


    /**
     *
     * @param activity
     * @param url 点击广告下载的时候要把链接复制到剪切板
     */
    public static void copyUrlOpenWx(Activity activity, String url) {
        if (activity != null) {
            if (!TextUtils.isEmpty(url)) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", url);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ArmsUtils.makeText(activity, "已复制到粘贴板");
                openWx(activity);
            }
        }
    }


    /**
     * @param activity
     * @return 获取剪切板内容
     */
    public static String getClipboardText(Activity activity) {
        String returnText = "";
        if (activity != null) {
            //获取剪贴板管理器：
            try{
                ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                //判断剪切版时候有内容
                if (!clipboardManager.hasPrimaryClip())
                    return returnText;
                ClipData clipData = clipboardManager.getPrimaryClip();
                //获取 ClipDescription
                ClipDescription clipDescription = clipboardManager.getPrimaryClipDescription();
                if(!(clipData!=null&&clipData.getItemCount()>0)){
                    return "";
                }
                //获取 text
                returnText = !TextUtils.isEmpty(clipData.getItemAt(0).getText())?clipData.getItemAt(0).getText().toString():"";
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }
        Timber.e("lhw_copy="+returnText);
        return returnText;
    }



    private static void openWx(Context context){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ArmsUtils.makeText(context,"检查到您手机没有安装微信，请安装后使用该功能");
        }

    }


}
