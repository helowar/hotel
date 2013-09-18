package com.mangocity.hotel.base.web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.framework.base.model.Entity;
import com.mangocity.framework.base.model.Tree;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlFITAlias;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.util.GenAllCity;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.search.index.HotelInfoIndexer;
import com.mangocity.hotel.search.mq.LuceneTopicListener;
import com.mangocity.hotel.search.searchengine.service.IndexBuilderService;
import com.mangocity.hotel.search.searchengine.service.RepEnvInfo;
import com.mangocity.hotel.search.searchengine.service.RepEnvInfoFactory;
import com.mangocity.mq.constant.MqConstants;
import com.mangocity.mq.hotel.JmsProvider;
import com.mangocity.util.ConfigParaBean;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.config.ConfigUtil;
import com.mangocity.util.hotel.constant.AssignMode;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.IsAutomatismSendFax;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.WebUtil;
import com.mangocity.webnew.constant.FITAliasConstant;


/**
 *  初始化调用 EJB 远程基础数据 
 * 
 * @author yuexiaofeng
 *
 */
public class InitServletImpl extends InitServlet {
    
    private static final long serialVersionUID = 542702089198710993L; 
    private static final MyLog log = MyLog.getLogger(InitServlet.class);
    
    public InitServletImpl() {
        super();
    }

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       cityAreaObj=new HashMap<String,String>();
        BaseDataObj=new HashMap<String,String>();
        allObj = new HashMap<String,String>();
        localCityObj = new HashMap<String,String>();    
        localProvinceObj = new HashMap<String,String>();    
        CountryObj=new HashMap<String,String>();
        ProvinceObj=new HashMap<String,String>();       
        mapProvinceObj=new HashMap<String,Map<String,String>>();
        mapCityObj=new HashMap<String,Map<String,String>>();
        cityObj=new HashMap<String,String>();
        cityObjs=new HashMap<String,String>();
        mapAreaCityObj=new HashMap<String,Map<String,String>>();
        mapCitySozeObj=new HashMap<String,Map<String,String>>();
        citySozeObj=new HashMap<String,String>();
        mapBusinessSozeObj=new HashMap<String,Map<String,String>>();
        businessSozeObj=new HashMap<String,String>();
        businessToObj=new HashMap<String,String>();
        mapSaloonSozeObj=new HashMap<String,Map<String,String>>();
        saloonSozeObj=new HashMap<String,String>();
        mapHotelCommmonObj=new HashMap<String,Map<String,String>>();
        diliveryCityObj=new HashMap<String,String>();
        mapDiliveryCityZoneObj=new HashMap<String,Map<String,String>>();
        diliveryCityZoneObj=new HashMap<String,String>();
        platObj=new HashMap<String,String>();
        
        ResourceManager resourceManager = (ResourceManager) WebUtil.getBean(getServletContext(), "resourceManager");
        resourceManager.getCache().removeAll();
        log.info("-----------动态加载CDM开始---------------");
        init();     
        log.info("-----------动态加载CDM结束---------------");
        //获取所有城市信息生成JS add by zhineng.zhuang 2008-09-24
        GenAllCity genAllCity = (GenAllCity) WebUtil.getBean(getServletContext(), "genAllCity");
        genAllCity.genJs();

        response.setContentType("text/html; charset=utf8");     
        ServletContext sc = getServletContext();
        RequestDispatcher rd = null;
        rd = sc.getRequestDispatcher("/outweb/resetCDM.jsp"); //定向的页面
        rd.forward(request, response);
        
    }
    
    public void init() throws ServletException {
        log.info(":::::::::::::::::::::::Start Init Servlet ::::::::::::::::::::::::::::::::::::");                
        SystemDataService systemDataService = (SystemDataService) WebUtil.getBean(getServletContext(), 
		"systemDataService");
        //是否开启VRM标签
        OrParam vrmParam = systemDataService.getSysParamByName("OPEN_VRM");
        if(vrmParam!=null){
        	  this.getServletContext().setAttribute("OPEN_VRM", vrmParam.getValue());
        }      
      //是否开启百分点标签
        OrParam bfdParam = systemDataService.getSysParamByName("OPEN_BFD");
        if(bfdParam!=null){
              this.getServletContext().setAttribute("OPEN_BFD", bfdParam.getValue());
        }
        //初始化热门城市所属区域
        queryCityArea();
        
        // 初始化自动分配模式
        initOrParam();
        
        //初始化芒果散客项目号
        setFitAliasObj();
        SAVE_ROOTPATH =getServletContext().getRealPath("/");
        
        saveHWEBpath = getServletContext().getRealPath("/upload/jsfile");
       // luceneIndexDir =  getServletContext().getRealPath(ConfigUtil.getResourceByKey("index.file.dir"));        
        
        String naming_factory = ConfigUtil.getResourceByKey(BaseConstant.NAMING_FACTORY);
        String namespace_address = ConfigUtil.getResourceByKey(BaseConstant.NAMESPACE_ADDRESS);
        String home_address = ConfigUtil.getResourceByKey(BaseConstant.HOME_ADDRESS);   
        log.info("::::::::::::::::::::::::naming_factory1:::::::::::::::::::::::::::::::::::"+naming_factory);
        log.info("::::::::::::::::::::::::namespace_address1:::::::::::::::::::::::::::::::::::"+namespace_address);
        log.info("::::::::::::::::::::::::home_address1:::::::::::::::::::::::::::::::::::"+home_address);        
//      java.util.Hashtable environment = new java.util.Hashtable();
//      environment.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,naming_factory);
//      environment.put(javax.naming.Context.PROVIDER_URL, namespace_address);
        try {
//          javax.naming.InitialContext ic = new javax.naming.InitialContext(environment);
//          Object o = ic.lookup(home_address);
//          CdmServiceHome home = (CdmServiceHome) (javax.rmi.PortableRemoteObject.narrow(o, CdmServiceHome.class));
//          CdmService test = home.create();
//          if(test!=null){
                log.info("::::::::::::::::clear cache::::::::::::::::::::");
                try{
                        ResourceManager resourceManager = (ResourceManager) WebUtil.getBean(getServletContext(), "resourceManager");
                        resourceManager.getCache().removeAll();
                }catch(Exception e){
                    e.printStackTrace();
                }
                long lBegin = System.currentTimeMillis();
                fillBaseTree(naming_factory, namespace_address, home_address);
                log.info("获取CDM数据共花 : "
                        + (System.currentTimeMillis() - lBegin) + "毫秒");
//          }else{
//              log.info(":::::::::::::::CDM start error:::::::::::::::::::::");
//          }
//          
//          log.info("::::::::::::::::::::::::Init Operation End:::::::::::::::::::::::::::::::::::");
//          log.info(":::::::::::::::::::::::End Init Servlet ::::::::::::::::::::::::::::::::::::");
            //printMap(BaseDataObj);
            
            // 设置汇率
            setCurrenyRate();
            
            // 设置加幅
            setIncreasePrice();
            
            //设置待用券的URL，给jsp使用
            setVoucherUrl();
            
            //begin 获取新图片库地址
            PICTUREURL = ParamServiceImpl.getInstance().getConfValue("{htl_read_pictures_url}");
            getServletContext().setAttribute("pictureUrl", PICTUREURL);
            log.info("------酒店图片库地址:"+PICTUREURL);
            //end
            
                     
			HotelInfoIndexer hotelInfoIndexer = (HotelInfoIndexer) WebUtil
					.getBean(getServletContext(), "hotelInfoIndexer");
			hotelInfoIndexer.createHotelInfoIndex();

			JmsProvider jmsProvider = (JmsProvider) WebUtil.getBean(
					getServletContext(), "jmsProvider");
			LuceneTopicListener luceneTopicListener = (LuceneTopicListener) WebUtil
					.getBean(getServletContext(), "luceneTopicListener");

			try {
				jmsProvider.setTopicListener(MqConstants.TOPIC_HOTEL_LUCENE,
						luceneTopicListener);
			} catch (Exception e) {
				log.error(e);
			}
			// 增加lucene的监听器 add by chenkeming
			//            System.out.println("start to set topic listener");
			//            JmsProvider jmsProvider = (JmsProvider) WebUtil.getBean(
			//					getServletContext(), "jmsProvider");
			//			LuceneTopicListener luceneTopicListener = (LuceneTopicListener) WebUtil
			//					.getBean(getServletContext(), "luceneTopicListener");
			//			
			//			RepEnvInfoFactory repEnvInfoFactory = (RepEnvInfoFactory) WebUtil.getBean(getServletContext(), "repEnvInfoFactory");
			//			
			//			RepEnvInfo repEnvInfo = repEnvInfoFactory.getRepEnvInfo();        
			//			String clientId = "ID:"+repEnvInfo.getRepConfig().getGroupName()+"-"+repEnvInfo.getRepConfig().getNodeName()+"-"+repEnvInfo.getRepConfig().getNodeHostPort();
			//			//jmsProvider.setTopicListener(MqConstants.TOPIC_HOTEL_LUCENE,luceneTopicListener,clientId,repEnvInfo.getRepConfig().getNodeName());	
			//			System.out.println("finish setting topic listener");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    private void fillBaseTree(String naming_factory, String namespace_address, String home_address) throws RemoteException {
    	SystemDataService systemDataService = (SystemDataService) WebUtil.getBean(getServletContext(), "systemDataService");
        Tree tree = null;
        // tree =
        // test.getParamsByPath(ConfigUtil.getResourceByKey(BaseConstant.COUNTRY_TREEPATH));
        tree = systemDataService.getParamsByPath(ConfigUtil
                .getResourceByKey(BaseConstant.COUNTRY_TREEPATH));
        // tree =
        // GetCDMUtil.getParamsByPath(ConfigUtil.getResourceByKey(BaseConstant.COUNTRY_TREEPATH),
        // hraManager.getConn());
        setBaseData(tree);
        // tree =
        // test.getParamsByPath(ConfigUtil.getResourceByKey(BaseConstant.HOTEL_TREEPATH));
        tree = systemDataService.getParamsByPath(ConfigUtil
                .getResourceByKey(BaseConstant.HOTEL_TREEPATH));
//        tree = GetCDMUtil.getParamsByPath(ConfigUtil.getResourceByKey(BaseConstant.HOTEL_TREEPATH), 
//                hraManager.getConn());
        if(tree !=null){
            if (tree.getChildren() != null) {
                List<Tree> childListObj = tree.getChildren();
                for (Tree childObj: childListObj) {
                    //实现一:当所有类别的列表在属性中存放时
                    /**
                    Map<String,String> mapObj=new HashMap<String,String>();
                    if (childObj.getPropertys() != null) {
                        List<Entity> proList = childObj.getPropertys();
                        for (Entity objProperty:proList) {
                            mapObj.put(objProperty.getName(), objProperty.getTitle());//objProperty.getDescription()//属性描述      
                        }
                    }   
                    mapHotelCommmonObj.put(childObj.getName(), mapObj);**/
                    
                    //实现二:当所有类别的列表在树中存在时
                    /****/
                    Map<String,String> mapObj=new HashMap<String,String>();
                    if(childObj.getChildren() !=null){
                        List<Tree> childListValueObj=childObj.getChildren();
                        for (Tree childValueObj: childListValueObj) {                               
                            mapObj.put(childValueObj.getName(),childValueObj.getTitle());
                        }                                                                   
                    }   
                    mapHotelCommmonObj.put(childObj.getName(), mapObj);
                }                                                   
            }                   
        }
        if(StringUtil.isValidStr(ConfigUtil.getResourceByKey(BaseConstant.HOTEL_COMMON_TREEPATH))){
//          tree = test.getParamsByPath(ConfigUtil.getResourceByKey(BaseConstant.HOTEL_COMMON_TREEPATH));
            tree = systemDataService.getParamsByPath(ConfigUtil.getResourceByKey(BaseConstant.HOTEL_COMMON_TREEPATH));
//            GetCDMUtil.getParamsByPath(ConfigUtil.getResourceByKey(BaseConstant.HOTEL_COMMON_TREEPATH), 
//                    hraManager.getConn());
            if(tree !=null){
                String hotel_common_treepath_flag = ConfigUtil.getResourceByKey(BaseConstant.HOTEL_COMMON_TREEPATH_FLAG);
                if (tree.getChildren() != null) {
                    List<Tree> childListObj = tree.getChildren();
                    for (Tree childObj: childListObj) {
                        if(hotel_common_treepath_flag.indexOf(childObj.getName())!=-1){                                                             
                            //实现一:当所有类别的列表在属性中存放时
                            /**
                            Map<String,String> mapObj=new HashMap<String,String>();
                            if (childObj.getPropertys() != null) {
                                List<Entity> proList = childObj.getPropertys();
                                for (Entity objProperty:proList) {
                                    mapObj.put(objProperty.getName(), objProperty.getTitle());//objProperty.getDescription()//属性描述      
                                }
                            }   
                            mapHotelCommmonObj.put(childObj.getName(), mapObj);**/
                            
                            //实现二:当所有类别的列表在树中存在时
                            /****/
                            Map<String,String> mapObj=new HashMap<String,String>();
                            if(childObj.getChildren() !=null){
                                List<Tree> childListValueObj=childObj.getChildren();
                                for (Tree childValueObj: childListValueObj) {                                   
                                    mapObj.put(childValueObj.getName(),childValueObj.getTitle());
                                }                                                                   
                            }                       
                            mapHotelCommmonObj.put(childObj.getName(), mapObj);
                        }   
                    }                                                   
                }                   
            }                   
        }       
//      log.info("::::::::::::::::::::::::Init Operation Start:::::::::::::::::::::::::::::::::::");
//      log.info("::::::::::::::::::::::::naming_factory:::::::::::::::::::::::::::::::::::"+naming_factory);
//      log.info("::::::::::::::::::::::::namespace_address:::::::::::::::::::::::::::::::::::"+namespace_address);
//      log.info("::::::::::::::::::::::::home_address:::::::::::::::::::::::::::::::::::"+home_address);
        getServletContext().setAttribute("descr", ProvinceObj);
        //printMap((Map<String,String>)getServletContext().getAttribute("descr"));
        
        // 初始化114用户省份数据
        //UserUtil.initOrgInfo((Map<String,String>)getServletContext().getAttribute("descr"));
    }   
    
    /**
     * 对国家、省份、城市、城区的数据进行相应的封装
     * 对国家的封装根据指定的过滤条件进行筛选具体以需求为准
     * 此时针对含有中国的国家进行封装即:只获取中国及其所有子对象、国外网站需求只封装城市
     * 
     * @param treeCountry Tree
     */
    public static void setBaseData(Tree tree){
        
        boolean bCheckHK = false, bCheckMA = false;
        
        if (tree != null) {
            if(tree.getChildren() !=null){
                List<Tree> childListCountryObj = tree.getChildren();        
                for(Tree childCountryObj: childListCountryObj){
                    //if(BaseConstant.COUNTRY_TREEPATH_NAME.equalsIgnoreCase(childCountryObj.getName())){//此时仅对中国的子对象进行封装,因为需求还没有涉及到国家
                        CountryObj.put(childCountryObj.getName(), childCountryObj.getTitle());//封装国家列表  
                        BaseDataObj.put(childCountryObj.getName(), childCountryObj.getTitle());//对所有树的封装
                        if (childCountryObj.getChildren() != null) {
                            List<Tree> childListProvinceObj = childCountryObj.getChildren();
                            Map<String,String> provinceRelationCountryObj=new HashMap<String,String>();
                            for (Tree childProvinceObj: childListProvinceObj) {
                                
                                if(childProvinceObj.getPropertys() !=null){// 省份区域设置
                                    Map<String,String> areaRelationCityObj = new HashMap<String,String>();
                                    List<Entity> proList = childProvinceObj.getPropertys();
                                    for(Entity propertyObj : proList){
                                        if(BaseConstant.AREACODE.equalsIgnoreCase(propertyObj.getDescription())){// 属性描述标识城市所属区域
                                            String areaCode = propertyObj.getName();// 区域编码
                                            String areaName = propertyObj.getTitle();// 区域名称
                                            String key = areaCode + "," + areaName;
                                            if(childProvinceObj.getChildren() !=null){
                                                List<Tree> childListAreaCityObj = childProvinceObj.getChildren();
                                                for (Tree childAreaCityObj : childListAreaCityObj) {                                                    
                                                    areaRelationCityObj.put(childAreaCityObj.getName(), childAreaCityObj.getTitle());
                                                }
                                            }
                                            if(mapAreaCityObj.containsKey(key)){        
                                                mapAreaCityObj.get(key).putAll(areaRelationCityObj);                                                    
                                            }else{                                                      
                                                mapAreaCityObj.put(key, areaRelationCityObj);
                                            }
                                        }
                                    }                                           
                                }                               
                                
                                if (childProvinceObj.getParent().getId()== childCountryObj.getId()) {
                                    provinceRelationCountryObj.put(childProvinceObj.getName(), childProvinceObj.getTitle());
                                    BaseDataObj.put(childProvinceObj.getName(), childProvinceObj.getTitle());
                                }           
                                ProvinceObj.put(childProvinceObj.getName(), childProvinceObj.getTitle());//封装国家下的省份列表，即:国内、国外
                                if(BaseConstant.COUNTRY_TREEPATH_NAME.equalsIgnoreCase(childCountryObj.getName())){
                                    localProvinceObj.put(childProvinceObj.getName(), childProvinceObj.getTitle());//封装国家下的省份列表，即:仅包括国内
                                }
                                mapProvinceObj.put(childCountryObj.getName(), provinceRelationCountryObj);//封装国家对应的省份列表                                                     
                                if(childProvinceObj.getChildren() !=null){
                                    List<Tree> childListCityObj = childProvinceObj.getChildren();
                                    Map<String,String> cityRelationProvinceObj=new HashMap<String,String>();
                                    for (Tree childCityObj: childListCityObj) { 
                                        
                                        Map<String,String> areaRelationCityObj = new HashMap<String,String>();
                                        boolean deliveryFlag = false;
                                        if(childCityObj.getPropertys() !=null){// 省份区域设置、配送设置、电子地图设置
                                            List<Entity> proCityList = childCityObj.getPropertys();
                                            for(Entity propertyCityObj : proCityList){
                                                if(BaseConstant.AREACODE.equalsIgnoreCase(propertyCityObj.getDescription())){// 属性描述标识城市所属区域
                                                    String areaCode = propertyCityObj.getName();// 区域编码
                                                    String areaName = propertyCityObj.getTitle();// 区域名称
                                                    String key = areaCode + "," + areaName;
                                                    areaRelationCityObj.put(childCityObj.getName(), childCityObj.getTitle());
                                                    if(mapAreaCityObj.containsKey(key)){        
                                                        mapAreaCityObj.get(key).putAll(areaRelationCityObj);                                                    
                                                    }else{                                                      
                                                        mapAreaCityObj.put(key, areaRelationCityObj);
                                                    }
                                                }else if(BaseConstant.DELIVERY_CITY_FLAG.equalsIgnoreCase(propertyCityObj.getName())&&BaseConstant.DELIVERY_CITY.equalsIgnoreCase(propertyCityObj.getTitle())){
                                                    // 属性 code 及 name 标识是否为配送城市
                                                    diliveryCityObj.put(childCityObj.getName(), childCityObj.getTitle());
                                                    deliveryFlag = true;
                                                    /**
                                                    if(childCityObj.getChildren() !=null){
                                                        List<Tree> childListCityBusinessZoneObj = childCityObj.getChildren();
                                                        for (Tree childCityBusinessZoneObj: childListCityBusinessZoneObj) {                             
                                                            if (BaseConstant.DISTRICT_FLAG.equalsIgnoreCase(childCityBusinessZoneObj.getName())) {
                                                                if(childCityBusinessZoneObj.getChildren() !=null){
                                                                    List<Tree> childListCityZoneObj = childCityBusinessZoneObj.getChildren();
                                                                    Map<String,String> cityZoneRelationCityObj=new HashMap<String,String>();
                                                                    for (Tree childCityZoneObj: childListCityZoneObj) {                             
                                                                        if (childCityZoneObj.getParent().getId()== childCityBusinessZoneObj.getId()) {
                                                                            cityZoneRelationCityObj.put(childCityZoneObj.getName(), childCityZoneObj.getTitle());                                                                           
                                                                        }                           
                                                                        mapDiliveryCityZoneObj.put(childCityObj.getName(), cityZoneRelationCityObj);//封装配送城市对应的配送城区列表
                                                                    }   
                                                                }
                                                            }
                                                        }
                                                    }
                                                    **/
                                                }else if(BaseConstant.PLATCODE.equalsIgnoreCase(propertyCityObj.getDescription())){// 属性描述标识城市所属电子地图
                                                    // 城市编码
                                                    // 电子地图编码
                                                    if(StringUtil.isValidStr(propertyCityObj.getTitle())){                                                      
                                                        platObj.put(propertyCityObj.getName(), propertyCityObj.getTitle());                                                 
                                                    }
                                                }
                                            }                                           
                                        }                                       
                                        
                                        if (childCityObj.getParent().getId()== childProvinceObj.getId()) {
                                            cityRelationProvinceObj.put(childCityObj.getName(), childCityObj.getTitle());
                                            BaseDataObj.put(childCityObj.getName(), childCityObj.getTitle());
                                        }   
                                       String firTitlePin = "";
                                       if(childCityObj!=null&&childCityObj.getTitlePin()!=""&&childCityObj.getTitlePin()!=null){                                           
//                                         String titlePin = childCityObj.getTitlePin();
//                                         firTitlePin = titlePin.substring(0, 1);
                                           firTitlePin = childCityObj.getTitlePin();
                                       }
                                        if(BaseConstant.COUNTRY_TREEPATH_NAME.equalsIgnoreCase(childCountryObj.getName())){
                                            localCityObj.put(childCityObj.getName(), firTitlePin+childCityObj.getTitle());//封装省份下的城市列表
                                        }                                      
                                        
                                        // 获取香港,澳门城市代码
                                        if(!bCheckHK && childCityObj.getTitle().indexOf("香港") != -1){
                                            OrderUtil.HK_CODE = childCityObj.getName();
                                            bCheckHK = true;
                                            // log.info("香港: " + childCityObj.getName());
                                        }
                                        if(!bCheckMA && childCityObj.getTitle().indexOf("澳门") != -1){
                                            OrderUtil.MA_CODE = childCityObj.getName();
                                            bCheckMA = true;
                                            // log.info("澳门: " + childCityObj.getName());
                                        }
                                        
                                        allObj.put(childCityObj.getName(), childCityObj.getTitlePin()+childCityObj.getTitle());//封装省份下的城市列表
                                        cityObj.put(childCityObj.getName(), childCityObj.getTitle());//封装所有城市列表
                                        cityObjs.put(childCityObj.getTitle(),childCityObj.getName());//封装所有城市列表
                                        mapCityObj.put(childProvinceObj.getName(), cityRelationProvinceObj);//封装省份对应的城市列表
                                        if(childCityObj.getChildren() !=null){
                                            List<Tree> childListCityBusinessZoneObj = childCityObj.getChildren();
                                            for (Tree childCityBusinessZoneObj: childListCityBusinessZoneObj) {                             
                                                if (BaseConstant.DISTRICT_FLAG.equalsIgnoreCase(childCityBusinessZoneObj.getName())) {
                                                    if(childCityBusinessZoneObj.getChildren() !=null){
                                                        List<Tree> childListCityZoneObj = childCityBusinessZoneObj.getChildren();
                                                        Map<String,String> cityZoneRelationCityObj=new HashMap<String,String>();
                                                        for (Tree childCityZoneObj: childListCityZoneObj) {                             
                                                            if (childCityZoneObj.getParent().getId()== childCityBusinessZoneObj.getId()) {
                                                                cityZoneRelationCityObj.put(childCityZoneObj.getName(), childCityZoneObj.getTitle());
                                                                BaseDataObj.put(childCityZoneObj.getName(), childCityZoneObj.getTitle());
                                                                citySozeObj.put(childCityZoneObj.getName(), childCityZoneObj.getTitle());//封装所有城区列表
                                                            }                           
                                                            mapCitySozeObj.put(childCityObj.getName(), cityZoneRelationCityObj);//封装城市对应的城区列表
                                                            if(deliveryFlag){//封装配送城市对应的配送城区列表
                                                                mapDiliveryCityZoneObj.put(childCityObj.getName(), cityZoneRelationCityObj);
                                                                diliveryCityZoneObj.put(childCityObj.getName(), childCityZoneObj.getTitle());//封装所有配送城区列表
                                                            }
                                                        }   
                                                    }
                                                }else if(BaseConstant.BUSINESS_FLAG.equalsIgnoreCase(childCityBusinessZoneObj.getName())){
                                                    if(childCityBusinessZoneObj.getChildren() !=null){
                                                        List<Tree> childListBusinessZoneObj = childCityBusinessZoneObj.getChildren();
                                                        Map<String,String> businessZoneRelationCityObj=new HashMap<String,String>();
                                                        for (Tree childBusinessZoneObj: childListBusinessZoneObj) {                             
                                                            if (childBusinessZoneObj.getParent().getId()== childCityBusinessZoneObj.getId()) {
                                                                businessZoneRelationCityObj.put(childBusinessZoneObj.getName(), childBusinessZoneObj.getTitle());
                                                                BaseDataObj.put(childBusinessZoneObj.getName(), childBusinessZoneObj.getTitle());
                                                                businessSozeObj.put(childBusinessZoneObj.getName(), childBusinessZoneObj.getTitle());//封装所有商业区列表
                                                                businessToObj.put(childBusinessZoneObj.getTitle(),childBusinessZoneObj.getName());//封装所有商业区列表
                                                            }                           
                                                            mapBusinessSozeObj.put(childCityObj.getName(), businessZoneRelationCityObj);//封装城市对应的商业区列表
                                                        }   
                                                    }                                                                                                                       
                                                }else if(BaseConstant.SALOON_FLAG.equalsIgnoreCase(childCityBusinessZoneObj.getName())){
                                                    if(childCityBusinessZoneObj.getChildren() !=null){
                                                        List<Tree> childListSaloonZoneObj = childCityBusinessZoneObj.getChildren();
                                                        Map<String,String> saloonZoneRelationCityObj=new HashMap<String,String>();
                                                        for (Tree childSaloonZoneObj: childListSaloonZoneObj) {                             
                                                            if (childSaloonZoneObj.getParent().getId()== childCityBusinessZoneObj.getId()) {
                                                                saloonZoneRelationCityObj.put(childSaloonZoneObj.getName(), childSaloonZoneObj.getTitle());
                                                                BaseDataObj.put(childSaloonZoneObj.getName(), childSaloonZoneObj.getTitle());
                                                                saloonSozeObj.put(childSaloonZoneObj.getName(), childSaloonZoneObj.getTitle());////封装所有展馆区列表
                                                            }                           
                                                            mapSaloonSozeObj.put(childCityObj.getName(), saloonZoneRelationCityObj);//封装城市对应的展馆区列表
                                                        }   
                                                    }                                                                                                                       
                                                }                               
                                            }                                           
                                        }
                                    }                       
                                }
                            }                   
                        }                       
                    //}                             
                }                       
            }       
        }               
    }       
    
    /**
     * 测试方法打印 Map 对象
     * 
     * @param map
     */
    public void printMap(Map map){
        Object obj;
        Set setObj=map.keySet();
        Iterator it=setObj.iterator();
        while(it.hasNext()){
            obj= it.next();
            log.info("Key = "+obj);
            log.info("Value = "+map.get(obj));
        }
    }   
    
    /**
     * 获取汇率
     */
    private void setCurrenyRate()
    {
        HotelManage hotelManage = (HotelManage) WebUtil
        .getBean(getServletContext(), "hotelManage");
        CurrencyBean.rateMap = hotelManage.getExchangeRateMap();
    }
    
    /**
     * 获取加幅
     * add by wuyun
     * 2009-04-19
     */
    private void setIncreasePrice()
    {
        HotelManage hotelManage = (HotelManage) WebUtil.getBean(getServletContext(), "hotelManage");
        hotelManage.getIncreasePriceMap();
    }
    
    /**
     * 每次重启需要初始化orparam表的值
     *
     */
    private void initOrParam() {
    	SystemDataService systemDataService = (SystemDataService) WebUtil.getBean(getServletContext(), 
			"systemDataService");
        
    	OrParam param = systemDataService.getSysParamByName("ASSIGN_MODE");
        OrParam sendFax = systemDataService.getSysParamByName("IS_SEND_FAX");
        if(param.isWorking()) {
            param.setValue(AssignMode.AUTO);    
            systemDataService.updateSysParamByName(param);
        }
        if(!sendFax.isSendFax()){
            sendFax.setValue(IsAutomatismSendFax.SENDSTOP);
            systemDataService.updateSysParamByName(sendFax);
        }       
                
        param = systemDataService.getSysParamByName("save_main");
        Date now_main = new Date();
        if((now_main.getTime() - param.getModifyTime().getTime()) > 1800000) {
            param.setModifyTime(now_main);  
            systemDataService.updateSysParamByName(param);
            saveMainCan = true;
        }
        
        //HwManage hwManage = (HwManage) WebUtil.getBean(getServletContext(), "HwManage");      
        param = systemDataService.getSysParamByName("save_popular");
        Date now_popular = new Date();
        if((now_popular.getTime() - param.getModifyTime().getTime()) > 1800000) {
            param.setModifyTime(now_popular);   
            systemDataService.updateSysParamByName(param);
            savePopularCan = true;
        }
        
        //启动时读取并更新数据库参数设置
        param = systemDataService.getSysParamByName("PAYMENT_SYNCHRONOUS");
        Date now_synchronous = new Date();
        if((now_synchronous.getTime() - param.getModifyTime().getTime()) > 1800000) {
            param.setModifyTime(now_synchronous);   
            systemDataService.updateSysParamByName(param);          
        }
        
        //获取非法输入的正则表达式
        OrParam illegalInputRegexParam = systemDataService.getSysParamByName("hotelII_illegal_input_regex");
        if(illegalInputRegexParam!=null){
        	System.out.println(illegalInputRegex);
        	illegalInputRegex=illegalInputRegexParam.getValue();
        	
        }
        
    }

    
    /**
     * 设置待用券的URL，给jsp使用
     * hotel2.9.3 add by chenjiajie 2009-08-27
     */
    private void setVoucherUrl(){
    	ConfigParaBean configParaBean = (ConfigParaBean)WebUtil.getBean(getServletContext(), "configParaBean");
    	//CC调用的代金券页面接口URL
    	getServletContext().setAttribute("ccVoucherRequestURL", configParaBean.getCcVoucherRequestURL());
    	//网站调用的代金券页面接口URL
    	getServletContext().setAttribute("hwebVoucherRequestURL", configParaBean.getHwebVoucherRequestURL());
    	
    	//新版网站信用卡url
    	String creditPath = ParamServiceImpl.getInstance().getConfValue("{hotels.mpm.creditcardurl}");
    	//本地开发把注释解开
    	//creditPath = creditPath.replaceAll("https", "http");
    	log.info("=======creditPath:" + creditPath + "=======");
    	getServletContext().setAttribute("creditPath", creditPath);

    	//新版网站订单核对页面url
    	String orderComfirmUrl = ParamServiceImpl.getInstance().getConfValue("{hotels.ordercheck.url}");
    	log.info("=======orderComfirmUrl:" + orderComfirmUrl + "=======");
    	getServletContext().setAttribute("orderComfirmUrl", orderComfirmUrl);

    	//新版网站订单核跳转url
    	String orderCompleteUrl = ParamServiceImpl.getInstance().getConfValue("{hotels.ordercomfirm.url}");
        String orderComfirmForwardUrl = "http://" + orderCompleteUrl;
        log.info("=======orderComfirmForwardUrl:" + orderComfirmForwardUrl + "=======");
        getServletContext().setAttribute("orderComfirmForwardUrl", orderComfirmForwardUrl);
    	
    	//新版网站会员url
    	String memberPath = ParamServiceImpl.getInstance().getConfValue("{hotels.member.merberurl}");
    	log.info("=======memberPath:" + memberPath + "=======");
    	getServletContext().setAttribute("memberPath", memberPath);
    	
    	//搜索行为分析、追踪url
    	String hotelSearchTrackURL = ParamServiceImpl.getInstance().getConfValue("{hotels.search.track.url}");
        getServletContext().setAttribute("searchtrackurl", hotelSearchTrackURL);
        
    }
    
    public void setFitAliasObj(){
    	IHotelFavourableReturnService returnService = (IHotelFavourableReturnService)WebUtil.getBean(getServletContext(), "returnService");
    	List<HtlFITAlias> fitAliasList = returnService.getFITAlias("1");
    	try{
	    	if(fitAliasList!=null && !fitAliasList.isEmpty()){
	    		Map<String,String> aliasMap = new HashMap<String,String>(fitAliasList.size());
	    		for(HtlFITAlias alias : fitAliasList){
	    			aliasMap.put(alias.getAliasId(), alias.getID().toString());
	        	}
	    		if(!aliasMap.isEmpty()){
	    			FITAliasConstant.fitAliasObj.clear();
	    			FITAliasConstant.fitAliasObj.putAll(aliasMap);
	    			log.info("::::::::::::::::::"+new Date()+"加载网站散客项目号成功！");
	    		}
	    	}
    	}catch(Exception e){
    		log.info("::::::::::::::::::::::::::::"+new Date()+"加载芒果网站散客项目号失败！");
    	}
    }
    
    /**
	 * 热门城市所属区域
	 */
	private void queryCityArea(){
		cityAreaObj.put("PEK", "pek");
		cityAreaObj.put("SHA", "sha");
		cityAreaObj.put("CAN", "can");
		cityAreaObj.put("SZX", "szx");
		cityAreaObj.put("HKG", "hkg");
		cityAreaObj.put("HGH", "hgh");
		cityAreaObj.put("CTU", "ctu");
		cityAreaObj.put("NKG", "nkg");
	}
}
