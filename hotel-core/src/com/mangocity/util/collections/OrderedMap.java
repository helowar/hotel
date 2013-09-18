package com.mangocity.util.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Extention of <code>java.util.HashMap</code> that can retain original order elements were add in.
 * 
 * <p>
 * Uses an internal <code>java.util.LinkedList</code> which can be iterated through by accessing
 * {@link #iterator()}.
 * </p>
 * 
 * @see java.util.HashMap
 */
public class OrderedMap extends HashMap implements Serializable {
    // ~ Instance fields ////////////////////////////////////////////////////////

    private List keyOrder = new LinkedList();

    // ~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Overrides superclass.
     */
    public void clear() {
        keyOrder.clear();
    }

    /**
     * Return keys in original order.
     * 
     * @return Iteration of keys.
     */
    public Iterator iterator() {
        return keyOrder.iterator();
    }

    /**
     * Overrides superclass.
     */
    public Object put(Object key, Object value) {
        keyOrder.add(key);

        return super.put(key, value);
    }

    public Object get(Object key) {
        if (key instanceof String) {
            String keyStr = (String) key;

            String[] keys = keyStr.split(",");

            StringBuffer buffer = new StringBuffer();

            Object obj = super.get(keys[0]);
            buffer.append(null == obj ? "" : obj);
            for (int m = 1; m < keys.length; m++) {
                obj = super.get(keys[m]);
                buffer.append(",").append(null == obj ? "" : obj);
            }

            return buffer.toString();
        }

        return super.get(key);
    }

    /**
     * Overrides superclass.
     */
    public Object remove(Object key) {
        keyOrder.remove(key);

        return super.remove(key);
    }

    public List toList() {
        List results = new ArrayList();

        Iterator iter = iterator();

        while (iter.hasNext()) {
            Object obj = iter.next();
            results.add(this.get(obj));
        }

        return results;

    }

    public Object[] toArray() {
        return this.toList().toArray();
    }

}