package com.mangocity.hotel.order.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.service.HotelDelayLoadService;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCtct;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.persistence.HtlTrafficInfo;
import com.mangocity.hotel.base.persistence.HtlWelcomePrice;
import com.mangocity.hotel.base.persistence.SellSeason;
import com.mangocity.hotel.order.constant.LocalFlag;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.PayMethod;

/**
 * 酒店CC查询异步Action
 * @author chenjiajie
 *
 */
public class HotelSearchDelayAction extends OrderAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -979158330900256671L;
	
	/**
	 * 处理延时加载信息的Service 
	 */
	private HotelDelayLoadService hotelDelayLoadService;
	
	private HotelRoomTypeService hotelRoomTypeService;
	
	/**
	 * 酒店信息接口
	 */
    private IHotelService hotelService;
    
    private HotelManage hotelManage;
	
	/**
	 * 合同相关接口
	 */
	private ContractManage contractManage;
	
	/* 页面变量 begin */
	
	private String childRoomTypeId;
	
	private String roomTypeId;
	
	private String hotelId;
	
	private Date beginDate;
	
	private Date endDate;
	
	private HtlHotel htlHotel;
	
	private List<HtlRoomtype> lsRoomType;
	
	private List<HtlCtct> lsCtctSel;
	
	private List<SellSeason> lsSellSel;
	
	private List<HtlTrafficInfo> lsTrafficInfo;
	
	private List<HtlBookSetup> lsBookSetup;
	
	private List<HtlHotelExt> lsHtlHotelExt;
	
	/**
	 * 是否接受所有国家客人入住
	 */
	private int custom;
	/* 页面变量 end */
	
	/**
	 * 查询芒果网促销大礼包
	 * @return
	 */
	public String queryMangoPresale(){
		String foward = "mangoGift";
		Map params = super.getParams();
		String preSaleId = (String) params.get("preSaleId");
		if(StringUtil.isValidStr(preSaleId)){
			//按照大礼包ID查询 
			HtlPresale htlPresale = hotelDelayLoadService.findHtlPresaleById(new Long(preSaleId));
			request.setAttribute("htlPresale", htlPresale);
		}
		return foward;
	}

	/**
	 * 查询酒店促销小礼包
	 * @return
	 */
	public String queryHotelSalesPromo(){
		String foward = "hotelGift";
		if(StringUtil.isValidStr(childRoomTypeId) 
				&& StringUtil.isValidStr(hotelId)
				&& null != beginDate ){
			//按照大礼包ID查询 
			List<HtlSalesPromo> htlSalesPromoList = hotelDelayLoadService.findHtlSalesPromo(new Long(hotelId), childRoomTypeId,beginDate);
			request.setAttribute("htlSalesPromoList", htlSalesPromoList);
		}
		return foward;
	}
	
	/**
	 * 查询酒店提示信息
	 * @return
	 */
	public String queryHotelTipInfo(){
		String foward = "hotelTipInfo";
		if(StringUtil.isValidStr(childRoomTypeId) 
				&& StringUtil.isValidStr(hotelId) 
				&& null != beginDate 
				&& null != endDate){
			//根据酒店ID,价格类型和起始日期查询出提示信息 
			String priceTypeTipInfo = hotelService.queryAlertInfoStr(Long.valueOf(hotelId),childRoomTypeId,beginDate,endDate,LocalFlag.CC);
			request.setAttribute("priceTypeTipInfo", priceTypeTipInfo);
		}
		return foward;
	}

	/**
	 * 查询房型基本信息
	 * @return
	 */
	public String queryRoomTypeBaseInfo(){
		if(StringUtil.isValidStr(roomTypeId) && StringUtil.isValidStr(hotelId)){
			//按照房型ID查询 
			HtlRoomtype htlRoomtype = hotelRoomTypeService.getHtlRoomTypeByRoomTypeId(Long.parseLong(roomTypeId));
			request.setAttribute("htlRoomtype", htlRoomtype);
		}
		return "roomTypeInfo";
	}
	
	/**
	 * 查询房型扩展信息
	 * @return
	 */
	public String queryRoomTypeExtendInfo (){
		String foward = "roomTypeExtendInfo";
		if(StringUtil.isValidStr(roomTypeId) 
				&& StringUtil.isValidStr(hotelId)
				&& null != beginDate 
				&& null != endDate){
			//按酒店id查询当前所在的合同实体 
			HtlContract htlContract = contractManage.queryCurrentContractByHotelId(new Long(hotelId));
			if(null != htlContract && null != htlContract.getID()){
				Long contractId = htlContract.getID();
				//免费宽带
				List<HtlInternet> internetList = contractManage.queryInternet(contractId, Long.valueOf(roomTypeId), beginDate, endDate);
				request.setAttribute("internetList", internetList);
				//面付以及预付的加早信息
				List<HtlAddBedPrice> payAddBedList = contractManage.queryAddBed(contractId, Long.valueOf(roomTypeId), beginDate, endDate, PayMethod.PAY);
				List<HtlAddBedPrice> prePayAddBedList = contractManage.queryAddBed(contractId, Long.valueOf(roomTypeId), beginDate, endDate, PayMethod.PRE_PAY);
				request.setAttribute("payAddBedList", payAddBedList);
				request.setAttribute("prePayAddBedList", prePayAddBedList);
				//加床信息
				List<HtlChargeBreakfast> breakfaseList = contractManage.queryBreakfast(contractId, beginDate, endDate);
				request.setAttribute("breakfaseList", breakfaseList);
				//查询某合同下的接送价信息
				List<HtlWelcomePrice> welcomePriceList = contractManage.queryWelcomePrice(contractId, beginDate, endDate);
				request.setAttribute("welcomePriceList", welcomePriceList);
			}
		}
		return foward;
	}
	
	/**
	 * 查询酒店基本信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryHotelBaseInfo(){
		Map params = super.getParams();
		String foward = (String) params.get("foward");
		if(!StringUtil.isValidStr(foward)){
			foward = "hotelBaseInfo";
		}
		if(StringUtil.isValidStr(hotelId)){
			htlHotel = hotelManage.findHotel(new Long(hotelId));
            lsRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(Long.parseLong(hotelId));
            lsCtctSel = htlHotel.getHtlCtct();
            lsSellSel = htlHotel.getSellSeason();
            lsTrafficInfo = htlHotel.getHtlTrafficInfo();
            lsBookSetup = htlHotel.getHtlBookSetup();
            lsHtlHotelExt = htlHotel.getHtelHotelExt();
            // 房态负责人
            if (0 < lsHtlHotelExt.size()) {
                HtlHotelExt hoext = new HtlHotelExt();
                for (int i = 0; i < lsHtlHotelExt.size(); i++) {
                    hoext = (HtlHotelExt) lsHtlHotelExt.get(i);
                    String roomStateManager = hoext.getRoomStateManager();
                    request.setAttribute("roomStateManager", roomStateManager);
                }
            }

            if (null != htlHotel.getAcceptCustom() && 0 < htlHotel.getAcceptCustom().length()) {
                custom = 1;
            } else {
                custom = 0;
            }
		}
		return foward;
	}
	
	/** getter and setter **/
	public HotelDelayLoadService getHotelDelayLoadService() {
		return hotelDelayLoadService;
	}

	public void setHotelDelayLoadService(HotelDelayLoadService hotelDelayLoadService) {
		this.hotelDelayLoadService = hotelDelayLoadService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public String getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(String childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	public String getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getCustom() {
		return custom;
	}

	public void setCustom(int custom) {
		this.custom = custom;
	}

	public HtlHotel getHtlHotel() {
		return htlHotel;
	}

	public void setHtlHotel(HtlHotel htlHotel) {
		this.htlHotel = htlHotel;
	}

	public List<HtlRoomtype> getLsRoomType() {
		return lsRoomType;
	}

	public void setLsRoomType(List<HtlRoomtype> lsRoomType) {
		this.lsRoomType = lsRoomType;
	}

	public List<HtlCtct> getLsCtctSel() {
		return lsCtctSel;
	}

	public void setLsCtctSel(List<HtlCtct> lsCtctSel) {
		this.lsCtctSel = lsCtctSel;
	}

	public List<SellSeason> getLsSellSel() {
		return lsSellSel;
	}

	public void setLsSellSel(List<SellSeason> lsSellSel) {
		this.lsSellSel = lsSellSel;
	}

	public List<HtlTrafficInfo> getLsTrafficInfo() {
		return lsTrafficInfo;
	}

	public void setLsTrafficInfo(List<HtlTrafficInfo> lsTrafficInfo) {
		this.lsTrafficInfo = lsTrafficInfo;
	}

	public List<HtlBookSetup> getLsBookSetup() {
		return lsBookSetup;
	}

	public void setLsBookSetup(List<HtlBookSetup> lsBookSetup) {
		this.lsBookSetup = lsBookSetup;
	}

	public List<HtlHotelExt> getLsHtlHotelExt() {
		return lsHtlHotelExt;
	}

	public void setLsHtlHotelExt(List<HtlHotelExt> lsHtlHotelExt) {
		this.lsHtlHotelExt = lsHtlHotelExt;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
}
