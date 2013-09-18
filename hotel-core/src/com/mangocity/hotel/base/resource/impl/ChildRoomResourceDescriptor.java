package com.mangocity.hotel.base.resource.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mangocity.util.StringUtil;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.log.MyLog;

/**
 */
public class ChildRoomResourceDescriptor// parasoft-suppress SERIAL.NSFSC "JAR 包问题"
        extends DbResourceDescriptor implements Serializable {
	private static final MyLog log = MyLog.getLogger(ChildRoomResourceDescriptor.class);
    private static final String CHILD_ROOM_RESOURCE = "childRoomType";

    private static final Object RESOURCE_KEY = "NAME";

    /**
     * 从数据库加载资源
     * 
     * @param queryID
     * @param params
     * @return
     */
    @Override
    protected Map loadFromDatabase(String queryID, Map params) {

        if (!StringUtil.isValidStr(queryID)) {
            log.error("查询ID不能为空");
            return Collections.EMPTY_MAP;
        }

        Map descr = new OrderedMap();

        List results = queryDao.queryForList(queryID, params);

        for (int i = 0; i < results.size(); i++) {
            Map data = (Map) results.get(i);
            descr.put(data.get("NAME").toString(), data.get("DESCR"));
        }
        return descr;
    }

}
