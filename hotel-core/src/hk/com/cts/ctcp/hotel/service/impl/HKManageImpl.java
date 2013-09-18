package hk.com.cts.ctcp.hotel.service.impl;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.dao.IExDao;
import hk.com.cts.ctcp.hotel.service.HKManage;
import hk.com.cts.ctcp.hotel.service.IExMappingService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.ClassNationAmtData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.ClassQtyData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.NationAmtData;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomAmtResponse;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomQtyResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.IncreaseBean;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlIncreasePrice;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.log.MyLog;

/**
 */
public class HKManageImpl implements HKManage {
	private static final MyLog log = MyLog.getLogger(HKManageImpl.class);
	
	/**
     * 与resourceDescr.xml中的值对应
     */
	private static final int CHANNEL_CTS = 8;
	
	/**
     * 中旅加幅类型
     */
    public static final String JINCREASETYPE = "1"; // 1:金额
    public static final String BINCREASETYPE = "2"; // 2:百分比

    private IExMappingService imappingService;

    private IExDao exDao;

    /**
     * 根据酒店id 找到香港酒店编码 add by shizhongwen 时间:Mar 17, 2009 4:37:00 PM
     * 
     * @param hotelId
     * @return
     */
    public String getHKHotelCode(Long hotelId) {
        String hotelCode = "";
        List<Long> hotelIds = new ArrayList<Long>();
        hotelIds.add(Long.valueOf(hotelId));
        try {
            List exMappingLst = imappingService.getMapping(CHANNEL_CTS, hotelIds);
            if (!exMappingLst.isEmpty()) {
                ExMapping em = (ExMapping) exMappingLst.get(0);
                hotelCode = em.getHotelcodeforchannel();
            }
        } catch (Exception e) {
            log.error(e);
        }
        return hotelCode;
    }

    /**
     * 根据酒店id,芒果房型id获取香港对应的房型Id add by shizhongwen 时间:Mar 23, 2009 4:24:12 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @return
     */
    public String getHKRoomTypeId(Long hotelId, long roomTypeId) {
        ExMapping exMapping = imappingService.getMapping(CHANNEL_CTS, hotelId,
            roomTypeId);
        if (null != exMapping) {
            return exMapping.getRoomtypecodeforchannel();
        } else {
            log.error("根据渠道、酒店id、芒果房型id获取香港房型id 为空");
            return null;
        }

    }

    /**
     * 
     * 根据渠道、芒果酒店id、香港房型id、香港价格类型Id获取芒果网的子房型ID add by shizhongwen 时间:Mar 19, 2009 3:53:51 PM
     * 
     * @param hotelId
     * @param hkrooomTypeId
     * @param sNation
     * @return
     */
    public List<String[]> getPriceMappingByHK(long hotelId, String hkrooomTypeId, String nation) {
        List<ExMapping> emlist = imappingService.getMappingOfNation(CHANNEL_CTS,
            hotelId, hkrooomTypeId, nation);
        List<String[]> priceList = new ArrayList<String[]>();
        if (null != emlist) {
            for (ExMapping exmapping : emlist) {
                priceList.add(new String[]{exmapping.getPriceTypeId(), 
                		exmapping.getRoomTypeId()});
            }
            return priceList;
        } else {
            log.error("em为空， 根据渠道、芒果酒店id、香港房型id、香港价格类型Id获取芒果网的子房型ID 为空");
            return priceList;
        }

    }

    /**
     * 根据渠道、酒店id、香港房型id获取芒果网的房型编码 add by shizhongwen 时间:Mar 19, 2009 3:20:00 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @return
     */
    public List<String> getRoomIdMappingByHK(long hotelId, String hkrooomTypeId) {
        List<ExMapping> liEm = imappingService.getMapping(CHANNEL_CTS, hotelId, hkrooomTypeId);
        List<String> liRes = new ArrayList<String>();
        if (null != liEm && 0 < liEm.size()) {
        	for(ExMapping em : liEm) {
        		liRes.add(em.getRoomTypeId());
        	}
            return liRes;            
        } else {
            log.error("根据渠道、酒店id(" + hotelId + ")、香港房型id(" 
            		+ hkrooomTypeId + ")获取芒果网的房型编码 为空");
            return liRes;
        }
    }

    /**
     * 根据酒店id 找到相应香港对应属性类ExMapping add by shizhongwen 时间:Mar 17, 2009 4:38:21 PM
     * 
     * @param hotelId
     * @return
     */
    public ExMapping getHKExMapping(Long hotelId) {
        ExMapping em = null;
        List<Long> hotelIds = new ArrayList<Long>();
        hotelIds.add(Long.valueOf(hotelId));

        List exMappingLst = imappingService.getMapping(CHANNEL_CTS, hotelIds);
        if (!exMappingLst.isEmpty()) {
            em = (ExMapping) exMappingLst.get(0);
        }
        return em;
    }

    /**
     * 取得酒店返回列表信息 add by shizhongwen 时间:Mar 17, 2009 4:51:18 PM
     * 
     * @param classQtyDatalist
     * @return
     *  by chenkeming@2009-04-17 修改获取dayIndex逻辑
     */
    public List<HKRoomQtyResponse> getHKRoomQtyList(Long hotelId,
			List<ClassQtyData> classQtyDatalist, String sDateFm,
			String roomTypes, int nDays) {
        List<HKRoomQtyResponse> hkRoomQtyResponseList = new ArrayList<HKRoomQtyResponse>();
        HKRoomQtyResponse hkRoomQtyResponse = null;
        String roomTypeId = "";
        List<String> liRt = null;
        String[] rtIndex = null;
        Date newdate;
        Date cutoffdate;
        Date dateFm = DateUtil.toDateByFormat(sDateFm, "yyyyMMdd");
//        log.info("getHKRoomQtyList,roomTypes:" + roomTypes);
        String[] ids = {};
        if (roomTypes != null){
        	ids = roomTypes.split("#");
        }
//        log.info("getHKRoomQtyList,ids.length:" + ids.length + ",nDays:" + nDays);
        int[] chkRes = new int[ids.length];
        int[] chkRtTotal = new int[ids.length];
        Map rtMap = new HashMap();
        Map extRtMap = new HashMap();
        String strTmp;
        for(int i=0; i<ids.length; i++) {
        	strTmp = (String)rtMap.get(ids[i]); 
        	if(null == strTmp) {
        		rtMap.put(ids[i], ""+i);        		
        	} else {
        		strTmp += "," + i;
        		rtMap.put(ids[i], strTmp);
        	}
        	chkRes[i] = ResultConstant.CHK_RESULT_CAN_BOOK;
        	chkRtTotal[i] = 0;
        }
        if (0 >= classQtyDatalist.size()) {
            log.error("---------调用房型数量接口出错:classQtyDatalist.size()=0---------");
            return null;
        }
        boolean bFirst = true;
        newdate = new Date();
        int testIndex = 0;
        for (ClassQtyData classQtyData : classQtyDatalist) {
            if (null != classQtyData) {           	                
                if(!StringUtil.isValidStr(classQtyData.getSClassCode())) {
                	for(int i=0; i<ids.length; i++) {
                		for(int j=0; j<nDays; j++) {
                            hkRoomQtyResponse = new HKRoomQtyResponse();
                        	hkRoomQtyResponse.setHotelId(hotelId);
                        	hkRoomQtyResponse.setRet(ResultConstant.CHK_RESULT_ROOM_NO_SCHEDULE);
                            hkRoomQtyResponse.setQty(0);// 房间数量
                            hkRoomQtyResponse.setDayIndex(j); // 日期序号
                            hkRoomQtyResponse.setRoomTypeId(ids[i]);
                            hkRoomQtyResponseList.add(hkRoomQtyResponse);
                		}
                        hkRoomQtyResponse = new HKRoomQtyResponse();
                    	hkRoomQtyResponse.setHotelId(hotelId);
                    	hkRoomQtyResponse.setRet(ResultConstant.CHK_RESULT_HOTEL_NO_SCHEDULE);
                        hkRoomQtyResponse.setQty(0);// 房间数量
                        hkRoomQtyResponse.setDayIndex(0); // 日期序号
                        hkRoomQtyResponse.setRoomTypeId(ids[i]);
                        hkRoomQtyResponseList.add(hkRoomQtyResponse);
                	}
                    return hkRoomQtyResponseList;                   
                } 
                
                if(bFirst) {
                	for(int i=0; i<ids.length; i++) {
                		for(int j=0; j<nDays; j++) {
                            hkRoomQtyResponse = new HKRoomQtyResponse();
                        	hkRoomQtyResponse.setHotelId(hotelId);
                        	hkRoomQtyResponse.setRet(ResultConstant.CHK_RESULT_ROOM_NO_SCHEDULE);
                            hkRoomQtyResponse.setQty(0);// 房间数量
                            hkRoomQtyResponse.setDayIndex(j); // 日期序号
                            hkRoomQtyResponse.setRoomTypeId(ids[i]);
                            hkRoomQtyResponseList.add(hkRoomQtyResponse);
                		}
                        hkRoomQtyResponse = new HKRoomQtyResponse();
                    	hkRoomQtyResponse.setHotelId(hotelId);
                    	hkRoomQtyResponse.setRet(ResultConstant.CHK_RESULT_ROOM_NO_SCHEDULE);
                        hkRoomQtyResponse.setQty(0);// 房间数量
                        hkRoomQtyResponse.setDayIndex(0); // 日期序号
                        hkRoomQtyResponse.setRoomTypeId(ids[i]);
                        hkRoomQtyResponseList.add(hkRoomQtyResponse);
                	}
                	bFirst = false;
                }
                // 芒果房型编码
                String strTmpCls = classQtyData.getSClassCode();
                roomTypeId = (String)extRtMap.get(strTmpCls);
                if(!StringUtil.isValidStr(roomTypeId)) {
//                    roomTypeId = this.getRoomIdMappingByHK(hotelId, strTmpCls);
                	liRt = this.getRoomIdMappingByHK(hotelId, strTmpCls);                    
                    if (liRt.isEmpty()) {
                        continue;
                    }                    
                    roomTypeId = "";
                    for(String rt : liRt) {
                    	roomTypeId += rt + ",";
                    }
                    extRtMap.put(strTmpCls, roomTypeId);
                }
                for(String curRt : roomTypeId.split(",")) {
                    strTmp = (String)rtMap.get(curRt);
                    if(null == strTmp) {
                    	continue;
                    }
                    int dayIndex = DateUtil.getDay(dateFm, DateUtil.toDateByFormat(
                            classQtyData.getSDate(), "yyyyMMdd"));
                    rtIndex = strTmp.split(",");
                    for(int i=0; i<rtIndex.length; i++) {
                    	int nTmp = Integer.parseInt(rtIndex[i]);
                    	int nIndex = nTmp * (nDays + 1) + dayIndex;                	
                        hkRoomQtyResponse = hkRoomQtyResponseList.get(nIndex);
                        if(null == hkRoomQtyResponse) {
                        	continue;
                        }
                        chkRtTotal[nTmp] ++;
                        hkRoomQtyResponse.setRoomTypeId(curRt);// 芒果房型编码
                        hkRoomQtyResponse.setHotelId(hotelId);
                        hkRoomQtyResponse.setDayIndex(dayIndex); // 日期序号
                        cutoffdate = DateUtil.toDateByFormat(classQtyData.getSCutoff(),
                                ResultConstant.DATEFORMAT_CUTOFF);                
                        // 如果当前日间小于截止时间 满足条件,否则跳过
                        if (newdate.before(cutoffdate) && 0 < classQtyData.getNQty()) {
                            hkRoomQtyResponse.setRet(classQtyData.getNRet());
                            hkRoomQtyResponse.setQty(classQtyData.getNQty());// 房间数量
                            hkRoomQtyResponse.setMaxQty(classQtyData.getNMaxQty());
                        } else {
                        	hkRoomQtyResponse.setRet(ResultConstant.CHK_RESULT_ROOM_NO_SCHEDULE);
                        	if(ResultConstant.CHK_RESULT_ROOM_NO_SCHEDULE < chkRes[nTmp]) {
                        		chkRes[nTmp] = ResultConstant.CHK_RESULT_ROOM_NO_SCHEDULE; 
                        	}                    
                            log.error("香港房型编码:" + classQtyData.getSClassCode());
                            log.error("当前时间为:" + DateUtil.toStringByFormat(newdate, "yyyy-MM-dd HH:mm:ss"));
                            log.error("此房间的截止时间:"
                                + DateUtil.toStringByFormat(cutoffdate, "yyyy-MM-dd HH:mm:ss"));
                        }
                    }	
                }                
            } else {
            	//log.info("classQtyData is null, testIndex:" + testIndex);
            }
            //testIndex ++;
        }
        
        for(int i=0; i<ids.length; i++) {
        	if(chkRtTotal[i] >= nDays) {
        		hkRoomQtyResponse = hkRoomQtyResponseList.get(i * (nDays + 1) + nDays);
        		hkRoomQtyResponse.setRet(chkRes[i]);
        	}
        }
        
        return hkRoomQtyResponseList;
    }

    /**
     * @param hotelId
     * @param classQtyDatalist
     * @param roomTypeId
     * @return
     */
    public List<Date> getHKRoomQtyListForWeb(Long hotelId,
			List<ClassQtyData> classQtyDatalist,
			long roomTypeId) {

    	String sClassCode = null;
    	List<Date> liUnbookDate = new ArrayList<Date>();
    	Date newdate = new Date();
        
        for (ClassQtyData classQtyData : classQtyDatalist) {
			if (null == classQtyData) {
				continue;
			}
			String strTmpCls = classQtyData.getSClassCode();
			if (!StringUtil.isValidStr(strTmpCls)) {
				return liUnbookDate;
			}
			// 芒果房型编码
			boolean bMatch = false;
			if (!StringUtil.isValidStr(sClassCode)) {
				List<String> liRt = this.getRoomIdMappingByHK(hotelId,
						strTmpCls);
				if (liRt.isEmpty()) {
					continue;
				}
				for (String rt : liRt) {
					if (("" + roomTypeId).equals(rt)) {
						sClassCode = strTmpCls;
						break;
					}
				}
				bMatch = true;
			} else {
				if (sClassCode.equals(strTmpCls)) {
					bMatch = true;
				}
			}
			if (bMatch) {
                Date cutoffdate = DateUtil.toDateByFormat(classQtyData.getSCutoff(),
                        ResultConstant.DATEFORMAT_CUTOFF);                
                if (newdate.before(cutoffdate) && 0 < classQtyData.getNQty()) {
					liUnbookDate.add(DateUtil.toDateByFormat(classQtyData
							.getSDate(), "yyyyMMdd"));
				}
			}
		}    	        
        return liUnbookDate;
    }
    
    /**
	 * 取得酒店所有房型价格列表 add by shizhongwen 时间:Mar 18, 2009 7:44:01 PM
	 * 
	 * @param hotelId
	 * @param classQtyDatalist
	 * @return
	 */
    public List<HKRoomAmtResponse> getHKRoomAmtList(Long hotelId,
        List<ClassNationAmtData> classNationDatalist, Date dateFm, 
        String roomTypes, int nDays, String childRoomTypes) {
        if (0 >= classNationDatalist.size()) {
            log.error("-------------在调用房型价格出错方法出错: classNationDatalist.size=0---------");
            return null;
        }
        List<HKRoomAmtResponse> hkRoomAmtList = new ArrayList<HKRoomAmtResponse>();
        HKRoomAmtResponse hkRoomAmt = null;
        // String roomTypeId = "";
        float baseAmt = 0f; // 底价
        float saleAmt = 0f; // 销售价
        String[] ids = roomTypes.split("#");
        String[] cids = childRoomTypes.split("#");        
//        int[] chkRes = new int[ids.length];
        int[] chkCrtTotal = new int[cids.length];
        Map rtMap = new HashMap();               
        for(int i=0; i<cids.length; i++) {
        	rtMap.put(cids[i], i);
        	chkCrtTotal[i] = 0;
        }
        boolean bFirst = true;
//        int testIndex = 0;
        for (ClassNationAmtData classNationData : classNationDatalist) {
        	if(!StringUtil.isValidStr(classNationData.getSClassCode())) {      		
        		for(int i=0; i<cids.length; i++) {
        			for(int j=0; j<nDays; j++) {
                        hkRoomAmt = new HKRoomAmtResponse();
                        hkRoomAmt.setRet(ResultConstant.CHK_RESULT_HOTEL_NO_SCHEDULE);
                        hkRoomAmt.setHotelId(hotelId);
                        hkRoomAmt.setRoomTypeId(ids[i]);
                        hkRoomAmt.setChildRoomTypeId(cids[i]);// 子房型
                        hkRoomAmt.setDayIndex(j);
                        hkRoomAmtList.add(hkRoomAmt);
        			}
                    hkRoomAmt = new HKRoomAmtResponse();
                    hkRoomAmt.setRet(ResultConstant.CHK_RESULT_HOTEL_NO_SCHEDULE);
                    hkRoomAmt.setHotelId(hotelId);
                    hkRoomAmt.setRoomTypeId(ids[i]);
                    hkRoomAmt.setChildRoomTypeId(cids[i]);// 子房型                    
                    hkRoomAmtList.add(hkRoomAmt);
        		}
                return hkRoomAmtList;
        	} 
        	if(bFirst) {
        		for(int i=0; i<ids.length; i++) {
        			for(int j=0; j<nDays; j++) {
                        hkRoomAmt = new HKRoomAmtResponse();
                        hkRoomAmt.setRet(ResultConstant.CHK_RESULT_ROOM_NO_SCHEDULE);
                        hkRoomAmt.setHotelId(hotelId);
                        hkRoomAmt.setRoomTypeId(ids[i]);
                        hkRoomAmt.setChildRoomTypeId(cids[i]);// 子房型
                        hkRoomAmt.setDayIndex(j);
                        hkRoomAmtList.add(hkRoomAmt);
        			}
                    hkRoomAmt = new HKRoomAmtResponse();
                    hkRoomAmt.setRet(ResultConstant.CHK_RESULT_ROOM_NO_SCHEDULE);
                    hkRoomAmt.setHotelId(hotelId);
                    hkRoomAmt.setRoomTypeId(ids[i]);
                    hkRoomAmt.setChildRoomTypeId(cids[i]);// 子房型                    
                    hkRoomAmtList.add(hkRoomAmt);
        		}
        		bFirst = false;
        	}
            // 将香港价格类型编码转换为芒果子房型编码
            List<String[]> childRoomTypeIdList = this.getPriceMappingByHK(hotelId, classNationData
                .getSClassCode(), classNationData.getSNation());

            if (0 >= childRoomTypeIdList.size()) {
                log.error("-------------在调用房型价格出错方法出错:没有通过香港价格类型编码取得芒果子房型编码----------");
                log.error("芒果网酒店ID=" + hotelId + ", 芒果子房型childRoomTypeIdList.size=" + childRoomTypeIdList.size());
                log.error("香港房型id=" + classNationData.getSClassCode() + ",香港价格类型id="
                    + classNationData.getSNation());
                continue;
            }
            for (String[] priceTypeId : childRoomTypeIdList) {
                if (null == priceTypeId[0]) {
                	continue;
                }
                Integer obj = (Integer)rtMap.get(priceTypeId[0]);
                if(null == obj) {
                	continue;
                }
                int crtIndex = obj.intValue();
                String sDate = classNationData.getSDate();
                Date curDate = dateFm;
                if (StringUtil.isValidStr(sDate)) {
                    curDate = DateUtil.toDateByFormat(sDate.substring(0, sDate.indexOf(' ')),
                        ResultConstant.DATEFOMAT_MANGO);
                }
                int dayIndex = DateUtil.getDay(dateFm, curDate);                
                int nIndex = crtIndex * (nDays + 1) + dayIndex;
                hkRoomAmt = hkRoomAmtList.get(nIndex);
                if(null == hkRoomAmt) {
                	continue;
                }
                chkCrtTotal[crtIndex] ++;
            	            	
                // priceTypeId ,芒果子房型编码                
                hkRoomAmt.setRet(classNationData.getNRet());
                hkRoomAmt.setHotelId(hotelId);
//                hkRoomAmt.setMessage(classNationData.getSMessage());
                hkRoomAmt.setRoomTypeId(priceTypeId[1]);
                hkRoomAmt.setChildRoomTypeId(priceTypeId[0]);// 子房型
                hkRoomAmt.setChildRoomTypeIdDesc(classNationData.getSNationDesc());
                /*
                 * add by shizhongwen 2009-04-21 注:classNationData.getNBaseAmt()已为香港酒店的底价
                 * (中科已不提供这个数据),nListAmt; 中科提供的他们底价(现在芒果网取这个作为底价) modify by shizhongwen 2009-04-28
                 * 注: 中科方面又改过来 classNationData.getNBaseAmt()为中科提供的底价, nListAmt为销售价
                 */
                baseAmt = classNationData.getNBaseAmt();// nBaseAmt已改为中科提供的他们底价

                if (0 > baseAmt) {
                    baseAmt = 0f;
                    log.error("----------从中旅取的底价<0--------");
                    log.error("此时的芒果网酒店ID=" + hotelId + ", 芒果房型id=" + priceTypeId[1]
                        + ",芒果子房型childRoomTypeIdList.size=" + childRoomTypeIdList.size());
                    log.error("香港房型id=" + classNationData.getSClassCode() + ",香港价格类型id="
                        + classNationData.getSNation());
                }

                saleAmt = this.getHKIncreasePrice(baseAmt, hotelId);

                // modify by chenkeming@2010-12-24 中旅酒店修改加幅策略,如果芒果没设置加幅则采用中旅销售价                
                if (0.1 > saleAmt) {
                    saleAmt = classNationData.getNListAmt();
                }
                
                hkRoomAmt.setBaseAmt(baseAmt);// 底价
                hkRoomAmt.setListAmt(saleAmt);// 销信价

                // 通过汇率转换为RMB
                String exchange = "";
                if (null != CurrencyBean.rateMap) {
                    exchange = CurrencyBean.rateMap.get(CurrencyBean.HKD);
                } else {
                    exchange = "1";
                }
                double dbExchange = StringUtil.getStrTodouble(exchange);
                if (0 == Double.compare(0.0, dbExchange)) {
                    dbExchange = 1;
                    log.error("CurrencyBean.rateMap.get(CurrencyBean.HKD)"
                        + " result = 0, set default rate is 1");
                }

                // v2.8 处理汇率转换 modify by chenkeming
                hkRoomAmt.setRmbBaseAmt(BigDecimal.valueOf(baseAmt * dbExchange).setScale(
                // 生产QC743 通过汇率转换为RMB, 改为逢一进十 by chenkeming 2009-08-13
                    0, BigDecimal.ROUND_UP).floatValue());
                hkRoomAmt.setRmbListAmt(BigDecimal.valueOf(saleAmt * dbExchange).setScale(
                // 生产QC743 通过汇率转换为RMB, 改为逢一进十 by chenkeming 2009-08-13
                    0, BigDecimal.ROUND_UP).floatValue());

                hkRoomAmt.setDate(DateUtil.dateToString(curDate));
                hkRoomAmt.setDayIndex(dayIndex);                
            }
//            testIndex ++;
        }
        for(int i=0; i<cids.length; i++) {
        	if(chkCrtTotal[i] >= nDays) {
        		hkRoomAmt = hkRoomAmtList.get(i * (nDays + 1) + nDays);
        		hkRoomAmt.setRet(ResultConstant.CHK_RESULT_CAN_BOOK);
        	}
        }
        
        return hkRoomAmtList;
    }

    /**
     * 根据芒果酒店id，芒果房型id,List<NationAmtData>封装HKRoomAmtResponse add by shizhongwen 时间:Mar 23, 2009
     * 5:31:31 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @param nationDatalist
     * @return
     */
    public List<HKRoomAmtResponse> getHKRoomNationAmtList(Long hotelId, long roomTypeId,
        List<NationAmtData> nationDatalist) {
        List<HKRoomAmtResponse> hkRoomAmtList = new ArrayList<HKRoomAmtResponse>();
        HKRoomAmtResponse hkRoomAmt = null;
        int i = 0;
        String priceTypeId = "";
        float baseAmt = 0f;
        float saleAmt = 0f;
        if (0 >= nationDatalist.size()) {
            log.error("-------------nationDatalist.size=0---------");
            return null;
        }
        for (NationAmtData nationData : nationDatalist) {
            hkRoomAmt = new HKRoomAmtResponse();
            hkRoomAmt.setRet(nationData.getNRet());
            hkRoomAmt.setHotelId(hotelId);
            hkRoomAmt.setMessage(nationData.getSMessage());
            hkRoomAmt.setDayIndex(i);
            // 将香港价格类型编码转换为芒果子房型编码
            priceTypeId = this
                .getPriceMappingByHK(hotelId, roomTypeId, nationData.getSNationCode());

            if (null == priceTypeId) {
                priceTypeId = "";
            }
            hkRoomAmt.setRoomTypeId(String.valueOf(roomTypeId));
            hkRoomAmt.setChildRoomTypeId(priceTypeId);// 子房型
            hkRoomAmt.setChildRoomTypeIdDesc(nationData.getSNationDesc());

            baseAmt = nationData.getNBaseAmt();// nBaseAmt已改为中科提供的他们底价
            try {
                saleAmt = this.getHKIncreasePrice(baseAmt, hotelId);
            } catch (Exception e) {
                log.error("------香港中科加幅区间有误,现在的底价为:" + baseAmt + " ,请增加正确的增幅区域空间----------");
            }

            if (0 > baseAmt) {
                baseAmt = 0f;
            }
            
            // modify by chenkeming@2010-12-24 中旅酒店修改加幅策略,如果芒果没设置加幅则采用中旅销售价                
            if (0.1 > saleAmt) {
                saleAmt = nationData.getNListAmt();
            }
            
            hkRoomAmt.setBaseAmt(baseAmt);// 底价
            hkRoomAmt.setListAmt(saleAmt);// 销信价

            // 通过汇率转换为RMB
            String exchange = "";
            if (null != CurrencyBean.rateMap) {
                exchange = CurrencyBean.rateMap.get(CurrencyBean.HKD);
            } else {
                exchange = "1";
            }

            double dbExchange = StringUtil.getStrTodouble(exchange);
            if (0 == Double.compare(0.0, dbExchange)) {
                dbExchange = 1;
                log.error("CurrencyBean.rateMap.get(CurrencyBean.HKD)"
                    + " result = 0, set default rate is 1");
            }

            // v2.8 处理汇率转换 modify by chenkeming
            hkRoomAmt.setRmbBaseAmt(BigDecimal.valueOf(baseAmt * dbExchange).setScale(
            // 生产QC743 通过汇率转换为RMB, 改为逢一进十 by chenkeming 2009-08-13
                0, BigDecimal.ROUND_UP).floatValue());
            hkRoomAmt.setRmbListAmt(BigDecimal.valueOf(saleAmt * dbExchange).setScale(
            // 生产QC743 通过汇率转换为RMB, 改为逢一进十 by chenkeming 2009-08-13
                0, BigDecimal.ROUND_UP).floatValue());

            hkRoomAmt.setDate(DateUtil.dateToString(DateUtil.toDateByFormat(nationData.getSDate(),
                ResultConstant.DATEFOMAT_MANGO)));
            hkRoomAmtList.add(hkRoomAmt);
            i++;
        }
        return hkRoomAmtList;
    }

    /**
     * 更新HK酒店价格 add by shizhongwen 时间:Mar 23, 2009 3:02:59 PM
     * 
     * @param hotelId
     * @param baseAmt
     * @param listAmt
     * @param start_date
     * @param childRoomTypeId
     * @param roomTypeId
     * @return String 0:成功,-1:失败
     */
    public String updateHKHotelPrice(Long hotelId, float baseAmt, float listAmt, String start_date,
        long childRoomTypeId, long roomTypeId) {
        return exDao.updateHKHotelPrice(hotelId, baseAmt, listAmt, start_date, childRoomTypeId,
            roomTypeId);
    }
    
    /**
     * B2B同步价格时候 将价格写入数据库 add by xuyiwen 2011-1-18
     * @param items
     * @param hotelId
     * @param priceTypeId
     * @param inDate
     * @param outDate
     */
    public void  updateB2BCTSHotelPrice(List<QueryHotelForWebSaleItems> items,long hotelId,long priceTypeId,long roomTypeId){
    	for(QueryHotelForWebSaleItems item:items){
    		updateHKHotelPrice(hotelId,(float)item.getBasePrice(),(float)item.getSalePrice(),DateUtil.dateToString(item.getFellowDate()),priceTypeId,roomTypeId);
    	}
    }

    /**
     * 根据渠道、酒店ID，芒果房型id、香港子房型id获取芒果子房型id add by shizhongwen 时间:Mar 23, 2009 5:17:52 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @param hkchildRoomTypeId
     * @return
     */
    public String getPriceMappingByHK(long hotelId, long roomTypeId, String hkchildRoomTypeId) {

        ExMapping em = imappingService.getMapping(CHANNEL_CTS, hotelId, roomTypeId,
            hkchildRoomTypeId);
        if (null != em) {
            return em.getPriceTypeId();
        } else {
            log.error("根据渠道、酒店id、香港房型id获取芒果网的房型编码 为空");
            return null;
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
        return exDao.updateHKhotelRoom(hotelId, qty, roomTypeId, datetime);
    }

    /**
     * 根据酒店ID，房型id、子房型id获取直连香港酒店价格id add by shizhongwen 时间:Mar 25, 2009 4:20:59 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @return
     */
    public String getHKChildRoomId(long hotelId, long roomTypeId, long childRoomTypeId) {
        ExMapping em = imappingService.getMapping(CHANNEL_CTS, hotelId, roomTypeId,
            childRoomTypeId);
        if (null != em) {
            return em.getRateplancode();
        } else {
            log.error("根据渠道、酒店id、香港房型id获取芒果网的房型编码 为空");
            return null;
        }
    }

    /**
     * 根据增幅规则获取新的销售价 add by shizhongwen 时间:Apr 21, 2009 2:39:07 PM
     * 
     * @param baseAmt
     * @return
     * @throws Exception
     */
    public float getHKIncreasePrice(float baseAmt) {
        float price = 0f;
        Map<String, List<HtlIncreasePrice>> increaseMap = IncreaseBean.increaseMap;
        List<HtlIncreasePrice> htlpricelist = increaseMap.get(IncreaseBean.CTS);
        if (null == htlpricelist || 0 >= htlpricelist.size()) {
            log.error("香港中科加幅设置为空");
            log.error("现在销售价=底价*(1+10%)");
            return baseAmt * (1.1f);
        }

        boolean tag = false;// 判断底价是否属于于哪个价格区间
        for (HtlIncreasePrice htlincreaseprice : htlpricelist) {
            if (baseAmt >= htlincreaseprice.getGreaterThan()
                && baseAmt < htlincreaseprice.getSmallerThan()) {
                tag = true;
                // 加幅类型 1:金额 2:百分比
                log.info("------------htlincreaseprice.getIncreaseType():"
                    + htlincreaseprice.getIncreaseType());
                log.info("------------htlincreaseprice.getAmount()" + htlincreaseprice.getAmount());
                if (htlincreaseprice.getIncreaseType().equals(JINCREASETYPE)) {
                    // price=baseAmt+(float)htlincreaseprice.getAmount();
                    price = baseAmt + Double.valueOf(htlincreaseprice.getAmount()).floatValue();
                }
                if (htlincreaseprice.getIncreaseType().equals(BINCREASETYPE)) {
                    // price=baseAmt*(float)(1+htlincreaseprice.getAmount());
                    price = baseAmt
                        * (1f + Double.valueOf(htlincreaseprice.getAmount()).floatValue());
                }
                break;
            }
        }

        // 对底价加幅后采用逢一进十
        if (0 < price - (int) price) {
            price = (float) ((int) price + 1);
        }
        if (!tag) {
            log.error("------香港中科加幅区间有误,现在的底价为:" + baseAmt + " ,请增加正确的增幅区域空间----------");
            log.error("现在销售价=底价*(1+10%)");
            return baseAmt * (1.1f);
        }
        return price;
    }

    /**
     * 
     * 根据增幅规则和酒店id获取新的销售价 hotel 2.9.2 add by chenjiajie 2009-08-04
     * 
     * @param baseAmt
     * @param hotelId
     * @return
     */
    public float getHKIncreasePrice(float baseAmt, Long hotelId) {
        float price = 0f;
        // Map<String, List<HtlIncreasePrice>> increaseMap = IncreaseBean.increaseMap;
        List<HtlIncreasePrice> htlpricelist = IncreaseBean.increaseCtsMap.get(hotelId);
        if (null == htlpricelist || 0 >= htlpricelist.size()) {
            /*log.error("香港中科加幅设置为空");
            log.error("现在销售价=底价*(1+10%)");
            return baseAmt * (1.1f);*/
        	// modify by chenkeming@2010-12-24 中旅酒店修改加幅策略
            return 0;
        	/*if(900 <= baseAmt) {
        		return (baseAmt + 300);
        	} else {
        		return (baseAmt + 100);
        	}*/
        }

        boolean tag = false;// 判断底价是否属于于哪个价格区间
        for (HtlIncreasePrice htlincreaseprice : htlpricelist) {
            // 取得该酒店的加幅数据，再进行下一轮判断
            //if (htlincreaseprice.getHotelId().equals(hotelId)) {
                if (baseAmt >= htlincreaseprice.getGreaterThan()
                    && baseAmt < htlincreaseprice.getSmallerThan()) {
                    tag = true;
                    // 加幅类型 1:金额 2:百分比
                    log.info("------------htlincreaseprice.getIncreaseType():"
                        + htlincreaseprice.getIncreaseType());
                    log.info("------------htlincreaseprice.getAmount()"
                        + htlincreaseprice.getAmount());
                    if (htlincreaseprice.getIncreaseType().equals(JINCREASETYPE)) {
                        // price=baseAmt+(float)htlincreaseprice.getAmount();
                        price = baseAmt + Double.valueOf(htlincreaseprice.getAmount()).floatValue();
                    }
                    if (htlincreaseprice.getIncreaseType().equals(BINCREASETYPE)) {
                        // price=baseAmt*(float)(1+htlincreaseprice.getAmount());
                        price = baseAmt
                            * (1f + Double.valueOf(htlincreaseprice.getAmount()).floatValue());
                    }
                    break;
                }
            // }
        }

        // 对底价加幅后采用逢一进十
        if (0 < price - (int) price) {
            price = (float) ((int) price + 1);
        }
        if (!tag) {
            log.error("------香港中科加幅区间有误,现在的底价为:" + baseAmt + " ,请增加正确的增幅区域空间----------");
            log.error("现在销售价=底价*(1+10%)");
            // modify by chenkeming@2010-12-24 中旅酒店修改加幅策略
            return 0;
        	/*if(900 <= baseAmt) {
        		return (baseAmt + 300);
        	} else {
        		return (baseAmt + 100);
        	}*/
        }
        return price;
    }

    public IExDao getExDao() {
        return exDao;
    }

    public void setExDao(IExDao exDao) {
        this.exDao = exDao;
    }
    
    public IExMappingService getImappingService() {
        return imappingService;
    }

    public void setImappingService(IExMappingService imappingService) {
        this.imappingService = imappingService;
    }

}
