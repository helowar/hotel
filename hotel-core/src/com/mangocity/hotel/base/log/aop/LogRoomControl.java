package com.mangocity.hotel.base.log.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * @author zhuangzhineng 房控处理时记录日志
 * 
 */
public class LogRoomControl extends DAOHibernateImpl implements MethodBeforeAdvice {
	private static final MyLog log = MyLog.getLogger(LogRoomControl.class);
    public void before(Method method, Object[] args, Object target) {
        RoomStateBean roomStateBean = new RoomStateBean();
        roomStateBean = (RoomStateBean) args[0];
        if (null != roomStateBean) {
            HtlRoomStatusProcess rsp = new HtlRoomStatusProcess();
            rsp.setHotelId(roomStateBean.getHotelID());
            rsp.setProcessBy(roomStateBean.getUserName());
            rsp.setProcessById(roomStateBean.getUserId());
            rsp.setProcessDate(DateUtil.getSystemDate());
            rsp.setProcessRemark(roomStateBean.getProcessRemark());
            rsp.setProcessDatetime(DateUtil.getSystemDate());
            rsp.setIsRoomStatusReport(Long.valueOf(roomStateBean.getIsRoomStatusReport())
                .longValue());
            super.saveOrUpdate(rsp);
        } else {
            log.info("roomStateBean is null!");
        }
    }
}