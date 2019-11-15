package com.tdpro.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @About 时间工具类
 * @Author jy
 * @Date 2018/6/13 16:40
 */
public class DateUtil {
    public static String getDateDiff(Date fromDate, Date toDate) {
        long from = fromDate.getTime();
        long to =toDate.getTime();
        long diff=to - from;
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 计算差多少小时
        long hour = diff/nh;
        // 计算差多少分钟
        long min = diff % nh / nm;
        String str="";
        if (hour>0){
            str=hour + "小时";
        }
        if (min>0){
            str+= min + "分钟";
        }
        return str;

    }

    /**
     * @Author zhouxia
     * @Date 2018/6/14 11:19
     */
    public static  Long getTimeDiff(Date startTime, Date endTime) {

        long l=endTime.getTime()-startTime.getTime();

        long day=l/(24*60*60*1000);

        long hour=(l/(60*60*1000)-day*24);

        long min=((l/(60*1000))-day*24*60-hour*60);
        if(min>0){
            hour=hour+1;
        }
        if(hour>0){
            return day+1;
        }
        return day;
    }

    /**
     * 计算相差多少小时
     * @param startTime
     * @param endTime
     * @return
     */
    public static double timeDifference(Date startTime,Date endTime){
        double  hours=0;
            long from = startTime.getTime();
            long to = endTime.getTime();
            hours = (double ) (((to - from)*1.0)/(1000*3600));
        return hours;
    }

    /**
     * 计算相差多少天
     * @param startTime
     * @param endTime
     * @return
     */
    public static Integer dayDifference(Date startTime,Date endTime){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            startTime=sdf.parse(sdf.format(startTime));
            endTime=sdf.parse(sdf.format(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endTime);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }


}
