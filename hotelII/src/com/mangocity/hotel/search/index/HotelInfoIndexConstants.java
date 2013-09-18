package com.mangocity.hotel.search.index;

public abstract class HotelInfoIndexConstants {
	public static final String INDEX_FIELD_HOTEL_ID = "HOTEL_ID";
	public static final String INDEX_FIELD_HOTEL_CHINESE_NAME = "CHN_NAME";
	public static final String INDEX_FIELD_HOTEL_ENGLISH_NAME = "ENG_NAME";
	public static final String INDEX_FIELD_HOTEL_CITY_ID = "CITY_ID";
	public static final String INDEX_FIELD_HOTEL_CITY_NAME = "CITY_NAME";
	public static final String INDEX_FIELD_HOTEL_ZONE = "ZONE";
	public static final String INDEX_FIELD_HOTEL_BIZ_ZONE = "BIZ_ZONE";

	public static final String INDEX_FIELD_HOTEL_LAYER_HIGH = "LAYER_HIGH";
	public static final String INDEX_FIELD_HOTEL_LAYER_COUNT = "LAYER_COUNT";

	public static final String INDEX_FIELD_HOTEL_STAR_LEVEL = "HOTEL_STAR";
	public static final String INDEX_FIELD_WEB_SITE = "WEBSITE";

	public static final String INDEX_FIELD_HOTEL_CHINESE_ADDRESS = "CHN_ADDRESS";

	public static final String INDEX_FIELD_HOTEL_ENGLISH_ADDRESS = "ENG_ADDRESS";

	public static final String INDEX_FIELD_HOTEL_PRACICE_DATE = "PRACICE_DATE";
	
	public static final String INDEX_FIELD_HOTEL_FITMENT_DATE = "FITMENT_DATE";

	public static final String INDEX_FIELD_HOTEL_FITMENT_DEGREE = "FITMENT_DEGREE";
	
	public static final String INDEX_FIELD_HOTEL_TELEPHONE = "TELEPHONE";
    public static final String INDEX_FIELD_HOTEL_CREDIT_CARD = "CREDIT_CARD_INFO";
    public static final String INDEX_FIELD_HOTEL_TYPE = "HOTEL_TYPE";

    public static final String INDEX_FIELD_HOTEL_INTRODUCE = "AUTO_INTRODUCE";
    
    public static final String INDEX_FIELD_HOTEL_CHINESE_INTRODUCE = "CHN_HOTEL_INTRODUCE";
    
	public static final String INDEX_FIELD_HOTEL_GROUP = "HOTEL_GROUP";
	
	public static final String INDEX_FIELD_HOTEL_ROOM_FIXTRUE = "ROOM_FIXTRUE";
	
	public static final String INDEX_FIELD_HOTEL_HANDICAPPED_FIXTRUE = "HANDICAPPED_FIXTRUE";

	public static final String INDEX_FIELD_HOTEL_MEAL_FIXTRUE = "MEAL_FIXTRUE";

	public static final String INDEX_FIELD_HOTEL_AROUND_VIEW = "AROUND_VIEW";	
	
	public static final String INDEX_FIELD_HOTEL_ALERT_MESSAGE = "ALERT_MESSAGE";
	
	public static final String INDEX_FIELD_HOTEL_NO_SMOKING_FLOOR = "NOSMOKINGFLOOR";

	public static final String INDEX_FIELD_HOTEL_FREE_SERVICE = "FREE_SERVICE";

    public static final String INDEX_FIELD_HOTEL_IS_WEB_PREPAY = "WEBPREPAYSHOW";
    
    public static final String INDEX_FIELD_HOTEL_BRAND = "BRAND";
    
    public static final String INDEX_FIELD_HOTEL_WEBSHOWBASEINFO = "WEBSHOWBASEINFO";
    
    public static final String INDEX_FIELD_HOTEL_FIRSTPAGE_RECOMMEND = "FIRSTPAGE_RECOMMEND";
    
    public static final String INDEX_FIELD_HOTEL_THEME = "THEME";
    
    public static final String INDEX_FIELD_OUT_PICTURE_NAME = "OUTPICTURENAME";
    
    public static final String INDEX_FIELD_CHECKIN_TIME = "CHECKIN_TIME";
    
    public static final String INDEX_FIELD_CHECKOUT_TIME = "CHECKOUT_TIME";
    
    public static final String INDEX_FIELD_TRAFFIC_INFO = "TRAFFICINFO";
    
    public static final String INDEX_FIELD_COMMEND_TYPE = "COMMENDTYPE";
    
    public static final String INDEX_FIELD_HOTEL_GISID = "GISID";
    
    public static final String INDEX_FIELD_HOTEL_LONGITUDE = "LONGITUDE";

    public static final String INDEX_FIELD_HOTEL_LATITUDE = "LATITUDE";
    
    public static final String INDEX_FIELD_HOTEL_ISALLNOSMOKING = "ISALLNOSMOKING";
    
    public static final String INDEX_FIELD_HOTEL_ROOM_TYPE = "ROOM_TYPE";
    
    public static final String SEPERATOR_BETWEEN_FIELDS = "!";//将一条记录拼装成string时，字段与字段之间的分隔符
    
    public static final String SEPERATOR_BETWEEN_RECORDS = "#";//将多条记录拼装成string时，每条记录与每条记录之间的分隔符
    
    public static final String SEPERATOR_BETWEEN_CMD_CODE = ",";//多个CMD代码之间的分隔符
    
    /*
     * 酒店地理信息相关的常量
     */
    public static final String INDEX_FIELD_MGSI_HOTEL_ID = "MGIS_HOTEL_ID";
    
	public static final String INDEX_FIELD_MGSI_GEO_ID = "MGIS_GEO_ID";
	
	public static final String INDEX_FIELD_MGSI_GEO_TYPE = "GEO_TYPE";
	
	public static final String INDEX_FIELD_MGSI_CITY_CODE = "CITY_CODE";
	
	public static final String INDEX_FIELD_MGSI_NAME = "NAME";
	
	public static final String INDEX_FIELD_MGSI_DISTANCE = "DISTANCE";
	
	public static final String INDEX_FIELD_MGSI_INFO_ID = "ID";
	
	
	public static final String INDEX_FIELD_GOE_ID="GOE_ID";
	public static final String INDEX_FIELD_GOE_ADDRESS = "GOE_ADDRESS";
	public static final String INDEX_FIELD_GOE_CITYCODE = "GOE_CITYCODE";
	public static final String INDEX_FIELD_GOE_CITYNAME = "GOE_CITYNAME";
	public static final String INDEX_FIELD_GOE_GISID = "GOE_GISID";
	public static final String INDEX_FIELD_GOE_GPTYPEID = "GOE_GPTYPEID";
	public static final String INDEX_FIELD_GOE_LATITUDE = "GOE_LATITUDE";
	public static final String INDEX_FIELD_GOE_LONGITUDE = "GOE_LONGITUDE";
	public static final String INDEX_FIELD_GOE_NAME = "GOE_NAME";
	public static final String INDEX_FIELD_GOE_OPERATIONDATE = "GOE_OPERATIONDATE";
	public static final String INDEX_FIELD_GOE_OPERATIONERID = "GOE_OPERATIONERID";
	public static final String INDEX_FIELD_GOE_PROVINCENAME = "GOE_PROVINCENAME";
	public static final String INDEX_FIELD_GOE_SEQNO = "GOE_SEQNO";
	public static final String INDEX_FIELD_GOE_OPERATIONER = "GOE_OPERATIONER";
	public static final String INDEX_FIELD_GOE_SORTNUM = "GOE_SORTNUM";
	

}
