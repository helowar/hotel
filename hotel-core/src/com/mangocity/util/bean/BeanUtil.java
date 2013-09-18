/*
 * Created on 2005-8-17
 */
package com.mangocity.util.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.mangocity.util.log.MyLog;

/**
 * @author xurong
 * 
 */
public class BeanUtil implements Serializable {
	private static final MyLog log = MyLog.getLogger(BeanUtil.class);
    /**
     * ����:����bean��null���Ե�Ĭ��ֵ
     * 
     * @param bean
     *            bean����
     */
    public static void SetNullDefault(Object bean) {
        if (null == bean)
            return;
        try {
            Class beanClass = bean.getClass();
            Field[] beanFields = beanClass.getDeclaredFields();
            for (int i = 0; i < beanFields.length; i++) {
                if (beanFields[i].getType().equals(String.class)
                    || beanFields[i].getType().equals(BigDecimal.class)
                    || beanFields[i].getType().equals(long.class)
                    || beanFields[i].getType().equals(int.class)
                    || beanFields[i].getType().equals(double.class)) {
                    java.beans.PropertyDescriptor p = new java.beans.PropertyDescriptor(
                        beanFields[i].getName(), beanClass);
                    Method readMethod = p.getReadMethod();
                    Object fieldValue = readMethod.invoke(bean, new Object[] {});
                    if (null == fieldValue) {
                        Method writeMethod = p.getWriteMethod();
                        // null���ַ�
                        if (beanFields[i].getType().equals(String.class)) {
                            writeMethod.invoke(bean, new Object[] { "" });
                        }
                        // null��BigDecimal
                        if (beanFields[i].getType().equals(BigDecimal.class)) {
                            writeMethod.invoke(bean, new Object[] { new BigDecimal(0) });
                        }
                        // null��Long
                        if (beanFields[i].getType().equals(long.class)) {
                            writeMethod.invoke(bean, new Object[] { Long.valueOf(0) });
                        }
                        // null��Double
                        if (beanFields[i].getType().equals(double.class)) {
                            writeMethod.invoke(bean, new Object[] { Double.valueOf(0) });
                        }
                    } else {
                        // ��ʼֵ��long
                        if (beanFields[i].getType().equals(long.class)) {
                            if (-9223372036854775808L == ((Long) fieldValue).longValue()) {
                                Method writeMethod = p.getWriteMethod();
                                writeMethod.invoke(bean, new Object[] { Long.valueOf(0) });
                            }
                        }
                        // ��ʼֵ��int
                        if (beanFields[i].getType().equals(int.class)) {
                            if (-9999 == ((Integer) fieldValue).intValue()) {
                                Method writeMethod = p.getWriteMethod();
                                writeMethod.invoke(bean, new Object[] { Integer.valueOf(0) });
                            }
                        }
                        // ��ʼֵ��double
                        if (beanFields[i].getType().equals(double.class)) {
                            if (0 == Double.compare(((Double) fieldValue).doubleValue(),
                                -99999999.99)) {
                                Method writeMethod = p.getWriteMethod();
                                writeMethod.invoke(bean, new Object[] { Double.valueOf(0) });
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        	log.error(e);
        }
    }

    /**
     * ����:����ͬ���͵�bean��Դbean������Ŀ��bean��ֻ������beanĬ��ֵ��ͬ������
     * 
     * @param srcBean
     *            Դbean
     * @param desBean
     *            Ŀ��bean
     */
    public static void CopySameTypeBean(Object srcBean, Object desBean) {
        if (null == srcBean || null == desBean)
            return;
        try {
            Class beanClass = srcBean.getClass();
            // ���Ĭ��ֵ��bean
            Object defaultBean = beanClass.newInstance();
            // ���bean����������
            Field[] beanFields = beanClass.getDeclaredFields();
            for (int i = 0; i < beanFields.length; i++) {

                java.beans.PropertyDescriptor p = new java.beans.PropertyDescriptor(beanFields[i]
                    .getName(), beanClass);
                Method readMethod = p.getReadMethod();
                // Դֵ
                Object srcValue = readMethod.invoke(srcBean, new Object[] {});
                // Ĭ��ֵ
                Object defValue = readMethod.invoke(defaultBean, new Object[] {});
                // ����ԤĬ��ֵ��ͬ��ֵ
                if (null != srcValue && !srcValue.equals(defValue)) {
                    Method writeMethod = p.getWriteMethod();
                    writeMethod.invoke(desBean, new Object[] { srcValue });
                }
            }
        } catch (Exception e) {
        	log.error(e);
        }
    }

}
