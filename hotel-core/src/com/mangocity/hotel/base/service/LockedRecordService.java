package com.mangocity.hotel.base.service;

import java.io.Serializable;

import com.mangocity.hotel.base.dao.impl.OrLockedRecordDao;
import com.mangocity.hotel.base.persistence.OrLockedRecords;

/**
 * 
 * @author chenjiajie
 * 
 */
public class LockedRecordService implements ILockedRecordService, Serializable {

    private OrLockedRecordDao orLockedRecordDao;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    public int deleteLockedRecord(OrLockedRecords lockedRecord) {
        return orLockedRecordDao.deleteLockedRecord(lockedRecord);
    }

    public boolean insertLockedRecord(OrLockedRecords lockedRecord) {
        return orLockedRecordDao.insertLockedOrder(lockedRecord);
    }

    public OrLockedRecords loadLockedRecord(OrLockedRecords lockedRecord) {
        return orLockedRecordDao.loadLockedRecord(lockedRecord);
    }

    public void replaceLockedRecords(OrLockedRecords oldRecord, OrLockedRecords newRecord) {
        // TODO Auto-generated method stub

    }

    public int updateLockedRecord(OrLockedRecords lockedRecord) {
        // TODO Auto-generated method stub
        return 0;
    }

    public OrLockedRecordDao getOrLockedRecordDao() {
        return orLockedRecordDao;
    }

    public void setOrLockedRecordDao(OrLockedRecordDao orLockedRecordDao) {
        this.orLockedRecordDao = orLockedRecordDao;
    }

    public int deleteLockedRecord(String recordCD, Integer lockedType) {
        OrLockedRecords orLockedRecords = new OrLockedRecords();
        orLockedRecords.setRecordCD(recordCD);
        orLockedRecords.setLockType(lockedType);
        return deleteLockedRecord(orLockedRecords);
    }

    public int deleteLockedRecordTwo(String recordCD, Integer lockedType, String lockedName) {
        OrLockedRecords orLockedRecords = new OrLockedRecords();
        orLockedRecords.setRecordCD(recordCD);
        orLockedRecords.setLockType(lockedType);
        orLockedRecords.setLockerLoginName(lockedName);
        return orLockedRecordDao.deleteLockedRecordTwo(orLockedRecords);
    }

}
