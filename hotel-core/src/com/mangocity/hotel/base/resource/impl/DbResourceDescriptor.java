package com.mangocity.hotel.base.resource.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.util.StringUtil;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.log.MyLog;

/**
 */
public class DbResourceDescriptor// parasoft-suppress SERIAL.NSFSC "JAR 包问题" 
        extends GenericDescriptor implements Serializable {
	private static final MyLog log = MyLog.getLogger(DbResourceDescriptor.class);
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
     * 从数据库加载资源
     * 
     * @param queryID
     * @param params
     * @return
     */
    protected Map loadFromDatabase(String queryID, Map params) {

        if (!StringUtil.isValidStr(queryID)) {
            log.error("查询ID不能为空");
            return Collections.EMPTY_MAP;
        }

        Map descr = new OrderedMap();

        List results = queryDao.queryForList(queryID, params);

        for (int m = 0; m < results.size(); m++) {
            Map data = (Map) results.get(m);

            descr.put(data.get("NAME").toString(), data.get("DESCR"));
        }

        return descr;
    }
}
