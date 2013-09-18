package com.mangocity.hotel.base.manage.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.impl.ChangePriceDaoImpl;
import com.mangocity.hotel.base.dao.impl.QueryAnyDaoImpl;
import com.mangocity.hotel.base.manage.IChangePriceManage;
import com.mangocity.hotel.base.persistence.HtlChangePrice;
import com.mangocity.hotel.base.persistence.HtlChangePriceLog;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.BaseConstant;

/**
 */
public class ChangePriceManageImpl extends DAOHibernateImpl implements IChangePriceManage {

    private ChangePriceDaoImpl changePriceDao;

    private QueryAnyDaoImpl queryAnyDao;

    public Long createChangePrice(HtlChangePrice changePrice) {
        super.saveOrUpdate(changePrice);
        return changePrice.getID();
    }

    public boolean checkChangePriceExist(long hotelId) {
        Date currDate = DateUtil.getDate(DateUtil.getSystemDate());
        List lstChangePrice = changePriceDao.getTodayChangePrice(hotelId, currDate);
        if (0 < lstChangePrice.size()) {
            return true;
        }
        return false;
    }

    public ChangePriceDaoImpl getChangePriceDao() {
        return changePriceDao;
    }

    public void setChangePriceDao(ChangePriceDaoImpl changePriceDao) {
        this.changePriceDao = changePriceDao;
    }

    public Long createOrUpdateChangePriceWithLog(HtlChangePrice changePrice,
        HtlChangePriceLog changePriceLog) {
        changePriceLog.setOperateDate(DateUtil.getSystemDate());
        super.saveOrUpdate(changePrice);
        super.saveOrUpdate(changePriceLog);
        return changePrice.getID();
    }

    /**
     * 更新为已调整
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateAdjusted(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog) {
        changePrice.setStatus(BaseConstant.CP_ADJUSTED);
        changePriceLog.setOperateState(BaseConstant.CP_ADJUSTED);
        changePriceLog.setOperateDate(DateUtil.getSystemDate());
        super.saveOrUpdate(changePrice);
        super.saveOrUpdate(changePriceLog);
        return changePrice.getID();
    }

    /**
     * 更新为QA已审核
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateQAcheck(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog) {
        changePrice.setStatus(BaseConstant.CP_QACHECK);
        changePriceLog.setOperateState(BaseConstant.CP_QACHECK);
        changePriceLog.setOperateDate(DateUtil.getSystemDate());
        super.saveOrUpdate(changePrice);
        super.saveOrUpdate(changePriceLog);
        return changePrice.getID();

    }

    /**
     * 更新为再次跟进
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateAgainChange(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog) {
        changePrice.setStatus(BaseConstant.CP_AGAINCHANGE);
        changePriceLog.setOperateState(BaseConstant.CP_AGAINCHANGE);
        changePriceLog.setOperateDate(DateUtil.getSystemDate());
        super.saveOrUpdate(changePrice);
        super.saveOrUpdate(changePriceLog);
        return changePrice.getID();
    }

    /**
     * 更新为再次核查
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateAgainCheck(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog) {
        changePrice.setStatus(BaseConstant.CP_AGAINCHECK);
        changePriceLog.setOperateState(BaseConstant.CP_AGAINCHECK);
        changePriceLog.setOperateDate(DateUtil.getSystemDate());
        super.saveOrUpdate(changePrice);
        super.saveOrUpdate(changePriceLog);
        return changePrice.getID();
    }

    /**
     * 更新为变价中
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateChangeing(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog) {
        changePrice.setStatus(BaseConstant.CP_CHANGING);
        changePriceLog.setOperateState(BaseConstant.CP_CHANGING);
        changePriceLog.setOperateDate(DateUtil.getSystemDate());
        super.saveOrUpdate(changePrice);
        super.saveOrUpdate(changePriceLog);
        return changePrice.getID();
    }

    /**
     * 更新为已核查
     * 
     * @param changePrice
     * @param changePriceLog
     * @return
     */
    public Long updateCheck(HtlChangePrice changePrice, HtlChangePriceLog changePriceLog) {
        changePrice.setStatus(BaseConstant.CP_CHECKED);
        changePriceLog.setOperateState(BaseConstant.CP_CHECKED);
        changePriceLog.setOperateDate(DateUtil.getSystemDate());
        super.saveOrUpdate(changePrice);
        super.saveOrUpdate(changePriceLog);
        return changePrice.getID();
    }

    public HtlChangePrice getChangePriceById(Long id) {
        return (HtlChangePrice) super.find(HtlChangePrice.class, id);
    }

    public List queryPriceHistory(Map map) {
        return queryAnyDao.queryForList("queryPrice", map);
    }

    /**
     * 得到变价工单历史记录
     * 
     * @param taskCode
     *            工单CD
     * @return
     */
    public List getChangePriceLog(String taskCode) {
        return changePriceDao.getChangePriceLog(taskCode);
    }

    public QueryAnyDaoImpl getQueryAnyDao() {
        return queryAnyDao;
    }

    public void setQueryAnyDao(QueryAnyDaoImpl queryAnyDao) {
        this.queryAnyDao = queryAnyDao;
    }

}
