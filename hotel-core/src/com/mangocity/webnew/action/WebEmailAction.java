package com.mangocity.webnew.action;

import java.io.IOException;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.web.TranslateUtil;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.order.service.assistant.OrderFax;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.proxy.member.service.MemberInterfaceService;


public class WebEmailAction  extends GenericCCAction{
	private IOrderService orderService;
    /**
     * 酒店本部接口
     */
    private IHotelService hotelService;
    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 发送类型，确认单，修改....
     */
    private String sendtype;
    /**
     * 模版名称
     */
    private String templateType;
    /**
     * 邮件内容
     */
    private String mailContent;
    
    /**
	 * 预览邮件
	 * 
	 * @param orderId
	 * @return
	 */
	public String previewHWebMail() {
		Map params = super.getParams();
        try {
            String memberCd = getMemberCdForWeb();
            if (null == memberCd || "".equals(memberCd)) {
                return forwardError("请登陆");
            } else {
            	member = getMemberSimpleInfoByMemberCd(memberCd, true);
                if (null == member) {
                    return forwardError("请登陆");
                } else {

                    if (null != params.get("memberCD") && null != params.get("memberId")) {
                        if (!member.getMembercd().equals(params.get("memberCD").toString())) {
                            return forwardError("请重新登陆");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getMemberInfoForWeb is Error" + e);
        }

		OrOrder order = orderService.getCustomOrderForMail(new Long(orderId));

		// 获取订单的member供预览页面用
		MemberDTO member = null;
		try {
			member = memberInterfaceService.getMemberByCode(order.getMemberCd());
		} catch (Exception e) {
			e.printStackTrace();
			return "获取订单会员信息错误！";
		}
		
		
		HtlHotel hotel = hotelService.findHotel(order.getHotelId().longValue());

		Integer sSendtype = Integer.parseInt(sendtype);
		String xmlString = "";
		OrderFax orderFax = null;
		String fileName = ServletActionContext.getRequest().getRealPath("/html");
	    fileName += "/hotelOrder";
        if (sSendtype == MemberConfirmType.CONFIRM) {
            orderFax = msgAssist.genOrderFaxMailByGuestMangoConfirm(order, hotel, member, "");
            fileName += "Confirm";
        }
        
        	fileName += "_zh_cn.htm";
		
		try {
			xmlString = msgAssist.getHolteOrderMailHtml(orderFax, fileName);
		} catch (IOException e) {
			xmlString = "无法查看";
		}
        mailContent = xmlString;
        // 调用公共的ajaxHandlerService具体处理ajax的请求，并输出结果
		return "preview";
	}
	
	public String printConfirm(){
		return "print";
	}
	public IHotelService getHotelService() {
		return hotelService;
	}

	public void setHotelService(IHotelService hotelService) {
		this.hotelService = hotelService;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public MemberInterfaceService getMemberInterfaceService() {
		return memberInterfaceService;
	}

	public void setMemberInterfaceService(
			MemberInterfaceService memberInterfaceService) {
		this.memberInterfaceService = memberInterfaceService;
	}

	public MsgAssist getMsgAssist() {
		return msgAssist;
	}

	public void setMsgAssist(MsgAssist msgAssist) {
		this.msgAssist = msgAssist;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public String getSendtype() {
		return sendtype;
	}

	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public TranslateUtil getTranslateUtil() {
		return translateUtil;
	}

	public void setTranslateUtil(TranslateUtil translateUtil) {
		this.translateUtil = translateUtil;
	}
}
