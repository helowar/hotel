package com.mangocity.hotel.base.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IQueryQuotaDao;
import com.mangocity.hotel.base.persistence.HtlQuotaCutoffDayNew;
import com.mangocity.hotel.base.persistence.HtlQuotaJudge;
import com.mangocity.hotel.base.persistence.HtlQuotaLog;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.service.assistant.QuotaReturn;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;


/**
 * 
 * @author zuoshengwei
 *
 */

public class QueryQuotaDaoImpl extends GenericDAOHibernateImpl implements IQueryQuotaDao {
	
	public HtlQuotaNew getQuotaNewById(Long quotaNewId) {
		return super.get(HtlQuotaNew.class, quotaNewId);
	}
	
	public void updateQuotaNew(HtlQuotaNew quotaNew) {
		super.update(quotaNew);
	}

    @SuppressWarnings("unchecked")
    public Map<String, ?> queryNewQuotaDetail(HtlQuotaJudge  quotaJudgeVO) {
    	List paramList = new ArrayList();
    	StringBuilder sql = new StringBuilder();
    	sql.append(" FROM HTL_QUOTA_CUTOFF_DAY_NEW QUOTANEW WHERE QUOTANEW.HOTEL_ID = ? ");
    	paramList.add(quotaJudgeVO.getHotelId());
    	
    	if(quotaJudgeVO.getID() > 0){
    		sql.append(" AND QUOTANEW.QUOTA_ID = ? ");
    		paramList.add(Long.valueOf(quotaJudgeVO.getID()));
    	}
    	
    	if((null != quotaJudgeVO.getQuotaShare())){
    		sql.append(" AND QUOTANEW.QUOTA_SHARE = ? ");
    		paramList.add(quotaJudgeVO.getQuotaShare());
    	}
    	
    	if( null != quotaJudgeVO.getStartDate() && null != quotaJudgeVO.getEndDate() ){
    		sql.append(" AND QUOTANEW.ABLE_DATE BETWEEN ? AND ? ");
    		paramList.add(quotaJudgeVO.getStartDate());
    		paramList.add(quotaJudgeVO.getEndDate());    		
    	}
    	
    	if(null != quotaJudgeVO.getWeeks() && quotaJudgeVO.getWeeks().length < 7){
			sql.append(" AND (TO_CHAR(QUOTANEW.ABLE_DATE, 'D')) IN (");
			for(String weekStr : quotaJudgeVO.getWeeks()){
				paramList.add(weekStr);
				sql.append("?, ");
			}
			sql.setCharAt(sql.length() - 2, ')');
		}
    	
    	if(null != quotaJudgeVO.getQuotaType()){
    		sql.append(" AND QUOTANEW.QUOTA_TYPE = ? ");
    		paramList.add(quotaJudgeVO.getQuotaType());
    	}else{
    		sql.append(" AND QUOTANEW.QUOTA_TYPE IN (1, 2, 3)");
    	}
    	
    	if(null != quotaJudgeVO.getRoomtypeId()){
    		sql.append(" AND QUOTANEW.ROOMTYPE_ID = ? ");
    		paramList.add(quotaJudgeVO.getRoomtypeId());
    	}
    	
    	if(null != quotaJudgeVO.getBedId()){
    		sql.append(" AND QUOTANEW.BED_ID = ? ");
    		paramList.add(quotaJudgeVO.getBedId());
    	}
    	sql.append(" ORDER BY ABLE_DATE, ROOMTYPE_ID, BED_ID, QUOTA_SHARE ");
    	int firstLengthOfSql = sql.length();
    	sql.insert(0, "QUOTA_NUM, QUOTA_AVAILABLE, QUOTA_USED, OUTOFDATE_FLAG, ISBACK ");
    	sql.insert(0, "QUOTA_PATTERN, QUOTA_SHARE, QUOTA_HOLDER, ABLE_DATE, CUTOFFDAY, CUTOFFTIME, ");
    	sql.insert(0, "SELECT HTL_QUOTA_DETAIL_ID, QUOTA_ID, HOTEL_ID, ROOMTYPE_ID, BED_ID, QUOTA_TYPE, ");
    	
    	int startNo = (quotaJudgeVO.getPageNo() - 1) * quotaJudgeVO.getPageSize();
    	List<?> quotaCutoffDayNewList = super.queryByNativeSQL(sql.toString(), startNo, quotaJudgeVO.getPageSize(), paramList.toArray(),
				HtlQuotaCutoffDayNew.class);
 	    
 	    Map map = new HashMap(2);
 	    map.put("query_newquota_detail_list", quotaCutoffDayNewList); 
 	    
 	    sql.delete(0, sql.length() - firstLengthOfSql);
 	    sql.insert(0, "SELECT count(*) ");
 	    List<?> countList = super.queryByNativeSQL(sql.toString(), 0, 0, paramList.toArray(), null);
		if(countList.isEmpty()){
			map.put("query_newquota_detail_totalnum",Long.valueOf(0L));
		}else{
			map.put("query_newquota_detail_totalnum", ((BigDecimal)countList.get(0)).longValue());
		}
     	
 		return map;
	}
    
    @SuppressWarnings("unchecked")
    public Map<String, ?> queryNewQuotaByForwarn(Long hotelId, Date beginDate, Date endDate, Long roomTypeId, 
    		String quotaHolder, Long shareType, Long bedId, Long forewarnFlag, int pageNo, int pageSize){
    	StringBuilder hql = new StringBuilder();
    	List paramList = new ArrayList();
    	hql.append(" from HtlQuotaNew quotanew where hotelId = ? ");
    	paramList.add(hotelId);
    	
    	if((null != shareType)){
    		hql.append(" and quotanew.quotaShareType = ? ");
    		paramList.add(shareType);
    	}
    	if(null != beginDate && null != endDate){
    		hql.append(" and quotanew.ableSaleDate between ? and ? ");
    		paramList.add(beginDate);
    		paramList.add(endDate);
    	}else{
    		hql.append(" and ableSaleDate >= ?");
    		paramList.add(new Date());
    	}
    	
      	if(null != roomTypeId && 0 < roomTypeId){
    		hql.append(" and quotanew.roomtypeId = ? ");
    		paramList.add(roomTypeId);
    	}
    	
    	if(null != bedId){
    		hql.append(" and quotanew.bedId = ? ");
    		paramList.add(bedId);
    	}
    	
    	if(null != forewarnFlag){
    		hql.append(" and quotanew.forewarnFlag = ? ");
    		paramList.add(forewarnFlag);
    	}
    	hql.append(" order by ableSaleDate, roomtypeId, bedId ");
    	int startNo = (pageNo - 1) * pageSize;
    	List<HtlQuotaNew> quotaNewList = super.query(hql.toString(), paramList.toArray(), startNo, pageSize, false);
    	
    	Map map = new HashMap(2);
 	    map.put("query_newquota_detail_list", quotaNewList); 
 	    
 	    hql.insert(0, "select count(*) ");
 	    List<?> countList = super.query(hql.toString(), paramList.toArray());
   	
		if(countList.isEmpty()){
			map.put("query_newquota_detail_totalnum", Long.valueOf(0L));
		}else{
			map.put("query_newquota_detail_totalnum", (Long)countList.get(0));
		}
    	return map;
	}
    
    @SuppressWarnings(value={"deprecation", "unchecked"})
    public String saveQuotaJudge(HtlQuotaJudge htlQuotaJudge){
    	//输入参数的索引和参数值键值对
    	Map inParamIdxAndValue = new HashMap(20);
    	inParamIdxAndValue.put(Integer.valueOf(1), htlQuotaJudge.getHotelId());
    	inParamIdxAndValue.put(Integer.valueOf(2), htlQuotaJudge.getRoomtypeId());
    	inParamIdxAndValue.put(Integer.valueOf(3), htlQuotaJudge.getBedId());
    	inParamIdxAndValue.put(Integer.valueOf(4), htlQuotaJudge.getQuotaType());
    	inParamIdxAndValue.put(Integer.valueOf(5), htlQuotaJudge.getQuotaPattern());    	
    	inParamIdxAndValue.put(Integer.valueOf(6), htlQuotaJudge.getQuotaShare());
    	inParamIdxAndValue.put(Integer.valueOf(7), htlQuotaJudge.getQuotaHolder());
    	inParamIdxAndValue.put(Integer.valueOf(8), DateUtil.dateToString(htlQuotaJudge.getStartDate()));
    	inParamIdxAndValue.put(Integer.valueOf(9), DateUtil.dateToString(htlQuotaJudge.getEndDate()));
    	inParamIdxAndValue.put(Integer.valueOf(10), htlQuotaJudge.getCutoffday());
    	inParamIdxAndValue.put(Integer.valueOf(11), htlQuotaJudge.getCutofftime());
    	inParamIdxAndValue.put(Integer.valueOf(12), htlQuotaJudge.getJudgeType());
    	inParamIdxAndValue.put(Integer.valueOf(13), htlQuotaJudge.getQuotaNum());
    	inParamIdxAndValue.put(Integer.valueOf(14), htlQuotaJudge.getQuotaChannel());
    	inParamIdxAndValue.put(Integer.valueOf(15), htlQuotaJudge.getOperatorDept());
    	inParamIdxAndValue.put(Integer.valueOf(16), htlQuotaJudge.getOperatorId());
    	inParamIdxAndValue.put(Integer.valueOf(17), htlQuotaJudge.getOperatorName());    	
    	inParamIdxAndValue.put(Integer.valueOf(18), htlQuotaJudge.getBlnBack());
    	inParamIdxAndValue.put(Integer.valueOf(19), htlQuotaJudge.getJudgeWeeks());
    	inParamIdxAndValue.put(Integer.valueOf(20), Long.valueOf(htlQuotaJudge.getQuotaCutoffDayNewId()));
    	
    	//输出参数的索引和数据类型键值对
    	Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(1);
    	outParamIdxAndType.put(Integer.valueOf(21), Integer.valueOf(java.sql.Types.VARCHAR));

        Map<Integer, ?> resultMap = super.execProcedure("{ call PRC_QUOTASAVE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 
        			inParamIdxAndValue, outParamIdxAndType);
        return resultMap.get(Integer.valueOf(21)).toString();
    }
    
    @SuppressWarnings("deprecation")
    public void quotaRefactor(Long quotaJudgeId){
        super.execProcedure("{ ? = call FUN_QUOTAREFACTOR(?)}", new Object[]{"", quotaJudgeId});
    }
    
    public long getQuotaNewCountByHtlId(Long hotelId) {
    	StringBuilder hql=new StringBuilder();
		hql.append(" select count(quotanew.ID) from HtlQuotaNew quotanew ");
		hql.append(" where quotanew.ableSaleDate >= TRUNC(sysdate) and quotanew.hotelId = ? ");
    	hql.append(" and quotanew.forewarnFlag = 1 and quotanew.quotaHolder ='CC' ");
    	List<?> countList = super.query(hql.toString(), new Object[]{hotelId});
    	
    	return countList.isEmpty()?0L : Long.parseLong(countList.get(0).toString());
    }
    
    public void saveQuotaJudgeModel(HtlQuotaJudge quotaJudge) {
    	super.save(quotaJudge);
    }
    
    public List<HtlQuotaNew> getQuotaNewsByQuotaJude(HtlQuotaJudge quotaJudge) {
    	StringBuilder hql = new StringBuilder();
    	hql.append(" from HtlQuotaNew quota where quota.ableSaleDate >= ? ");
    	hql.append(" and quota.ableSaleDate <= ? and quota.quotaHolder = ? ");
    	hql.append(" and quota.hotelId = ? and quota.quotaShareType = ? ");
    	hql.append(" and quota.roomtypeId = ? and quota.bedId = ? ");
        Object[] params = {quotaJudge.getStartDate(), quotaJudge.getEndDate(), quotaJudge.getQuotaHolder(), 
        		quotaJudge.getHotelId(),Long.valueOf(quotaJudge.getQuotaShare()), quotaJudge.getRoomtypeId(), 
        		quotaJudge.getBedId()};
        return super.query(hql.toString(), params);
    }
    
    public void batchSaveOrUpdateQuotaNew(List<HtlQuotaNew> quatoNewList) {
    	super.saveOrUpdateAll(quatoNewList);
    }
    
    public long getHotelCount4QuotaForewarn() {
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select count(hotelId) from HtlQuotaNew where forewarnFlag = 1 ");
    	hql.append(" and ableSaleDate >= trunc(sysdate) and ableSaleDate <= trunc(sysdate+60) group by hotelId");    	
    	List<?> countList = super.query(hql.toString(), null);
    	
    	return countList.isEmpty()?0L :countList.size();
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getBuyQuotaByHtlIdSaleDate(String beginDateStr, String endDateStr,
			String hotelIdStr) {
		StringBuilder sql = new StringBuilder();
    	sql.append(" select ABLE_SALE_DATE, CHN_NAME, ROOM_NAME, sum(COMMON_QUOTA_SUM) COMMON_QUOTA_SUM, ");
    	sql.append(" sum(COMMON_QUOTA_USED_NUM) COMMON_QUOTA_USED_NUM, ");
    	sql.append(" sum(COMMON_QUOTA_OUTOFDATE_NUM) COMMON_QUOTA_OUTOFDATE_NUM, ");
    	sql.append(" sum(BUY_QUOTA_SUM) BUY_QUOTA_SUM, sum(BUY_QUOTA_USED_NUM) BUY_QUOTA_USED_NUM, ");
    	sql.append(" sum(BUY_QUOTA_OUTOFDATE_NUM) BUY_QUOTA_OUTOFDATE_NUM, ");
    	sql.append(" sum(OUTSIDE_QUOTA_SUM) OUTSIDE_QUOTA_SUM, sum(CASUAL_QUOTA_SUM) CASUAL_QUOTA_SUM, ");
    	sql.append(" sum(CASUAL_QUOTA_USED_NUM) CASUAL_QUOTA_USED_NUM, ");
    	sql.append(" sum(CASUAL_QUOTA_OUTOFDATE_NUM) CASUAL_QUOTA_OUTOFDATE_NUM ");
    	sql.append(" from ");
    	sql.append(" ( SELECT H.ABLE_SALE_DATE, HTL.CHN_NAME, ROOM.ROOM_NAME, ");
    	sql.append(" NVL(H.COMMON_QUOTA_SUM,0) COMMON_QUOTA_SUM, ");
    	sql.append(" NVL(H.COMMON_QUOTA_USED_NUM,0) COMMON_QUOTA_USED_NUM, ");
    	sql.append(" NVL(H.COMMON_QUOTA_OUTOFDATE_NUM,0) COMMON_QUOTA_OUTOFDATE_NUM, ");
    	sql.append(" NVL(H.BUY_QUOTA_SUM,0) BUY_QUOTA_SUM, NVL(H.BUY_QUOTA_USED_NUM,0) BUY_QUOTA_USED_NUM, ");
    	sql.append(" NVL(H.BUY_QUOTA_OUTOFDATE_NUM,0) BUY_QUOTA_OUTOFDATE_NUM, ");
    	sql.append(" NVL(H.OUTSIDE_QUOTA_SUM,0) OUTSIDE_QUOTA_SUM, ");
    	sql.append(" NVL(H.CASUAL_QUOTA_SUM,0) CASUAL_QUOTA_SUM, ");
    	sql.append(" NVL(H.CASUAL_QUOTA_USED_NUM,0) CASUAL_QUOTA_USED_NUM, ");
    	sql.append(" NVL(H.CASUAL_QUOTA_OUTOFDATE_NUM,0) CASUAL_QUOTA_OUTOFDATE_NUM ");
    	sql.append(" FROM HTL_QUOTA_NEW H ,HTL_ROOMTYPE ROOM,HTL_HOTEL HTL ");
    	sql.append(" WHERE ROOM.ROOM_TYPE_ID = H.ROOMTYPE_ID AND HTL.HOTEL_ID = H.HOTEL_ID ");
    	sql.append(" AND H.QUOTA_SHARE_TYPE = 2 AND ROOM.ROOM_NAME like '%世博特惠%' ");
    	sql.append(" AND H.ABLE_SALE_DATE >= to_date(?, 'YYYY-MM-DD') ");
    	sql.append(" AND H.ABLE_SALE_DATE <= to_date(?, 'YYYY-MM-DD') ");
    	sql.append(" AND H.HOTEL_ID in (?) )");
    	sql.append(" GROUP BY ABLE_SALE_DATE, CHN_NAME, ROOM_NAME ");
    	sql.append(" ORDER BY CHN_NAME, ABLE_SALE_DATE, ROOM_NAME ");
    	
    	List<?> resultList = super.queryByNativeSQL(sql.toString(), 0, 0, 
    			new Object[] { beginDateStr, endDateStr, hotelIdStr }, null);
    	if(resultList.isEmpty()) {
    		return Collections.EMPTY_LIST;
    	}
    	
    	return putEachRecord2MapAndReturnList(resultList);
    }

	private List<Map<String, String>> putEachRecord2MapAndReturnList(List<?> resultList) {
		List<Map<String, String>> returnedRecordList = new ArrayList<Map<String, String>>(resultList.size());
    	for(Object resultObj : resultList) {
    		Object[] objArray = (Object[])resultObj;
    		Map<String, String> recordMap = new HashMap<String, String>(resultList.size());
    		recordMap.put("ABLE_SALE_DATE", objArray[0].toString());
    		recordMap.put("CHN_NAME", objArray[1].toString());
    		recordMap.put("ROOM_NAME", objArray[2].toString());
    		recordMap.put("COMMON_QUOTA_SUM", objArray[3].toString());
    		recordMap.put("COMMON_QUOTA_USED_NUM", objArray[4].toString());
    		recordMap.put("COMMON_QUOTA_OUTOFDATE_NUM", objArray[5].toString());
    		recordMap.put("BUY_QUOTA_SUM", objArray[6].toString());
    		recordMap.put("BUY_QUOTA_USED_NUM", objArray[7].toString());
    		recordMap.put("BUY_QUOTA_OUTOFDATE_NUM", objArray[8].toString());
    		recordMap.put("OUTSIDE_QUOTA_SUM", objArray[9].toString());
    		recordMap.put("CASUAL_QUOTA_SUM", objArray[10].toString());
    		recordMap.put("CASUAL_QUOTA_USED_NUM", objArray[11].toString());
    		recordMap.put("CASUAL_QUOTA_OUTOFDATE_NUM", objArray[12].toString());
    		
    		returnedRecordList.add(recordMap);
    	}
    	
		return returnedRecordList;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, ?> queryNewQuotaDateList(Long hotelId, List<HtlRoomtype> roomTypeList,  int pageNo, int pageSize) {
		StringBuilder hql = new StringBuilder();    	
    	hql.append(" from HtlQuotaNew quotanew where quotanew.ableSaleDate >= TRUNC(sysdate) ");
    	hql.append(" and quotanew.hotelId = ? and quotanew.forewarnFlag = 1 ");
    	hql.append(" and quotanew.quotaHolder = 'CC' ");    	
    	List paramsList = new ArrayList();
    	paramsList.add(hotelId);

    	if(!roomTypeList.isEmpty()) {
    		hql.append(" and quotanew.roomtypeId in( ");    	
            for (HtlRoomtype roomtype : roomTypeList) {
            	hql.append("?, ");
            	paramsList.add(roomtype.getID());
            }
            hql.setCharAt(hql.length() - 2, ')');
    	}
    	hql.append(" order by quotanew.ableSaleDate");
    	int firstLenthOfHql = hql.length();   	
    	
    	int startNo = (pageNo - 1) * pageSize;
    	hql.insert(0, "select distinct quotanew.ableSaleDate ");
	    List<?> saleDateList = super.query(hql.toString(), paramsList.toArray(), startNo, pageSize, false);
	    
	    Map map = new HashMap(2);
	    map.put("query_newquota_detail_list", saleDateList); 
	    
	    hql.delete(0, hql.length() - firstLenthOfHql);
	    hql.insert(0, "select count(distinct quotanew.ableSaleDate) ");
    	List<?> countList = super.query(""+ hql.toString(), paramsList.toArray());
		if(!countList.isEmpty()){
				map.put("query_newquota_detail_totalnum", (Long)countList.get(0));
		}
    	
		return map;
	}
	
	public HtlQuotaCutoffDayNew getCutOffDayDetail(QuotaQuery quotaQuery, int cutOffDay) {
		StringBuilder sql = new StringBuilder();
		appendCommonStatement2Sql(sql);		
		sql.append(" AND HTLQUOTACU0_.ABLE_DATE >= to_date(?, 'yyyy-MM-dd') ");		
		sql.append(" AND HTLQUOTACU0_.ABLE_DATE < to_date(?, 'yyyy-MM-dd') ");
		sql.append(" AND HTLQUOTACU0_.QUOTA_AVAILABLE > 0 ");				
		sql.append(" AND HTLQUOTACU0_.CUTOFFDAY <= ? ");
		sql.append(" AND HTLQUOTACU0_.QUOTA_SHARE = ? ");		
		appendQuotaHolder2SqlByMemberType(quotaQuery.getMemberType(), sql);		
		sql.append(" ORDER BY HTLQUOTACU0_.ABLE_DATE, HTLQUOTACU0_.CUTOFFDAY DESC ");		
		sql.append(" ) QD WHERE ROWNUM = 1 ");
		Object[] params = new Object[] {quotaQuery.getHotelId(), quotaQuery.getRoomTypeId(), quotaQuery.getBedID(),
				quotaQuery.getQuotaType(), DateUtil.dateToString(quotaQuery.getBeginDate()),
				DateUtil.dateToString(quotaQuery.getEndDate()), Integer.valueOf(cutOffDay), quotaQuery.getQuotaShare()};		
		List<HtlQuotaCutoffDayNew> resultList = super.queryByNativeSQL(sql.toString(), 0, 0, params, 
				HtlQuotaCutoffDayNew.class);		
		
		return resultList.isEmpty()?null : resultList.get(0);
	}

	private void appendCommonStatement2Sql(StringBuilder sql) {
		sql.append(" SELECT QD.HTL_QUOTA_DETAIL_ID, QD.QUOTA_ID, QD.HOTEL_ID, QD.ROOMTYPE_ID, ");
		sql.append(" QD.BED_ID, QD.QUOTA_TYPE, QD.QUOTA_PATTERN, QD.QUOTA_SHARE, QD.QUOTA_HOLDER, ");
		sql.append(" QD.ABLE_DATE, QD.CUTOFFDAY, QD.CUTOFFTIME, QD.QUOTA_NUM, QD.QUOTA_AVAILABLE, ");
		sql.append(" QD.QUOTA_USED, QD.OUTOFDATE_FLAG, QD.ISBACK ");
		sql.append(" FROM ");
		sql.append(" ( SELECT HTLQUOTACU0_.HTL_QUOTA_DETAIL_ID, HTLQUOTACU0_.QUOTA_ID, HTLQUOTACU0_.HOTEL_ID, ");
		sql.append(" HTLQUOTACU0_.ROOMTYPE_ID, HTLQUOTACU0_.BED_ID, HTLQUOTACU0_.QUOTA_TYPE, ");
		sql.append(" HTLQUOTACU0_.QUOTA_PATTERN, HTLQUOTACU0_.QUOTA_SHARE, HTLQUOTACU0_.QUOTA_HOLDER, ");
		sql.append(" HTLQUOTACU0_.ABLE_DATE, HTLQUOTACU0_.CUTOFFDAY, HTLQUOTACU0_.CUTOFFTIME, ");
		sql.append(" HTLQUOTACU0_.QUOTA_NUM, HTLQUOTACU0_.QUOTA_AVAILABLE, HTLQUOTACU0_.QUOTA_USED, ");
		sql.append(" HTLQUOTACU0_.OUTOFDATE_FLAG, HTLQUOTACU0_.ISBACK ");
		sql.append(" FROM HTL_QUOTA_CUTOFF_DAY_NEW HTLQUOTACU0_ ");		
		sql.append(" WHERE HTLQUOTACU0_.HOTEL_ID = ? ");
		sql.append(" AND HTLQUOTACU0_.ROOMTYPE_ID = ? ");
		sql.append(" AND HTLQUOTACU0_.BED_ID = ? ");
		sql.append(" AND HTLQUOTACU0_.QUOTA_TYPE = ? ");
		sql.append(" AND HTLQUOTACU0_.OUTOFDATE_FLAG <= 0  ");
	}

	private void appendQuotaHolder2SqlByMemberType(int memberType, StringBuilder sql) {
		if (memberType == 2) {
			sql.append(" AND ( HTLQUOTACU0_.QUOTA_HOLDER = 'TP' OR  HTLQUOTACU0_.QUOTA_HOLDER = 'CC' ) ");
		}else if (memberType == 3) {
			sql.append(" AND ( HTLQUOTACU0_.QUOTA_HOLDER = 'TMC' OR  HTLQUOTACU0_.QUOTA_HOLDER = 'CC' ) ");
		}else{
			sql.append(" AND HTLQUOTACU0_.QUOTA_HOLDER = 'CC' ");
		}
	}
	
	public HtlQuotaCutoffDayNew getCutOffDayTempDetail(QuotaQuery quotaQuery) {		
		StringBuilder sql = new StringBuilder();
		appendCommonStatement2Sql(sql);
		sql.append(" AND HTLQUOTACU0_.ABLE_DATE >= TO_DATE(?, 'YYYY-MM-DD') ");		
		sql.append(" AND HTLQUOTACU0_.ABLE_DATE < TO_DATE(?, 'YYYY-MM-DD') ");
		sql.append(" AND HTLQUOTACU0_.QUOTA_AVAILABLE > 0 ");
		appendQuotaHolder2SqlByMemberType(quotaQuery.getMemberType(), sql);
		sql.append(" ORDER BY HTLQUOTACU0_.ABLE_DATE, HTLQUOTACU0_.CUTOFFDAY DESC ");
		sql.append(" ) QD WHERE ROWNUM = 1 ");
		Object[] params = new Object[] {quotaQuery.getHotelId(), quotaQuery.getRoomTypeId(), quotaQuery.getBedID(),
				quotaQuery.getQuotaType(), DateUtil.dateToString(quotaQuery.getBeginDate()),
				DateUtil.dateToString(quotaQuery.getEndDate())};
		List<HtlQuotaCutoffDayNew> resultList = super.queryByNativeSQL(sql.toString(), 0, 0, params,
				HtlQuotaCutoffDayNew.class);
		
		return resultList.isEmpty()?null : resultList.get(0);
	}
	
	public HtlQuotaCutoffDayNew getQuotaReturnDetail(QuotaReturn quotaReturn) {
		StringBuilder sql = new StringBuilder();
		appendCommonStatement2Sql(sql);
		sql.append(" AND HTLQUOTACU0_.ABLE_DATE = TO_DATE(?, 'YYYY-MM-DD') ");
		sql.append(" AND HTLQUOTACU0_.QUOTA_SHARE = ? ");		
		sql.append(" AND HTLQUOTACU0_.QUOTA_USED > 0 ");
		appendQuotaHolder2SqlByMemberType(quotaReturn.getMemberType(), sql);
		sql.append(" ORDER BY HTLQUOTACU0_.CUTOFFDAY ASC ");
		sql.append(" ) QD WHERE ROWNUM = 1 ");		
		Object[] params = new Object[] {quotaReturn.getHotelId(), quotaReturn.getRoomTypeId(), 
				quotaReturn.getBedId(), quotaReturn.getQuotaType(), DateUtil.dateToString(quotaReturn.getQuotaDate()),
				(quotaReturn.getQuotaShare() == null?"" : quotaReturn.getQuotaShare())};
		List<HtlQuotaCutoffDayNew> resultList = super.queryByNativeSQL(sql.toString(), 0, 0, params,
				HtlQuotaCutoffDayNew.class);
		
		return resultList.isEmpty()?null : resultList.get(0);
	}
	
	public HtlQuotaCutoffDayNew getQuotaReturnTempDetail(QuotaReturn quotaReturn) {		
		StringBuilder sql = new StringBuilder();
		appendCommonStatement2Sql(sql);
		sql.append(" AND HTLQUOTACU0_.ABLE_DATE = TO_DATE(?, 'YYYY-MM-DD') ");
		sql.append(" AND HTLQUOTACU0_.OUTOFDATE_FLAG = 0 ");
		sql.append(" AND HTLQUOTACU0_.QUOTA_USED > 0 ");
		appendQuotaHolder2SqlByMemberType(quotaReturn.getMemberType(), sql);		
		sql.append(" ORDER BY HTLQUOTACU0_.ABLE_DATE, HTLQUOTACU0_.CUTOFFDAY DESC ");
		sql.append(" ) QD WHERE ROWNUM = 1 ");		
		Object[] params = new Object[] {quotaReturn.getHotelId(), quotaReturn.getRoomTypeId(), 
				quotaReturn.getBedId(), quotaReturn.getQuotaType(), DateUtil.dateToString(quotaReturn.getQuotaDate())};
		List<HtlQuotaCutoffDayNew> resultList = super.queryByNativeSQL(sql.toString(), 0, 0, params,
				HtlQuotaCutoffDayNew.class);
		
		return resultList.isEmpty()?null : resultList.get(0);		
	}
	
	@SuppressWarnings("deprecation")
	public void updateCutOffDayNew(long quotaCutOffDayNewId) {
		super.execProcedure("{ ? = call FUN_SUM_QUOTAR_CUT(?)}", new Object[]{"", Long.valueOf(quotaCutOffDayNewId)});
	}
	
	public void saveQuotaLog(HtlQuotaLog htlQuotaLog) {
		super.save(htlQuotaLog);
	}
	
	public void updateAvailableQuotaAndUsedQuota(HtlQuotaCutoffDayNew quotaCutOffDayNew) {
		String hql = "update HtlQuotaCutoffDayNew set quotaAvailable = ?, quotaUsed = ? where ID = ?";
		super.updateByQL(hql, new Object[]{quotaCutOffDayNew.getQuotaAvailable(), 
				quotaCutOffDayNew.getQuotaUsed(), quotaCutOffDayNew.getID()});
	}
}
