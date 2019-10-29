package com.google.android.app.utils.sv;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;


/**
 * 设备信息
 */
public class DeviceInformationHelper {

    //保存文件的路径
    private static final String CACHE_IMAGE_DIR = "alibaba/cache/devices";
    //保存的文件 采用隐藏文件的形式进行保存
    private static final String DEVICES_FILE_NAME = ".ALIBABA";


    /**
     * 获取设备唯一码
     *
     * @return
     */
    public static String imei2(Context context) {
        //读取保存的在sd卡中的唯一标识符
        String imeisString = readDeviceID(context);
        if (!TextUtils.isEmpty(imeisString)) {
            return imeisString;
        }
        //用于生成最终的唯一标识符
        StringBuffer s = new StringBuffer();
        //如果以上搜没有获取相应的则自己生成相应的UUID作为相应设备唯一标识符
        String deviceId = "";
        if (s == null || s.length() <= 0) {
            UUID uuid = UUID.randomUUID();
            deviceId = uuid.toString().replace("-", "");
            s.append(deviceId);
        }
        try {
            //获取设备的MACAddress地址 去掉中间相隔的冒号
//            deviceId = getLocalMac().replace(":", "");//原方法
            deviceId = getMacAddressFromIp(context).replace(":", "");//新方法
            s.append(deviceId);
            //获取手机设备序列号
            String deviceSN = getDeviceSN2();
            s.append(deviceSN);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
        String md5 = getMD5(s.toString(), false);
        if (s.length() > 0) {
            //持久化操作, 进行保存到SD卡中
            saveDeviceID(md5, context);
        }
        return md5;
    }

    /**
     * 获取序列号
     *
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    private static String getDeviceSN2() {
        String serial = null;
        try {
            List<Integer> radomListy = RandomUtil.createRandomList(6,
                    0, RandomUtil.radomList.length - 1);
            String radomsStrng = "";
            for (int i = 0; i < radomListy.size(); i++) {
                radomsStrng += RandomUtil.radomList[radomListy.get(i)];
            }
            UUID uuid = UUID.randomUUID();
            serial = uuid.toString().replace("-", "");
            serial += radomsStrng;
            serial = Base64.encode(serial.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }


    /**
     * 读取固定的文件中的内容,这里就是读取sd卡中保存的设备唯一标识符
     *
     * @param context
     * @return
     */
    private static String readDeviceID(Context context) {
        File file = getDevicesDir(context);
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int i;
            while ((i = in.read()) > -1) {
                buffer.append((char) i);
            }
            in.close();
            isr.close();
            fis.close();
            return buffer.toString();
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * 保存 内容到 SD卡中,  这里保存的就是 设备唯一标识符
     *
     * @param str
     * @param context
     */
    public static void saveDeviceID(String str, Context context) {
        File file = getDevicesDir(context);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str);
            out.close();
            fos.close();
        } catch (IOException e) {

        }
    }




    /**
     * 统一处理设备唯一标识 保存的文件的地址
     *
     * @param context
     * @return
     */
    private static File getDevicesDir(Context context) {
        File mCropFile = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cropdir = new File(Environment.getExternalStorageDirectory(), CACHE_IMAGE_DIR);
            if (!cropdir.exists()) {
                cropdir.mkdirs();
            }
            mCropFile = new File(cropdir, DEVICES_FILE_NAME); // 用当前时间给取得的图片命名
        } else {
            File cropdir = new File(context.getFilesDir(), CACHE_IMAGE_DIR);
            if (!cropdir.exists()) {
                cropdir.mkdirs();
            }
            mCropFile = new File(cropdir, DEVICES_FILE_NAME);
        }
        return mCropFile;
    }


    /**
     * 获取设备MAC 地址
     *
     * @param context
     * @return
     */
    public static String getMacAddressFromIp(Context context) {
        String mac_s = "";
        StringBuilder buf = new StringBuilder();
        try {
            byte[] mac;
            List<Integer> radomListy = RandomUtil.createRandomList(9,
                    0, RandomUtil.radomList.length - 1);
            String radomsStrng = "";
            for (int i = 0; i < radomListy.size(); i++) {
                radomsStrng += RandomUtil.radomList[radomListy.get(i)];
            }
            mac = radomsStrng.getBytes();
            if (mac == null) {
                String dev = Build.CPU_ABI +
                        Build.DISPLAY +
                        Build.MANUFACTURER +
                        Build.ID +
                        Build.HOST +
                        Build.MODEL;
                mac = dev.getBytes();
            }
            if (mac != null) {
                for (byte b : mac) {
                    buf.append(String.format("%02X:", b));
                }
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            mac_s = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac_s;
    }

    /**
     * 对挺特定的 内容进行 md5 加密
     *
     * @param message   加密明文
     * @param upperCase 加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    public static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] input = message.getBytes();

            byte[] buff = md.digest(input);

            md5str = bytesToHex(buff, upperCase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    public static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }
}
