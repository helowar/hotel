package com.mangocity.tmc.service.impl;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.tmc.persistence.view.HotelInfoTmc;
import com.mangocity.tmc.persistence.view.HotelQueryConditionTmc;
import com.mangocity.tmc.service.IHotelQueryService;
import com.mangocity.tmc.service.assistant.HotelInfo;
import com.mangocity.tmc.service.assistant.HotelQueryCondition;
import com.mangocity.tmc.service.assistant.HotelQueryPageInfo;
import com.mangocity.tmc.service.assistant.RoomInfo;
import com.mangocity.tmc.service.assistant.RoomType;
import com.mangocity.tmc.service.assistant.SaleItem;
import com.mangocity.tmc.service.constant.HotelOrderConstans;
import com.mangocity.tmc.service.util.QueryAssist;
import com.mangocity.tmccommon.persistence.Company;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.hotel.constant.PayMethod;
import com.mangocity.util.log.MyLog;

public class HotelQueryService implements IHotelQueryService {
	private static final MyLog log = MyLog.getLogger(HotelQueryService.class);
	private DAOIbatisImpl queryDao;

	HotelQueryPageInfo hotelQueryPageInfo;// 将他定义成了一个属性了,可以直接使用
    
    private QueryAssist queryAssist;

	/**
	 * 用于对酒店按销售价格进行排序,点击排序时由action调用此方法
	 * param 传入排序方式
	 * 
	 */
	public HotelQueryPageInfo sortHotel(HotelQueryCondition hotelQueryCondition,String aOrder,String aOrderType) throws Exception {
		log.info("====comehere=====");
		List allHotelList = null;
		if (this.getHotelQueryPageInfo() != null)// 查询到有值
		{

			allHotelList = hotelQueryPageInfo.getAllHotelList();// 获得所有酒店集合
			
			//modifyby chunshen.gong 按推荐酒店排序,主推类型(特推酒店1，金牌酒店2，银牌酒店3,黑4)
			if (aOrderType !=null && !aOrderType.equals("commendType")){
				List bothHotelList = new ArrayList();// 共有酒店集合
				List tmcHotelList = new ArrayList();// 协议酒店集合
				List hotelHotelList = new ArrayList();// 本部酒店集合
				// 对酒店类型进行判断分别加进相应集合
				for (int i = 0; i < allHotelList.size(); i++) {
					HotelInfo objHotel = (HotelInfo)allHotelList.get(i);
					if (objHotel.getHotelKind().equals("共有酒店")) {
						bothHotelList.add(objHotel);
					} else if (objHotel.getHotelKind().equals("协议酒店")) {
						tmcHotelList.add(objHotel);
					} else if (objHotel.getHotelKind().equals("本部酒店")) {
						hotelHotelList.add(objHotel);
					}
				}
				if (bothHotelList.size() > 1) {
					bothHotelList = this.getAfterSort(bothHotelList, aOrder, aOrderType);//排序
				}
				if (tmcHotelList.size() > 1) {
					tmcHotelList = this.getAfterSort(tmcHotelList, aOrder, aOrderType);
				}
				if (hotelHotelList.size() > 1) {
					hotelHotelList = this.getAfterSort(hotelHotelList, aOrder, aOrderType);
				}
				
				allHotelList.clear();//清空原对象
							
				allHotelList.addAll(tmcHotelList);
				allHotelList.addAll(bothHotelList);
				allHotelList.addAll(hotelHotelList);
			}else {
				/**
				 * begin addby chunshen.gong 2008-12-15 按酒店类型排序
				 * 酒店查询结果按特推酒店1，金牌酒店2，银牌酒店排序3，其他的
				 * 酒店内部再按酒店最低价格升序排列。
				 */
				ComparatorHotelCommendType comparatorCommendType= new ComparatorHotelCommendType();
				Collections.sort(allHotelList,comparatorCommendType);								
				//end chunshen.gong
			}

		}
		
		hotelQueryPageInfo.setAllHotelList(allHotelList);//注意:开始写错了啊
		hotelQueryPageInfo.setSort(aOrder);//根据传来的排序方式设置值
		hotelQueryPageInfo.setSortType(aOrderType);//根据传来的排序类别设置值
		return this.queryPerpageHotel(hotelQueryCondition,hotelQueryPageInfo.getPageNo());
	
	}

	/**
	 * 用于进行对集合的排序
	 * 
	 * @param list
	 *            传入需排序的酒店信息集合
	 * @param aOrder
	 *            传入要排序的方式(升或降)
	 * @param aOrderTYpe
	 *            传入要排序的类别(价格或者星级)
	 * @return 返回排序后的集合
	 */
	private List getAfterSort(List hotelList, String aOrder,String aOrderType) {
		for (int i = 0; i < hotelList.size() - 1; i++)
			for (int j = i + 1; j < hotelList.size(); j++) {
				HotelInfo a = (HotelInfo) hotelList.get(i);
				HotelInfo b = (HotelInfo) hotelList.get(j);
				if (aOrder.equals("ascending") && aOrderType.equals("price"))// 升序
				{
					if (Double.parseDouble(a.getLowPrice()) > Double
							.parseDouble(b.getLowPrice())) {

						hotelList.set(i, b);
						hotelList.set(j, a);
					}
				} else if (aOrder.equals("descending") && aOrderType.equals("price"))// 降序
				{
					if (Double.parseDouble(a.getLowPrice()) < Double
							.parseDouble(b.getLowPrice())) {

						hotelList.set(i, b);
						hotelList.set(j, a);
					}
				} else if (aOrder.equals("ascending") && aOrderType.equals("star"))// 升序
				{
					if (Double.parseDouble(a.getHotelStar()) < Double
							.parseDouble(b.getHotelStar())) {

						hotelList.set(i, b);
						hotelList.set(j, a);
					}
				} else if (aOrder.equals("descending") && aOrderType.equals("star"))// 降序
				{
					if (Double.parseDouble(a.getHotelStar()) > Double
							.parseDouble(b.getHotelStar())) {

						hotelList.set(i, b);
						hotelList.set(j, a);
					}
				}
			}
		return hotelList;
	}
	
	/**
	 * 缺省排序
	 * 
	 * @param list
	 *            传入需排序的酒店信息集合
	 * @return 返回排序后的集合
	 */
	private List getDefaultSort(List hotelList) {
		for (int i = 0; i < hotelList.size() - 1; i++){
			for (int j = i + 1; j < hotelList.size(); j++) {
				HotelInfo a = (HotelInfo) hotelList.get(i);
				HotelInfo b = (HotelInfo) hotelList.get(j);
				//第二关键字价格缺省降序排列
				if (Double.parseDouble(a.getLowPrice()) < Double
							.parseDouble(b.getLowPrice())) {
						hotelList.set(i, b);
						hotelList.set(j, a);
				}else if(Double.parseDouble(a.getLowPrice()) == Double
						.parseDouble(b.getLowPrice())) {
					if (Double.parseDouble(a.getHotelStar()) > Double
							.parseDouble(b.getHotelStar())) {
						hotelList.set(i, b);
						hotelList.set(j, a);
					}					
				}				
			}
		}
		return hotelList;
	}

    /**
     * CC查询协议酒店
     * 
     * @author chenkeming Sep 28, 2009 
     * @param hotelQueryCondition
     * @param member
     * @return
     */
    public List<HotelInfoTmc> queryHotelForCC(
            HotelQueryConditionTmc condTmc, MemberDTO member) {
        HotelQueryCondition cond = queryAssist.queryCondMango2Tmc(condTmc, member);
        List<HotelInfoTmc> liRes = new ArrayList<HotelInfoTmc>();
        try {
            HotelQueryPageInfo pageInfo = queryHotel(cond, condTmc.getPageNo());
            liRes = queryAssist.PageInfo2Lst(pageInfo);
        } catch(Exception e) {
            log.error(e);            
        }
        
        return liRes;
    }
    
    /**
     * 网站查询协议酒店
     * 
     * @author chenkeming Sep 29, 2009
     * @param condWeb
     * @param companyCd
     * @param companyId
     * @return
     */
    public HotelPageForWebBean queryHotelForWeb(
            QueryHotelForWebBean condWeb, String companyCd, Long companyId) {
        HotelPageForWebBean res = new HotelPageForWebBean();
        HotelQueryCondition cond = queryAssist.queryCondWeb2Tmc(condWeb, 
                companyCd, companyId);
        try {
            HotelQueryPageInfo pageInfo = queryHotel(cond, condWeb.getPageIndex());
            getOtherHotelInfoForWeb(pageInfo);
            res = queryAssist.PageInfo2PageForWebBean(pageInfo, 
                    cond);
        } catch(Exception e) {
            log.error(e);           
        }
        
        return res;
    }
    
    /**
     * 网站查协议酒店,查出分页结果后，继续查酒店的其他信息
     * @author chenkeming 
     * @param pageInfo
     */
    private void getOtherHotelInfoForWeb(HotelQueryPageInfo pageInfo) {
        List li = pageInfo.getHotelList();
        if(null != li && 0 < li.size()) {
            Map params = new HashMap();
            for(int i=0; i<li.size(); i++) {
                HotelInfo hotelInfo = (HotelInfo)li.get(i);
                params.put("hotelId", Long.valueOf(hotelInfo.getHotelId()));
                
                // 酒店logo图片
                String hotelLogo = (String) queryDao.queryForObject("queryHotelLogo", 
                        params);                
                hotelInfo.setHotelLogo(hotelLogo);
            }
        }
    }
    
	/**
	 * 获得酒店信息
	 */
	public HotelQueryPageInfo queryHotel (
			HotelQueryCondition hotelQueryCondition, int pageNo)
			throws Exception {
		//酒店查询结果分页信息
		hotelQueryPageInfo = new HotelQueryPageInfo();// modify by xuyun
        hotelQueryPageInfo.setPageSize(hotelQueryCondition.getPageSize());
		int pageSize = hotelQueryPageInfo.getPageSize();// 获得每页多少条记录
		hotelQueryPageInfo.setPageNo(pageNo);// 第几页
		
//		List allHotelList = new ArrayList();//存储查询出的所有的酒店(未分页的)
	

		Map params = MyBeanUtil.describe(hotelQueryCondition);// 封装了传来的参数

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(DateUtil.getDate(hotelQueryCondition.getJoinDate()));// 转换入住日期
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(DateUtil.getDate(hotelQueryCondition.getLeaveDate())); // 转换离店日期

		// 根据条件查询出所有的本部酒店(可能含协议酒店)id列表 --记录共有酒店

		// 根据条件查询出所有的协议酒店id列表
		List tmcResults = queryDao.queryForList("queryTmcHotelId", params);
        int size = tmcResults.size();// 查询出的所有酒店的ID集合,分页
        int totalPage = size / pageSize;
        int residue = size % pageSize;
        if (residue > 0) {
            totalPage += 1;
        } else {
            residue = pageSize;
        }
        hotelQueryPageInfo.setTotalRecord(size);// 总记录数(酒店数)
        hotelQueryPageInfo.setTotalPage(totalPage);// (总页数)
        hotelQueryPageInfo.setResidue(residue);//(模)xuyun
        hotelQueryPageInfo.setPageNo(pageNo);
        
		// 打上类别标记
        // 共有部分也只需要查询出协议房价
        putResultKind(tmcResults, "协议酒店");
           //原分页位置
		
		List results = null;// 获得分页后的子集合(酒店ID)
		if (size > 0) {
			if (pageNo < totalPage) {
                results = tmcResults.subList((pageNo - 1) * pageSize, pageNo
						* pageSize);
			} else {
                results = tmcResults.subList((pageNo - 1) * pageSize, (pageNo - 1)
						* pageSize + residue);
			}
		}
        
//		List bothHotelList = new ArrayList();// 共有酒店集合modify by xuyun
		List tmcHotelList = new ArrayList();// 协议酒店集合
		
		try {
			if (results != null) {//由curList 改为result了,要组装出所有的酒店信息
				for (int i = 0; i < results.size(); i++) {
					Map subParam = new HashMap();
					Map item = (HashMap) results.get(i);
					subParam.put("hotelId", item.get("HOTEL_ID"));//只是一个个的酒店ID
					subParam.put("joinDate", hotelQueryCondition.getJoinDate());//从查询条件中来
					subParam.put("leaveDate", hotelQueryCondition
							.getLeaveDate());
					subParam.put("priceLowQry", hotelQueryCondition
							.getPriceLowQry());
					subParam.put("priceHighQry", hotelQueryCondition
							.getPriceHighQry());
					subParam.put("companyID", hotelQueryCondition
							.getCompanyID());
					HotelInfo hotel = null;//接收组装的酒店信息

					List tmcInfo = queryDao.queryForList("queryTmcHotelInfo",
                            subParam);// 
                    hotel = makeHotelInfo_Tmc(tmcInfo, calStart, calEnd);
                    if (hotel != null) {
                        tmcHotelList.add(hotel);//modify by xy from hotelList
                    }

				}
			}
//			if (bothHotelList.size() > 1) {//缺省 价格降序排列 星级降序排列
//				bothHotelList = this.getDefaultSort(bothHotelList);
//			}
			if (tmcHotelList.size() > 1) {
				tmcHotelList = this.getDefaultSort(tmcHotelList);
			}
		} catch (Exception e) {
			log.error(e);
		}		
		//获得所有的查询出的酒店集合
//		allHotelList.addAll(tmcHotelList);
//		allHotelList.addAll(bothHotelList);		

//		hotelQueryPageInfo.setAllHotelList(allHotelList);//modify
        hotelQueryPageInfo.setHotelList(tmcHotelList);


		//hotelQueryPageInfo.setSort("ascending");//默认查出是升序		
//		return this.queryPerpageHotel(hotelQueryCondition,pageNo);
		return hotelQueryPageInfo;	
	}
	
	/**
	 * 专门进行分页,点分页时就通过action调用此方法
	 */
	public HotelQueryPageInfo queryPerpageHotel(
            HotelQueryCondition hotelQueryCondition, int pageNo)
            throws Exception {
        List curList = null;
        if (this.getHotelQueryPageInfo() != null) {
            if (this.getHotelQueryPageInfo().getAllHotelList().size() > 0) {
                if (pageNo < this.getHotelQueryPageInfo().getTotalPage()) {
                    curList = this.getHotelQueryPageInfo().getAllHotelList()
                            .subList(
                                    (pageNo - 1)
                                            * this.getHotelQueryPageInfo()
                                                    .getPageSize(),
                                    pageNo
                                            * this.getHotelQueryPageInfo()
                                                    .getPageSize());
                } else {
                    curList = this.getHotelQueryPageInfo().getAllHotelList()
                            .subList(
                                    (pageNo - 1)
                                            * this.getHotelQueryPageInfo()
                                                    .getPageSize(),
                                    (pageNo - 1)
                                            * this.getHotelQueryPageInfo()
                                                    .getPageSize()
                                            + this.getHotelQueryPageInfo()
                                                    .getResidue());//必须减1才不报错啊why(数据查询有问题)
                }
            }
            /*List curList = null;// 获得分页后的子集合(酒店ID)这是原分页代码以参照
             if (size > 0) {
             if (pageNo < totalPage) {
             curList = results.subList((pageNo - 1) * pageSize, pageNo
             * pageSize);
             } else {
             curList = results.subList((pageNo - 1) * pageSize, (pageNo - 1)
             * pageSize + residue);
             }
             }*/
        }
        this.getHotelQueryPageInfo().setHotelList(curList);//设置每页的集合  
        this.getHotelQueryPageInfo().setPageNo(pageNo);
        return this.getHotelQueryPageInfo();

    }
	

	
	
	/**
	 * 组装协议酒店信息
	 * 
	 * @param tmcInfo
	 * @return
	 */
	private HotelInfo makeHotelInfo_Tmc(List tmcInfo, Calendar calStart,
			Calendar calEnd) {

		if (tmcInfo == null || tmcInfo.size() == 0) {
			return null;
		}
		HotelInfo ret = new HotelInfo();
		String roomTypeId = "";
		String childRoomTypeId = "";
		String payMethod = "";
		String quotaType = "";// 配额类型
		String priceDate = "";

		String roomTypeId_pre = "";
		String childRoomTypeId_pre = "";
		String payMethod_pre = "";
		String quotaType_pre = "";
		String priceDate_pre = "";

		String firstDate = "";
		double priceLow = 0d;
		double priceHigh = 0d;

		RoomType roomType = null;//用来组装房型配额类型等信息
		SaleItem saleItem = null, saleItemPre = null;
		RoomInfo roomInfo = null, roomInfoPre = null;

		Calendar calTmp = (Calendar) calStart.clone();
        
        boolean bNewPre = false;

		for (int i = 0; i < tmcInfo.size(); i++) {
			Map item = (HashMap) tmcInfo.get(i);
			// 酒店基本信息
			if (i == 0) {
				ret.setHotelId((item.get("HOTEL_ID")).toString());
				ret.setCity((String) item.get("CITY"));
				ret.setHotelChnName((String) item.get("CHN_NAME"));
				ret.setHotelEngName((String) item.get("ENG_NAME"));
				ret.setHotelKind("协议酒店");// 类别
				ret.setHotelStar((String) item.get("HOTEL_STAR"));
				ret.setHotelType((String) item.get("HOTEL_TYPE"));// 类型?
				ret.setChnAddress((String) item.get("CHN_ADDRESS"));
				ret.setEngAddress((String) item.get("ENG_ADDRESS"));
                String strIntro = (String) item.get("AUTO_INTRODUCE");
                String strReserv = (String)item.get("RESERVEITEM");
				ret.setHotelIntroduce((StringUtil.isValidStr(strIntro) ? strIntro : "") + 
                        (StringUtil.isValidStr(strReserv) ? ("<br><br>预订提示:"
                        + strReserv) : ""));//酒店简介            
				ret.setCancelModifyStr(strReserv);
				ret.setClueInfo((String) item.get("ALERT_MESSAGE"));
				ret.setAssureInfo("");
				ret.setTmcAssureInfo("");
				ret.setSaleInfo("");
				ret.setCancelMessage("");
				ret.setZone((String) item.get("ZONE"));
				ret.setBizZone((String) item.get("BIZ_ZONE"));
				// 加入币种xy
				ret.setCurrency("RMB");// 协议酒店只有RMB
                
                // 接机服务
                String strService = (String) item.get("FREE_SERVICE");
                String strMixture = (String) item.get("MEAL_MIXTURE");
                if(StringUtil.isValidStr(strService) && 0 <= strService.indexOf("13")
                        || StringUtil.isValidStr(strMixture) && 0 <= strMixture.indexOf("43")) {
                    ret.setForPlane(1);
                } else {
                    ret.setForPlane(0);
                }

                // 健身设施
                if(StringUtil.isValidStr(strService) && 0 <= strService.indexOf("1,")) {
                    ret.setForFreeGym(1);
                } else {
                    ret.setForFreeGym(0);
                }
                
                // 停车场
                if(StringUtil.isValidStr(strService) && 0 <= strService.indexOf("4")) {
                    ret.setForFreeStop(1);
                } else {
                    ret.setForFreeStop(0);
                }           
                
                // 游泳池
                if(StringUtil.isValidStr(strService) && 0 <= strService.indexOf("2")) {
                    ret.setForFreePool(1);
                } else {
                    ret.setForFreePool(0);                    
                }                   
			}

			roomTypeId = (item.get("ROOMTYPEID")).toString();
			childRoomTypeId = "";
			payMethod = "pre";
			quotaType = "";
			priceDate = DateUtil.dateToString((Date) item.get("PRICEDATE"));

			// 新的房型和子房型开始
			if (!(roomTypeId + childRoomTypeId).equals(roomTypeId_pre
					+ childRoomTypeId_pre)) {
				roomType = new RoomType();
				roomType.setRoomTypeId(roomTypeId);
				roomType.setChildRoomTypeId(childRoomTypeId);
				roomType.setRoomTypeName((String) item.get("ROOMNAME"));
				roomType.setChildRoomTypeName("标准");
                
                // 含早信息
                BigDecimal breakfastNum = (BigDecimal)item.get("BREAKFASTNUM");
                roomType.setBreakfastNum(null != breakfastNum ? breakfastNum.intValue() : 0);
                
                // 宽带信息
                String strEqu = (String)item.get("ROOM_EQUIPMENT");
                roomType.setRoomEquipment(strEqu);
                if(StringUtil.isValidStr(strEqu) && 0 <= strEqu.indexOf("21")) {
                    strEqu = (String)item.get("INCLUDENET");
                    if("Y".equals(strEqu)) {
                        roomType.setNet("免费");
                    } else {
                        roomType.setNet("收费");
                    }
                } else {
                    roomType.setNet("无");
                }
                
                // 加床数
                breakfastNum = (BigDecimal)item.get("ADD_BED_QTY");
                roomType.setAddBedQty(null != breakfastNum ? 
                        breakfastNum.intValue() : 0);
                
                // 最大可住人数
                breakfastNum = (BigDecimal)item.get("ROOM_MAXPERSONS");
                roomType.setMaxPersons(null != breakfastNum ? 
                        breakfastNum.intValue() : 0);       
                
                // 房间其他设施
                roomType.setRoomOtherEquipment((String)item.get("OTHER_EQUIPMENT"));
                
                // 床型字符串,如"1,2"
                roomType.setBedTypeStr((String)item.get("BED_TYPE"));

				ret.addTmcRoomType(roomType);
                
                bNewPre = false;

			}
			// 新的支付类型和配额类型开始
			if (!(roomTypeId + childRoomTypeId + payMethod + quotaType)
					.equals(roomTypeId_pre + childRoomTypeId_pre
							+ payMethod_pre + quotaType_pre)) {
				saleItem = new SaleItem();
				saleItem.setPayMethod(PayMethod.PAY);
				saleItem.setQuotaType(quotaType);

				roomType.addSaleItem(saleItem);
				// 日期重新初始化
				calTmp = (Calendar) calStart.clone();
			}

			// 新的日期开始
			if (!(roomTypeId + childRoomTypeId + payMethod + quotaType + priceDate)
					.equals(roomTypeId_pre + childRoomTypeId_pre
							+ payMethod_pre + quotaType_pre + priceDate_pre)) {

                Double prepayPrice = Double.valueOf(item.get("PREPAY_PRICE") == null ? "0" : (item
                        .get("PREPAY_PRICE")).toString());
                
                if (!bNewPre && prepayPrice.doubleValue() > 0 && 
                        !(roomTypeId + childRoomTypeId).equals(roomTypeId_pre
                        + childRoomTypeId_pre)) {
                    saleItemPre = new SaleItem();
                    saleItemPre.setPayMethod(PayMethod.PRE_PAY);
                    saleItemPre.setQuotaType(quotaType);
                    roomType.addSaleItem(saleItemPre);
                    
                    bNewPre = true;
                }
                
				Date fellowDate = (Date) item.get("PRICEDATE");
				Double salePrice = Double
						.valueOf(item.get("PRICE") == null ? "0" : (item
								.get("PRICE")).toString());
				Double serviceFeeT = Double
						.valueOf(item.get("SERVICEFEE") == null ? "0" : (item
								.get("SERVICEFEE")).toString());
				Double netFee = Double.valueOf(item.get("NETFEE") == null ? "0"
						: (item.get("NETFEE")).toString());

				String serviceFeeType = item.get("SERVICEFEETYPE") == null ? ""
						: item.get("SERVICEFEETYPE").toString();                
                Double serviceFee = null;
				// 服务费收费方式为百分比
				if ("1".equals(serviceFeeType)) {
					serviceFee = salePrice * serviceFeeT / 100;
				} else {
                    serviceFee = serviceFeeT;
                }
				// 没有价格信息的日期需要补齐
                boolean bFoundInfo = false;
				while (calEnd.after(calTmp)) {
                    if(bNewPre) {
                        roomInfoPre = new RoomInfo();
                    }
					if (DateUtil.dateToString(fellowDate).equals(
							DateUtil.dateToString(calTmp.getTime()))) {
						roomInfo = new RoomInfo();
						roomInfo.setFellowDate(calTmp.getTime());
						roomInfo.setQuotaPattern((String) item
								.get("QUOTA_PATTERN"));
						String breakfastType = (String) item
								.get("BREAKFASTTYPE");
						String breakfastNum = item.get("BREAKFASTNUM") == null ? "0" : item.get("BREAKFASTNUM").toString();

						roomInfo.setBreakfast(breakfastType);
                        roomInfo.setBreakNum(breakfastNum);
						roomInfo.setCurrency((String) item.get("CURRENCY"));
						roomInfo.setSalePrice(salePrice.toString());
						roomInfo.setNetFee(netFee.toString());
						roomInfo.setServiceFee(serviceFee.toString());
                        
                        // 判断是否有关房
                        breakfastType = (String)item.get("OPENROOM");
                        if("N".equals(breakfastType)) {
                            roomType.setClose_flag("G");
                        }
                        
                        // 是否必须面转预
                        breakfastType = (String)item.get("ISPAYTOREPAY");
                        if("Y".equals(breakfastType)) {
                            roomType.setPayToPrepay(1);
                        }

						saleItem.addRoomInfo(roomInfo);
                                                
						calTmp.add(Calendar.DATE, 1);//日期增加
                        
                        if(bNewPre) { 
                            if ("1".equals(serviceFeeType)) {
                                serviceFee = prepayPrice * serviceFeeT / 100;
                            } else {
                                serviceFee = serviceFeeT;
                            }
                            roomInfoPre.setServiceFee(serviceFee.toString());
                        }
                        
                        bFoundInfo = true;                                                
					} else {
						roomInfo = new RoomInfo();
						roomInfo.setFellowDate(calTmp.getTime());
						roomInfo.setQuotaPattern("无");
						roomInfo.setBreakfast("无");
						roomInfo.setCurrency("RMB");
						roomInfo.setSalePrice("无");

						roomInfo.setNetFee("无");
						roomInfo.setServiceFee("无");

						saleItem.addRoomInfo(roomInfo);
						saleItem.addNoPriceDate(DateUtil.dateToString(calTmp.getTime()));
                        if(bNewPre) {
                            saleItemPre.addNoPriceDate(DateUtil.dateToString(calTmp.getTime()));
                        }
						calTmp.add(Calendar.DATE, 1);      
                        
                        if(bNewPre) { 
                            roomInfoPre.setServiceFee("无");
                        }
					}
                    
                    if(bNewPre) {                        
                        roomInfoPre.setFellowDate(roomInfo.getFellowDate());
                        roomInfoPre.setQuotaPattern(roomInfo.getQuotaPattern());
                        roomInfoPre.setBreakfast(roomInfo.getBreakfast());
                        roomInfoPre.setBreakNum(roomInfo.getBreakNum());
                        roomInfoPre.setCurrency(roomInfo.getCurrency());
                        roomInfoPre.setSalePrice(prepayPrice.toString());
                        roomInfoPre.setNetFee(roomInfo.getNetFee());                        
                        saleItemPre.addRoomInfo(roomInfoPre);
                    }
                    
                    if(bFoundInfo) {
                        break;
                    }
				}

				// 记录首日日期及首日价格
				if (i == 0) {
					firstDate = priceDate;
					priceLow = salePrice.doubleValue();
					priceHigh = salePrice.doubleValue();
				}
				// 比较首日价格，获得最低价和最高价
				if (firstDate.equals(priceDate)) {
					if (priceLow > salePrice.doubleValue()) {
						priceLow = salePrice.doubleValue();
					}
					if (priceHigh < salePrice.doubleValue()) {
						priceHigh = salePrice.doubleValue();
					}
				}
			}

			roomTypeId_pre = roomTypeId;
			childRoomTypeId_pre = childRoomTypeId;
			payMethod_pre = payMethod;
			quotaType_pre = quotaType;
			priceDate_pre = priceDate;
		}
		ret.setLowPrice(priceLow + "");
		ret.setHighPrice(priceHigh + "");
		processNoPriceDate(ret, calStart, calEnd);
		return ret;
	}


	/**
	 * 处理末端没有价格的日期，前面或者中间没有价格的日期组装的时候已经处理
	 * 
	 * @param hotelInfo
	 * @param calStart
	 * @param calEnd
	 */
	private void processNoPriceDate(HotelInfo hotelInfo, Calendar calStart,
			Calendar calEnd) {
		Calendar calTmp = Calendar.getInstance();
		RoomInfo roomInfo = null;
		for (int i = 0; i < hotelInfo.getTmcRoomTypes().size(); i++) {
			RoomType roomType = (RoomType) hotelInfo.getTmcRoomTypes().get(i);
			for (int j = 0; j < roomType.getSaleItems().size(); j++) {
				SaleItem saleItem = (SaleItem) roomType.getSaleItems().get(j);
				List roomInfos = saleItem.getRoomInfos();
				if (roomInfos.size() == 0) {
					fillNullDate(saleItem, calStart, calEnd,
							HotelOrderConstans.HOTEL_SOURCE_TMC);
				} else if (roomInfos.size() > 0) {
					roomInfo = (RoomInfo) roomInfos.get(roomInfos.size() - 1);
					calTmp.setTime(roomInfo.getFellowDate());
					calTmp.add(Calendar.DATE, 1);
					fillNullDate(saleItem, calTmp, calEnd,
							HotelOrderConstans.HOTEL_SOURCE_TMC);
				}
			}
		}

		for (int i = 0; i < hotelInfo.getHotelRoomTypes().size(); i++) {
			RoomType roomType = (RoomType) hotelInfo.getHotelRoomTypes().get(i);
			for (int j = 0; j < roomType.getSaleItems().size(); j++) {
				SaleItem saleItem = (SaleItem) roomType.getSaleItems().get(j);
				List roomInfos = saleItem.getRoomInfos();
				if (roomInfos.size() == 0) {
					fillNullDate(saleItem, calStart, calEnd,
							HotelOrderConstans.HOTEL_SOURCE_HOTEL);
				} else if (roomInfos.size() > 0) {
					roomInfo = (RoomInfo) roomInfos.get(roomInfos.size() - 1);
					calTmp.setTime(roomInfo.getFellowDate());
					calTmp.add(Calendar.DATE, 1);
					fillNullDate(saleItem, calTmp, calEnd,
							HotelOrderConstans.HOTEL_SOURCE_HOTEL);
				}
			}
		}
	}

	/**
	 * 构造空的房价信息
	 * 
	 * @param saleItem
	 * @param calStart
	 * @param calEnd
	 * @param flag
	 */
	private void fillNullDate(SaleItem saleItem, Calendar calStart,
			Calendar calEnd, String flag) {
		Calendar calTmp = (Calendar) calStart.clone();
		RoomInfo roomInfo = null;
		if (HotelOrderConstans.HOTEL_SOURCE_HOTEL.equals(flag)) {
			while (calEnd.after(calTmp)) {
				roomInfo = new RoomInfo();
				roomInfo.setFellowDate(calTmp.getTime());
				roomInfo.setQuotaAmount("无");
				roomInfo.setQuotaPattern("无");
				roomInfo.setBreakfast("无");
				roomInfo.setRoomStatus("无");
				roomInfo.setCurrency("无");
				roomInfo.setSalePrice("无");
				saleItem.addRoomInfo(roomInfo);
				saleItem.addNoPriceDate(DateUtil.dateToString(calTmp
						.getTime()));
				calTmp.add(Calendar.DATE, 1);
			}
		} else if (HotelOrderConstans.HOTEL_SOURCE_TMC.equals(flag)) {
			while (calEnd.after(calTmp)) {
				roomInfo = new RoomInfo();
				roomInfo.setFellowDate(calTmp.getTime());
				roomInfo.setQuotaPattern("无");
				roomInfo.setBreakfast("无");
				roomInfo.setCurrency("无");
				roomInfo.setSalePrice("无");
				roomInfo.setNetFee("无");
				roomInfo.setServiceFee("无");
				saleItem.addRoomInfo(roomInfo);
				saleItem.addNoPriceDate(DateUtil.dateToString(calTmp
						.getTime()));
				calTmp.add(Calendar.DATE, 1);
			}
		}

	}

	/**
	 * 给每一个酒店ID号打上酒店类型标记
	 * 
	 * @param list
	 * @param kind
	 */
	private void putResultKind(List list, String kind) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map item = (HashMap) list.get(i);
				item.put("KIND", kind);
			}
		}
	}
	

	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	public static void main(String[] args) {
		System.out.println("18:30".compareTo(""));
	}

	/**
	 * 
	 * @return
	 * create by xu
	 */
	public HotelQueryPageInfo getHotelQueryPageInfo() {
		return hotelQueryPageInfo;
	}

	public void setHotelQueryPageInfo(HotelQueryPageInfo hotelQueryPageInfo) {
		this.hotelQueryPageInfo = hotelQueryPageInfo;
	}

    public QueryAssist getQueryAssist() {
        return queryAssist;
    }

    public void setQueryAssist(QueryAssist queryAssist) {
        this.queryAssist = queryAssist;
    }
    
    
    /**
     * 查询TMC酒店价格列表(前后日期)
     * add by shizhongwen
     * 时间:Oct 14, 2009  3:14:21 PM
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param beginDate
     * @param endDate
     * @param minPrice
     * @param maxPrice
     * @param payMethod
     * @param quotaType
     * @return
     */
    public List<QueryHotelForWebSaleItems> queryTMCPriceForWeb(long hotelId, long roomTypeId,
          Date beginDate, Date endDate, double minPrice, double maxPrice,
        String payMethod,Company company){
        // ---对参考价格的查询需要参考入住日期是否为当天、次天或其他
        Date tempBeginDate = new Date();
        Date tempEndDate = new Date();
        Calendar calendar = Calendar.getInstance();
        if (0 == DateUtil.getDayOverToday(beginDate)) {
            calendar.setTime(endDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 4);// 让结束日期加4
            tempEndDate = calendar.getTime();
            tempBeginDate = beginDate;
        } else if (1 == DateUtil.getDayOverToday(beginDate)) {
            calendar.setTime(endDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 3);// 让结束日期加3
            tempEndDate = calendar.getTime();
            calendar.setTime(beginDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);// 让起始日期减1
            tempBeginDate = calendar.getTime();
        } else {
            calendar.setTime(endDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 2);// 让结束日期加2
            tempEndDate = calendar.getTime();
            calendar.setTime(beginDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 2);// 让起始日期减2
            tempBeginDate = calendar.getTime();
        }
        List resultLis = queryTMCPriceDetailForWeb(hotelId,roomTypeId,tempBeginDate,
            tempEndDate, minPrice,  maxPrice,payMethod,company);
//        return removeSomePrice(resultLis, beginDate, endDate);
        return resultLis;
    }
    
    
    /**
     * 查询TMC酒店价格列表
     * add by shizhongwen
     * 时间:Oct 15, 2009  3:48:54 PM
     * @param hotelID
     * @param beginDateExt
     * @param endDateExt
     * @param roomTypeID
     * @param childRoomTypeID
     * @param minPrice
     * @param maxPrice
     * @param payMethod
     * @param company
     * @return
     */
    public List<QueryHotelForWebSaleItems> queryTMCPriceDetailForWeb(Long hotelID,long roomTypeId,
         Date beginDateExt, Date endDateExt,
         double minPrice, double maxPrice, String payMethod,Company company) {
        
        Map subParam = new HashMap();        
        subParam.put("hotelId", hotelID);//酒店ID   
        subParam.put("roomtypeid", roomTypeId);
        subParam.put("joinDate", DateUtil.dateToString(beginDateExt));//从查询条件中来
        subParam.put("leaveDate", DateUtil.dateToString(endDateExt));
        subParam.put("priceLowQry", minPrice);
        subParam.put("priceHighQry", maxPrice);
        subParam.put("companyID", company.getID());
        HotelInfo hotel = null;//接收组装的酒店信息

        List tmcInfo = queryDao.queryForList("queryTmcHotelInfo",
                subParam);// 
        List<QueryHotelForWebSaleItems> saleResults = new ArrayList<QueryHotelForWebSaleItems>();
      
        if (null != tmcInfo &&tmcInfo.size()>0) {
            for (int i = 0; i < tmcInfo.size(); i++) {
                Map item = (HashMap) tmcInfo.get(i);
                String pricetem="";
                String prepaypricetem="";
                if(null!=item.get("PRICE")){
                    pricetem=item.get("PRICE").toString();
                }
                if(null!=item.get("PREPAY_PRICE")){
                    prepaypricetem=item.get("PREPAY_PRICE").toString();
                }
                
                QueryHotelForWebSaleItems saleItem = new QueryHotelForWebSaleItems();
                if(null!=pricetem&&!pricetem.equals("")&&"pay".equals(payMethod)){//面付价                    
                    saleItem.setFellowDate((Date) item.get("PRICEDATE"));//房间间夜日期
                    saleItem.setAvailQty(1); //配额数量
                    saleItem.setRoomState("");//房态
                    String breakfastnum= item.get("BREAKFASTNUM").toString();               
                    saleItem.setBreakfastNum(Integer.parseInt(breakfastnum));//早餐数量
                    saleItem.setBreakfastType((String)item.get("BREAKFASTTYPE"));//早餐类型
                    saleItem.setClose_flag("");//开关房标志
                    saleItem.setCanBook(0);//子房型能否预订信息                 
                    saleItem.setSalePrice(Double.parseDouble(pricetem));//销售价
                    //add by shizhongwen 2009-10-3 加入TMC价格
                    saleItem.setTmcPirce(Double.parseDouble(pricetem));                
                    saleItem.setBasePrice(Double.parseDouble(pricetem));       
                    
                    //saleItem.setPayToPrepay(webItem.getPayToPrepay());
                    saleResults.add(saleItem);
                }
                if(null!=prepaypricetem&&!prepaypricetem.equals("")&&"pre_pay".equals(payMethod)){//预付价                    
                    saleItem.setFellowDate((Date) item.get("PRICEDATE"));//房间间夜日期
                    saleItem.setAvailQty(1); //配额数量
                    saleItem.setRoomState("");//房态
                    String breakfastnum= item.get("BREAKFASTNUM").toString();               
                    saleItem.setBreakfastNum(Integer.parseInt(breakfastnum));//早餐数量
                    saleItem.setBreakfastType((String)item.get("BREAKFASTTYPE"));//早餐类型
                    saleItem.setClose_flag("");//开关房标志
                    saleItem.setCanBook(0);//子房型能否预订信息                 
                    saleItem.setSalePrice(Double.parseDouble(prepaypricetem));//销售价
                    //add by shizhongwen 2009-10-3 加入TMC价格
                    saleItem.setTmcPirce(Double.parseDouble(pricetem));                
                    saleItem.setBasePrice(Double.parseDouble(pricetem));
                    //saleItem.setPayToPrepay(webItem.getPayToPrepay());
                    saleResults.add(saleItem);
                }
                
                // 宽带信息 add by chenkeming@2009-11-04
                String strEqu = (String)item.get("ROOM_EQUIPMENT");
                if(StringUtil.isValidStr(strEqu) && 0 <= strEqu.indexOf("21")) {
                    strEqu = (String)item.get("INCLUDENET");
                    if("Y".equals(strEqu)) {
                        saleItem.setRoomEquipment("免费");
                    } else {
                        saleItem.setRoomEquipment("收费");
                    }
                } else {
                    saleItem.setRoomEquipment("无");
                }                                
            }
        }
        return saleResults;
    }
    

}
