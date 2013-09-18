package com.mangocity.hotel.search.album.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;

import com.mangocity.hotel.base.persistence.QueryCashBackControl;
import com.mangocity.hotel.base.service.ChannelCashBackManagerService;
import com.mangocity.hotel.ejb.search.HotelSearchEJB;
import com.mangocity.hotel.search.album.service.HotelQuotionQueryService;
import com.mangocity.hotel.search.album.vo.QuotationQueryInfoVO;
import com.mangocity.hotel.search.album.vo.QuotationSaleInfoVO;
import com.mangocity.hotel.search.dao.HotelInfoDAO;
import com.mangocity.hotel.search.dto.HotelQuotationQueryInfoDTO;
import com.mangocity.hotel.search.dto.HtlQuotationQueryDTO;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.util.PriceUtil;
import com.mangocity.hotel.search.util.SaleChannel;
import com.mangocity.hotel.search.vo.HotelResultVO;
import com.mangocity.util.DateUtil;

public class HotelQuotionQueryServiceImpl implements HotelQuotionQueryService {

	private static final Logger log = Logger.getLogger(HotelQuotionQueryServiceImpl.class);
	private HotelSearchEJB hotelSearchEJB;
	private HotelInfoDAO hotelInfoDao;
    /**
     * 返利查询service add by hushunqiang
     */
	public ChannelCashBackManagerService channelCashBackService;
	private double cashbackratevalue;

	public String queryHotelQuotionInfoByHotelId(String hotelId, String priceTypeId, Date checkInDate, Date checkOutDate,String projectcode) {
		log.info("query quotion start " + hotelId + "  " + priceTypeId);
		//1.增加返现控制，通过渠道获取相应的返现比例
		cashbackratevalue = channelCashBackService.getChannelCashBacktRate(projectcode);
		
		List<HotelQuotationQueryInfoDTO> quotationQueryInfoList;
		try {
			HotelBasicInfo hotelInfo = hotelInfoDao.queryHotelSimpleInfo(Long.parseLong(hotelId),checkInDate,checkOutDate);
			QuotationQueryInfoVO quotationQueryInfoVO = new QuotationQueryInfoVO();
			if (hotelInfo != null) {

				HtlQuotationQueryDTO quotationQueryDTO = setQuotationQueryDTO(hotelId, priceTypeId, hotelInfo.getCityId(), checkInDate, checkOutDate);
				if (quotationQueryDTO == null) {
					return null;
				} else {

					quotationQueryInfoList = hotelSearchEJB.searchHotelQuotationQueryInfo(quotationQueryDTO);

					if (quotationQueryInfoList != null && quotationQueryInfoList.size() > 0) {

						HotelQuotationQueryInfoDTO quotationQueryInfo = quotationQueryInfoList.get(0);


						quotationQueryInfoVO.setHotelId(String.valueOf(quotationQueryInfo.getHotelId()));

						quotationQueryInfoVO.setRoomTypeId(String.valueOf(quotationQueryInfo.getRoomtypeId()));
						quotationQueryInfoVO.setRoomTypeName(quotationQueryInfo.getRoomtypeName());

						quotationQueryInfoVO.setPriceTypeId(String.valueOf(quotationQueryInfo.getCommodityId()));
						quotationQueryInfoVO.setPriceTypeName(quotationQueryInfo.getCommodityName());

						quotationQueryInfoVO.setHotelChName(hotelInfo.getChnName());
						quotationQueryInfoVO.setHotelEnName(hotelInfo.getEngName());
						
						quotationQueryInfoVO.setCityCode(hotelInfo.getCityId());
						quotationQueryInfoVO.setHasReferrals(false);
						
						List<QuotationSaleInfoVO> quotationSaleInfoVOs=new ArrayList<QuotationSaleInfoVO>();
						
						QuotationSaleInfoVO payQuotationSaleInfoVO=setQuotationSaleInfoVO(quotationQueryInfoList,"pay");
						QuotationSaleInfoVO prepayQuotationSaleInfoVO=setQuotationSaleInfoVO(quotationQueryInfoList,"pre_pay");
						
						if(payQuotationSaleInfoVO!=null){
							quotationSaleInfoVOs.add(payQuotationSaleInfoVO);
						}
						if(prepayQuotationSaleInfoVO!=null){
							quotationSaleInfoVOs.add(prepayQuotationSaleInfoVO);
						}
						
						
						quotationQueryInfoVO.setSaleInfo(quotationSaleInfoVOs);

					} else {
						quotationQueryInfoVO.setHasReferrals(true);
					}
				}
			}
			else {
				quotationQueryInfoVO.setHasReferrals(true);
			}

			return JSONArray.fromObject(quotationQueryInfoVO).toString();
		} catch (Exception e) {
			log.error("queryHotelQuotionInfoByHotelId has a wrong ", e);
			return null;
		}

	}

	public List<HotelResultVO> queryHotelQuotionInfoByHotelIds(List<String> hotelIds, Date checkInDate, Date checkOutDate) {

		return null;
	}

	/**
	 * 设置销售信息，分预付、面付
	 * @param quotationQueryInfoList
	 * @param payMethod
	 * @return
	 */
	private QuotationSaleInfoVO setQuotationSaleInfoVO(List<HotelQuotationQueryInfoDTO> quotationQueryInfoList, String payMethod) {

		if (quotationQueryInfoList == null || quotationQueryInfoList.size() <= 0) {
			return null;
		} else {
			List<HotelQuotationQueryInfoDTO> quotationQueryInfoPayMethods = filterHotelQuotationQueryInfoDTOByPayMethod(quotationQueryInfoList, payMethod);

			if (quotationQueryInfoPayMethods == null || quotationQueryInfoPayMethods.size() <= 0) {
				return null;
			}

			int days = quotationQueryInfoPayMethods.size();

			HotelQuotationQueryInfoDTO quotationQueryInfoDTO = quotationQueryInfoList.get(0);

			QuotationSaleInfoVO quotationSaleInfoVO = new QuotationSaleInfoVO();
			double sumSalePrice = 0;
			double sumReturnAmount = 0;

			for (HotelQuotationQueryInfoDTO quotationQueryInfo : quotationQueryInfoPayMethods) {
				if (quotationQueryInfo.getSaleprice() < 0.1 || quotationQueryInfo.getSaleprice() > 800000) {
					days--;
				} else {
					sumSalePrice += quotationQueryInfo.getSaleprice();
				}
				sumReturnAmount += quotationQueryInfo.getReturnCash();
			}
			if (days == 0) {
				days = 1;
			}
			double avlSalePrice = PriceUtil.numSaveInDecimal(sumSalePrice / days, 1);
			double avlReturnCashNum = PriceUtil.numSaveInDecimal(sumReturnAmount / days, 0, PriceUtil.TYPE_FLOOR);

			quotationSaleInfoVO.setSalePrice(avlSalePrice);
			quotationSaleInfoVO.setRetuanAmout((avlReturnCashNum));

			//增加渠道返利控制，通过渠道设定的返现比例进行处理 add by hushunqiang
			Double lastReturnAmount = channelCashBackService.getlastCashBackAmount(cashbackratevalue, avlReturnCashNum);
			quotationSaleInfoVO.setRetuanAmout(lastReturnAmount);
			
			quotationSaleInfoVO.setPayMethod(payMethod);
			quotationSaleInfoVO.setCurrency(quotationQueryInfoDTO.getCurrency());
            boolean fagClose= "G".equals(quotationQueryInfoDTO.getCloseflag()) || "g".equals(quotationQueryInfoDTO.getCloseflag());
			if ("0".equals(quotationQueryInfoDTO.getHasbook()) || avlSalePrice < 0.1 || fagClose) {
				quotationSaleInfoVO.setCanBook(false);
			} else {
				quotationSaleInfoVO.setCanBook(true);
			}

			return quotationSaleInfoVO;

		}

	}

	/**
	 * 根据支付方式
	 * 
	 * @param quotationQueryInfoList
	 * @param payMethod
	 * @return
	 */
	private List<HotelQuotationQueryInfoDTO> filterHotelQuotationQueryInfoDTOByPayMethod(List<HotelQuotationQueryInfoDTO> quotationQueryInfoList,
			String payMethod) {
		List<HotelQuotationQueryInfoDTO> quotationQueryInfoDTOPayMethods = new ArrayList<HotelQuotationQueryInfoDTO>();

		if (quotationQueryInfoList != null && quotationQueryInfoList.size() > 0) {
			for (HotelQuotationQueryInfoDTO quotationQueryInfo : quotationQueryInfoList) {
				if (payMethod != null && payMethod.equals(quotationQueryInfo.getPaymethod())) {
					quotationQueryInfoDTOPayMethods.add(quotationQueryInfo);
				}
			}
		}

		return quotationQueryInfoDTOPayMethods;
	}

	private HtlQuotationQueryDTO setQuotationQueryDTO(String hotelId, String priceTypeId, String cityCode, Date checkInDate, Date checkOutDate) {

		if ((hotelId == null || "".equals(hotelId)) || (priceTypeId == null || "".equals(priceTypeId))) {
			return null;
		} else {
			HtlQuotationQueryDTO quotationQueryDTO = new HtlQuotationQueryDTO();
			if (checkInDate == null || checkOutDate == null) {
				checkInDate = DateUtil.getDate(DateUtil.getSystemDate(), 1);
				checkOutDate = DateUtil.getDate(DateUtil.getSystemDate(), 2);
			}
			List<String> hotelIdList = new ArrayList<String>();
			List<String> commodityIDList = new ArrayList<String>();
			List<String> cityCodeList = new ArrayList<String>();
			hotelIdList.add(hotelId);
			commodityIDList.add(priceTypeId);
			cityCodeList.add(cityCode);
			quotationQueryDTO.setHotelIdList(hotelIdList);
			quotationQueryDTO.setCommodityIDList(commodityIDList);
			quotationQueryDTO.setCityCodeList(cityCodeList);
			quotationQueryDTO.setCheckInDate(checkInDate);
			quotationQueryDTO.setCheckOutDate(checkOutDate);
			quotationQueryDTO.setFromChannel(SaleChannel.WEB);

			hotelIdList = null;
			commodityIDList = null;
			cityCodeList = null;
			return quotationQueryDTO;
		}

	}

	public void setHotelSearchEJB(HotelSearchEJB hotelSearchEJB) {
		this.hotelSearchEJB = hotelSearchEJB;
	}

	public void setHotelInfoDao(HotelInfoDAO hotelInfoDao) {
		this.hotelInfoDao = hotelInfoDao;
	}

	public ChannelCashBackManagerService getChannelCashBackService() {
		return channelCashBackService;
	}

	public void setChannelCashBackService(
			ChannelCashBackManagerService channelCashBackService) {
		this.channelCashBackService = channelCashBackService;
	}
}
