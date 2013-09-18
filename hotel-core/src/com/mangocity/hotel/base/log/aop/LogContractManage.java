package com.mangocity.hotel.base.log.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlAlerttypeInfoLog;
import com.mangocity.util.OperationeFlagUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 合同管理提示信息日志实现类
 * 
 * @author zuoshengwei
 * 
 */
public class LogContractManage extends DAOHibernateImpl implements MethodBeforeAdvice {
	private static final MyLog log = MyLog.getLogger(LogContractManage.class);
    private HtlAlerttypeInfoLog alerttypeInfoLog = new HtlAlerttypeInfoLog();

    private JdbcTemplate jdbcTemplate;

    public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {

        HtlAlerttypeInfo alerttypeInfo = (HtlAlerttypeInfo) arg1[0];
        if (null != alerttypeInfo) {

            if (arg0.getName().equals("createAlerttypeInfo")) {
                Long nextSequ = sequNextVa();
                if (null != nextSequ) {
                    alerttypeInfoLog.setAlerttypeId(nextSequ);
                }
                alerttypeInfoLog.setOperateFlag(OperationeFlagUtil.ADD);
            } else if (arg0.getName().equals("modifyAlerttypeInfo")) {
                alerttypeInfoLog.setAlerttypeId(alerttypeInfo.getId());
                alerttypeInfoLog.setOperateFlag(OperationeFlagUtil.UPDATE);
            } else if (arg0.getName().equals("deleteAlerttypeInfoById")) {
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

            super.save(alerttypeInfoLog);

        } else {

            log.error("提示信息实体类为空！");

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

    public HtlAlerttypeInfoLog getAlerttypeInfoLog() {
        return alerttypeInfoLog;
    }

    public void setAlerttypeInfoLog(HtlAlerttypeInfoLog alerttypeInfoLog) {
        this.alerttypeInfoLog = alerttypeInfoLog;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
