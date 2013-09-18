package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import com.mangocity.model.mbrship.MbrshipCategory;

/**
 * 初始化调用 EJB 远程基础数据
 * 
 * @author yuexiaofeng
 * 
 */
public abstract class InitServlet extends HttpServlet {

   
    /**
	 * 香港和澳门的三字编码
	 * modify by chenkeming@2008-02-10 城市编码写死,因为初始化有可能没取到该数据
	 */
    public static String HK_CODE = "HKG"; 
    public static String MA_CODE = "MAC";   
    
    //到到网提供的xml点评信息存放位置
    public static String SAVE_ROOTPATH ="";

    // 114历史订单信息专用
    public static String save114Path = "";

    public static String save114WebPath = "/upload/download114/";

    public static boolean save114Can = false;

    // 号百调用webservice接口传递历史订单信息
    public static boolean save114WebServiceCan = true;

    public static String saveHWEBpath = "";

    public static boolean saveMainCan = false;

    public static boolean savePopularCan = false;
    
    public static String PICTUREURL = "";  //新图片库地址
    
    /**
	 * 热门城市所属区域
	 */
	public static Map<String,String> cityAreaObj=new HashMap<String,String>();

    /**
     * 对所有酒店用到的基础公共数据进行封装主要用于数据显示用
     */
    public static Map<String, String> BaseDataObj = new HashMap<String, String>();

    /**
     * 对所有公共数据城市进行 Key=code Value=name 封装包括国内、国外并且 Value 由拼音字母和名称组成
     */
    public static Map<String, String> allObj = new HashMap<String, String>();

    /**
     * 对所有公共数据城市进行 Key=code Value=name 封装仅包括国内并且 Value 由拼音字母和名称组成
     */
    public static Map<String, String> localCityObj = new HashMap<String, String>();

    /**
     * 对所有公共数据省份进行 Key=code Value=name 封装仅包括国内
     */
    public static Map<String, String> localProvinceObj = new HashMap<String, String>();

    /**
     * 对所有公共数据国家进行 Key=code Value=name 封装
     */
    public static Map<String, String> CountryObj = new HashMap<String, String>();

    /**
     * 对所有公共数据省份进行 Key=code Value=name 封装包括国内、国外
     */
    public static Map<String, String> ProvinceObj = new HashMap<String, String>();

    /**
     * 对所有公共数据国家对应的省份进行封装即:Key=国家code Value=省份列表
     */
    public static Map<String, Map<String, String>> mapProvinceObj = 
        new HashMap<String, Map<String, String>>();

    /**
     * 对所有公共数据省份对应的城市进行封装即:Key=省份code Value=城市列表
     */
    public static Map<String, Map<String, String>> mapCityObj = 
        new HashMap<String, Map<String, String>>();

    /**
     * 对所有公共数据城市进行封装即:Key=城市code Value=城市name
     */
    public static Map<String, String> cityObj = new HashMap<String, String>();
    
    /**
     * 对所有公共数据城市进行封装即:Key=城市name Value=城市code
     */
    public static Map<String,String> cityObjs=new HashMap<String,String>();

    /**
     * 对所有公共数据区域对应的城市进行封装即:Key=区域code,区域name Value=城市列表 --add by kun.chen 2007-9-27
     */
    public static Map<String, Map<String, String>> mapAreaCityObj = 
        new HashMap<String, Map<String, String>>();

    /**
     * 对所有公共数据城市对应的城区进行封装即:Key=城市code Value=城区列表
     */
    public static Map<String, Map<String, String>> mapCitySozeObj = 
        new HashMap<String, Map<String, String>>();

    /**
     * 对所有公共数据城区进行封装即:Key=城区code Value=城区name
     */
    public static Map<String, String> citySozeObj = new HashMap<String, String>();

    /**
     * 对所有公共数据城市对应的商业区进行封装即:Key=城市code Value=商业区列表
     */
    public static Map<String, Map<String, String>> mapBusinessSozeObj = 
        new HashMap<String, Map<String, String>>();
    /**
     * 对所有公共数据商业区进行封装即:Key=商业区name Value=商业区code 
     */
    public static Map<String,String> businessToObj=new HashMap<String,String>();

    /**
     * 对所有公共数据商业区进行封装即:Key=商业区code Value=商业区name
     */
    public static Map<String, String> businessSozeObj = new HashMap<String, String>();

    /**
     * 对所有公共数据城市对应的展馆区进行封装即:Key=城市code Value=展馆区列表
     */
    public static Map<String, Map<String, String>> mapSaloonSozeObj = 
        new HashMap<String, Map<String, String>>();

    /**
     * 对所有公共数据展馆区进行封装即:Key=展馆区code Value=展馆区name
     */
    public static Map<String, String> saloonSozeObj = new HashMap<String, String>();

    /**
     * 对所有酒店特有的基础数据进行封装如:关房原因、币种、
     */
    public static Map<String, Map<String, String>> mapHotelCommmonObj = 
        new HashMap<String, Map<String, String>>();

    /**
     * 对所有公共数据配送城市进行 Key=code Value=name 封装
     */
    public static Map<String, String> diliveryCityObj = 
        new HashMap<String, String>();

    /**
     * 对所有公共数据配送城市对应的配送城区进行封装即:Key=配送城市code Value=配送城区列表
     */
    public static Map<String, Map<String, String>> mapDiliveryCityZoneObj =
        new HashMap<String, Map<String, String>>();

    /**
     * 对所有公共数据配送城区进行封装即:Key=配送城区code Value=配送城区name
     */
    public static Map<String, String> diliveryCityZoneObj = new HashMap<String, String>();

    /**
     * 对电子地图的封装即:Key=code Value=地区电子地图编码
     */
    public static Map<String, String> platObj = new HashMap<String, String>();
    /**
     * 联名商家
     */
    public static List<MbrshipCategory>   mbrshipCategoryList = new ArrayList();
    
    /**
     * 不合法的输入正则表达式，以两个空格分离
     */
    public static String illegalInputRegex=null;

}
