package com.mangocity.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public class ListUtil implements Serializable {

    public static List itTo(Iterator it) {
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            list.add(o);
        }
        return list;
    }

    public static List getFirstElementOfObjects(List src) {
        List list = new ArrayList<Object>();
        for (int i = 0; i < src.size(); i++) {
            Object o = src.get(i);
            if (o instanceof Object[]) {
                Object[] objects = (Object[]) o;
                list.add(objects[0]);
            } else {
                list.add(o);
            }
        }
        return list;
    }
}
