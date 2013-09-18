package hk.com.cts.ctcp.hotel.dao.impl;

import hk.com.cts.ctcp.hotel.dao.IExDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class ExDaoImpl extends DAOHibernateImpl implements IExDao {
	private static final MyLog log = MyLog.getLogger(ExDaoImpl.class);

    private static final long serialVersionUID = -7270736124496391774L;

    /**
     * 根据sql语句更新记录 add by shizhongwen 时间:Mar 17, 2009 2:41:34 PM
     * 
     * @param hsql
     * @param values
     */
    public void update(final String hsql, final Object[] values) {
        super.remove(hsql, values);
    }

    public String updateHKHotelPrice(Long hotelId, float baseAmt, float listAmt, String start_date,
        Long childRoomTypeId, Long roomTypeId) {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call updateHKhotelPrice(?,?,?,?,?,?)}";

            cstmt = super.getCurrentSession().connection().prepareCall(procedureName);
            cstmt.setLong(1, hotelId);
            cstmt.setFloat(2, baseAmt);
            cstmt.setFloat(3, listAmt);
            cstmt.setString(4, start_date);
            cstmt.setLong(5, childRoomTypeId);
            cstmt.setLong(6, roomTypeId);

            cstmt.execute();
            return "0";
        } catch (Exception e) {
            log.error("调用更新HK价格的存储过程报错:" + e);
            return "-1";
        } finally {
            if (null != cstmt) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                	log.error(e);
                }
            }
        }
    }

    /**
     * 更新HK酒店房间数 add by shizhongwen 时间:Mar 23, 2009 5:48:04 PM
     * 
     * @param hotelId
     * @param qty
     * @param roomTypeId
     * @param datetime
     * @return
     */
    public String updateHKhotelRoom(Long hotelId, int qty, Long roomTypeId, String datetime) {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call updateHKhotelRoom(?,?,?,?)}";

            cstmt = super.getCurrentSession().connection().prepareCall(procedureName);
            cstmt.setLong(1, hotelId);
            cstmt.setInt(2, qty);
            cstmt.setLong(3, roomTypeId);
            cstmt.setString(4, datetime);
            cstmt.execute();
            return "0";
        } catch (Exception e) {
            log.error("调用更新HK酒店房间数的存储过程报错:" + e);
            return "-1";
        } finally {
            if (null != cstmt) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }
    }

    /**
     * 用sql语句查询
     * 
     * @param sql
     * @return
     * @author chenjiajie v2.4.2 2008-12-26
     */
    public List doSqlQuery(String sql) {
        List resultLst = new ArrayList();
        Session session = super.getSession();
        resultLst = session.createSQLQuery(sql).list();
        return resultLst;
    }
    
    
    /**
     * 
     * 获取中旅订单信息
     * 
     * @return
     */
    public List getHKOrderInfos() {
        String sqlHtlHk = "select txn_no " + " from htl_hk.cts_dayend_txn "
				+ " where trunc(txn_time) = trunc(sysdate)-1 "
				+ " group by txn_no ";
		return doSqlQuery(sqlHtlHk);
    }
    
    /**
     * 
     * 获取芒果中旅单信息
     * 
     * @return
     */
    public List getMangoHKOrderInfos() {
		String sqlChannelNo = "select orderChannel,orderid "
				+ " from or_channelNo "
				+ " where trunc(commitTime) = trunc(sysdate)-1 "
				+ " and status = 2";
		return doSqlQuery(sqlChannelNo);
    }
    
    /**
     * 
     * 获取芒果订单编号
     * 
     * @return
     */
    public List getMangoOrderCd(String orderId) {
		String sqlOrder = "select orderCd from or_order o where o.orderid="
				+ orderId;
		return doSqlQuery(sqlOrder);
	}
    
    /**
     * 
     * 获取度假中旅单信息
     * 
     * @return
     */
    public List getPkgHKOrderInfos() {
		String sqlPkg = "select transactionNo,ppOrderId "
				+ " from hotel.pkg_hkhoteltrace "
				+ " where trunc(modifiedtime) = trunc(sysdate)-1 "
				+ " and nowstate = 'trading'";
		return doSqlQuery(sqlPkg);
    }
    
    /**
     * 
     * 获取度假订单编号
     * 
     * @return
     */
    public List getPkgOrderCd(String orderId) {
		String sqlOrder = "select t.ordercd from hotel.pkg_packageproductorder o,hotel.t_order t "
				+ " where o.id = " + orderId + " and t.id = o.orderid ";
		return doSqlQuery(sqlOrder);
	}
    
    /**
     * 
     * 获取中旅房型信息
     * 
     * @return
     */
    public List getHKRoomInfos() {
		String sqlPkg = "select hotel_id,room_type_id,room_name,room_eng_name,create_time "
				+ " from htl_roomtype_cts "
				+ " where active='0' and comm_level = 0";
		return doSqlQuery(sqlPkg);
    }
    
    /**
     * 
     * 获取芒果酒店名称
     * 
     * @return
     */
    public List getMangoHotelName(Integer hotelId) {
		String sqlHtlHk = "select chn_name from htl_hotel where hotel_id="
				+ hotelId + " and rownum=1";
		return doSqlQuery(sqlHtlHk);
    }
    
    /**
     * 
     * 获取中旅价格信息
     * 
     * @return
     */
    public List getHKPriceInfos() {
		String sqlPkg = "select hotel_id,room_type_id,room_name,create_time "
				+ " from htl_roomtype_cts "
				+ " where active='0' and comm_level = 1 ";
		return doSqlQuery(sqlPkg);
    }
    
    /**
     * 
     * 获取芒果房型名称
     * 
     * @return
     */
    public List getMangoRoomName(Integer roomTypeId) {
		String sqlHtlHk = "select room_name from htl_roomtype where room_type_id="
				+ roomTypeId + " and rownum=1";
		return doSqlQuery(sqlHtlHk);
    }
    
    /**
     * sql插入
     */
    @SuppressWarnings("deprecation")
    public void insertlog(String sql) {
        Session session = super.getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            tx = session.beginTransaction();
            conn = session.connection();
            stmt = conn.createStatement();
            stmt.execute(sql);

            tx.commit(); // 使用 Hibernate事务处理边界
        } catch (Exception he) {
            tx.rollback();
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException sqlex) {
                    System.err.println(this.getClass().getName() + ".mymethod - 不能关闭数据库连接: "
                        + sqlex.toString());
                }
            }
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException sqlex) {
                    System.err.println(this.getClass().getName() + ".mymethod - 不能关闭数据库连接: "
                        + sqlex.toString());
                }
            }
            session.close();
        }
    }
}
