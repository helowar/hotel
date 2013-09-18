package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.hotel.dto.MGExHotelListReturn;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.HotelPriorityManage;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlChannelMapInfo;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlChildWelcomePrice;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCtct;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelBase;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlSetPriority;
import com.mangocity.hotel.base.persistence.HtlTrafficInfo;
import com.mangocity.hotel.base.persistence.HtlWelcomePrice;
import com.mangocity.hotel.base.persistence.OrLockedRecords;
import com.mangocity.hotel.base.persistence.SellSeason;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.service.ILockedRecordService;
import com.mangocity.hotel.base.service.IQuotaForCCService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.util.SendLuceneMQ;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.mq.constant.MqConstants;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.HotelMappingType;

/**
 */
public class HotelInfoAction extends PersistenceAction {
	
	public static String NOSMOKING="35"; //禁烟表示的数字

    private long hotelId;
    
    private long contractId;

    private String source;

    private HtlHotel htlHotel;
    
    private String cusId;

    private String roomTypeID;

    private String FORWARD;

    private HotelManage hotelManage;
    
    private HotelRoomTypeService hotelRoomTypeService;
    
    //add by yong.zeng 2009-12-24
    private IHDLService hdlService;

    private int lsCtctrowNum;

    private int lsSellrowNum;

    private int trafficNum;

    private int lsBookSetupwNum;

    private int lsRoomTypeNum;

    private int hotelType;

    private HtlCtct htlCtct;

    // private int
    // 酒店联系信息
    private List lsCtctSel;

    // 销售季节
    private List lsSellSel;

    // 交通信息
    private List lsTrafficInfo;

    // 预定联系
    private List lsBookSetup;

    // 房间信息
    private List lsRoomType;

    // 信用卡复选框内容
    private String[] creditCard;

    // 销售渠道复选框内容
    private String[] saleChannel;

    // 采购渠道复选框内容
    private String[] purchaseChannel;

    // 酒店类型复选框内容
    private String[] hotelTypeCheck;

    /**
     * 酒店主题复选框内容
     */
    private String[] theme;

    private String[] lang;

    private int custom;

    private HotelPriorityManage hotelPriorityManage;

    private HtlSetPriority htlSetPriority;

    private IQuotaForCCService quotaForCCService;

    // 调价提示在CC不显示标志位
    private int justForCC;

    // 查询时间段内的早餐价信息和接送价信息 add by baofeng.si V2.3 2008-5-30 Start
    private ContractManage contractManage;

    private List<HtlChargeBreakfast> breakfastList = new ArrayList<HtlChargeBreakfast>();

    private List<HtlWelcomePrice> welcomeList = new ArrayList<HtlWelcomePrice>();

    private List<HtlAddBedPrice> bedPriceList = new ArrayList<HtlAddBedPrice>();

    private List<HtlInternet> internetList = new ArrayList<HtlInternet>();

    // 酒店扩展表信息 add by zhineng.zhuang
    private List lsHtlHotelExt;

    // 查询时间段内的早餐价信息和接送价信息 add by baofeng.si V2.3 2008-5-30 End

    // 酒店，合同，房态，配额等加解锁操作接口
    private ILockedRecordService lockedRecordService;
    
    // 发送MQ功能
    private SendLuceneMQ sendLuceneMQ;
    
    //房态改造，从房态页面跳到酒店基本信息修改页面，保存或取消操作时，关闭IE；
    private String closeIeOrnot;
    
    private MemberDTO member;
    
    private int channelType;
    
    private String channelChnName;
    
    private String requestChannel;
    
    private static final Map<Integer,String> CHANNEL_CODE_CHNNAME_MAP = new HashMap<Integer,String>(); 
    static{
    	CHANNEL_CODE_CHNNAME_MAP.put(ChannelType.CHANNEL_GLHT,"格林豪泰");
    	CHANNEL_CODE_CHNNAME_MAP.put(ChannelType.CHANNEL_JREZ,"锦江集团");
    }

	public String getChannelChnName() {
		return channelChnName;
	}

	public void setChannelChnName(String channelChnName) {
		this.channelChnName = channelChnName;
	}

    protected Class getEntityClass() {
        return HtlHotel.class;
    }

    public String querySysOuterOrder() {
        super.setEntityID(hotelId);
        super.setEntity(super.populateEntity());
        this.setHtlHotel((HtlHotel) this.getEntity());
        if (0 < hotelId) {
            htlHotel = hotelManage.findHotel(hotelId);
            lsRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(htlHotel.getID().longValue());
            lsCtctSel = htlHotel.getHtlCtct();
            lsSellSel = htlHotel.getSellSeason();
            lsTrafficInfo = htlHotel.getHtlTrafficInfo();
            lsBookSetup = htlHotel.getHtlBookSetup();

            lsCtctrowNum = lsCtctSel.size();
            lsSellrowNum = lsSellSel.size();

            trafficNum = lsTrafficInfo.size();
            lsBookSetupwNum = lsBookSetup.size();

            if (null != htlHotel.getAcceptCustom() && 0 < htlHotel.getAcceptCustom().length()) {
                custom = 1;
            } else {
                custom = 0;
            }
        } else {
            htlHotel = new HtlHotel();
            htlHotel.setCheckinTime("1400");
            htlHotel.setCheckoutTime("1200");
        }
        FORWARD = "querySysOuter";
        return FORWARD;
    }

    /* 增加查询方法. by haibo.li */
    /**
     * 增加加锁功能 modify by chenjiajie 2008-10-13
     */
    public String queryHotelMessage() {
        super.setEntityID(hotelId);
        super.setEntity(super.populateEntity());
        this.setHtlHotel((HtlHotel) this.getEntity());
        if (0 < hotelId) {

            // 查询时间段内的早餐价信息和接送价信息 add by baofeng.si V2.3 2008-5-30 Start
            Map params = super.getParams();
            Date beginDate = DateUtil.getDate((String) params.get("beginDate"));
            Date endDate = DateUtil.getDate((String) params.get("endDate"));
            if (null == beginDate) {
                beginDate = new Date();
            }
            if (null == endDate) {
                endDate = new Date();
            }
            HtlContract contract = contractManage.checkContractDateNew(hotelId, beginDate);
            if(null!=contract)contractId = contract.getID();
            // 查询时间段内的早餐价信息和接送价信息 add by baofeng.si V2.3 2008-5-30 End

            htlHotel = hotelManage.findHotel(hotelId);
            lsRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(htlHotel.getID().longValue());
            lsCtctSel = htlHotel.getHtlCtct();
            lsSellSel = htlHotel.getSellSeason();
            lsTrafficInfo = htlHotel.getHtlTrafficInfo();
            lsBookSetup = htlHotel.getHtlBookSetup();
            lsHtlHotelExt = htlHotel.getHtelHotelExt();

            lsCtctrowNum = lsCtctSel.size();
            lsSellrowNum = lsSellSel.size();

            trafficNum = lsTrafficInfo.size();
            lsBookSetupwNum = lsBookSetup.size();

            if (null != htlHotel.getAcceptCustom() && 0 < htlHotel.getAcceptCustom().length()) {
                custom = 1;
            } else {
                custom = 0;
            }
            // 房态负责人
            lsHtlHotelExt = htlHotel.getHtelHotelExt();
            if (0 < lsHtlHotelExt.size()) {
                HtlHotelExt hoext = new HtlHotelExt();
                for (int i = 0; i < lsHtlHotelExt.size(); i++) {
                    hoext = (HtlHotelExt) lsHtlHotelExt.get(i);
                    String roomStateManager = hoext.getRoomStateManager();
                    log.info(roomStateManager);
                    request.setAttribute("roomStateManager", roomStateManager);
                }
            }

            /** 检查酒店是否锁定 add by chenjiajie V2.4 2008-8-26 Begin **/
            OrLockedRecords orLockedRecord = new OrLockedRecords();
            orLockedRecord.setRecordCD(String.valueOf(hotelId));
            orLockedRecord.setLockType(01);
            orLockedRecord.setRemark(htlHotel.getChnName());
            OrLockedRecords lockedRecords = lockedRecordService.loadLockedRecord(orLockedRecord);
            /** begin haibo.li **/
            String backToHotelInfo = (String) params.get("forwordHotelInfo");
            if (null != backToHotelInfo && !backToHotelInfo.equals("")) {
                // 如果是合同那边的过来。根据判断进来跳转到另外一个页面
                if (null != lockedRecords) { // 已锁
                    String lockerName = lockedRecords.getLockerName();
                    String lockerLoginName = lockedRecords.getLockerLoginName();
                    if (!super.getOnlineRoleUser().getLoginName().equals(lockerLoginName)) {
                        // 不是锁定人进入
                        String lockedMSG = "此酒店已被锁定，在被解锁之前，只有锁定人才能进入(锁定人:" + lockerName + "["
                            + lockerLoginName + "])";
                        request.setAttribute("lockedMSG", lockedMSG);
                        // request.setAttribute("backUrl","queryHotel.jsp");
                        return "forwordContract";
                    }
                } else {
                    if (null != super.getOnlineRoleUser()) {
                        orLockedRecord.setRemark(htlHotel.getChnName());
                        orLockedRecord.setLockerName(super.getOnlineRoleUser().getName());
                        orLockedRecord.setLockerLoginName(super.getOnlineRoleUser().getLoginName());
                        orLockedRecord.setLockTime(DateUtil.getSystemDate());
                        lockedRecordService.insertLockedRecord(orLockedRecord);
                    }
                }
                /** end haibo.li **/
            } else {
                if (null != lockedRecords) { // 已锁
                    String lockerName = lockedRecords.getLockerName();
                    String lockerLoginName = lockedRecords.getLockerLoginName();
                    if (!super.getOnlineRoleUser().getLoginName().equals(lockerLoginName)) {
                        // 不是锁定人进入
                        String lockedMSG = "此酒店已被锁定，在被解锁之前，只有锁定人才能进入（锁定人：" + lockerName + "["
                            + lockerLoginName + "]）";
                        request.setAttribute("lockedMSG", lockedMSG);
                        // request.setAttribute("backUrl","queryHotel.jsp");
                        return "lockedHint";
                    }
                } else {
                    if (null != super.getOnlineRoleUser()) {
                        orLockedRecord.setRemark(htlHotel.getChnName());
                        orLockedRecord.setLockerName(super.getOnlineRoleUser().getName());
                        orLockedRecord.setLockerLoginName(super.getOnlineRoleUser().getLoginName());
                        orLockedRecord.setLockTime(DateUtil.getSystemDate());
                        lockedRecordService.insertLockedRecord(orLockedRecord);
                    }
                }
            }
            /** 检查酒店是否锁定 add by chenjiajie V2.4 2008-8-26 End **/

        } else {
            htlHotel = new HtlHotel();
            htlHotel.setCheckinTime("1400");
            htlHotel.setCheckoutTime("1200");
        }
        if (null != FORWARD && FORWARD.equals("queryForCC")) {
            return FORWARD;
        }
        if (1 == hotelType) {
            FORWARD = "querySysOuter";
            return FORWARD;
        }
        FORWARD = "query";
        return FORWARD;
    }

    /**
     * 原来的方法，不做加锁解锁功能 modifu by chenjiajie 2008-10-13
     * 
     * @return
     */
    public String allFroward() {
        /*
         * QuotaQueryPo po = new QuotaQueryPo(); List<QuotaReturnPo> listest = new
         * ArrayList<QuotaReturnPo>(); ApplicationContext ctx = new
         * FileSystemXmlApplicationContext("classpath:spring/applicationContext.xml");
         * IQuotaForCCService yyy = (IQuotaForCCService)ctx.getBean("quotaForCCService");
         * po.setBeginDate(DateUtil.stringToDate("200712100000")); po.setChildRoomId(362);
         * po.setHotelId(1490); po.setQuotaType("1"); po.setMemberType(1); po.setPayMethod("pay");
         * po.setQuotaNum(2); po.setRoomTypeId(1520);
         * po.setEndDate(DateUtil.getDate(po.getBeginDate(), 2)); listest =
         * quotaForCCService.deductQuota(po);
         */
        super.setEntityID(hotelId);
        super.setEntity(super.populateEntity());
        this.setHtlHotel((HtlHotel) this.getEntity());
        if (0 < hotelId) {

            // 查询时间段内的早餐价信息和接送价信息 add by baofeng.si V2.3 2008-5-30 Start
            Map params = super.getParams();
            Date beginDate = DateUtil.getDate((String) params.get("beginDate"));
            Date endDate = DateUtil.getDate((String) params.get("endDate"));
            if (null == beginDate) {
                beginDate = new Date();
            }
            if (null == endDate) {
                endDate = new Date();
            }
            HtlContract contract = contractManage.checkContractDateNew(hotelId, beginDate);
            // 查询时间段内的早餐价信息和接送价信息 add by baofeng.si V2.3 2008-5-30 End

            htlHotel = hotelManage.findHotel(hotelId);
            lsRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(htlHotel.getID().longValue());
            lsCtctSel = htlHotel.getHtlCtct();
            lsSellSel = htlHotel.getSellSeason();
            lsTrafficInfo = htlHotel.getHtlTrafficInfo();
            lsBookSetup = htlHotel.getHtlBookSetup();
            lsCtctrowNum = lsCtctSel.size();
            lsSellrowNum = lsSellSel.size();
            lsHtlHotelExt = htlHotel.getHtelHotelExt();

            trafficNum = lsTrafficInfo.size();
            lsBookSetupwNum = lsBookSetup.size();

            if (null != htlHotel.getAcceptCustom() && 0 < htlHotel.getAcceptCustom().length()) {
                custom = 1;
            } else {
                custom = 0;
            }
            // 房态负责人
            lsHtlHotelExt = htlHotel.getHtelHotelExt();
            if (0 < lsHtlHotelExt.size()) {
                HtlHotelExt hoext = new HtlHotelExt();
                for (int i = 0; i < lsHtlHotelExt.size(); i++) {
                    hoext = (HtlHotelExt) lsHtlHotelExt.get(i);
                    String roomStateManager = hoext.getRoomStateManager();
                    log.info(roomStateManager);
                    request.setAttribute("roomStateManager", roomStateManager);
                }
            }

        } else {
            htlHotel = new HtlHotel();
            htlHotel.setCheckinTime("1400");
            htlHotel.setCheckoutTime("1200");
        }
        if (null != FORWARD && FORWARD.equals("queryForCC")) {
            return FORWARD;
        }
        if (null != FORWARD && FORWARD.equals("queryForCCNew")) {
            return FORWARD;
        }
        if (1 == hotelType) {
            FORWARD = "querySysOuter";
            return FORWARD;
        }
        FORWARD = "query";
        return FORWARD;
    }

    /**
     * 修改一个酒店的基本信息
     * 
     * 目前只是初步实现，以后还需要进行相关逻辑判断和元素补充 如修改人，修改时间，修改内容等等
     * 
     * @param htlHotel
     *            酒店的基本实体类
     * @return SUCCESS，ERROR
     */
    public String updateHotel() {
        super.setEntity(super.populateEntity());
        this.setHtlHotel((HtlHotel) this.getEntity());
        Map params = super.getParams();
        List lsCtct = MyBeanUtil.getBatchObjectFromParam(params, HtlCtct.class, lsCtctrowNum);
        List lsSell = MyBeanUtil.getBatchObjectFromParam(params, SellSeason.class, lsSellrowNum);
        List lsTraffic = MyBeanUtil.getBatchObjectFromParam(params, HtlTrafficInfo.class,
            trafficNum);

        List lisBookSetup = MyBeanUtil.getBatchObjectFromParam(params, HtlBookSetup.class,
            lsBookSetupwNum);

        htlHotel.setHotelType(BizRuleCheck.ArrayToString(hotelTypeCheck));

        htlHotel.setCreditCardInfo(BizRuleCheck.ArrayToString(creditCard));

        htlHotel.setLanguage(BizRuleCheck.ArrayToString(lang));
        if (null != super.getOnlineRoleUser()) {
            lsCtct = CEntityEvent.setCEntity(lsCtct, super.getOnlineRoleUser().getName(), super
                .getOnlineRoleUser().getLoginName());
            lsTraffic = CEntityEvent.setCEntity(lsTraffic, super.getOnlineRoleUser().getName(),
                super.getOnlineRoleUser().getLoginName());
            lsSell = CEntityEvent.setCEntity(lsSell, super.getOnlineRoleUser().getName(), super
                .getOnlineRoleUser().getLoginName());
            // lisBookSetup=CEntityEvent.setCEntity(lisBookSetup,
            // super.getOnlineRoleUser().getName(),
            // super.getOnlineRoleUser().getLoginName());
            htlHotel.setModifyById(super.getOnlineRoleUser().getLoginName());
            htlHotel.setModifyBy(super.getOnlineRoleUser().getName());
        }
        htlHotel.setModifyTime(DateUtil.getDate(new Date()));

        htlHotel.setHtlTrafficInfo(lsTraffic);
        htlHotel.setHtlCtct(lsCtct);
        htlHotel.setSellSeason(lsSell);
        htlHotel.setHtlBookSetup(lisBookSetup);

        int resultInt = hotelManage.modifyHotel(htlHotel);
        if (0 != resultInt) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String addForward() {
        FORWARD = "forward";
        return FORWARD;
    }

    public String selDefForward() {
        FORWARD = "selDef";
        return FORWARD;
    }

    public String selLieForward() {
        FORWARD = "selLie";
        return FORWARD;
    }

    public String selfreSForward() {
        FORWARD = "selfrS";
        return FORWARD;
    }

    public String udForward() {
        FORWARD = "update";
        htlHotel = hotelManage.findHotel(hotelId);
        lsCtctSel = htlHotel.getHtlCtct();
        lsSellSel = htlHotel.getSellSeason();
        lsTrafficInfo = htlHotel.getHtlTrafficInfo();

        lsCtctrowNum = lsCtctSel.size();
        lsSellrowNum = lsSellSel.size();
        trafficNum = lsTrafficInfo.size();
        return FORWARD;
    }

    /**
     * 新增加一个酒店
     * 
     * @param htlHotel
     *            酒店的基本实体类
     * @return hotelid
     */
    public String addHotel() {
        super.setEntity(super.populateEntity());
        this.setHtlHotel((HtlHotel) this.getEntity());
        Map params = super.getParams();
        List lsCtct = MyBeanUtil.getBatchObjectFromParam(params, HtlCtct.class, lsCtctrowNum);
        List lsSell = MyBeanUtil.getBatchObjectFromParam(params, SellSeason.class, lsSellrowNum);
        List lsTraffic = MyBeanUtil.getBatchObjectFromParam(params, HtlTrafficInfo.class,
            trafficNum);

        new BizRuleCheck();
        htlHotel.setActive(BizRuleCheck.getTrueString());
        if (null != super.getOnlineRoleUser()) {
            htlHotel.setCreateById(super.getOnlineRoleUser().getLoginName());
            htlHotel.setCreateBy(super.getOnlineRoleUser().getName());
            htlHotel.setCreateTime(DateUtil.getSystemDate());
        }
        htlHotel.setModifyTime(DateUtil.getDate(new Date()));
        if (null != super.getOnlineRoleUser()) {
            lsCtct = CEntityEvent.setCEntity(lsCtct, super.getOnlineRoleUser().getName(), super
                .getOnlineRoleUser().getLoginName());
            lsTraffic = CEntityEvent.setCEntity(lsTraffic, super.getOnlineRoleUser().getName(),
                super.getOnlineRoleUser().getLoginName());
            lsSell = CEntityEvent.setCEntity(lsSell, super.getOnlineRoleUser().getName(), super
                .getOnlineRoleUser().getLoginName());
            // lisBookSetup=CEntityEvent.setCEntity(lisBookSetup,
            // super.getOnlineRoleUser().getName(),
            // super.getOnlineRoleUser().getLoginName());
        }

        htlHotel.setHotelType(BizRuleCheck.ArrayToString(hotelTypeCheck));

        htlHotel.setCreditCardInfo(BizRuleCheck.ArrayToString(creditCard));

        htlHotel.setLanguage(BizRuleCheck.ArrayToString(lang));
        htlHotel.setHtlTrafficInfo(lsTraffic);
        htlHotel.setHtlCtct(lsCtct);
        htlHotel.setSellSeason(lsSell);
        Long hotelId = hotelManage.createHotel(htlHotel);

        if (0 >= hotelId.intValue()) {
            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * 删除一个酒店信息
     * 
     */
    public String delHotelInfo() {
        int i;
        i = hotelManage.delHotelInfo(hotelId);
        
        // 增加发送mq信息 add by chenkeming
        sendLuceneMQ.send("hotelInfo#" + hotelId);
        
        if (0 == i) {
            FORWARD = "del";
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 跳到设置酒店优先级信息
     * 
     */
    public String setPriority() {
        htlHotel = hotelManage.findHotel(hotelId);
        htlSetPriority = hotelPriorityManage.findHotelPriority(hotelId);
        String PRI = "PRI";
        return PRI;
    }

    public String saveOrUpdate() { 	
    	
        super.setEntity(super.populateEntity());
        this.setHtlHotel((HtlHotel) this.getEntity());
        Map params = super.getParams();
        
        //勾选全部禁烟后 对应的该酒店下的HtlRoomType 全部增加禁烟 add by xuyiwen 2010-10-21 
        if(null != params.get("hotelId")&&null != params.get("isAllNoSmoking")){
        	Long  id=Long.valueOf(params.get("hotelId").toString());
        	int isAllNoSmoking = Integer.valueOf(params.get("isAllNoSmoking").toString());
	        if(isAllNoSmoking == 1){ 
	        	List<HtlRoomtype> htlRoomTypes=hotelRoomTypeService.getHtlRoomTypeListByHotelId(id);
	        	for(HtlRoomtype htlRoomtype:htlRoomTypes){
	        		String roomEquipment = htlRoomtype.getRoomEquipment(); //注意roomEquipment可能为空
	        		if(null==roomEquipment||"".equals(roomEquipment)){
	        			htlRoomtype.setRoomEquipment(NOSMOKING+",");
	        			hotelRoomTypeService.updateHtlRoomType(htlRoomtype);
	        		}
	        		else if(roomEquipment.indexOf(NOSMOKING)==-1){ //如果没有禁烟的标记、加入并更新
	        			htlRoomtype.setRoomEquipment(roomEquipment+NOSMOKING+",");
	        			//TODO 调用manage里面的方法 进行更新
	        			hotelRoomTypeService.updateHtlRoomType(htlRoomtype);
	        		}
	        	}
	        }
        }
        
        List lsCtct = MyBeanUtil.getBatchObjectFromParam(params, HtlCtct.class, lsCtctrowNum);
        List lsCtctTemp = new ArrayList();
        for (int i = 0; i < lsCtct.size(); i++) {
        	HtlCtct ct = (HtlCtct) lsCtct.get(i);
            if (null == ct.getCtctType() 
            		&& (null == ct.getCtctchnName() ||  ct.getCtctchnName().trim().equals(""))
            		&& (null == ct.getCtctMobile() || ct.getCtctMobile().trim().equals(""))
            		&& (null == ct.getCtcttelephone() || ct.getCtcttelephone().trim().equals(""))
            		&& (null==ct.getCtctfax() || ct.getCtctfax().trim().equals(""))
            		&& (null == ct.getCtctheadship() || ct.getCtctheadship().equals(""))
            		&& (null == ct.getCtctheadtime() || ct.getCtctheadtime().equals(""))
            		&& (null == ct.getCtctemail() || ct.getCtctemail().equals(""))) {
                continue;
            }
            lsCtctTemp.add(ct);
            ct.setHtlHotel(htlHotel);
        }
        List lsSell = MyBeanUtil.getBatchObjectFromParam(params, SellSeason.class, lsSellrowNum);

        List lsSellTemp = new ArrayList();
        for (int i = 0; i < lsSell.size(); i++) {
            SellSeason ss = (SellSeason) lsSell.get(i);
            // if(ss.getSellName()==null || ss.getSellName().trim().equals("")){
            // continue;
            // }
            if ((null != ss.getSellBeginDate() && !ss.getSellBeginDate().toString().equals(""))
                || (null != ss.getSellEndDate() && !ss.getSellEndDate().toString().equals(""))
                || (null != ss.getSellWeek() && !ss.getSellWeek().equals(""))
                || (null != ss.getSellRemark() && !ss.getSellRemark().equals(""))) {
                lsSellTemp.add(ss);
                ss.setHtlHotel(htlHotel);
            } else {
                continue;
            }

        }
        List lsTraffic = MyBeanUtil.getBatchObjectFromParam(params, HtlTrafficInfo.class,
            trafficNum);
        List lsTrafficTemp = new ArrayList();
        for (int i = 0; i < lsTraffic.size(); i++) {
            HtlTrafficInfo ti = (HtlTrafficInfo) lsTraffic.get(i);
            if (null == ti.getArriveAddress() || ti.getArriveAddress().trim().equals("")) {
                continue;
            }
            lsTrafficTemp.add(ti);
            ti.setHtlHotel(htlHotel);
        }
        List lisBookSetup = MyBeanUtil.getBatchObjectFromParam(params, HtlBookSetup.class,
            lsBookSetupwNum);
        List lisBookSetupTemp = new ArrayList();
        for (int i = 0; i < lisBookSetup.size(); i++) {
            HtlBookSetup bs = (HtlBookSetup) lisBookSetup.get(i);
            if (null == bs.getBookChnName() || bs.getBookChnName().trim().equals("")) {
                continue;
            }
            new BizRuleCheck();
            bs.setActive(BizRuleCheck.getTrueString());
            if (null == bs.getID() || 0 == bs.getID()) {
                if (null != super.getOnlineRoleUser()) {
                    bs.setCreateById(super.getOnlineRoleUser().getLoginName());
                    bs.setCreateBy(super.getOnlineRoleUser().getName());
                }
            }
            if (null != super.getOnlineRoleUser()) {
                bs.setModifyBy(super.getOnlineRoleUser().getName());
                bs.setModifyById(super.getOnlineRoleUser().getLoginName());
                bs.setModifyTime(DateUtil.getSystemDate());
            }

            bs.setCreateTime(DateUtil.getSystemDate());
            bs.setHtlHotel(htlHotel);
            lisBookSetupTemp.add(bs);
        }
        if (null != super.getOnlineRoleUser()) {
        	lsCtctTemp = CEntityEvent.setCEntity(lsCtctTemp, super.getOnlineRoleUser().getName(), super
                .getOnlineRoleUser().getLoginName());
            lsTrafficTemp = CEntityEvent.setCEntity(lsTrafficTemp, super.getOnlineRoleUser()
                .getName(), super.getOnlineRoleUser().getLoginName());
            lsSellTemp = CEntityEvent.setCEntity(lsSellTemp, super.getOnlineRoleUser().getName(),
                super.getOnlineRoleUser().getLoginName());
            // lisBookSetup=CEntityEvent.setCEntity(lisBookSetup,
            // super.getOnlineRoleUser().getName(),
            // super.getOnlineRoleUser().getLoginName());
        }
        htlHotel.setHotelType(BizRuleCheck.ArrayToString(hotelTypeCheck));

        /*
         * 房态负责人修改 //房态负责人begin //List lsHtlHotelExt = htlHotel.getHtelHotelExt();
         * if(lsHtlHotelExt.size()>0) { String roomStateManager =
         * (String)params.get("roomStateManager"); if(StringUtil.isValidStr(roomStateManager)) {
         * HtlHotelExt he = (HtlHotelExt)lsHtlHotelExt.get(0); he.setHtlHotel(htlHotel);
         * he.setRoomStateManager(roomStateManager); hotelManage.saveOrUpdateExt(he); } }else
         * if(lsHtlHotelExt.size()==0) { String roomStateManager =
         * (String)params.get("roomStateManager"); if(StringUtil.isValidStr(roomStateManager)) {
         * HtlHotelExt he = new HtlHotelExt(); he.setHtlHotel(htlHotel);
         * he.setRoomStateManager(roomStateManager); hotelManage.saveOrUpdateExt(he); } }
         * 
         * //end
         */

        // 加酒店扩展表 add by zhineng.zhuang
        List lsHtlHotelExt = htlHotel.getHtelHotelExt();
        // modify by shizhongwen 2009-05-14 酒店2.8 加入酒店扩展表isCTSHotel 信息
        String isCTSHotel = "";
        if (null != lsHtlHotelExt && 0 < lsHtlHotelExt.size()) {
            for (Object object : lsHtlHotelExt) {
                HtlHotelExt hotelext = (HtlHotelExt) object;
                isCTSHotel = hotelext.getIsCTSHotel();
            }
        }
        List lsHHExt = MyBeanUtil.getBatchObjectFromParam(params, HtlHotelExt.class, 0);

        for (int i = 0; i < lsHHExt.size(); i++) {
            HtlHotelExt htlHotelExt = (HtlHotelExt) lsHHExt.get(i);
            String roomStateManager = (String) params.get("roomStateManager");// 房态负责人
            htlHotelExt.setRoomStateManager(roomStateManager);
            htlHotelExt.setHtlHotel(htlHotel);
            htlHotelExt.setIsCTSHotel(isCTSHotel);
            String freeServiceRemark=(String)params.get("freeServiceRemark");//免费服务信息备注
            htlHotelExt.setFreeServiceRemark(freeServiceRemark);
        }
        htlHotel.setHtelHotelExt(lsHHExt);
        htlHotel.setHotelType(BizRuleCheck.ArrayToString(hotelTypeCheck));
        htlHotel.setCreditCardInfo(BizRuleCheck.ArrayToString(creditCard));
        htlHotel.setSaleChannel(BizRuleCheck.ArrayToString(saleChannel));
        htlHotel.setPurchaseChannel(BizRuleCheck.ArrayToString(purchaseChannel));
        htlHotel.setLanguage(BizRuleCheck.ArrayToString(lang));
        htlHotel.setHtlTrafficInfo(lsTrafficTemp);
        htlHotel.setHtlCtct(lsCtctTemp);
        htlHotel.setSellSeason(lsSellTemp);
        htlHotel.setHtlBookSetup(lisBookSetupTemp);
        htlHotel.setActive(BizRuleCheck.getHotelActive());
        htlHotel.setTheme(BizRuleCheck.ArrayToString(theme));

        if (null == htlHotel.getID() || 0 == htlHotel.getID()) {
            if (null != super.getOnlineRoleUser()) {
                htlHotel.setCreateById(super.getOnlineRoleUser().getLoginName());
                htlHotel.setCreateBy(super.getOnlineRoleUser().getName());
                htlHotel.setCreateTime(DateUtil.getSystemDate());
            }
        }
        if (null != super.getOnlineRoleUser()) {
            htlHotel.setModifyById(super.getOnlineRoleUser().getLoginName());
            htlHotel.setModifyBy(super.getOnlineRoleUser().getName());
        }

        htlHotel.setModifyTime(DateUtil.getDate(new Date()));

        long resultInt = hotelManage.saveOrUpdateHotel(htlHotel);
        if (0 != resultInt) {
            return ERROR;
        }

        /** 解除酒店锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
        lockedRecordService.deleteLockedRecordTwo(String.valueOf(htlHotel.getID()), 01, super
            .getOnlineRoleUser().getLoginName());
        /** 解除酒店锁定 add by chenjiajie V2.4 2008-8-26 End* */
        
        // 增加发送mq信息 add by chenkeming
        sendLuceneMQ.send("hotelInfo#" + htlHotel.getID());
        
        hotelId = htlHotel.getID();
        if (null != FORWARD && 0 < FORWARD.length()) {
        	if(0 == contractId && "saveAndContract".equals(FORWARD))
        		FORWARD = "toNewContract";//如果是找不到合同,则返回到新增合同
            return FORWARD;
        } else {
            return SUCCESS;
        }
    }

    /**
     * 返回酒店基本信息查询页面，并解锁
     * 
     * @return
     */
    public String backToList() {
        Map params = super.getParams();
        String hotelId = (String) params.get("hotelId");
        /** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
        lockedRecordService.deleteLockedRecordTwo(hotelId, 01, super.getOnlineRoleUser()
            .getLoginName());
        /** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 End* */
        return "queryhotel";
    }

    /**
     * 解锁公用方法
     * 
     * @return
     */
    public String unLockHotel() {
        Map params = super.getParams();
        String recordCD = (String) params.get("recordCD");
        String lockType = (String) params.get("lockType");
        /** 解除酒店合同锁定 add by chenjiajie V2.4 2008-9-8 Begin* */
        lockedRecordService.deleteLockedRecord(recordCD, Integer.valueOf(lockType));
        /** 解除酒店合同锁定 add by chenjiajie V2.4 2008-9-8 End* */
        return "unLock";
    }

    /**
     * 批量解锁公用方法
     * 
     * @return
     */
    public String unLockSelected() {
        /** 批量解除酒店锁定 add by chenjiajie V2.4 2008-9-22 Begin* */
        Map params = super.getParams();
        String unlockParams = (String) params.get("unlockParams");
        String[] unlockItems = unlockParams.split("\\*");
        for (int i = 0; i < unlockItems.length; i++) {
            String[] unlockItem = unlockItems[i].split("\\|");
            lockedRecordService.deleteLockedRecord(unlockItem[0], Integer.valueOf(unlockItem[1]));
        }
        /** 批量解除酒店锁定 add by chenjiajie V2.4 2008-9-22 End* */
        return "unLock";
    }

    /**
     * 新增系统外酒店
     * 
     * @return
     */
    public String saveOrUpdate1() {
        super.setEntity(super.populateEntity());
        this.setHtlHotel((HtlHotel) this.getEntity());
        Map params = super.getParams();
        /**qc 653 没有传cusId到跳转的Action add by xiaowei.wang  begin */
        member = (MemberDTO) getSession().get("onlineMember");
        cusId = member.getMembercd();
        /**qc 653 没有传cusId到跳转的Action add by xiaowei.wang  end */
        if (params.containsKey("ID")) {
            params.put("ID", null);
        }
        List lsCtct = MyBeanUtil.getBatchObjectFromParam(params, HtlCtct.class, lsCtctrowNum);
        htlHotel.getHtlCtct().clear();
        for (int i = 0; i < lsCtct.size(); i++) {
            HtlCtct ctct = (HtlCtct) lsCtct.get(i);
            htlHotel.getHtlCtct().add(ctct);
            ctct.setHtlHotel(htlHotel);
        }
        // List lsSell = MyBeanUtil.getBatchObjectFromParam(params,
        // SellSeason.class, lsSellrowNum);
        // for (int i=0;i<lsSell.size();i++){
        // SellSeason ss= (SellSeason)lsSell.get(i);
        // ss.setHtlHotel(htlHotel);
        // }
        List lsTraffic = MyBeanUtil.getBatchObjectFromParam(params, HtlTrafficInfo.class,
            trafficNum);
        htlHotel.getHtlTrafficInfo().clear();
        for (int i = 0; i < lsTraffic.size(); i++) {
            HtlTrafficInfo ti = (HtlTrafficInfo) lsTraffic.get(i);
            if (null == ti.getArriveAddress() || ti.getArriveAddress().trim().equals("")) {
                continue;
            }
            htlHotel.getHtlTrafficInfo().add(ti);
            ti.setHtlHotel(htlHotel);
        }
        List lisBookSetup = MyBeanUtil.getBatchObjectFromParam(params, HtlBookSetup.class,
            lsBookSetupwNum);
        htlHotel.getHtlBookSetup().clear();
        for (int i = 0; i < lisBookSetup.size(); i++) {
            HtlBookSetup bs = (HtlBookSetup) lisBookSetup.get(i);
            if (null == bs.getBookChnName() || bs.getBookChnName().trim().equals("")) {
                continue;
            }
            new BizRuleCheck();
            bs.setActive(BizRuleCheck.getTrueString());
            if (null != super.getOnlineRoleUser()) {
                bs.setCreateById(super.getOnlineRoleUser().getLoginName());
                bs.setCreateBy(super.getOnlineRoleUser().getName());
            }
            bs.setCreateTime(DateUtil.getSystemDate());
            htlHotel.getHtlBookSetup().add(bs);
            bs.setHtlHotel(htlHotel);
        }
        //		
        // htlHotel.setHotelType(BizRuleCheck.ArrayToString(hotelTypeCheck));
        // htlHotel.setCreditCardInfo(BizRuleCheck.ArrayToString(creditCard));
        // htlHotel.setLanguage(BizRuleCheck.ArrayToString(lang));
        // htlHotel.setSellSeason(lsSell);
        if (null != super.getOnlineRoleUser()) {
            CEntityEvent.setCEntity(htlHotel.getHtlCtct(), super.getOnlineRoleUser().getName(),
                super.getOnlineRoleUser().getLoginName());
            CEntityEvent.setCEntity(htlHotel.getHtlTrafficInfo(), super.getOnlineRoleUser()
                .getName(), super.getOnlineRoleUser().getLoginName());
            // lsSell=CEntityEvent.setCEntity(lsSell,
            // super.getOnlineRoleUser().getName(),
            // super.getOnlineRoleUser().getLoginName());
            // lisBookSetup=CEntityEvent.setCEntity(lisBookSetup,
            // super.getOnlineRoleUser().getName(),
            // super.getOnlineRoleUser().getLoginName());
        }
        htlHotel.setActive(BizRuleCheck.getHotelActive());
        htlHotel.setHotelSystemSign("02");

        if (null != super.getOnlineRoleUser()) {
            htlHotel.setCreateById(super.getOnlineRoleUser().getLoginName());
            htlHotel.setCreateBy(super.getOnlineRoleUser().getName());
            htlHotel.setCreateTime(DateUtil.getSystemDate());
        }
        htlHotel.setModifyTime(DateUtil.getDate(new Date()));

        long resultInt = hotelManage.saveOrUpdateHotel(htlHotel);
        if (0 != resultInt) {
            return ERROR;
        }
        if (null != FORWARD && 0 < FORWARD.length()) {
            return FORWARD;
        } else {
            return "pre";
        }

    }

    /**
     * 修改酒店联系人信息
     * 
     * @return
     */
    public String saveOrUpdateHtlCtct() {
        if (0 < hotelId && null != htlCtct.getID()) {
            htlHotel = hotelManage.findHotel(hotelId);
            htlCtct.setHtlHotel(htlHotel);
            long resultInt = hotelManage.saveOrUpdateCtct(htlCtct);
            if (0 != resultInt) {
                return ERROR;
            }
        } else {
            return ERROR;
        }
        FORWARD = "updateCtct";
        request.setAttribute("ctctMSG", "1");
        return FORWARD;
    }

    public String channelMappingCompareControl(){
    	
    	channelChnName =  CHANNEL_CODE_CHNNAME_MAP.get(channelType);
    	try{
	    	List<ExMapping> hotelList = hotelManage.getHotelMapping(HotelMappingType.HOTEL_TYPE, channelType);//从ExMapping取出所有酒店列表
	    	List<ExMapping> roomList =  hotelManage.getHotelMapping(HotelMappingType.ROOM_TYPE, channelType);//从ExMapping取出所有房间列表
	    	List<HtlHotelBase> hotelBaseLst = hotelManage.getBaseHotelListForExMapping(hotelList, roomList);//得到所有的酒店基本信息列表(本部的)
	    	
	    	List<HtlChannelMapInfo> htlChannelMapInfoList = hotelManage.queryHtlChannelMapInfoList(HotelMappingType.ROOM_TYPE,channelType);
	    	
	    	hotelBaseLst = hotelManage.addHtlChannelMapInfoListToHotelBaseLst(hotelBaseLst,htlChannelMapInfoList);
	    	
	    	hotelBaseLst = hotelManage.mergerHotelRoomMC(hotelBaseLst);	
	    	request.setAttribute("hotelLst", hotelBaseLst);
    	}catch (Exception e ){
    		log.error("酒店编码比较出错，channelType = "+channelType,e);
    	}
    	
    	return "allhotel";
    }
    
    /**
     * 设置酒店
     * @param fromLst
     * @param toLst
     */
    private void setHtlName(List<MGExHotelListReturn> fromLst,List<MGExHotelListReturn> toLst){
    	for(MGExHotelListReturn from:fromLst){
    		String fromCode = from.getHotelCode();
    		if(null!=fromCode){
	    		for(MGExHotelListReturn to:toLst){
	    			
	    			if(fromCode.equals(to.getHotelCode())){
	    				to.setHotelName(from.getHotelName());
	    			}
	    		}
    		}
    	}
    }
    
    /**
     * 
     * 盗梦等网站应用重新导入lucene文件的消息 add by chenkeming
     * 
     * @return
     */
    public String reloadLucene() {
    	
    	sendLuceneMQ.send("reload");
    	
    	return super.forwardMsg("重新读取lucene文件!");
    }
    
    public List getLsCtctSel() {
        return lsCtctSel;
    }

    public void setLsCtctSel(List lsCtctSel) {
        this.lsCtctSel = lsCtctSel;
    }

    public List getLsSellSel() {
        return lsSellSel;
    }

    public void setLsSellSel(List lsSellSel) {
        this.lsSellSel = lsSellSel;
    }

    public int getLsCtctrowNum() {
        return lsCtctrowNum;
    }

    public void setLsCtctrowNum(int lsCtctrowNum) {
        this.lsCtctrowNum = lsCtctrowNum;
    }

    public int getLsSellrowNum() {
        return lsSellrowNum;
    }

    public void setLsSellrowNum(int lsSellrowNum) {
        this.lsSellrowNum = lsSellrowNum;
    }

    public String[] getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String[] creditCard) {
        this.creditCard = creditCard;
    }

    public String[] getHotelTypeCheck() {
        return hotelTypeCheck;
    }

    public void setHotelTypeCheck(String[] hotelTypeCheck) {
        this.hotelTypeCheck = hotelTypeCheck;
    }

    public String[] getSaleChannel() {
        return saleChannel;
    }

    public void setSaleChannel(String[] saleChannel) {
        this.saleChannel = saleChannel;
    }

    public List getLsBookSetup() {
        return lsBookSetup;
    }

    public void setLsBookSetup(List lsBookSetup) {
        this.lsBookSetup = lsBookSetup;
    }

    public List getLsTrafficInfo() {
        return lsTrafficInfo;
    }

    public void setLsTrafficInfo(List lsTrafficInfo) {
        this.lsTrafficInfo = lsTrafficInfo;
    }

    public int getTrafficNum() {
        return trafficNum;
    }

    public void setTrafficNum(int trafficNum) {
        this.trafficNum = trafficNum;
    }

    public String[] getLang() {
        return lang;
    }

    public void setLang(String[] lang) {
        this.lang = lang;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public int getLsBookSetupwNum() {
        return lsBookSetupwNum;
    }

    public void setLsBookSetupwNum(int lsBookSetupwNum) {
        this.lsBookSetupwNum = lsBookSetupwNum;
    }

    public String getFORWARD() {
        return FORWARD;
    }

    public void setFORWARD(String forward) {
        FORWARD = forward;
    }

    public List getLsRoomType() {
        return lsRoomType;
    }

    public void setLsRoomType(List lsRoomType) {
        this.lsRoomType = lsRoomType;
    }

    public int getLsRoomTypeNum() {
        return lsRoomTypeNum;
    }

    public void setLsRoomTypeNum(int lsRoomTypeNum) {
        this.lsRoomTypeNum = lsRoomTypeNum;
    }

    public String getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(String roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public int getCustom() {
        return custom;
    }

    public void setCustom(int custom) {
        this.custom = custom;
    }

    public HotelPriorityManage getHotelPriorityManage() {
        return hotelPriorityManage;
    }

    public void setHotelPriorityManage(HotelPriorityManage hotelPriorityManage) {
        this.hotelPriorityManage = hotelPriorityManage;
    }

    public HtlSetPriority getHtlSetPriority() {
        return htlSetPriority;
    }

    public void setHtlSetPriority(HtlSetPriority htlSetPriority) {
        this.htlSetPriority = htlSetPriority;
    }

    public int getHotelType() {
        return hotelType;
    }

    public void setHotelType(int hotelType) {
        this.hotelType = hotelType;
    }

    public IQuotaForCCService getQuotaForCCService() {
        return quotaForCCService;
    }

    public void setQuotaForCCService(IQuotaForCCService quotaForCCService) {
        this.quotaForCCService = quotaForCCService;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getJustForCC() {
        return justForCC;
    }

    public void setJustForCC(int justForCC) {
        this.justForCC = justForCC;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public List<HtlChargeBreakfast> getBreakfastList() {
        return breakfastList;
    }

    public void setBreakfastList(List<HtlChargeBreakfast> breakfastList) {
        this.breakfastList = breakfastList;
    }

    public List<HtlWelcomePrice> getWelcomeList() {
        return welcomeList;
    }

    public void setWelcomeList(List<HtlWelcomePrice> welcomeList) {
        this.welcomeList = welcomeList;
    }

    public List<HtlAddBedPrice> getBedPriceList() {
        return bedPriceList;
    }

    public void setBedPriceList(List<HtlAddBedPrice> bedPriceList) {
        this.bedPriceList = bedPriceList;
    }

    public List<HtlInternet> getInternetList() {
        return internetList;
    }

    public void setInternetList(List<HtlInternet> internetList) {
        this.internetList = internetList;
    }

    public HtlCtct getHtlCtct() {
        return htlCtct;
    }

    public void setHtlCtct(HtlCtct htlCtct) {
        this.htlCtct = htlCtct;
    }

    public ILockedRecordService getLockedRecordService() {
        return lockedRecordService;
    }

    public void setLockedRecordService(ILockedRecordService lockedRecordService) {
        this.lockedRecordService = lockedRecordService;
    }

    public String[] getPurchaseChannel() {
        return purchaseChannel;
    }

    public void setPurchaseChannel(String[] purchaseChannel) {
        this.purchaseChannel = purchaseChannel;
    }

    public List getLsHtlHotelExt() {
        return lsHtlHotelExt;
    }

    public void setLsHtlHotelExt(List lsHtlHotelExt) {
        this.lsHtlHotelExt = lsHtlHotelExt;
    }

    public String[] getTheme() {
        return theme;
    }

    public void setTheme(String[] theme) {
        this.theme = theme;
    }

	public long getContractId() {
		return contractId;
	}

	public void setContractId(long contractId) {
		this.contractId = contractId;
	}

	public IHDLService getHdlService() {
		return hdlService;
	}

	public void setHdlService(IHDLService hdlService) {
		this.hdlService = hdlService;
	}

	public String getCloseIeOrnot() {
		return closeIeOrnot;
	}
	
	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public void setCloseIeOrnot(String closeIeOrnot) {
		this.closeIeOrnot = closeIeOrnot;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public void setSendLuceneMQ(SendLuceneMQ sendLuceneMQ) {
		this.sendLuceneMQ = sendLuceneMQ;
	}

	public String getRequestChannel() {
		return requestChannel;
	}

	public void setRequestChannel(String requestChannel) {
		this.requestChannel = requestChannel;
	}


}
