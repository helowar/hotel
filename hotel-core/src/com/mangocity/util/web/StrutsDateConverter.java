package com.mangocity.util.web;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.mangocity.util.DateUtil;

/**
 * 
 * Struts2的自定义日期转换类，<br>
 * 用于从页面JSP到Action里的Date类型的类变量转换<br>
 * 处理繁体IE, IE7的情况
 * 
 * @author chenkeming
 * 
 */
public class StrutsDateConverter extends StrutsTypeConverter implements Serializable {

    private static final String DATE_FOMART_IE = "yyyy-MM-dd";

    private static final String DATE_FOMART_FF = "yy/MM/dd";

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        Date date = null;
        String dateString = null;
        if (null != values && 0 < values.length) {
            dateString = values[0];
            if (null != dateString) {
                // 匹配IE浏览器
                SimpleDateFormat format = new SimpleDateFormat(DATE_FOMART_IE, Locale.CHINA);
                try {
                    date = format.parse(dateString);
                } catch (ParseException e1) {
                    date = null;
                }

                // 匹配Firefox浏览器
                if (null == date) {
                    format = new SimpleDateFormat(DATE_FOMART_FF, Locale.CHINA);
                    try {
                        date = format.parse(dateString);
                    } catch (ParseException e) {
                        date = null;
                    }
                }
            }
        }
        return date;
    }

    @Override
    public String convertToString(Map context, Object o) {
        Date date = (Date) o;
        return DateUtil.dateToString(date);
    }

}
