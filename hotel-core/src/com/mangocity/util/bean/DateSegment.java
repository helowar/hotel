package com.mangocity.util.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 日期段 用于封装起始日期
 */
public class DateSegment implements Serializable {

    private Date start;

    private Date end;

    public Date getEnd() {
        // SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-mm-dd" );
        // String formatDate = formatter.format(end);
        // Date sometime = formatter.parse(formatDate);

        return end;// sometime;
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

}
