package com.mangocity.hotel.order.web;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.CurrencyBean;

/**
 * 供配送查看订单信息
 * 
 * @author chenkeming
 * 
 */
public class FulfillShowOrderAction extends OrderAction {

    private static final long serialVersionUID = 8348100220871591684L;

    private IHotelService hotelService;
    /**
     * 币种符号
     */
    private String idCurStr;

    /**
     * 查看订单信息
     * 
     * @return
     */
    public String show() {
        Map params = getParams();
        order = orderService.getCustomOrderByOrderCD((String) params.get("orderCD"), new String[] {
            "orderItems", "fulfill" });
        if (null == order) {
            return forwardError("该订单不存在！");
        }

        idCurStr = CurrencyBean.idCurMap.get(order.getPaymentCurrency());

        return "success";
    }

    /**
     * 查看入住凭证
     * 
     * @return
     */
    public String showFax() {
        Map params = getParams();
        order = orderService.getCustomOrderByOrderCD((String) params.get("orderCD"), new String[] {
            "fellowList", "remark" });
        if (null == order) {
            return forwardError("该订单不存在！");
        }

        HtlHotel hotelInfo = hotelService.findHotel(order.getHotelId().longValue());
        request.setAttribute("hotelInfo", hotelInfo);

        List orderItemGroupByList = orderService.hQueryOrderItemByFaxGroup(order.getID());

        // 获取订单的member供预览页面用
        try {
        	member = memberInterfaceService.getMemberByCode(order
                .getMemberCd());
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            return forwardError("获取订单会员信息错误！");
        }

        String nameInfo = "";
        List fellowList = order.getFellowList();
        for (int i = 0; i < fellowList.size(); i++) {
            OrFellowInfo fellow = (OrFellowInfo) fellowList.get(i);
            // TODO: 国籍的处理
            nameInfo += fellow.getFellowName() + "(" + fellow.getFellowNationality() + ")      ";
        }
        request.setAttribute("customerInfo", nameInfo);

        int nightCount = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        request.setAttribute("nightCount", nightCount);

        request.setAttribute("faxType", "1");
        request.setAttribute("orderItemGroupByList", orderItemGroupByList);

        return "showFax";
    }

    public String getIdCurStr() {
        return idCurStr;
    }

    public void setIdCurStr(String idCurStr) {
        this.idCurStr = idCurStr;
    }

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

}
