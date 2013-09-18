package com.mangocity.hotel.base.log.aop.aspect;

import java.lang.reflect.InvocationTargetException;

import org.aspectj.lang.JoinPoint;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlAlerttypeInfoLog;
import com.mangocity.util.OperationeFlagUtil;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.log.MyLog;

/**
 * AOP 提示信息 日志 类
 * 
 * add by shengwei.zuo 2010-3-29
 * 
 */
public class LogAlertInfoHandle {
	
	private static final MyLog log = MyLog.getLogger(LogAlertInfoHandle.class);

    private JdbcTemplate jdbcTemplate;
    
    private DAO  entityManager;

	/**
     * 拦截酒店提示信息的操作类，进行日志写入
     */
    public void logAlertInfo(JoinPoint  jp) throws IllegalAccessException, InvocationTargetException {
    	
    	Object[] objLog =  jp.getArgs();
    	
    	HtlAlerttypeInfo alerttypeInfo = (HtlAlerttypeInfo) objLog[0];
    	
    	String methodStr =jp.getSignature().getName();
    	
    	log.info("=====================酒店提示信息日志的写入=====Aop method : "+methodStr);
       
        if (null != alerttypeInfo) {
        	
        	 //提示信息日志类
            HtlAlerttypeInfoLog alerttypeInfoLog = new HtlAlerttypeInfoLog();

            if (methodStr.equals("createAlerttypeInfo")) {
                Long nextSequ = sequNextVa();
                if (null != nextSequ) {
                    alerttypeInfoLog.setAlerttypeId(nextSequ);
                }
                alerttypeInfoLog.setOperateFlag(OperationeFlagUtil.ADD);
            } else if (methodStr.equals("modifyAlerttypeInfo")) {
                alerttypeInfoLog.setAlerttypeId(alerttypeInfo.getId());
                alerttypeInfoLog.setOperateFlag(OperationeFlagUtil.UPDATE);
            } else if (methodStr.equals("deleteAlerttypeInfoById")) {
                alerttypeInfoLog.setAlerttypeId(alerttypeInfo.getId());
                alerttypeInfoLog.setOperateFlag(OperationeFlagUtil.DELETE);
            }
            alerttypeInfoLog.setAlerttypeInfo(alerttypeInfo.getAlerttypeInfo());
            alerttypeInfoLog.setBeginDate(alerttypeInfo.getBeginDate());
            alerttypeInfoLog.setContractId(alerttypeInfo.getContractId());
            alerttypeInfoLog.setCreateBy(alerttypeInfo.getCreateBy());
            alerttypeInfoLog.setCreateById(alerttypeInfo.getCreateById());
            alerttypeInfoLog.setCreateTime(alerttypeInfo.getCreateTime());
            alerttypeInfoLog.setEndDate(alerttypeInfo.getEndDate());
            alerttypeInfoLog.setHotelId(alerttypeInfo.getHotelId());
            alerttypeInfoLog.setLocalFlag(alerttypeInfo.getLocalFlag());
            alerttypeInfoLog.setModifyBy(alerttypeInfo.getModifyBy());
            alerttypeInfoLog.setModifyById(alerttypeInfo.getModifyById());
            alerttypeInfoLog.setModifyTime(alerttypeInfo.getModifyTime());
            alerttypeInfoLog.setPriceTypeId(alerttypeInfo.getPriceTypeId());
            alerttypeInfoLog.setPriceTypeName(alerttypeInfo.getPriceTypeName());
            alerttypeInfoLog.setWeek(alerttypeInfo.getWeek());

            entityManager.save(alerttypeInfoLog);

        } else {

        	log.info("提示信息实体类为空！");

        }

    }

    /**
     * 取出下一个sequnce;
     * 
     * @param sql
     * @return
     */
    public Long sequNextVa() {
        String sql = "select SEQ_HTL_ALERTTYPE_INFO.nextval from dual";
        Long countRow = jdbcTemplate.queryForLong(sql);
        countRow += 1;
        return countRow;
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	public DAO getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(DAO entityManager) {
		this.entityManager = entityManager;
	}
	
}
