package com.mangocity.util.dao.paging;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.mangocity.util.log.MyLog;

/**
 * 设置sqlExecutor成员
 * 
 * @author chenkeming
 * 
 */
public class ReflectUtil implements Serializable {

	private static final long serialVersionUID = -2255717473097181577L;
	
	private static final MyLog log = MyLog.getLogger(ReflectUtil.class);
	@SuppressWarnings("unchecked")
    public static void setFieldValue(Object target, String fname, Class ftype, Object fvalue) {
        if (null == target || null == fname || "".equals(fname)
            || (null != fvalue && !ftype.isAssignableFrom(fvalue.getClass()))) {
            return;
        }
        Class clazz = target.getClass();
        try {
            Method method = clazz.getDeclaredMethod("set" + Character.toUpperCase(fname.charAt(0))
                + fname.substring(1), ftype);
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            method.invoke(target, fvalue);

        } catch (Exception me) {
                log.debug(me);
            try {
                Field field = clazz.getDeclaredField(fname);
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                field.set(target, fvalue);
            } catch (Exception fe) {
                    log.debug(fe);
            }
        }
    }
}
