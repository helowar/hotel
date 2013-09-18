package com.mangocity.tmc.service.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.util.WebStrUtil;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.hweb.persistence.QueryHotelForWebRoomType;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.tmc.persistence.view.HotelInfoTmc;
import com.mangocity.tmc.persistence.view.HotelQueryConditionTmc;
import com.mangocity.tmc.persistence.view.RoomInfoTmc;
import com.mangocity.tmc.persistence.view.RoomTypeTmc;
import com.mangocity.tmc.persistence.view.SaleItemTmc;
import com.mangocity.tmc.service.assistant.HotelInfo;
import com.mangocity.tmc.service.assistant.HotelQueryCondition;
import com.mangocity.tmc.service.assistant.HotelQueryPageInfo;
import com.mangocity.tmc.service.assistant.RoomInfo;
import com.mangocity.tmc.service.assistant.RoomType;
import com.mangocity.tmc.service.assistant.SaleItem;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.hotel.constant.PayMethod;

/**
 * TMC查酒店工具类 
 * 
 * @author chenkeming Sep 28, 2009 
 */
public class QueryAssist {
    
    ResourceManager resourceManager;

    /**
     * CC查询条件类转换成协议查询条件类
     * 
     * @author chenkeming Sep 28
     * @param condTmc
     * @param member
     * @return
     */
    public HotelQueryCondition queryCondMango2Tmc(
            HotelQueryConditionTmc condTmc, MemberDTO member) {
        HotelQueryCondition cond = new HotelQueryCondition();
        cond.setCityCode(condTmc.getCityId());
        cond.setCityName(condTmc.getCityName());
        cond.setZone(condTmc.getDistrict());
        cond.setBizZone(condTmc.getBizZone());
        cond.setJoinDate(DateUtil.dateToString(condTmc.getBeginDate()));
        cond.setJoinDays(condTmc.getDays());
        cond.setLeaveDate(DateUtil.dateToString(condTmc.getEndDate()));
//        cond.setHotelSource(hotelSource);
        //cond.setCompanyNo(member.getBtMember().getCompanycd());
        //cond.setCompanyID(member.getBtMember().getCompanyid());
        String sTmp = condTmc.getMinPrice();
        if(StringUtil.isValidStr(sTmp)) {
            cond.setPriceLow(Double.valueOf(sTmp));   
        }
        sTmp = condTmc.getMaxPrice();
        if(StringUtil.isValidStr(sTmp)) {
            cond.setPriceHigh(Double.valueOf(sTmp));   
        }
        cond.setHotelStar(condTmc.getHotelStar());
        cond.setHotelType(condTmc.getHotelType());
//        cond.setHotelFixtrue(condTmc.get)
        cond.setChnName(condTmc.getHotelChnName());
        cond.setEngName(condTmc.getHotelEngName());
        cond.setChnAddress(condTmc.getChnAddress());
        cond.setMemberID(member.getId());
        
        cond.setPageSize(condTmc.getPageSize());
        
        return cond;
    }
    
    /**
     * 网站查询条件类转换成协议查询条件类
     * TODO: 1.宽带,酒店式公寓和开业/装修;2.英文酒店名
     * 
     * @author chenkeming Sep 29, 2009 2:43:03 PM
     * @param condWeb
     * @param companyCd
     * @param companyId
     * @return
     */
    public HotelQueryCondition queryCondWeb2Tmc(
            QueryHotelForWebBean condWeb, String companyCd, Long companyId) {
        HotelQueryCondition cond = new HotelQueryCondition();
        cond.setCityCode(condWeb.getCityId());
        cond.setCityName(condWeb.getCityName());
        cond.setZone(condWeb.getDistrict());
        cond.setBizZone(condWeb.getBizZone());
        cond.setJoinDate(DateUtil.dateToString(condWeb.getInDate()));
        cond.setJoinDays(DateUtil.getDay(condWeb.getInDate(), condWeb.getOutDate()));
        cond.setLeaveDate(DateUtil.dateToString(condWeb.getOutDate()));
//        cond.setHotelSource(hotelSource);
        cond.setCompanyNo(companyCd);
        cond.setCompanyID(companyId);
        String sTmp = condWeb.getMinPrice();
        if(StringUtil.isValidStr(sTmp)) {
            cond.setPriceLow(Double.valueOf(sTmp));   
        }
        sTmp = condWeb.getMaxPrice();
        if(StringUtil.isValidStr(sTmp)) {
            cond.setPriceHigh(Double.valueOf(sTmp));   
        }
        cond.setHotelStar(condWeb.getHotelStar());
        cond.setHotelType(condWeb.getHotelType());
        cond.setChnName(condWeb.getHotelName());        
        
        cond.setPageSize(condWeb.getPageSize());
        //add by shizhongwen 2009-10-17 增加是否为海尔VIP会员的标识
        cond.setHaierVipMember(condWeb.isHaierVipMember());
        
        // 宽带条件
        String strSpec = condWeb.getSpecialRequest();
        if(StringUtil.isValidStr(strSpec) && 0 <= strSpec.indexOf('3')) {
            cond.setIncludeNet(true);
        } else {
            cond.setIncludeNet(false);
        }
        
        // 装修
        if(StringUtil.isValidStr(strSpec) && 0 <= strSpec.indexOf('4')) {
            cond.setCheckFitment(true);
        } else {
            cond.setCheckFitment(false);
        }
        
        return cond;
    }
    
    /**
     * tmc前台查协议酒店pageInfo转化成散客查询结果类型
     * 
     * @author chenkeming Sep 29, 2009
     * @param pageInfo
     * @return
     */
    public List<HotelInfoTmc> PageInfo2Lst(HotelQueryPageInfo pageInfo) {
        List<HotelInfoTmc> liRes = new ArrayList<HotelInfoTmc>();
        List liHotel = pageInfo.getHotelList();
        if(null != liHotel && 0 < liHotel.size()) {
            for(int i=0; i<liHotel.size(); i++) {
                HotelInfo hotelInfo = (HotelInfo)liHotel.get(i);
                HotelInfoTmc infoTmc = new HotelInfoTmc();
                infoTmc.setHotelId(Long.parseLong(hotelInfo.getHotelId()));
                infoTmc.setHotelChnName(hotelInfo.getHotelChnName());
                infoTmc.setHotelEngName(hotelInfo.getHotelEngName());
                infoTmc.setHotelStar(hotelInfo.getHotelStar());
                String strHotelStar = resourceManager.getDescription(
                        "res_hotelStarToNum", hotelInfo.getHotelStar());
                if (StringUtil.isValidStr(strHotelStar)) {
                    infoTmc.setHotelStar1(new Float(strHotelStar));
                } else {
                    infoTmc.setHotelStar1(1);
                }
                
                // 酒店类型
                infoTmc.setHotelType(hotelInfo.getHotelType());
                // 酒店中文地址
                infoTmc.setChnAddress(hotelInfo.getChnAddress());
                // /酒店介绍
                infoTmc.setHotelIntroduce(hotelInfo.getHotelIntroduce());
                // 地区
                infoTmc.setBizZone(hotelInfo.getBizZone());
                // 城市
                infoTmc.setCity(hotelInfo.getCity());
                String currency = hotelInfo.getCurrency();
                if(!StringUtil.isValidStr(currency)) {
                    currency = "RMB"; 
                }
                infoTmc.setCurrency(currency);                
                // 汇率值                
                infoTmc.setRateValue(CurrencyBean.rateMap.get(currency));
                //add by wuyun 酒店直联
                infoTmc.setCooperateChannel(hotelInfo.getCooperate_channel());
                
                // TODO: 中旅属性
//                hotelInfo.setFlagCtsHK(info.isFlagCtsHK());
                
                liRes.add(infoTmc);
                
                List<RoomType> liRoomType = hotelInfo.getTmcRoomTypes();
                for(RoomType roomType : liRoomType) {
                    RoomTypeTmc roomTypeTmc = new RoomTypeTmc();
                    
                    roomTypeTmc.setChildRoomTypeId(roomType.getChildRoomTypeId());
                    roomTypeTmc.setChildRoomTypeName(roomType.getChildRoomTypeName());
                    roomTypeTmc.setRoomTypeName(roomType.getRoomTypeName());
                    roomTypeTmc.setRoomTypeId(roomType.getRoomTypeId());
                    roomTypeTmc.setRecommend(roomType.getRecommend());                    
                    String sTmp = roomType.getCooperate_channel();
                    int nTmp = StringUtil.isValidStr(sTmp) ? Integer.parseInt(sTmp) : 0; 
                    roomTypeTmc.setRoomChannel(nTmp);
                    
                    infoTmc.getRoomTypes().add(roomTypeTmc);
                    
                    List liSaleItem = roomType.getSaleItems();
                    if(null != liSaleItem && 0 < liSaleItem.size()) {
                        for(int j=0; j<liSaleItem.size(); j++) {
                            SaleItem saleItem = (SaleItem)liSaleItem.get(j);
                            SaleItemTmc saleItemTmc = new SaleItemTmc();
                            
                            saleItemTmc.setQuotaType(saleItem.getQuotaType());
                            saleItemTmc.setPayMethod(saleItem.getPayMethod());
                            
                            roomTypeTmc.getSaleItems().add(saleItemTmc);
                            List liRoomInfo = saleItem.getRoomInfos();
                            if(null != liRoomInfo && liRoomInfo.size() > 0) {
                                for(int k=0; k<liRoomInfo.size(); k++) {
                                    RoomInfo roomInfo = (RoomInfo)liRoomInfo.get(k);
                                    RoomInfoTmc roomInfoTmc = new RoomInfoTmc();
                                    
                                    roomInfoTmc.setCurrency(roomInfo.getCurrency());
                                    roomInfoTmc.setRmbCurrency(roomInfo.getCurrency());
                                    sTmp = roomInfo.getSalePrice();
                                    double fTmp = StringUtil.isValidStr(sTmp) ?
                                            Double.parseDouble(sTmp) : 0.0;
                                    roomInfoTmc.setRmbSalePrice(fTmp);
                                    sTmp = roomInfo.getQuotaAmount();
                                    nTmp = StringUtil.isValidStr(sTmp) ? Integer.parseInt(sTmp) : 0; 
                                    roomInfoTmc.setQuotaAmount(nTmp);
                                    roomInfoTmc.setSalePrice(fTmp);
                                    roomInfoTmc.setRoomStatus(roomInfo.getRoomStatus());
                                    roomInfoTmc.setBreakfast(roomInfo.getBreakfast());
                                    roomInfoTmc.setBreakNum(roomInfo.getBreakNum());
                                    roomInfoTmc.setFellowDate(roomInfo.getFellowDate());
                                    
                                    saleItemTmc.getRoomItems().add(roomInfoTmc);
                                }
                            }
                        }
                    }
                }
            }
        }
        return liRes;
    }
    
    
    /**
     * tmc查协议酒店结果类转化成网站结果类
     * TODO: 1.
     *       2.
     *       3.
     * 
     * @author chenkeming Sep 29
     * @param pageInfo
     * @param cond
     * @return
     */
    public HotelPageForWebBean PageInfo2PageForWebBean(HotelQueryPageInfo pageInfo,
            HotelQueryCondition cond) {
        HotelPageForWebBean res = new HotelPageForWebBean();
        int totalIndex = 0;
        int pageSize = pageInfo.getPageSize();
        int totalSize = pageInfo.getTotalRecord(); 
        if (0 != totalSize % pageSize) {
            totalIndex = totalSize / pageSize + 1;
        } else {
            totalIndex = totalSize / pageSize;
        }
        res.setTotalIndex(totalIndex);
        res.setPageSize(pageSize);
        res.setPageIndex(pageInfo.getPageNo());
        res.setRateStr(CurrencyBean.rateMap.get(CurrencyBean.HKD));
        
        // 计算周需要显示的行数
        Date inDate = DateUtil.getDate(cond.getJoinDate());
        Date outDate = DateUtil.getDate(cond.getLeaveDate()); 
        int difdays = DateUtil.getDay(inDate, outDate);
        int weekTotal = (difdays - 1) / 7 + 1;
        // 计算显示的页面显示的宽度
        int colCount = (1 < weekTotal ? 8 : difdays) + 6;   
        
        List<QueryHotelForWebResult> liRes = new ArrayList<QueryHotelForWebResult>();
        List liHotel = pageInfo.getHotelList();
        if(null != liHotel && 0 < liHotel.size()) {
            for(int i=0; i<liHotel.size(); i++) {
                HotelInfo hotelInfo = (HotelInfo)liHotel.get(i);
                QueryHotelForWebResult hotelResult = new QueryHotelForWebResult();
                hotelResult.setHotelId(Long.parseLong(hotelInfo.getHotelId()));
                hotelResult.setCommendType(hotelInfo.getCommendType()); 
                hotelResult.setCooperateChannel(hotelInfo.getCooperate_channel());
                hotelResult.setHotelLogo(hotelInfo.getHotelLogo());
                hotelResult.setForPlane(hotelInfo.getForPlane());
                hotelResult.setForFreeGym(hotelInfo.getForFreeGym());
                hotelResult.setForFreeStop(hotelInfo.getForFreeStop());
                hotelResult.setForFreePool(hotelInfo.getForFreePool());
                String strCMStr = hotelInfo.getCancelModifyStr();
                hotelResult.setCancelModifyStr(StringUtil.isValidStr(strCMStr) ? 
                        strCMStr.replaceAll("\r\n", "<br>") : "");                
                List liRoomType = hotelInfo.getTmcRoomTypes();                
                if(null != liRoomType && 0 < liRoomType.size()) {
                    String oldRoomType = "";
                    String curRoomType = "";
                    int nRoomType = 0;
                    for(int j=0; j<liRoomType.size(); j++) {
                        RoomType roomType = (RoomType)liRoomType.get(j);
                        QueryHotelForWebRoomType roomTypeWeb = new QueryHotelForWebRoomType();
                        curRoomType = roomType.getRoomTypeId();
                        if(!curRoomType.equals(oldRoomType)) {                            
                            nRoomType ++;
                            oldRoomType = curRoomType;
                        }
                        
                        roomTypeWeb.setCurrency(hotelInfo.getCurrency());                                                
                        
                        roomTypeWeb.setChildRoomTypeId(roomType.getChildRoomTypeId());
                        roomTypeWeb.setChildRoomTypeName(roomType.getChildRoomTypeName());
                        roomTypeWeb.setRoomTypeName(roomType.getRoomTypeName());
                        roomTypeWeb.setRoomTypeId(roomType.getRoomTypeId());                        
                                                                        
                        roomTypeWeb.setRoomChannel(0);
                        roomTypeWeb.setBookButtonenAble("1");
                        roomTypeWeb.setBookButtonenAbleForPrepay("1");    
                        
                        // 含早信息
                        roomTypeWeb.setBreakfastNum(roomType.getBreakfastNum());
                        
                        // 宽带信息
                        roomTypeWeb.setNet(roomType.getNet());
                        
                        // 房间设施信息
                        roomTypeWeb.setAddBedQty(roomType.getAddBedQty());
                        roomTypeWeb.setRoomEquipment(roomType.getRoomEquipment());
                        roomTypeWeb.setRoomOtherEquipment(roomType.getRoomOtherEquipment());
                        roomTypeWeb.setMaxPersons(roomType.getMaxPersons());
                        
                        // 开关房
                        roomTypeWeb.setClose_flag(roomType.getClose_flag());
                        
                        // 是否要面转预
                        roomTypeWeb.setPayToPrepay(roomType.getPayToPrepay());
                         
                        // 设置预订的床型字符串
                        String bedTypeStr = roomType.getBedTypeStr();
                        roomTypeWeb.setTmcBedTypeStr(bedTypeStr);
                        roomTypeWeb.setBedType(WebStrUtil.getTmcBedType(bedTypeStr));
                        
                        List liSaleItem = roomType.getSaleItems();
                        String roomState = "";
                        if(null != liSaleItem && 0 < liSaleItem.size()) {
                            int fk = 0;
                            int fkY = 0;
                            int fkM = 0;
                            for(int k=0; k<liSaleItem.size(); k++) {
                                SaleItem saleItem = (SaleItem)liSaleItem.get(k);
                                
                                roomTypeWeb.setPayMethod(saleItem.getPayMethod());
                                roomTypeWeb.setQuotaType(saleItem.getQuotaType());                                                               
                                
                                List liRoomInfo = saleItem.getRoomInfos();                                
                                for(int l=0; l<liRoomInfo.size(); l++) {
                                    RoomInfo roomInfo = (RoomInfo)liRoomInfo.get(l);
                                    
                                    if(0 == inDate.compareTo(roomInfo.getFellowDate())) {
                                        roomState = roomInfo.getRoomStatus();   
                                    }                                    
                                    
                                    QueryHotelForWebSaleItems saleItemWeb = new QueryHotelForWebSaleItems();
                                    
                                    saleItemWeb.setRoomState(roomInfo.getRoomStatus());
                                    saleItemWeb.setFellowDate(roomInfo.getFellowDate());
                                    String sTmp = roomInfo.getSalePrice();
                                    double fTmp = StringUtil.isValidStr(sTmp) ? Double.parseDouble(sTmp) : 0.0;
                                    saleItemWeb.setSalePrice(fTmp);
                                    sTmp = roomInfo.getQuotaAmount();
                                    int nTmp = StringUtil.isValidStr(sTmp) ? Integer.parseInt(sTmp) : 0;
                                    saleItemWeb.setAvailQty(nTmp);
                                
                                    saleItemWeb.setMustLastDate(roomInfo.getMustLastDate());
                                    saleItemWeb.setMustFirstDate(roomInfo.getMustFirstDate());
                                    saleItemWeb.setContinueDay(roomInfo.getContinueDay());
                                    saleItemWeb.setMustInDate(roomInfo.getMustInDate());                                    
                                    
                                    if(PayMethod.PAY.equals(saleItem.getPayMethod())) {
                                        roomTypeWeb.getFaceItems().add(saleItemWeb);
                                        fkM = 2;
                                    } else {
                                        roomTypeWeb.getSaleItems().add(saleItemWeb);                                    
                                        fkY = 1;
                                    }
                                }
                                                           
                            }
                            fk = fkM + fkY;
                            roomTypeWeb.setFk(fk);
                            if(3 > fk) {
                                if(2 == fk) {
                                    roomTypeWeb.setItemsList(roomTypeWeb.getFaceItems());
                                    roomTypeWeb.setFaceItems(new ArrayList<QueryHotelForWebSaleItems>());
                                } else {
                                    roomTypeWeb.setItemsList(roomTypeWeb.getSaleItems());
                                    roomTypeWeb.setSaleItems(new ArrayList<QueryHotelForWebSaleItems>());
                                }
                            }
                        }
                        
                        roomTypeWeb.setWeekTotal(weekTotal);
                        // roomTypeWeb.setBedType(roomState, 1);
                        
                        hotelResult.getRoomTypes().add(roomTypeWeb);
                    }
                    hotelResult.setFx(nRoomType);
                }
                String sTmp = hotelInfo.getLowPrice();
                double fTmp = StringUtil.isValidStr(sTmp) ? Double.parseDouble(sTmp) : 0.0;
                hotelResult.setMinPrice(fTmp);
                hotelResult.setHotelStar(hotelInfo.getHotelStar());
                                             
                hotelResult.setColCount(colCount);                
                hotelResult.setWeekTotal(weekTotal);
                
                List weekNum = getDateStrList(inDate, outDate);
                hotelResult.setWeekNum(weekNum);
                List<String> DateNum = getDateStrLst(inDate, outDate);
                hotelResult.setDateNum(DateNum);                  
                hotelResult.setHotelChnName(hotelInfo.getHotelChnName());
                hotelResult.setHotelEngName(hotelInfo.getHotelEngName());
                hotelResult.setAutoIntroduce(hotelInfo.getHotelIntroduce());
                hotelResult.setBizZone(hotelInfo.getBizZone());
                hotelResult.setDistrict(hotelInfo.getZone());

                // 判断是否只查询一晚 add by haibo.li 电子地图二期
                if (1 < difdays) {
                    hotelResult.setContinueLong(true);
                } else {
                    hotelResult.setContinueLong(false);
                }

                String rateStr = "1";
                if (CurrencyBean.RMB.equals(hotelInfo.getCurrency())) {
                    hotelResult.setRateStr("1");
                } else {
                    if (null == CurrencyBean.rateMap) {
                        rateStr = "1";
                    } else {
                        rateStr = (String) CurrencyBean.rateMap.get(hotelInfo.getCurrency());
                        if (null == rateStr) {
                            rateStr = "1";
                        }
                    }
                    hotelResult.setRateStr(rateStr);
                }
                hotelResult.setCurrency(hotelInfo.getCurrency());
                // add by shizhongwen 2009-10-17 海尔VIP会员 面转预
                if(cond.isHaierVipMember()){
                    hotelResult.setPayToPrepay(1);
                }
                liRes.add(hotelResult);   
            }
        }                
        res.setList(liRes);
        return res;
    }
    
    /**
     * 对于日期进行格式描述
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("deprecation")
    private List<String> getDateStrList(Date startDate, Date endDate) {

        int difdays = DateUtil.getDay(startDate, endDate);

        List<String> dateStrList = new ArrayList<String>();

        difdays = 7 <= difdays ? 7 : difdays;
        for (int i = 0; i < difdays; i++) {
            Date reservationDate = DateUtil.getDate(startDate, i);
            int week = reservationDate.getDay();
            String dateStr = "";
            switch (week) {
            case 1:
                dateStr = "周一";
                break;
            case 2:
                dateStr = "周二";
                break;
            case 3:
                dateStr = "周三";
                break;
            case 4:
                dateStr = "周四";
                break;
            case 5:
                dateStr = "周五";
                break;
            case 6:
                dateStr = "周六";
                break;
            case 0:
                dateStr = "周日";
                break;

            }

            dateStrList.add(dateStr);
        }

        return dateStrList;
    }
    
    /**
     * 对日期进行封装
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    private List<String> getDateStrLst(Date startDate, Date endDate) {
        List dateStrList = new ArrayList();
        dateStrList = DateUtil.getDates(startDate, endDate);
        int difdays = dateStrList.size() - 1;
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < difdays; i++) {
            String str = dateStrList.get(i).toString();
            java.util.Date a = DateUtil.getDate(str);
            String b = DateUtil.formatDateToMD(a);
            list.add(b);
        }
        return list;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
		
}
