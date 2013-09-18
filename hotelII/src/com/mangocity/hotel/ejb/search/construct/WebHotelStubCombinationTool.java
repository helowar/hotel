package com.mangocity.hotel.ejb.search.construct;

import com.mangocity.hotel.search.constant.NotBookReason;
import com.mangocity.hotel.search.dto.CommoditySummaryDTO;
import com.mangocity.hotel.search.dto.HotelInfoDTO;
import com.mangocity.hotel.search.dto.HotelQuotationDTO;
import com.mangocity.hotel.search.dto.HtlQuotationQueryDTO;
import com.mangocity.hotel.search.dto.RoomTypeDTO;
import com.mangocity.hotel.search.dto.SaleItemDTO;
import com.mangocity.hotel.search.handler.AbstractHotelQueryHandler;
import com.mangocity.hotel.search.util.HotelHandlerUtil;
import com.mangocity.hotel.search.util.HotelStubCombinationTool;
import com.mangocity.util.bean.CurrencyBean;

public class WebHotelStubCombinationTool extends HotelStubCombinationTool {
	public static void finishFillCommodity(HotelQuotationDTO curComm, RoomTypeDTO curRoom, HotelInfoDTO curHotel, 
			HtlQuotationQueryDTO  queryDTO, AbstractHotelQueryHandler handler) {		

		if(null == curRoom) return;
		/**
		 * 1. 判断商品能否预订(每天的情况) 
		 * 2. 确定商品的直联方式,商品的直联方式暂时取首日的直联方式,完全可以满足当前和未来很长时间的业务需要
		 * 3. 商品能否预订 
		 * 
		 */
		boolean canbook = false;
		boolean isRoomful = false;
		boolean canBookBed[] = {true, true, true};
		StringBuilder cantbookReason = new StringBuilder("");
		String notSatisfyClauseReason = "";
		for(SaleItemDTO curInfo : curComm.getSaleInfoList()) {
			
			if(curInfo.isRoomful()) {
				isRoomful = true;
			}
			
//			if (0 == DateUtil.getDay(curInfo.getAbleDate(), queryDTO.getCheckInDate())) {// 入住首日
				curComm.setHdltype(curInfo.getHdltype());
//			}
			
			int nBedType = Integer.parseInt(curInfo.getBedtype()) - 1;
			
			if(!canBookBed[nBedType]) {
				continue;
			}
			
			setCanbookPerDay(curInfo, queryDTO);// 每一天能否预订

			if (!curInfo.isBookEnAble()) {
				canBookBed[nBedType] = false;
				String reason = curInfo.getReasonOfDisableBook();
				cantbookReason.append(reason).append(",");
				if(NotBookReason.NotSatisfyClause.equals(reason)) {
					notSatisfyClauseReason = curInfo.getNotsatisfyClauseOfReason();
				}
			}
									
		}
		StringBuilder bedTypes = new StringBuilder("");
		String bedAll = curComm.getBedAll();
		for(int i=0; i<canBookBed.length; i++) {
			if(canBookBed[i] && 0 <= bedAll.indexOf("" + (i+1))) {
				canbook = true;
				bedTypes.append(i + 1).append(",");
			}
		}
		curComm.setCanBook(canbook);
		curComm.setRoomFull(isRoomful);
		if(canbook) {
			bedTypes.deleteCharAt(bedTypes.length() - 1);					
			curComm.setBedtype(bedTypes.toString());	
		} else {
			curComm.setBedtype(bedAll);
			curComm.setCantbookReason(getCantBookReasonByCommodity(cantbookReason
					.toString(), notSatisfyClauseReason));	
		}
		

		/**
		 * 商品是否显示
		 */
		showCommodity(curComm, curHotel, queryDTO.getFromChannel());


		// 更新该商品所属房型的能否预订属性
		if (!curRoom.isCanbook() && curComm.isCanBook()) {
			curRoom.setCanbook(true);
		}

		// 更新该商品所属房型的最低价和最高价
		double rate = CurrencyBean.getRateToRMB(curComm.getCurrency());		
		double minPriceCom = Math.ceil(curComm.getMinPirceRoomType()*rate*10)/10;
		double maxPriceCom = Math.ceil(curComm.getMaxPriceRoomType()*rate*10)/10;
		if(!curComm.isShow()){//如果外网不显示该商品，则该商品就不参与最低价和最高价的计算，与外网一致
			minPriceCom=0;
			maxPriceCom=0;
		}
			
		if(0.1 < minPriceCom) {
			double minPrice = curRoom.getMinPrice();
			if (0.1 < minPrice) {
				if(minPrice > minPriceCom) {
					curRoom.setMinPrice(minPriceCom);
					curRoom.setQuotationId(curComm.getQuotationId());
				}
			} else {
				curRoom.setMinPrice(minPriceCom);
				curRoom.setQuotationId(curComm.getQuotationId());
			}
		}
		
		if(0.1 < maxPriceCom) {
			double maxPrice = curRoom.getMaxPrice();
			if (0.1 < maxPrice) {
				if(maxPrice < maxPriceCom) {
					curRoom.setMaxPrice(maxPriceCom);
				}
			} else {
				curRoom.setMaxPrice(maxPriceCom);
			}
		}
		
		//更新该商品所属房型的是否含早信息
		if(curComm.getBreakfastnumber()>0||curComm.getBreakfastnumber()==-1) {
			curRoom.setHasBreakfast(true);
		}

		// 回调结束处理该商品信息
		CommoditySummaryDTO curComSummary = new CommoditySummaryDTO();
		curComSummary.setMinPirceRoomType(minPriceCom);
		curComSummary.setMaxPriceRoomType(maxPriceCom);
		curComSummary.setHdltype(curComm.getHdltype());
		curComSummary.setShow(curComm.isShow());
		curComSummary.setCanBook(curComm.isCanBook());
		curComSummary.setCantbookReason(curComm.getCantbookReason());
		if(!curComSummary.isRoomful()) {
			curComSummary.setRoomful(curComm.isRoomFull());
		}
		curComSummary.setBedType(curComm.getBedtype());
//		curComSummary.setBandInfo(curComm.isFreeNet() ? "免费" : (curRoom.isHasNet() ? "收费" : "无"));// 是否含宽带
		curComSummary.setBandInfo(curRoom.isHasNet() ? (curComm.isFreeNet()?"免费":"收费"):"无");// 是否含宽带
		curRoom.setHdltype(HotelHandlerUtil.hdlTypeTransformation(curRoom.getHdltype(), curComSummary.getHdltype()));
		handler.endHandleQuotation(curComSummary);
	}
	
}
