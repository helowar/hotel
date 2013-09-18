/*
 * Created on 2005-10-24 11:19:53 文件名：HotelFaxManager.java
 */
package com.mangocity.hotel.order.manager;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.HotelConfirmType;
import com.mangocity.hotel.order.constant.MemberConfirmType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.assistant.MemberAliasConstants;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Fax;

/**
 * <p>
 * Description:酒店传真的管理器类，封装对传真的操作
 * </p>
 * <p>
 * Company:ibm
 * </p>
 * 
 * @author xurong
 * @version 1.0
 */
public class HotelFaxManager {
	private static final MyLog log = MyLog.getLogger(HotelFaxManager.class);
    private HotelManage hotelManage;

    private CommunicaterService communicaterService;

    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;

    /**
     * 给酒店发送传真
     * 
     * @param modifiedInfo
     * @param order
     * @param taskId
     * @param orderItemGroupByList
     * @return
     */
    public Long sendNotifyHotelOrderInfoFax(Map modifiedInfo, OrOrder order, Long ID,
        String taskId, List orderItemGroupByList, String isAnewSend, UserWrapper roleUser) {

        String faxNo = (String) modifiedInfo.get("faxNum");
        String faxType = (String) modifiedInfo.get("faxType");

        // 无传真号
        if (null == faxNo || 0 == faxNo.length()) {
            // throw new Exception("发送酒店预订通知(后加房)传真失败，没有酒店传真号！");
        }

        if (null == faxType || 0 == faxType.length()) {
            // throw new Exception("发送酒店预订通知(后加房)传真失败，没有酒店传真号！");
        }

        HtlHotel hotel = hotelManage.findHotel(order.getHotelId().longValue());
        // String xmlString = msgAssist.genOrderDocumentByHotelFax(order, orderItemGroupByList);
        String templateNo = "";
        String xmlString = "";
        int nFaxType = Integer.parseInt(faxType);
        if (nFaxType == HotelConfirmType.CONFIRM) {
            templateNo = FaxEmailModel.NOTIFY_HOTEL_ORDER_INFO; // "70";
            xmlString = msgAssist.genOrderFaxByHotelFaxConfirm(order, hotel, isAnewSend, ID,
                orderItemGroupByList, modifiedInfo);
        } else if (nFaxType == HotelConfirmType.MODIFY) {
            templateNo = FaxEmailModel.NOTIFY_HOTEL_ORDER_CHANGE; // "72";
            xmlString = msgAssist.genOrderFaxByHotelFaxModify(order, hotel, isAnewSend, ID,
                orderItemGroupByList, modifiedInfo);

        } else if (nFaxType == HotelConfirmType.CANCEL) {
            templateNo = FaxEmailModel.NOTIFY_HOTEL_ORDER_CANCEL; // "71";
            xmlString = msgAssist.genOrderFaxByHotelFaxCancel(order, hotel, isAnewSend, ID,
                orderItemGroupByList, modifiedInfo);
        } else if (nFaxType == HotelConfirmType.CONTINUE_TO_LIVE) {
            templateNo = FaxEmailModel.NOTIFY_HOTEL_ORDER_CONTINUE; // "73";
            xmlString = msgAssist.genOrderFaxByHotelFaxConfirm(order, hotel, isAnewSend, ID,
                orderItemGroupByList, modifiedInfo);
        } else if (nFaxType == HotelConfirmType.CHANNLE_SPECIAL_REQUEST) {
            /**
             * hotel2.5 将特殊要求传送给酒店
             */
            templateNo = FaxEmailModel.NOTIFY_HOTEL_CHANNEL_SPECIAL_REQUEST; // "直联酒店特殊要求";
            xmlString = msgAssist.genSpecitalRequestFaxHotel(order, hotel, isAnewSend, ID,
                orderItemGroupByList, modifiedInfo);

        }

        Fax fax = new Fax();
        fax.setXml(xmlString);
        fax.setApplicationName("hotel");
        fax.setTemplateFileName(templateNo);
        fax.setTo(new String[] { faxNo });
        fax.setFrom(roleUser.getLoginName());
        
        Long ret = 0L;
        
        try{
        	ret = communicaterService.sendFax(fax); // 需要返回一个渠道实例号
        }catch(Exception e){
        	log.error(e.getMessage(),e);
        	ret = 0L;
        }

        return ret;
    }

    /**
     * 给客人发送传真
     * 
     * @param modifiedInfo
     * @param order
     * @param taskId
     * @param orderItemGroupByList
     * @return
     */
    public Long sendNotifyGuestOrderInfoFax(Map modifiedInfo, OrOrder order, String taskId,
        List orderItemGroupByList, MemberDTO member, UserWrapper roleUser) {

        String faxNo = (String) modifiedInfo.get("faxNum");
        String faxType = (String) modifiedInfo.get("faxType");
        // String sender = (String)modifiedInfo.get("sender");
        String sender = "0";
        // 无传真号
        if (null == faxNo || 0 == faxNo.length()) {

        }

        if (null == faxType || 0 == faxType.length()) {

        }

        HtlHotel hotel = hotelManage.findHotel(order.getHotelId().longValue());
        String templateNo = "";
        String xmlString = "";
        int nFaxType = Integer.parseInt(faxType);

        // modify by chenkeming
        if(!order.isCooperateOrder()) { // 非交行全卡商旅等渠道订单
            if (member.isMango()||"XJG".equals(order.getMemberState())) {
                if (nFaxType == MemberConfirmType.CONFIRM) {
                    templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK;
                    xmlString = msgAssist.genOrderFaxByGuestMangoConfirm(order, hotel,
                        orderItemGroupByList, member, sender);

                } else if (nFaxType == MemberConfirmType.CANCEL) {
                    templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL;
                    xmlString = msgAssist.genOrderFaxByGuestMangoCancel(order, hotel,
                        orderItemGroupByList, member, sender);
                }
            } else { // 114

                if (MemberAliasConstants.LTT.equals(member.getAliasid())) {// 联通商旅 　　LTT
                    sender = "4";
                } else if (MemberAliasConstants.WTT.equals(member.getAliasid())) {// 　中国网通116114　WTT
                    sender = "3";
                } else if (MemberAliasConstants.WTBJ.equals(member.getAliasid())) {// 北京网通114电话导航　WTBJ
                    sender = "5";
                } else if (MemberAliasConstants.NHZY.equals(member.getAliasid())) {// 南航电话导航　NHZY
                    sender = "6";
                } else if (MemberAliasConstants.GDLT.equals(member.getAliasid())) {// 南航电话导航　NHZY
                    sender = "7";
                }else { // 114
                    sender = "2";
                }

                if (nFaxType == MemberConfirmType.CONFIRM) {
                    templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK;
                    xmlString = msgAssist.genOrderFaxByGuest114Confirm(order, hotel,
                        orderItemGroupByList, member, sender);
                } else if (nFaxType == MemberConfirmType.CANCEL) {
                    templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL;
                    xmlString = msgAssist.genOrderFaxByGuest114Cancel(order, hotel,
                        orderItemGroupByList, member, sender);
                }
            }	
        } else { // 交行全卡商旅等渠道订单
        	sender = "8";
            if (nFaxType == MemberConfirmType.CONFIRM) {
                templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOBOOK;
                xmlString = msgAssist.genOrderFaxByGuest114Confirm(order, hotel,
                    orderItemGroupByList, member, sender);
            } else if (nFaxType == MemberConfirmType.CANCEL) {
                templateNo = FaxEmailModel.NOTIFY_CUSTOMER_MEMO_MANGOCANCEL;
                xmlString = msgAssist.genOrderFaxByGuest114Cancel(order, hotel,
                    orderItemGroupByList, member, sender);
            }
        }

        Fax fax = new Fax();
        fax.setXml(xmlString);
        fax.setApplicationName("hotel");
        fax.setTemplateFileName(templateNo);
        fax.setTo(new String[] { faxNo });
        fax.setFrom(roleUser.getLoginName());
        Long ret = communicaterService.sendFax(fax); // 需要返回一个渠道实例号

        return ret;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public MsgAssist getMsgAssist() {
        return msgAssist;
    }

    public void setMsgAssist(MsgAssist msgAssist) {
        this.msgAssist = msgAssist;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

}
