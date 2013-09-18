package com.mangocity.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.mangocity.util.log.MyLog;

/**
 * 反射Object ,实现类的copy
 * 
 * @author shizhongwen 日期:Mar 11, 2009 时间:5:23:58 PM
 */
public class ReflectObject {
	private static final MyLog log = MyLog.getLogger(ReflectObject.class);
    public static Object copy(Object object) throws Exception {
        // 获得对象的类型
        Class<?> classType = object.getClass();
        log.info("Class:" + classType.getName());

        // 通过默认构造方法创建一个新的对象
        Object objectCopy = classType.getConstructor(new Class[] {}).newInstance(new Object[] {});

        // 获得对象的所有属性
        Field[] fields = classType.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            String fieldName = field.getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            // 获得和属性对应的getXXX()方法的名字
            String getMethodName = "get" + firstLetter + fieldName.substring(1);
            // 获得和属性对应的setXXX()方法的名字
            String setMethodName = "set" + firstLetter + fieldName.substring(1);

            // 获得和属性对应的getXXX()方法
            Method getMethod = classType.getMethod(getMethodName, new Class[] {});
            // 获得和属性对应的setXXX()方法
            Method setMethod = classType.getMethod(setMethodName, new Class[] { field.getType() });

            // 调用原对象的getXXX()方法
            Object value = getMethod.invoke(object, new Object[] {});
            log.info(fieldName + ":" + value);
            // 调用拷贝对象的setXXX()方法
            setMethod.invoke(objectCopy, new Object[] { value });
        }
        return objectCopy;
    }
}
