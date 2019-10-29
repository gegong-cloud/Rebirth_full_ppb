package com.google.android.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.R;
import com.google.android.app.MyApplication;
import com.google.android.mvp.model.api.service.LoginService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * Created by Administrator on 2017/12/25.
 */

public class StringUtils {


    public static String PHOTOSTR = "添加";
    private static final String SD_PATH = "/sdcard/ldx/pic/";
    private static final String IN_PATH = "/ldx/pic/";


    public static List<String> getSplit(String data) {
        List<String> dataList = new ArrayList<>();
        if (!TextUtils.isEmpty(data)) {
            String[] tempArr = data.split("\\,");
            if (tempArr != null && tempArr.length > 0) {
                for (String str : tempArr) {
                    if (!TextUtils.isEmpty(str) && !",".equals(str)) {
                        dataList.add(str);
                    }
                }
            }

        }
        if (dataList != null && dataList.size() > 0) {
            return dataList;
        } else {
            dataList.add(data);
        }
        return dataList;
    }

    public static List<String> getSplitFh(String data) {
        List<String> dataList = new ArrayList<>();
        if (!TextUtils.isEmpty(data)) {
            String[] tempArr = data.split("\\;");
            if (tempArr != null && tempArr.length > 0) {
                for (String str : tempArr) {
                    if (!TextUtils.isEmpty(str) && !";".equals(str)) {
                        dataList.add(str);
                    }
                }
            }

        }
        return dataList;
    }


    public static String listToString(List<String> data) {
        String returnStr = "";
        if (data != null && data.size() > 0) {
            for (String ins : data) {
                returnStr = returnStr + ins + ",";
            }
        }
        return returnStr;
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static String stringToLong(String strTime)
            throws ParseException {
        Date date = stringToDate(strTime, "yyyy-MM-dd"); // String类型转成date类型
        if (date == null) {
            return strTime;
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }
    }

    //服务端过来的utc时间显示
    public static String stringToShow(String strTime)
            throws ParseException {
        Date date = new Date((Long.parseLong(strTime) * 1000));
        if (date == null) {
            return strTime;
        } else {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
    }


    /**
     * @param strTime
     * @return 显示月日，小时分钟
     * @throws ParseException
     */
    public static String stringToShow_MMdd_HHmm(String strTime)
            throws ParseException {
        Date date = new Date((Long.parseLong(strTime) * 1000));
        if (date == null) {
            return strTime;
        } else {
            return new SimpleDateFormat("MM-dd HH:mm").format(date);
        }
    }

//    //服务端过来的utc时间显示
//    public static String stringToShow(String strTime)
//            throws ParseException {
//        Date date = stringToDate(strTime, "yyyy-MM-dd HH:mm:ss"); // String类型转成date类型
//        if (date == null) {
//            return strTime;
//        } else {
//            return new SimpleDateFormat("yyyy-MM-dd").format(date);
//        }
//    }

    //服务端过来的utc时间显示
    public static String stringToShowMMdd(String strTime)
            throws ParseException {
        Date date = new Date((Long.parseLong(strTime) * 1000));
//        Date date = stringToDate(strTime, "yyyy-MM-dd HH:mm:ss"); // String类型转成date类型
        if (date == null) {
            return strTime;
        } else {
            return new SimpleDateFormat("MM-dd").format(date);
        }
    }

    //服务端过来的utc时间显示
    public static String stringToShowMMdd(long strTime)
            throws ParseException {
        Date date = new Date((strTime));
//        Date date = stringToDate(strTime, "yyyy-MM-dd HH:mm:ss"); // String类型转成date类型
        if (date == null) {
            return "";
        } else {
            return new SimpleDateFormat("MM-dd").format(date);
        }
    }


    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static List<String> getSplitBy$(String data) {
        List<String> dataList = new ArrayList<>();
        if (!TextUtils.isEmpty(data)) {
            String[] tempArr = data.split("\\$");
            if (tempArr != null && tempArr.length > 0) {
                for (String str : tempArr) {
                    if (!TextUtils.isEmpty(str) && !"$".equals(str)) {
                        dataList.add(str);
                    }
                }
            }
        }
        return dataList;
    }

    /**
     * 判断SDCard是否存在,并可写
     *
     * @return
     */
    public static boolean checkSDCard() {
        String flag = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(flag);
    }


    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }


    private static String getSortkey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        } else
            return "#";   //获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
    }

    public static String replaceToXing(String telStr) {
        if (!TextUtils.isEmpty(telStr) && telStr.length() >= 11) {
            return telStr.substring(0, 3) + "****" + telStr.substring(7, telStr.length());
        }
        return telStr;
    }


    public static ArrayList<String> removeAddStr(ArrayList<String> dataList) {
        ArrayList<String> returnList = new ArrayList<>();
        if (dataList != null && dataList.size() > 0) {
            for (String tempStr : dataList) {
                if (PHOTOSTR.equals(tempStr)) {
                    continue;
                } else {
                    returnList.add(tempStr);
                }
            }
        }
        return returnList;
    }

    /**
     * @return 返回年
     */
    public static String getCurrentYearString() {
        return new SimpleDateFormat("yyyy").format(System.currentTimeMillis());
    }

    /**
     * @return 返回当前月
     */
    public static String getCurrentMothString() {
        return new SimpleDateFormat("MM").format(System.currentTimeMillis());
    }

    /**
     * @return 返回当前日
     */
    public static String getCurrentDayString() {
        return new SimpleDateFormat("dd").format(System.currentTimeMillis());
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static List<String> getHouseType() {
        return null;
    }


    public static boolean isToday(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {//格式化为相同格式
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static void stringBorrw(Activity activity, String urlStr) {
        if (!TextUtils.isEmpty(urlStr)) {
            Uri uri = Uri.parse(urlStr);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }
    }


    /**
     * @return 10位的时间戳
     */
    public static String getTenNumberTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }

    public static String encode(String s) {
        return java.net.URLEncoder.encode(s);
    }

    public static String replaceBfh(String resouceData) {
        if (!TextUtils.isEmpty(resouceData)) {
            return resouceData.replace("%", "％");
        }
        return resouceData == null ? "" : resouceData;
    }

    /**
     * @param longlat
     * @return 获取偏移位置
     */
    public static Double getLong(double longlat) {
        if (longlat > 0) {
//            longlat = longlat-0.00525;//对应半径500
            longlat = longlat - 0.01025;//对应半径1000
            return longlat;
        }
        return 0.0;
    }

    /**
     * @param longlat
     * @return 获取偏移位置
     */
    public static Double getLong(double longlat, float rangeSize) {
        double coefficient = 0.00001025f;
        double subtracted = 0.01025f;
        if (rangeSize <= 1000) {
            subtracted = 0.01025f;
        } else {
            subtracted = coefficient * rangeSize;
        }
        if (longlat > 0) {
//            longlat = longlat-0.00525;//对应半径500
            longlat = longlat - subtracted;//对应半径1000
            return longlat;
        }
        return longlat;
    }


    /**
     * @return 设置地图缩放级别
     */
    public static float getZoomSize(float rangeSize) {
        float zoomSize = 14.6f;
        if (rangeSize <= 1000) {
            zoomSize = 14.6f;
        } else {
            if (rangeSize > 8000 && rangeSize <= 10000) {//2000-10000米
                zoomSize = 14.6f - (1.1f + ((rangeSize - 2000) / 100) * 0.030f);
            } else if (rangeSize > 6600 && rangeSize <= 8000) {//2000-10000米
                zoomSize = 14.6f - (1.1f + ((rangeSize - 2000) / 100) * 0.035f);
            } else if (rangeSize > 5400 && rangeSize <= 6600) {//2000-10000米
                zoomSize = 14.6f - (1.1f + ((rangeSize - 2000) / 100) * 0.040f);
            } else if (rangeSize >= 4000 && rangeSize <= 5400) {//2000-10000米
                zoomSize = 14.6f - (1.1f + ((rangeSize - 2000) / 100) * 0.045f);
            } else if (rangeSize > 2000 && rangeSize < 4000) {//2000-10000米
                zoomSize = 14.6f - (1.1f + ((rangeSize - 2000) / 100) * 0.055f);
            } else if (rangeSize <= 2000) {//2000米以内
                zoomSize = 14.6f - ((rangeSize - 1000.0f) / 100) * 0.12f;
            } else {
                zoomSize = 8.0f;
            }
        }
        return zoomSize;
    }


    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view  控件view
     * @param event 焦点位置
     * @return 是否隐藏
     */
    public static void hideKeyboard(MotionEvent event, View view,
                                    Activity activity) {
        try {
            if (view != null && view instanceof EditText
            ) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 隐藏键盘
     *
     * @param view
     * @param activity
     */
    public static void hideKeyboard(EditText view,
                                    Activity activity) {
        try {
            int[] location = {0, 0};
            view.getLocationInWindow(location);
            // 隐藏键盘
            IBinder token = view.getWindowToken();
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //根据秒数转化为时分秒   00:00:00
    public static String getTime(int second) {
        if (second < 10) {
            return "00:0" + second;
        }
        if (second < 60) {
            return "00:" + second;
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }
            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + minute + ":0" + second;
            }
            return "0" + hour + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour + minute + ":0" + second;
        }
        return hour + minute + ":" + second;
    }


    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmapSp(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        FileOutputStream fos = null;
        try {
            filePic = new File(savePath + "spimg.jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return filePic.getAbsolutePath();
    }

    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        FileOutputStream fos = null;
        try {
            filePic = new File(savePath + System.currentTimeMillis() + "_tempImg.jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return filePic.getAbsolutePath();
    }


    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmapShare(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        FileOutputStream fos = null;
        try {
            filePic = new File(savePath + "share_qrcode.jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return filePic.getAbsolutePath();
    }


    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmapShare1(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        FileOutputStream fos = null;
        try {
            filePic = new File(savePath + "user_share_qrcode.jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return filePic.getAbsolutePath();
    }


    /**
     * @param context
     * @return 生成唯一识别码, android id 和build 信息
     */
    public static String buildDeviceUUID(Context context) {
        String androidId = getAndroidId(context);
        if ("9774d56d682e549c".equals(androidId)) {
            return getUniquePsuedoID();
        }
//        return getUniquePsuedoID();
        return new UUID(androidId.hashCode(), getBuildInfo().hashCode()).toString();
    }

    /**
     * @param context
     * @return android_id
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * @return android.os.Build类包含了很多设备信息，包括系统版本号、手机型号等硬件软件信
     */
    public static String getBuildInfo() {
        StringBuffer buildSB = new StringBuffer();
        buildSB.append(Build.BRAND).append("/");
        buildSB.append(Build.PRODUCT).append("/");
        buildSB.append(Build.DEVICE).append("/");
        buildSB.append(Build.ID).append("/");
        buildSB.append(Build.VERSION.INCREMENTAL);
        return buildSB.toString();
    }


    /**
     * @return 随机一个数字
     */
    public static int randomInt(int size) {
        // 数字
        Random random = new Random();
        int[] ranInt = new int[size];
        for (int i = 0; i < size; i++) {
            ranInt[i] = i;
        }
        return random.nextInt(ranInt.length);
    }

    /**
     * @param phoneStr
     * @return 替换手机号码为****
     */
    public static String repleacePhone(String phoneStr) {
        if (!TextUtils.isEmpty(phoneStr) && phoneStr.length() > 10) {
            phoneStr = phoneStr.substring(0, 3) + "****" + phoneStr.substring(7, 11);
        }
        return phoneStr;
    }


    /**
     * 校验获取手机号 return boolean
     */
    public static boolean checkPhoneNum(String phonenum) {
        //return Pattern.compile("^[1][0-9]{10}$").matcher(phonenum).matches();
        return Pattern.compile("^[0-9]*$").matcher(phonenum).matches();
        // return  true;
    }

    public static String getBaseUrl() {
        String imgUrl = TextConstant.APP_DOMAIN;
        if (!TextUtils.isEmpty(MyApplication.getsInstance().getImgUrl())) {
            imgUrl = MyApplication.getsInstance().getImgUrl();
        }
//        makeTextCenter(MyApplication.getsInstance(),imgUrl);
        return "";
    }

    /**
     * @return 图片地址是否有flag
     */
    public static boolean isFlag(String imgUrl) {
        return imgUrl.contains("s=noencrypt");
    }

    //获取hashCode
    public static String getStrHashCode(String tempStr) {
        if (!TextUtils.isEmpty(tempStr)) {
            return "" + tempStr.hashCode();
        }
        return "" + (("" + System.currentTimeMillis()).hashCode());
    }


    public static final int XOR_CONST = 0X19; //密钥

    /**
     * @param mAppComponent 网络请求控件
     * @param img           需要显示的图片容器
     * @param context
     * @param url           图片地址
     * @param imgId         图片的唯一id
     */
    public static void showImgView(AppComponent mAppComponent, ImageView img, Context context, String url, String imgId) {
        if (context == null) {
            return;
        }
        if (isFlag(url)) {//这里判断是判断是否是不解密图片
            try {
                Glide.with(context).load(url).into(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            String path = getSavePath(context, url, imgId);
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    try {
                        Timber.e("lhw---" + path);
                        Glide.with(context).load(path).into(img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            try {
                mAppComponent.repositoryManager().obtainRetrofitService(LoginService.class).getImg(url)
                        .subscribeOn(Schedulers.io())
                        .map(new Function<ResponseBody, String>() {
                            @Override
                            public String apply(ResponseBody responseBody) throws Exception {
                                if (responseBody == null || responseBody.contentLength() <= 0) {
                                    return "";
                                }
                                try {
                                    if (getImgSuffix(url).contains(".jpg")) {
                                        return tempBitmap(context, getBitmapFromInputStream(responseBody.byteStream()), url, imgId);
                                    } else {
                                        return saveGif(context, responseBody.byteStream(), url, imgId);
                                    }
                                } finally {
                                    if (responseBody != null) {
                                        responseBody.close();
                                    }
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String bitmap) {
                                if (!TextUtils.isEmpty(bitmap)
                                        && context != null) {
                                    try {
                                        Glide.with(context).load(bitmap).into(img);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        img.setImageResource(R.drawable.cloud_default_img);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                try {
                                    img.setImageResource(R.drawable.cloud_default_img);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    img.setImageResource(R.drawable.cloud_default_img);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    /**
     * @param is
     * @return 获取下载图片的解密的bitmap
     */
    public static Bitmap getBitmapFromInputStream(InputStream is) {
        Bitmap bitmap = null;
        List<Byte> list = new ArrayList();
        try {
            int read;
            while ((read = is.read()) > -1) {
                read = read ^ XOR_CONST;
                list.add((byte) read);
            }
            byte[] arr = new byte[list.size()];
            int i = 0;
            for (Byte btyeItem : list) {
                arr[i++] = btyeItem;
            }
            if (arr != null && arr.length > 0) {
                bitmap = BitmapFactory.decodeByteArray(arr, 0, list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }


    /**
     * @param context
     * @param is
     * @param imgUrl
     * @param imgId
     * @return 保存gif到本地
     */
    public static String saveGif(Context context, InputStream is, String imgUrl, String imgId) {
        List<Byte> list = new ArrayList();
        String savePath = getSavePath(context) + "_" + imgId + getImgSuffix(imgUrl);
        //读写文件
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(savePath);
            FileChannel fileChannel = out.getChannel();
            //下面是解密
            int read;
            while ((read = is.read()) > -1) {
                read = read ^ XOR_CONST;
                list.add((byte) read);
            }
            byte[] arr = new byte[list.size()];
            int i = 0;
            for (Byte btyeItem : list) {
                arr[i++] = btyeItem;
            }
            //保存gif文件
            fileChannel.write(ByteBuffer.wrap(arr)); //将字节流写入文件中
            fileChannel.force(true);//强制刷新
            fileChannel.close();
            return savePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     *
     * @param context
     * @return 获取保存文件夹地址
     */
    public static String getSavePath(Context context) {
        File externalFilesDir = context.getExternalFilesDir("Caches");
        String savePath;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (externalFilesDir.exists()) {
                savePath = externalFilesDir.getPath();
            } else {
                savePath = SD_PATH;
            }
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        return savePath;
    }

    /**
     * @param context
     * @param imgId   图片id
     * @return
     */
    public static String getSavePath(Context context,  String imgId) {
        return (getSavePath(context) + "_" + imgId + ".jpg");
    }

    /**
     * @param context
     * @param imgUrl  图片地址
     *  @param imgId   图片id
     * @return 获取保存图片地址
     */
    public static String getSavePath(Context context,String imgUrl, String imgId) {
        return (getSavePath(context) + "_" + imgId + getImgSuffix(imgUrl));
    }

    /**
     * @param imgUrl
     * @return 获取图片后缀
     */
    public static String getImgSuffix(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            String[] tempUrls = imgUrl.split("\\.");
            if (tempUrls != null && tempUrls.length > 0 && !TextUtils.isEmpty(tempUrls[tempUrls.length - 1])) {
                if (tempUrls[tempUrls.length - 1].contains("gif") || tempUrls[tempUrls.length - 1].contains("GIF")) {
                    return ".gif";
                } else {
                    return ".jpg";
                }
            }
        }
        return ".jpg";
    }


    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String tempBitmap(Context context, Bitmap mBitmap, String imgUrl, String imgId) {
        if (mBitmap == null) {
            return "";
        }

        File filePic;
        String savePath = getSavePath(context);
        FileOutputStream fos = null;
        try {
            filePic = new File(savePath + "_" + imgId + getImgSuffix(imgUrl));
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return filePic.getAbsolutePath();
    }




    public static String getNoLast(String cardNo) {
        if (!TextUtils.isEmpty(cardNo) && cardNo.length() > 4) {
            return cardNo.substring(cardNo.length() - 4, cardNo.length());
        }
        return cardNo;
    }

    public static String replaceXieGang(String cardNo) {
        if (!TextUtils.isEmpty(cardNo)) {
            return cardNo.replaceAll("\\\\", "");
        }
        return cardNo;
    }

    /**
     * @param pageAllNumber
     * @return 获取分页总数
     */
    public static int getPageAll(int pageAllNumber) {
        int intNumber = 0;
        if ((pageAllNumber / TextConstant.PAGE_SIZE) > 0) {
            intNumber = pageAllNumber / TextConstant.PAGE_SIZE;
            if (pageAllNumber % TextConstant.PAGE_SIZE > 0) {
                intNumber = intNumber + 1;
                return intNumber;
            } else {
                return intNumber;
            }
        }
        return 0;
    }


    static public Toast mToast;

    /**
     * 单例 toast
     *
     * @param string
     */
    public static void makeTextCenter(Context context, String string) {
        if (mToast == null) {
            mToast = Toast.makeText(context, string, Toast.LENGTH_LONG);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(string);
        mToast.show();
    }


    /**
     * 打开微信并跳入到二维码扫描页面
     *
     * @param context
     */
    public static void openWeixinToQE_Code(Context context) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            context.startActivity(intent);
        } catch (Exception e) {
            ArmsUtils.makeText(context, "没有安装微信");
        }
    }


    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

}
