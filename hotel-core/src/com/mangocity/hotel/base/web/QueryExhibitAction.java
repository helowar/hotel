package com.mangocity.hotel.base.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlExhibit;
import com.mangocity.util.dao.DAOIbatisImpl;

/**
 * 用于房态界面查询某日期内的会展信息调用
 * 
 * @author chenjuesu 2009-05-08
 */
public class QueryExhibitAction {

    private DAOIbatisImpl queryDao;

    /**
     * 查询指定城市某日期内的会展信息
     * 
     * @param city
     *            城市
     * @param exhibitStartDate
     *            会展开始日期
     * @param exhibitEndDate
     *            会展结束日期
     * @return
     */
    public List<HtlExhibit> queryExhibit(String city, String exhibitStartDate, 
        String exhibitEndDate) {
        // 组装查询参数
        Map params = new HashMap();
        params.put("city", city);
        params.put("exhibitStartDate", exhibitStartDate);
        params.put("exhibitEndDate", exhibitEndDate);
        List exhibits = queryDao.queryForList("queryExhibit", params);
        return exhibits;
    }

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }
}
