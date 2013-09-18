package com.mangocity.hotel.base.resource.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mangocity.hotel.base.resource.IResourceDescriptor;
import com.mangocity.hotel.base.resource.ResourceDescriptorFactory;
import com.mangocity.util.collections.FormatMap;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public abstract class GenericDescriptor implements IResourceDescriptor, Serializable {
	private static final MyLog log = MyLog.getLogger(GenericDescriptor.class);

    protected DAOIbatisImpl queryDao;// parasoft-suppress SERIAL.NSFSC "JAR 包问题" 

    protected String queryID;

    protected ResourceDescriptorFactory factory;// parasoft-suppress SERIAL.NSFSC "JAR 包问题" 

    /**
     * 缓存数据，以后要修改成使用ehcache
     */
    protected static Map cacheData = new HashMap();

    /**
     * 是数据库，还是其它的类型
     */
    protected String type;

    /**
     * 是否缓存
     */
    protected boolean cacheAllowed = true;

    /**
     * 资源名称
     */
    protected String name;

    protected Map description = new OrderedMap();

    public String getDescr(String statusName) {
        return (String) description.get(statusName);
    }

    public void setFactory(ResourceDescriptorFactory factory) {
        this.factory = factory;
    }

    public Map getDescription() {

        return getResource();

    }

    abstract protected Map getResource();

    abstract protected Map getResource(Map params);

    public Map getDescription(Map params) {

        if (null == params)
            params = new FormatMap();

        return getResource(params);

    }

    public boolean isCacheAllowed() {
        return cacheAllowed;
    }

    public void setCacheAllowed(boolean cache) {
        this.cacheAllowed = cache;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueryID() {
        return queryID;
    }

    public void setQueryID(String queryID) {
        this.queryID = queryID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(Map description) {
        this.description = description;
    }

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

}
