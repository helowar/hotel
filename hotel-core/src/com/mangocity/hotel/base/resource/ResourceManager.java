package com.mangocity.hotel.base.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.mangocity.hotel.base.resource.impl.DbResourceDescriptor;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.exception.ConfigException;
import com.mangocity.util.log.MyLog;

/**
 * @author zhengxin
 *  用于加载从各种数据源中加载基础数据到前端显示
 */
public class ResourceManager implements Serializable {

	private static final MyLog log = MyLog.getLogger(ResourceManager.class);

    private ResourceDescriptorFactory resourceFactory;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    private Cache cache;

    public String getDescription(String resourceID, String value) {
        Map resourceMap = getResourceMapByParam(resourceID, null);
        return (String) resourceMap.get(value);
    }

    public String getDescription(String resourceID, long value) {
        Map resourceMap = getResourceMapByParam(resourceID, null);
        return (String) resourceMap.get(Long.toString(value));
    }

    /**
     * 根据资源ID获得描述
     * 
     * @param resourceID
     * @return Map 以Map的形式返回资源的描述数据，
     * @throws Exception
     */
    public Map getDescription(String resourceID) {
        Map data = this.getDataFromCache(resourceID);

        if (null == data) {

            IResourceDescriptor descriptor = resourceFactory.getResouceDescriptor(resourceID);

            if (null == descriptor)
                throw new ConfigException("can not find resource by resourceID:" + resourceID);
            data = descriptor.getDescription();

            this.cacheData(resourceID, data);
        }

        return data;

    }

    public Collection getResource(String resourceID) {

        Map map = this.getDescription(resourceID);

        return convertList(map);
    }

    public Collection getResourceByParam(String resourceID, Map params) {

        Map map = this.getResourceMapByParam(resourceID, params);

        return convertList(map);
    }

    public Map getResourceMapByParam(String resourceID, Map params) {

        String key = resourceID + (null == params ? "" : params.toString());

        Map data = this.getDataFromCache(key);

        if (null == data) {

            IResourceDescriptor descriptor = resourceFactory.getResouceDescriptor(resourceID);

            if (null == descriptor)
                throw new ConfigException("can not find resource by resourceID:" + resourceID);
            data = descriptor.getDescription(params);
            if (!(descriptor instanceof DbResourceDescriptor)) {
                this.cacheData(key, data);
            }
        }

        return data;
    }

    /**
     * 重新刷新数据
     * 
     * @param resourceID
     */
    public void refresh(String resourceID) {
        this.getCache().remove(resourceID);
    }

    /**
     * 根据不同的查询参数，刷新数据
     * 
     * @param resourceID
     * @param params
     */
    public void refresh(String resourceID, Map params) {
        String key = resourceID + params.toString();

        this.getCache().remove(key);
    }

    private List convertList(Map resources) {
        if (resources.isEmpty())
            return Collections.EMPTY_LIST;

        OrderedMap map = (OrderedMap) resources;
        List results = new ArrayList();

        Iterator iter = map.iterator();

        while (iter.hasNext()) {
            Object obj = iter.next();

            if (null != obj) {
                Object key = map.get(obj);

                String name = null == key ? "" : key.toString();

                results.add(new Property(obj.toString(), name));
            }
        }

        return results;

    }

    public ResourceDescriptorFactory getResourceFactory() {
        return resourceFactory;
    }

    public void setResourceFactory(ResourceDescriptorFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * 从缓存中获取数据
     * 
     * @param key
     * @return
     */
    protected Map getDataFromCache(String key) {

        try {
            Element element = cache.get(key);

            if (null != element)
                return (Map) element.getValue();

        } catch (Exception e) {
            log.error("从缓存中获取数据时，发生异常：" + e.getMessage());
            log.error("从缓存中获取数据时，发生异常：",e);
        }

        /** 没有重新抛出异常，是因为系统可以重新再从数据库或XML中读取数据，而不致于出现中断 */
        return null;
    }

    protected void cacheData(String key, Map data) {

        Element element = new Element(key, (Serializable) data);

        cache.put(element);

    }

    public void clearCache(String key) {
        cache.remove(key);
    }

}
