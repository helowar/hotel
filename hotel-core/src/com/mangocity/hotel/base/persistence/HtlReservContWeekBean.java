package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

/**
 * 预定条款,必住日期增加星期的选择 这个类主要是用来收集起止时间和选择的星期 从中筛选出合适的日期,增加到原来的必住日期集合中 所以这个类只是一个临时中间件
 * 
 * @author chenjuesu add 2009-7-30
 */
public class HtlReservContWeekBean implements Serializable {

    /*
     * 必住星期的开始日期
     */
    private Date continueWeekDateBegin;

    /*
     * 必住星期的结束日期
     */
    private Date continueWeekDateEnd;

    /*
     * 必住的星期集合
     */
    private String[] continueWeeks;

    private final String DEFAULTWEEKS = "1,2,3,4,5,6,7";

    /**
     * 返回合适的必住日期HtlReservContTemplate
     * 
     * @return List<HtlReservContTemplate>
     */

    public List<HtlReservContTemplate> getHtlReservContTemplate() {
        List<HtlReservContTemplate> matchsContDates = new ArrayList<HtlReservContTemplate>();
        if (null == continueWeeks && null == continueWeekDateBegin && null == continueWeekDateEnd)
            return matchsContDates;
        Date[] dates = DateUtil.getDateWithWeek(continueWeekDateBegin, continueWeekDateEnd,
            continueWeeks);
        for (Date date : dates) {
            HtlReservContTemplate hct = new HtlReservContTemplate();
            hct.setContinueDate(date);
            matchsContDates.add(hct);
        }
        return matchsContDates;
    }

    /**
     * 增加从星期中筛选出来的必住日期 addby juesuchen V2.9.2 2009-7-30
     * 
     * @param listHtlReservContTplt2
     * @param htlWeekBeans
     */
    public void addHtlReservContDateByWeek(List listHtlReservContTplt2, Class reservContType) {
        // TODO Auto-generated method stub
        List<HtlReservContTemplate> matchsContDates = getHtlReservContTemplate();
        // 需要把模板转化为目标类
        // 如果是模板
        if (reservContType.toString().contains("HtlReservContTemplate")) {
            for (HtlReservContTemplate template : matchsContDates) {
                if (!listHtlReservContTplt2.contains(template))// 如果不存在必住日期,则加入集合中
                    listHtlReservContTplt2.add(template);
            }
            // 如果是批次
        } else if (reservContType.toString().contains("HtlReservContBatch")) {
            for (HtlReservContTemplate template : matchsContDates) {
                HtlReservContBatch htlBatch = new HtlReservContBatch();
                htlBatch.setContinueDate(template.getContinueDate());
                if (!listHtlReservContTplt2.contains(htlBatch))// 如果不存在必住日期,则加入集合中
                    listHtlReservContTplt2.add(htlBatch);
            }// 如果是每天表
        } else if (reservContType.toString().contains("HtlReservCont")) {
            for (HtlReservContTemplate template : matchsContDates) {
                HtlReservCont htlReser = new HtlReservCont();
                htlReser.setContinueDate(template.getContinueDate());
                if (!listHtlReservContTplt2.contains(htlReser))// 如果不存在必住日期,则加入集合中
                    listHtlReservContTplt2.add(htlReser);
            }// 如果是每天表
        }
    }

    /**
     * 返回星期的字符串形式
     * 
     * @return
     */
    public String getStringContinueWeeks() {
        if (null != continueWeeks) {
            return StringUtil.joinStr(continueWeeks);
        }
        return getDEFAULTWEEKS();
    }

    public Date getContinueWeekDateBegin() {
        return continueWeekDateBegin;
    }

    public void setContinueWeekDateBegin(Date continueWeekDateBegin) {
        this.continueWeekDateBegin = continueWeekDateBegin;
    }

    public Date getContinueWeekDateEnd() {
        return continueWeekDateEnd;
    }

    public void setContinueWeekDateEnd(Date continueWeekDateEnd) {
        this.continueWeekDateEnd = continueWeekDateEnd;
    }

    public String[] getContinueWeeks() {
        return continueWeeks;
    }

    public void setContinueWeeks(String[] continueWeeks) {
        this.continueWeeks = continueWeeks;
    }

    public String getDEFAULTWEEKS() {
        return DEFAULTWEEKS;
    }
}
