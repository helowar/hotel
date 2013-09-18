package com.mangocity.util.dao;

import java.util.Map;

/**
 * @author yuexiaofeng
 * 
 */
public interface IbatisDao {

    /**
     * 
     * 延长合同
     * 
     * @param id
     * @param param
     * @return integer
     */
    public Object queryForObject(String id, Map params);

}
