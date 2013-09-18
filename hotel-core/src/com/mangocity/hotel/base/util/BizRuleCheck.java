package com.mangocity.hotel.base.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlBatchAssign;
import com.mangocity.hotel.base.persistence.HtlBatchSalePrice;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

/**
 */
public class BizRuleCheck implements Serializable {

    /**
     * 检查当前cutoffDay是否有效,用业务日期与当前日期比较
     * 
     * @param cutDays
     *            cutoffday的天数
     * @param bizDate
     *            业务日期 如果不过期则返回ture 否则返回false;
     */
    public static boolean checkCutOffDayIsAvail(int cutDays, String cutoffTime, Date bizDate) {
        boolean result = false;
        if (cutDays <= DateUtil.getDay(DateUtil.getSystemDate(), bizDate)) {
            // 只有在当天的时候，才检查时间，否则是不检查时间过期的情况
            if (null != cutoffTime && cutDays == 
                DateUtil.getDay(DateUtil.getSystemDate(), bizDate)) {
                if (0 < cutoffTime.compareTo(DateUtil.formatTimeToString(
                    DateUtil.getSystemDate()))) {
                    result = true;
                }
            } else {
                result = true;
            }
        }
        return result;
    }

    /**
     *主要从当前数据库中找出这个配额的所有cutoffDay,然后检查有没有cutDay 这个天数的记录
     * 
     * @param lstOldCutoffDay
     *            这个配额的所有cutoffDay
     * @param cutDay
     *            cut 天数
     * @return 如果存在就返回找到的HtlCutoffDayQuota 否则返回null;
     */
    public static HtlCutoffDayQuota checkCutoffDayIsExist(List lstOldCutoffDay, int cutDay) {
        // 如果lstOldCutoffDay是空，则返回null
        if (null == lstOldCutoffDay || 0 == lstOldCutoffDay.size()) {
            return null;
        }
        for (int i = 0; i < lstOldCutoffDay.size(); i++) {
            HtlCutoffDayQuota cq = (HtlCutoffDayQuota) lstOldCutoffDay.get(i);
            if (cq.getCutoffDay() == cutDay) {
                return cq;
            }
        }
        return null;
    }

    public static boolean isPrepayMethod(String sMethod) {
        return null != sMethod && sMethod.endsWith("2");
    }

    public static HtlBatchSalePrice reCalculatePrice(HtlBatchSalePrice batchPrice) {
        return batchPrice;
    }

    public static HtlPrice reCalcAddScope(HtlPrice price) {
        return price;
    }

    public static int reCalcFreeShareQuota(HtlFreeOperate freeOperate) {
        List lstAssign = freeOperate.getLstBatchAssign();
        int allPrivate = 0; // 独占
        for (int i = 0; i < lstAssign.size(); i++) {
            HtlBatchAssign assign = (HtlBatchAssign) lstAssign.get(i);
            allPrivate += assign.getPrivateQuota();
        }
        // 共享数= 释放数量 - 所有独占数量之和。
        freeOperate.setShareQty(freeOperate.getFreeQty() - allPrivate);
        return freeOperate.getFreeQty() - allPrivate;
    }

    public static String ArrayToString(String[] str) {
        if (null == str) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            if (i == str.length - 1) {
                sb.append(str[i]);
            } else {
                sb.append(str[i]).append(",");
            }
        }
        return sb.toString();
    }

    // 临时取得会员列表
    public static List<CodeName> getTempList() {
        List<CodeName> lstMember = new ArrayList<CodeName>();
        CodeName cn = new CodeName();
        cn.setCode("1");
        cn.setName("CC");
        lstMember.add(cn);
        CodeName cn1 = new CodeName();
        cn1.setCode("2");
        cn1.setName("TMC");
        lstMember.add(cn1);
        CodeName cn2 = new CodeName();
        cn2.setCode("3");
        cn2.setName("B2B");
        lstMember.add(cn2);
        CodeName cn3 = new CodeName();
        cn3.setCode("4");
        cn3.setName("TP");
        lstMember.add(cn3);
        return lstMember;
    }

    // 临时取得会员扣减配额优先级别
    public static List<CodeName> getCutMemberQuotaLevel() {
        List<CodeName> lstLevel = new ArrayList<CodeName>();
        CodeName cn = new CodeName();
        cn.setCode("1");
        cn.setName("CC");
        lstLevel.add(cn);
        CodeName cn1 = new CodeName();
        cn1.setCode("2");
        cn1.setName("TMC");
        lstLevel.add(cn1);
        CodeName cn2 = new CodeName();
        cn2.setCode("3");
        cn2.setName("B2B");
        lstLevel.add(cn2);
        CodeName cn3 = new CodeName();
        cn3.setCode("4");
        cn3.setName("TP");
        lstLevel.add(cn3);
        return lstLevel;
    }

    /**
     * 通过共享方式判别是哪种类型,检查是不是面付独占,如果是分别独占，那也包括了面付独占
     * 
     * @param shareType
     * @return
     */
    public static boolean isFaceToPayQuota(String shareType) {
        boolean result = false;
        if (null == shareType) {
            return result;
        }
        if (shareType.equals("1") || shareType.equals("4")) {
            result = true;
        }
        return result;
    }

    /**
     * 通过共享方式判别是哪种类型,检查是不是预付独占,如果是分别独占，那也包括了预付独占
     * 
     * @param shareType
     * @return
     */
    public static boolean isPrepayQuota(String shareType) {
        boolean result = false;
        if (null == shareType) {
            return result;
        }
        if (shareType.equals("2") || shareType.equals("4")) {
            result = true;
        }
        return result;
    }

    /**
     * 通过共享方式判别是哪种类型,检查是不是面预付共享
     * 
     * @param shareType
     * @return
     */
    public static boolean isQuotaShare(String shareType) {
        boolean result = false;
        if (null == shareType) {
            return result;
        }
        if (shareType.equals("3")) {
            result = true;
        }
        return result;
    }

    /**
     * 通过共享方式判别是哪种类型,检查是不是面预付分别独占
     * 
     * @param shareType
     * @return
     */
    public static boolean isAlonePrivateQuota(String shareType) {
        boolean result = false;
        if (null == shareType) {
            return result;
        }
        if (shareType.equals("4")) {
            result = true;
        }
        return result;
    }

    /**
     * 取到面付独占的值
     * 
     * @return
     */
    public static String getFaceToPayQuotaValue() {
        return "1";
    }

    /**
     * 取到预付独占的值
     * 
     * @return
     */
    public static String getPrepayQuotaValue() {
        return "2";
    }

    /**
     * 检查这一天在不在这个cutoffDay的开始日期与结束日期并且是调整周当中
     * 
     * @param bizDate
     *            业务日期
     * @param BeginDate
     *            批次cutoffDay的开始日期
     * @param endDate
     *            批次cutoffDay的结束日期
     * @param weeks
     *            批次的调整周
     * @return 如果这一天在这条件当中，返回true 否则返回假
     */
    public static boolean bizDateInAdjustDates(Date bizDate, Date beginDate, Date endDate,
        String weeks) {
        boolean result = false;
        int ib = DateUtil.getDay(beginDate, bizDate);
        int ie = DateUtil.getDay(bizDate, endDate);
        if (0 <= ib && 0 <= ie) {
            if (StringUtil.isValidStr(weeks)) {
                String week = "" + DateUtil.getWeekOfDate(bizDate);
                if (0 <= weeks.indexOf(week)) {
                    result = true;
                }
            } else {
                result = true;
            }
        }
        return result;
    }

    public static boolean isAllWeek(String[] weeks) {
        return (null == weeks) || (null != weeks && 7 == weeks.length);
    }

    public static String getDefaultLastAssureTime() {
        return "18:00";
    }

    public static String getCreateChangePriceCode(String hotelCode) {
        return hotelCode + DateUtil.toStringByFormat(DateUtil.getSystemDate(), "yyyyMMddHHmmss");
    }

    public static String getAvailStatus() {
        return "A";
    }

    public static String getNotAvailStatus() {
        return "S";
    }

    public static String getChangePriceCode() {
        return "1";
    }

    public static String getGuanFangCode() {
        return "G";
    }

    public static String getKaiFangCode() {
        return "K";
    }

    public static String getHotelActive() {
        return "1";
    }

    public static String getTrueString() {
        return "1";
    }

    public static String getFalseString() {
        return "0";
    }

    public static String weekToString(String[] weeks) {
        StringBuilder weekStr = new StringBuilder();
        for (int i = 0; i < weeks.length; i++) {
            switch (Integer.valueOf(weeks[i])) {
            case 1:
                weekStr.append("星期一 ");
                break;
            case 2:
                weekStr.append("星期二 ");
                break;
            case 3:
                weekStr.append("星期三 ");
                break;
            case 4:
                weekStr.append("星期四 ");
                break;
            case 5:
                weekStr.append("星期五 ");
                break;
            case 6:
                weekStr.append("星期六 ");
                break;
            case 7:
                weekStr.append("星期日 ");
                break;
            }
        }
        return weekStr.toString();
    }
}
