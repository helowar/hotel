/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.mangocity.hotel.base.resource;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mangocity.util.StringUtil;
import com.mangocity.util.collections.FormatMap;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 *  MyOA
 * @version 0.1
 * @author zhengxin
 *  at 2006-7-10 ����02:14:23
 * : ��Դ������ 1.����ݿ��м�����Դ���� 2.��XML�м��� 3.�����ѯ���
 * 
 * 
 *           ����Ŀǰ��û�п���thread-safe�����⡣
 *  by zhengxin at 2006-7-10 ����02:14:23
 */
public class ResourceDescriptor implements Serializable {
	private static final MyLog log = MyLog.getLogger(ResourceDescriptor.class);

    public static String RESOURCE_DATABASE = "database";

    private DAOIbatisImpl queryDao;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    private String queryID;

    /**
     * ��Ҫ������ݿ⣬���ǲ�ѯ�����ֵ�ǲ�ѯ������
     */
    private Map cacheData;

    /**
     * ��Դ�����ͣ���ΪXML��Database}����Դ����,Ŀǰ��֧��Properties4������Դ
     */
    private String type;

    /**
     * �Ƿ����ݽ��л���
     */
    private boolean cache;

    /**
     * ��Դ��Ψһ��ʶ���
     */
    private String name;

    private Map description = new OrderedMap();

    public ResourceDescriptor(String name) {
        this.name = name;
    }

    public String getDescr(String statusName) {
        return (String) description.get(statusName);
    }

    public Map getDescription() {

        if (RESOURCE_DATABASE.equals(type) && (null == description || description.isEmpty())) {
            description = this.loadFromDatabase(queryID);
        }

        return description;
    }

    public Map getDescription(Map params) {
        Map results = null;
        if (cache) {
            results = (Map) cacheData.get(params.toString());
        }

        if (null == params)
            params = new FormatMap();

        /** 是否要从数据库中查询 */
        if (RESOURCE_DATABASE.equals(type) && (null == results || results.isEmpty())) {
            return loadFromDatabase(queryID, params);
        } else
            return getDescription();

    }

    private Map loadFromDatabase(String queryID) {
        return loadFromDatabase(queryID, Collections.EMPTY_MAP);
    }

    /**
     * ����ݿ��м�����Դ
     * 
     * @param queryID
     * @param params
     * @return
     */
    private Map loadFromDatabase(String queryID, Map params) {

        if (!StringUtil.isValidStr(queryID)) {
            log.error("查询ID不能为空");
            return Collections.EMPTY_MAP;
        }

        Map descr = new OrderedMap();
        try {
            List results = queryDao.queryForList(queryID, params);

            for (int m = 0; m < results.size(); m++) {
                Map data = (Map) results.get(m);

                descr.put(data.get("NAME").toString(), data.get("DESCR"));
            }

            if (cache) {
                if (params.isEmpty()) {
                    this.description = descr;
                } else
                    cacheData.put(params.toString(), descr);
            }

        } catch (Exception e) {
           log.error(e);
        }

        return descr;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
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
