/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.mangocity.util.collections;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mangocity.util.DateUtil;
import com.mangocity.util.StringConstants;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;

/**
 *  MyOA
 * @version
 * @author zhengxin
 *  at 2006-7-6 15:09:49
 * : advanced map collection. which extends hashMap, but format the data from map
 *           automatically;
 *  by zhengxin at 2006-7-6 15:09:49
 */
public class FormatMap extends HashMap implements Serializable {
	private static final MyLog log = MyLog.getLogger(FormatMap.class);
    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static Date INVALID_DATE = null;

    public static int MIN_DATE_LENGTH = 8;

    public FormatMap() {
        super();
    }

    public FormatMap(Object key, Object obj) {
        super();

        this.put(key, obj);
    }

    public FormatMap(Map map) {
        super();

        if (null == map)
            return;

        super.putAll(map);
    }

    public Date toDate(Object key) {
        return getDate(key, DATE_FORMAT);
    }

    public Date toDateTime(Object key) {
        return getDate(key, DATE_TIME_FORMAT);
    }

    public Date getDate(Object key, String format) {
        String str = (String) this.get(key);
        if (null == str || str.length() < MIN_DATE_LENGTH)
            return INVALID_DATE;

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
        	log.error(e);
            return INVALID_DATE;
        }
    }

    public String dateToString(Object key, String format) {
        Object obj = this.get(key);

        if (null == obj)
            return StringConstants.EMPTY_STRING;

        Date date = null;

        if (obj instanceof String) {

            String value = (String) obj;
            if (StringUtil.isValidStr(value))
                date = DateUtil.getDate(value);
            else
                return value;
        } else if (obj instanceof Date) {
            date = (Date) obj;
        } else
            return "invalid date obj";

        return DateUtil.toStringByFormat(date, format);
    }

    public String dateToString(Object key) {
        return dateToString(key, DATE_FORMAT);
    }

    public String dateTimeString(Object key) {
        return dateToString(key, DATE_TIME_FORMAT);
    }

    /**
     * this method gurantee you should never get null value from Map.
     * 
     * @param key
     * @return
     */
    public String getString(Object key) {
        Object obj = this.get(key);

        if (null == obj)
            return StringConstants.EMPTY_STRING;

        try {
            if (obj instanceof String[])
                return ((String[]) obj)[0];

            String str = (String) this.get(key);
            return StringUtil.convertStringIfNull(str);
        } catch (ClassCastException e) {
            return obj.toString();
        }
    }

    public Integer getInteger(Object key) {
        String str = (String) this.get(key);

        return Integer.valueOf(str);
    }

    public int getInt(Object key) {
        String str = (String) this.get(key);

        if (StringUtil.isValidStr(str)) {
            return Integer.parseInt(str);
        }

        return -1;
    }

    public List getList(Object key) {
        Object value = this.get(key);

        return null == value ? Collections.EMPTY_LIST : (List) value;
    }

}
