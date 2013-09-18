package com.mangocity.hotel.order.service.impl;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;


import com.mangocity.hotel.order.service.IOrderMessyCodeFixService;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 订单乱码修复调用接口实现类
 * @author zhouna
 *
 */
public class OrderMessyCodeFixServiceImpl extends DAOHibernateImpl implements IOrderMessyCodeFixService{
	
	private static final MyLog log = MyLog.getLogger(OrderMessyCodeFixServiceImpl.class);
	/**
	 * 调用乱码修复的存储过程
	 * @param orderCD
	 * @return
	 */
	public String callFixOrderMessyCodeProcdure(String orderCD){
        CallableStatement cstmt = null;
        String message = null;
        try {
            String procedureName = "{call p_update_order_chinese(?,?)}";
            cstmt = super.getCurrentSession().connection().prepareCall(procedureName);
            cstmt.setString(1, orderCD);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.execute();
            message=cstmt.getString(2);
        } catch (Exception e) {
            log.error("callFixOrderMessyCodeProcdure error! the cause is:" + e);
        } finally{
            try {
                if(null!=cstmt){
                    cstmt.close();
                }
            } catch (SQLException e) {
                log.error("callFixOrderMessyCodeProcdure finally error! the cause is:" + e);
            }
        }
        return message;

}
}
