package com.mangocity.tmc.service.constant;



import java.util.HashMap;
import java.util.Map;


public class HotelOrderConstans {
	public static Map <String,String>textCodeMap = new HashMap();
	
	
	//--------------------------------------------------------------用于记录整个系统范围内的订单,报告单是否被打开,锁定---------
	 //打开状态
	public static String STATUS_OPEN = "open";
	
	//锁定状态
	public static String STATUS_LOCKED = "locked";
	
	//查询操作
	public static String OPERATION_SELECT = "select";
	
	//获取操作
	public static String OPERATION_GET = "get";
	
	
	//--------------------------------------------------------------订单业务状态常量---------
    //订单草稿
	public static final String ORDER_STATUS_DRAFT = "draft";
	
	//已生成订单
	public static final String ORDER_STATUS_CREATE = "create";
	
	//已通知供应商未通知客户
	public static final String ORDER_STATUS_HOTEL = "hotel";
	
	//已通知客户未通知供应商
	public static final String ORDER_STATUS_CUSTOMER = "customer";
	
	//未确认
	public static final String ORDER_STATUS_UNSURENESS = "unsureness";
	
	//已成交未绑定
	public static final String ORDER_STATUS_UNIT = "unit";
	
	//已成交已绑定
	public static final String ORDER_STATUS_BINDING = "binding";
	
	//已结算
	public static final String ORDER_STATUS_BALANCE = "balance";
	
	//已取消
	public static final String ORDER_STATUS_CANCLE = "cancel";
	
	//因修改取消
	public static final String ORDER_STATUS_SPECIALCANCLE = "specialCancel";
	
   //因超时取消
	public static final String ORDER_STATUS_OVERTIMECANCLE = "overtimeCancel";

	//--------------------------------------------------------------订单来源---------
	//电话
	public static final String ORDER_SOURCE_PHONE = "phone";
	//网站
	public static final String ORDER_SOURCE_NET= "NET";
	
	//--------------------------------------------------------------订单紧急程度常量---------
	//紧急
	public static final String DEGREE_EXIGENCE = "MGC";
	
	//一般
	public static final String DEGREE_GENERIC = "NML";
	
	//--------------------------------------------------------------订单是否确认有房---------
	//已确认有房
	public static final String CONFIRM_ROOM_YES = "1";
	
	//未确认或者无房
	public static final String CONFIRM_ROOM_NO = "0";
	
	//--------------------------------------------------------------出行性质---------
    //因公出差
	public static final String EVECTION_PUBLIC = "public";
	
    //个人私行
	public static final String EVECTION_PRIVATE = "private";
	
	//--------------------------------------------------------------订单支付方式---------
    //面付
	public static final String PAYMENT_PRESENT = "pay";
	
    //预付
	public static final String PAYMENT_ADVANCE = "pre_pay";
	
	//面付转预付
	public static final String PAYMENT_CONVERSION = "conversion";

	//--------------------------------------------------------------订单类型---------
    //本部房型订单
	public static final String ORDER_TYPE_BASE = "base";
	
    //协议房型订单
	public static final String ORDER_TYPE_PROTOCOL = "protocol";
	
	//外购房型订单
	public static final String ORDER_TYPE_OUT = "outsourcing";
	
	//-----------------------------------------------------------------房间费用类别---------------
    //房费(散客价)
	public static final String FEE_SALEPRICE = "saleprice";
	//折扣价
	public static final String FEE_REBATEPRICE = "rebateprice";
    //房费(底价)
	public static final String FEE_BASEPRICE = "baseprice";
	
	//上网费
	public static final String FEE_NETWORK = "network";

	
    //政府基金
	public static final String FEE_GOVFUND = "govfund";
	
    //税费
	public static final String FEE_REVENUE = "revenue";
	
	//服务费
	public static final String FEE_SERVICE = "service";
	
    //加床费
	public static final String FEE_BED = "bed";
	
    //中式早餐
	public static final String FEE_CHINA_B = "china_b";
	
	//西式早餐
	public static final String FEE_ENG_B = "eng_b";
	//自助早餐
	public static final String FEE_SELF_B = "self_b";
	
	//-------------------------- 担保类型 ------------------------------
	//最晚担保
	public static final String ASSUREE_TYPE1 = "1";
    //	首日担保
	public static final String ASSUREE_TYPE2 = "2";
    //	峰时担保
	public static final String ASSUREE_TYPE3 = "3";
    //	全额担保
	public static final String ASSUREE_TYPE4 = "4";
	
	//----------------------------------扣款类型----------------------------
	//首日金额
	public static final String DEDUCT_TYPE1 = "01";
//	全额金额
	public static final String DEDUCT_TYPE2 = "02";
//	最高价格百分比
	public static final String DEDUCT_TYPE3 = "03";
//	首日百分比
	public static final String DEDUCT_TYPE4 = "04";
	
	//-------------------------------------信用卡,预付取消/修改类型-----------------------------------
//	取消
	public static final String OPERATE_TYPE1 = "01";
//	修改
	public static final String OPERATE_TYPE2 = "02";
//	取消或修改
	public static final String OPERATE_TYPE3 = "03";
	
	//--------------------------------------------------------------该房型与酒店结算的方式---------
    //GOA 前台现付返佣
	public static final String MNGHOTEL_BALANCE_01 = "01";
	
	//COA 客人到之前
	public static final String MNGHOTEL_BALANCE_02= "02";
   //COD 退房前付
	public static final String MNGHOTEL_BALANCE_03 = "03";
	
	//SEND BILL 月结房费
	public static final String MNGHOTEL_BALANCE_04= "04";
	
	//--------------------------------------------------------------房源定义---------
	//本部房源
	public static final String HOTEL_SOURCE_HOTEL = "hotel";
	//协议房源
	public static final String HOTEL_SOURCE_TMC = "tmc";
	//全部房源
	public static final String HOTEL_SOURCE_BOTH = "both";
	
	
	//--------------------------------------------------------------------床类型-----------------
	//大床
	public static final String BED_TYPE_01 = "1";	
	//双床 
	public static final String BED_TYPE_02 = "2";
    //单床 
	public static final String BED_TYPE_03 = "3";
	
	
	//---------------------------------------------------------------- 配额类型 --------------
	//普通配额
	public static final String QUOTA_TYPE_GENERALgeneral= "1";	
	//包房配额
	public static final String BED_TYPE_INCLUDE= "2";
    //临时配额
	public static final String BED_TYPE_TEMP = "3";
	//呼出配额
	public static final String BED_TYPE_CALL = "4";
	
	
	//-----------------------------------------------------------------房态-------------------
    //freesale
	public static final String ROOM_STATUS_FREESALSE= "0";	
	//良好
	public static final String ROOM_STATUS_NICER= "1";
    //紧张
	public static final String ROOM_STATUS_ATWITTER = "2";
	//不可超
	public static final String ROOM_STATUS_IMPASSABLE = "3";
	//满房
	public static final String ROOM_STATUS_ROOMFUL = "4";
	
//	-----------------------------------------------------------------生成IT订单编码的因子-------------------
    //商旅机票 
	public static final int TMC_ITCODE_TICKET= 12;	
	//商旅酒店 
	public static final int TMC_ITCODE_HOTEL =  32;	
	
	//----------------------------------------------------------------发送传真 邮件 手机显示的模板号-------------------
    //发给酒店的预定单模板号
	public static final String TEMPLET_HOTEL_ORDER= "order";	
	//发给酒店的取消单模板号
	public static final String TEMPLET_HOTEL_CANCEL= "orderCancel";
    //发给酒店的修改单模板号
	public static final String TEMPLET_HOTEL_MODIFY = "orderModify";
	//发给客人的预定单模板号
	public static final String TEMPLET_CUSTOMER_ORDER = "customer";
	//发给客人的修改单模板号
	public static final String TEMPLET_CUSTOMER_MODIFY = "customerModify";
	//发给客人的修改单模板号
	public static final String TEMPLET_CUSTOMER_CANCEL = "customerCancel";
	//
	public static final String TEMPLET_MAIL_CONTENT="contractHint";
	
	//----------------------------------------------------------------消息对象类型-------------------
    //客户
	public static final String MSG_TARGETTYPE_CUSTOMER = "customer";	
	//酒店
	public static final String MSG_TARGETTYPE_HOTEL = "hotel";
	
	//----------------------------------------------------------------消息发送方式-------------------
    //邮件
	public static final String MSG_SENDSTYLE_EMAIL = "5";	
	//传真
	public static final String MSG_SENDSTYLE_FAX = "6";
	//手机
	public static final String MSG_SENDSTYLE_MIBLE = "1";
	
	//----------------------------------------------------------------消息发送类型-------------------
    //预定
	public static final String MSG_SENDTYPE_ORDER = "1";	
	//修改
	public static final String MSG_SENDTYPE_MODIFY = "2";
	//取消
	public static final String MSG_SENDTYPE_CANCEL = "3";
	
	//----------------------------------------------------------------消息发送结果-------------------
    //成功
	public static final String MSG_SENDRESULT_SUCCEED = "1";	
	//失败
	public static final String MSG_SENDRESULT_DEFEAT = "0";
	
//	----------------------------------------------------------------货币类型-------------------
    //RMB
	public static final String TMC_CURRENCY_TYPE = "RMB";	
	
//	----------------------------------------------------------------日审订单状态-------------------	
	   /** 
  * 已入住 
  */ 
 public static int CHECKIN = 4; 
 
 /** 
  * 提前退房 
  */ 
 public static int EARLY_QUIT = 5; 
 
 /** 
  * 正常退房 
  */ 
 public static int NORMAL_QUIT = 6; 
 
 /** 
  * 延住 
  */ 
 public static int EXTEND = 7; 
 
 /** 
  * NOSHOW 
  */ 
 public static int NOSHOW = 13;
 
 //--------------add 2008-06-26 start-------------------
 /**
  * 撤消
  */
 public static int CANTERFORCC = 14;
 //----------------------end----------------------------
 
//	----------------------------------------------------------------日审结果-------------------	
 /** 
  *已入住 
  */ 
 public static int ORDERITEM_CHECKIN= 1;
 /** 
  * 未入住 
  */ 
 public static int ORDERITEM_UNCHECKIN = 2;

 
 
//	----------------------------------------------------------------日审操作状态 -------------------	

 /** 
  * 未操作 
  */ 
 public static int NOT_WORK = 0; 
 
 /** 
  * 已保存 
  */ 
 public static int ALREADY_SAVE = 1; 
 
 /** 
  * 待审核 
  */ 
 public static int STAY_AUDITING = 2; 
 
 /** 
  * 完成 
  */ 
 public static int ACHIEVE = 3; 
 
 //--------------------------------------酒店星级-----------
    
 //5星级
public static String HOTEL_LEVLE19 ="19"; 

//准5星
public static String HOTEL_LEVLE29 ="29"; 

//4星级
public static String HOTEL_LEVLE39 ="39"; 

///准4星
public static String HOTEL_LEVLE49 ="49"; 

//3星级
public static String HOTEL_LEVLE59 ="59"; 

//2星级
public static String HOTEL_LEVLE69 ="69";

//----------------------------------------------------------------发送价格方式-------------------
//1:发送底价(折扣价)
public static final String TMC_SENDPRICIETYPE_REBATE = "1";	
//2:发送面价
public static final String TMC_SENDPRICIETYPE_SALE = "2";
//----------------------------------------------------------------是否含(如早餐 税费等)-------------------
//1:含
public static final String TMC_INCLUD_YES = "1";	
//0:不含
public static final String TMC_INCLUD_NO = "0";

//----------------------------------------------------------------配额模式-------------------
//进店模式
public static final String QUOTA_MODEL_IN = "S-I";	
//住店模式
public static final String QUOTA_MODEL_AT = "C-I";	

//----------------------------------------------------------------结算方式(底价加服务费,卖价返点)----------
//1:卖价返点 
public static final String TMC_FOOTWAY_SALEPRICE = "1";	
//2:底价加服务费
public static final String TMC_FOOTWAY_BASEPRICE = "2";

/**
 * 酒店的CDM读取路径
 */
public static final String CDM_READ_HOTEL = "/root/comment/area" ;

}
