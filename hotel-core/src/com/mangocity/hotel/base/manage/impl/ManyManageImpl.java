package com.mangocity.hotel.base.manage.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.constant.ClaueType;
import com.mangocity.hotel.base.dao.IClauseDao;
import com.mangocity.hotel.base.manage.ManyManage;
import com.mangocity.hotel.base.persistence.AssureClauseTemplate;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplate;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateFive;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateFour;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateOne;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateThree;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateTwo;
import com.mangocity.hotel.base.persistence.HtlAssureTemplate;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplate;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateFive;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateFour;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateOne;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateThree;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateTwo;
import com.mangocity.hotel.base.persistence.HtlPrepayTemplate;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.PrepayClauseTemplate;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 该类主要是对预付和担保条款的操作
 */
//TODO 类名有点问题,看能不能和ClauseManageImpl合并
public class ManyManageImpl extends DAOHibernateImpl implements ManyManage {

	private static final MyLog log = MyLog.getLogger(ManyManageImpl.class);
	//注入dao
	private IClauseDao clauseDao;
	
	// 查询酒店价格类型,找到相关的dao
	public List<HtlRoomtype> queryPriceType(long hotelId) {
		String hql = "from HtlRoomtype where hotelID = ?";
		List<HtlRoomtype> lstroomtype = super.query(hql, hotelId);
		return lstroomtype == null ? Collections.EMPTY_LIST : lstroomtype;
	}

	// 查询酒店所有模板
	public List<HtlPreconcertItemTemplet> queryModel(long hotelId) {
		return clauseDao.queryModel(hotelId);
	}

	// 根据预定条款查询某一条预定条款

	public List<HtlPreconcertItemTemplet> quertHotelAjaxModel(long modelId, long hotelId) {
		List<HtlPreconcertItemTemplet> lstModel = clauseDao.quertHotelAjaxModel(modelId, hotelId);
		for (HtlPreconcertItemTemplet hi : lstModel) {
			List<HtlAssureTemplate> lstHtlAssureTemp = hi.getHtlAssureTemplateZ();// 担保条款
			// 重新组装担保条款
			refitAssureTemplate(lstHtlAssureTemp);
			List<HtlPrepayTemplate> lstPrepay = hi.getHtlPrepayTemplateZ();// 预付条款
			// 重新组装预付条款
			refitPrepayTemplate(lstPrepay);
		}
		return lstModel;
	}

	
	/**
	 * 重新组装担保条款
	 * @param lstHtlAssureTemp
	 */
	private void refitAssureTemplate(List<HtlAssureTemplate> lstHtlAssureTemp) {
		//5个临时变量，用于存放对应的模板条款
		List<AssureClauseTemplate> lisHtlAssureItemTemplateOne = new ArrayList<AssureClauseTemplate>();
		List<AssureClauseTemplate> lisHtlAssureItemTemplateTwo = new ArrayList<AssureClauseTemplate>();
		List<AssureClauseTemplate> lisHtlAssureItemTemplateThree = new ArrayList<AssureClauseTemplate>();
		List<AssureClauseTemplate> lisHtlAssureItemTemplateFour = new ArrayList<AssureClauseTemplate>();
		List<AssureClauseTemplate> lisHtlAssureItemTemplateFive = new ArrayList<AssureClauseTemplate>();
		for (int j = 0; j < lstHtlAssureTemp.size(); j++) {
			HtlAssureTemplate htlAssureTemplate = lstHtlAssureTemp.get(j);
			List<HtlAssureItemTemplate> lstHtlAssureItemTemplate = htlAssureTemplate.getLisHtlAssureItemTemplate();
			for (int k = 0; k < lstHtlAssureItemTemplate.size(); k++) {
				HtlAssureItemTemplate htlAssureItemTemplate = lstHtlAssureItemTemplate.get(k);
				// 可以用switch,也可以用if else
				// 防止htlAssureItemTemplate为空，转换为int型报错
				if (null == htlAssureItemTemplate.getType() || "".equals(htlAssureItemTemplate.getType())) {
					continue;
				}
				int type = Integer.parseInt(htlAssureItemTemplate.getType());
				switch (type) {
				case ClaueType.ASSURE_TYPE_ONE_INT: {
					HtlAssureItemTemplateOne htlAssureItemTemplateOne = new HtlAssureItemTemplateOne();
					htlAssureItemTemplateOne.setParams(htlAssureItemTemplate);
					lisHtlAssureItemTemplateOne.add(htlAssureItemTemplateOne);
					htlAssureTemplate.setLisHtlAssureItemTemplateOne(lisHtlAssureItemTemplateOne);
					break;
				}
				case ClaueType.ASSURE_TYPE_TWO_INT: {
					AssureClauseTemplate htlAssureItemTemplateTwo = new HtlAssureItemTemplateTwo();
					htlAssureItemTemplateTwo.setParams(htlAssureItemTemplate);
					lisHtlAssureItemTemplateTwo.add(htlAssureItemTemplateTwo);
					htlAssureTemplate.setLisHtlAssureItemTemplateTwo(lisHtlAssureItemTemplateTwo);
					break;
				}
				case ClaueType.ASSURE_TYPE_THREE_INT: {
					AssureClauseTemplate htlAssureItemTemplateThree = new HtlAssureItemTemplateThree();
					htlAssureItemTemplateThree.setParams(htlAssureItemTemplate);
					lisHtlAssureItemTemplateThree.add(htlAssureItemTemplateThree);
					htlAssureTemplate.setLisHtlAssureItemTemplateThree(lisHtlAssureItemTemplateThree);
					break;
				}
				case ClaueType.ASSURE_TYPE_FORV_INT: {
					AssureClauseTemplate htlAssureItemTemplateFour = new HtlAssureItemTemplateFour();
					htlAssureItemTemplateFour.setParams(htlAssureItemTemplate);
					lisHtlAssureItemTemplateFour.add(htlAssureItemTemplateFour);
					htlAssureTemplate.setLisHtlAssureItemTemplateFour(lisHtlAssureItemTemplateFour);
					break;
				}
				case ClaueType.ASSURE_TYPE_FIVE_INT: {
					AssureClauseTemplate htlAssureItemTemplateFive = new HtlAssureItemTemplateFive();
					htlAssureItemTemplateFive.setParams(htlAssureItemTemplate);
					// 增加之后的设定 add by shengwei.zuo hotel2.6 2009-06-04 begin;

					// 增加之后的设定 add by shengwei.zuo hotel2.6 2009-06-04 end;
					lisHtlAssureItemTemplateFive.add(htlAssureItemTemplateFive);
					htlAssureTemplate.setLisHtlAssureItemTemplateFive(lisHtlAssureItemTemplateFive);
				}
				}

			}
		}
	}

   /**
    * 重新组装预付条款
    * @param lstPrepay
    */
	private void refitPrepayTemplate(List<HtlPrepayTemplate> lstPrepay) {
		//5个临时变量，用于存放对应的模板条款
		List<PrepayClauseTemplate> lisHtlPrepayItemTemplateOne = new ArrayList<PrepayClauseTemplate>();
	    List<PrepayClauseTemplate> lisHtlPrepayItemTemplateTwo = new ArrayList<PrepayClauseTemplate>();
		List<PrepayClauseTemplate> lisHtlPrepayItemTemplateThree = new ArrayList<PrepayClauseTemplate>();
		List<PrepayClauseTemplate> lisHtlPrepayItemTemplateFour = new ArrayList<PrepayClauseTemplate>();
		List<PrepayClauseTemplate> lisHtlPrepayItemTemplateFive = new ArrayList<PrepayClauseTemplate>();
		for (int y = 0; y < lstPrepay.size(); y++) {
			HtlPrepayTemplate htlPrepayTemplate = (HtlPrepayTemplate) lstPrepay.get(y);
			List<HtlPrepayItemTemplate> lsthtlPrepayItemTemplate = htlPrepayTemplate.getLisHtlPrepayItemTemplate();
			for (int h = 0; h < lsthtlPrepayItemTemplate.size(); h++) {
				//用策略模式				
				HtlPrepayItemTemplate htlPrepayItemTemplate = (HtlPrepayItemTemplate) lsthtlPrepayItemTemplate.get(h);
				if (ClaueType.PREPAY_TYPE_ONE.equals(htlPrepayItemTemplate.getType())) {
					PrepayClauseTemplate htlPrepayItemTemplateOne = new HtlPrepayItemTemplateOne();
					htlPrepayItemTemplateOne.setParams(htlPrepayItemTemplate);
					lisHtlPrepayItemTemplateOne.add(htlPrepayItemTemplateOne);
					htlPrepayTemplate.setLisHtlPrepayItemTemplateOne(lisHtlPrepayItemTemplateOne);
				} else if (ClaueType.PREPAY_TYPE_TWO.equals(htlPrepayItemTemplate.getType())) {
					PrepayClauseTemplate htlPrepayItemTemplateTwo = new HtlPrepayItemTemplateTwo();
					htlPrepayItemTemplateTwo.setParams(htlPrepayItemTemplate);
					lisHtlPrepayItemTemplateTwo.add(htlPrepayItemTemplateTwo);
					htlPrepayTemplate.setLisHtlPrepayItemTemplateTwo(lisHtlPrepayItemTemplateTwo);
				} else if (ClaueType.PREPAY_TYPE_THREE.equals(htlPrepayItemTemplate.getType())) {
					PrepayClauseTemplate htlPrepayItemTemplateThree = new HtlPrepayItemTemplateThree();
					htlPrepayItemTemplateThree.setParams(htlPrepayItemTemplate);
					lisHtlPrepayItemTemplateThree.add(htlPrepayItemTemplateThree);
					htlPrepayTemplate.setLisHtlPrepayItemTemplateThree(lisHtlPrepayItemTemplateThree);
				} else if (ClaueType.PREPAY_TYPE_FORV.equals(htlPrepayItemTemplate.getType())) {
					PrepayClauseTemplate htlPrepayItemTemplateFour = new HtlPrepayItemTemplateFour();
					htlPrepayItemTemplateFour.setParams(htlPrepayItemTemplate);
					lisHtlPrepayItemTemplateFour.add(htlPrepayItemTemplateFour);
					htlPrepayTemplate.setLisHtlPrepayItemTemplateFour(lisHtlPrepayItemTemplateFour);
				} else if (ClaueType.PREPAY_TYPE_FIVE.equals(htlPrepayItemTemplate.getType())) {
					PrepayClauseTemplate htlPrepayItemTemplateFive = new HtlPrepayItemTemplateFive();
					htlPrepayItemTemplateFive.setParams(htlPrepayItemTemplate);
					lisHtlPrepayItemTemplateFive.add(htlPrepayItemTemplateFive);
					htlPrepayTemplate.setLisHtlPrepayItemTemplateFive(lisHtlPrepayItemTemplateFive);
				}
			}
		}
	}

	// 存储过程,在批次表中查询记录,然后在插入每天表中
	public boolean saveOrupdatePro(long hotelid, long id, long contractId, long priceTypeid, Date beginDate,
			Date engDate, String active) throws SQLException {
		CallableStatement cstmt = null;
		try {

			String procedureName = "{call sp_hotel_Terms_manyClause(?,?,?,?,?,?,?)} ";
			cstmt = super.getCurrentSession().connection().prepareCall(procedureName);
			java.sql.Date bDate = new java.sql.Date(beginDate.getTime());
			java.sql.Date eDate = new java.sql.Date(engDate.getTime());
			cstmt.setLong(1, hotelid);
			cstmt.setLong(2, id);
			cstmt.setLong(3, contractId);
			cstmt.setLong(4, priceTypeid);
			cstmt.setDate(5, bDate);
			cstmt.setDate(6, eDate);
			cstmt.setString(7, active);
			cstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			cstmt.close();
		}

	}

	// 插入批次表数据，相关的方法应该可以直接DAOHibernateImpl
	public boolean saveOrupdateAll(HtlPreconcertItemBatch htlPreconcertItemBatch) {
		try {
			super.save(htlPreconcertItemBatch);
			return true;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}

	}

	// 查询酒店合同,找到相关的dao
	public List<HtlContract> queryHtlContract(long ID) {
		String sql = "from HtlContract where ID =?";
		List<HtlContract> lstContract = super.query(sql, ID);
		return lstContract == null ? Collections.EMPTY_LIST : lstContract;
	}

	// 查询批次表,拿的操作记录，找到相关的dao
	public List<HtlPreconcertItemBatch> queryHtlBatch(long hotelId) {
		String hsql = "from  HtlPreconcertItemBatch where hotel_id=? " + "and doubletofalg=" + "2"
				+ " order by ID desc";
		List<HtlPreconcertItemBatch> lstHtlBatch = super.doquery(hsql, hotelId, 0, 5, false);
		return lstHtlBatch == null ? Collections.EMPTY_LIST : lstHtlBatch;

	}

	// 查询担保预付条款模板
	public List<HtlPreconcertItemTemplet> queryModelOnly(long modelId) {
		return clauseDao.queryModelOnly(modelId);

	}

	public void setClauseDao(IClauseDao clauseDao) {
		this.clauseDao = clauseDao;
	}

}
