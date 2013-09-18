package com.mangocity.util.hotel.constant;

/**
 * 传真,邮件模板常量
 * 
 * @author chenkeming
 * 
 */
public interface FaxEmailModel {

    /**************** 传真部分 begin ********************/

    /** 会员部分 begin */

    /**
     * 订单确认（入住凭证）： 通知客户-芒果预定 传真模模板，入住凭证
     */
    String NOTIFY_CUSTOMER_MEMO_MANGOBOOK = "73";

    /**
     * 订单确认（入住凭证，香港，繁体字）： 通知客户-芒果预定- 香港
     */
    String NOTIFY_CUSTOMER_MEMO_MANGOBOOK_HK = "customers_mail_confirm";
     // add by diandian.hou
    /**
     * 香港繁体模板（预订）customers_mail_confirm_hk
     */
    String NOTIFY_CUSTOMER_MANGOBOOK_HK_BIGFIVE ="customers_mail_confirm_hk";
    /**
     * 香港繁体模板（取消）customers_mail_cancel_hk
     */
    String NOTIFY_CUSTOMER_MANGOCANCEL_HK_BIGFIVE ="customers_mail_cancel_hk";
    
    
    /**
     * 订单取消： 通知客户-芒果取消
     */
    String NOTIFY_CUSTOMER_MEMO_MANGOCANCEL = "74";

    /**
     * 订单取消（香港）： 通知客户-芒果取消-香港
     */
    String NOTIFY_CUSTOMER_MEMO_MANGOCANCEL_HK = "customers_mail_cancel";

    /**
     * 通知客户-114预定
     */
    String NOTIFY_CUSTOMER_MEMO_114BOOK = "mango114_mail_confirm";

    /**
     * 通知客户-114取消
     */
    String NOTIFY_CUSTOMER_MEMO_114CANCEL = "mango114_mail_cancel";

    /**
     * 通知客户-联通商旅预定
     */
    String UNICOM_ORDER_EMAIL_SEND_CONFIRM = "UnicomOrderEmailSend_confirm";

    /**
     * 通知客户-联通商旅取消
     */
    String UNICOM_ORDER_EMAIL_SEND_CANCEL = "UnicomOrderEmailSend_cancel";

    /**
     * 通知客户-网通商旅预定
     */
    String NETCOM_ORDER_EMAIL_SEND_CONFIRM = "NetcomOrderEmailSend_confirm";

    /**
     * 通知客户-网通商旅取消
     */
    String NETCOM_ORDER_EMAIL_SEND_CANCEL = "NetcomOrderEmailSend_cancel";

    /**
     * 通知客户-北京网通114电话导航预定
     */
    String BJNETCOM_ORDER_EMAIL_SEND_CONFIRM = "BjNetcom114EmailSend_confirm";

    /**
     * 通知客户-北京网通114电话导航取消
     */
    String BJNETCOM_ORDER_EMAIL_SEND_CANCEL = "BjNetcom114EmailSend_cancel";

    /**
     * 通知客户-南航电话导航预定
     */
    String NHZY_ORDER_EMAIL_SEND_CONFIRM = "NHZYEmailSend_confirm";

    /**
     * 通知客户-南航导航取消
     */
    String NHZY_ORDER_EMAIL_SEND_CANCEL = "NHZYEmailSend_cancel";
    
    /**
     * 通知客户-交行信用卡-芒果网商旅预定
     */
    String JIAOHANG_ORDER_EMAIL_SEND_CONFIRM = "JiaohangOrderEmailSend_confirm";

    /**
     * 通知客户-交行信用卡-芒果网商旅取消
     */
    String JIAOHANG_ORDER_EMAIL_SEND_CANCEL = "JiaohangOrderEmailSend_cancel";    

    /** 会员部分 end */

    /** 酒店部分 begin */

    /**
     * 通知酒店订单信息 酒店传真，确认
     */
    String NOTIFY_HOTEL_ORDER_INFO = "70";

    /**
     * 通知酒店订单修改 酒店传真，修改
     */
    String NOTIFY_HOTEL_ORDER_CHANGE = "72";

    /**
     * 通知酒店订单取消 酒店传真，取消
     */
    String NOTIFY_HOTEL_ORDER_CANCEL = "71";

    /**
     * 通知酒店订单信息 酒店传真，续住
     */
    String NOTIFY_HOTEL_ORDER_CONTINUE = "76";
    
    /**
     * 通知供应商订单信息 酒店传真，确认
     */
    String RMP_NOTIFY_HOTEL_ORDER_INFO = "70";

    /**
     * 通知供应商订单修改 酒店传真，修改
     */
    String RMP_NOTIFY_HOTEL_ORDER_CHANGE = "72";

    /**
     * 通知供应商订单取消 酒店传真，取消
     */
    String RMP_NOTIFY_HOTEL_ORDER_CANCEL = "71";

    /**
     * 通知供应商订单信息 酒店传真，续住
     */
    String RMP_NOTIFY_HOTEL_ORDER_CONTINUE = "76";

    /**
     * 通知直联酒店特殊要求信息
     */
    String NOTIFY_HOTEL_CHANNEL_SPECIAL_REQUEST = "77";

    /**
     * 通知酒店预订行程
     */
    String NOTIRY_HOTEL_BOOKING_SCHEDULE = "Hotel_Booking_Schedule";
    String HAIER_NOTIRY_HOTEL_BOOKING_SCHEDULE = "Haier_Hotel_Booking_Schedule";
    /**
     * 发送给Hair的订单审批信息
     */
    String HOTEL_HAIR_ADUIT_ORDER = "Hotel_Hair_Audit_Schedule";

    /** 酒店部分 end */

    /**************** 传真部分 end ********************/

    /**************** 邮件部分 begin ********************/
    String EMAIL_FOR_HOTELWEB_GROUP_ORDER = "99";

    /**
     * 通知酒店订单信息 酒店Email，确认
     */
    String NOTIFY_EMAIL_HOTEL_ORDER_INFO = "HotelOrderEmailSend_book";

    /**
     * 通知酒店订单信息 酒店Email，修改
     */
    String NOTIFY_EMAIL_HOTEL_ORDER_CHANGE = "HotelOrderEmailSend_modify";

    /**
     * 通知酒店订单信息 酒店Email，取消
     */
    String NOTIFY_EMAIL_HOTEL_ORDER_CANCEL = "HotelOrderEmailSend_cancel";

    /**
     * 通知酒店订单信息 酒店Email，续住
     */
    String NOTIFY_EMAIL_HOTEL_ORDER_CONTINUE = "HotelOrderEmailSend_continue";
    
    
    /****************魅影邮件部分start******************/
    /**
     * 通知酒店订单信息 酒店Email，确认
     */
    String RMP_NOTIFY_EMAIL_HOTEL_ORDER_INFO = "RMP_HotelOrderEmailSend_book";

    /**
     * 通知酒店订单信息 酒店Email，修改
     */
    String RMP_NOTIFY_EMAIL_HOTEL_ORDER_CHANGE = "RMP_HotelOrderEmailSend_modify";

    /**
     * 通知酒店订单信息 酒店Email，取消
     */
    String RMP_NOTIFY_EMAIL_HOTEL_ORDER_CANCEL = "RMP_HotelOrderEmailSend_cancel";

    /**
     * 通知酒店订单信息 酒店Email，续住
     */
    String RMP_NOTIFY_EMAIL_HOTEL_ORDER_CONTINUE = "RMP_HotelOrderEmailSend_continue";
    
    /**************** 魅影邮件部分 end ********************/
    /**************** 邮件部分 end ********************/

    /**
     * 日审传真
     */
    String DAY_CHECK_FORM = "75"; // 日审单

    /**
     * 合约组
     */
    String NOTIFY_EMAIL_CONTRACTOR = "HotelOrderEmailSend_contractor";

    /**
     * 担保单转香港组 add by diandian.hou
     */
    
    String ASSUREORDER_FROMHK = "assureOrderFromHK_NotAssured";
    /**
     * 酒店电子地图发送邮件
     */
    String HOTEL_EMAP_SEND_MAIL = "HotelEMapSend_mail";
    
    /**
     * 中旅日审邮件
     */
    String HOTEL_CTS_ORDER = "HotelCtsOrderEmail";
    
    /**
     * 中旅提醒邮件
     */
    String HOTEL_CTS_ALERT = "HotelCtsAlertEmail";
    
    /**
     * 配额满房预警发邮件
     */
    String HOTEL_QUOTA__ROOM_STATE_MAIL="HotelQuotaRoomStateMail";
    
    
    /**
     * 房控保留房协议模板
     */
    String HOTEL_PROTOCOLROOM_FAX = "78";
}
