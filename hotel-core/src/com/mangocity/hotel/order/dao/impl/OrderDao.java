package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.dao.IOrderDao;
import com.mangocity.hotel.order.persistence.Order;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 */
public class OrderDao extends DAOHibernateImpl implements IOrderDao {

    /**
     * 生成订单编号，并保存在订单当中
     */
    public void insertOrder(Order order) {
        long sn = super.getSequenceNextVal("seq_order_order");

        order.setOrderCD("H" + (sn + 1));
        super.save(order);

    }

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Order loadOrder(Serializable orderID) {
        return (Order) super.load(Order.class, orderID);
    }

    public void updateOrder(Order order) {
        super.saveOrUpdate(order);

    }

    public void saveOrUpdate(Order order) {// parasoft-suppress PB.IMC "与父类同名,暂不修改"
        if (null == order.getID()) {
            insertObject(order);
        } else
            updateOrder(order);
    }

    public List queryByNamedQuery(String queryID, Object[] params) {

        return super.getHibernateTemplate().findByNamedQuery(queryID, params);

    }


	/**
	 * 
	 */
	public List findOrderByMemberCd(String membercd, String hotelId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();	
		sql.append(" select ordercd");
		sql.append(" from or_order t ");
		sql.append(" where orderstate in (3, 4, 5, 6, 7, 8) ");
		sql.append(" and checkindate <= sysdate ");
		sql.append(" and membercd = ?");
		sql.append(" and hotelid = ?");
		sql.append(" order By orderid ASC ");
		List list = queryByNativeSql4Map(sql.toString(),new Object[]{membercd,hotelId},false);

		return list;
	}

	public List<Object[]> findOrderInfoByAuditedDate(String auditedDate) {
		// TODO Auto-generated method stub

		StringBuffer sql = new StringBuffer();
		
		sql.append(" select orderid,ordercd,orderstate,memberid,membercd,hotelname,hotelid,mobile,checkindate,checkoutdate ");
		sql.append(" from htl_ii.or_order o ");
		sql.append(" where o.type = 1 ");
		sql.append(" and trunc(auditeddate) = to_date(?,'yyyy-mm-dd') ");
		sql.append(" and ((o.orderstate = 6 and o.paytohotelok = 2) or ");
		sql.append(" (o.orderstate = 5 and o.paytohotelok = 2) or ");
		sql.append(" (o.orderstate = 7 and o.paytohotelok = 2) or ");
		sql.append(" (o.orderstate = 8 and o.paytohotelok = 0)) ");
		sql.append(" and checkindate >= to_date(?,'yyyy-mm-dd') - 30 ");
		sql.append(" and checkindate < to_date(?,'yyyy-mm-dd') + 1 ");
		
		List list = queryByNativeSql4Map(sql.toString(),new Object[]{auditedDate,auditedDate,auditedDate},false);

		return list;
	}

}
