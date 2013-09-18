package com.mangocity.util.hotel.constant;

import java.io.Serializable;

/**
 */
public class BaseConstant implements Serializable {

    /**
     * 配额面付
     */
    public static final String SFACE = "face";

    /**
     * 配额预付
     */
    public static final String SPREPAY = "prepay";

    /**
     * 普通配额
     */
    public static final String GENERAL_QUOTATYPE = "1";

    /**
     * 变价中
     */
    public static final String CP_CHANGING = "changing";

    /**
     * 已核查
     */
    public static final String CP_CHECKED = "checked";

    /**
     * 已核查
     */
    public static final String CP_QACHECK = "QAcheck";

    /**
     * 新工单
     */
    public static final String CP_NEW = "new";

    /**
     * 再次跟进
     */
    public static final String CP_AGAINCHANGE = "againChange";

    /**
     * 再次核查
     */
    public static final String CP_AGAINCHECK = "againCheck";

    /**
     * 已调整
     */
    public static final String CP_ADJUSTED = "adjusted";

    /**
     * 配置文件路径即:src/config/spring/jdbc.properties
     */
    public static final String CONFIG_FILE = "spring.jdbc";

    /**
     * EJB NAMING_FACTORY
     */
    public static final String NAMING_FACTORY = "naming_factory";

    /**
     * EJB NAMESPACE_ADDRESS
     */
    public static final String NAMESPACE_ADDRESS = "namespace_address";

    /**
     * EJB HOME_ADDRESS
     */
    public static final String HOME_ADDRESS = "home_address";

    /**
     * 酒店基础数据的父路径
     */
    public static final String HOTEL_TREEPATH = "hotel_treepath";

    /**
     * 酒店基础数据的公用读取路径
     */
    public static final String HOTEL_COMMON_TREEPATH = "hotel_common_treepath";

    /**
     * 标识酒店基础数据的读取路径
     */
    public static final String HOTEL_COMMON_TREEPATH_FLAG = "hotel_common_treepath_flag";

    /**
     * 远程基础数据国家对应的 TreePath
     */
    public static final String COUNTRY_TREEPATH = "country_treepath";

    /**
     * 远程基础数据国家对应的名称
     */
    public static final String COUNTRY_TREEPATH_NAME = "CN";

    /**
     * 远程基础数据省份对应的 TreePath
     */
    public static final String PROVINCE_TREEPATH = "province_treepath";

    /**
     * 标识本地查询共用基础数据如:国家、省份、城市、城区、商业区
     */
    public static final String COMMON_DATA_FLAG = "queryBaseData";

    /**
     * 标识国家
     */
    public static final String COUNTRY_FLAG = "country";

    /**
     * 标识省份
     */
    public static final String PROVINCE_FLAG = "province";

    /**
     * 标识城市所属区域
     */
    public static final String AREACODE = "area";

    /**
     * 标识城市所在的电子地图
     */
    public static final String PLATCODE = "mapcode";

    /**
     * 标识配送城市的值
     */
    public static final String DELIVERY_CITY = "T";

    /**
     * 标识配送城市
     */
    public static final String DELIVERY_CITY_FLAG = "hotelDeliveryCity";

    /**
     * 标识配送城市的城区
     */
    public static final String DELIVERY_CITY_DISTRICT_FLAG = "deliveryCityDistrict";

    /**
     * 标识城市
     */
    public static final String CITY_FLAG = "city";

    /**
     * 标识城区
     */
    public static final String DISTRICT_FLAG = "district";

    /**
     * 标识商业区
     */
    public static final String BUSINESS_FLAG = "business";

    /**
     * 标识展馆区
     */
    public static final String SALOON_FLAG = "saloon";

    /**
     * 合作伙伴 酒店编码
     */
    public static final String HOTELCODENAME = "hotelCodeName";

    /**
     * 合作伙伴 房型编码
     */
    public static final String ROOMTYPECODENAME = "roomTypeCodeName";

    /**
     * 合作伙伴 价格类型编码
     */
    public static final String PRICETYPECODENAME = "priceTypeCodeName";

    public static final String SZCODE = "SZ";

	public static final String USERNAME = "mango";
	
	/*
	 * 交易渠道网站
	 */
	public static final String TRANSCHANNEL_NET = "11";
	
	/*
	 * 交易渠道CC
	 */
	public static final String TRANSCHANNEL_CC = "12";
	
	
	/*
	 * 交易渠道序列号(CC)
	 */
	public static final String TRANSCHANNELSN_CC = "661201000000"; 
	
	/*
	 * 交易渠道序列号(简体网站)
	 */
	public static final String TRANSCHANNELSN_NET = "661101000000"; 
	
	/*
	 * 交易渠道序列号(繁体网站)
	 */
	public static final String TRANSCHANNELSN_BIG = "661102000000"; 
	
	/*
	 * 订单提示信息
	 */
	public static final String ORDER_EXTINFO_TIPINFO = "01";
	
	/*
	 * 订单支付凭证
	 */
	public static final String ORDER_EXTINFO_SID = "02";
	public static final String HOP_CREDIT_CONFIG="HOP_CREDIT_CONFIG";
	public static final String YOU_BI_BI_CODE="0108001";
	
	
}
