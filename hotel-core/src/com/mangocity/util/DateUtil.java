/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.mangocity.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.mangocity.util.exception.InvalidDataException;

/**
 * @author zhengxin created 2005-4-27 9:45:44 version 1.0
 */
public class DateUtil implements Serializable {

    public static final int SECOND = 1000;

    public static final int MINUTE = SECOND * 60;

    public static final int HOUR = MINUTE * 60;

    public static final int DAY = HOUR * 24;

    public static final int WEEK = DAY * 7;

    public static final int YEAR = DAY * 365; // or 366 ???

    /**
     * 
     */
    public static long millionSecondsOfDay = 86400000;

    /**
     * Creates a Date, at 00:00:00 on the given day.
     * 
     * @param month
     *            1-12 (0 = January)
     * @param date
     * @param year
     */
    public static Date newDate(int month, int date, int year) {
        Calendar inst = Calendar.getInstance();
        inst.clear();
        inst.set(year, month - 1, date);
        return inst.getTime();
    }

    /**
     * 测试日期是否在某一段日期之间
     * 
     * @param date
     * @param start
     * @param end
     * @return
     */
    public static boolean between(Date date, Date start, Date end) {
        return 0 <= getDay(start, date) && 0 >= getDay(end, date);
    }

    /**
     * @param date
     * @param i
     * @return
     */
    public static Date getDate(Date date, int i) {

        if (null == date)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, i);

        return calendar.getTime();

    }

    public static int compare(Date date1, Date date2) {
        return getDay(date1, date2);
    }

    /**
     * add by zhineng.zhuang 比较两个时间点的大小，格式为1200，中间无分号
     * 
     * @param time1
     * @param time2
     * @return
     */
    public static boolean compareHour(String sTime, String bTime) {
        // add by lixiaoyong v2.6 酒店时间格式如果为mm:ss转化为mmss便于比较
        bTime = getTimeFromString(bTime);
        boolean bigger = false;
        int bTimeHour = 0;
        int sTimeHour = 0;
        if (StringUtil.isValidStr(sTime) && StringUtil.isValidStr(bTime)) {
            bTimeHour = Integer.parseInt(bTime);
            sTimeHour = Integer.parseInt(sTime);
        }
        if (bTimeHour > sTimeHour) {
            bigger = true;
        }
        return bigger;
    }

    /**
     * 
     * @param date
     * @param hour
     * @return
     */
    public static Date getDateByHour(Date date, int hour) {

        if (null == date)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);

        return calendar.getTime();

    }
    
    /**
     * add by xuyiwen 2010-12-14
     * @param date
     * @param month
     * @return
     */
    public static Date getDateByMonth(Date date,int month){
    	if(null==date) return null;
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * @param date1
     * @param date2
     * @return
     */
    public static int getDay(Date date1, Date date2) {
        if (null == date1 || null == date2)
            return 0;

        date1 = getDate(date1);
        date2 = getDate(date2);

        return Long.valueOf((date2.getTime() - date1.getTime()) / millionSecondsOfDay).intValue();
    }

    /**
     * 日期的星期
     * 
     * @param date
     *            Date
     * @return int 1-7
     */
    public static int getWeekOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return 0 == (calendar.get(Calendar.DAY_OF_WEEK) - 1) ? 7 : calendar
            .get(Calendar.DAY_OF_WEEK) - 1;
    }
	
	/**
     * @param date
     *            Date
     * @return int hours(24)*60+minute
     */
    public static int getMinsDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
    }

    /**
     * 日期是否符合某一星期
     * 
     * @param date
     * @param week
     * @return
     */
    public static boolean isMatchWeek(Date date, int week) {
        return getWeekOfDate(date) == week;
    }

    public static boolean isMatchWeek(Date date, Integer[] weeks) throws InvalidDataException {
        if (null == weeks)
            throw new InvalidDataException("weeks cannot be null!");

        int len = weeks.length;

        for (int m = 0; m < len; m++) {

            if (isMatchWeek(date, weeks[m].intValue()))
                return true;
        }

        return false;
    }

    public static boolean isMatchWeek(Date date, String[] weeks) throws InvalidDataException {
        if (null == weeks)
            throw new InvalidDataException("weeks cannot be null!");

        int len = weeks.length;

        for (int m = 0; m < len; m++) {

            if (isMatchWeek(date, Integer.valueOf(weeks[m]).intValue()))
                return true;
        }

        return false;
    }

    public static java.sql.Date getSqlDate(Date date) {
        if (null == date)
            return null;
        return new java.sql.Date(date.getTime());
    }

    public static Date getDate(Date date) {
        if (null == date)
            return null;
        return getDate(DateUtil.dateToString(date));

    }

    public static Date getDefaultDateTime(Date date) {
        if (null == date)
            return null;
        String str = dateToString(date) + " 12:00";

        return stringToDatetime(str);
    }

    /**
     * 根据开始日期和结束日期，获得一个日期列表
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static List<Date> getDates(Date date1, Date date2) {
        if (null == date1 || null == date2)
            return new ArrayList<Date>(0);

        int day = getDay(date1, date2);

        List<Date> dates = new ArrayList<Date>(day);
        for (int i = 0; i <= day; i++) {
            dates.add(getDate(date1, i));
        }
        
        return dates;
    }

    public static String dateToString(Date date) {
        if (null == date)
            return "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(date);
    }

    public static String dateToStringNew(Date date) {
        if (null == date)
            return "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        return sdf.format(date);
    }

    public static String datetimeToString(Date date) {
        if (null == date)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return sdf.format(date);
    }

    public static String toStringByFormat(Date date, String format) {
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.format(date);
        }
        return "";
    }

    public static Date toDateByFormat(String str, String format) {
        if (null == str || str.equals(""))
            return null;
        // modify by shizhongwen 2009-05-21 针对香港所传过来的String 如: Wed Feb 23 10:12:34 CST 2009
        if (0 < str.indexOf("CST")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US);
                Date d = sdf.parse(str);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDate(String str) {
        if (null == str || str.equals(""))
            return null;
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        // modify by shizhongwen 2009-05-21 针对香港所传过来的String 如: Wed Feb 23 10:12:34 CST 2009
        if (0 < str.indexOf("CST")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US);
                Date d = sdf.parse(str);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据字符串日期 datestr, 日期模板获得 Date add by shizhongwen 时间:Mar 16, 2009 3:37:41 PM
     * 
     * @param datestr
     * @param format
     *            如:yyyyMMdd,yyyy-MM-dd
     * @return
     */
    public static Date stringToDateMain(String datestr, String format) {
        if (null == datestr || datestr.equals(""))
            return null;
        // modify by shizhongwen 2009-05-21 针对香港所传过来的String 如: Wed Feb 23 10:12:34 CST 2009
        if (0 < datestr.indexOf("CST")||0<datestr.indexOf("GMT")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US);
                Date d = sdf.parse(datestr);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return sdf.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDateTime(String str) {
        if (null == str || str.equals(""))
            return null;
        // modify by shizhongwen 2009-05-21 针对香港所传过来的String 如: Wed Feb 23 10:12:34 CST 2009
        if (0 < str.indexOf("CST")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US);
                Date d = sdf.parse(str);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得日期格式为yyyy-MM-dd HH:mm 的时间 add by guzhijie
     * 
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date stringToDateMinute(String str) {
        if (null == str || str.equals(""))
            return null;
        // modify by shizhongwen 2009-05-21 针对香港所传过来的String 如: Wed Feb 23 10:12:34 CST 2009
        if (0 < str.indexOf("CST")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US);
                Date d = sdf.parse(str);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDatetime(String str) {
        if (null == str || str.equals(""))
            return null;
        // modify by shizhongwen 2009-05-21 针对香港所传过来的String 如: Wed Feb 23 10:12:34 CST 2009
        if (0 < str.indexOf("CST")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US);
                Date d = sdf.parse(str);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDate(String str) {
        if (null == str || str.equals(""))
            return null;
        // modify by shizhongwen 2009-05-21 针对香港所传过来的String 如: Wed Feb 23 10:12:34 CST 2009
        if (0 < str.indexOf("CST")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US);
                Date d = sdf.parse(str);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getTimeString(int duration) {
        int hours = duration / DateUtil.HOUR;
        int remain = duration - (hours * DateUtil.HOUR);
        int minutes = remain / DateUtil.MINUTE;
        StringBuffer time = new StringBuffer(64);
        if (0 != hours) {
            if (1 == hours) {
                time.append("1 hour and ");
            } else {
                time.append(hours).append(" hours and ");
            }
        }
        if (1 == minutes) {
            time.append("1 minute");
        } else {
            // what if minutes == 0 ???
            time.append(minutes).append(" minute(s)");
        }
        return time.toString();
    }

    /**
     * @param str
     * @return
     * @throws ParseException
     */
    // public static String getICSDate(String str) throws ParseException {
    // if (str == null || str.equals(""))
    // return null;
    //
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    //
    // Date date = sdf.parse(str);
    //
    // return com.sune365.air.bookingEngine.util.DateUtil.dateToString(date,
    // "ddMMMyy");
    // }
    /**
     * @return int
     */
    public static int getYearOfSysTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @return int
     */
    public static int getMonthOfSysTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDayOfSysTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(stringToDateTime("2008-07-03 11:44:13").getTime() / (1000 * 60));
    }
    public static Date getDate(int year,int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		Date date = cal.getTime();
		return date;
    }
    /***
     * 得到这个日期的第一天
     * @param date
     * @return
     */
    public static String getBeginDate(Date date){
    	Date beginDay;
        Calendar   cDay1   =   Calendar.getInstance();   
        cDay1.setTime(date);   
        beginDay   =   cDay1.getTime();   
        beginDay.setDate(cDay1.getActualMinimum(Calendar.DAY_OF_MONTH));   
    	return dateToString(beginDay);
    }
    /***
     * 得到这个日期的最后天
     * @param date
     * @return
     */
    public static String getEndDate(Date date){
    	Date endDay;
        Calendar   cDay1   =   Calendar.getInstance();   
        cDay1.setTime(date);     
        endDay   =   cDay1.getTime();   
        endDay.setDate(cDay1.getActualMaximum(Calendar.DAY_OF_MONTH));
    	return dateToString(endDay);
    }
    /**
     * add by lixiaoyong 转换mm:ss为mmss以便于时间比较
     * 
     * @param sTime
     *            源时间格式
     * @return
     */
    public static String getTimeFromString(String sTime) {
        String dTime = "";
        if (sTime.contains(":")) {
            dTime = sTime.replace(":", "");
        } else {
            dTime = sTime;
        }
        return dTime;
    }

    public static Date getSystemDate() {
        return new Date();
    }

    public static String formatDateToSQLString(Date srcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(srcDate);
    }

    public static String formatDateToYMDHMS(Date srcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.CHINA);
        return sdf.format(srcDate);
    }

    public static String formatDateToYMDHMS1(Date srcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(srcDate);
    }

    public static String formatDateToMMDD(Date srcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.CHINA);
        return sdf.format(srcDate);
    }

    public static String formatTimeToString(Date srcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(srcDate);
    }

    public static String formatTimeHToString(Date srcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.CHINA);
        return sdf.format(srcDate);
    }

    /**
     * add by zhineng.zhuang 取得时间的小时和分钟
     * 
     * @param srcDate
     * @return
     */
    public static String formatHourTimeHToString(Date srcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.CHINA);
        return sdf.format(srcDate);
    }

    public static String weeksToString(String[] week) {
        if (null == week) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String result = "";
        for (int i = 0; i < week.length; i++) {
            if (i == week.length - 1) {
                sb.append(week[i]);
            } else {
                sb.append(week[i]).append(",");
            }
            result = sb.toString();
        }
        return result;
    }

    /**
     * 计算某个日期时间段内符合某个星期数组 的方法
     * 
     * @param begindate
     *            ,enddate,weeks
     * @return date[]
     */
    public static Date[] getDateWithWeek(Date beginDate, Date endDate, Integer[] weeks) {
        int dateNum = compare(beginDate, endDate);
        List datelist = new ArrayList();
        for (int i = 0; i <= dateNum; i++) {
            if (isMatchWeek(getDate(beginDate, i), weeks)) {
                datelist.add(getDate(beginDate, i));
            }
        }
        Date[] dates = new Date[datelist.size()];
        for (int j = 0; j < datelist.size(); j++) {
            dates[j] = (Date) datelist.get(j);
        }
        return dates;
    }

    public static Date[] getDateWithWeek(Date beginDate, Date endDate, String[] weeks) {
        int dateNum = compare(beginDate, endDate);
        List datelist = new ArrayList();
        for (int i = 0; i < dateNum; i++) {
            if (isMatchWeek(getDate(beginDate, i), weeks)) {
                datelist.add(getDate(beginDate, i));
            }
        }
        Date[] dates = new Date[datelist.size()];
        for (int j = 0; j < datelist.size(); j++) {
            dates[j] = (Date) datelist.get(j);
        }
        return dates;
    }

    public static Date minusDate(Date date, int day) {
        long time = date.getTime() - 24 * 60 * 60 * 1000 * day;
        Date date1 = null;
        try {
            date1 = new Date(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static String formatDateToMD(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM", Locale.CHINA);
        return fmt.format(date);
    }
    
    /**
     * 
     * @param 需要格式的人日期
     * @param 格式月日的模式，例如"08-31"
     * @return 返回格式化后的日期
     */
    public static String formatDateToMD(Date date,String pattern) {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern, Locale.CHINA);
        return fmt.format(date);
    }

    // 计算某日期与当前系统日期之间相差的天数，0表示相差为0天，1表示相差一天，3表示多余1天，注意传入的date应该大于等于当前日期
    public static int getDayOverToday(Date date) {
        if (null == date) {
            return 4;
        } else {
            Date now = new Date();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(now);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(date);
            Calendar c3 = Calendar.getInstance();
            c3.set(c1.get(Calendar.YEAR), 11, 31);
            int j = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
            if (0 == c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) {
                // System.out.println(c2.get(c2.DAY_OF_YEAR)-c1.get(c1.DAY_OF_YEAR));
                if (0 == j) {
                    return 0;
                } else if (1 == j) {
                    return 1;
                } else {
                    return 3;
                }
            } else if (1 == c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) {
                j = (c3.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR))
                    + c2.get(Calendar.DAY_OF_YEAR);
                if (0 == j) {
                    return 0;
                } else if (1 == j) {
                    return 1;
                } else {
                    return 3;
                }
            } else {
                return 3;
            }
        }
    }

    /**
     * 1----00:00-07:29 生成的订单（包含当天与非当天入住） 2----07:30-22:29 生成的订单(包含当天与非当天入住);22:30-23:59 当天入住
     * 3----22:30-23:59 非当天入住 4----07:30-22:30 营业部付款(包含当天与非当天入住) 5----全天候,无时间段区分,全额积分预付
     * 
     * public static int getOrderAccomplishTime(Date date, boolean isDepartment, boolean
     * isAllpoint){ if(isAllpoint){ return 5; } SimpleDateFormat fmt = new SimpleDateFormat("HHmm");
     * Date now = new Date(); Calendar calendar = Calendar.getInstance(); calendar.setTime(now);
     * Calendar cal = Calendar.getInstance(); cal.setTime(date); int i =
     * Integer.parseInt(fmt.format(now)); if(i>=730&&i<2230&&isDepartment){ return 4; }else
     * if(i>=0&&i<730){ return 1; }else if(i>=730&&i<2230){ return 2; } else
     * if(i>=2230&&i<2400&&(calendar.get(calendar.YEAR)==cal.get(cal.YEAR) &&
     * calendar.get(calendar.MONTH)==cal.get(cal.MONTH) &&
     * calendar.get(calendar.DAY_OF_YEAR)==cal.get(cal.DAY_OF_YEAR))) { return 2; }else
     * if(i>=2230&&i<2400&&(!(calendar.get(calendar.YEAR)==cal.get(cal.YEAR) &&
     * calendar.get(calendar.MONTH)==cal.get(cal.MONTH) &&
     * calendar.get(calendar.DAY_OF_YEAR)==cal.get(cal.DAY_OF_YEAR)))){ return 3; } return 0; }
     */
    /**
     * fuhoujun update
     * 
     * @param isDepartment
     *            是否营业部付款
     * @param isAllpoint
     * @param enterDate
     *            入住日期
     * @return
     */
    public static int getOrderAccomplishTime(Date enterDate, boolean isDepartment,
        boolean isAllpoint) {
        if (isAllpoint) {
            return 5;
        }
        if (isDepartment) {
            return 4;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("HHmm", Locale.CHINA);
        Calendar currentCalendar = Calendar.getInstance();
        Calendar enterCalendar = Calendar.getInstance();
        enterCalendar.setTime(enterDate);
        int i = Integer.parseInt(fmt.format(currentCalendar.getTime()));
        if (0 <= i && 729 > i) {
            return 1;
        }
        if (729 <= i && 2159 >= i) {
            return 2;
        } else {
            if (currentCalendar.get(Calendar.YEAR) == enterCalendar.get(Calendar.YEAR)
                && currentCalendar.get(Calendar.MONTH) == enterCalendar.get(Calendar.MONTH)
                && currentCalendar.get(Calendar.DATE) == enterCalendar.get(Calendar.DATE)) {// 当天入住
                return 2;
            } else {
                return 3;
            }
        }
    }

    public static Date getDateWithoutWeekend(Date source, int days) {
        int i = 0, j = 0;
        while (j <= days) {
            int dayOfweek = getWeekOfDate(getDate(source, -i));
            i++;
            if (6 == dayOfweek || 7 == dayOfweek) {
                continue;
            } else {
                j++;
            }
        }
        return getDate(source, -(i - 1));
    }

    /**
     * 增加字符串日期
     * 
     * @param dateStr
     *            格式必须是yyyy-MM-dd
     * @param offset
     *            增加,减少天数
     * @return
     */
    public static String addStringDate(String dateStr, int offset) {
        if (null == dateStr || dateStr.equals(""))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date tempDate = new Date();
        Calendar cal = Calendar.getInstance();
        try {
            tempDate = sdf.parse(dateStr);
            cal.setTime(tempDate);
            cal.set(Calendar.DAY_OF_MONTH, (cal.get(Calendar.DAY_OF_MONTH) + offset));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd", Locale.CHINA);
        return sdf1.format(cal.getTime());
    }
    
        
    public static String formatDate(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd", Locale.CHINA);
        return fmt.format(date);
    }
    
    public static String formatDateToMDStr(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        return fmt.format(date);
    }

    /**
     * 
     * @param dateStr
     * @param offset
     * @return
     */

    /**
     * 增加字符串日期 返回格式也是格式必须是yyyy-MM-dd add by shizhongwen 时间:Feb 3, 2009 4:07:46 PM
     * 
     * @param dateStr
     *            格式必须是yyyy-MM-dd
     * @param offset
     *            增加,减少天数
     * @return
     */
    public static String addStringDateALL(String dateStr, int offset) {
        if (null == dateStr || dateStr.equals(""))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date tempDate = new Date();
        Calendar cal = Calendar.getInstance();
        try {
            tempDate = sdf.parse(dateStr);
            cal.setTime(tempDate);
            cal.set(Calendar.DAY_OF_MONTH, (cal.get(Calendar.DAY_OF_MONTH) + offset));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf1.format(cal.getTime());
    }

    /**
     * dateStr 为"yyyyMMdd"模式 add by shizhongwen 时间:Mar 16, 2009 7:38:05 PM
     * 
     * @param dateStr
     * @param offset
     * @param format
     * @return
     */
    public static String addStringDateALLMain(String dateStr, int offset, String format) {
        if (null == dateStr || dateStr.equals(""))
            return null;
        if (null == format || format.equals(""))
            format = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date tempDate = new Date();
        Calendar cal = Calendar.getInstance();
        try {
            tempDate = sdf.parse(dateStr);
            cal.setTime(tempDate);
            cal.set(Calendar.DAY_OF_MONTH, (cal.get(Calendar.DAY_OF_MONTH) + offset));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(format, Locale.CHINA);
        return sdf1.format(cal.getTime());
    }

    /**
     * 对于日期进行格式描述 modify by chenkeming Feb 11, 2009 10:25:47 AM
     * 
     * @param startDate
     * @param endDate
     * @param forOneWeek
     * @return
     */
    public static List<String> getDateStrList(Date startDate, Date endDate, boolean forOneWeek) {

        int difdays = DateUtil.getDay(startDate, endDate);

        List dateStrList = new ArrayList();

        difdays = forOneWeek ? (7 <= difdays ? 7 : difdays) : difdays;
        for (int i = 0; i < difdays; i++) {
            Date reservationDate = DateUtil.getDate(startDate, i);
            int week = reservationDate.getDay();

            String dateStr = (reservationDate.getMonth() + 1) + "/" + reservationDate.getDate()
                + " 周" + (0 == week ? "日" : week);
            dateStrList.add(dateStr);
        }

        return dateStrList;
    }

    /**
     * 原始时间字符转换 例如将Tue Oct 09 00:00:00 CST 2009转换为相应的时间(Locale.US)
     * 
     * @param strDate
     * @return
     */
    public static Date getDateFromOriDateStr(String strDate) {
        Date date = null;
        if (null == strDate || "".equals(strDate)) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            try {
                date = sdf.parse(strDate);
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }
        }
        return date;
    }

    /**
     * 时间字符转换 例如将2010-01-01 12:30:33转换为相应的时间(Locale.CHINA)
     * 
     * @param strDate
     * @return
     */
    public static Date getDateTimeFromTimeStr(String timeStr) {
        Date date = null;
        if (null == timeStr || "".equals(timeStr)) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                date = sdf.parse(timeStr);
            } catch (ParseException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return date;
    }
    
    /**
     * 时间转换字符 例如将new Date(2010-01-01 12:30:33)转换为相应的时间字符串"2010-01-01 12:30:33"
     * 
     * @param strDate
     * @return
     */
    public static String getTimeStrFromDate(Date date,String fmt) {
    	String str = null;
        SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.CHINA);
        return sdf.format(date);
    }
    /**
     * 传进的时间与当前日期时间比较，系统日期比其小返回true add by zhineng.zhuang
     * 
     * @param compaDate
     * @param compaTime
     * @return
     */
    public static boolean compareDateTimeToSys(Date compaDate, String compaTime) {
        boolean bigger = false;
        if (0 < compare(DateUtil.getDate(DateUtil.getSystemDate()), compaDate)
            || (0 == compare(DateUtil.getDate(DateUtil.getSystemDate()), compaDate) && compareHour(
            	formatHourTimeHToString(DateUtil.getSystemDate()),compaTime))) {
            bigger = true;
        }
        return bigger;
    }
    
    
    /**
     * 传进的时间与当前系统日期时间比较，系统日期比其大返回true add by shengwei.zuo
     * 
     * @param compaDate
     * @param compaTime
     * @return
     */
    public static boolean compareDateTimeToSysBig(Date compaDate, String compaTime) {
        boolean bigger = false;
        if (0 < compare(compaDate,DateUtil.getDate(DateUtil.getSystemDate()))
            || (0 == compare(compaDate,DateUtil.getDate(DateUtil.getSystemDate())) && compareHour(
            compaTime,formatHourTimeHToString(DateUtil.getSystemDate())))) {
            bigger = true;
        }
        return bigger;
    }

    /**
     * 判断一个日期是否在一个起止日期之间，是返回true 比如 2009-02-17 在 2009-02-17与 2009-02-18之间，返回true add by
     * zhineng.zhuang
     * 
     * @param betwDate
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean isBetween(Date betwDate, Date beginDate, Date endDate) {
        boolean isBetween = false;
        if (null != betwDate && null != beginDate && null != endDate
            && (0 <= compare(beginDate, betwDate) && 0 < compare(betwDate, endDate))) {
            isBetween = true;
        }
        return isBetween;
    }

    /**
     * 判断当前系统时间是否在一个起止日期之间，是返回true 比如 2009-02-17 09:00 在 2009-02-16 09:00 与 2009-02-18 09：00之间，返回true add by
     * shengwei.zuo 
     * 
     * @param beTime
     * @param beginDate
     * @param endDate
     * @param endTime
     * @return
     */
    public static boolean isBetweenDateTime(Date beginDate,String beTime, Date endDate,String endTime) {
        boolean isBetween = false;
        
        if (null != beginDate && null != beTime && null != endDate && null !=endTime
	            && compareDateTimeToSys(endDate,endTime)
	            && compareDateTimeToSysBig(beginDate,beTime)
            ) {
            isBetween = true;
        }
        return isBetween;
    }
    
    public static String formatDateToMDTwo(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("d/M", Locale.CHINA);
        return fmt.format(date);
    }

    /**
     * add by wuyun 2009-03-17
     * 
     * @param s
     *            待转换的字符串，为yyyymmdd格式
     * @return
     */
    public static String getString4Paydate(String s) {
        String ss = "";
        try {
            String yyyy = s.substring(0, 4);
            String mm = s.substring(4, 6);
            String dd = s.substring(6, 8);
            ss = yyyy + "年" + mm + "月" + dd + "日";
        } catch (Exception e) {
            // TODO: handle exception
        }
        return ss;

    }
    
    /**
     * 传进来是15/01格式..转换成为当年的 2010-1-15
     * add by haibo.li 
     * 代理系统
     */
    public static Date getB2BDate(String s){
    	try{
    		GregorianCalendar g=new GregorianCalendar();
            String year =String.valueOf( g.get(Calendar.YEAR));
            String dd = s.substring(0,2);
            String mm = s.substring(3,5);
            StringBuffer a = new StringBuffer();
            a.append(year);
            a.append("-");
            a.append(mm);
            a.append("-");
            a.append(dd);
    		return DateUtil.getDate(a.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    	
    }
    /**
     * 检查日期是否在日期区间中，并且在规定的日期中 add by chenjiajie 2009-10-16
     * @param checkedDate
     * @param begin
     * @param end
     * @param week 1,2,3,4,5,6,7
     * @return
     */
    public static boolean checkDateBetween(Date checkedDate,Date begin,Date end,String week){
    	boolean checkResult = false;
    	//检查日期是否在规则的开始结束日期区间
    	boolean isBetween = DateUtil.between(checkedDate, begin, end);
		if(isBetween){
			String weekOfDate = String.valueOf(DateUtil.getWeekOfDate(checkedDate));
			//检查日期是否在规定的星期中
			if(week.indexOf(weekOfDate) >= 0){
				checkResult = true;
			}
		}
    	return checkResult;
    }
    
    /**
	 * 获得某一日期的后一天 add by haibo.li
	 * @param date
	 * @return Date
	 */
	public static Date getNextDate(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		int day=calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE,day+1);
		return getSqlDate(calendar.getTime());
	}
	
	 /**
	 * 获得当前日期 add by haibo.li
	 * @param date
	 * @return Date
	 */
	public static Date getDateTwo(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		int day=calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE,day);
		return getSqlDate(calendar.getTime());
	}
	
	/**
	 * 获得某一日期的前一天 add by haibo.li
	 * @param date
	 * @return Date
	 */
	public static Date getPreviousDate(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		int day=calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE,day-1);
		return getSqlDate(calendar.getTime());
	}
	
	/**
	 * 传进来time是1500 返回 15:00格式  add by haibo.li 网站改版
	 * @param strTime
	 * @return
	 */
	public static String subStrTime(String strTime){
		String inTime = strTime.substring(0,2);
		String outTime = strTime.substring(2,strTime.length());	
		return inTime+":"+outTime;
	}
	/**
     * 取得两个日期之间的日期，包括起止两天 addby juesuchen
     * @param begin
     * @param end
     * @return
     */
    public static Date[] getDaysBetween(Date beginDate,Date endDate){
    	if(null != beginDate && null != endDate){
    		int count = DateUtil.getDay(beginDate, endDate);
    		Date[] days = new Date[count+1];
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(beginDate);
    		days[0] = cal.getTime();
    		for (int i = 1; i <= count; i++) {
    			cal.add(Calendar.DAY_OF_MONTH, 1);
    			days[i] = cal.getTime();
			}
    		return days;
    	}
    	return null;
    }
	public static String getDayAndMonth(String dateStr){
    	if(StringUtil.isValidStr(dateStr))
    		return null;
    	Date date = DateUtil.getDate(dateStr);
    	if(date != null){
    		DateUtil.getDayAndMonth(date);
    	}
    	return null;
    	
    }
    public static String getDayAndMonth(Date date){
    	if(date != null){
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1);
    	}
    	return null;
    }
    
    /**
     * 比较两个日期是否为同一天
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2){    	
    	return dateToString(date1).equals(dateToString(date2));
    }
    
    //add by diandian.hou 2012-4-1
    public enum WeekDay{
		Monday(1,"周一"),Tuesday(2,"周二"),Wednesday(3,"周三"),Thursday(4,"周四"),
		Friday(5,"周五"),Saturday(6,"周六"),Sunday(7,"周日"),Errorday(0,"日期数字不对");
		
		private int key;
		private String value;
		
		WeekDay(int key,String value){
			this.key = key;
			this.value = value;
		}
		
		 public static WeekDay getEnumByKey(int key) {
			for (int i = 0; i < WeekDay.values().length; i++) {
				if (WeekDay.values()[i].getKey() == key) {
					return WeekDay.values()[i];
				}
			}
			return Errorday;
		}
		public static String getValueByKey(int key){
			return getEnumByKey(key).getValue();
		}

		 
		   public String getValue() {
	            return value;
	        }
	        public int getKey() {
	            return key;
	        }
		
	}
  public static Date getLatestDateByReservType(String typeStr,String firstDateOrDays,String firstTime,Date checkInDate,String arrivalTime){
    	Date result = null;
    	int hours = 0;
        int minutes = 0;
        long milliseconds = 0;
        int earlyDays = -1; 	
        int type = -1;
        if(null != typeStr && "".equalsIgnoreCase(typeStr)){
        	type = Integer.parseInt(typeStr);
        }
        if(type != -1){
            if (1 == type) { // 第1种: 凡是均需
            	
            } else if (2 == type || 3 == type) { // 第2种: 在几日几点至几日几点,第3种: 在几日几点至入住当日几点
                String strDate = firstDateOrDays;
                String strTime = firstTime;
                if (StringUtil.isValidStr(strDate)) {
                    Date tDate = DateUtil.stringToDateMinute(strDate + " " + strTime);
                    Date tmpeDate = new Date(tDate.getTime() - 24 * 60 * 60 * 1000);
                    result = tmpeDate;
                }
            } else if (4 == type) { // 第4种: 在入住日期前几天几点至几天几点
                int nDays = Integer.parseInt(firstDateOrDays);
                String sTime = firstTime;
                    earlyDays = nDays;
                    String[] sTimes = new String[2];
                    if (-1 < sTime.indexOf(":")) {
                        sTimes = sTime.split(":");
                    }
                    hours = Integer.parseInt(sTimes[0]);
                    minutes = Integer.parseInt(sTimes[1]);
                    milliseconds = (hours * 60 + minutes) * 60 * 1000;
                    //firstEarlyDate = 入住日期 - (在入住日期前几天 + 1) 后面加上小时
                    Date firstEarlyDate = DateUtil.getDate(checkInDate, (earlyDays + 1) * -1);
                    result = new Date(firstEarlyDate.getTime() + milliseconds);
            } else if (5 == type) { // 第5种: 几点之前/之后
                String sTime = firstTime;
                String[] sTimes = new String[2];
                if (-1 < sTime.indexOf(":")) {
                    sTimes = sTime.split(":");
                }
                hours = Integer.parseInt(sTimes[0]);
                minutes = Integer.parseInt(sTimes[1]);
                milliseconds = (hours * 60 + minutes) * 60 * 1000;
                result = new Date(checkInDate.getTime() + milliseconds - 24
                    * 60 * 60 * 1000);
            }        	
        }else{
        	System.currentTimeMillis();
        }
    	return result;
    }
    
    /**
     * 根据页面传入的参数得到订单取消状态类型
     * @param cancelCode
     * @param checkInDate
     * @param arrivalTime
     * @param suretyCode
     * @param payMethod
     * @param cancelModifyDate
     * @return result
     * 1：可网站取消
     * 2：不可取消
     * 3：可电话取消
     * 4：取消申请中
     */
    public static String getHintType4WebCancel(Object cancelCode,Object checkInDate,Object arrivalTime,Object suretyCode,Object payMethod,Object cancelModifyDate,Object originOrderStates,Object auditDate,Object customConfirm){
    	String result = null;
    	Date lasestDate = null;
    	Calendar calendar = Calendar.getInstance();
    	Date nowDate = calendar.getTime();
    	Date twoHoursLaterOfModDate =null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	
    	String cancelCodeStr = null;
    	String originOrderStatesStr = null;
    	String suretyCodeStr = null;
    	String payMethodStr = null;
    	Date checkInDateTemp = null;
    	Date cancelModifyDateTemp = null;
    	String checkInDateStr = null;
    	String cancelModifyDateStr = null;
    	String arrivalTimeStr = null; 
    	int isAuditDate = 0;
    	String customConfirmStr=null;
    	try{
    		if (null != cancelCode && cancelCode instanceof BigDecimal){
    			cancelCodeStr = String.valueOf(cancelCode);
    		}
    		if (suretyCode instanceof String){
    			suretyCodeStr = String.valueOf(suretyCode);
    		}
    		if (payMethod instanceof String){
    			payMethodStr = String.valueOf(payMethod);
    		}
    		if (null != cancelModifyDate && cancelModifyDate instanceof String){
    			cancelModifyDateStr = String.valueOf(cancelModifyDate);
    			cancelModifyDateTemp = sdf.parse(cancelModifyDateStr);
    			twoHoursLaterOfModDate = new Date(cancelModifyDateTemp.getTime()+2*60*60*1000);
    		}
    		if (checkInDate instanceof Date){
    			checkInDateStr = String.valueOf(checkInDate)+" 17:00:00";
    			checkInDateTemp = sdf.parse(checkInDateStr);
    		}
    		if (null != arrivalTime && arrivalTime instanceof String){
    			arrivalTimeStr = String.valueOf(arrivalTime);
    		}
    		if (originOrderStates instanceof BigDecimal){
    			originOrderStatesStr = String.valueOf(originOrderStates);
    		}
    		if (null != auditDate){
    			isAuditDate =1; //1.已审核，0.未审核
    		}else{
    			isAuditDate =0; 
    		}
    		if (null != customConfirm){
    			customConfirmStr = String.valueOf(customConfirm);
    		}else{
    			customConfirmStr = "0";
    		}
    		String sTime = arrivalTimeStr;
        	//到店时间如果为空，给其默认值18:00
        	if(arrivalTimeStr == null ||"".equalsIgnoreCase(arrivalTimeStr)){
        		sTime = "18:00";
        	}
            String[] sTimes = splitTime(sTime,":");
            long baseTime4Cancel = getBaseTime4Cancel(sTimes,checkInDateTemp,-1,-1);
            lasestDate = new Date(baseTime4Cancel);
            
    		/**
    		 * 首先判断订单的状态是否为：预订取消、已入住、正常退房、延住（对应的状态码为4或6），
    		 * 如果是上述状态之一则该订单的状态为“不可取消”，前台显示为 ---
    		 * 如果2小时内做过取消申请，则该订单的状态为“取消申请中”
    		 * 否则，根据checkindate来后做续处理
    		 */
            if((originOrderStatesStr.equals("5")&& isAuditDate==1)||originOrderStatesStr.equals("6")||originOrderStatesStr.equals("7")||(originOrderStatesStr.equals("8")&& customConfirmStr.equals("1"))||(originOrderStatesStr.equals("13")&& isAuditDate==1)||originOrderStatesStr.equals("14")){
            	result = "2";
            }else{
            	if(null !=cancelCodeStr && nowDate.compareTo(twoHoursLaterOfModDate) < 0){
                	result = "4";
                }else{
                	 /**
                     * 如果当前日期已经大于checkindate，则不可取消
                     * 否则根据面预付不同来进行处理
                     * 面付非担保的可以网站取消，担保的只能电话取消
                     * 预付只能电话取消
                     */
                    if(nowDate.compareTo(checkInDateTemp) < 0){
                    	if(originOrderStatesStr.equals("1")){
                    		result="1";
                    	}else if("pay".equalsIgnoreCase(payMethodStr)){
            				if(null != suretyCodeStr && "0".equalsIgnoreCase(suretyCodeStr)){//如果是面付无担保或订单未提交则可网站取消
            					result="1";
            				}else {//订单提交成功后的面付担保单可电话取消
            					result="3";
            				}
            			}else if("pre_pay".equalsIgnoreCase(payMethodStr)){ //预付订单可电话取消   
            				result="3";
            			}
                    }else{
                    	result = "2";
                    }
                }
            }
            
/*    		if(null ==cancelCodeStr){
                *//**
                 * 如果当前日期已经大于checkindate，则不可取消
                 * 否则根据面预付不同来进行处理
                 *//*
                if(nowDate.compareTo(checkInDateTemp) < 0){
        			if("pre-pay".equalsIgnoreCase(payMethodStr)){//如果是预付则不可取消
        				result="2";
        			}else if("pay".equalsIgnoreCase(payMethodStr)){ //面付订单处理流程       				
        				if(null != suretyCodeStr && "0".equalsIgnoreCase(suretyCodeStr)){//如果是面付无担保则可网站取消
        					result="1";
        				}else if(nowDate.compareTo(lasestDate) >= 0){//如果是面付担保，且已经超过最晚到店时间则可电话取消
        					result="3";
        				}else if(nowDate.compareTo(lasestDate) < 0){//如果是面付担保，且没有超过最晚到店时间则可网站取消
        					result="1";
        				}
        			}
                }else{
                	result = "2";
                }

    		}else{    			
    			if(nowDate.compareTo(twoHoursLaterOfModDate) < 0){//如果当前时间小于订单取消时间+2小时，则为取消申请中   				
    				result = "4";
    			}else if("6".equalsIgnoreCase(orderStatesStr)){//ToDo 否则如果该订单网站状态为预订取消则不可取消
    				result = "2";
    			}else{//否则可网站取消
    				result = "1";
    			}
    		}*/
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * 根据页面传入的条款类型、首住日，最晚到店时间等参数计算出最晚取消时间
     * @param typeStr
     * @param firstDateOrDays
     * @param firstTime
     * @param checkInDate
     * @param arrivalTime
     * @return Date 最晚取消时间
     */
    public static Date getLatestDateByReservType(Object typeStr,Object firstDateOrDays,Object firstTime,Object checkInDate,Object arrivalTime){
    	Date result = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	
    	String typeStrTemp = null;
    	String firstDateOrDaysStr = null;
    	String firstTimeStr = null;
    	Date checkInDateTemp = null;
    	String checkInDateStr = null;
    	String arrivalTimeStr = null;    	

        int earlyDays = -1; 	
        int type = -1;
    	try{
    	if (typeStr instanceof String){
    		typeStrTemp = String.valueOf(typeStr);
    	}
    	if (firstDateOrDays instanceof String){
    		firstDateOrDaysStr = String.valueOf(firstDateOrDays);
    	}
    	if (firstTime instanceof String){
    		firstTimeStr = String.valueOf(firstTime);
    	}
    	if (checkInDate instanceof Date){
    		checkInDateStr = String.valueOf(checkInDate);
    		checkInDateTemp = sdf.parse(checkInDateStr);
    	}
    	if (arrivalTime instanceof String){
    		arrivalTimeStr = String.valueOf(arrivalTime);
    	}
        if(null != typeStrTemp && "".equalsIgnoreCase(typeStrTemp)){
        	type = Integer.parseInt(typeStrTemp);
        }
        /**
         * type=-1：担保/取消条款的type为-1，说明该订单不存在此类条款，最晚取消时间为checkindate+arrivaltime；
         * type!=-1：担保/取消条款的type不为-1，说明该订单有此类条款，按照不同type值分别计算最晚取消时间
         */
        if(type != -1){
            if (1 == type) { // 第1种: 凡是均需
            	
            } else if (2 == type || 3 == type) { // 第2种: 在几日几点至几日几点,第3种: 在几日几点至入住当日几点
                String strDate = firstDateOrDaysStr;
                String strTime = firstTimeStr;
                if (StringUtil.isValidStr(strDate)) {
                    Date tDate = DateUtil.stringToDateMinute(strDate + " " + strTime);
                    Date tmpeDate = new Date(tDate.getTime() - 24 * 60 * 60 * 1000);
                    result = tmpeDate;
                }
            } else if (4 == type) { // 第4种: 在入住日期前几天几点至几天几点
                int nDays = Integer.parseInt(firstDateOrDaysStr);
                String sTime = firstTimeStr;
                    earlyDays = nDays;
                    String[] sTimes = splitTime(sTime,":");
                    long baseTime4Cancel = getBaseTime4Cancel(sTimes,checkInDateTemp,4,earlyDays);
                    result = new Date(baseTime4Cancel);
/*                    hours = Integer.parseInt(sTimes[0]);
                    minutes = Integer.parseInt(sTimes[1]);
                    milliseconds = (hours * 60 + minutes) * 60 * 1000;
                    //firstEarlyDate = 入住日期 - (在入住日期前几天 + 1) 后面加上小时
                    Date firstEarlyDate = DateUtil.getDate(checkInDateTemp, (earlyDays + 1) * -1);
                    result = new Date(firstEarlyDate.getTime() + milliseconds);*/
            } else if (5 == type) { // 第5种: 提前一天的几点之前/之后
                String sTime = firstTimeStr;
                String[] sTimes = splitTime(sTime,":");
                long baseTime4Cancel = getBaseTime4Cancel(sTimes,checkInDateTemp,5,earlyDays);
                result = new Date(baseTime4Cancel -24*60*60*1000);
/*                hours = Integer.parseInt(sTimes[0]);
                minutes = Integer.parseInt(sTimes[1]);
                milliseconds = (hours * 60 + minutes) * 60 * 1000;
                result = new Date(checkInDateTemp.getTime() + milliseconds - 24
                    * 60 * 60 * 1000);*/
            }        	
        }else{
        	String sTime = arrivalTimeStr;
        	//到店时间如果为空，给其默认值18:00
        	if(arrivalTimeStr == null ||"".equalsIgnoreCase(arrivalTimeStr)){
        		sTime = "18:00";
        	}
            String[] sTimes = splitTime(sTime,":");
            long baseTime4Cancel = getBaseTime4Cancel(sTimes,checkInDateTemp,4,earlyDays);
            result = new Date(baseTime4Cancel);
/*            hours = Integer.parseInt(sTimes[0]);
            minutes = Integer.parseInt(sTimes[1]);
            milliseconds = (hours * 60 + minutes) * 60 * 1000;
            result = new Date(checkInDateTemp.getTime() + milliseconds);   */         
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    public static String[] splitTime(String sTime,String splitSymbol){
    	String[] sTimes = new String[2];
        if (-1 < sTime.indexOf(splitSymbol)) {
            sTimes = sTime.split(splitSymbol);
        }
    	return sTimes;
    }
    
    public static long getBaseTime4Cancel(String[] sTimes,Date checkInDate,int type,int offSet){
    	long result=1L;
    	int hours = 0;
        int minutes = 0;
        long milliseconds = 0;
    	hours = Integer.parseInt(sTimes[0]);
        minutes = Integer.parseInt(sTimes[1]);
        milliseconds = (hours * 60 + minutes) * 60 * 1000;
        if(type != -1){
        	if(4==type){
            	Date firstEarlyDate = DateUtil.getDate(checkInDate, (offSet + 1) * -1);
            	result = firstEarlyDate.getTime() + milliseconds;
            }else{
            	result= checkInDate.getTime() + milliseconds;
            }  	
        }else{
        	result= checkInDate.getTime() + milliseconds;
        }
    	return result;
    }
}
