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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mangocity.util.log.MyLog;

/**
 *  myoa
 * @version 0.1
 * @author zhengxin
 *  at 2006-7-4 20:57:58
 * : provide common mehtod for string operations;
 *  by zhengxin
 */
public class StringUtil implements Serializable {
	private static final MyLog log = MyLog.getLogger(StringUtil.class);
    /**
     * ת��һ��յ��ַ�
     * 
     * @param str
     * @return
     */
    public static String convertStringIfNull(String str) {
        return null == str ? StringConstants.EMPTY_STRING : str;
    }

    /**
     * 判断是否是有效的字符串，空串为无效串
     * 
     * @param str
     * @return
     */
    public static boolean isValidStr(String str) {
        return null != str && 0 < str.trim().length();
    }

    /**
     * 将字符串的换行替换成HTML的换行符号
     * 
     * @param str
     * @return
     */
    public static String formatHtmlString(String str) {

        return str.replaceAll("\r\n", "<BR/>");
    }

    /**
     * 将数组中的字符，连成以逗号分隔的形式 如果 obj 为null，并不抛出异常，直接返回为空
     * 
     * @param obj
     * @return
     */
    public static String joinStr(Object[] obj) {
        if (null == obj)
            return null;

        StringBuffer buffer = new StringBuffer();

        if (0 < obj.length)
            buffer.append(obj[0]);

        for (int m = 1; m < obj.length; m++) {
            buffer.append(StringConstants.COMMA).append(obj[m]);
        }

        return buffer.toString();
    }

    /**
     * 根据字符串转换为布尔值ֵ
     * 
     * @param str
     * @return
     */
    public static boolean getBooleanValue(String str) {
        return isValidStr(str) ? str.toLowerCase().trim().equals(StringConstants.TRUE) : false;
    }

    public static int getIntValue(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        	log.error("StringUtil getIntValue Exception",e);
            return defaultValue;
        }
    }

    public static BigDecimal getBigDecimal(String param) {
        if (StringUtil.isValidStr(param)) {
            try {
                return new BigDecimal(param);
            } catch (NumberFormatException e) {
            	log.error("StringUtil getBigDecimal NumberFormatException",e);
            }
        }

        return new BigDecimal(-1);
    }

    public static String describe(Object[] values) {
        StringBuffer buff = new StringBuffer();

        for (int m = 0; m < values.length; m++) {
            buff.append(values[m]).append(", ");
        }

        return buff.toString();
    }
    /**
     * 把一个二进制的字符串转换成long类型的数字 added by xieyanhui
     * 
     * @param value 二进制字符串
     * @return long
     */
    public static long getBinaryStrToLong(String value) {
		long result = 0;
        if (null == value || (null != value && value.equals(""))){
        	log.error("StringUtil binaryStrToLong Exception：the paramter is null or empty");
            return result;
        }
		long mul = 1;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(value);
		if (isNum.matches()) {
			int length = value.length();
			for (int i = length - 1; i >= 0; i--) {
				result += mul * (value.charAt(i) == '1' ? 1 : 0);
				mul *= 2;
			}
		} else {
			log.error("StringUtil binaryStrToLong Exception：the paramter is not a number string!");
		}
		return result;
	}
    
    public static long getStrTolong(String value) {
        long result = 0;
        if (null == value || (null != value && value.equals("")))
            return result;

        try {
            result = Long.parseLong(value);
        } catch (Exception e) {
        	log.error("StringUtil getStrTolong Exception",e);
        }
        return result;
    }

    public static double getStrTodouble(String value) {
        double result = 0;
        if (null == value || (null != value && value.equals("")))
            return result;
        try {
            result = Double.parseDouble(value);
        } catch (Exception e) {
        	log.error("StringUtil getStrTodouble Exception",e);
        }
        return result;
    }

    /**
     * 比较两个字符串是否相等
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean StringEquals(String str1, String str2) {
        if ((null == str1 || 0 == str1.length()) && (null == str2 || 0 == str2.length())) {
            return true;
        }
        return null != str1 && null != str2 && str1.equals(str2);
    }

    /**
     * 比较两个字符串是否相等
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean StringEquals1(String str1, String str2) {
        if ((null == str1 || 0 == str1.length()) && (null != str2 && 0 < str2.length())
            || (null != str1 && 0 < str1.length()) && (null == str2 || 0 == str2.length())) {
            return false;
        }
        if (null == str1 && null == str2) {
            return true;
        }
        return str1.equals(str2);
    }

    /**
     * 比较两个日期是否相等
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean DateEquals1(Date date1, Date date2) {
        if ((null == date1) && (null != date2) || (null != date1) && (null == date2)) {
            return false;
        }
        if (null == date1 && null == date2) {
            return true;
        }

        return 0 == date1.compareTo(date2);
    }

    /**
     * 比较两个字符串是否相等
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean StringEquals2(String str1, String str2) {
        if (null == str1 && null == str2) {
            return false;
        }
        return StringEquals(str1, str2);
    }

    public static String toZeroIfNull(String str) {
        return null == str ? "00:00" : str;
    }
    
    /**
     * 四舍五入保留N 为小数
     * 
     * add by shengwei.zuo 2010-1-19
     * 
     * @param x
     * @param n
     * @return
     */
    public static double Baoliu(double x,int n){ 
    	return new BigDecimal(x).setScale(n, RoundingMode.HALF_UP).doubleValue();
    } 
    
    /**
     * 替换特殊字符
     * @param srcStr
     * @return
     */
    public static String replaceSpecialStr(String srcStr){
    	srcStr = srcStr.replaceAll("\r\n", "<br/>&nbsp;&nbsp;");
    	srcStr = srcStr.replaceAll("\n\r", "&nbsp;&nbsp;<br/>");
    	srcStr = srcStr.replaceAll("\r", "<br/>");
    	srcStr = srcStr.replaceAll("\n", "&nbsp;&nbsp;");
    	srcStr = srcStr.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
    	return srcStr;
    }
    
    /**
     * 删除字符串最后一个字符
     * @param srcStr
     * @param charToDelete
     */
    public static String deleteLastChar(String srcStr,char charToDelete){
    	if(isValidStr(srcStr) && srcStr.lastIndexOf(charToDelete) == srcStr.length() - 1){
    		srcStr = srcStr.substring(0, srcStr.length() - 1);
    	}
    	return srcStr;
    }
    
    /**

     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN    add by shengwei.zuo  2010-6-4

     * @param v 需要四舍五入的数字

     * @param scale 小数点后保留几位

     * @return 四舍五入后的结果

     */

    public static double roundDouble(double v,int scale)

    {
        return roundBaseFun(v, scale, BigDecimal.ROUND_HALF_EVEN);
    }
    
    
    /**

     * 提供精确的小数位四舍五入处理  add by shengwei.zuo  2010-6-4

     * @param v 需要四舍五入的数字

     * @param scale 小数点后保留几位

     * @param round_mode 指定的舍入模式

     * @return 四舍五入后的结果

     */

    public static double roundBaseFun(double v, int scale, int round_mode)
    {
       if(scale<0)
    	   
       {
    	   
           throw new IllegalArgumentException("The scale must be a positive integer or zero");
           
       }
       
       BigDecimal b = new BigDecimal(Double.toString(v));

       return b.setScale(scale, round_mode).doubleValue();

    }
    
    
	/**
    	 * 根据数组转换成sql条件的stringBuffer
    	 * @param buffer
    	 * @param params
    	 * @param field
    	 */
    	public static void compositeHSql(StringBuffer buffer, Object[] params, String field) {

            if (null != params) {
                if (1 == params.length) {
                    buffer.append(StringConstants.AND).append(field).append("  = ? ");

                } else if (1 < params.length) {

                    buffer.append(StringConstants.AND).append(field).append(StringConstants.SPACE)
                        .append(" in ( ? ");
                    for (int m = 1; m < params.length; m++) {
                        buffer.append(" , ? ");
                    }

                    buffer.append(" ) ");

                }
                buffer.append(StringConstants.SPACE);
            }
        }
    	
    	
    public static long setStringToLong(String strVal){
    	BigDecimal  sumRmbSl =  new  BigDecimal(strVal);  
    	return sumRmbSl.longValue();
    }
    
    public static double setStringToDouble(String strVal){
    	BigDecimal  sumRmbSl =  new  BigDecimal(strVal);  
    	return sumRmbSl.doubleValue();
    }
    
    public static int setStringToInt(String strVal){
    	BigDecimal  sumRmbSl =  new  BigDecimal(strVal);  
    	return sumRmbSl.intValue();
    }
    
}
