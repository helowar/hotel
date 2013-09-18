package com.mangocity.util.bean;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class NewDateSegment implements Serializable {
    private Date start;

    private Date end;

    private String[] week;

    private Integer[] intWeek;

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;

        if (null != week && 0 < week.length) {
            intWeek = new Integer[week.length];
            for (int m = 0; m < week.length; m++) {
                intWeek[m] = Integer.valueOf(week[m]);
            }
        }
    }

    public Integer[] getIntWeek() {
        return intWeek;
    }
}
