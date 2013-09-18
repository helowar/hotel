package com.mangocity.hagtb2b.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mangocity.hagtb2b.dao.IB2bOrderDao;
import com.mangocity.hagtb2b.persistence.HtlB2bTempComminfo;
import com.mangocity.hotel.order.persistence.B2bModifyOrderInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;

public class B2bOrderDao extends DAOHibernateImpl implements IB2bOrderDao,Serializable   {

	public void updateOrder(B2bModifyOrderInfo b2border) {
		super.saveOrUpdate(b2border);
	}
	
/*	*//**
	 * 
	 * 增加或更新佣金设置公式
	 * add by yong.zeng 2010-3-16
	 * @param commSet
	 *//*

	public void saveOrUpdateCommSet(CommisionSet commSet) {
		super.saveOrUpdate(commSet);
	}
	*//**
	 * 调整佣金
	 * add by yong.zeng 2010-3-16
	 * @param commAdjust
	 *//*
	public void saveOrUpdateCommAdjust(CommisionAdjust commAdjust) {
		super.saveOrUpdate(commAdjust);
	}
*/	
	/**
	 * 批量更新佣金设置公式
	 * add by yong.zeng
	 * 2010.3.16
	 * @param commLst
	 */
	public void batchUpdate(List commLst){
		super.saveOrUpdateAll(commLst);
	}
	/**
	 * save or update HtlB2bTempComminfo object
	 * add by zhijie.gu
	 */
	public void saveOrUpderComTemp(HtlB2bTempComminfo htlB2bTempComminfo){
		super.saveOrUpdate(htlB2bTempComminfo);
	}
	public void insertObject(Object obj) {
	     super.save(obj);
	}
	
	public OrOrder loadOrder(Serializable orderID) {
	      return (OrOrder) super.load(OrOrder.class, orderID);
	}
	public void doSqlUpdateBatch(String hql, Object[] obj){
		 super.doUpdateBatch(hql, obj);
	}
	
	public Object queryObj(String hsql){
		return super.find(hsql);
	}
	
	 /**
	   * 根据模板id删除对应数据
	   * @param tempId
	   * @return
	   */
	public void deleteList(Long tempId,String loginChnName,String loginId){
		String sql ="update htl_b2b_temp_comminfo hc set hc.active=0,hc.modify_id='"+loginId+"',hc.modify_name='"
						+loginId+"',hc.modify_time=to_date('"+DateUtil.formatDateToYMDHMS1(new Date())+"','yyyy-MM-dd hh24:mi:ss')"+" where hc.id="+tempId;
		String sql1 ="update htl_b2b_temp_comminfo_item hi set hi.active=0,hi.modify_id='"+loginId+"',hi.modify_name='"
						+loginId+"',hi.modify_time=to_date('"+DateUtil.formatDateToYMDHMS1(new Date())+"','yyyy-MM-dd hh24:mi:ss')"+" where hi.temp_id="+tempId;
	    super.doSqlUpdate(sql1);
		super.doSqlUpdate(sql);
	}
	
	/**
	   * 根据价格类型id获取酒店名称和价格类型名称
	   * @param priceTypeStr
	   * @return
	   */
	  public List fillHotelNameAndPriceTypeName(String priceTypeIdStr){
		  String sql ="select h.hotel_id,h.chn_name,pt.price_type_id,r.room_name || "+"'(' || "+"pt.price_type "+" || ')'"+" from "+
			" htl_hotel h, htl_roomtype r, htl_price_type pt where h.hotel_id = r.hotel_id "+
			" and r.room_type_id = pt.room_type_id  and pt.price_type_id in("+priceTypeIdStr+")";
		  List lis = super.doquerySQL(sql, false);
		  return lis;
	  }

}
