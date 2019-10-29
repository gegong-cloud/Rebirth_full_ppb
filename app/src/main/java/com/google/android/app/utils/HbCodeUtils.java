package com.google.android.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.jess.arms.base.App;
import com.google.android.app.MyApplication;
import com.google.android.mvp.model.back_entity.BaseUrl;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.back_entity.UserEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HbCodeUtils {
    public final static Integer PAGESIZE = 20;


    public final static SharedPreferences PREFERENCES;
    private final static SharedPreferences.Editor EDITOR;
    public static final MyApplication CONTEXT = MyApplication
            .getsInstance();

    /**
     * 存的文件名称
     */
    private final static String COMMON_PREFERENCE_NAME = "team_zero.xml";

    static {
        PREFERENCES = CONTEXT.getSharedPreferences(COMMON_PREFERENCE_NAME,
                Context.MODE_APPEND
        );
        EDITOR = PREFERENCES.edit();
    }

    public static void setString(String key, String value) {
        EDITOR.putString(key, value);
        EDITOR.apply();
    }


    @SuppressWarnings("unused")
    public static void setInt(String key, int value) {
        EDITOR.putInt(key, value);
        EDITOR.apply();
    }


    public static void setNetUrl(String deviceId) {
        setString("netUrl", deviceId);
    }

    public static String getNetUrl() {

        return PREFERENCES.getString("netUrl", "");
    }


    public static void setSpare(String deviceId) {
        setString("spare", deviceId);
    }

    public static String getSpare() {
        return PREFERENCES.getString("spare", "");
    }



    public static void setScrollNumber(String deviceId) {
        setString("scrollNumber", deviceId);
    }

    public static String getScrollNumber() {

        return PREFERENCES.getString("scrollNumber", "");
    }

    /**
     *
     * @param deviceId  广告图片本地地址
     */
    public static void setSpImgBg(String deviceId) {
        setString("spImgBg", deviceId);
    }

    public static String getSpImgBg() {
        return PREFERENCES.getString("spImgBg", "");
    }




    /**
     *
     * @param deviceId  图片服务器相对地址
     */
    public static void setImgAddress(String deviceId) {
        setString("imgAddress", deviceId);
    }

    public static String getImgAddress() {
        return PREFERENCES.getString("imgAddress", "");
    }







    public static void setAndroidDevice(String deviceId) {
        setString("androidDevice", deviceId);
    }

    public static String getAndroidDevice() {
        return PREFERENCES.getString("androidDevice", "");
    }


    public static void setSharePath(String deviceId) {
        setString("sharePath", deviceId);
    }

    public static String getSharePath() {
        return PREFERENCES.getString("sharePath", "");
    }


    public static void setSharePathAll(String deviceId) {
        setString("sharePathAll", deviceId);
    }

    public static String getSharePathAll() {
        return PREFERENCES.getString("sharePathAll", "");
    }


    public static void setDeviceId(String deviceId) {
        setString("deviceId", deviceId);
    }

    public static String getDeviceId() {
        return PREFERENCES.getString("deviceId", "");
    }

    public static void setToken(String deviceId) {
        setString("token", deviceId);
    }

    public static String getToken() {
        return PREFERENCES.getString("token", "");
    }

    public static void setUserType(String deviceId) {
        setString("UserType", deviceId);
    }

    public static String getUserType() {
        return PREFERENCES.getString("UserType", "");
    }




    public static void setWxAppId(String deviceId) {
        setString("wxappId", deviceId);
    }

    public static String getWxAppId() {
        return PREFERENCES.getString("wxappId", "");
    }





    /**
     *
     * @param adStarEntity 客服信息
     */
    public static void setServiceAdd(ScrollMsg adStarEntity) {
        setString("serviceAddress", ((App) MyApplication.getsInstance()).getAppComponent().gson().toJson(adStarEntity));
    }

    public static ScrollMsg getServiceAdd() {
        if (!TextUtils.isEmpty(PREFERENCES.getString("serviceAddress", ""))) {
            return ((App) MyApplication.getsInstance()).getAppComponent().gson().fromJson(PREFERENCES.getString("serviceAddress", ""), ScrollMsg.class);
        }
        return null;
    }


    /**
     *
     * @param adStarEntity app配置信息
     */
    public static void setUserInfo(UserEntity adStarEntity) {
        setString("userInfo", ((App) MyApplication.getsInstance()).getAppComponent().gson().toJson(adStarEntity));
    }

    public static UserEntity getUserInfo() {
        if (!TextUtils.isEmpty(PREFERENCES.getString("userInfo", ""))) {
            return ((App) MyApplication.getsInstance()).getAppComponent().gson().fromJson(PREFERENCES.getString("userInfo", ""), UserEntity.class);
        }
        return null;
    }



    /**
     *
     * @param adStarEntity app配置信息
     */
    public static void setAdvEntity(HomeAdv adStarEntity) {
        setString("advEntity", ((App) MyApplication.getsInstance()).getAppComponent().gson().toJson(adStarEntity));
    }

    public static HomeAdv getAdvEntity() {
        if (!TextUtils.isEmpty(PREFERENCES.getString("advEntity", ""))) {
            return ((App) MyApplication.getsInstance()).getAppComponent().gson().fromJson(PREFERENCES.getString("advEntity", ""), HomeAdv.class);
        }
        return null;
    }


    /**
     *
     * @param adStarEntity app配置信息
     */
    public static void setNewUrl(List<BaseUrl> adStarEntity) {
        setString("newUrl", ((App) MyApplication.getsInstance()).getAppComponent().gson().toJson(adStarEntity));
    }

    public static List<BaseUrl> getNewUrl() {
        if (!TextUtils.isEmpty(PREFERENCES.getString("newUrl", ""))) {
            return ((App) MyApplication.getsInstance()).getAppComponent().gson().fromJson(PREFERENCES.getString("newUrl", ""), new TypeToken<List<BaseUrl>>(){}.getType());
        }
        return null;
    }





    /**
     *校验本地是否有token
     */
    public static boolean isEmptyToken() {
        String token=HbCodeUtils.getToken();
        return TextUtils.isEmpty(token);
    }


    public static void shareText(Context context, String title, String content) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, "分享到..."));
    }

    public static void shareImage(Context context, String url) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到..."));
    }

    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(checkHttp(url)));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String checkHttp(String url){
        url = replaceAir(url);
        return (url.startsWith("http://")||url.startsWith("https://"))?url:("http://"+url);
    }

    public static String replaceAir(String url){
        return !TextUtils.isEmpty(url)?(url.replaceAll(" ","")):"";
    }

    public static void selectImage(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), requestCode);
    }


    public static String cacheImageFromContentResolver(Context context, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            String path = context.getExternalCacheDir().getPath()
                    + File.separator
                    + System.currentTimeMillis() + ".png";
            File bkFile = new File(path);
            if (!bkFile.exists()) {
                bkFile.createNewFile();
                FileOutputStream out = new FileOutputStream(bkFile);
                byte[] b = new byte[1024 * 4];// 5KB
                int len;
                while ((len = is.read(b)) != -1) {
                    out.write(b, 0, len);
                }
                out.flush();
                is.close();
                out.close();
                return (bkFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String removeP(String html) {
        String result = html;
        if (result.contains("<p>") && result.contains("</p>")) {
            result = result.replace("<p>", "");
            result = result.replace("</p>", "<br>");
            if (result.endsWith("<br>")) {
                result = result.substring(0, result.length() - 4);
            }
        }
        return result;
    }


    /**
     * 手机号正则表达式
     **/
    public final static String MOBLIE_PHONE_PATTERN = "^((16[0-9])|(19[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$";
//    public final static String MOBLIE_PHONE_PATTERN = "^((1[3-9][0-9]))\\d{8}$";

    /**
     * 通过正则验证是否是合法手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isMobile(String phoneNumber) {
        Pattern p = Pattern.compile(MOBLIE_PHONE_PATTERN);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }


    public static void exitApp() {
        HbCodeUtils.setSharePath(null);//去掉用户分享链接
    }

    public static void exitLogin() {
//        HbCodeUtils.setUserInfo(null);
//        HbCodeUtils.setToken(null);
//        CONTEXT.startActivity(new Intent(CONTEXT, LoginActivity.class)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        );
    }

    public static void exitLogin1() {
//        HbCodeUtils.setUserInfo(null);
        HbCodeUtils.setToken(null);
    }


}
