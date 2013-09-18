package com.mangocity.hotel.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IQueryQuotaDao;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuotaCutoffDayNew;
import com.mangocity.hotel.base.persistence.HtlQuotaJudge;
import com.mangocity.hotel.base.persistence.HtlQuotaLog;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.HotelRoomService;
import com.mangocity.hotel.base.service.IQuotaControlService;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.service.assistant.QuotaReturn;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.log.MyLog;

public class QuotaControlImpl implements IQuotaControlService {

	private static final MyLog log = MyLog.getLogger(QuotaControlImpl.class);

	public IQuotaManage quotaManage;
	
	private IQueryQuotaDao qryQuotaDao;
	
	private HotelRoomService hotelRoomService;
	
	private ContractManage contractManage;
	
	private IPriceManage priceManage;

	/**
	 * 配额共享时
	 */
    private HotelManage hotelManage;
    
	/**
	 * 扣配额方法 
	 * 1. 首先判定合同为进店还是在店模式, 
	 * 2. 合同为空时直接返回可扣配额,并且不可退 
	 * 3. 合同为进店模式时,扣除所有房第一天的配额数, 其它天的配额不用扣,但返回的其他天配额数量为1,操作日志记录里面也记录为1 
	 * 4. 合同为在店模式时,扣除所有房每一天的数据, 扣除时查询配额明细需要一天一天的查询,主要是因为每天的CUTOFFDAY不同所致
	 * @param quotaQuery
	 */
	public List<QuotaReturn> deductQuota(QuotaQuery quotaQuery) {

		List<QuotaReturn> quotaReturnList = new ArrayList<QuotaReturn>();

		// 判断传入类是否为空
		if (null == quotaQuery || 0 == quotaQuery.getQuotaNum()) {
			QuotaReturn quotaReturn = new QuotaReturn();
			quotaReturnList.add(quotaReturn);
			return quotaReturnList;
		}

		/**
		 * 计算入住日期和退房日期相间隔天数，即入住几晚
		 */
		int dayNum = DateUtil.getDay(quotaQuery.getBeginDate(), quotaQuery.getEndDate());
		/**
		 * 计算CUTOFFDAY天数
		 */
		int cutOffDayNum = DateUtil.getDay(new Date(), quotaQuery.getBeginDate());

        /**
         * 增加配额的床型是否共享
         * add by guojun 2010-1-25 17:50
         */
        HtlRoomtype HtlRoomtype = hotelManage.findHtlRoomtype(quotaQuery.getRoomTypeId());
        
        Long bedTypeBackUp=0L;
        if(HtlRoomtype!=null){
        	if(null !=HtlRoomtype.getQuotaBedShare()){
	        	if(HtlRoomtype.getQuotaBedShare()>0){
	        		bedTypeBackUp = quotaQuery.getBedID();  
	        		/**
	        		 * 配额共享就扣双床的配额
	        		 */
	        		quotaQuery.setBedID(2L);
	        	}
        	}
        }
        
		int roomCout = quotaQuery.getQuotaNum();
		
		/**
		 * 查找合同判断配额模式，根据配额模式扣配额， 如果为S-I在店每天都扣，如果为C-I进店只扣第一天
		 * 合同查找条件为入住第一天所在合同里的配额模式
		 */
		HtlContract htlContract = contractManage.getHtlContractByHtlIdDateRange(quotaQuery.getHotelId(), 
				quotaQuery.getBeginDate(), quotaQuery.getEndDate());
		String quotaPattern = htlContract == null?null : htlContract.getQuotaPattern();
		if (null == quotaPattern) {
			for (int i = 0; i < dayNum; i++) {
				for (int j = 0; j < roomCout; j++) {
					QuotaReturn quotaReturn = new QuotaReturn();

					// C-1进店模式，填写下面参数
					// 酒店id
					quotaReturn.setHotelId(quotaQuery.getHotelId());
					// 房型id
					quotaReturn.setRoomTypeId(quotaQuery.getRoomTypeId());
					// 价格类型
					quotaReturn.setChildRoomTypeId(quotaQuery.getChildRoomId());
					// 床型ID
					quotaReturn.setBedId(quotaQuery.getBedID());
					// 支付方式
					quotaReturn.setPayMethod(quotaQuery.getPayMethod());
					// 会员类型(TMC,CC,TP)
					quotaReturn.setMemberType(quotaQuery.getMemberType());
					// 被扣配额的所属日期
					quotaReturn.setQuotaDate(DateUtil.getDate(quotaQuery
							.getBeginDate(), i));
					// 扣配额日期
					quotaReturn.setUseQuotaDate(new Date());
					// 扣配额数量
					quotaReturn.setQuotaNum(0);
					// 配额模式
					quotaReturn
							.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);

					// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
					// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
					quotaReturn.setSign(1);
					// 当合同为空时,此时的配额状态反映为不可退的状态
					quotaReturn.setTakebackQuota(false);
					quotaReturnList.add(quotaReturn);
				}
			}
			return quotaReturnList;
		}

		// 如果配额模式－－进店,则只扣入住当天的配额,其他的都为零
		List<HtlRoom> htlRoomList = null;
		List<HtlPrice> htlPriceList = null;
		
		// 配额类型
		String quotaType = null;

		if (HotelBaseConstantBean.PAY.equals(quotaQuery.getPayMethod())) {
			quotaQuery.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
			quotaType = HotelBaseConstantBean.GENERALQUOTA;
			quotaQuery.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPEPAY);
		} else {
			quotaType = HotelBaseConstantBean.GENERALQUOTA;
			quotaQuery.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
			quotaQuery.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPEPREPAY);
		}
		String siQuotaType = quotaQuery.getQuotaType();
		String siQuotaShare = quotaQuery.getQuotaShare();

		if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(quotaPattern)) {
			htlRoomList = hotelRoomService.qryHtlRoomByRoomTypeSaleDateRange(quotaQuery.getRoomTypeId(), 
					quotaQuery.getBeginDate(), quotaQuery.getEndDate());
			
			//将房态整合进MAP
			Map<Integer, String> htlRoomMap = new HashMap<Integer, String>();
			if(htlRoomList==null){
				//进店模式只需要考虑第一天的房态
				htlRoomMap.put(0, "0");
			}else{
				int i = 0;
				for(Iterator<HtlRoom> itr = htlRoomList.iterator();itr.hasNext();){
					HtlRoom htlRoom = (HtlRoom)itr.next();
					htlRoomMap.put(i, htlRoom.getRoomState());
					i = i+1;
				}
			}
			
			/**
			 * 先读取普通配额的价格,让配额的扣退类型跟着价格录入的类型走
			 * 将价格整合进MAP
			 */
			htlPriceList = priceManage.getHtlPriceBySaleDateRangePriceType(quotaQuery.getBeginDate(), 
					quotaQuery.getEndDate(), quotaQuery.getChildRoomId(), quotaQuery.getPayMethod(), quotaType);
			Map<Integer, Double> htlPriceBasicMap = new HashMap<Integer, Double>();
			Map<Integer, Double> htlPriceSaleRoomMap = new HashMap<Integer, Double>();
			Map<Integer, Double> htlPriceSaleMap = new HashMap<Integer, Double>();
			
			if( htlPriceList == null || htlPriceList.size()<=0){
				/**
				 * 将价格整合进MAP
				 * 如普通配额的价格没有,就读取包房配额的价格
				 */
				quotaQuery.setQuotaType(HotelBaseConstantBean.CHARTERQUOTA);
				quotaType = HotelBaseConstantBean.CHARTERQUOTA;
				htlPriceList = priceManage.getHtlPriceBySaleDateRangePriceType(quotaQuery.getBeginDate(), 
						quotaQuery.getEndDate(), quotaQuery.getChildRoomId(), quotaQuery.getPayMethod(), quotaType);
				if(htlPriceList == null){
					htlPriceBasicMap.put(0, 0.0D);
					htlPriceSaleRoomMap.put(0, 0.0D);
					htlPriceSaleMap.put(0, 0.0D);
				}else{
					int i = 0;
					for(Iterator<HtlPrice> itr = htlPriceList.iterator();itr.hasNext();){
						HtlPrice htlPrice = (HtlPrice)itr.next();
						htlPriceBasicMap.put(i, htlPrice.getBasePrice());
						htlPriceSaleRoomMap.put(i, htlPrice.getSalesroomPrice());
						htlPriceSaleMap.put(i, htlPrice.getSalePrice());
						i++ ;
					}
				}
				
			}else{
				int i = 0;
				for(Iterator<HtlPrice> itr = htlPriceList.iterator();itr.hasNext();){
					HtlPrice htlPrice = (HtlPrice)itr.next();
					htlPriceBasicMap.put(i, htlPrice.getBasePrice());
					htlPriceSaleRoomMap.put(i, htlPrice.getSalesroomPrice());
					htlPriceSaleMap.put(i, htlPrice.getSalePrice());
					i++ ;
				}
			}

			int i=0;
			/**
			 *  当合作方为直联酒店时,不扣取配额
			 */
			Boolean isDirection = false;
			for(i=0;i<dayNum;i++){
				
				/**
				 * 当合作方为直联酒店时,不扣取配额
				 */
				if(quotaQuery.getChannel()!=null&&quotaQuery.getChannel()>0){
					isDirection = true;
					for (int j = 0; j < roomCout; j++) {
						QuotaReturn quotaReturn = new QuotaReturn();
						// 酒店id
						quotaReturn.setHotelId(quotaQuery.getHotelId());
						// 房型id
						quotaReturn.setRoomTypeId(quotaQuery.getRoomTypeId());
						// 价格类型
						quotaReturn.setChildRoomTypeId(quotaQuery.getChildRoomId());
						// 床型ID
						quotaReturn.setBedId(quotaQuery.getBedID());
						// 支付方式
						quotaReturn.setPayMethod(quotaQuery.getPayMethod());
						// 会员类型(TMC,CC,TP)
						quotaReturn.setMemberType(quotaQuery.getMemberType());
						// 被扣配额的所属日期
						quotaReturn.setQuotaDate(DateUtil.getDate(quotaQuery.getBeginDate(),i));
						// 扣配额日期
						quotaReturn.setUseQuotaDate(new Date());
						// 底价
						quotaReturn.setBasePrice((Double)htlPriceBasicMap.get(i));
						// 门市价
						quotaReturn.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(i));
						// 销售价
						quotaReturn.setSalePrice((Double)htlPriceSaleMap.get(i));
						// 房态
						quotaReturn.setRoomState((String)htlRoomMap.get(i));
						// 配额模式
						quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);
						// 设置配额类型
						quotaReturn.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
						// 设置配额共享模式
						quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE));
						// 扣配额数量
						quotaReturn.setQuotaNum(1);
						// 是否允许退
						quotaReturn.setTakebackQuota(false);
						// 0表示扣成功,1表示没成功
						quotaReturn.setSign(1);
						quotaReturnList.add(quotaReturn);
					}
				}
			}
			//重新排序
			if(isDirection==true){
				if(quotaReturnList!=null&&quotaReturnList.size()>0){
					for(int m=0;m<quotaReturnList.size();m++){
						QuotaReturn tmpQuotaReturn = new QuotaReturn();
						for(int n=m+1;n<quotaReturnList.size();n++){
							if(quotaReturnList.get(m).getQuotaDate().getTime()>
									quotaReturnList.get(n).getQuotaDate().getTime()){
								tmpQuotaReturn = quotaReturnList.get(m);
								quotaReturnList.set(m, quotaReturnList.get(n));
								quotaReturnList.set(n, tmpQuotaReturn);
							}
						}
					}
				}
				return quotaReturnList;
			}
			
			cutOffDayNum = DateUtil.getDay(new Date(), quotaQuery
					.getBeginDate());
			
			int isFreeSale = 0;
			String bedId = String.valueOf(quotaQuery.getBedID());
			for (int j = 0; j < roomCout; j++) {
				// 进店模式查询每一天的配额数量

				if(htlRoomList==null){
					fulfillCIQuota(dayNum, 1, quotaReturnList, quotaQuery, htlRoomMap, htlPriceBasicMap,htlPriceSaleRoomMap,htlPriceSaleMap,HotelBaseConstantBean.QUOTAPATTERNCI);
					return quotaReturnList;
				}				
				
				quotaQuery.setEndDate(DateUtil.getDate(quotaQuery.getBeginDate(), 1));
				Boolean blnDeduct = false;
				HtlQuotaCutoffDayNew cutOffDayNew = null;
				
				if(HotelBaseConstantBean.CHARTERQUOTA.equals(quotaQuery.getQuotaType())){
					/**
					 * 包房配额,如果已经扣面付共享/预付共享配额,
					 */
					quotaQuery.setQuotaType(HotelBaseConstantBean.CHARTERQUOTA);
					quotaQuery.setQuotaShare(siQuotaShare);
					cutOffDayNew = qryQuotaDao.getCutOffDayDetail(quotaQuery, cutOffDayNum);
					if (cutOffDayNew != null) {
						// 取得必须的配额信息
						HtlRoom htlRoom = new HtlRoom();
						htlRoom.setRoomState((String)htlRoomMap.get(0));
						HtlPrice htlPrice = new HtlPrice();
						htlPrice.setBasePrice((Double)htlPriceBasicMap.get(0));
						htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(0));
						htlPrice.setSalePrice((Double)htlPriceSaleMap.get(0));
						blnDeduct = quotaDetailControl(quotaReturnList,
							cutOffDayNew, quotaQuery,
						HotelBaseConstantBean.QUOTAPATTERNCI,htlRoom,htlPrice);
					}

					
					// 成功扣掉包房配额
					if (blnDeduct == true) {
						fulfillCIQuota(dayNum - 1, 1, quotaReturnList, quotaQuery,  htlRoomMap, htlPriceBasicMap,htlPriceSaleRoomMap,htlPriceSaleMap,HotelBaseConstantBean.QUOTAPATTERNCI);
						continue;
					}
	
					/**
					 * 包房配额,如成功扣除面付共享/预付共享配额后,依然不够扣，则扣面预付共享配额
					 */
					quotaQuery.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPE);
					cutOffDayNew = qryQuotaDao.getCutOffDayDetail(quotaQuery, cutOffDayNum);
					if (cutOffDayNew != null) {
						// 取得必须的配额信息
						HtlRoom htlRoom = new HtlRoom();
						htlRoom.setRoomState((String)htlRoomMap.get(0));
						HtlPrice htlPrice = new HtlPrice();
						htlPrice.setBasePrice((Double)htlPriceBasicMap.get(0));
						htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(0));
						htlPrice.setSalePrice((Double)htlPriceSaleMap.get(0));
						blnDeduct = quotaDetailControl(quotaReturnList,cutOffDayNew, quotaQuery,
								HotelBaseConstantBean.QUOTAPATTERNCI,htlRoom,htlPrice);
					}
					// 包房配额,成功扣掉面预付共享配额
					if (blnDeduct == true) {
						fulfillCIQuota(dayNum - 1, 1, quotaReturnList, quotaQuery,  htlRoomMap, htlPriceBasicMap,htlPriceSaleRoomMap,htlPriceSaleMap,HotelBaseConstantBean.QUOTAPATTERNCI);
						continue;
					}
				}else{
					//重新初始化成普通配额
					quotaQuery.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
					
					/**
					 * 普通配额,如果已经扣面付共享/预付共享配额,
					 */
					if(null!=htlRoomMap.get(0)){
						isFreeSale = ((String)htlRoomMap.get(0)).indexOf(bedId+":0");
					}
					if(isFreeSale<0){
						quotaQuery.setQuotaShare(siQuotaShare);
						cutOffDayNew = qryQuotaDao.getCutOffDayDetail(quotaQuery, cutOffDayNum);
						if (cutOffDayNew != null) {
							// 取得必须的配额信息
							HtlRoom htlRoom = new HtlRoom();
							htlRoom.setRoomState((String)htlRoomMap.get(0));
							HtlPrice htlPrice = new HtlPrice();
							htlPrice.setBasePrice((Double)htlPriceBasicMap.get(0));
							htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(0));
							htlPrice.setSalePrice((Double)htlPriceSaleMap.get(0));
							blnDeduct = quotaDetailControl(quotaReturnList,
									cutOffDayNew, quotaQuery,
									HotelBaseConstantBean.QUOTAPATTERNCI,htlRoom,htlPrice);
						}
					}else{
						QuotaReturn quotaReturn = new QuotaReturn();
						// 酒店id
						quotaReturn.setHotelId(quotaQuery.getHotelId());
						// 房型id
						quotaReturn.setRoomTypeId(quotaQuery.getRoomTypeId());
						// 价格类型
						quotaReturn.setChildRoomTypeId(quotaQuery.getChildRoomId());
						// 床型ID
						quotaReturn.setBedId(quotaQuery.getBedID());
						// 支付方式
						quotaReturn.setPayMethod(quotaQuery.getPayMethod());
						// 会员类型(TMC,CC,TP)
						quotaReturn.setMemberType(quotaQuery.getMemberType());
						// 被扣配额的所属日期
						quotaReturn.setQuotaDate(DateUtil.getDate(quotaQuery.getBeginDate(),0));
						// 扣配额日期
						quotaReturn.setUseQuotaDate(new Date());
						// 底价
						quotaReturn.setBasePrice((Double)htlPriceBasicMap.get(0));
						// 门市价
						quotaReturn.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(0));
						// 销售价
						quotaReturn.setSalePrice((Double)htlPriceSaleMap.get(0));
						// 房态
						quotaReturn.setRoomState((String)htlRoomMap.get(0));
						// 配额模式
						quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);
						// 设置配额类型
						quotaReturn.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
						// 设置配额共享模式
						quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE));
						// 扣配额数量
						quotaReturn.setQuotaNum(1);
						// 是否允许退
						quotaReturn.setTakebackQuota(false);
						// 0表示扣成功,1表示没成功
						quotaReturn.setSign(1);
						quotaReturnList.add(quotaReturn);
						blnDeduct = true;
					}
					
					// 成功扣掉普通配额
					if (blnDeduct == true) {
						fulfillCIQuota(dayNum - 1, 1, quotaReturnList, quotaQuery,  htlRoomMap, htlPriceBasicMap,htlPriceSaleRoomMap,htlPriceSaleMap,HotelBaseConstantBean.QUOTAPATTERNCI);
						continue;
					}
	
					/**
					 * 普通配额,如成功扣除面付共享/预付共享配额后,依然不够扣，则扣面预付共享配额
					 */
					quotaQuery.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPE);
					cutOffDayNew = qryQuotaDao.getCutOffDayDetail(quotaQuery, cutOffDayNum);
					if (cutOffDayNew != null) {
						// 取得必须的配额信息
						HtlRoom htlRoom = new HtlRoom();
						htlRoom.setRoomState((String)htlRoomMap.get(0));
						HtlPrice htlPrice = new HtlPrice();
						htlPrice.setBasePrice((Double)htlPriceBasicMap.get(0));
						htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(0));
						htlPrice.setSalePrice((Double)htlPriceSaleMap.get(0));
						blnDeduct = quotaDetailControl(quotaReturnList,cutOffDayNew, quotaQuery,
								HotelBaseConstantBean.QUOTAPATTERNCI,htlRoom,htlPrice);
					}
					// 成功扣掉面预付共享配额
					if (blnDeduct == true) {
						fulfillCIQuota(dayNum - 1, 1, quotaReturnList, quotaQuery,  htlRoomMap, htlPriceBasicMap,htlPriceSaleRoomMap,htlPriceSaleMap,HotelBaseConstantBean.QUOTAPATTERNCI);
						continue;
					}
				}
				/**
				 * 临时配额，如成功扣除面付共享/预付共享配额,面预付共享配额后,依然不够扣，则扣临时配额
				 */
				quotaQuery.setQuotaType(HotelBaseConstantBean.TEMPQUOTA);
				cutOffDayNew = qryQuotaDao.getCutOffDayTempDetail(quotaQuery);
				if (cutOffDayNew != null) {
					// 取得必须的配额信息
					HtlRoom htlRoom = new HtlRoom();
					htlRoom.setRoomState((String)htlRoomMap.get(0));
					HtlPrice htlPrice = new HtlPrice();
					htlPrice.setBasePrice((Double)htlPriceBasicMap.get(0));
					htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(0));
					htlPrice.setSalePrice((Double)htlPriceSaleMap.get(0));
					blnDeduct = quotaDetailControl(quotaReturnList,cutOffDayNew, quotaQuery,
							HotelBaseConstantBean.QUOTAPATTERNCI,htlRoom,htlPrice);
				}
				// 成功扣掉面预付共享配额
				if (blnDeduct == true) {
					fulfillCIQuota(dayNum - 1, 1, quotaReturnList, quotaQuery,  htlRoomMap, htlPriceBasicMap,htlPriceSaleRoomMap,htlPriceSaleMap,HotelBaseConstantBean.QUOTAPATTERNCI);
					continue;
				}

				/**
				 * 呼出配额，如成功扣除面付共享/预付共享配额,面预付共享配额,临时配额后,依然不够扣，则扣呼出配额
				 */
				HtlQuotaJudge htlQuotaJudge = new HtlQuotaJudge();
				htlQuotaJudge.setHotelId(quotaQuery.getHotelId());
				htlQuotaJudge.setRoomtypeId(quotaQuery.getRoomTypeId());
				htlQuotaJudge.setBedId(quotaQuery.getBedID());
				htlQuotaJudge.setCutoffday(0L);
				htlQuotaJudge.setCutofftime("20:10");
				htlQuotaJudge.setEndDate(DateUtil.getDate(quotaQuery.getBeginDate()));
				htlQuotaJudge.setStartDate(DateUtil.getDate(quotaQuery.getBeginDate()));
				htlQuotaJudge.setQuotaChannel("CC");
				if (quotaQuery.getMemberType() == 1) {
					htlQuotaJudge.setQuotaHolder("CC");
				} else if (quotaQuery.getMemberType() == 2) {
					htlQuotaJudge.setQuotaHolder("TP");
				} else if (quotaQuery.getMemberType() == 3) {
					htlQuotaJudge.setQuotaHolder("TMC");
				}
				if(quotaQuery.getOperatorDept()!=null){
					quotaQuery.setOperatorDept(quotaQuery.getOperatorDept());
				}
				if(quotaQuery.getOperatorId()!=null){
					quotaQuery.setOperatorId(quotaQuery.getOperatorId());
				}
				if(quotaQuery.getOperatorName()!=null){
					quotaQuery.setOperatorName(quotaQuery.getOperatorName());
				}
				htlQuotaJudge.setOperatorTime(new Date());
				htlQuotaJudge.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);
				htlQuotaJudge.setQuotaType(HotelBaseConstantBean.OUTSIDEQUOTA);
				htlQuotaJudge.setQuotaShare(siQuotaShare);
				htlQuotaJudge.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_ADD);
				htlQuotaJudge.setQuotaNum(1L);
				//设置为可退类型
				htlQuotaJudge.setBlnBack(1L);
				//添加星期
				htlQuotaJudge.setJudgeWeeks("1,2,3,4,5,6,7");
				try {
					quotaManage.sateQuotaJudge(htlQuotaJudge);
				} catch (Exception ex) {
					log.error("quotaManage.sateQuotaJudge exception ----!! ",ex);
				}
				try {
					quotaManage.saveQuotaJudgeNew(htlQuotaJudge);
				} catch (Exception ex) {
					log.error("quotaManage.saveQuotaJudgeNew exception ----!! ",ex);
				}

				QuotaReturn quotaReturn = new QuotaReturn();
				// 酒店id
				quotaReturn.setHotelId(quotaQuery.getHotelId());
				// 房型id
				quotaReturn.setRoomTypeId(quotaQuery.getRoomTypeId());
				// 价格类型
				quotaReturn.setChildRoomTypeId(quotaQuery.getChildRoomId());
				// 床型ID
				quotaReturn.setBedId(quotaQuery.getBedID());
				// 支付方式
				quotaReturn.setPayMethod(quotaQuery.getPayMethod());
				// 会员类型(TMC,CC,TP)
				quotaReturn.setMemberType(quotaQuery.getMemberType());
				// 被扣配额的所属日期
				quotaReturn.setQuotaDate(quotaQuery.getBeginDate());
				// 扣配额日期
				quotaReturn.setUseQuotaDate(new Date());
				// 配额模式
				quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);
				// 设置配额类型
				quotaReturn.setQuotaType(HotelBaseConstantBean.OUTSIDEQUOTA);
				// 设置配额共享模式
				quotaReturn.setQuotaShare(
						new Long(HotelBaseConstantBean.QUOTASHARETYPE));
				// 扣配额数量
				quotaReturn.setQuotaNum(1);
				// 配额可退

				// 底价
				quotaReturn.setBasePrice((Double)htlPriceBasicMap.get(0));
				// 门市价
				quotaReturn.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(0));
				// 销售价
				quotaReturn.setSalePrice((Double)htlPriceSaleMap.get(0));
				// 房态
				quotaReturn.setRoomState((String)htlRoomMap.get(0));
				// 0表示成功,1表示失败
				quotaReturn.setSign(1);
				quotaReturn.setTakebackQuota(false);
				quotaReturnList.add(quotaReturn);
				
				quotaQuery.setQuotaType(HotelBaseConstantBean.OUTSIDEQUOTA);
				quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE));
				
				fulfillCIQuota(dayNum - 1, 1, quotaReturnList, quotaQuery,  htlRoomMap, htlPriceBasicMap,htlPriceSaleRoomMap,htlPriceSaleMap,HotelBaseConstantBean.QUOTAPATTERNCI);
			}
			
			if(quotaReturnList!=null&&quotaReturnList.size()>0){
				for(int m=0;m<quotaReturnList.size();m++){
					QuotaReturn tmpQuotaReturn = new QuotaReturn();
					for(int n=m+1;n<quotaReturnList.size();n++){
						if(quotaReturnList.get(m).getQuotaDate().getTime()>
								quotaReturnList.get(n).getQuotaDate().getTime()){
							tmpQuotaReturn = quotaReturnList.get(m);
							quotaReturnList.set(m, quotaReturnList.get(n));
							quotaReturnList.set(n, tmpQuotaReturn);
						}
					}
				}
			}
			return quotaReturnList;
		}

		// 如果配额模式－－在店,则扣除每天的配额
		if (HotelBaseConstantBean.QUOTAPATTERNSI.equals(quotaPattern)) {
			// 在店模式只查询第一天的配额数量
			Date startDate = quotaQuery.getBeginDate();
			Date endDate = quotaQuery.getEndDate();

			htlRoomList = hotelRoomService.qryHtlRoomByRoomTypeSaleDateRange(quotaQuery.getRoomTypeId(), 
					quotaQuery.getBeginDate(), quotaQuery.getEndDate());
			
			//将房态整合进MAP
			Map<Integer, String> htlRoomMap = new HashMap<Integer, String>();
			if(htlRoomList==null){
				//进店模式只需要考虑第一天的房态
				htlRoomMap.put(0, "0");
			}else{
				int i = 0;
				for(Iterator<HtlRoom> itr = htlRoomList.iterator();itr.hasNext();){
					HtlRoom htlRoom = (HtlRoom)itr.next();
					htlRoomMap.put(i, htlRoom.getRoomState());
					i = i+1;
				}
			}
			
			//将价格整合进MAP
			htlPriceList = priceManage.getHtlPriceBySaleDateRangePriceType(quotaQuery.getBeginDate(), 
					quotaQuery.getEndDate(), quotaQuery.getChildRoomId(), quotaQuery.getPayMethod(), quotaType);
			Map<Integer, Double> htlPriceBasicMap = new HashMap<Integer, Double>();
			Map<Integer, Double> htlPriceSaleRoomMap = new HashMap<Integer, Double>();
			Map<Integer, Double> htlPriceSaleMap = new HashMap<Integer, Double>();
			if(null==htlPriceList || htlPriceList.size()<=0){
				/**
				 * 将价格整合进MAP
				 * 如普通配额的价格没有,就读取包房配额的价格
				 */
				quotaQuery.setQuotaType(HotelBaseConstantBean.CHARTERQUOTA);
				quotaType = HotelBaseConstantBean.CHARTERQUOTA;
				htlPriceList = priceManage.getHtlPriceBySaleDateRangePriceType(quotaQuery.getBeginDate(), 
						quotaQuery.getEndDate(), quotaQuery.getChildRoomId(), quotaQuery.getPayMethod(), quotaType);
				if(htlPriceList == null){
					htlPriceBasicMap.put(0, 0.0D);
					htlPriceSaleRoomMap.put(0, 0.0D);
					htlPriceSaleMap.put(0, 0.0D);
				}else{
					int i = 0;
					for(Iterator<HtlPrice> itr = htlPriceList.iterator();itr.hasNext();){
						HtlPrice htlPrice = (HtlPrice)itr.next();
						htlPriceBasicMap.put(i, htlPrice.getBasePrice());
						htlPriceSaleRoomMap.put(i, htlPrice.getSalesroomPrice());
						htlPriceSaleMap.put(i, htlPrice.getSalePrice());
						i++ ;
					}
				}
			}else{
				int i = 0;
				for(Iterator<HtlPrice> itr = htlPriceList.iterator();itr.hasNext();){
					HtlPrice htlPrice = (HtlPrice)itr.next();
					htlPriceBasicMap.put(i, htlPrice.getBasePrice());
					htlPriceSaleRoomMap.put(i, htlPrice.getSalesroomPrice());
					htlPriceSaleMap.put(i, htlPrice.getSalePrice());
					i++;
				}
			}
			
			siQuotaType = quotaQuery.getQuotaType();
			for (int i = 0; i < dayNum; i++) {
				
				quotaQuery.setBeginDate(DateUtil.getDate(startDate, i));
				quotaQuery.setEndDate(DateUtil.getDate(startDate, i + 1));
				
				// 如果房态为空或者不存在,直接填充数据
				if(htlRoomList==null){
					fulfillCIQuota(dayNum, 1, quotaReturnList, quotaQuery, htlRoomMap, htlPriceBasicMap,htlPriceSaleRoomMap,htlPriceSaleMap,HotelBaseConstantBean.QUOTAPATTERNSI);
					return quotaReturnList;
				}
				// 读取价格
				int index = 0;
				if(null!=htlRoomMap.get(i)){
					/**
					 * 检查配额是否为FreeSalse状态  bedid:0
					 */
					index = ((String)htlRoomMap.get(i)).indexOf(String.valueOf(quotaQuery.getBedID())+":0");
				}
				/**
				 * 当配额为FreeSalse状态,该配额设置成可普通配额,并且不可退
				 */
				if(index>=0||(quotaQuery.getChannel()!=null&&quotaQuery.getChannel()>0)){
					for (int j = 0; j < roomCout; j++) {
						QuotaReturn quotaReturn = new QuotaReturn();
						// 酒店id
						quotaReturn.setHotelId(quotaQuery.getHotelId());
						// 房型id
						quotaReturn.setRoomTypeId(quotaQuery.getRoomTypeId());
						// 价格类型
						quotaReturn.setChildRoomTypeId(quotaQuery.getChildRoomId());
						// 床型ID
						quotaReturn.setBedId(quotaQuery.getBedID());
						// 支付方式
						quotaReturn.setPayMethod(quotaQuery.getPayMethod());
						// 会员类型(TMC,CC,TP)
						quotaReturn.setMemberType(quotaQuery.getMemberType());
						// 被扣配额的所属日期
						quotaReturn.setQuotaDate(DateUtil.getDate(startDate,i));
						// 扣配额日期
						quotaReturn.setUseQuotaDate(new Date());
						// 底价
						quotaReturn.setBasePrice((Double)htlPriceBasicMap.get(i));
						// 门市价
						quotaReturn.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(i));
						// 销售价
						quotaReturn.setSalePrice((Double)htlPriceSaleMap.get(i));
						// 房态
						quotaReturn.setRoomState((String)htlRoomMap.get(i));
						// 配额模式
						quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNSI);
						// 设置配额类型
						quotaReturn.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
						// 设置配额共享模式
						quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE));
						// 扣配额数量
						quotaReturn.setQuotaNum(1);
						// 是否允许退
						quotaReturn.setTakebackQuota(false);
						// 0表示扣成功,1表示没成功
						quotaReturn.setSign(1);
						quotaReturnList.add(quotaReturn);
					}
					continue;
				}
				
				cutOffDayNum = DateUtil.getDay(new Date(), quotaQuery
						.getBeginDate());
				
				
				for (int j = 0; j < roomCout; j++) {
					
					quotaQuery.setQuotaType(siQuotaType);
					quotaQuery.setQuotaShare(siQuotaShare);
					HtlQuotaCutoffDayNew cutOffDayNew = null;
					Boolean blnDeduct = false;
					
					if(HotelBaseConstantBean.CHARTERQUOTA.equals(quotaQuery.getQuotaType())){
						cutOffDayNew = qryQuotaDao.getCutOffDayDetail(quotaQuery, cutOffDayNum);
						/**
						 * 包房配额,如果已经扣面付共享/预付共享配额,
						 */
						blnDeduct = false;
						if ( cutOffDayNew!= null ) {
							// 取得必须的配额信息
							HtlRoom htlRoom = new HtlRoom();
							htlRoom.setRoomState((String)htlRoomMap.get(i));
							HtlPrice htlPrice = new HtlPrice();
							htlPrice.setBasePrice((Double)htlPriceBasicMap.get(i));
							htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(i));
							htlPrice.setSalePrice((Double)htlPriceSaleMap.get(i));
							blnDeduct = quotaDetailControl(quotaReturnList,cutOffDayNew, quotaQuery,
									HotelBaseConstantBean.QUOTAPATTERNSI,htlRoom,htlPrice);
						}
						// 包房配额,成功扣掉普通配额
						if (blnDeduct == true) {
							continue;
						}
	
						/**
						 * 包房配额,如成功扣除面付共享/预付共享配额后,依然不够扣，则扣面预付共享配额
						 */
						quotaQuery.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPE);
						cutOffDayNew = qryQuotaDao.getCutOffDayDetail(quotaQuery,cutOffDayNum);
						if (cutOffDayNew != null) {
							// 取得必须的配额信息
							HtlRoom htlRoom = new HtlRoom();
							htlRoom.setRoomState((String)htlRoomMap.get(i));
							HtlPrice htlPrice = new HtlPrice();
							htlPrice.setBasePrice((Double)htlPriceBasicMap.get(i));
							htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(i));
							htlPrice.setSalePrice((Double)htlPriceSaleMap.get(i));
							blnDeduct = quotaDetailControl(quotaReturnList,
									cutOffDayNew, quotaQuery,
									HotelBaseConstantBean.QUOTAPATTERNSI,htlRoom,htlPrice);
						}
						// 包房配额,成功扣掉面预付共享配额
						if (blnDeduct == true) {
							continue;
						}
					}else{
						/**
						 * 初始化成普通配额
						 */
						quotaQuery.setQuotaType(HotelBaseConstantBean.GENERALQUOTA);
						
						cutOffDayNew = qryQuotaDao.getCutOffDayDetail(quotaQuery, cutOffDayNum);
						/**
						 * 普通配额,如果已经扣面付共享/预付共享配额,
						 */
						blnDeduct = false;
						if ( cutOffDayNew!= null ) {
							// 取得必须的配额信息
							HtlRoom htlRoom = new HtlRoom();
							htlRoom.setRoomState((String)htlRoomMap.get(i));
							HtlPrice htlPrice = new HtlPrice();
							htlPrice.setBasePrice((Double)htlPriceBasicMap.get(i));
							htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(i));
							htlPrice.setSalePrice((Double)htlPriceSaleMap.get(i));
							blnDeduct = quotaDetailControl(quotaReturnList,cutOffDayNew, quotaQuery,
									HotelBaseConstantBean.QUOTAPATTERNSI,htlRoom,htlPrice);
						}
						// 成功扣掉普通配额
						if (blnDeduct == true) {
							continue;
						}
	
						/**
						 * 普通配额,如成功扣除面付共享/预付共享配额后,依然不够扣，则扣面预付共享配额
						 */
						quotaQuery.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPE);
						cutOffDayNew = qryQuotaDao.getCutOffDayDetail(quotaQuery,cutOffDayNum);
						if (cutOffDayNew != null) {
							// 取得必须的配额信息
							HtlRoom htlRoom = new HtlRoom();
							htlRoom.setRoomState((String)htlRoomMap.get(i));
							HtlPrice htlPrice = new HtlPrice();
							htlPrice.setBasePrice((Double)htlPriceBasicMap.get(i));
							htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(i));
							htlPrice.setSalePrice((Double)htlPriceSaleMap.get(i));
							blnDeduct = quotaDetailControl(quotaReturnList,
									cutOffDayNew, quotaQuery,
									HotelBaseConstantBean.QUOTAPATTERNSI,htlRoom,htlPrice);
						}
						// 成功扣掉面预付共享配额
						if (blnDeduct == true) {
							continue;
						}
					}
					
					/**
					 * 临时配额,如成功扣除面付共享/预付共享配额,面预付共享配额后,依然不够扣，则扣临时配额
					 */
					quotaQuery.setQuotaType(HotelBaseConstantBean.TEMPQUOTA);
					quotaQuery.setQuotaShare(HotelBaseConstantBean.QUOTASHARETYPE);
					cutOffDayNew = qryQuotaDao.getCutOffDayTempDetail(quotaQuery);
					if (cutOffDayNew != null) {
						// 取得必须的配额信息
						HtlRoom htlRoom = new HtlRoom();
						htlRoom.setRoomState((String)htlRoomMap.get(i));
						HtlPrice htlPrice = new HtlPrice();
						htlPrice.setBasePrice((Double)htlPriceBasicMap.get(i));
						htlPrice.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(i));
						htlPrice.setSalePrice((Double)htlPriceSaleMap.get(i));
						blnDeduct = quotaDetailControl(quotaReturnList,
								cutOffDayNew, quotaQuery,
								HotelBaseConstantBean.QUOTAPATTERNSI,htlRoom,htlPrice);
					}
					// 成功扣掉面预付共享配额
					if (blnDeduct == true) {
						continue;
					}

					/**
					 * 呼出配额,如成功扣除面付共享/预付共享配额,面预付共享配额,临时配额后,依然不够扣，则扣呼出配额
					 */
					HtlQuotaJudge htlQuotaJudge = new HtlQuotaJudge();
					htlQuotaJudge.setHotelId(quotaQuery.getHotelId());
					htlQuotaJudge.setRoomtypeId(quotaQuery.getRoomTypeId());
					htlQuotaJudge.setBedId(quotaQuery.getBedID());
					htlQuotaJudge.setCutoffday(0L);
					htlQuotaJudge.setCutofftime("22:10");
					htlQuotaJudge.setEndDate(DateUtil.getDate(quotaQuery.getBeginDate()));
					htlQuotaJudge.setStartDate(DateUtil.getDate(quotaQuery.getBeginDate()));
					htlQuotaJudge.setQuotaChannel("CC");
					htlQuotaJudge.setJudgeType("ADD");
					if (quotaQuery.getMemberType() == 1) {
						htlQuotaJudge.setQuotaHolder("CC");
					} else if (quotaQuery.getMemberType() == 2) {
						htlQuotaJudge.setQuotaHolder("TP");
					} else if (quotaQuery.getMemberType() == 3) {
						htlQuotaJudge.setQuotaHolder("TMC");
					}
					if(quotaQuery.getOperatorDept()!=null){
						htlQuotaJudge.setOperatorDept(quotaQuery.getOperatorDept());
					}
					if(quotaQuery.getOperatorId()!=null){
						htlQuotaJudge.setOperatorId(quotaQuery.getOperatorId());
					}
					if(quotaQuery.getOperatorName()!=null){
						htlQuotaJudge.setOperatorName(quotaQuery.getOperatorName());
					}
					htlQuotaJudge.setOperatorName("CC");
					htlQuotaJudge.setOperatorTime(new Date());
					htlQuotaJudge.setQuotaNum(Long.valueOf(quotaQuery
							.getQuotaNum()));
					htlQuotaJudge.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNSI);
					htlQuotaJudge.setQuotaType(HotelBaseConstantBean.OUTSIDEQUOTA);
					htlQuotaJudge.setQuotaShare(siQuotaShare);
					htlQuotaJudge.setJudgeType(HotelBaseConstantBean.JUDGE_TYPE_ADD);
					htlQuotaJudge.setQuotaNum(1L);
					//设置为可退类型
					htlQuotaJudge.setBlnBack(1L);
					//添加星期
					htlQuotaJudge.setJudgeWeeks("1,2,3,4,5,6,7");
					try {
						quotaManage.sateQuotaJudge(htlQuotaJudge);
					} catch (Exception ex) {
						log.error("quotaManage.sateQuotaJudge error ----!! ",ex);
					}
					try {
						quotaManage.saveQuotaJudgeNew(htlQuotaJudge);
					} catch (Exception ex) {
						log.error("quotaManage.saveQuotaJudgeNew exception ----!! ",ex);
					}

					QuotaReturn quotaReturn = new QuotaReturn();
					// 酒店id
					quotaReturn.setHotelId(quotaQuery.getHotelId());
					// 房型id
					quotaReturn.setRoomTypeId(quotaQuery.getRoomTypeId());
					// 价格类型
					quotaReturn.setChildRoomTypeId(quotaQuery.getChildRoomId());
					// 床型ID
					quotaReturn.setBedId(quotaQuery.getBedID());
					// 支付方式
					quotaReturn.setPayMethod(quotaQuery.getPayMethod());
					// 会员类型(TMC,CC,TP)
					quotaReturn.setMemberType(quotaQuery.getMemberType());
					// 被扣配额的所属日期
					quotaReturn.setQuotaDate(quotaQuery.getBeginDate());
					// 扣配额日期
					quotaReturn.setUseQuotaDate(new Date());
					// 底价
					quotaReturn.setBasePrice((Double)htlPriceBasicMap.get(i));
					// 门市价
					quotaReturn.setSalesroomPrice((Double)htlPriceSaleRoomMap.get(i));
					// 销售价
					quotaReturn.setSalePrice((Double)htlPriceSaleMap.get(i));
					// 房态
					quotaReturn.setRoomState((String)htlRoomMap.get(i));
					
					// 配额模式
					quotaReturn.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNSI);
					// 设置配额类型
					quotaReturn.setQuotaType(HotelBaseConstantBean.OUTSIDEQUOTA);
					// 设置配额共享模式
					quotaReturn.setQuotaShare(new Long(HotelBaseConstantBean.QUOTASHARETYPE)
							);
					// 扣配额数量
					quotaReturn.setQuotaNum(1);
					// 是否允许退
					quotaReturn.setTakebackQuota(false);
					// 0表示扣成功,1表示没成功
					quotaReturn.setSign(1);
					quotaReturnList.add(quotaReturn);

					if (blnDeduct == true) {
						continue;
					}
				}
			}
			return quotaReturnList;
		}
		return quotaReturnList;

	}
	
	/**
	 * 退配额方法
	 * 
	 * @param List
	 *            <QuotaReturn> quotaReturnList
	 *
	 */
	public Boolean returnQuota(List<QuotaReturn> quotaReturnList) {

        /**
         * 增加配额的床型是否共享
         * add by guojun 2010-1-25 17:50
         */
		Boolean isQuotaBedShare = false;
		if(quotaReturnList!=null&&quotaReturnList.size()>0){
			
	        HtlRoomtype HtlRoomtype = hotelManage.findHtlRoomtype(quotaReturnList.get(0).getRoomTypeId());
	        
	        if(HtlRoomtype!=null){
	        	if(HtlRoomtype.getQuotaBedShare()>0){
	        		isQuotaBedShare = true;
	        		/**
	        		 * 配额共享就扣双床的配额
	        		 */
	        	}
	        }
		}
        
		for (QuotaReturn quotaReturn : quotaReturnList) {

			HtlQuotaCutoffDayNew cutOffDayNew = null;
			
			if(quotaReturn.getSign()==1){
				continue;
			}
			
			if(quotaReturn.getQuotaDate()!=null){
				if(quotaReturn.getQuotaDate().getTime()<=
						DateUtil.getDate("2009-10-27").getTime()){
					continue;
				}
			}
			
			/**
			 * 如果是床型相同,配额共享那就只能是退双床的配额
			 */
			if(isQuotaBedShare==true){
				quotaReturn.setBedId(2L);
			}
			
			log.info("  ---quotaReturn.getHotelId():"+quotaReturn.getHotelId());
			log.info("  ---quotaReturn.getRoomTypeId():"+quotaReturn.getRoomTypeId());
			log.info("  ---quotaReturn.getBedId():"+quotaReturn.getBedId());
			log.info("  ---quotaReturn.getQuotaDate():"+DateUtil.dateToString(quotaReturn.getQuotaDate()));
			log.info("  ---quotaReturn.getQuotaShare():"+quotaReturn.getQuotaShare()!=null?String.valueOf(quotaReturn.getQuotaShare()):"0");
			/**
			 * 如果是临时配额,则读取临时配额类型
			 */
			if (HotelBaseConstantBean.TEMPQUOTA.equals(quotaReturn
					.getQuotaType())) {
				cutOffDayNew = qryQuotaDao.getQuotaReturnTempDetail(quotaReturn);
			} else {
				/**
				 * 如果是普通配额或者包房配额,则读取普通配额类型或者包房配额
				 */
				cutOffDayNew = qryQuotaDao.getQuotaReturnDetail(quotaReturn);
			}
			if (cutOffDayNew != null) {
				log.info(" quota used:"+cutOffDayNew.getQuotaUsed());
				// 如果当前配额的使用数大于当前退的配额数
				if(cutOffDayNew.getQuotaUsed()>0){
					if (cutOffDayNew.getQuotaUsed() >= quotaReturn.getQuotaNum()) {
						// 在现有可用配额的基础上新增可用配额数
						cutOffDayNew.setQuotaAvailable(cutOffDayNew.getQuotaAvailable()
								+ 1);
						// 减掉已经使用的配额数
						cutOffDayNew.setQuotaUsed(cutOffDayNew.getQuotaUsed() - 1);
	
					} else {
	
						// 在现有可用配额的基础上新增可用配额数
						cutOffDayNew.setQuotaAvailable(cutOffDayNew.getQuotaAvailable() + 1);
						// 减掉已经使用的配额数
						cutOffDayNew.setQuotaUsed(cutOffDayNew.getQuotaUsed() - 1);
	
						
					}
				}
			}
			if(cutOffDayNew!=null){
				qryQuotaDao.updateAvailableQuotaAndUsedQuota(cutOffDayNew);
				saveReturnQuotaLog(quotaReturn,cutOffDayNew);
				qryQuotaDao.updateCutOffDayNew(cutOffDayNew.getID().longValue());
			}
		}
		return true;
	}

	/**
	 * 配额详情操作,用于扣某一天的配额,
	 * 
	 * @param quotaReturnList
	 *            返回的配额列表
	 * @param cutOffDayList
	 *            配额详情列表
	 * @param quotaQuery
	 *            配额查询情况
	 * @return
	 */
	private Boolean quotaDetailControl(List<QuotaReturn> quotaReturnList,
			HtlQuotaCutoffDayNew cutOffDayNew, QuotaQuery quotaQuery,
			String quotaPattern,HtlRoom htlRoom,HtlPrice htlPrice) {
		Boolean blnDeduct = false;

		QuotaReturn quotaReturn = new QuotaReturn();
		if (cutOffDayNew!=null) {
			// 如果配额明细可用数大于零,并且配额明细可用数大于房间数
			if (cutOffDayNew.getQuotaAvailable() > 0) {
				// 酒店id
				quotaReturn.setHotelId(quotaQuery.getHotelId());
				// 房型id
				quotaReturn.setRoomTypeId(quotaQuery.getRoomTypeId());
				// 价格类型
				quotaReturn.setChildRoomTypeId(quotaQuery.getChildRoomId());
				// 床型ID
				quotaReturn.setBedId(quotaQuery.getBedID());
				// 支付方式
				quotaReturn.setPayMethod(quotaQuery.getPayMethod());
				// 会员类型(TMC,CC,TP)
				quotaReturn.setMemberType(quotaQuery.getMemberType());
				// 被扣配额的所属日期
				quotaReturn.setQuotaDate(quotaQuery.getBeginDate());
				// 扣配额日期
				quotaReturn.setUseQuotaDate(new Date());
				// 设置配额的共享模式
				quotaReturn.setQuotaType(cutOffDayNew.getQuotaType());
				// 配额模式
				quotaReturn.setQuotaPattern(quotaPattern);
				// 底价
				quotaReturn.setBasePrice(null != htlPrice?
						htlPrice.getBasePrice() : 0.0);
				// 门市价
				quotaReturn.setSalesroomPrice(null != htlPrice?
						htlPrice.getSalesroomPrice() : 0.0);
				// 销售价
				quotaReturn.setSalePrice(null != htlPrice ? 
						htlPrice.getSalePrice() : 0.0);
				// 房态
				quotaReturn.setRoomState(htlRoom.getRoomState());
				// 设置配额类型
				quotaReturn.setQuotaShare(new Long(cutOffDayNew.getQuotaShare())
						);
				// 当前配额是否可退,如果BlnBack为1表示可退,否则为不可退
				if (cutOffDayNew.getBlnBack() == 1) {
					quotaReturn.setTakebackQuota(true);
					// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
					// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)

					quotaReturn.setSign(0);
				} else {
					quotaReturn.setTakebackQuota(false);
					// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
					// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
					quotaReturn.setSign(1);
				}

				// 扣配额数量
				quotaReturn.setQuotaNum(1);
				
				/**
				 * 配额明细表改动,剩余的配额明细可用数
				 */
				cutOffDayNew.setQuotaAvailable(cutOffDayNew.getQuotaAvailable() - 1);
				
				/**
				 * 配额明细表改动,配额已用数量调整
				 */
				cutOffDayNew.setQuotaUsed(cutOffDayNew.getQuotaUsed() + 1);

				blnDeduct = true;
				
				quotaReturnList.add(quotaReturn);
			}
		}

		if(cutOffDayNew!=null&&blnDeduct==true){
			qryQuotaDao.updateAvailableQuotaAndUsedQuota(cutOffDayNew);
			saveDeductQuotaLog(quotaReturn,cutOffDayNew,quotaPattern);
			qryQuotaDao.updateCutOffDayNew(cutOffDayNew.getID().longValue());
		}
		return blnDeduct;
	}

	/**
	 * 填充进店模式的配额信息,将首日配额以外的配额信息填充进去
	 * 
	 * @param dayNum
	 * @param roomNum
	 * @param quotaDetailList
	 */
	private void fulfillCIQuota(int dayNum, int roomNum,
			List<QuotaReturn> quotaReturnList, QuotaQuery quotaQuery,Map htlRoomMap,Map basicPriceMap,Map saleRoomMap,Map saleMap, String quotaPattern) {

		for (int i = 1; i < dayNum+1; i++) {
			for (int j = 0; j < roomNum; j++) {
				QuotaReturn quotaReturn = new QuotaReturn();
				// C-1进店模式，填写下面参数
				// 酒店id
				quotaReturn.setHotelId(quotaQuery.getHotelId());
				// 房型id
				quotaReturn.setRoomTypeId(quotaQuery.getRoomTypeId());
				// 价格类型
				quotaReturn.setChildRoomTypeId(quotaQuery.getChildRoomId());
				// 床型ID
				quotaReturn.setBedId(quotaQuery.getBedID());
				// 支付方式
				quotaReturn.setPayMethod(quotaQuery.getPayMethod());
				// 会员类型(TMC,CC,TP)
				quotaReturn.setMemberType(quotaQuery.getMemberType());
				// 被扣配额的所属日期
				quotaReturn.setQuotaDate(DateUtil.getDate(quotaQuery.getBeginDate(), i));
				// 底价
				quotaReturn.setBasePrice((Double)basicPriceMap.get(i));
				// 门市价
				quotaReturn.setSalesroomPrice((Double)saleRoomMap.get(i));
				// 销售价
				quotaReturn.setSalePrice((Double)saleMap.get(i));
				// 配额类型
				quotaReturn.setQuotaType(quotaQuery.getQuotaType());
				// 配额类型
				quotaReturn.setQuotaPattern(quotaPattern);
				// 配额共享方式
				if(quotaQuery.getQuotaShare()==null){
					quotaReturn.setQuotaShare(3L);
				}else{
					quotaReturn.setQuotaShare(Long.valueOf(quotaQuery.getQuotaShare()));
				}
				// 房态
				quotaReturn.setRoomState((String)htlRoomMap.get(i));
				// 扣配额日期
				quotaReturn.setUseQuotaDate(new Date());
				// 扣配额数量
				quotaReturn.setQuotaNum(1);
				// 配额是否可退
				quotaReturn.setTakebackQuota(false);
				// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
				// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
				quotaReturn.setSign(1);

				quotaReturnList.add(quotaReturn);

			}
		}
	}

	/**
	 * 扣配额的LOG日志
	 * @param quotaQuery
	 * @param cutOffDayNew
	 */
	private void saveDeductQuotaLog(QuotaReturn quotaReturn,HtlQuotaCutoffDayNew cutOffDayNew,String quotaPattern){
		HtlQuotaLog htlQuotaLog = new HtlQuotaLog();
		// 配额的修改时间
		htlQuotaLog.setAbleDate(quotaReturn.getUseQuotaDate());
		htlQuotaLog.setBedId(quotaReturn.getBedId());
		// CUTOFFDAY提前天数
		htlQuotaLog.setCutoffday(cutOffDayNew.getCutoffday());
		// 提前时间
		htlQuotaLog.setCutofftime(cutOffDayNew.getCutofftime());
		htlQuotaLog.setHotelId(cutOffDayNew.getHotelId());
		htlQuotaLog.setHtlQuotaDetailId(cutOffDayNew.getID());
		htlQuotaLog.setHtlQuotaId(cutOffDayNew.getQuotaId());
		// 配额调整
		htlQuotaLog.setJudgeType(HotelBaseConstantBean.DEDUCT_QUOTA);
		htlQuotaLog.setRoomtypeId(cutOffDayNew.getRoomtypeId());
		htlQuotaLog.setOperatorDept("CC");
		htlQuotaLog.setOperatorId("CC");
		htlQuotaLog.setOperatorName("CC ORDER");
		htlQuotaLog.setOperatorOrigin("CC");
		htlQuotaLog.setOperatorTime(new Date());
		// 当前可用数
		htlQuotaLog.setQuotaAvailable(String.valueOf(cutOffDayNew.getQuotaAvailable()));
		// 配额改变数,此时的改变数为需要退掉的配额数
		htlQuotaLog.setQuotaChange(Long.valueOf(quotaReturn.getQuotaNum()));
		// 配额持有者
		htlQuotaLog.setQuotaHolder(cutOffDayNew.getQuotaHolder());
		// 配额总数
		htlQuotaLog.setQuotaNum(cutOffDayNew.getQuotaNum());
		// C-I只做进店模式
		htlQuotaLog.setQuotaPattern(quotaPattern);
		// 配额共享模式
		htlQuotaLog.setQuotaShare(cutOffDayNew.getQuotaShare());
		// 配额类型
		htlQuotaLog.setQuotaType(cutOffDayNew.getQuotaType());
		// 配额使用数
		htlQuotaLog.setQuotaUsed(String.valueOf(cutOffDayNew.getQuotaUsed()));
		
		// 将变化的配额记录到配额变化表中
		qryQuotaDao.saveQuotaLog(htlQuotaLog);
	}
	
	/**
	 * 退配额的LOG日志
	 * 
	 * @param quotaReturn
	 * @param cutOffDayNew
	 */
	private void saveReturnQuotaLog(QuotaReturn quotaReturn,HtlQuotaCutoffDayNew cutOffDayNew){
		HtlQuotaLog htlQuotaLog = new HtlQuotaLog();
		// 配额的修改时间
		htlQuotaLog.setAbleDate(quotaReturn.getUseQuotaDate());
		htlQuotaLog.setBedId(quotaReturn.getBedId());
		// CUTOFFDAY提前天数
		htlQuotaLog.setCutoffday(cutOffDayNew.getCutoffday());
		// 提前时间
		htlQuotaLog.setCutofftime(cutOffDayNew.getCutofftime());
		htlQuotaLog.setHotelId(cutOffDayNew.getHotelId());
		htlQuotaLog.setHtlQuotaDetailId(cutOffDayNew.getID());
		htlQuotaLog.setHtlQuotaId(cutOffDayNew.getQuotaId());
		// 配额调整
		htlQuotaLog.setJudgeType(HotelBaseConstantBean.RETURN_QUOTA);
		htlQuotaLog.setRoomtypeId(cutOffDayNew.getRoomtypeId());
		htlQuotaLog.setOperatorDept("CC");
		htlQuotaLog.setOperatorId("CC");
		htlQuotaLog.setOperatorName("CC ORDER");
		htlQuotaLog.setOperatorOrigin("CC");
		htlQuotaLog.setOperatorTime(new Date());
		// 当前可用数
		htlQuotaLog.setQuotaAvailable(String.valueOf(cutOffDayNew.getQuotaAvailable()));
		// 配额改变数,此时的改变数为需要退掉的配额数
		htlQuotaLog.setQuotaChange(Long.valueOf(quotaReturn.getQuotaNum()));
		// 配额持有者
		htlQuotaLog.setQuotaHolder(cutOffDayNew.getQuotaHolder());
		// 配额总数
		htlQuotaLog.setQuotaNum(cutOffDayNew.getQuotaNum());
		// 退配额时暂时不管C-I/S-I退模式
		//htlQuotaLog.setQuotaPattern(quotaPattern);
		// 配额共享模式
		htlQuotaLog.setQuotaShare(cutOffDayNew.getQuotaShare());
		// 配额类型
		htlQuotaLog.setQuotaType(cutOffDayNew.getQuotaType());
		// 配额使用数
		htlQuotaLog.setQuotaUsed(String.valueOf(cutOffDayNew.getQuotaUsed()));
		
		//将变化的配额记录到配额变化表中
		qryQuotaDao.saveQuotaLog(htlQuotaLog);
	}
	
	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}
	
	public IQuotaManage getQuotaManage() {
		return quotaManage;
	}

	public void setQuotaManage(IQuotaManage quotaManage) {
		this.quotaManage = quotaManage;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}
	
	public void setHotelRoomService(HotelRoomService hotelRoomService) {
		this.hotelRoomService = hotelRoomService;
	}
	
	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public void setQryQuotaDao(IQueryQuotaDao qryQuotaDao) {
		this.qryQuotaDao = qryQuotaDao;
	}
}
