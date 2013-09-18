package com.mangocity.util.web;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

/**
 * 主要是用于BeanUtils类，提供的日期转换插件机制。
 */
public class DateConverter implements Converter, Serializable {
    public Object convert(Class aClass, Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Date) {
            return value;
        }

        try {

            String str = value.toString();
            if (str.equals("yyyy-mm-dd")) {
                return null;
            }
            if (StringUtil.isValidStr(str)) {
                if (0 <= str.indexOf(':')) { // 长日期
                    return DateUtil.stringToDatetime(str);
                } else { // 短日期
                    return DateUtil.getDate(str);
                }
            }

            return null;
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }
}
