package com.mangocity.util;

import java.util.Collection;
import java.util.Map;

/**
 * 提供一些通用的数据校验方法，包括校验字符串是否为null或者空字符串，校验集合对象是否为null或者集合中的元素是否0，
 * 校验电子邮件地址是否有效，等等。
 * 
 * @author pizhenghua
 *
 */
public class ValidationUtil {
	
	/**
	 * 对象是否为空
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Object obj) {
		if (obj instanceof String) {
			return isEmpty((String)obj);
		} else if (obj instanceof Collection) {
			return isEmpty((Collection)obj);
		} else if (obj instanceof Map) {
			return isEmpty((Map)obj);
		}else if (obj instanceof Object[]) {
			return isEmpty((Object[])obj);
		} else {
			return isNull(obj);
		}		
	}
	
	/**
	 * 字符串是否为null或者空字符串
	 * 
	 * @param aStr
	 * @return
	 */
	public static boolean isEmpty(String strObject) {
		return (strObject == null || strObject.trim().equals(""));
	}

	/**
	 * 实现了Collection接口的对象是否为空
	 * 
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Collection c) {
		return (c == null || c.size() == 0);
	}
	
	/**
	 * 实现了Map接口的对象是否为空
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Map map) {
		return (map == null || map.size() == 0);
	}
	
	/**
	 * 数组对象是否为空
	 * 
	 * @param objArray
	 * @return
	 */
	public static boolean isEmpty(Object objArray[]) {		
		return (objArray == null || objArray.length == 0);
	}
	
	/**
	 * 对象是否为null
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return (obj == null);
	}
	
	/**
	 * 输入的电子邮件地址是否有效
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmailAddress(String email) {
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[_A-Za-z-]+)";
		return email.matches(regex);
	}

}
