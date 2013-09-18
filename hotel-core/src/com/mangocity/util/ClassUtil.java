package com.mangocity.util;

import java.io.Serializable;

import com.mangocity.util.exception.ClassInstantiateException;

/**
 */
public class ClassUtil implements Serializable {

    public static Object newInstance(Class entityClass) {

        Object obj;
        try {
            obj = entityClass.newInstance();
        } catch (Exception e) {
            throw new ClassInstantiateException(e);
        }

        return obj;
    }

    public static Object newInstance(String className) {

        Object obj;
        try {
            obj = Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new ClassInstantiateException(e);
        }

        return obj;
    }

}
