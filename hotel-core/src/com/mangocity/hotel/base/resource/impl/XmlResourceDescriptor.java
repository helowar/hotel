package com.mangocity.hotel.base.resource.impl;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @version 0.1
 * @author zhengxin⡣
 *  by zhengxin at 2006-7-10 ����02:14:23
 */
public class XmlResourceDescriptor// parasoft-suppress SERIAL.NSFSC "暂不修改"  
        extends GenericDescriptor implements Serializable {

    @Override
    public String getDescr(String statusName) {
        return (String) description.get(statusName);
    }

    @Override
    protected Map getResource() {

        return description;
    }

    @Override
    protected Map getResource(Map params) {
        return getDescription();

    }

}
