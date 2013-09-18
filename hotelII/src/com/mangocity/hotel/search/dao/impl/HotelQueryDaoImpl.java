package com.mangocity.hotel.search.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.search.constant.CloseRoomReason;
import com.mangocity.hotel.search.constant.OpenCloseRoom;
import com.mangocity.hotel.search.constant.QueryTableNameConstant;
import com.mangocity.hotel.search.dao.HotelQueryDao;
import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.model.QueryDynamicCondition;
import com.mangocity.hotel.search.model.SortCondition;
import com.mangocity.hotel.search.service.assistant.SortResType;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;
import com.mangocity.util.hotel.constant.SaleChannel;
import com.mangocity.util.log.MyLog;

/**
 * 酒店查询Dao接口实现
 * @author yong.zeng
 *
 */

public class HotelQueryDaoImpl extends GenericDAOHibernateImpl implements HotelQueryDao {
  
	private static final MyLog log = MyLog.getLogger(HotelQueryDaoImpl.class);
	
	public static Map<String, String> tableNameMap = new HashMap<String, String>();
	
	/**
	 * 初始化tableNameMap
	 */
	public void init() {
		tableNameMap.put("PEK", "HTLQUERY_PEK");
		tableNameMap.put("SHA", "HTLQUERY_SHA");
		tableNameMap.put("CAN", "HTLQUERY_CAN");
		tableNameMap.put("SZX", "HTLQUERY_SZX");
		tableNameMap.put("HKG", "HTLQUERY_HKG");
		tableNameMap.put("HGH", "HTLQUERY_HGH");
		tableNameMap.put("CTU", "HTLQUERY_CTU");
		tableNameMap.put("NKG", "HTLQUERY_NKG");
    	String hqlstr = "from HtlArea area where area.areaCode = ?";    	
    	List<HtlArea> liArea = super.query(hqlstr, new Object[]{"HDQ"}, 0, 0, false);
    	for(HtlArea area : liArea) {
    		String cityCode = area.getCityCode();
    		if ("SHA".equals(cityCode) || "HGH".equals(cityCode)
					|| "NKG".equals(cityCode)) {
				continue;
			}
			tableNameMap.put(cityCode, "HTLQUERY_HDQ");
    	}
    	hqlstr = "from HtlArea area where area.areaCode = ?";    	
    	liArea = super.query(hqlstr, new Object[]{"HBQ"}, 0, 0, false);
    	for(HtlArea area : liArea) {
    		String cityCode = area.getCityCode();
    		if ("PEK".equals(cityCode)) {
				continue;
			}
			tableNameMap.put(cityCode, "HTLQUERY_HBQ");
    	}
    	
	}
	
    /**
     * 查询数据库，并把数据库返回的记录放到List�?
     * @param queryBean
     * @return 数据记录
     */
    public List<QueryCommodityInfo> queryHotelResultList(QueryCommodityCondition queryBean){
    	//sql语句
  	   String sqlstr = connectQuerySql(queryBean.getCityCode(),queryBean.getHotelIdLst());
    	//查询结果
    	List<Object[]> lstResult = super.queryByNativeSQL(sqlstr, 0, 0, 
    			new Object[]{queryBean.getFromChannel(),
    			queryBean.getInDate(),queryBean.getOutDate()},null);
    	//数据转换
    	if(null==lstResult||lstResult.isEmpty())return Collections.emptyList();
    	return changeArrayToCommodityInfo(lstResult);
    }
    
    /**
     * 封装商品信息查询语句
     * @param citycode 城市三字�?
     * @return
     */
    private static String CONNECT_QUERY_SQL1 = "select tq.queryid,tq.distid,tq.abledate,tq.distribchannel,tq.membertype,tq.usertype,"
			+ "tq.closeflag,tq.closereason,tq.paymethod,tq.commodityid,tq.commodityname,tq.commodityno,tq.commoditycount,"
			+ "tq.bedtype,tq.roomtypeid,tq.roomtypename,tq.hotelid,tq.hdltype,"
			+ "tq.priceid,tq.saleprice,tq.salesroomprice,tq.breakfasttype,tq.breakfastnumber,tq.breakfastprice,"
			+ "tq.currency,tq.dealer_favourableid,tq.dealer_promotionsaleid,tq.provider_favourableid,tq.provider_promotionsaleid,"
			+ "tq.bookclauseid,tq.paytoprepay,tq.bookstartdate,tq.bookenddate,tq.morningtime,tq.eveningtime,tq.continueday,"
			+ "tq.continuum_in_end,tq.continuum_in_start,"
			+ "tq.must_in,tq.restrict_in,tq.continue_dates_relation,tq.need_assure,"
			+ "tq.quotanumber,tq.hasbook,tq.hasoverdraft,tq.formula,tq.commission,tq.commissionrate,tq.freeNet,tq.advice_price  ";    	 	    	
    private String connectQuerySql(String citycode,String hotelIdList){
    	StringBuilder sbsql = new StringBuilder();
    	/*sbsql.append("select tq.queryid,tq.distid,tq.abledate,tq.distribchannel,tq.membertype,tq.usertype,");
    	sbsql.append("tq.closeflag,tq.closereason,tq.paymethod,tq.commodityid,tq.commodityname,tq.commodityno,tq.commoditycount,");
    	sbsql.append("tq.bedtype,tq.roomtypeid,tq.roomtypename,tq.hotelid,tq.hdltype,");
    	sbsql.append("tq.priceid,tq.saleprice,tq.salesroomprice,tq.breakfasttype,tq.breakfastnumber,tq.breakfastprice,");
    	sbsql.append("tq.currency,tq.dealer_favourableid,tq.dealer_promotionsaleid,tq.provider_favourableid,tq.provider_promotionsaleid,");
    	sbsql.append("tq.bookclauseid,tq.paytoprepay,tq.bookstartdate,tq.bookenddate,tq.morningtime,tq.eveningtime,tq.continueday,");
    	sbsql.append("tq.continuum_in_end,tq.continuum_in_start,");
    	sbsql.append("tq.must_in,tq.restrict_in,tq.continue_dates_relation,tq.need_assure,");
    	sbsql.append("tq.quotanumber,tq.hasbook,tq.hasoverdraft,tq.formula,tq.commission,tq.commissionrate  ");*/
    	 
    	//表名
    	String tablename = tableNameMap.get(citycode.toUpperCase());
    	if(null == tablename || 0 == tablename.length()) {
    		tablename = QueryTableNameConstant.OTHER;
    	}
    	/*if("PEK".equals()){
    		tablename = QueryTableNameConstant.PEK;
    	}else if("SHA".equals(citycode.toUpperCase())){
    		tablename = QueryTableNameConstant.SHA;
    	}else if("CAN".equals(citycode.toUpperCase())){
    		tablename = QueryTableNameConstant.CAN;
    	}else if("SZX".equals(citycode.toUpperCase())){
    		tablename = QueryTableNameConstant.SZX;	
    	}else{
    		tablename = QueryTableNameConstant.OTHER;
    	}*/
    	
    	sbsql.append(CONNECT_QUERY_SQL1).append(" from " + tablename + " tq ");
    	
    	//where条件
    	sbsql.append(" where tq.active = 1 and tq.hotelid in ("+ hotelIdList+") and (tq.distribchannel is null or bitand(tq.distribchannel,?) != 0)");
    	sbsql.append(" and tq.abledate >= ? and tq.abledate<? ");
//    	sbsql.append(" and (nvl(tq.closeflag, 'K') = 'K' and nvl(tq.saleprice, 0) > 0 and tq.hasbook = 1) ");
    	sbsql.append("order by tq.hotelid, tq.roomtypeid, tq.commodityid, tq.paymethod, tq.abledate, tq.bedtype");
    	return sbsql.toString();
    	
    }
    
    private List<QueryCommodityInfo> changeArrayToCommodityInfo(List<Object[]> resultLst){
    	List<QueryCommodityInfo> commoLst = new ArrayList<QueryCommodityInfo>();
    	
    	
    	QueryCommodityInfo commo = null;
    	for(int i=0;i<resultLst.size();i++){
    		Object[] objArray = resultLst.get(i);
    		commo = new QueryCommodityInfo();
    		//tq.queryid,tq.distid,tq.abledate,tq.distribchannel,tq.membertype,tq.usertype
    		if(null!=objArray[0])commo.setQueryId(Long.parseLong(objArray[0].toString()));//查询ID
    		if(null!=objArray[1])commo.setDistId(Long.parseLong(objArray[1].toString()));//上架ID
    		if(null!=objArray[2])commo.setAbledate(DateUtil.getDate(objArray[2].toString()));//日期
    		if(null!=objArray[3])commo.setDistchannel(objArray[3].toString());//�?售渠�?
    		if(null!=objArray[4])commo.setMembertype(objArray[4].toString());//会员类型
    		if(null!=objArray[5])commo.setUsertype(objArray[5].toString());//用户类型
    		
    		//tq.closeflag,tq.closereason,tq.paymethod,tq.commodityid,tq.commodityname,tq.commodityno,tq.commoditycount,
    		if(null!=objArray[6])commo.setCloseflag(objArray[6].toString());//关房标志
    		if(null!=objArray[7])commo.setClosereason(objArray[7].toString());//关房原因
    		if(null!=objArray[8])commo.setPaymethod(objArray[8].toString());//支付方式
    		if(null!=objArray[9])commo.setCommodityId(Long.parseLong(objArray[9].toString()));//商品ID
    		if(null!=objArray[10])commo.setCommodityName(objArray[10].toString());//商品名称
    		if(null!=objArray[11])commo.setCommodityNo(objArray[11].toString());//商品编码
    		if(null!=objArray[12])commo.setCommodityCount(Long.parseLong(objArray[12].toString()));//商品数量
    		
    		//tq.bedtype,tq.roomtypeid,tq.roomtypename,tq.hotelid,tq.hdltype,
    		if(null!=objArray[13])commo.setBedtype(objArray[13].toString());//床型
    		if(null!=objArray[14])commo.setRoomtypeId(Long.parseLong(objArray[14].toString()));//房型ID
    		if(null!=objArray[15])commo.setRoomtypeName(objArray[15].toString());//房型名称
    		if(null!=objArray[16])commo.setHotelId(Long.parseLong(objArray[16].toString()));//酒店ID
    		if(null!=objArray[17])commo.setHdltype(objArray[17].toString());//直联类型
    		
    		//tq.priceid,tq.saleprice,tq.salesroomprice,tq.breakfasttype,tq.breakfastnumber,tq.breakfastprice,
    		if(null!=objArray[18])commo.setPriceId(Long.parseLong(objArray[18].toString()));//价格ID
    		if(null!=objArray[19])commo.setSaleprice(Double.parseDouble(objArray[19].toString()));//�?售价
    		if(null!=objArray[20])commo.setSalesroomprice(Double.parseDouble(objArray[20].toString()));//门市�?
    		if(null!=objArray[21])commo.setBreakfasttype(Long.parseLong(objArray[21].toString()));//早餐类型
    		if(null!=objArray[22])commo.setBreakfastnumber(Long.parseLong(objArray[22].toString()));//早餐数量
    		if(null!=objArray[23])commo.setBreakfastprice(Double.parseDouble(objArray[23].toString()));//早餐�?
    		
    		
    		
    		//tq.currency,tq.dealer_favourableid,tq.dealer_promotionsaleid,tq.provider_favourableid,tq.provider_promotionsaleid,
    		if(null!=objArray[24])commo.setCurrency(objArray[24].toString());//币种
    		if(null!=objArray[25])commo.setDealerFavourableId(Long.parseLong(objArray[25].toString()));//商家优惠ID
    		if(null!=objArray[26])commo.setDealerPromotionsaleId(Long.parseLong(objArray[26].toString()));//商家促销ID
    		if(null!=objArray[27])commo.setProviderFavourableId(Long.parseLong(objArray[27].toString()));//供应商优惠ID
    		if(null!=objArray[28])commo.setProviderPromotionsaleId(Long.parseLong(objArray[28].toString()));//供应商促�?ID
    		
    		//tq.bookclauseid,tq.paytoprepay,tq.bookstartdate,tq.bookenddate,tq.morningtime,tq.eveningtime,tq.continueday,
    		if(null!=objArray[29])commo.setBookclauseId(Long.parseLong(objArray[29].toString()));//预订定条款ID
    		if(null!=objArray[30])commo.setPaytoprepay(objArray[30].toString());//面转�?
    		if(null!=objArray[31])commo.setBookstartdate(DateUtil.getDate(objArray[31].toString()));//预订�?始日�?
    		if(null!=objArray[32])commo.setBookenddate(DateUtil.getDate(objArray[32].toString()));//预订结束日期
    		if(null!=objArray[33])commo.setMorningtime(objArray[33].toString());//预订�?始时�?
    		if(null!=objArray[34])commo.setEveningtime(objArray[34].toString());//预订结束时间
    		if(null!=objArray[35])commo.setContinueDay(Long.parseLong(objArray[35].toString()));//连住几天
    		
    		//tq.continuum_in_end,tq.continuum_in_start,
    		if(null!=objArray[36])commo.setContinuumInEnd(DateUtil.getDate(objArray[36].toString()));//连住结束日期
    		if(null!=objArray[37])commo.setContinuumInStart(DateUtil.getDate(objArray[37].toString()));//连住�?始日�?
    		
    		//tq.must_in,tq.restrict_in,tq.continue_dates_relation,tq.need_assure,
    		if(null!=objArray[38])commo.setMustIn(objArray[38].toString());//必住日期的字符串
    		if(null!=objArray[39])commo.setRestrictIn(Long.parseLong(objArray[39].toString()));//限住几天
    		if(null!=objArray[40])commo.setContinueDatesRelation(objArray[40].toString());//必住的关�?,or/and
    		if(null!=objArray[41])commo.setNeedAssure(objArray[41].toString());//是否无条件担�?
    		
    		//tq.quotanumber,tq.hasbook,tq.hasoverdraft 
    		if(null!=objArray[42])commo.setQuotanumber(Long.parseLong(objArray[42].toString()));//配额数量
    		if(null!=objArray[43])commo.setHasbook(objArray[43].toString());//能否预订--房�??
    		if(null!=objArray[44])commo.setHasoverdraft(objArray[44].toString());//能否透支
    		
    		//佣金计算公式,佣金,佣金率
    		if(null!=objArray[45])commo.setFormula(objArray[45].toString());//配额数量
    		if(null!=objArray[46])commo.setCommission(Double.parseDouble(objArray[46].toString()));//能否预订--房�??
    		if(null!=objArray[47])commo.setCommissionRate(Double.parseDouble(objArray[47].toString()));//能否透支    		
    		
    		if (null != objArray[48])
				commo.setFreeNet("1".equals(objArray[48].toString())); // 是否有免费宽带
    		
    		if (null != objArray[49])
				commo.setAdvicePrice(Double.valueOf(objArray[49].toString())); // 净价
    		
    		/**
    		 * 对数据正确性一致性判断
    		 */
    		
    		//checkData(commo);
    		commoLst.add(commo);

    	}
    	
    	return commoLst;
    }
 
    /**
     * 根据动态条件查询商品信息
     * @param queryBean
     * @return
     */
    public Map<Long,List<QueryCommodityInfo>> queryHotelDynamicinfo(QueryDynamicCondition queryBean){
    	
    	List<Object[]> lstResult = getObjectArrFromQueryTable(queryBean);
    	if(null==lstResult || lstResult.isEmpty()) return null;
    	
    	Map<Long,List<QueryCommodityInfo>> map = new HashMap<Long,List<QueryCommodityInfo>>();
    	QueryCommodityInfo commo = null;
    	for(int i=0;i<lstResult.size();i++){
    		
    		Object[] objArray = lstResult.get(i);
    		long curHotelId = Long.parseLong(objArray[1].toString());
    		if(null==map.get(curHotelId)||map.get(curHotelId).isEmpty()){
    			List<QueryCommodityInfo> commoLst = new ArrayList<QueryCommodityInfo>();
    			map.put(curHotelId, commoLst);
    		}

    		commo = new QueryCommodityInfo();
    		if(null!=objArray[0])commo.setQueryId(Long.parseLong(objArray[0].toString()));//查询ID
    		if(null!=objArray[1])commo.setHotelId(curHotelId);//酒店ID
    		if(null!=objArray[2])commo.setAbledate(DateUtil.getDate(objArray[2].toString()));//日期
    		if(null!=objArray[3])commo.setSaleprice(Double.parseDouble(objArray[3].toString()));//价格
    		if(null!=objArray[4])commo.setBreakfastnumber(Long.parseLong(objArray[4].toString()));//早餐数
    		if(null!=objArray[5])commo.setBreakfasttype(Long.parseLong(objArray[5].toString()));//早餐类型
    		map.get(curHotelId).add(commo);
    		
    		
    	}
    	
    	return map;
    }
    
    
    private List<Object[]> getObjectArrFromQueryTable (QueryDynamicCondition queryBean){
    	String citycode = queryBean.getCityCode();
    	//表名
    	String tablename = tableNameMap.get(citycode.toUpperCase());
    	if(null == tablename || 0 == tablename.length()) {
    		tablename = QueryTableNameConstant.OTHER;
    	}
    	/*if("PEK".equals(citycode.toUpperCase())){
    		tablename = QueryTableNameConstant.PEK;
    	}else if("SHA".equals(citycode.toUpperCase())){
    		tablename = QueryTableNameConstant.SHA;
    	}else if("CAN".equals(citycode.toUpperCase())){
    		tablename = QueryTableNameConstant.CAN;
    	}else if("SZX".equals(citycode.toUpperCase())){
    		tablename = QueryTableNameConstant.SZX;	
    	}else{
    		tablename = QueryTableNameConstant.OTHER;
    	}*/
    	
    	String sqlstr = "select hq.queryid,hq.hotelid,hq.abledate,hq.saleprice,hq.breakfastnumber,hq.breakfasttype from " 
    	                + tablename+" hq where hq.active = 1 and hq.hotelid in ("+ queryBean.getHotelIdLst() +") and (hq.distribchannel is null or bitand(hq.distribchannel,?) != 0) "
                     	+" and hq.abledate >= ? and hq.abledate<? and hq.saleprice > 0 ";
    	//TODO 处理hotelIdLst大于1000的，现只处理2000内的，大于2000的会出错 add by diandian.hou
    	String[] hotelIdArray = queryBean.getHotelIdLst().split(",");
    	
    	StringBuffer sqlBuf=new StringBuffer();
    	if(hotelIdArray.length >= 1000){			
			final int MAX_SQL_IN = 999;
    		int totalNum = hotelIdArray.length;
    		int loopNum = hotelIdArray.length/MAX_SQL_IN + 1;
    		boolean fagFull=true;
    		StringBuffer[] hotelIds_sb = new StringBuffer[loopNum];	
    		for(int i = 0; i < loopNum && fagFull; i++){
    			int startIndex = MAX_SQL_IN * i;
        		int endIndex = (MAX_SQL_IN * (i+1) < totalNum)?MAX_SQL_IN*(i+1) : totalNum;
        		hotelIds_sb[i] = new StringBuffer();
    		    for(int j = startIndex; j < endIndex ; j++){
    			    hotelIds_sb[i].append(hotelIdArray[j]+",");  
    		    }
    		    hotelIds_sb[i].deleteCharAt(hotelIds_sb[i].lastIndexOf(","));
    		    if(MAX_SQL_IN * (i+1)>=totalNum){
    		    	fagFull=false;
    		    }
    		}  
    		sqlBuf.append("select hq.queryid,hq.hotelid,hq.abledate,hq.saleprice,hq.breakfastnumber,");
    		sqlBuf.append("hq.breakfasttype from ").append(tablename);
    		sqlBuf.append(" hq where ( hq.hotelid in ("+ hotelIds_sb[0].toString() +") ");
    		for(int i = 1; i < loopNum; i++){
    			sqlBuf.append( " or hq.hotelid in (");
    			sqlBuf.append(hotelIds_sb[i].toString());
    			sqlBuf.append(") ");
    		}
    		sqlBuf.append(") and (hq.distribchannel is null ");
    		sqlBuf.append("or bitand(hq.distribchannel,?) != 0) ");
    		sqlBuf.append("and hq.abledate >= ? and hq.abledate<? and hq.saleprice > 0 ");	
    		
    		sqlstr=sqlBuf.toString();
    	}
    	
    	if(queryBean.getMinPrice()>0){
    		sqlstr+= "  and hq.saleprice>="+queryBean.getMinPrice();
    	}
    	if(queryBean.getMaxPrice()>0){
    		sqlstr+= " and hq.saleprice<="+queryBean.getMaxPrice();
    	}
    	if(queryBean.isContainBreakfast()){
    		sqlstr+= " and hq.BREAKFASTNUMBER>0";
    	}
    	if(queryBean.isHasFreeNet()){
    		sqlstr+= " and hq.freeNet=1";
    	}
    	
    	List<Object[]> lstResult = super.queryByNativeSQL(sqlstr, 0, 0, new Object[]{queryBean.getFromChannel(),
    			queryBean.getInDate(),queryBean.getOutDate()},null);
    	
    	return lstResult;
    }
    /**
     * 根据动态条件查询商品信息,得到字符串
     * @param queryBean
     * @return
     */
    public String queryHotelDynamicinfoGetStr(QueryDynamicCondition queryBean){
    	
    	List<Object[]> lstResult = getObjectArrFromQueryTable(queryBean);
    	if(null==lstResult || lstResult.isEmpty()) return null;

    	String idLst = "";
    	Map<Long,Long> map = new HashMap<Long,Long>();
    	
    	for(int i=0;i<lstResult.size();i++){
    		Object[] objArray = lstResult.get(i);
    		long curHotelId = Long.parseLong(objArray[1].toString());
    		if(null==map.get(curHotelId)){
    			map.put(curHotelId, curHotelId);
    			idLst = idLst + curHotelId+",";
    		}
    	}
    	if(idLst.endsWith(","))idLst=idLst.substring(0,idLst.length()-1);
    	
    	return idLst;
    	
    }    
    
    /**
     * 数据完整性和一致性判断,比如:关房字段为"G"时,还需要考虑关房原因,这有二义性
     * 
     * @param commo
     */
    private void checkData(QueryCommodityInfo commo){
    	
    	if(OpenCloseRoom.CloseRoom.equals(commo.getCloseflag())&&CloseRoomReason.CloseCCCan.equals(commo.getClosereason())){//策略性关房CC可订	
    		if( (Long.parseLong(SaleChannel.CC) & Long.parseLong(commo.getDistchannel())) != 0   ){//CC渠道时
    			commo.setCloseflag(OpenCloseRoom.OpenRoom);
    		}
    	}
    }
    
    /**
     * 查询宽带
     * @param qcc
     * @return
     */
    /*public List<HtlInternet> queryFreeNet(QueryCommodityCondition qcc){
    	String hql =" from  HtlInternet net where net.hotelId in("+qcc.getHotelIdLst()+") and net.internetBeginDate<=? and net.internetEndDate>=? and net.active=1";
    	return super.query(hql, new Object[]{qcc.getInDate(),qcc.getOutDate()});
    }*/    
    
    @SuppressWarnings(value={"deprecation","unchecked"})
    public SortResType hotelSort(SortCondition sc, String hotelIdList) {
		Map inParamIdxAndValue = new HashMap(8);
		inParamIdxAndValue.put(Integer.valueOf(1), DateUtil.dateToString(sc
				.getInDate()));
		inParamIdxAndValue.put(Integer.valueOf(2), DateUtil.dateToString(sc
				.getOutDate()));
		inParamIdxAndValue.put(Integer.valueOf(3), sc.getCityCode());
		inParamIdxAndValue.put(Integer.valueOf(4), hotelIdList);
		inParamIdxAndValue.put(Integer.valueOf(5), sc.getSorttype());
		inParamIdxAndValue.put(Integer.valueOf(6), sc.getSortUpOrDown());
		inParamIdxAndValue.put(Integer.valueOf(7), sc.getPageSize());
		inParamIdxAndValue.put(Integer.valueOf(8), sc.getPageNo());
		inParamIdxAndValue.put(Integer.valueOf(9), sc.getGeoId());

		Map<Integer, Integer> outParamIdxAndType = new HashMap<Integer, Integer>(
				3);
		outParamIdxAndType.put(Integer.valueOf(10), Integer
				.valueOf(java.sql.Types.VARCHAR));
		outParamIdxAndType.put(Integer.valueOf(11), Integer
				.valueOf(java.sql.Types.INTEGER));
		outParamIdxAndType.put(Integer.valueOf(12), Integer
				.valueOf(java.sql.Types.VARCHAR));
		Map<Integer, ?> resultMap = super.execProcedure(
				"{call sp_hotelIIsort(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}",
				inParamIdxAndValue, outParamIdxAndType);
		
		// 设置返回的酒店数和酒店ID字符串
		SortResType sortRes = new SortResType();
		String idList = (String) resultMap.get(10);
		sortRes.setSortedHotelIdList(StringUtil.isValidStr(idList) ? idList : "");
		Integer iCount = (Integer) resultMap.get(11);
		sortRes.setHotelCount(null != iCount ? iCount : 0);

		return sortRes;
	}


	public List<Long> queryHasPrepayPriceTypeHotelAndSaleChannel(String hotelIdList,String payMethod,Date beginDate,Date endDate,String cityCode,String saleChannel) {
		String tablename = null;
		if(cityCode!=null){
		tablename=tableNameMap.get(cityCode.toUpperCase());
		}
		if(null == tablename || 0 == tablename.length()) {
    		tablename = QueryTableNameConstant.OTHER;
    	}
	   StringBuilder sql=new StringBuilder(" select hp.hotelid from ") ;
	   sql.append(tablename);
	   sql.append(" hp  where exists (" );	
	   
	   sql.append("select h.hotel_id from htl_hotel h where h.hotel_id in(");
	   sql.append(hotelIdList);
	   sql.append (" ) and  h.webprepayshow=1 and  h.hotel_id=hp.hotelid");
	   sql.append("  ) and hp.paymethod=? ");
	   sql.append(" and hp.abledate >=?  ");
	   sql.append(" and hp.abledate <? ");
	   sql.append(" and bitand(hp.distribchannel,?) <> 0");
	   sql.append(" group by hp.hotelid ");

		return super.queryByNativeSQL(sql.toString(),new Object[]{payMethod,beginDate,endDate,saleChannel});
	}
	
	public List<Long> queryHasPrepayPriceTypeHotel(String hotelIdList,String payMethod,Date beginDate,Date endDate,String cityCode) {
		String tablename = null;
		if(cityCode!=null){
		tablename=tableNameMap.get(cityCode.toUpperCase());
		}
		if(null == tablename || 0 == tablename.length()) {
    		tablename = QueryTableNameConstant.OTHER;
    	}
	   StringBuilder sql=new StringBuilder(" select hp.hotelid from ") ;
	   sql.append(tablename);
	   sql.append(" hp  where hp.hotelid in (" );	
	   sql.append(hotelIdList);
	   sql.append("  ) and hp.paymethod=? ");
	   sql.append(" and hp.abledate >=?  ");
	   sql.append(" and hp.abledate <? ");	  
	   sql.append(" group by hp.hotelid ");

		return super.queryByNativeSQL(sql.toString(),new Object[]{payMethod,beginDate,endDate});
	}
	
   
}
