package com.mangocity.hotel.base.service;

import com.mangocity.hotel.base.persistence.OrLockedRecords;

/**
 * 酒店，合同，房态，配额等加解锁操作接口
 * 
 * @author chenjiajie
 * 
 */
public interface ILockedRecordService {
    public boolean insertLockedRecord(OrLockedRecords lockedRecord);

    public OrLockedRecords loadLockedRecord(OrLockedRecords lockedRecord);

    public int updateLockedRecord(OrLockedRecords lockedRecord);

    public int deleteLockedRecord(OrLockedRecords lockedRecord);

    /**
     * 
     * @param recordCD
     *            记录标识
     * @param lockedType
     *            解锁类型 01：酒店锁 02：房态锁 03：合同锁 04：配额锁
     * 
     * @return
     */
    public int deleteLockedRecord(String recordCD, Integer lockedType);

    public int deleteLockedRecordTwo(String recordCD, Integer lockedType, String lockedName);

    public void replaceLockedRecords(OrLockedRecords oldRecord, OrLockedRecords newRecord);
}
