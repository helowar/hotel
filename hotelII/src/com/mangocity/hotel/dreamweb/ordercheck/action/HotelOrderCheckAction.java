package com.mangocity.hotel.dreamweb.ordercheck.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.mangocity.framework.creditcard.SidGenerator;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.search.util.PriceUtil;
import com.mangocity.hotel.search.vo.SaleItemVO;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.webnew.util.action.GenericWebAction;

public class HotelOrderCheckAction extends GenericWebAction {	
	private List<SaleItemVO> saleItemVOList ; //价格详情展示

	@SuppressWarnings("unchecked")
	public String execute(){
		//封装订单基本数据，还包括预订条款、客户确认方式、会员信息等 (不分面预付/面付担保单) 
		super.fillOrderBaseInfo();//该方法主要修改order，hotelOrderFromBean的属性
		OrFulfillment orFulfillment = new OrFulfillment();
		Map params = super.getParams();
		MyBeanUtil.copyProperties(orFulfillment, params);
		request.setAttribute("orFulfillment", orFulfillment);
		String payMethodTemp = hotelOrderFromBean.isPayToPrepay() ? "pre_pay" : hotelOrderFromBean.getPayMethod();
		hotelOrderFromBean.setCurrencyStr(PriceUtil.currencyMap.get(hotelOrderFromBean.getCurrency()));
		
		//updateOrderRecord();
        try{
            saleItemVOList = PriceUtil.getSaleItemVOList(order.getPriceList(), payMethodTemp, hotelOrderFromBean.getCurrency());
        }catch(Exception e){
        	log.error("query every price error",e);
        }
     //订支付方式为信用卡时，获取支付凭证
        
      if("2".equals(params.get("orderPayType")) || hotelOrderFromBean.isNeedAssure()) {
    	  String sid = SidGenerator.generate("H2");
    	  log.info("sid=" + sid);
      	  request.setAttribute("sid", sid);
      }
      
		return SUCCESS;
	}

/*	*//**
	 * create by ting.li
	 * @return
	 *//*
     private void updateOrderRecord() {

		HttpSession httpSession = request.getSession(false);
		if (httpSession != null) {
			OrderRecord orderRecord = (OrderRecord) httpSession.getAttribute("orderRecord");
			orderRecordService.updateOrderRecord(orderRecord, request, hotelOrderFromBean);
		}

	}*/
	
	public List<SaleItemVO> getSaleItemVOList() {
		return saleItemVOList;
	}

	public void setSaleItemVOList(List<SaleItemVO> saleItemVOList) {
		this.saleItemVOList = saleItemVOList;
	}



	
}
