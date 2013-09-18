package com.mangocity.util.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mangocity.util.DateUtil;

/**
 */
public class MustDate implements Serializable {

    private Date mustDateT;

    private Date continueDate;

    private Date continueEndDate;

    private String weeks;

    public static final int DATE_TYPE = 0;

    public static final int WEEK_TYPE = 1;

    public static int getMustIndatesAndType(List<MustDate> mustInDates, String mustInDateString) {
        mustInDates.clear();
        String[] mustInDatesArray = mustInDateString.split("/");
        if (!mustInDateString.contains("*")) {// 对传统必住日期的封闭
            for (String inDate : mustInDatesArray) {
                String[] mustInDatesOld = inDate.split(",");
                for (String date : mustInDatesOld) {
                    MustDate md = new MustDate();
                    Date continueDate = DateUtil.getDate(date);
                    md.setContinueDate(continueDate);
                    mustInDates.add(md);
                }
            }
            return MustDate.DATE_TYPE;
        } else if (mustInDateString.contains("**")) {// 对必住日期的封闭
            for (String inDate : mustInDatesArray) {
                MustDate md = new MustDate();
                int endIndex = inDate.indexOf("**");
                Date continueDate = DateUtil.getDate(inDate.substring(0, endIndex));
                md.setContinueDate(continueDate);
                mustInDates.add(md);
            }
            return MustDate.DATE_TYPE;
        } else {// 对必住星期的封闭
            for (String inDate : mustInDatesArray) {
                String[] elements = inDate.split("\\*");
                MustDate md = new MustDate();
                md.setContinueDate(DateUtil.getDate(elements[0]));
                md.setContinueEndDate(DateUtil.getDate(elements[1]));
                md.setWeeks(elements[2]);
                mustInDates.add(md);
            }
            return MustDate.WEEK_TYPE;
        }
    }

    public Date getContinueEndDate() {
        return continueEndDate;
    }

    public void setContinueEndDate(Date continueEndDate) {
        this.continueEndDate = continueEndDate;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public Date getMustDateT() {
        return mustDateT;
    }

    public void setMustDateT(Date mustDateT) {
        this.mustDateT = mustDateT;
    }

    public Date getContinueDate() {
        return continueDate;
    }

    public void setContinueDate(Date continueDate) {
        this.continueDate = continueDate;
    }
}
