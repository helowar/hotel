package com.mangocity.tmc.out.impl;


import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.CustInfoData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnDtlData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnStatusData;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomAmtResponse;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomQtyResponse;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BeginData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CalAmtData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CustInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.tmc.out.ITmcOutInterface;
import com.mangocity.tmc.persistence.view.HotelInfoTmc;
import com.mangocity.tmc.persistence.view.HotelQueryConditionTmc;
import com.mangocity.tmc.persistence.view.RoomInfoTmc;
import com.mangocity.tmc.persistence.view.RoomTypeTmc;
import com.mangocity.tmc.persistence.view.SaleItemTmc;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.log.MyLog;


/**
 * 酒店提供给tmc的rmi接口,包括查询酒店和中旅接口
 * @author chenkeming
 *
 */
public class TmcOutInterfaceImpl implements ITmcOutInterface {
	
	private static final MyLog log = MyLog.getLogger(TmcOutInterfaceImpl.class);
	
	protected IHotelService hotelService;
	
	/**
	 * 中旅接口
	 * @author chenkeming Mar 17, 2009 4:00:09 PM
	 */
	private HKService hkService;
	
	
	private HotelManageWeb hotelManageWeb;
	
	
		
	/**
	 * cc查询酒店
	 * @param condition
	 * @return
	 */
	public List<HotelInfoTmc> queryHotelsForCc(HotelQueryConditionTmc condition) {
		log.info("CC开始查询酒店:" + condition.getCityId() + "," + 
				condition.getBeginDate());
		try {
			com.mangocity.hotel.base.service.assistant.HotelQueryCondition
			mangoCondition = new  com.mangocity.hotel.base.service.assistant.HotelQueryCondition();
			MyBeanUtil.copyProperties(mangoCondition, condition);
			mangoCondition.setForTmc(true);
			List liMango = hotelService.queryHotels(mangoCondition); 
			List<HotelInfoTmc> liRes = new ArrayList<HotelInfoTmc>();
			if(null != liMango && 0 < liMango.size()) {
				int nSize = liMango.size();
				for (int i = 0; i < nSize; i++) {
					com.mangocity.hotel.base.service.assistant.HotelInfo mangoHotelInfo = 
						(com.mangocity.hotel.base.service.assistant.HotelInfo) liMango.get(i);
					HotelInfoTmc hotelInfo = new HotelInfoTmc();
					MyBeanUtil.copyProperties(hotelInfo, mangoHotelInfo);
					hotelInfo.setRoomTypes(new ArrayList<RoomTypeTmc>());
					hotelInfo.setLstRoomTaxCharge(null);
					liRes.add(hotelInfo);
					List liSub = mangoHotelInfo.getRoomTypes();
					int nSizeRoomType = 0;
					if(null != liSub) {
						nSizeRoomType = liSub.size(); 
						for(int j=0; j<nSizeRoomType; j++) {
							com.mangocity.hotel.base.service.assistant.RoomType 
							mangoRoomType = (com.mangocity.hotel.base.service.assistant.RoomType) 
								mangoHotelInfo.getRoomTypes().get(j);
							RoomTypeTmc roomType = new RoomTypeTmc();
							MyBeanUtil.copyProperties(roomType, mangoRoomType);
							roomType.setSaleItems(new ArrayList<SaleItemTmc>());
							hotelInfo.getRoomTypes().add(roomType);		
							List liSaleItem = mangoRoomType.getSaleItems();
							if(null != liSaleItem) {
								int nSaleItem = liSaleItem.size();
								for(int k=0; k<nSaleItem; k++) {
									com.mangocity.hotel.base.service.assistant.SaleItem 
										mangoSaleItem = (com.mangocity.hotel.base.service.assistant.SaleItem)
											liSaleItem.get(k);
									SaleItemTmc saleItem = new SaleItemTmc();
									MyBeanUtil.copyProperties(saleItem, mangoSaleItem);
									saleItem.setRoomItems(new ArrayList<RoomInfoTmc>());
									roomType.getSaleItems().add(saleItem);
									List liRoomInfo = mangoSaleItem.getRoomItems();
									if(null != liRoomInfo) {
										int nRoomInfo = liRoomInfo.size();
										for(int l=0; l<nRoomInfo; l++) {
											com.mangocity.hotel.base.service.assistant.RoomInfo 
												mangoRoomInfo = (com.mangocity.hotel.base.service.assistant.RoomInfo)
													liRoomInfo.get(l);
											RoomInfoTmc roomInfo = new RoomInfoTmc();
											MyBeanUtil.copyProperties(roomInfo, mangoRoomInfo);
											saleItem.getRoomItems().add(roomInfo);
										}
									}
								}
							}
						}
					}
				}
			}
			if(null != liRes && 0 < liRes.size()) {
				liRes.get(0).setTotalPage(mangoCondition.getTotalPage());
			}
			return liRes;
		} catch (Exception e) {
			return new ArrayList<HotelInfoTmc>();
		}				
	}
	
	
	
	
	/**
	 * 网站查询酒店
	 * @param queryBean
	 * @return
	 */
	public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean queryBean) {
		log.info("网站开始查询酒店:" + queryBean.getCityId() + "," + 
				queryBean.getInDate());
		HotelPageForWebBean res = hotelManageWeb.queryHotelsForWeb(queryBean);
		log.info("本次查询结果酒店数:" + res.getList().size());
		return res;
	}
	
	/**
	 * 网站根据酒店ID查询某家酒店的信息
	 */
	public HotelInfoForWeb queryHotelInfoForWeb(long hotelId) {
		return hotelManageWeb.queryHotelInfoForWeb(hotelId);
	}
	
	/**
	 * 网站查询主题酒店
	 * @param qureyBean
	 * @return
	 */
	public HotelPageForWebBean queryThemeHotelsForWeb(QueryHotelForWebBean qureyBean) {
		return hotelManageWeb.queryThemeHotelsForWeb(qureyBean);
	}
	
	
	/**
	 * 根据酒店ID、入住日期、离店日来查询酒店所有房型的数量列表
	 * author:shizhongwen
	 * 日期:Mar 9, 2009
	 * 时间:5:48:05 PM
	 * @param hotelId 酒店Id
	 * @param dateFm  入住日期
	 * @param dateTo 离店日期
	 * @return List<ClassQtyData>
	 */
	public List<HKRoomQtyResponse> enqRoomQty(long hotelId,Date dateFm,Date dateTo) {
		return hkService.enqRoomQty(hotelId, dateFm, dateTo, null);
	}
			

	/**
	 * 根据酒店ID、入住日期、离店日期来查询所有房型价格的列表
	 * author:shizhongwen
	 * 日期:Mar 9, 2009
	 * 时间:6:04:12 PM
	 * @param hotelId 酒店ID
	 * @param dateFm 入住日期
	 * @param dateTo 离店日期
	 * @return List<ClassNationAmtData>
	 */
	public List<HKRoomAmtResponse> enqRoomNationAmt(long hotelId,Date dateFm,Date dateTo) {
		return hkService.enqRoomNationAmt(hotelId, dateFm, dateTo, null, null);
	}
	
	/**
	 * 根据酒店ID、入住日期、离店日期、房型编码来查询房型价格列表
	 * author:shizhongwen
	 * 日期:Mar 9, 2009
	 * 时间:6:11:06 PM	
	 * @param hotelId 酒店ID
	 * @param dateFm 入住日期
	 * @param dateTo 离店日期
	 * @param roomTypeId 房型编码
	 * @return List<NationAmtData>
	 */	
	public List<HKRoomAmtResponse> enqNationAmt(long hotelId,Date dateFm,Date dateTo,long roomTypeId) {
		return hkService.enqNationAmt(hotelId, dateFm, dateTo, roomTypeId);		
	}
	
	/**
	 * 根据酒店ID、入住日期、离店日期、房型编码、子房型编码来查询房型价格列表
	 * add by shizhongwen
	 * 时间:Mar 23, 2009  3:47:53 PM
	 * @param hotelId
	 * @param dateFm
	 * @param dateTo
	 * @param roomTypeId
	 * @param childRoomTypeId
	 * @return
	 */
	public List<HKRoomAmtResponse> enqHKAmtByNation(long hotelId,Date dateFm,Date dateTo,
			long roomTypeId,String childRoomTypeId) {
		return hkService.enqHKAmtByNation(hotelId, dateFm, dateTo, 
				roomTypeId, childRoomTypeId);		
	}
	
	/**
	 * 交易编号，查询当前或完成交易的客户信息
	 * author:shizhongwen
	 * 日期:Mar 9, 2009
	 * 时间:6:15:11 PM
	 * @param sTxnNo 交易编号
	 * @return CustInfoData
	 */
	public List<CustInfoData> enqCustInfo(String sTxnNo) {
		return hkService.enqCustInfo(sTxnNo);		
	}
	
	/**
	 * 根据交易编号来查询交易（所有交易类型）的状态
	 * author:shizhongwen
	 * 日期:Mar 9, 2009
	 * 时间:6:18:46 PM	
	 * @param sTxnNo 交易编号
	 * @return
     * @see TxnStatusType
	 */
	public TxnStatusData enqTxnStatus(String sTxnNo) {
		return hkService.enqTxnStatus(sTxnNo);		
	}
	
	/**
	 * 根据交易编号来查询目前或完成交易的清单的内容
	 * author:shizhongwen
	 * 日期:Mar 9, 2009
	 * 时间:6:19:33 PM
	 * @param sTxnNo
	 * @return
	 */
	public List<TxnDtlData> enqTxnDtl(String sTxnNo) {
		return hkService.enqTxnDtl(sTxnNo);		
	}
	
	/**
	 * 开始交易(交易之前都要调用这个操作)获得交易编号
	 * author:shizhongwen
	 * 日期:Mar 9, 2009
	 * 时间:6:42:59 PM
	 * @return
	 */
    public BeginData saleBegin() {
    	return hkService.saleBegin();    	
    }
    

    /**
     * 根据交易编号、酒店ID、日期、房型编码、价格编码、数量来预订房间（HoldRoom）
     * add by shizhongwen
     * 时间:Mar 12, 2009  5:31:32 PM
     * @param sTxnNo 交易编号
     * @param hotelId 酒店ID
     * @param dateFm  入住日期
     * @param dateTo 离店日期
     * @param roomTypeId 芒果房型
     * @param childRoomTypeId 芒果子房型
     * @param nQty  入住间数
     * @return
     */
    public BasicData holdRoom(String sTxnNo,long hotelId,Date dateFm,Date dateTo,
    		long roomTypeId, long childRoomTypeId,int nQty) {
    	return hkService.holdRoom(sTxnNo, hotelId, dateFm, dateTo, 
    			roomTypeId, childRoomTypeId, nQty);    	
    }

    /**
     * 根据密钥、交易编号来查询当前交易的净额
     * author:shizhongwen
     * 日期:Mar 10, 2009
     * 时间:4:47:46 PM
     * @param sTxnNo 交易编号
     * @return
     */
	public CalAmtData saleCalAmt(String sTxnNo) {
		return hkService.saleCalAmt(sTxnNo);		
	}
	
	/**
	 * 填写入住客户的信息。
	 * author:shizhongwen
	 * 日期:Mar 10, 2009
	 * 时间:4:49:05 PM	
	 * @param sTxnNo 交易编号
	 * @param aInfo
	 * @param sRmk 备注
	 * @return
	 */
	public BasicData saleAddCustInfo(String sTxnNo,List<CustInfo> aInfo, String sRmk ) {
		return hkService.saleAddCustInfo(sTxnNo, aInfo, sRmk);		
	}
	
	/**
	 * 承诺（确认）买卖交易
	 * author:shizhongwen
	 * 日期:Mar 10, 2009
	 * 时间:4:52:12 PM
	 * @param sTxnNo 交易编号
	 * @param nTotItem 房间数?
	 * @return
	 */
	public BasicData saleCommit(String sTxnNo,int nTotItem) {
		return hkService.saleCommit(sTxnNo, nTotItem);		
	}
	
	/**
	 * 回滚,取消订单
	 * author:shizhongwen
	 * 日期:Mar 10, 2009
	 * 时间:4:54:54 PM
	 * @param sTxnNo 交易编号
	 * @return
	 */
	public BasicData saleRollback(String sTxnNo) {
		return hkService.saleRollback(sTxnNo);		
	}
	
	
	
	

	
	
	public IHotelService getHotelService() {
		return hotelService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public HKService getHkService() {
		return hkService;
	}

	public void setHkService(HKService hkService) {
		this.hkService = hkService;
	}




	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}




	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}
}
