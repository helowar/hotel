package com.mangocity.hotel.order.dao.impl;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mangocity.hotel.order.dao.IRoomStatusControlDao;
import com.mangocity.hotel.order.persistence.HtlRoomstateCcBean;
import com.mangocity.hotel.order.persistence.HtlRoomstateCcBed;
import com.mangocity.util.dao.DAOHibernateImpl;

public class RoomStatusControlDaoImpl extends DAOHibernateImpl implements IRoomStatusControlDao {

	 public int updateRoomStatuCC(final HtlRoomstateCcBean htlRoomstateCcBean,
		        final HtlRoomstateCcBed htlRoomstateCcBed) {
		        HibernateCallback cb = new HibernateCallback() {
		            public Object doInHibernate(Session session) throws HibernateException, SQLException {

		                int k = 0;

		                String Hql = "update HtlRoom room set room.roomState = ? where room.hotelId=?  "
		                    + "and room.roomTypeId=? and ( room.ableSaleDate >=? "
		                    + "and room.ableSaleDate<=?) ";

		                Query query = session.createQuery(Hql);
		                query.setString(k++, htlRoomstateCcBed.getRoomstate());
		                query.setLong(k++, htlRoomstateCcBean.getHotelid());
		                query.setLong(k++, htlRoomstateCcBed.getRoomtypeid());
		                query.setDate(k++, htlRoomstateCcBean.getBegindate());
		                query.setDate(k++, htlRoomstateCcBean.getEnddate());
		                query.executeUpdate();
		                return 0;
		            }
		        };
		        Integer ret = (Integer) getHibernateTemplate().execute(cb);
		        if (null == ret) {
		            return -1;
		        } else {
		            return ret.intValue();
		        }
		    }
}
