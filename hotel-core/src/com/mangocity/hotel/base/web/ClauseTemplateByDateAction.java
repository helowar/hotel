package com.mangocity.hotel.base.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.ClaueType;
import com.mangocity.hotel.base.manage.ClauseTemplateByDateManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.RoomControlManage;
import com.mangocity.hotel.base.persistence.HtlAssure;
import com.mangocity.hotel.base.persistence.HtlAssureItemEveryday;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateFive;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateFour;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateOne;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateThree;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateTwo;
import com.mangocity.hotel.base.persistence.HtlBinding;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlPrepayEveryday;
import com.mangocity.hotel.base.persistence.HtlPrepayItemEveryday;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateFive;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateFour;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateOne;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateThree;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateTwo;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlReservCont;
import com.mangocity.hotel.base.persistence.HtlReservation;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.bean.MustDate;
import com.mangocity.util.bean.MyBeanUtil;

import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;

/**
 */
public class ClauseTemplateByDateAction extends PersistenceAction {

    private Long ID;

    private Long hotelId;
    
    private String hotelChnName;
    
    private  HotelManage hotelManage;

    private Long reservationTemplateId;

    private String reservationName;

    private Date validDate;

    private Long priceTypeId;

    private String payToPrepay;

    private String attention;

    private String createBy;

    private String createById;

    private Date createTime;

    private String modifyBy;

    private String modifyById;

    private Date modifyTime;

    private String delBy;

    private String delById;

    private Date delTime;

    private String active;

    private String beginDt;

    private String currentlyDt;

    private String endDt;

    private String isFirstQuery;

    private String priceType;

    private long price;

    private Long priceTypeID;

    /*
     * 
     * ************************************************预订条款 Begin
     */

    /*
     * 提前天数
     */
    private Long aheadDay;

    /*
     * 提前时间点
     */
    private String aheadTime;

    /*
     * 必须提前日期
     */
    private Date mustAheadDate;

    /*
     * 必须提前日期时间点
     */
    private String mustAheadTime;

    /**
     * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 begin
     */
    //必须的起始日期  
    private Date mustFromDate;
    
    //必须的截止日期
    private Date mustToDate;
    
    //必须的起始时间  
    private String mustFromTime;
    
    //必须的截止时间
    private String mustToTime;
    
    /**
     * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 end
     */	
    
    /*
     * 连住晚数
     */
    private Long continueNights;

    /*
     * 最多连住间夜
     */
    private Long maxRestrictNights;

    /*
     * 最少连住间夜
     */
    private Long minRestrictNights;

    /*
     * 
     * *********************************************预订条款 End
     */

    /*
     * 
     * $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$担保条款 begin
     */
    /*
     * 是否无条件担保
     */
    private String isNotConditional;

    /*
     * 最晚担保时间
     */
    private String latestAssureTime;

    /*
     * 超房数量
     */
    private Long overRoomNumber;

    /*
     * 超间夜数量
     */
    private Long overNightsNumber;

    /*
     * 担保类型
     */
    private String assureType;

    /*
     * 是否需要担保函
     */
    private String assureLetter;

    private IHotelService hotelService;

    /*
     * 
     * $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$担保条款 End
     */

    /*
     * &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&预付条款 begin
     */

    /*
     * 结算方法
     */
    private String balanceType;

    /*
     * 支付类型
     */
    private String paymentType;

    /*
     * 预付金额类型
     */
    private String amountType;

    /*
     * 付款时限日期
     */
    private Date limitDate;

    /*
     * 付款时限日期时间点
     */
    private String limitTime;

    /*
     * 付款时限提前天数
     */
    private Long limitAheadDays;

    /*
     * 付款时限提前天数时间点
     */
    private String limitAheadDaysTime;

    /*
     * 付款时限类型
     */
    private String timeLimitType;

    /*
     * 扣款百分比
     */
    private String prepayDeductType;

    /*
     * 付款时限确认后天数
     */
    private Long daysAfterConfirm;

    /*
     * 付款时限确认后时间点
     */
    private String timeAfterConfirm;

    /*
     * &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&预付条款 end
     */

    /*
     * 按日期调整预定条款实现类
     */
    private ClauseTemplateByDateManage clauseTemplateByDateManage;

    private RoomControlManage roomControlManage;

    private List<HtlRoomtype> lstRoomType = new ArrayList<HtlRoomtype>();

    /*
     * 
     * 酒店预订担保预付条款
     */

    private HtlPreconcertItem htlPreconcertItemZ;

    /**
     * 预订条款
     */
    private List<HtlReservation> htlReservationList = new ArrayList<HtlReservation>();

    /**
     * 每日预订必须连住日期表
     */
    private List<HtlReservCont> lisHtlReservacont = new ArrayList<HtlReservCont>();

    /**
     * 每日的担保条款
     */
    private List<HtlAssure> htlAssureList = new ArrayList<HtlAssure>();

    /**
     * 每日的担保修改取消条款
     */
    private List<HtlAssureItemEveryday> lisHtlAssureItemEveryday = 
        new ArrayList<HtlAssureItemEveryday>();

    private List<HtlAssureItemTemplateOne> lisHtlAssureItemTemplateOne = 
        new ArrayList<HtlAssureItemTemplateOne>();

    private List<HtlAssureItemTemplateTwo> lisHtlAssureItemTemplateTwo = 
        new ArrayList<HtlAssureItemTemplateTwo>();

    private List<HtlAssureItemTemplateThree> lisHtlAssureItemTemplateThree = 
        new ArrayList<HtlAssureItemTemplateThree>();

    private List<HtlAssureItemTemplateFour> lisHtlAssureItemTemplateFour = 
        new ArrayList<HtlAssureItemTemplateFour>();

    private List<HtlAssureItemTemplateFive> lisHtlAssureItemTemplateFive = 
        new ArrayList<HtlAssureItemTemplateFive>();

    /**
     *每日的预付条款
     */
    private List<HtlPrepayEveryday> htlPrepayEverydayList =
        new ArrayList<HtlPrepayEveryday>();

    /**
     * 每日的预付修改取消条款
     */
    private List<HtlPrepayItemEveryday> lisHtlPrepayItemEveryday = 
        new ArrayList<HtlPrepayItemEveryday>();

    private List<HtlPrepayItemTemplateOne> lisHtlPrepayItemTemplateOne = 
        new ArrayList<HtlPrepayItemTemplateOne>();

    private List<HtlPrepayItemTemplateTwo> lisHtlPrepayItemTemplateTwo = 
        new ArrayList<HtlPrepayItemTemplateTwo>();

    private List<HtlPrepayItemTemplateThree> lisHtlPrepayItemTemplateThree =
        new ArrayList<HtlPrepayItemTemplateThree>();

    private List<HtlPrepayItemTemplateFour> lisHtlPrepayItemTemplateFour = 
        new ArrayList<HtlPrepayItemTemplateFour>();

    private List<HtlPrepayItemTemplateFive> lisHtlPrepayItemTemplateFive =
        new ArrayList<HtlPrepayItemTemplateFive>();

    private List dataList = new ArrayList();

    private HtlBinding htlBinding;

    private int assuerOne;

    private int assuerTwo;

    private int assuerThree;

    private int assuerFour;

    private int assuerFive;

    private int prepayOne;

    private int prepayTwo;

    private int prepayThree;

    private int prepayFour;

    private int prepayFive;

    private List lsmustDate = new ArrayList();

    private int dateNum;

    private String validDt;

    // 房型名称
    private String roomName;

    /**
     * 从星期中筛选出来的数量 addby juesuchen
     */
    private int weekDateNum;

    /**
     * 必住日期的关系 and or
     */
    private String continueDatesRelation;

    private List Recordlist;

    private long RecordId;

    private long ContractId;

    private List lstContract = new ArrayList();

    public List getLstContract() {
        return lstContract;
    }

    public void setLstContract(List lstContract) {
        this.lstContract = lstContract;
    }

    public long getRecordId() {
        return RecordId;
    }

    public void setRecordId(long recordId) {
        RecordId = recordId;
    }

    public List getRecordlist() {
        return Recordlist;
    }

    public void setRecordlist(List recordlist) {
        Recordlist = recordlist;
    }

    @Override
    protected Class getEntityClass() {

        return HtlPreconcertItem.class;

    }

    /*
     * 查看指定酒店选定日期的预定条款
     */

    public String viewHtlPITByDate() {
    	
    	/*************************add by yong.zeng************************************/
    	String changePriceHint = "";
    	HtlHotel curhotel = hotelManage.findHotel(hotelId);
    	if(null!=curhotel){
        hotelChnName = curhotel.getChnName();
        
        changePriceHint = curhotel.getChangePriceHint();
    	}
        request.setAttribute("changePriceHint", changePriceHint);
		
        super.setEntityID(ID);
        super.setEntity(super.populateEntity());
        this.setHtlPreconcertItemZ((HtlPreconcertItem) this.getEntity());

        // 本部：当一段时间内有的日期绑定有的日期不绑定时，按日期调整预订条款查询后报错 的bug修复 add by shengwei.zuo 2009-05-22 hotel2.6
        // begin
        // 检验开始日期和结束日期是否为空
        if (null != beginDt && !beginDt.equals("") && null != endDt && !endDt.equals("")) {
            /*
             * 查询出该酒店在指定日期范围内的日期列表；
             */
            dataList = clauseTemplateByDateManage.findDateLis(hotelId, beginDt, endDt);
            if (0 == dataList.size() || null == dataList) { // 如果这个酒店没有相应的日期则跳转到没有显示页面
                return "clauseError";
            }

            htlPreconcertItemZ.setBeginDt(beginDt);// 把开始时间和结束时间set
            htlPreconcertItemZ.setEndDt(endDt);
        }

        lstContract = clauseTemplateByDateManage.queryContract(hotelId);// 查询出合同
        if (null != lstContract && 0 < lstContract.size()) {
            HtlContract hc = (HtlContract) lstContract.get(0);
            ContractId = hc.getID();// 取得合同ID
        }
        /*
         * 第一次进入按日期调整预定条款页面，查询该酒店的预定条款相关信息
         */
        if (null == isFirstQuery || isFirstQuery.equals("")) {// 页面传来参数 判断是否第一次查询
            // 如果是第一次进来则取第一天的记录，转换为字符串
            validDate = DateUtil.getDate(dataList.get(0).toString());
            validDt = DateUtil.dateToString(validDate);

            // 查询该酒店下没有解除绑定的房型
            lstRoomType = clauseTemplateByDateManage.findRoomTypeLis(hotelId, validDt);
            if (lstRoomType.isEmpty()) {
                return "clauseError";
            }

            // 第一次查询，设置一个默认的价格类型
            HtlRoomtype htlRoomtypeEntiy = lstRoomType.get(0);
            if (null != htlRoomtypeEntiy) {
                List priceTypeLis = htlRoomtypeEntiy.getLstPriceType();
                if (!priceTypeLis.isEmpty()) {
                    HtlPriceType htlPriceTypeEntiy = (HtlPriceType) priceTypeLis.get(0);
                    price = htlPriceTypeEntiy.getID();
                    priceType = htlPriceTypeEntiy.getID().toString();
                }
            }

            // 查询出预定条款的相关信息；
            htlPreconcertItemZ = clauseTemplateByDateManage.findHtlPreconItemInfo(hotelId, price,
                validDt);

        } else {// 在按日期调整预定条款页面，进行查询操作

            if (null != validDate && !"".equals(validDate.toString())) {
                validDt = DateUtil.dateToString(validDate);// 如果不是则取他选择的日期
            }
            // 根据日期重新查询房型
            lstRoomType.clear();
            lstRoomType = clauseTemplateByDateManage.findRoomTypeLis(hotelId, validDt);
            // 如果为空就跳转到错误提示页面
            if (lstRoomType.isEmpty()) {
                return "clauseError";
            }

            if (null == priceTypeID || ("").equals(priceTypeID.toString())) {

                // 重新选择日期，价格类型 列表重新赋值，同事赋个默认值
                HtlRoomtype htlRoomtypeEntiy = lstRoomType.get(0);
                if (null != htlRoomtypeEntiy) {
                    List priceTypeLis = htlRoomtypeEntiy.getLstPriceType();
                    if (!priceTypeLis.isEmpty()) {
                        HtlPriceType htlPriceTypeEntiy = (HtlPriceType) priceTypeLis.get(0);
                        price = htlPriceTypeEntiy.getID();
                        priceTypeID = htlPriceTypeEntiy.getID();
                    }
                }

            }
            htlPreconcertItemZ = clauseTemplateByDateManage.findHtlPreconItemInfo(hotelId,
                priceTypeID, validDt);

        }

        // 本部：当一段时间内有的日期绑定有的日期不绑定时，按日期调整预订条款查询后报错 的bug修复 add by shengwei.zuo 2009-05-22 hotel2.6 end

        if (null != htlPreconcertItemZ) {

            htlReservationList = htlPreconcertItemZ.getHtlReservationList();
            if (null != htlReservationList && 0 != htlReservationList.size()) {
                if (0 < htlReservationList.size()) {

                    lisHtlReservacont = htlReservationList.get(0).getHtlReservacontList();

                    dateNum = lisHtlReservacont.size();

                    for (int i = 0; i < lisHtlReservacont.size(); i++) {

                        HtlReservCont htlRC = lisHtlReservacont.get(i);

                        MustDate mDate = new MustDate();

                        mDate.setContinueDate(htlRC.getContinueDate());
                        mDate.setContinueEndDate(htlRC.getContinueEndDate());
                        mDate.setWeeks(htlRC.getWeeks());

                        lsmustDate.add(mDate);

                    }

                }
            }

            htlAssureList = htlPreconcertItemZ.getHtlAssureList();

            if (!htlAssureList.isEmpty()) {

                lisHtlAssureItemEveryday = htlAssureList.get(0).getHtlAssureItemEverydayList();

                for (int i = 0; i < lisHtlAssureItemEveryday.size(); i++) {

                    HtlAssureItemEveryday htlAssEvday = lisHtlAssureItemEveryday.get(i);

                    String assEvdayType = htlAssEvday.getType();

                    if (assEvdayType.equals(ClaueType.ASSURE_TYPE_ONE)
                        || ClaueType.ASSURE_TYPE_ONE.equals(assEvdayType)) {

                        HtlAssureItemTemplateOne htlAssTplOne = new HtlAssureItemTemplateOne();

                        htlAssTplOne.setScopeOne(htlAssEvday.getScope());
                        htlAssTplOne.setDeductTypeOne(htlAssEvday.getDeductType());
                        htlAssTplOne.setPercentageOne(htlAssEvday.getPercentage());

                        lisHtlAssureItemTemplateOne.add(htlAssTplOne);

                    } else if (assEvdayType.equals(ClaueType.ASSURE_TYPE_TWO)
                        || ClaueType.ASSURE_TYPE_TWO.equals(assEvdayType)) {

                        HtlAssureItemTemplateTwo htlAssTplTwo = new HtlAssureItemTemplateTwo();

                        htlAssTplTwo.setScopeTwo(htlAssEvday.getScope());
                        htlAssTplTwo.setDeductTypeTwo(htlAssEvday.getDeductType());
                        htlAssTplTwo.setPercentageTwo(htlAssEvday.getPercentage());
                        htlAssTplTwo.setFirstDateOrDaysTwo(htlAssEvday.getFirstDateOrDays());
                        htlAssTplTwo.setFirstTimeTwo(htlAssEvday.getFirstTime());
                        htlAssTplTwo.setSecondDateOrDaysTwo(htlAssEvday.getSecondDateOrDays());
                        htlAssTplTwo.setSecondTimeTwo(htlAssEvday.getSecondTime());

                        lisHtlAssureItemTemplateTwo.add(htlAssTplTwo);

                    } else if (ClaueType.ASSURE_TYPE_THREE.equals(assEvdayType)
                        || assEvdayType.equals(ClaueType.ASSURE_TYPE_THREE)) {

                        HtlAssureItemTemplateThree htlAssTplThree =
                            new HtlAssureItemTemplateThree();

                        htlAssTplThree.setScopeThree(htlAssEvday.getScope());
                        htlAssTplThree.setDeductTypeThree(htlAssEvday.getDeductType());
                        htlAssTplThree.setPercentageThree(htlAssEvday.getPercentage());
                        htlAssTplThree.setFirstDateOrDaysThree(htlAssEvday.getFirstDateOrDays());
                        htlAssTplThree.setFirstTimeThree(htlAssEvday.getFirstTime());
                        htlAssTplThree.setSecondTimeThree(htlAssEvday.getSecondTime());

                        lisHtlAssureItemTemplateThree.add(htlAssTplThree);

                    } else if (ClaueType.ASSURE_TYPE_FORV.equals(assEvdayType)
                        || assEvdayType.equals(ClaueType.ASSURE_TYPE_FORV)) {

                        HtlAssureItemTemplateFour htlAssTplFour = new HtlAssureItemTemplateFour();

                        htlAssTplFour.setScopeFour(htlAssEvday.getScope());
                        htlAssTplFour.setDeductTypeFour(htlAssEvday.getDeductType());
                        htlAssTplFour.setPercentageFour(htlAssEvday.getPercentage());
                        htlAssTplFour.setFirstDateOrDaysFour(htlAssEvday.getFirstDateOrDays());
                        htlAssTplFour.setFirstTimeFour(htlAssEvday.getFirstTime());
                        htlAssTplFour.setSecondDateOrDaysFour(htlAssEvday.getSecondDateOrDays());
                        htlAssTplFour.setSecondTimeFour(htlAssEvday.getSecondTime());

                        lisHtlAssureItemTemplateFour.add(htlAssTplFour);

                    } else if (ClaueType.ASSURE_TYPE_FIVE.equals(assEvdayType)
                        || assEvdayType.equals(ClaueType.ASSURE_TYPE_FIVE)) {

                        HtlAssureItemTemplateFive htlAssTplFive = new HtlAssureItemTemplateFive();

                        htlAssTplFive.setScopeFive(htlAssEvday.getScope());
                        htlAssTplFive.setDeductTypeFive(htlAssEvday.getDeductType());
                        htlAssTplFive.setPercentageFive(htlAssEvday.getPercentage());
                        htlAssTplFive.setBeforeOrAfterFive(htlAssEvday.getBeforeOrAfter());
                        htlAssTplFive.setFirstTimeFive(htlAssEvday.getFirstTime());

                        lisHtlAssureItemTemplateFive.add(htlAssTplFive);

                    }

                }

                assuerOne = lisHtlAssureItemTemplateOne.size();
                assuerTwo = lisHtlAssureItemTemplateTwo.size();
                assuerThree = lisHtlAssureItemTemplateThree.size();
                assuerFour = lisHtlAssureItemTemplateFour.size();
                assuerFive = lisHtlAssureItemTemplateFive.size();

            }

            htlPrepayEverydayList = htlPreconcertItemZ.getHtlPrepayEverydayList();

            if (!htlPrepayEverydayList.isEmpty()) {

                lisHtlPrepayItemEveryday = htlPrepayEverydayList.get(0)
                    .getHtlPrepayItemEverydayList();

                for (int i = 0; i < lisHtlPrepayItemEveryday.size(); i++) {

                    HtlPrepayItemEveryday htlPreEvday = lisHtlPrepayItemEveryday.get(i);

                    String preEvdayType = htlPreEvday.getType();

                    if (ClaueType.PREPAY_TYPE_ONE.equals(preEvdayType)
                        || preEvdayType.equals(ClaueType.PREPAY_TYPE_ONE)) {

                        HtlPrepayItemTemplateOne htlpltpOne = new HtlPrepayItemTemplateOne();

                        htlpltpOne.setScopePPOne(htlPreEvday.getScope());
                        htlpltpOne.setDeductTypePPOne(htlPreEvday.getDeductType());
                        htlpltpOne.setPercentagePPOne(htlPreEvday.getPercentage());

                        lisHtlPrepayItemTemplateOne.add(htlpltpOne);

                    } else if (ClaueType.PREPAY_TYPE_TWO.equals(preEvdayType)
                        || preEvdayType.equals(ClaueType.PREPAY_TYPE_TWO)) {

                        HtlPrepayItemTemplateTwo htlpltpTwo = new HtlPrepayItemTemplateTwo();

                        htlpltpTwo.setScopePPTwo(htlPreEvday.getScope());
                        htlpltpTwo.setDeductTypePPTwo(htlPreEvday.getDeductType());
                        htlpltpTwo.setPercentagePPTwo(htlPreEvday.getPercentage());
                        htlpltpTwo.setFirstDateOrDaysPPTwo(htlPreEvday.getFirstDateOrDays());
                        htlpltpTwo.setFirstTimePPTwo(htlPreEvday.getFirstTime());
                        htlpltpTwo.setSecondDateOrDaysPPTwo(htlPreEvday.getSecondDateOrDays());
                        htlpltpTwo.setSecondTimePPTwo(htlPreEvday.getSecondTime());

                        lisHtlPrepayItemTemplateTwo.add(htlpltpTwo);

                    } else if (ClaueType.PREPAY_TYPE_THREE.equals(preEvdayType)
                        || preEvdayType.equals(ClaueType.PREPAY_TYPE_THREE)) {

                        HtlPrepayItemTemplateThree htlpltpThree = new HtlPrepayItemTemplateThree();

                        htlpltpThree.setScopePPThree(htlPreEvday.getScope());
                        htlpltpThree.setDeductTypePPThree(htlPreEvday.getDeductType());
                        htlpltpThree.setPercentagePPThree(htlPreEvday.getPercentage());
                        htlpltpThree.setFirstDateOrDaysPPThree(htlPreEvday.getFirstDateOrDays());
                        htlpltpThree.setFirstTimePPThree(htlPreEvday.getFirstTime());
                        htlpltpThree.setSecondTimePPThree(htlPreEvday.getSecondTime());

                        lisHtlPrepayItemTemplateThree.add(htlpltpThree);

                    } else if (ClaueType.PREPAY_TYPE_FORV.equals(preEvdayType)
                        || preEvdayType.equals(ClaueType.PREPAY_TYPE_FORV)) {

                        HtlPrepayItemTemplateFour htlpltpFour = new HtlPrepayItemTemplateFour();

                        htlpltpFour.setScopePPFour(htlPreEvday.getScope());
                        htlpltpFour.setDeductTypePPFour(htlPreEvday.getDeductType());
                        htlpltpFour.setPercentagePPFour(htlPreEvday.getPercentage());
                        htlpltpFour.setFirstDateOrDaysPPFour(htlPreEvday.getFirstDateOrDays());
                        htlpltpFour.setFirstTimePPFour(htlPreEvday.getFirstTime());
                        htlpltpFour.setSecondDateOrDaysPPFour(htlPreEvday.getSecondDateOrDays());
                        htlpltpFour.setSecondTimePPFour(htlPreEvday.getSecondTime());

                        lisHtlPrepayItemTemplateFour.add(htlpltpFour);

                    } else if (ClaueType.PREPAY_TYPE_FIVE.equals(preEvdayType)
                        || preEvdayType.equals(ClaueType.PREPAY_TYPE_FIVE)) {

                        HtlPrepayItemTemplateFive htlpltpFive = new HtlPrepayItemTemplateFive();

                        htlpltpFive.setScopePPFive(htlPreEvday.getScope());
                        htlpltpFive.setDeductTypePPFive(htlPreEvday.getDeductType());
                        htlpltpFive.setPercentagePPFive(htlPreEvday.getPercentage());
                        htlpltpFive.setFirstTimePPFive(htlPreEvday.getFirstTime());
                        htlpltpFive.setBeforeOrAfterPPFive(htlPreEvday.getBeforeOrAfter());

                        lisHtlPrepayItemTemplateFive.add(htlpltpFive);

                    }

                }

                prepayOne = lisHtlPrepayItemTemplateOne.size();
                prepayTwo = lisHtlPrepayItemTemplateTwo.size();
                prepayThree = lisHtlPrepayItemTemplateThree.size();
                prepayFour = lisHtlPrepayItemTemplateFour.size();
                prepayFive = lisHtlPrepayItemTemplateFive.size();

            }

        }
        return "viewClTpByDate";
    }

    /*
     * add by shengwei.zuo 2009-02-08 点击预览
     */
    public String reviewClauseEveryInfo() {

        // 将表单数据存入到实体中。
        super.setEntityID(ID);
        super.setEntity(super.populateEntity());
        this.setHtlPreconcertItemZ((HtlPreconcertItem) this.getEntity());

        Map params = super.getParams();

        /*
         * 设置添加时状态标识的值
         */
        htlPreconcertItemZ = clauseTemplateByDateManage.findHtlPreconItemInfo(hotelId, priceTypeId,
            validDt);
        if (null == htlPreconcertItemZ) {
            htlPreconcertItemZ = new HtlPreconcertItem();
        }
        if (null != priceTypeId && !"".equals(priceTypeId.toString())) {

            htlPreconcertItemZ.setPriceTypeId(priceTypeId);
        } else {
            htlPreconcertItemZ.setPriceTypeId(priceTypeID);

        }
        htlPreconcertItemZ.setActive(BizRuleCheck.getTrueString());
        htlPreconcertItemZ.setHotelId(hotelId);
        htlPreconcertItemZ.setValidDate(DateUtil.getDate(validDt));
        htlPreconcertItemZ.setAttention(attention);

        /*
         * 预订条款
         */

        HtlReservation htlReservat = new HtlReservation();
        htlReservat.setAheadDay(aheadDay);
        htlReservat.setAheadTime(aheadTime);
        htlReservat.setContinueNights(continueNights);
        htlReservat.setMinRestrictNights(minRestrictNights);
        htlReservat.setMaxRestrictNights(maxRestrictNights);
        htlReservat.setContinueDatesRelation(continueDatesRelation);
        htlReservat.setMustAheadDate(mustAheadDate);
        
        /**
         * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 begin
         */
        htlReservat.setMustFromDate(mustFromDate);
        htlReservat.setMustToDate(mustToDate);
        htlReservat.setMustFromTime(mustFromTime);
        htlReservat.setMustToTime(mustToTime);
        /**
         * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 end
         */
        
        htlReservat.setMustAheadTime(mustAheadTime);
        htlReservat.setHtlPreconcertItem(htlPreconcertItemZ);

        // 设置预订必住日期信息--add by shengwei.zuo 2009-02-12

        lsmustDate = MyBeanUtil.getBatchObjectFromParam(params, MustDate.class, dateNum);

        for (int i = 0; i < lsmustDate.size(); i++) {
            MustDate mustDate = (MustDate) lsmustDate.get(i);
            HtlReservCont htlRcont = new HtlReservCont();
            htlRcont.setContinueDate(mustDate.getContinueDate());
            if (null != mustDate.getContinueEndDate() 
                && StringUtil.isValidStr(mustDate.getWeeks())) {
                htlRcont.setContinueEndDate(mustDate.getContinueEndDate());
                htlRcont.setWeeks(mustDate.getWeeks());
            }
            lisHtlReservacont.add(htlRcont);

        }

        htlReservat.setHtlReservacontList(lisHtlReservacont);

        htlReservationList.add(htlReservat);

        /*
         * 担保条款模板
         */

        HtlAssure htlAssure = new HtlAssure();

        htlAssure.setAssureLetter(assureLetter);
        htlAssure.setAssureType(assureType);
        htlAssure.setIsNotConditional(isNotConditional);
        htlAssure.setLatestAssureTime(latestAssureTime);
        htlAssure.setOverRoomNumber(overRoomNumber);
        htlAssure.setOverNightsNumber(overNightsNumber);
        htlAssure.setHtlPreconcertItem(htlPreconcertItemZ);

        // 第一种--取消和修改担保条款信息添加

        lisHtlAssureItemTemplateOne = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateOne.class, assuerOne);

        // lisHtlAssureItemTemplateOne=CEntityEvent.setCEntity(lisHtlAssureItemTemplateOne,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        for (int i = 0; i < lisHtlAssureItemTemplateOne.size(); i++) {

            HtlAssureItemEveryday htlAssureItemEverydayEntiyOe = new HtlAssureItemEveryday();

            HtlAssureItemTemplateOne htlAssureItemTemplateOne = lisHtlAssureItemTemplateOne.get(i);

            htlAssureItemEverydayEntiyOe.setScope(htlAssureItemTemplateOne.getScopeOne());
            htlAssureItemEverydayEntiyOe.setDeductType(htlAssureItemTemplateOne.getDeductTypeOne());
            htlAssureItemEverydayEntiyOe.setPercentage(htlAssureItemTemplateOne.getPercentageOne());
            htlAssureItemEverydayEntiyOe.setType(ClaueType.ASSURE_TYPE_ONE);

            // htlAssureItemEverydayEntiyOe.setHtlAssure(htlAssure);

            lisHtlAssureItemEveryday.add(htlAssureItemEverydayEntiyOe);

        }

        // 第二种--取消和修改担保条款信息添加
        lisHtlAssureItemTemplateTwo = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateTwo.class, assuerTwo);
        // lisHtlAssureItemTemplateTwo=CEntityEvent.setCEntity(lisHtlAssureItemTemplateTwo,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        for (int i = 0; i < lisHtlAssureItemTemplateTwo.size(); i++) {

            HtlAssureItemEveryday htlAssureItemEverydayEntiyTw = new HtlAssureItemEveryday();

            HtlAssureItemTemplateTwo htlAssureItemTemplateTwo = lisHtlAssureItemTemplateTwo.get(i);

            htlAssureItemEverydayEntiyTw.setDeductType(htlAssureItemTemplateTwo.getDeductTypeTwo());
            htlAssureItemEverydayEntiyTw.setScope(htlAssureItemTemplateTwo.getScopeTwo());
            htlAssureItemEverydayEntiyTw.setFirstDateOrDays(htlAssureItemTemplateTwo
                .getFirstDateOrDaysTwo());
            htlAssureItemEverydayEntiyTw.setFirstTime(htlAssureItemTemplateTwo.getFirstTimeTwo());
            htlAssureItemEverydayEntiyTw.setSecondDateOrDays(htlAssureItemTemplateTwo
                .getSecondDateOrDaysTwo());
            htlAssureItemEverydayEntiyTw.setSecondTime(htlAssureItemTemplateTwo.getSecondTimeTwo());
            htlAssureItemEverydayEntiyTw.setPercentage(htlAssureItemTemplateTwo.getPercentageTwo());
            htlAssureItemEverydayEntiyTw.setType(ClaueType.ASSURE_TYPE_TWO);

            // htlAssureItemEverydayEntiyTw.setHtlAssure(htlAssure);

            lisHtlAssureItemEveryday.add(htlAssureItemEverydayEntiyTw);

        }

        // 第三种--取消和修改担保条款信息添加
        lisHtlAssureItemTemplateThree = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateThree.class, assuerThree);
        // lisHtlAssureItemTemplateThree=CEntityEvent.setCEntity(lisHtlAssureItemTemplateThree,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        for (int i = 0; i < lisHtlAssureItemTemplateThree.size(); i++) {

            HtlAssureItemEveryday htlAssureItemEverydayEntiyTr = new HtlAssureItemEveryday();

            HtlAssureItemTemplateThree htlAssureItemTemplateThree = 
                lisHtlAssureItemTemplateThree
                .get(i);

            htlAssureItemEverydayEntiyTr.setDeductType(htlAssureItemTemplateThree
                .getDeductTypeThree());
            htlAssureItemEverydayEntiyTr.setScope(htlAssureItemTemplateThree.getScopeThree());
            htlAssureItemEverydayEntiyTr.setFirstDateOrDays(htlAssureItemTemplateThree
                .getFirstDateOrDaysThree());
            htlAssureItemEverydayEntiyTr.setFirstTime(htlAssureItemTemplateThree
                .getFirstTimeThree());
            htlAssureItemEverydayEntiyTr.setSecondTime(htlAssureItemTemplateThree
                .getSecondTimeThree());
            htlAssureItemEverydayEntiyTr.setPercentage(htlAssureItemTemplateThree
                .getPercentageThree());
            htlAssureItemEverydayEntiyTr.setType(ClaueType.ASSURE_TYPE_THREE);

            // htlAssureItemEverydayEntiyTr.setHtlAssure(htlAssure);

            lisHtlAssureItemEveryday.add(htlAssureItemEverydayEntiyTr);

        }

        // 第四种--取消和修改担保条款信息添加
        lisHtlAssureItemTemplateFour = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateFour.class, assuerFour);
        // lisHtlAssureItemTemplateFour=CEntityEvent.setCEntity(lisHtlAssureItemTemplateFour,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        for (int i = 0; i < lisHtlAssureItemTemplateFour.size(); i++) {

            HtlAssureItemEveryday htlAssureItemEverydayEntiyFu = new HtlAssureItemEveryday();

            HtlAssureItemTemplateFour htlAssureItemTemplateFour = lisHtlAssureItemTemplateFour
                .get(i);

            htlAssureItemEverydayEntiyFu.setDeductType(htlAssureItemTemplateFour
                .getDeductTypeFour());
            htlAssureItemEverydayEntiyFu.setScope(htlAssureItemTemplateFour.getScopeFour());
            htlAssureItemEverydayEntiyFu.setFirstDateOrDays(htlAssureItemTemplateFour
                .getFirstDateOrDaysFour());
            htlAssureItemEverydayEntiyFu.setFirstTime(htlAssureItemTemplateFour.getFirstTimeFour());
            htlAssureItemEverydayEntiyFu.setSecondDateOrDays(htlAssureItemTemplateFour
                .getSecondDateOrDaysFour());
            htlAssureItemEverydayEntiyFu.setSecondTime(htlAssureItemTemplateFour
                .getSecondTimeFour());
            htlAssureItemEverydayEntiyFu.setPercentage(htlAssureItemTemplateFour
                .getPercentageFour());
            htlAssureItemEverydayEntiyFu.setType(ClaueType.ASSURE_TYPE_FORV);

            // htlAssureItemEverydayEntiyFu.setHtlAssure(htlAssure);

            lisHtlAssureItemEveryday.add(htlAssureItemEverydayEntiyFu);

        }

        // 第五种--取消和修改担保条款信息添加
        lisHtlAssureItemTemplateFive = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateFive.class, assuerFive);
        // lisHtlAssureItemTemplateFive=CEntityEvent.setCEntity(lisHtlAssureItemTemplateFive,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        for (int i = 0; i < lisHtlAssureItemTemplateFive.size(); i++) {

            HtlAssureItemEveryday htlAssureItemEverydayEntiyFv = new HtlAssureItemEveryday();

            HtlAssureItemTemplateFive htlAssureItemTemplateFive = lisHtlAssureItemTemplateFive
                .get(i);

            htlAssureItemEverydayEntiyFv.setDeductType(htlAssureItemTemplateFive
                .getDeductTypeFive());
            htlAssureItemEverydayEntiyFv.setScope(htlAssureItemTemplateFive.getScopeFive());
            htlAssureItemEverydayEntiyFv.setBeforeOrAfter(htlAssureItemTemplateFive
                .getBeforeOrAfterFive());
            htlAssureItemEverydayEntiyFv.setFirstTime(htlAssureItemTemplateFive.getFirstTimeFive());
            htlAssureItemEverydayEntiyFv.setPercentage(htlAssureItemTemplateFive
                .getPercentageFive());
            htlAssureItemEverydayEntiyFv.setType(ClaueType.ASSURE_TYPE_FIVE);

            // htlAssureItemEverydayEntiyFv.setHtlAssure(htlAssure);

            lisHtlAssureItemEveryday.add(htlAssureItemEverydayEntiyFv);

        }

        htlAssure.setHtlAssureItemEverydayList(lisHtlAssureItemEveryday);

        htlAssureList.add(htlAssure);

        /*
         * 预付条款模板
         */

        HtlPrepayEveryday htlPrepayEvery = new HtlPrepayEveryday();

        htlPrepayEvery.setAmountType(amountType);
        htlPrepayEvery.setBalanceType(balanceType);
        htlPrepayEvery.setDaysAfterConfirm(daysAfterConfirm);
        htlPrepayEvery.setLimitAheadDays(limitAheadDays);
        htlPrepayEvery.setLimitAheadDaysTime(limitAheadDaysTime);
        htlPrepayEvery.setLimitDate(limitDate);
        htlPrepayEvery.setLimitTime(limitTime);
        htlPrepayEvery.setTimeLimitType(timeLimitType);
        htlPrepayEvery.setPrepayDeductType(prepayDeductType);
        htlPrepayEvery.setPaymentType(paymentType);
        htlPrepayEvery.setTimeAfterConfirm(timeAfterConfirm);

        htlPrepayEvery.setHtlPreconcertItem(htlPreconcertItemZ);

        // 第一种--取消和修改预付条款种类
        lisHtlPrepayItemTemplateOne = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateOne.class, prepayOne);
        // lisHtlPrepayItemTemplateOne=CEntityEvent.setCEntity(lisHtlPrepayItemTemplateOne,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        for (int i = 0; i < lisHtlPrepayItemTemplateOne.size(); i++) {

            HtlPrepayItemEveryday htlPrepayItemEverydayEntiyOe = new HtlPrepayItemEveryday();

            HtlPrepayItemTemplateOne htlPrepayItemTemplateOne = lisHtlPrepayItemTemplateOne.get(i);

            htlPrepayItemEverydayEntiyOe.setScope(htlPrepayItemTemplateOne.getScopePPOne());
            htlPrepayItemEverydayEntiyOe.setDeductType(htlPrepayItemTemplateOne
                .getDeductTypePPOne());
            htlPrepayItemEverydayEntiyOe.setPercentage(htlPrepayItemTemplateOne
                .getPercentagePPOne());
            htlPrepayItemEverydayEntiyOe.setType(ClaueType.PREPAY_TYPE_ONE);
            // htlPrepayItemEverydayEntiyOe.setHtlPrepayEveryday(htlPrepayEvery);

            lisHtlPrepayItemEveryday.add(htlPrepayItemEverydayEntiyOe);

        }

        // 第二种--取消和修改预付条款种类
        lisHtlPrepayItemTemplateTwo = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateTwo.class, prepayTwo);
        // lisHtlPrepayItemTemplateTwo=CEntityEvent.setCEntity(lisHtlPrepayItemTemplateTwo,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        if (0 < lisHtlPrepayItemTemplateTwo.size()) {

            for (int i = 0; i < lisHtlPrepayItemTemplateTwo.size(); i++) {

                HtlPrepayItemEveryday htlPrepayItemEverydayEntiyTw = new HtlPrepayItemEveryday();

                HtlPrepayItemTemplateTwo htlPrepayItemTemplateTwo = lisHtlPrepayItemTemplateTwo
                    .get(i);

                htlPrepayItemEverydayEntiyTw.setDeductType(htlPrepayItemTemplateTwo
                    .getDeductTypePPTwo());
                htlPrepayItemEverydayEntiyTw.setScope(htlPrepayItemTemplateTwo.getScopePPTwo());
                htlPrepayItemEverydayEntiyTw.setFirstDateOrDays(htlPrepayItemTemplateTwo
                    .getFirstDateOrDaysPPTwo());
                htlPrepayItemEverydayEntiyTw.setFirstTime(htlPrepayItemTemplateTwo
                    .getFirstTimePPTwo());
                htlPrepayItemEverydayEntiyTw.setSecondDateOrDays(htlPrepayItemTemplateTwo
                    .getSecondDateOrDaysPPTwo());
                htlPrepayItemEverydayEntiyTw.setSecondTime(htlPrepayItemTemplateTwo
                    .getSecondTimePPTwo());
                htlPrepayItemEverydayEntiyTw.setPercentage(htlPrepayItemTemplateTwo
                    .getPercentagePPTwo());
                htlPrepayItemEverydayEntiyTw.setType(ClaueType.PREPAY_TYPE_TWO);
                // htlPrepayItemEverydayEntiyTw.setHtlPrepayEveryday(htlPrepayEvery);

                lisHtlPrepayItemEveryday.add(htlPrepayItemEverydayEntiyTw);

            }
        }
        // 第三种--取消和修改预付条款种类

        lisHtlPrepayItemTemplateThree = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateThree.class, prepayThree);
        // lisHtlPrepayItemTemplateThree=CEntityEvent.setCEntity(lisHtlPrepayItemTemplateThree,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        if (0 < lisHtlPrepayItemTemplateThree.size()) {

            for (int i = 0; i < lisHtlPrepayItemTemplateThree.size(); i++) {

                HtlPrepayItemEveryday htlPrepayItemEverydayEntiyTr = new HtlPrepayItemEveryday();

                HtlPrepayItemTemplateThree htlPrepayItemTemplateThree = 
                    lisHtlPrepayItemTemplateThree
                    .get(i);

                htlPrepayItemEverydayEntiyTr.setDeductType(htlPrepayItemTemplateThree
                    .getDeductTypePPThree());
                htlPrepayItemEverydayEntiyTr.setScope(htlPrepayItemTemplateThree.getScopePPThree());
                htlPrepayItemEverydayEntiyTr.setFirstDateOrDays(htlPrepayItemTemplateThree
                    .getFirstDateOrDaysPPThree());
                htlPrepayItemEverydayEntiyTr.setFirstTime(htlPrepayItemTemplateThree
                    .getFirstTimePPThree());
                htlPrepayItemEverydayEntiyTr.setSecondTime(htlPrepayItemTemplateThree
                    .getSecondTimePPThree());
                htlPrepayItemEverydayEntiyTr.setPercentage(htlPrepayItemTemplateThree
                    .getPercentagePPThree());
                htlPrepayItemEverydayEntiyTr.setType(ClaueType.PREPAY_TYPE_THREE);
                // htlPrepayItemEverydayEntiyTr.setHtlPrepayEveryday(htlPrepayEvery);

                lisHtlPrepayItemEveryday.add(htlPrepayItemEverydayEntiyTr);

            }

        }
        // 第四种--取消和修改预付条款种类

        lisHtlPrepayItemTemplateFour = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateFour.class, prepayFour);
        // lisHtlPrepayItemTemplateFour=CEntityEvent.setCEntity(lisHtlPrepayItemTemplateFour,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        if (0 < lisHtlPrepayItemTemplateFour.size()) {

            for (int i = 0; i < lisHtlPrepayItemTemplateFour.size(); i++) {

                HtlPrepayItemEveryday htlPrepayItemEverydayEntiyFu = new HtlPrepayItemEveryday();

                HtlPrepayItemTemplateFour htlPrepayItemTemplateFour = lisHtlPrepayItemTemplateFour
                    .get(i);

                htlPrepayItemEverydayEntiyFu.setDeductType(htlPrepayItemTemplateFour
                    .getDeductTypePPFour());
                htlPrepayItemEverydayEntiyFu.setScope(htlPrepayItemTemplateFour.getScopePPFour());
                htlPrepayItemEverydayEntiyFu.setFirstDateOrDays(htlPrepayItemTemplateFour
                    .getFirstDateOrDaysPPFour());
                htlPrepayItemEverydayEntiyFu.setFirstTime(htlPrepayItemTemplateFour
                    .getFirstTimePPFour());
                htlPrepayItemEverydayEntiyFu.setSecondDateOrDays(htlPrepayItemTemplateFour
                    .getSecondDateOrDaysPPFour());
                htlPrepayItemEverydayEntiyFu.setSecondTime(htlPrepayItemTemplateFour
                    .getSecondTimePPFour());
                htlPrepayItemEverydayEntiyFu.setPercentage(htlPrepayItemTemplateFour
                    .getPercentagePPFour());
                htlPrepayItemEverydayEntiyFu.setType(ClaueType.PREPAY_TYPE_FORV);
                // htlPrepayItemEverydayEntiyFu.setHtlPrepayEveryday(htlPrepayEvery);

                lisHtlPrepayItemEveryday.add(htlPrepayItemEverydayEntiyFu);

            }
        }

        // 第五种--取消和修改预付条款种类
        lisHtlPrepayItemTemplateFive = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateFive.class, prepayFive);
        // lisHtlPrepayItemTemplateFive=CEntityEvent.setCEntity(lisHtlPrepayItemTemplateFive,
        // super.getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

        if (0 < lisHtlPrepayItemTemplateFive.size()) {

            for (int i = 0; i < lisHtlPrepayItemTemplateFive.size(); i++) {

                HtlPrepayItemEveryday htlPrepayItemEverydayEntiyFv = new HtlPrepayItemEveryday();

                HtlPrepayItemTemplateFive htlPrepayItemTemplateFive = lisHtlPrepayItemTemplateFive
                    .get(i);

                htlPrepayItemEverydayEntiyFv.setDeductType(htlPrepayItemTemplateFive
                    .getDeductTypePPFive());
                htlPrepayItemEverydayEntiyFv.setScope(htlPrepayItemTemplateFive.getScopePPFive());
                htlPrepayItemEverydayEntiyFv.setBeforeOrAfter(htlPrepayItemTemplateFive
                    .getBeforeOrAfterPPFive());
                htlPrepayItemEverydayEntiyFv.setFirstTime(htlPrepayItemTemplateFive
                    .getFirstTimePPFive());
                htlPrepayItemEverydayEntiyFv.setPercentage(htlPrepayItemTemplateFive
                    .getPercentagePPFive());
                htlPrepayItemEverydayEntiyFv.setType(ClaueType.PREPAY_TYPE_FIVE);
                // htlPrepayItemEverydayEntiyFv.setHtlPrepayEveryday(htlPrepayEvery);

                lisHtlPrepayItemEveryday.add(htlPrepayItemEverydayEntiyFv);

            }
        }
        // 添加所有的取消和修改预付条款信息

        htlPrepayEvery.setHtlPrepayItemEverydayList(lisHtlPrepayItemEveryday);
        htlPrepayEverydayList.add(htlPrepayEvery);

        htlPreconcertItemZ.setHtlReservationList(htlReservationList);
        htlPreconcertItemZ.setHtlPrepayEverydayList(htlPrepayEverydayList);
        htlPreconcertItemZ.setHtlAssureList(htlAssureList);
        htlBinding = new HtlBinding();

        htlBinding.setBeginDt(beginDt);
        htlBinding.setEndDt(endDt);
        htlBinding.setValidDt(validDt);
        htlBinding.setPricetype(priceType);
        htlBinding.setPriceTypeID(priceTypeID);
        htlBinding.setRoomName(roomName);

        super.putSession("htlBinding", htlBinding);

        super.putSession("htlPreconcertItemZ", htlPreconcertItemZ);

        return "reviewClauseByDate";

    }

    // 点击保存操作 add shengwei.zuo 2009-04-16
    public String saveClauseEveryInfo() throws SQLException {

        htlPreconcertItemZ = (HtlPreconcertItem) super.getFromSession("htlPreconcertItemZ");

        htlBinding = (HtlBinding) super.getFromSession("htlBinding");

        htlPreconcertItemZ.setPriceTypeId(htlBinding.getPriceTypeID());

        htlPreconcertItemZ.setValidDate(DateUtil.getDate(htlBinding.getValidDt()));

        /*
         * 添加人的工号，ID，姓名和添加时间;
         */
        if (null != super.getOnlineRoleUser()) {

            if (null == htlPreconcertItemZ.getID() || 0 == htlPreconcertItemZ.getID()) {

                htlPreconcertItemZ.setCreateBy(super.getOnlineRoleUser().getName());
                htlPreconcertItemZ.setCreateById(super.getOnlineRoleUser().getLoginName());
                htlPreconcertItemZ.setCreateTime(DateUtil.getSystemDate());

            }

            htlPreconcertItemZ.setModifyBy(super.getOnlineRoleUser().getName());
            htlPreconcertItemZ.setModifyById(super.getOnlineRoleUser().getLoginName());
            htlPreconcertItemZ.setModifyTime(DateUtil.getSystemDate());

        } else {// 如果拿不到登陆用户信息,则提示重新登陆

            super.getSession().remove("htlBinding");
            super.getSession().remove("htlPreconcertItemZ");
            return super.forwardError("获取登陆用户信息失效,请重新登陆");
        }

        // 插入每天表之前,要调用hotelservice中的putRecord方法，用来对比修改了那些记录，保存起来，做操作日志用

        // HtlPreconcertItem htlPreconcertItem =
        // clauseTemplateByDateManage.findHtlPreconItemInfo(hotelId,priceTypeID,validDt);

        clauseTemplateByDateManage.savePreconcertItem(htlPreconcertItemZ);// 保存到每日表中去
        // hotelService.putRecord(htlPreconcertItemZ, htlPreconcertItem,htlPreconcertItemZ.getID());
        clauseTemplateByDateManage.proUpdateDate(htlPreconcertItemZ.getID(), hotelId, DateUtil
            .getDate(htlBinding.getValidDt()), htlBinding.getPriceTypeID());

        super.getSession().remove("htlBinding");
        super.getSession().remove("htlPreconcertItemZ");

        return "successl";

    }

    /**
     * 操作日志中查看明细 add by haibo.li
     * 
     * @return
     */
    public String queryModifRecord() {
        try {
            Recordlist = clauseTemplateByDateManage.queryRecord(RecordId);
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }

        return "recordFoword";
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDelBy() {
        return delBy;
    }

    public void setDelBy(String delBy) {
        this.delBy = delBy;
    }

    public String getDelById() {
        return delById;
    }

    public void setDelById(String delById) {
        this.delById = delById;
    }

    public Date getDelTime() {
        return delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public List<HtlAssure> getHtlAssureList() {
        return htlAssureList;
    }

    public void setHtlAssureList(List<HtlAssure> htlAssureList) {
        this.htlAssureList = htlAssureList;
    }

    public List<HtlPrepayEveryday> getHtlPrepayEverydayList() {
        return htlPrepayEverydayList;
    }

    public void setHtlPrepayEverydayList(List<HtlPrepayEveryday> htlPrepayEverydayList) {
        this.htlPrepayEverydayList = htlPrepayEverydayList;
    }

    public List<HtlReservation> getHtlReservationList() {
        return htlReservationList;
    }

    public void setHtlReservationList(List<HtlReservation> htlReservationList) {
        this.htlReservationList = htlReservationList;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyById() {
        return modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(String payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

    public Long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public Long getReservationTemplateId() {
        return reservationTemplateId;
    }

    public void setReservationTemplateId(Long reservationTemplateId) {
        this.reservationTemplateId = reservationTemplateId;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public HtlPreconcertItem getHtlPreconcertItemZ() {
        return htlPreconcertItemZ;
    }

    public void setHtlPreconcertItemZ(HtlPreconcertItem htlPreconcertItemZ) {
        this.htlPreconcertItemZ = htlPreconcertItemZ;
    }

    public ClauseTemplateByDateManage getClauseTemplateByDateManage() {
        return clauseTemplateByDateManage;
    }

    public void setClauseTemplateByDateManage
    (ClauseTemplateByDateManage clauseTemplateByDateManage) {
        this.clauseTemplateByDateManage = clauseTemplateByDateManage;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public String getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(String beginDt) {
        this.beginDt = beginDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public List<HtlReservCont> getLisHtlReservacont() {
        return lisHtlReservacont;
    }

    public void setLisHtlReservacont(List<HtlReservCont> lisHtlReservacont) {
        this.lisHtlReservacont = lisHtlReservacont;
    }

    public List<HtlAssureItemEveryday> getLisHtlAssureItemEveryday() {
        return lisHtlAssureItemEveryday;
    }

    public void setLisHtlAssureItemEveryday(List<HtlAssureItemEveryday> lisHtlAssureItemEveryday) {
        this.lisHtlAssureItemEveryday = lisHtlAssureItemEveryday;
    }

    public List<HtlAssureItemTemplateFive> getLisHtlAssureItemTemplateFive() {
        return lisHtlAssureItemTemplateFive;
    }

    public void setLisHtlAssureItemTemplateFive(
        List<HtlAssureItemTemplateFive> lisHtlAssureItemTemplateFive) {
        this.lisHtlAssureItemTemplateFive = lisHtlAssureItemTemplateFive;
    }

    public List<HtlAssureItemTemplateFour> getLisHtlAssureItemTemplateFour() {
        return lisHtlAssureItemTemplateFour;
    }

    public void setLisHtlAssureItemTemplateFour(
        List<HtlAssureItemTemplateFour> lisHtlAssureItemTemplateFour) {
        this.lisHtlAssureItemTemplateFour = lisHtlAssureItemTemplateFour;
    }

    public List<HtlAssureItemTemplateOne> getLisHtlAssureItemTemplateOne() {
        return lisHtlAssureItemTemplateOne;
    }

    public void setLisHtlAssureItemTemplateOne(
        List<HtlAssureItemTemplateOne> lisHtlAssureItemTemplateOne) {
        this.lisHtlAssureItemTemplateOne = lisHtlAssureItemTemplateOne;
    }

    public List<HtlAssureItemTemplateThree> getLisHtlAssureItemTemplateThree() {
        return lisHtlAssureItemTemplateThree;
    }

    public void setLisHtlAssureItemTemplateThree(
        List<HtlAssureItemTemplateThree> lisHtlAssureItemTemplateThree) {
        this.lisHtlAssureItemTemplateThree = lisHtlAssureItemTemplateThree;
    }

    public List<HtlAssureItemTemplateTwo> getLisHtlAssureItemTemplateTwo() {
        return lisHtlAssureItemTemplateTwo;
    }

    public void setLisHtlAssureItemTemplateTwo(
        List<HtlAssureItemTemplateTwo> lisHtlAssureItemTemplateTwo) {
        this.lisHtlAssureItemTemplateTwo = lisHtlAssureItemTemplateTwo;
    }

    public List<HtlPrepayItemTemplateFive> getLisHtlPrepayItemTemplateFive() {
        return lisHtlPrepayItemTemplateFive;
    }

    public void setLisHtlPrepayItemTemplateFive(
        List<HtlPrepayItemTemplateFive> lisHtlPrepayItemTemplateFive) {
        this.lisHtlPrepayItemTemplateFive = lisHtlPrepayItemTemplateFive;
    }

    public List<HtlPrepayItemTemplateFour> getLisHtlPrepayItemTemplateFour() {
        return lisHtlPrepayItemTemplateFour;
    }

    public void setLisHtlPrepayItemTemplateFour(
        List<HtlPrepayItemTemplateFour> lisHtlPrepayItemTemplateFour) {
        this.lisHtlPrepayItemTemplateFour = lisHtlPrepayItemTemplateFour;
    }

    public List<HtlPrepayItemTemplateOne> getLisHtlPrepayItemTemplateOne() {
        return lisHtlPrepayItemTemplateOne;
    }

    public void setLisHtlPrepayItemTemplateOne(
        List<HtlPrepayItemTemplateOne> lisHtlPrepayItemTemplateOne) {
        this.lisHtlPrepayItemTemplateOne = lisHtlPrepayItemTemplateOne;
    }

    public List<HtlPrepayItemTemplateThree> getLisHtlPrepayItemTemplateThree() {
        return lisHtlPrepayItemTemplateThree;
    }

    public void setLisHtlPrepayItemTemplateThree(
        List<HtlPrepayItemTemplateThree> lisHtlPrepayItemTemplateThree) {
        this.lisHtlPrepayItemTemplateThree = lisHtlPrepayItemTemplateThree;
    }

    public List<HtlPrepayItemTemplateTwo> getLisHtlPrepayItemTemplateTwo() {
        return lisHtlPrepayItemTemplateTwo;
    }

    public void setLisHtlPrepayItemTemplateTwo(
        List<HtlPrepayItemTemplateTwo> lisHtlPrepayItemTemplateTwo) {
        this.lisHtlPrepayItemTemplateTwo = lisHtlPrepayItemTemplateTwo;
    }

    public List<HtlPrepayItemEveryday> getLisHtlPrepayItemEveryday() {
        return lisHtlPrepayItemEveryday;
    }

    public void setLisHtlPrepayItemEveryday(List<HtlPrepayItemEveryday> lisHtlPrepayItemEveryday) {
        this.lisHtlPrepayItemEveryday = lisHtlPrepayItemEveryday;
    }

    public Long getAheadDay() {
        return aheadDay;
    }

    public void setAheadDay(Long aheadDay) {
        this.aheadDay = aheadDay;
    }

    public String getAheadTime() {
        return aheadTime;
    }

    public void setAheadTime(String aheadTime) {
        this.aheadTime = aheadTime;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getAssureLetter() {
        return assureLetter;
    }

    public void setAssureLetter(String assureLetter) {
        this.assureLetter = assureLetter;
    }

    public String getAssureType() {
        return assureType;
    }

    public void setAssureType(String assureType) {
        this.assureType = assureType;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public Long getContinueNights() {
        return continueNights;
    }

    public void setContinueNights(Long continueNights) {
        this.continueNights = continueNights;
    }

    public Long getDaysAfterConfirm() {
        return daysAfterConfirm;
    }

    public void setDaysAfterConfirm(Long daysAfterConfirm) {
        this.daysAfterConfirm = daysAfterConfirm;
    }

    public String getIsNotConditional() {
        return isNotConditional;
    }

    public void setIsNotConditional(String isNotConditional) {
        this.isNotConditional = isNotConditional;
    }

    public String getLatestAssureTime() {
        return latestAssureTime;
    }

    public void setLatestAssureTime(String latestAssureTime) {
        this.latestAssureTime = latestAssureTime;
    }

    public Long getLimitAheadDays() {
        return limitAheadDays;
    }

    public void setLimitAheadDays(Long limitAheadDays) {
        this.limitAheadDays = limitAheadDays;
    }

    public String getLimitAheadDaysTime() {
        return limitAheadDaysTime;
    }

    public void setLimitAheadDaysTime(String limitAheadDaysTime) {
        this.limitAheadDaysTime = limitAheadDaysTime;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    public Date getMustAheadDate() {
        return mustAheadDate;
    }

    public void setMustAheadDate(Date mustAheadDate) {
        this.mustAheadDate = mustAheadDate;
    }

    public String getMustAheadTime() {
        return mustAheadTime;
    }

    public void setMustAheadTime(String mustAheadTime) {
        this.mustAheadTime = mustAheadTime;
    }

    public Long getOverRoomNumber() {
        return overRoomNumber;
    }

    public void setOverRoomNumber(Long overRoomNumber) {
        this.overRoomNumber = overRoomNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTimeAfterConfirm() {
        return timeAfterConfirm;
    }

    public void setTimeAfterConfirm(String timeAfterConfirm) {
        this.timeAfterConfirm = timeAfterConfirm;
    }

    public int getAssuerFive() {
        return assuerFive;
    }

    public void setAssuerFive(int assuerFive) {
        this.assuerFive = assuerFive;
    }

    public int getAssuerFour() {
        return assuerFour;
    }

    public void setAssuerFour(int assuerFour) {
        this.assuerFour = assuerFour;
    }

    public int getAssuerOne() {
        return assuerOne;
    }

    public void setAssuerOne(int assuerOne) {
        this.assuerOne = assuerOne;
    }

    public int getAssuerThree() {
        return assuerThree;
    }

    public void setAssuerThree(int assuerThree) {
        this.assuerThree = assuerThree;
    }

    public int getAssuerTwo() {
        return assuerTwo;
    }

    public void setAssuerTwo(int assuerTwo) {
        this.assuerTwo = assuerTwo;
    }

    public List getLsmustDate() {
        return lsmustDate;
    }

    public void setLsmustDate(List lsmustDate) {
        this.lsmustDate = lsmustDate;
    }

    public int getPrepayFive() {
        return prepayFive;
    }

    public void setPrepayFive(int prepayFive) {
        this.prepayFive = prepayFive;
    }

    public int getPrepayFour() {
        return prepayFour;
    }

    public void setPrepayFour(int prepayFour) {
        this.prepayFour = prepayFour;
    }

    public int getPrepayOne() {
        return prepayOne;
    }

    public void setPrepayOne(int prepayOne) {
        this.prepayOne = prepayOne;
    }

    public int getPrepayThree() {
        return prepayThree;
    }

    public void setPrepayThree(int prepayThree) {
        this.prepayThree = prepayThree;
    }

    public int getPrepayTwo() {
        return prepayTwo;
    }

    public void setPrepayTwo(int prepayTwo) {
        this.prepayTwo = prepayTwo;
    }

    public int getDateNum() {
        return dateNum;
    }

    public void setDateNum(int dateNum) {
        this.dateNum = dateNum;
    }

    public String getIsFirstQuery() {
        return isFirstQuery;
    }

    public void setIsFirstQuery(String isFirstQuery) {
        this.isFirstQuery = isFirstQuery;
    }

    public String getValidDt() {
        return validDt;
    }

    public void setValidDt(String validDt) {
        this.validDt = validDt;
    }

    public RoomControlManage getRoomControlManage() {
        return roomControlManage;
    }

    public void setRoomControlManage(RoomControlManage roomControlManage) {
        this.roomControlManage = roomControlManage;
    }

    public String getTimeLimitType() {
        return timeLimitType;
    }

    public void setTimeLimitType(String timeLimitType) {
        this.timeLimitType = timeLimitType;
    }

    public String getPrepayDeductType() {
        return prepayDeductType;
    }

    public void setPrepayDeductType(String prepayDeductType) {
        this.prepayDeductType = prepayDeductType;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Long getPriceTypeID() {
        return priceTypeID;
    }

    public void setPriceTypeID(Long priceTypeID) {
        this.priceTypeID = priceTypeID;
    }

    public String getCurrentlyDt() {
        return currentlyDt;
    }

    public void setCurrentlyDt(String currentlyDt) {
        this.currentlyDt = currentlyDt;
    }

    public HtlBinding getHtlBinding() {
        return htlBinding;
    }

    public void setHtlBinding(HtlBinding htlBinding) {
        this.htlBinding = htlBinding;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public IHotelService getHotelService() {
        return hotelService;
    }

    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    public void setContractId(long contractId) {
        ContractId = contractId;
    }

    public long getContractId() {
        return ContractId;
    }

    public List<HtlRoomtype> getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List<HtlRoomtype> lstRoomType) {
        this.lstRoomType = lstRoomType;
    }

    public Long getMaxRestrictNights() {
        return maxRestrictNights;
    }

    public void setMaxRestrictNights(Long maxRestrictNights) {
        this.maxRestrictNights = maxRestrictNights;
    }

    public Long getMinRestrictNights() {
        return minRestrictNights;
    }

    public void setMinRestrictNights(Long minRestrictNights) {
        this.minRestrictNights = minRestrictNights;
    }

    public Long getOverNightsNumber() {
        return overNightsNumber;
    }

    public void setOverNightsNumber(Long overNightsNumber) {
        this.overNightsNumber = overNightsNumber;
    }

    public String getContinueDatesRelation() {
        return continueDatesRelation;
    }

    public void setContinueDatesRelation(String continueDatesRelation) {
        this.continueDatesRelation = continueDatesRelation;
    }

    public int getWeekDateNum() {
        return weekDateNum;
    }

    public void setWeekDateNum(int weekDateNum) {
        this.weekDateNum = weekDateNum;
    }

	public Date getMustFromDate() {
		return mustFromDate;
	}

	public void setMustFromDate(Date mustFromDate) {
		this.mustFromDate = mustFromDate;
	}

	public Date getMustToDate() {
		return mustToDate;
	}

	public void setMustToDate(Date mustToDate) {
		this.mustToDate = mustToDate;
	}

	public String getMustFromTime() {
		return mustFromTime;
	}

	public void setMustFromTime(String mustFromTime) {
		this.mustFromTime = mustFromTime;
	}

	public String getMustToTime() {
		return mustToTime;
	}

	public void setMustToTime(String mustToTime) {
		this.mustToTime = mustToTime;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}
}
