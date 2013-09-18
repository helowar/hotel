package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.mangocity.hotel.base.dao.RoomControlDao;
import com.mangocity.hotel.base.manage.assistant.RoomControlBean;
import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.manage.assistant.UpdatePriceBean;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.assistant.RoomBedRecord;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * 房态控制Dao
 * @author xuyiwen
 */
public class RoomControlDaoImpl extends GenericDAOHibernateImpl implements RoomControlDao{

    public int update( RoomControlBean roomBean)
            throws HibernateException {
    	  List ls = new ArrayList();
	      StringBuilder builder = new StringBuilder();
	      builder.append("update HtlRoom room set room.roomState = ? where room.hotelId=?  ");
	      StringBuffer dateBuffer=new StringBuffer();
	      StringBuffer roomidBuffer=new StringBuffer("and room.roomTypeId in( ");
	      
	      ls = roomBean.getDateSegments();
	      if (!ls.isEmpty() && 1 == ls.size()) {
	          dateBuffer.append("and (room.ableSaleDate >=? and room.ableSaleDate<=?) ");
	      } else {
	          dateBuffer.append("and ((room.ableSaleDate >=? and room.ableSaleDate<=?) ");
	          for (int i = 1; i < ls.size(); i++) {
	              dateBuffer.append("or ( room.ableSaleDate >=? and room.ableSaleDate<=?) ");
	          }
	          dateBuffer.append(") ");
	      }
	      if (null != roomBean.getRoomType() && 0 < roomBean.getRoomType().length) {
	          for (int i = 0; i < roomBean.getRoomType().length; i++) {
	              if (i != roomBean.getRoomType().length - 1) {
	                  roomidBuffer.append(" "+roomBean.getRoomType()[i]+",");
	              } else {
	                  roomidBuffer.append(" "+roomBean.getRoomType()[i]);
	              }
	          }
	          roomidBuffer.append(" )");
	          builder.append(dateBuffer).append(roomidBuffer);
	      } else {
	          builder.append(dateBuffer);
	      }
    	Object[] paramValues=new Object[]{};
    	paramValues[0]=roomBean.getReason();
    	paramValues[1]=roomBean.getHotelID();
    	int k=2;
    	for (int j = 0; j < ls.size(); j++) {
          DateSegment dateSegment = (DateSegment) ls.get(j);
          paramValues[k]=dateSegment.getStart();
          paramValues[k+1]=dateSegment.getEnd();
    	}
    	
    	return super.updateByQL(builder.toString(), paramValues);
    }

    public int updateRoomStatu( RoomStateBean roomStateBean) {
    	StringBuilder builder=new StringBuilder();
        builder.append("update HtlRoom room set room.roomState = ? where room.hotelId=?  ")
        	  .append("and room.roomTypeId=? and ( room.ableSaleDate >=? ")
        	  .append("and room.ableSaleDate<=?) ")
        	  .append("and room.week in (");

        if (null != roomStateBean.getWeek() && 7 != roomStateBean.getWeek().length) {
            String[] wek = roomStateBean.getWeek();
            builder.append(BizRuleCheck.ArrayToString(wek))
            	  .append(" )");
        }
        Object[] paramValues=new Object[]{roomStateBean.getRoomStatus(),roomStateBean.getHotelID(),
        					roomStateBean.getRoomTypeID(),roomStateBean.getBegindate(), roomStateBean.getEnddate()
        					};
       return super.updateByQL(builder.toString(), paramValues);
    }

    public int updateRoomState( long roomId,  String roomState) {
    	String hql = "update HtlRoom room set room.roomState = ? where room.ID=?  ";
    	Object[] paramValues=new Object[]{roomState,Long.valueOf(roomId)};
    	return super.updateByQL(hql, paramValues);
    }

    public List<HtlOpenCloseRoom> selectCloseRoomDate( long hotelId) {
        List<HtlOpenCloseRoom> lstHtlOpenCloseRoom = super.queryByNamedQuery("selectCloseRoomDate",
            new Object[] { hotelId });
        return lstHtlOpenCloseRoom;
    }

    public int checkMainCommendDate(long pCommendId, long pHotelId, Date beginD, Date endD,
        Date bD, Date eD) {
    	int sign=0;
        List lstContract = super.queryByNamedQuery("checkMainCommendDate", new Object[] {
            pCommendId, pHotelId, beginD, endD, bD, eD });
        if (null != lstContract) {
            if (0 < lstContract.size()) sign=1;
        }
        return sign;
    }

    public int updateRoomStatuByHtlRoomID( HtlRoom htlroom) {
    	return updateRoomState(htlroom.getID(),htlroom.getRoomState()); //调用上面的方法、避免重写代码
    }
    
    public List<HtlRoom> selectRoomStatu( RoomStateBean roomStateBean) {
    	  String hql = " from HtlRoom room where room.hotelId=?  "
              + "and ( room.ableSaleDate >=? and room.ableSaleDate<=?) ";
          String weekStr = "and room.week in (";
          if (null != roomStateBean.getWeek() && 7 != roomStateBean.getWeek().length) {
              String[] wek = roomStateBean.getWeek();
              weekStr += BizRuleCheck.ArrayToString(wek);
              weekStr += " )";
              hql += weekStr;
          }
          Object[] paramValues=new Object[]{roomStateBean.getHotelID(),roomStateBean.getBegindate(),roomStateBean.getEnddate()};
          return super.query(hql, paramValues);
    }
    
	public List getRoomTypeHavaForewarn(long hotelId) {
		 String quotaHolderCC="CC";
		 StringBuilder builder=new StringBuilder();
		 builder.append("select distinct roomTy ")
		 		.append(" from HtlRoomtype roomTy, HtlQuotaNew quota ")
		 		.append(" where quota.forewarnFlag = 1 and quota.quotaHolder = ? and (roomTy.ishkroomtype=0 or roomTy.ishkroomtype is null) ")
		 		.append(" and  roomTy.ID = quota.roomtypeId ")
		 		.append(" and  roomTy.hotelID = quota.hotelId ")
		 		.append(" and  roomTy.hotelID = ? ");
		 
		 Object[] paramValues = new Object[] {quotaHolderCC,hotelId};
		 return super.query(builder.toString(), paramValues);
	}
	
	public int saveRoomStateBatch( RoomBedRecord roomBed,  int v_thisBedId,  int p_roomState) {
		 StringBuilder builder = new StringBuilder();
         builder.append("update HtlRoom r set r.roomState = REGEXP_REPLACE(r.roomState,'")
         	.append(v_thisBedId).append(":-?[0-9]','").append(v_thisBedId).append(":").append(p_roomState)
         	.append("') where r.roomTypeId = ? and ( r.ableSaleDate >=? and r.ableSaleDate<=?)");
         if(StringUtil.isValidStr(roomBed.getTheWeeks()))
 			builder.append(" and r.week in ("+roomBed.getTheWeeks()+")");
		
         Object[] paramValues=new Object[]{roomBed.getTheRoomTypeId(),roomBed.getTheStart(),roomBed.getTheEnd()};
         return super.updateByQL(builder.toString(), paramValues);
	}
    
	public int saveRoomStateBatch( RoomBedRecord roomBed, String roomStatusReg, String roomStatus){
		StringBuilder builder = new StringBuilder();
         builder.append("update HtlRoom r set r.roomState = REGEXP_REPLACE(r.roomState,'")
         		.append(roomStatusReg).append("','").append(roomStatus)
         		.append("') where r.roomTypeId = ? and ( r.ableSaleDate >=? and r.ableSaleDate<=?)");
         if(StringUtil.isValidStr(roomBed.getTheWeeks())) builder.append(" and r.week in ("+roomBed.getTheWeeks()+")");
		Object[] paramValues=new Object[]{roomBed.getTheRoomTypeId(),roomBed.getTheStart(),roomBed.getTheEnd()};
         return super.updateByQL(builder.toString(), paramValues);
	}
	
	public List getRoomTypeOrDatesByCCSet(int type, long hotelID,int startNo, int pageSize) {
		List result = null;
		StringBuilder builder = new StringBuilder();
		builder.append("from (select p.room_type_id rtId,p.able_sale_date saleDate from htl_price p ")
				.append("where p.able_sale_date >= trunc(sysdate) and p.hotel_id=? and p.close_flag='G' ")
				.append("union all ")
				.append("select  b.roomtypeid rtId,b.roomdate saleDate from HTL_ROOMSTATE_CC_BED b,HTL_ROOMSTATE_CC c ")
				.append("where b.roomstateccid = c.roomstateccid and c.hotelid = ? and c.reviewstate = '0' ")
				.append("and b.roomdate >=trunc(sysdate)) t");
		
		Object[] args = new Object[]{hotelID,hotelID}; 
		
		if (1 == type) {
			// 获取CC设置关房,满房房型ID
			builder.insert(0, "select distinct t.rtId ");
			//List rtIdList = super.doquerySQL(sqlSb.toString(), args ,false);
			List rtIdList = super.queryByNativeSQL(builder.toString(), 0, 0, args, null);
			if(rtIdList.isEmpty()) return null;
			Object[] objs = rtIdList.toArray();
			String rtIdStr = StringUtil.joinStr(objs);
			String hql = "from HtlRoomtype where ID in ("+rtIdStr+")";
			// 获取CC设置关房,满房的房型
			result=super.query(hql, new Object[]{});
		} else if (2 == type) {
			// 获取CC设置关房的日期
			builder.insert(0, "select distinct t.saleDate ").append(" order by t.saleDate");
			result=super.queryByNativeSQL(builder.toString(), startNo, pageSize, args, null);
		}else if(3 == type){
			builder.insert(0, "select count(distinct t.saleDate) ");
			result=super.queryByNativeSQL(builder.toString(), 0, 0, args, null);
		}
		return result;
	}
	
	public List<HtlRoomtype> getRoomTypeByCCSet(long hotelID) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("select distinct t.rtId ")
				.append("from (select p.room_type_id rtId,p.able_sale_date saleDate from htl_price p ")
				.append("where p.able_sale_date >= trunc(sysdate) and p.hotel_id=? and p.close_flag='G' ")
				.append("union all ")
				.append("select  b.roomtypeid rtId,b.roomdate saleDate from HTL_ROOMSTATE_CC_BED b,HTL_ROOMSTATE_CC c ")
				.append("where b.roomstateccid = c.roomstateccid and c.hotelid = ? and c.reviewstate = '0' ")
				.append("and b.roomdate >=trunc(sysdate)) t");
		
		Object[] args = new Object[]{hotelID,hotelID}; 
		// 获取CC设置关房,满房房型ID
		List<Long> rtIdList = super.queryByNativeSQL(builder.toString(), 0, 0, args, null);
		
		Object[] objs = rtIdList.toArray();
		
		String rtIdStr = StringUtil.joinStr(objs);
		
		StringBuilder hqlBuilder = new StringBuilder();
		
		hqlBuilder.append("from HtlRoomtype where ID in (")
		 		   .append(rtIdStr)
		 		   .append(")");
		// 获取CC设置关房,满房的房型
		return super.query(hqlBuilder.toString(), new Object[]{});
	}
	
	public List<Date> getCloseRoomDateByCCSet(long hotelID,int startNo, int pageSize) {
		StringBuilder builder = new StringBuilder();
		builder.append("select distinct t.saleDate ")
				.append("from (select p.room_type_id rtId,p.able_sale_date saleDate from htl_price p ")
				.append("where p.able_sale_date >= trunc(sysdate) and p.hotel_id=? and p.close_flag='G' ")
				.append("union all ")
				.append("select  b.roomtypeid rtId,b.roomdate saleDate from HTL_ROOMSTATE_CC_BED b,HTL_ROOMSTATE_CC c ")
				.append("where b.roomstateccid = c.roomstateccid and c.hotelid = ? and c.reviewstate = '0' ")
				.append("and b.roomdate >=trunc(sysdate)) t")
				.append(" order by t.saleDate");
		Object[] args = new Object[]{hotelID,hotelID}; 
		//获取CC设置关房的日期
		return super.queryByNativeSQL(builder.toString(), startNo, pageSize, args, null);
	}
	
	public List<Long> getCloseRoomDateNumByCCSet(long hotelID) {
		StringBuilder builder = new StringBuilder();
		builder.append("select count(distinct t.saleDate) ")
				.append("from (select p.room_type_id rtId,p.able_sale_date saleDate from htl_price p ")
				.append("where p.able_sale_date >= trunc(sysdate) and p.hotel_id=? and p.close_flag='G' ")
				.append("union all ")
				.append("select  b.roomtypeid rtId,b.roomdate saleDate from HTL_ROOMSTATE_CC_BED b,HTL_ROOMSTATE_CC c ")
				.append("where b.roomstateccid = c.roomstateccid and c.hotelid = ? and c.reviewstate = '0' ")
				.append("and b.roomdate >=trunc(sysdate)) t");
		Object[] args = new Object[]{hotelID,hotelID}; 
		//获取CC设置关房的日期的总数
		return super.queryByNativeSQL(builder.toString(), 0, 0, args, null);
	}
	
	public void saveOrUpdateHtlRoom(List<HtlRoom> htlRoom) {
		super.saveOrUpdateAll(htlRoom);
	}
	
	public void  saveOrUpdateRoomStatuPro(HtlRoomStatusProcess roomStaPro){
		 super.saveOrUpdate(roomStaPro);
	}
	
	public void updateTempQuotaByPro(String sql,Long[] quotaId){
		super.execProcedure(sql,quotaId);
	}

	public List<HtlRoomcontrolHotelSchedule> getHotelScheduleByHtlIdSchDate(long hotelId, Date schDate) {
		Date formatedDate = DateUtil.getDate(schDate);
		String hql = " from HtlRoomcontrolHotelSchedule where hotelid = ? and scheduledate = ? ";
		
		return super.query(hql, new Object[]{hotelId, formatedDate});
	}
	
	public void updateHtlRoomcontrolHotelSchedule(HtlRoomcontrolHotelSchedule htlRoomcontrolHotelSchedule) {
		super.update(htlRoomcontrolHotelSchedule);
	}
	
	public void batchUpdateOrSaveOpenCloseRooms(List<HtlOpenCloseRoom> htlOpenCloseRoomList) {
		super.saveOrUpdateAll(htlOpenCloseRoomList);
	}
	
	public HtlOpenCloseRoom findHtlOpenCloseRoomById(long id) {
		return super.get(HtlOpenCloseRoom.class, Long.valueOf(id));
	}
	
	public void updateHotelStatus(String hotelStatus, long hotelId){
		String hql = " update HtlHotel set hotelStatus = ? where ID = ? ";
		super.updateByQL(hql, new Object[] { hotelStatus, Long.valueOf(hotelId) });
	}
	
	public List<HtlOpenCloseRoom> qryCloseRoomRecordsByHtlId(long hotelId){
		String hql = " from HtlOpenCloseRoom where hotelId = ? and opCloseSign = 'G' "
			+ " order by closeRoomTime desc, roomTypeId, beginDate ";
		
		return super.query(hql, new Object[]{Long.valueOf(hotelId)});
	}
	
	@SuppressWarnings("unchecked")
	public int updateOpenOrCloseRoom(UpdatePriceBean updatePriceBean, String openOrCloseFlag) {
		StringBuilder hql = new StringBuilder();
		hql.append(" update HtlPrice set closeFlag = ?, allCloseReason = ? ");
		hql.append(" where childRoomTypeId = ? ");
		
		Integer[] week = updatePriceBean.getWeeks();
		List paramList = new ArrayList();
		paramList.add(openOrCloseFlag);
		paramList.add(updatePriceBean.getCauseSign());
		paramList.add(updatePriceBean.getChildRoomID());
		if("G".equals(openOrCloseFlag)){
			hql.append(" and roomTypeId = ? ");
			paramList.add(updatePriceBean.getRoomTypeID());
		}
		
		if (week != null && week.length != 0) {
			hql.append(" and week in (");
	        for (int i = 0; i < week.length; i++) {
	        	hql.append("?, ");
	        	paramList.add(week[i]);
	        }
	        hql.setLength(hql.length() - 2);
	        hql.append(") ");
		}
		
		hql.append(" and ableSaleDate between ? and ? ");
		paramList.add(updatePriceBean.getBeginDate());
		paramList.add(updatePriceBean.getEndDate());
		
        return super.updateByQL(hql.toString(), paramList.toArray());
	}
}
