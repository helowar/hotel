package hk.com.cts.ctcp.hotel.service.impl;

import hk.com.cts.ctcp.hotel.constant.CtsErrorMsg;
import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.service.HKManage;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.commonservice.AppRegData;
import hk.com.cts.ctcp.hotel.webservice.commonservice.CommonService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.ClassNationAmtData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.ClassQtyData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.CustInfoData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.EnquiryService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.NationAmtData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnDtlData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnStatusData;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomAmtResponse;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomQtyResponse;
import hk.com.cts.ctcp.hotel.webservice.saleservice.AddItemData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BeginData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CalAmtData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CustInfo;
import hk.com.cts.ctcp.hotel.webservice.saleservice.SaleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;

/**
 * 港中旅接口实现
 * 
 * @author shizhongwen 日期:Mar 10, 2009 时间:4:29:04 PM
 */
public class HKServiceImpl implements HKService {
	private static final MyLog log = MyLog.getLogger(HKServiceImpl.class);

	private CommonService commonService; // 公共接口

	private EnquiryService enquiryService; // 查询接口

	private SaleService saleService;// 交易接口

	private HKManage hkManage;// 业务逻辑处理类 用于获取酒店编码

	private static String sApiKey = "";

	private String agentCode = "";

	private HKServiceImpl() {

	}

	/**
	 * 根据sAgentCode取密钥对象AppRegData对象
	 * 
	 * @param sAgentCode
	 *            是 系统预订义标识字符串 如: 所有API用户必须首先调用此函数获取密钥 sAgentCode 为 MANGOCC -
	 *            Mango call center sAgentCode 为 MANGOIT – 芒果网站标识 add by
	 *            shizhongwen 时间:Mar 17, 2009 3:03:14 PM
	 * @param sAgentCode
	 * @return
	 */
	public AppRegData getComAppReg(String sAgentCode) {
		AppRegData appregdate = null;
		try {
			appregdate = commonService.comAppReg(sAgentCode);
		} catch (RuntimeException e) {
			log.error(e);
		}
		return appregdate;
	}

	/**
	 * 根据sAgentCode取得密钥 author:shizhongwen 日期:Mar 10, 2009 时间:5:19:30 PM
	 * 
	 * @param agentCode
	 *            芒果网站 sAgentCode为MANGOIT: 芒果非网站的系统则 sAgentCode为MANGOCC
	 * @return String
	 */
	public String getComAppReg() {
		AppRegData appregdate = null;
		try {
			appregdate = commonService.comAppReg(agentCode);
			if (null != appregdate) {
				sApiKey = appregdate.getSApiKey();
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return null;
		}
		return sApiKey;
	}

	/**
	 * 根据酒店id、入住日期、离店日来查询酒店所有房型的数量列表 * author:shizhongwen 日期:Mar 10, 2009
	 * 时间:5:48:05 PM
	 * 
	 * @param sHotelCode
	 *            酒店
	 * @param sDateFm
	 *            入住日期
	 * @param sDateTo
	 *            离店日期
	 * @param roomTypes
	 *            房型
	 * @return List<ClassQtyData>
	 */
	public List<HKRoomQtyResponse> enqRoomQty(long hotelId, Date dateFm,
			Date dateTo, String roomTypes) {
		List<HKRoomQtyResponse> hkRoomQtyResponselist = new ArrayList<HKRoomQtyResponse>();
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}

		// 如果key无效则返回null add by chenkeming
		if (!StringUtil.isValidStr(sApiKey)) {
			log.error("sApiKey 密钥为空 ");
			HKRoomQtyResponse hkroomQtyResponse = new HKRoomQtyResponse();
			hkroomQtyResponse.setHotelId(hotelId);
			hkroomQtyResponse.setRet(-1);
			hkroomQtyResponse.setMessage("sApiKey 密钥为空");
			hkRoomQtyResponselist.add(hkroomQtyResponse);
			return hkRoomQtyResponselist;
		}

		List<ClassQtyData> classQtyDatalist = null;
		// 根据酒店id,取得中旅酒店编码
		String sHotelCode = hkManage.getHKHotelCode(hotelId);

		// 入住日期 yyyyMMdd
		String sDateFm = DateUtil.dateToStringNew(dateFm);
		// 离店日期 yyyyMMdd
		String sDateTo = DateUtil.dateToStringNew(dateTo);
		int nDays = DateUtil.getDay(dateFm, dateTo) + 1;
		boolean updatetag = true;

		try {
			// 从港中旅取得酒店列表信息
			classQtyDatalist = enquiryService.enqClassQty(sApiKey, sHotelCode,
					sDateFm, sDateTo);
			if (0 < classQtyDatalist.size()) {
				// 将classQtyDatalist封装到hkRoomQtyResponselist返回列表中
				hkRoomQtyResponselist = hkManage.getHKRoomQtyList(hotelId,
						classQtyDatalist, sDateFm, roomTypes, nDays);
			} else {
				log.error(" 调用getHKRoomQtyList方法取得数据为空, sApiKey:" + sApiKey
						+ ", sHotelCode:" + sHotelCode + ", sDateFm:" + sDateFm
						+ ", sDateTo" + sDateTo);
				HKRoomQtyResponse hkroomQtyResponse = new HKRoomQtyResponse();
				hkroomQtyResponse.setRet(-1);
				hkroomQtyResponse.setHotelId(hotelId);
				hkroomQtyResponse.setMessage("取得数据为空");
				hkRoomQtyResponselist.add(hkroomQtyResponse);
				;
				return hkRoomQtyResponselist;
			}
			// modify by shizhongwen 2009-04-19
			// 更新房型数量到存储过程取消(因为HWEB也是通过提供的接口取数据,故无需存放到数据库中)
			// for(HKRoomQtyResponse response:hkRoomQtyResponselist){
			// if(response.getRet() == ResultConstant.RESULT_CUTOFF_TIME) {
			// continue;
			// }
			// qty=response.getQty();
			// roomTypeId=Long.parseLong(response.getRoomTypeId());
			// datetime=response.getDate();
			// tag=hkManage.updateHKhotelRoom(hotelId, qty, roomTypeId,
			// datetime);
			// if(!tag.equals("0")){
			// updatetag=false;
			// }
			// }

		} catch (Exception e) {
			log.error("enqRoomQty()调中旅接口查配额失败",e);
			HKRoomQtyResponse hkroomQtyResponse = new HKRoomQtyResponse();
			hkroomQtyResponse.setRet(-1);
			hkroomQtyResponse.setHotelId(hotelId);
			hkroomQtyResponse.setMessage("查询getHKRoomQtyList方法报错"
					+ e.getMessage());
			hkRoomQtyResponselist.add(hkroomQtyResponse);
			;
			return hkRoomQtyResponselist;
		}
		if (updatetag) {
			return hkRoomQtyResponselist;
		} else {
			return null;
		}
	}

	/**
	 * 网站查配额
	 * 
	 * @param hotelId
	 * @param dateFm
	 * @param dateTo
	 * @param roomTypeId
	 * @return
	 */
	public List<Date> enqRoomQtyForWeb(long hotelId, Date dateFm, Date dateTo,
			long roomTypeId) {
		List<Date> liUnbookDate = new ArrayList<Date>();
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}

		// 如果key无效则返回null add by chenkeming
		if (!StringUtil.isValidStr(sApiKey)) {
			log.error("sApiKey 密钥为空 ");
			return liUnbookDate;
		}

		List<ClassQtyData> classQtyDatalist = null;
		// 根据酒店id,取得中旅酒店编码
		String sHotelCode = hkManage.getHKHotelCode(hotelId);

		// 入住日期 yyyyMMdd
		String sDateFm = DateUtil.dateToStringNew(dateFm);
		// 离店日期 yyyyMMdd
		String sDateTo = DateUtil.dateToStringNew(dateTo);
		try {
			// 从港中旅取得酒店列表信息
			classQtyDatalist = enquiryService.enqClassQty(sApiKey, sHotelCode,
					sDateFm, sDateTo);
			if (0 < classQtyDatalist.size()) {
				return hkManage.getHKRoomQtyListForWeb(hotelId,
						classQtyDatalist, roomTypeId);
			} else {
				log.error(" 调用getHKRoomQtyList方法取得数据为空, sApiKey:" + sApiKey
						+ ", sHotelCode:" + sHotelCode + ", sDateFm:" + sDateFm
						+ ", sDateTo" + sDateTo);
				return liUnbookDate;
			}
		} catch (Exception e) {
			log.error("enqRoomQtyForWeb()调中旅接口查配额失败",e);
			return liUnbookDate;
		}
	}

	/**
	 * 根据酒店ID、入住日期、离店日期来查询所有房型价格的列表 author:shizhongwen 日期:Mar 10, 2009
	 * 时间:6:04:12 PM
	 * 
	 * @param hotelId
	 *            酒店
	 * @param sDateFm
	 *            入住日期
	 * @param sDateTo
	 *            离店日期
	 * @return List<ClassNationAmtData>
	 */
	public List<HKRoomAmtResponse> enqRoomNationAmt(long hotelId, Date dateFm,
			Date dateTo, String roomTypes, String childRoomTypes) {
		List<ClassNationAmtData> classNationAmtDataList = null;
		List<HKRoomAmtResponse> hkRoomAmtResponseList = new ArrayList<HKRoomAmtResponse>();

		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		// 如果key无效则返回null add by chenkeming
		if (!StringUtil.isValidStr(sApiKey)) {
			log.error("sApiKey 密钥为空 ");
			HKRoomAmtResponse hkroomamtresponse = new HKRoomAmtResponse();
			hkroomamtresponse.setRet(-1);
			hkroomamtresponse.setHotelId(hotelId);
			hkroomamtresponse.setMessage("sApiKey 密钥为空");
			hkRoomAmtResponseList.add(hkroomamtresponse);
			return hkRoomAmtResponseList;
		}

		// 根据酒店id 获取香港酒店编码
		String sHotelCode = hkManage.getHKHotelCode(hotelId);// "MAC01";
		String sDateFm = DateUtil.dateToStringNew(dateFm);
		String sDateTo = DateUtil.dateToStringNew(dateTo);
		int nday = DateUtil.getDay(dateFm, dateTo) + 1;
		try {
			// 通过调用香港提供的接口方法查询酒店的价格
			classNationAmtDataList = enquiryService.enqClassNationAmt(sApiKey,
					sHotelCode, sDateFm, sDateTo);
			if (0 >= classNationAmtDataList.size()) {
				log
						.error("--通过调用香港提供的接口方法查询酒店的价格 classNationAmtDataList为空--- ");
				log.error("--sApiKey=" + sApiKey + " sHotelCode=" + sHotelCode
						+ " sDateFm" + sDateFm + " sDateTo=" + sDateTo);
				HKRoomAmtResponse hkroomamtresponse = new HKRoomAmtResponse();
				hkroomamtresponse.setRet(ResultConstant.RESULT_FAIL);
				hkroomamtresponse.setHotelId(hotelId);
				hkroomamtresponse.setMessage("调用enqClassNationAmt方法取得数据为空");
				hkRoomAmtResponseList.add(hkroomamtresponse);
				return hkRoomAmtResponseList;
			}
			/*
			 * if (classNationAmtDataList.size() < nday) { HKRoomAmtResponse
			 * hkRoomAmtResponse = new HKRoomAmtResponse();
			 * hkRoomAmtResponse.setRet(ResultConstant.RESULT_FAIL);
			 * hkRoomAmtResponse.setHotelId(hotelId);
			 * hkRoomAmtResponse.setMessage("获取最新价格失败!!!");
			 * hkRoomAmtResponseList.add(hkRoomAmtResponse); return
			 * hkRoomAmtResponseList; }
			 */
			
			
			
			// 如果数据库价格更新成功则将数据进行封装
				hkRoomAmtResponseList = hkManage.getHKRoomAmtList(hotelId,
						classNationAmtDataList, dateFm, roomTypes, nday,
						childRoomTypes);
				
			//将价格写入数据库
			updateHotelPrice(hotelId,classNationAmtDataList,hkRoomAmtResponseList);
			// classNationAmtDataList hkRoomAmtResponseList 两个的数据不一致故导致插入数据库的数据不一致
			return hkRoomAmtResponseList;
		} catch (Exception e) {
			log.error("enqRoomNationAmt()调中旅接口查价格失败",e);
			HKRoomAmtResponse hkRoomAmtResponse = new HKRoomAmtResponse();
			hkRoomAmtResponse.setRet(ResultConstant.RESULT_FAIL);
			hkRoomAmtResponse.setHotelId(hotelId);
			hkRoomAmtResponse.setMessage("错误方法:" + e.getMessage());
			hkRoomAmtResponseList.add(hkRoomAmtResponse);
			return hkRoomAmtResponseList;
		}
	}

	/**
	 * 将刷回来的价格写到芒果网数据库中
	 * @param hotelId
	 *            酒店ID
	 * @param classNationAmtDatas
	 * @return
	 */
	private boolean updateHotelPrice(long hotelId,List<ClassNationAmtData> classNationAmtDatas,List<HKRoomAmtResponse> hkRoomAmtResponses){
    	boolean updateTag = false;
    	
    	long roomTypeId;
    	 //更新酒店价格
         for(ClassNationAmtData classNationAmtData:classNationAmtDatas){
	         if(classNationAmtData!=null){
		         //将香港房型id转换为芒果房型id 因为芒果网房型ID与中旅房型ID为一对一的关系；故直接取第一个
	        	 List<String> list= hkManage.getRoomIdMappingByHK(hotelId, classNationAmtData.getSClassCode());
	        	 if(list.size() == 0) continue;
		         roomTypeId=Long.parseLong(list.get(0));
		         //将香港价格类型编码转换为芒果子房型编码 如香港价格类型为all,则对应此芒果一系列对应的此子房型
		         List<String[]> childRoomTypeIdList=hkManage.getPriceMappingByHK(hotelId,classNationAmtData.getSClassCode(),classNationAmtData.getSNation());
		        					 
		         //将香港的日期格式转换为芒果网所有的日期格式(yyyymmdd-->yyy-MM-dd)
		        String ableSaleDate=DateUtil.dateToString(DateUtil.toDateByFormat(classNationAmtData.getSDate(),ResultConstant.DATEFOMAT_MANGO));
		         float nBaseAmt=0f;
		         float  nListAmt=0f;
//		         if(nBaseAmt<0){
//		        	 nBaseAmt=0f;
//		         }
//		         if(nListAmt<0){
//		        	 nListAmt=0f;
//		         }
		         for(String[] str:childRoomTypeIdList){
			       long  childRoomTypeId=Long.parseLong(str[0]);
			       for(HKRoomAmtResponse hkRoomAmtResponse:hkRoomAmtResponses){ //将加幅后的 价格 赋值过来 这个效率太差有待改进
			    	   if(childRoomTypeId==Long.valueOf(hkRoomAmtResponse.getChildRoomTypeId())&&hkRoomAmtResponse.getListAmt()!=0
			    			&& ableSaleDate.equals(hkRoomAmtResponse.getDate())){
			    		   nBaseAmt = hkRoomAmtResponse.getBaseAmt();
			    		   nListAmt =  hkRoomAmtResponse.getListAmt();
			    	   }
			       }
			         //更新价格 调用存储过程
			         String tag=hkManage.updateHKHotelPrice(hotelId,nBaseAmt, nListAmt,ableSaleDate, childRoomTypeId, roomTypeId);
			         
			         if("0".equals(tag)){
			        	 updateTag=true;
			         }else{
				         log.error("更新香港酒店价格失败,hotelId:"+hotelId+
				         "roomTypeId:"+roomTypeId+" childRoomTypeId:"
				         +childRoomTypeId+" ableSaleDate:"+ableSaleDate
				         +" nBaseAmt:"+nBaseAmt+" nListAmt:"+nListAmt);
				         break;
			         }
		         }
	         }
         }
    	return updateTag;
    }

	/**
	 * 根据酒店ID、入住日期、离店日期、房型编码来查询房型价格列表 add by shizhongwen 时间:Mar 23, 2009 3:47:53
	 * PM
	 * 
	 * @param hotelId
	 * @param dateFm
	 * @param dateTo
	 * @param roomTypeId
	 * @param childRoomTypeId
	 * @return
	 */
	public List<HKRoomAmtResponse> enqNationAmt(long hotelId, Date dateFm,
			Date dateTo, long roomTypeId) {
		List<HKRoomAmtResponse> hkRoomAmtResponseList = new ArrayList<HKRoomAmtResponse>();
		List<NationAmtData> nationAmtDataList = null;
		boolean updatetag = false;

		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			HKRoomAmtResponse hkroomamtresponse = new HKRoomAmtResponse();
			hkroomamtresponse.setRet(-1);
			hkroomamtresponse.setMessage("调用密钥出错:返回密钥为空 sApiKey:" + sApiKey);
			hkRoomAmtResponseList.add(hkroomamtresponse);
			log.error("sApiKey 密钥为空 ");
			return hkRoomAmtResponseList;
		}
		// 根据酒店id 获取香港酒店编码
		String sHotelCode = hkManage.getHKHotelCode(hotelId);// "MAC01";
		// 根据酒店id 芒果网房型id 获取香港房型id
		String sHkroomTypeId = hkManage.getHKRoomTypeId(hotelId, roomTypeId);
		String sDateFm = DateUtil.dateToStringNew(dateFm);
		String sDateTo = DateUtil.dateToStringNew(dateTo);
		try {
			nationAmtDataList = enquiryService.enqNationAmt(sApiKey,
					sHotelCode, sDateFm, sDateTo, sHkroomTypeId);
			if (0 >= nationAmtDataList.size()) {
				log.error("---------根据酒店ID、入住日期、离店日期、房型编码来查询"
						+ "					房型价格列表为空 nationAmtDataList.size()=0------ ");
				log.error("--sApiKey=" + sApiKey + " sHotelCode=" + sHotelCode
						+ " sDateFm" + sDateFm + " sDateTo=" + sDateTo
						+ " sHkroomTypeId=" + sHkroomTypeId);
				HKRoomAmtResponse hkroomamtresponse = new HKRoomAmtResponse();
				hkroomamtresponse.setRet(-1);
				hkroomamtresponse
						.setMessage("调用enqNationAmt出错:返回nationAmtDataList为空 ");
				hkRoomAmtResponseList.add(hkroomamtresponse);
				return hkRoomAmtResponseList;
			}
			// modify by shizhongwen
			// 2009-04-19hotelv2.8更新房型价格到存储过程取消(因为HWEB也是通过提供的接口取数据,故无需存放到数据库中)
			updatetag = true;
			// for(NationAmtData object:nationAmtDataList){
			// if(object!=null){
			// //将香港价格类型编码转换为芒果子房型编码
			// List<String> childRoomTypeIdList=hkManage
			// .getPriceMappingByHK(hotelId,
			// sHkroomTypeId, object.getSNationCode());
			//					
			// //将香港的日期格式转换为芒果网所有的日期格式(yyyymmdd-->yyy-MM-dd)
			// ableSaleDate=DateUtil.dateToString(DateUtil
			// .toDateByFormat(object.getSDate(),
			// ResultConstant.DATEFOMAT_HK));
			//					                     
			// // v2.8 modify by chenkeming
			// nBaseAmt = object.getNBaseAmt();
			// nListAmt = object.getNListAmt();
			//                 
			// for (String schildRoomType : childRoomTypeIdList) {
			// // 更新价格 调用存储过程
			// childRoomTypeId = Long.parseLong(schildRoomType);
			// String tag = hkManage.updateHKHotelPrice(hotelId,
			// nBaseAmt, nListAmt, ableSaleDate,
			// childRoomTypeId, roomTypeId);
			// if (tag.equals("0")) {
			// updatetag = true;
			// } else {
			// log.error("根据酒店id,房型id更新香港酒店价格失败,hotelId:"
			// + hotelId + " roomTypeId:" + roomTypeId
			// + " childRoomTypeId:" + childRoomTypeId
			// + " ableSaleDate:" + ableSaleDate
			// + " nBaseAmt:" + nBaseAmt + " nListAmt:"
			// + nListAmt);
			// break;
			// }
			// }
			// }
			// }
			// 如果数据库价格更新成功则将数据进行封装
			if (updatetag) {
				hkRoomAmtResponseList = hkManage.getHKRoomNationAmtList(
						hotelId, roomTypeId, nationAmtDataList);
			}
		} catch (RuntimeException e) {
			log.error("enqNationAmt()调中旅接口查价格类型失败",e);
			HKRoomAmtResponse hkroomamtresponse = new HKRoomAmtResponse();
			hkroomamtresponse.setRet(-1);
			hkroomamtresponse.setMessage(e.getMessage());
			hkRoomAmtResponseList.add(hkroomamtresponse);
			return hkRoomAmtResponseList;
		}
		return hkRoomAmtResponseList;
	}

	/**
	 * 根据酒店ID、入住日期、离店日期、房型编码、子房型编码来查询房型价格列表 add by shizhongwen 时间:Mar 23, 2009
	 * 3:47:53 PM
	 * 
	 * @param hotelId
	 * @param dateFm
	 * @param dateTo
	 * @param roomTypeId
	 * @param childRoomTypeId
	 * @return
	 */
	public List<HKRoomAmtResponse> enqHKAmtByNation(long hotelId, Date dateFm,
			Date dateTo, long roomTypeId, String childRoomTypeId) {
		List<HKRoomAmtResponse> hkroominfolist;
		List<HKRoomAmtResponse> hkroominfolistnew = new ArrayList<HKRoomAmtResponse>();
		hkroominfolist = this.enqNationAmt(hotelId, dateFm, dateTo, roomTypeId);
		if (0 < hkroominfolist.size()) {
			for (HKRoomAmtResponse object : hkroominfolist) {
				if (object.getChildRoomTypeId().equals(childRoomTypeId)) {
					hkroominfolistnew.add(object);
				}
			}
		} else {
			log.error("------调用enqNationAmt返回hkroominfolist.size()="
					+ hkroominfolist.size());
			hkroominfolist = new ArrayList<HKRoomAmtResponse>();
			HKRoomAmtResponse roomamtresponse = new HKRoomAmtResponse();
			roomamtresponse.setRet(-1);
			roomamtresponse.setMessage(" hkroominfolist.size="
					+ hkroominfolist.size());
			hkroominfolist.add(roomamtresponse);
		}
		return hkroominfolistnew;
	}

	/**
	 * 根据密钥、酒店编码、入住日期、离店日期、房型编码来查询房型价格列表 author:shizhongwen 日期:Mar 10, 2009
	 * 时间:6:11:06 PM
	 * 
	 * @param sHotelCode
	 *            酒店
	 * @param sDateFm
	 *            入住日期
	 * @param sDateTo
	 *            离店日期
	 * @param sClassCode
	 *            房型编码
	 * @return List<NationAmtData>
	 */
	public List<NationAmtData> enqNationAmt(String sHotelCode, String sDateFm,
			String sDateTo, String sClassCode) {
		List<NationAmtData> nationAmtDataList = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			nationAmtDataList = new ArrayList<NationAmtData>();
			NationAmtData nationamtData = new NationAmtData();
			nationamtData.setNRet(-1);
			nationamtData.setSMessage("sApiKey 密钥为空 ");
			nationAmtDataList.add(nationamtData);
			log.error("sApiKey 密钥为空 ");
			return nationAmtDataList;
		}
		try {
			nationAmtDataList = enquiryService.enqNationAmt(sApiKey,
					sHotelCode, sDateFm, sDateTo, sClassCode);
		} catch (RuntimeException e) {
			nationAmtDataList = new ArrayList<NationAmtData>();
			NationAmtData nationamtData = new NationAmtData();
			nationamtData.setNRet(-1);
			nationamtData.setSMessage("错误:" + e.getMessage());
			nationAmtDataList.add(nationamtData);
			log.error("enqNationAmt()调中旅接口查价格类型失败",e);
			return nationAmtDataList;
		}
		return nationAmtDataList;
	}

	/**
	 * 根交易编号，查询当前或完成交易的客户信息列表 author:shizhongwen 日期:Mar 10, 2009 时间:6:15:11 PM
	 * 
	 * @param sTxnNo
	 *            交易编号
	 * @return CustInfoData
	 */
	public List<CustInfoData> enqCustInfo(String sTxnNo) {
		List<CustInfoData> custInfoDatalist = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}

		if (!StringUtil.isValidStr(sApiKey)) {
			custInfoDatalist = new ArrayList<CustInfoData>();
			CustInfoData custinfoData = new CustInfoData();
			custinfoData.setNRet(-1);
			custinfoData.setSMessage("sApiKey 密钥为空");
			custInfoDatalist.add(custinfoData);
			log.error("sApiKey 密钥为空 ");
			return custInfoDatalist;
		}
		try {
			custInfoDatalist = enquiryService.enqCustInfo(sApiKey, sTxnNo);
		} catch (Exception e) {
			log.error("enqCustInfo()调中旅接口查询当前或完成交易的客户信息列表失败",e);
			custInfoDatalist = new ArrayList<CustInfoData>();
			CustInfoData custinfoData = new CustInfoData();
			custinfoData.setNRet(-1);
			custinfoData.setSMessage("错误原因:" + e.getMessage());
			custInfoDatalist.add(custinfoData);
			return custInfoDatalist;
		}
		return custInfoDatalist;
	}

	/**
	 * 根据交易编号来查询交易（所有交易类型）的状态 author:shizhongwen 日期:Mar 10, 2009 时间:6:18:46 PM
	 * 
	 * @param sTxnNo
	 *            交易编号
	 * @return
	 */
	public TxnStatusData enqTxnStatus(String sTxnNo) {
		TxnStatusData txnStatusData = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			txnStatusData = new TxnStatusData();
			txnStatusData.setNRet(-1);
			txnStatusData.setSMessage("sApiKey 密钥为空");
			log.error("sApiKey 密钥为空 ");
			return txnStatusData;
		}
		try {
			txnStatusData = enquiryService.enqTxnStatus(sApiKey, sTxnNo);
			if (txnStatusData.getNRet() >= ResultConstant.RESULT_SUCCESS) {
				String sCode = txnStatusData.getSStatus();
				if (TxnStatusType.sRollbacked.equals(sCode)) { // rollback
					txnStatusData.setNRet(TxnStatusType.Rollbacked);
				} else if (TxnStatusType.sCommited.equals(sCode)) { // commited
					txnStatusData.setNRet(TxnStatusType.Commited);
				} else if (TxnStatusType.sBegin.equals(sCode)) { // begin
					txnStatusData.setNRet(TxnStatusType.Begin);
				}
			}
		} catch (RuntimeException e) {
			txnStatusData = new TxnStatusData();
			txnStatusData.setNRet(-1);
			txnStatusData.setSMessage("错误原因:" + e.getMessage());
			log.error("enqTxnStatus()调中旅接口查询交易（所有交易类型）的状态失败",e);
			return txnStatusData;
		}
		return txnStatusData;
	}

	/**
	 * 根据交易编号来查询目前或完成交易的清单的内容 author:shizhongwen 日期:Mar 10, 2009 时间:6:19:33 PM
	 * 
	 * @param sTxnNo
	 * @return
	 */
	public List<TxnDtlData> enqTxnDtl(String sTxnNo) {
		List<TxnDtlData> txnDtlDataList = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			log.error("sApiKey 密钥为空 ");
			txnDtlDataList = new ArrayList<TxnDtlData>();
			TxnDtlData txnDtldata = new TxnDtlData();
			txnDtldata.setNRet(-1);
			txnDtldata.setSMessage("sApiKey 密钥为空 ");
			txnDtlDataList.add(txnDtldata);
			return txnDtlDataList;
		}
		try {
			txnDtlDataList = enquiryService.enqTxnDtl(sApiKey, sTxnNo);
		} catch (RuntimeException e) {
			log.error("enqTxnDtl()调中旅接口查询目前或完成交易的清单的内容失败",e);
			txnDtlDataList = new ArrayList<TxnDtlData>();
			TxnDtlData txnDtldata = new TxnDtlData();
			txnDtldata.setNRet(-1);
			txnDtldata.setSMessage("错误原因:" + e.getMessage());
			txnDtlDataList.add(txnDtldata);
			return txnDtlDataList;
		}
		return txnDtlDataList;
	}

	/**
	 * 开始交易(交易之前都要调用这个操作) author:shizhongwen 日期:Mar 10, 2009 时间:6:42:59 PM
	 * 
	 * @param sApiKey
	 * @return
	 */
	public BeginData saleBegin() {
		BeginData beginData = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			log.error("sApiKey 密钥为空 ");
			beginData = new BeginData();
			beginData.setNRet(-1);
			beginData.setSMessage("sApiKey 密钥为空");
			return beginData;
		}
		try {
			beginData = saleService.saleBegin(sApiKey);
		} catch (RuntimeException e) {
			log.error("saleBegin()调中旅接口开始交易失败",e);
			beginData = new BeginData();
			beginData.setNRet(-1);
			beginData.setSMessage("错误原因:" + e.getMessage());
			return beginData;
		}
		return beginData;
	}

	/**
	 * 根据交易编号、酒店编码、日期、房型编码、价格编码、数量来预订房间（HoldRoom） add by shizhongwen 时间:Mar 25,
	 * 2009 4:10:41 PM
	 * 
	 * @param sTxnNo
	 *            交易编号
	 * @param sHotelCode
	 *            酒店编码
	 * @param sDateFm
	 *            入住日期
	 * @param sDateTo
	 *            离店日期
	 * @param sClassCode
	 *            房型编码
	 * @param sNationCode
	 *            价格编码
	 * @param nQty
	 *            入住间数
	 * @return by chenkeming@2009-03-27 返回类型改成BasicData
	 */
	public BasicData holdRoom(String txnNo, long hotelId, Date dateFm,
			Date dateTo, long roomTypeId, long childRoomTypeId, int qty) {
		BasicData ret = new BasicData();
		AddItemData addItemData = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			ret.setNRet(ResultConstant.RESULT_FAIL);
			ret.setSMessage("sApiKey 密钥为空");
			log.error("sApiKey 密钥为空 ");
			return ret;
		}
		// 根据酒店id 获取香港酒店编码
		String sHotelCode = hkManage.getHKHotelCode(hotelId);// "MAC01";
		// 根据酒店id 芒果网房型id 获取香港房型id
		String sHkroomTypeId = hkManage.getHKRoomTypeId(hotelId, roomTypeId);
		// 根据酒店id、 芒果网房型id 、芒果子房型id 获取香港价格类型id
		String sHkChildRoomTypeId = hkManage.getHKChildRoomId(hotelId,
				roomTypeId, childRoomTypeId);

		String sDateFm = DateUtil.dateToStringNew(dateFm);
		int nday = DateUtil.getDay(dateFm, dateTo);
		String sDate = "";
		for (int i = 0; i < nday; i++) {
			sDate = DateUtil.addStringDateALLMain(sDateFm, i,
					ResultConstant.DATEFOMAT_HK);
			// 调用hold 房接口
			addItemData = saleService.saleAddItem(sApiKey, txnNo, sHotelCode,
					sDate, sHkroomTypeId, sHkChildRoomTypeId, qty);
			if (null != addItemData) {
				ret.setNRet(addItemData.getNRet());
				if (addItemData.getNRet() < ResultConstant.RESULT_SUCCESS) {
					ret.setSMessage(CtsErrorMsg.toChnMsg(addItemData
							.getSMessage()));
					log.error(addItemData.getSMessage());
					return ret;
				}
			} else {
				ret.setNRet(ResultConstant.RESULT_FAIL);
				ret.setSMessage("返回 hold 房对象为空");
				log.error("返回 hold 房对象为空");
				return ret;
			}
		}
		return ret;
	}

	/**
	 * 根据密钥、交易编号来查询当前交易的净额 author:shizhongwen 日期:Mar 10, 2009 时间:4:47:46 PM
	 * 
	 * @param sApiKey
	 *            密钥
	 * @param sTxnNo
	 *            交易编号
	 * @return
	 */
	public CalAmtData saleCalAmt(String sTxnNo) {
		CalAmtData calAmtData = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			calAmtData = new CalAmtData();
			calAmtData.setNRet(-1);
			calAmtData.setSMessage("sApiKey 密钥为空");
			log.error("sApiKey 密钥为空 ");
			return calAmtData;
		}
		try {
			calAmtData = saleService.saleCalAmt(sApiKey, sTxnNo);
		} catch (RuntimeException e) {
			log.error("saleCalAmt()调中旅接口查询当前交易的净额失败",e);
			calAmtData = new CalAmtData();
			calAmtData.setNRet(-1);
			calAmtData.setSMessage("sApiKey 密钥为空");
			return calAmtData;

		}
		return calAmtData;
	}

	/**
	 * 填写入住客户的信息。 author:shizhongwen 日期:Mar 10, 2009 时间:4:49:05 PM
	 * 
	 * @param sApiKey
	 *            密钥
	 * @param sTxnNo
	 *            交易编号
	 * @param aInfo
	 * @return
	 */
	public BasicData saleAddCustInfo(String sTxnNo, List<CustInfo> aInfo,
			String sRmk) {
		BasicData basicData = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			basicData = new BasicData();
			basicData.setNRet(-1);
			basicData.setSMessage("sApiKey 密钥为空");
			log.error("sApiKey 密钥为空 ");
			return basicData;
		}
		try {
			basicData = saleService.saleAddCustInfo(sApiKey, sTxnNo, aInfo,
					sRmk);
		} catch (RuntimeException e) {
			basicData = new BasicData();
			basicData.setNRet(-1);
			basicData.setSMessage("错误原因:" + e.getMessage());
			log.error("saleAddCustInfo()调中旅接口填写入住客户的信息失败",e);
			return basicData;
		}
		return basicData;
	}

	/**
	 * 承诺（确认）买卖交易 author:shizhongwen 日期:Mar 10, 2009 时间:4:52:12 PM
	 * 
	 * @param sApiKey
	 *            密钥
	 * @param sTxnNo
	 *            交易编号
	 * @param nTotItem
	 *            房间数
	 * @return
	 */
	public BasicData saleCommit(String sTxnNo, int nTotItem) {
		BasicData basicData = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			basicData = new BasicData();
			basicData.setNRet(-1);
			basicData.setSMessage("sApiKey 密钥为空");
			log.error("sApiKey 密钥为空 ");
			return basicData;
		}
		try {
			basicData = saleService.saleCommit(sApiKey, sTxnNo, nTotItem);
		} catch (RuntimeException e) {
			basicData = new BasicData();
			basicData.setNRet(-1);
			basicData.setSMessage("错误原因:" + e.getMessage());
			log.error("saleCommit()提交订单时，调中旅接口失败",e);
			return basicData;
		}
		return basicData;
	}

	/**
	 * 回滚 author:shizhongwen 日期:Mar 10, 2009 时间:4:54:54 PM
	 * 
	 * @param sTxnNo
	 *            交易编号
	 * @return
	 */
	public BasicData saleRollback(String sTxnNo) {
		BasicData basicData = null;
		// 获得密钥
		if (null == sApiKey || sApiKey.equals("")) {
			sApiKey = getComAppReg();
		}
		if (!StringUtil.isValidStr(sApiKey)) {
			basicData = new BasicData();
			basicData.setNRet(-1);
			basicData.setSMessage("sApiKey 密钥为空");
			log.error("sApiKey 密钥为空 ");
			return basicData;
		}
		try {
			basicData = saleService.saleRollback(sTxnNo);
		} catch (Exception e) {
			// modify by chenkeming : try 2 more times when rollback fail on exception
			e.printStackTrace();
			try {
				Thread.sleep(100);
				basicData = saleService.saleRollback(sTxnNo);
			} catch (Exception e1) {
				e1.printStackTrace();
				try {
					Thread.sleep(100);
					basicData = saleService.saleRollback(sTxnNo);
				} catch (Exception e2) {
					e2.printStackTrace();
					basicData = new BasicData();
					basicData.setNRet(-1);
					basicData.setSMessage("错误原因:" + e.getMessage());
					log.error(e2.getMessage(), e2);
					log.error("CTS order rollback fail on exception, CTS orderNo: " + sTxnNo);
					return basicData;
				}				
			}
		}
		return basicData;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public EnquiryService getEnquiryService() {
		return enquiryService;
	}

	public void setEnquiryService(EnquiryService enquiryService) {
		this.enquiryService = enquiryService;
	}

	public SaleService getSaleService() {
		return saleService;
	}

	public void setSaleService(SaleService saleService) {
		this.saleService = saleService;
	}

	public String getSApiKey() {
		return sApiKey;
	}

	public void setSApiKey(String apiKey) {
		sApiKey = apiKey;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public HKManage getHkManage() {
		return hkManage;
	}

	public void setHkManage(HKManage hkManage) {
		this.hkManage = hkManage;
	}

}
