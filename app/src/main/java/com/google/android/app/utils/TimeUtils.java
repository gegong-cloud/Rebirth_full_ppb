package com.google.android.app.utils;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具
 * Created by tly on 2017/7/30.
 */
public class TimeUtils {

    public static String timeFormat(long timeMillis, String pattern) {

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        /**
         * php 返回的秒数
         */
        Date date=new Date(timeMillis*1000);
        return format.format(date);
    }

    public static String formatPhotoDate(long time) {
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatPhotoDate(String path) {
        File file = new File(path);
        if (file.exists()) {
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }

    public static Long getTodayBeginTime() {
        //获取当天零点时间
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * 毫秒数转时分秒  00:00:00
     * 
     * @param time
     * @return
     */
	public  static String frptTime(long time){
		long all_s=time/1000;
		long d=all_s/60/60/24;
		long h=(all_s-d*24*60*60)/60/60;
		long m=(all_s-h*60*60-d*24*60*60)/60;
		long s=all_s-d*24*60*60-h*60*60-m*60;
		//String str_d=d>0?d+"天":"";
		String str_h=h>0||d>0?h>9?h+":":"0"+h+":":"00:";
		String str_m=m>0||h>0||d>0?m>9?m+":":"0"+m+":":"00:";
		String str_s=s>0||m>0||h>0||d>0?s>9?s+"":"0"+s:"00";
		return str_h+str_m+str_s;
	}
}
