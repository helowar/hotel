package com.mangocity.hotel.base.dao.impl;

import com.mangocity.hotel.base.persistence.OrLockedRecords;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 * 对酒店，合同，房态，配额等加解锁操作
 * 
 * @author chenjiajie
 * 
 */
public class OrLockedRecordDao extends DAOIbatisImpl {
	private static final MyLog log = MyLog.getLogger(OrLockedRecordDao.class);
    public boolean insertLockedOrder(OrLockedRecords lockedRecord) {
        boolean isExistsOrder = false;
        try {
            super.save("insertLockedRecord", lockedRecord);
            isExistsOrder = true;
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
        }
        return isExistsOrder;
    }

    /**
     * 按ID和类型 查询记录是否被锁，
     * 
     * @param lockedOrders
     * @return
     */
    public OrLockedRecords loadLockedRecord(OrLockedRecords lockedRecord) {
        return (OrLockedRecords) super.queryForObject("getLockedRecord", lockedRecord);
    }

    /**
     * 按ID和类型 删除被锁记录
     * 
     * @param lockedOrders
     * @return
     */
    public int deleteLockedRecord(OrLockedRecords lockedRecord) {
        return super.delete("deleteLockedRecord", lockedRecord);
    }

    public int deleteLockedRecordTwo(OrLockedRecords lockedRecord) {
        return super.delete("deleteLockedRecordTwo", lockedRecord);
    }

}
