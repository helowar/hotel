package com.mangocity.hotel.base.resource.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.util.StringUtil;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.log.MyLog;

/**
 * 调用 EJB 远程基础数据
 * 
 * @author yuexiaofeng
 * 
 */
public class BaseDataResourceDescriptor// parasoft-suppress SERIAL.NSFSC "JAR 包问题" 
        extends GenericDescriptor implements Serializable {
	
    /**
     * 
     */
    private static final long serialVersionUID = -7723374207922499438L;
    private static final MyLog log = MyLog.getLogger(BaseDataResourceDescriptor.class);

    @Override
    public String getDescr(String statusName) {
        return (String) description.get(statusName);
    }

    @Override
    protected Map getResource() {
        description = this.loadFromDatabase(queryID);
        return description;
    }

    @Override
    protected Map getResource(Map params) {
        return loadFromDatabase(queryID, params);

    }

    protected Map loadFromDatabase(String queryID) {
        return loadFromDatabase(queryID, new HashMap());
    }

    /**
     * 从基础数据库读取资源
     * 
     * @param queryID
     * @param params
     * @return
     */
    protected Map loadFromDatabase(String queryID, Map params) {
        Map descr = new OrderedMap();
        try {
            String parentID = null;
            if (!StringUtil.isValidStr(queryID)) {
                log.error("查询ID不能为空");
                return Collections.EMPTY_MAP;
            } else if (BaseConstant.COMMON_DATA_FLAG.equalsIgnoreCase(queryID)) {
                if (null != params && params.containsKey("type")) {
                    if (BaseConstant.COUNTRY_FLAG.equalsIgnoreCase(params.get("type").toString())) {
                        descr = coversionMap(InitServlet.CountryObj,
                            descr);
                    } else if (BaseConstant.PROVINCE_FLAG.equalsIgnoreCase(params.get("type")
                        .toString())) {
                        if (params.containsKey("parent") 
                                && null != params.get("parent").toString()) {
                            // 需求二:有国家级别
                            parentID = params.get("parent").toString();
                            descr = getMap(InitServlet.mapProvinceObj,
                                parentID, descr);
                        } else {
                            // 需求一:没有国家级别
                            descr = coversionMap(InitServlet.localProvinceObj, descr);
                        }
                    } else if (BaseConstant.CITY_FLAG.equalsIgnoreCase(params.get("type")
                        .toString())) {
                        if (params.containsKey("parent") 
                                && null != params.get("parent").toString()) {
                            // 仅仅封装省份对应的城市列表
                            parentID = params.get("parent").toString();
                            descr = getMap(InitServlet.mapCityObj,
                                parentID, descr);
                        } else {
                            // 封装所有的城市列表包括国内、国外,主要完成网站需求
                            descr = coversionMap(InitServlet.localCityObj, descr);
                        }
                    } else if (BaseConstant.DISTRICT_FLAG.equalsIgnoreCase(params.get("type")
                        .toString())) {
                        parentID = params.get("parent").toString();
                        descr = getMap(InitServlet.mapCitySozeObj,
                            parentID, descr);
                    } else if (BaseConstant.BUSINESS_FLAG.equalsIgnoreCase(params.get("type")
                        .toString())) {
                        parentID = params.get("parent").toString();
                        descr = getMap(InitServlet.mapBusinessSozeObj,
                            parentID, descr);
                    } else if (BaseConstant.SALOON_FLAG.equalsIgnoreCase(params.get("type")
                        .toString())) {
                        parentID = params.get("parent").toString();
                        descr = getMap(InitServlet.mapSaloonSozeObj,
                            parentID, descr);
                    } else if (BaseConstant.DELIVERY_CITY_FLAG.equalsIgnoreCase(params.get("type")
                        .toString())) {
                        descr = coversionMap(InitServlet.diliveryCityObj, descr);
                    } else if (BaseConstant.DELIVERY_CITY_DISTRICT_FLAG.equalsIgnoreCase(params
                        .get("type").toString())) {
                        parentID = params.get("parent").toString();
                        descr = getMap(InitServlet.mapDiliveryCityZoneObj,
                            parentID, descr);
                    }
                }
            } else {
                if (InitServlet.mapHotelCommmonObj
                    .containsKey(queryID)) {
                    descr = coversionMap(InitServlet.mapHotelCommmonObj.get(queryID),
                        descr);
                }
            }
        } catch (Exception ex) {
        	log.error(ex);
        }
        return descr;
    }

    /**
     * 根据父亲 ID 获取对应的所有儿子列表 如:国家对应的省份、省份对应的城市、城市对应的城区、城市对应的商业区
     * 
     * @param mapParam1
     *            Map<String,Map<String,String>>
     * @param parentID
     *            String
     * @param mapParam2
     *            Map
     * @return Map
     */
    public Map getMap(Map<String, Map<String, String>> mapParam1, String parentID, Map mapParam2) {
        Object obj;
        Map<String, String> map = null;
        Set setObj = mapParam1.keySet();
        Iterator it = setObj.iterator();
        while (it.hasNext()) {
            obj = it.next();
            if (parentID.equals(obj.toString())) {
                map = mapParam1.get(obj);
                mapParam2 = coversionMap(map, mapParam2);
                break;
            }
        }
        return mapParam2;
    }

    /**
     * 把范型转化成普通格式,因为底层的实现没有支持范型，改动会存在很大风险 如: new OrderedMap(); 它仅仅 extends HashMap
     * 
     * @param mapParam1
     *            Map<? extends Object,String>
     * @param mapParam2
     *            Map
     * @return Map
     */
    public Map coversionMap(Map<? extends Object, String> mapParam1, Map mapParam2) {
        Object obj;
        Set setObj = mapParam1.keySet();
        Iterator it = setObj.iterator();
        while (it.hasNext()) {
            obj = it.next();
            mapParam2.put(obj, mapParam1.get(obj));
        }
        return mapParam2;
    }

    /**
     * 测试方法打印 Map 对象
     * 
     * @param map
     */
    public void printMap(Map map) {
        Object obj;
        Set setObj = map.keySet();
        Iterator it = setObj.iterator();
        while (it.hasNext()) {
            obj = it.next();
            log.info("Key = " + obj);
            log.info("Value = " + map.get(obj));
        }
    }

    /**
     * 本地外部 queryBaseData.jsp 测试用
     * 
     * @param queryID
     * @param params
     * @return
     */
    public Map loadFromDatabaseT(String queryID, Map params) {
        return loadFromDatabase(queryID, params);
    }

    /**
     * 本地调试 EJB 接口用
     * 
     * @param args
     */
    public static void main(String[] args) {
        Map mapObj = new HashMap();
        mapObj.put("type", "province");
        new BaseDataResourceDescriptor().printMap(new BaseDataResourceDescriptor()
            .loadFromDatabase("queryBaseData", mapObj));
    }

}
