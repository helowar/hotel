package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.ClaueType;
import com.mangocity.hotel.base.manage.ClauseTemplateManage;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplate;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateFive;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateFour;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateOne;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateThree;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateTwo;
import com.mangocity.hotel.base.persistence.HtlAssureTemplate;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplate;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateFive;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateFour;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateOne;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateThree;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateTwo;
import com.mangocity.hotel.base.persistence.HtlPrepayTemplate;
import com.mangocity.hotel.base.persistence.HtlReservContTemplate;
import com.mangocity.hotel.base.persistence.HtlReservContWeekBean;
import com.mangocity.hotel.base.persistence.HtlReservationTemplate;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;



/**
 */
public class ClauseTemplateAction extends PersistenceAction {

    // 酒店预订担保预付条款模板ID
    private Long ID;

    // 预订条款模板名称
    private String reservationName;

    // 创建人工号
    private String createBy;

    // 创建人名称
    private String createByID;

    // 创建时间
    private Date createTime;

    // 修改人工号
    private String modifyBy;

    // 修改人名称
    private String modifyByID;

    // 修改时间
    private Date modifyTime;

    // 删除人工号
    private String delBy;

    // 删除人名称
    private String delByID;

    // 删除时间
    private Date delTime;

    // 有效标志
    private String Active;

    // 酒店ID;
    private long hotelId;

    /*
     * 酒店预订担保预付条款模板-实现类
     */
    private ClauseTemplateManage claluseTemplateManage;

    /*
     * 酒店预订担保预付条款模板-实体类
     */
    private HtlPreconcertItemTemplet htlPreconcertItemTempletZ;

    /*
     * 酒店预订模板实体
     */

    private HtlReservationTemplate htlReservationTemplate = new HtlReservationTemplate();

    /*
     * 酒店的担保模板实体
     */

    private HtlAssureTemplate htlAssureTemplate;

    /*
     * 酒店预付模板实体
     */

    private HtlPrepayTemplate htlPrepayTemplate;

    /*
     * 预订条款模板-实体类
     */
    private List<HtlReservationTemplate> htlReservationTemplateZ = 
        new ArrayList<HtlReservationTemplate>();

    /*
     * 担保条款模板-实体类
     */
    private List<HtlAssureTemplate> htlAssureTemplateZ =
        new ArrayList<HtlAssureTemplate>();

    /*
     * 预付条款模板-实体类
     */
    private List<HtlPrepayTemplate> htlPrepayTemplateZ = 
        new ArrayList<HtlPrepayTemplate>();

    /*
     * 
     * ************************************************预订条款模板 Begin
     */
    /*
     * 条款模板ID
     */
    private Long reservationTemplateId;

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
     * 
     * *********************************************预订条款模板 End
     */

    /*
     * 
     * $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$担保条款模板 begin
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

    /*
     * 
     * $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$担保条款模板 End
     */

    /*
     * &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&预付条款模板 begin
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
     * 付款时限确认后天数
     */
    private Long daysAfterConfirm;

    /*
     * 付款时限类型
     */
    private String timeLimitType;

    /*
     * 扣款百分比
     */
    private String prepayDeductType;

    /*
     * 付款时限确认后时间点
     */
    private String timeAfterConfirm;

    /*
     * &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&预付条款模板 end
     */

    /*
     * 预付取消和修改模板
     */
    private List<HtlPrepayItemTemplate> lisHtlPrepayItemTemplate = 
        new ArrayList<HtlPrepayItemTemplate>();

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

    /*
     * 必须连住日期模板
     */
    private List<HtlReservContTemplate> lisHtlReservContTemplate =
        new ArrayList<HtlReservContTemplate>();

    private List<HtlReservContTemplate> listHtlReservContTplt = 
        new ArrayList<HtlReservContTemplate>();

    /*
     * 担保取消和修改条款模板
     */
    private List<HtlAssureItemTemplate> lisHtlAssureItemTemplate = 
        new ArrayList<HtlAssureItemTemplate>();

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

    private List lsmustDate = new ArrayList();

    private int dateNum;

    private int assuerItemOne;

    private int assuerItemTwo;

    private int assuerItemThree;

    private int assuerItemFour;

    private int assuerItemFive;

    private int prepayItemOne;

    private int prepayItemTwo;

    private int prepayItemThree;

    private int prepayItemFour;

    private int prepayItemFive;

    /**
     * 从星期中筛选出来的数量
     */
    private int weekDateNum;

    /**
     * 必住日期的关系 and or
     */
    private String continueDatesRelation;

    @Override
    protected Class getEntityClass() {

        return HtlPreconcertItemTemplet.class;

    }

    /*
     * add by shengwei.zuo 2009-02-08 添加预定条款时的页面跳转
     */
    public String addHtlPreItemTpt() {

        return "addHtlPreItemTpt";

    }

    /*
     * add by shengwei.zuo 2009-02-08 添加酒店预订担保预付条款模板信息
     */
    public String saveClauseTemplate() {

        // 将表单数据存入到实体中。
        super.setEntity(super.populateEntity());
        this.setHtlPreconcertItemTempletZ((HtlPreconcertItemTemplet) this.getEntity());

        Map params = super.getParams();

        /*
         * 设置添加时状态标识的值
         */
        new BizRuleCheck();
        htlPreconcertItemTempletZ.setActive(BizRuleCheck.getTrueString());

        /*
         * 添加人的工号，ID，姓名和添加时间;
         */
        if (null != super.getOnlineRoleUser()) {

            if (null == htlPreconcertItemTempletZ.getID() || 0 == 
                htlPreconcertItemTempletZ.getID()) {

                htlPreconcertItemTempletZ.setCreateBy(super.getOnlineRoleUser().getName());
                htlPreconcertItemTempletZ.setCreateByID(super.getOnlineRoleUser().getLoginName());
                htlPreconcertItemTempletZ.setCreateTime(DateUtil.getSystemDate());

            }

            // 添加修改人，修改时间；
            htlPreconcertItemTempletZ.setModifyBy(super.getOnlineRoleUser().getName());
            htlPreconcertItemTempletZ.setModifyByID(super.getOnlineRoleUser().getLoginName());
            htlPreconcertItemTempletZ.setModifyTime(DateUtil.getSystemDate());

        }

        /*
         * 预订条款模板
         */

        // 页面上获取的必住日期 add by shengwei.zuo hotel V2.6 2009-04-22
        HtlReservContTemplate htlReservContTemplate = new HtlReservContTemplate();
        lisHtlReservContTemplate = MyBeanUtil.getBatchObjectFromParam(params,
            HtlReservContTemplate.class, dateNum);
        // 增加从星期中筛选出来的必住日期 addby juesuchen V2.9.2 2009-7-30
        List<HtlReservContWeekBean> htlWeekBeans = MyBeanUtil.getBatchObjectFromParam(params,
            HtlReservContWeekBean.class, weekDateNum);
        // end
        // 预订条款模板相关字段不为空的校验 add by shengwei.zuo hotel V2.6 2009-04-22
        if ((null != aheadDay && !"".equals(aheadDay.toString()))
            || (null != aheadTime && !"".equals(aheadTime))
            || (null != mustFromDate && !"".equals(mustFromDate.toString()))
            || (null != mustToDate && !"".equals(mustToDate.toString()))
            || (null != mustFromTime && !"".equals(mustFromTime))
            || (null != mustToTime && !"".equals(mustToTime))
            || (null != continueNights && !"".equals(continueNights.toString()))
            || (null != maxRestrictNights && !"".equals(maxRestrictNights.toString()))
            || null != minRestrictNights && !"".equals(minRestrictNights.toString())
            || (null != mustAheadDate && !"".equals(mustAheadDate.toString()))
            || 0 < lisHtlReservContTemplate.size() || 0 < htlWeekBeans.size()
            || (null != mustAheadTime && !"".equals(mustAheadTime))) {
            if (0 < lisHtlReservContTemplate.size()) {// 必住日期有输入
                for (int i = 0; i < lisHtlReservContTemplate.size(); i++) {
                    htlReservContTemplate = lisHtlReservContTemplate.get(i);
                    // 在星期中筛选出来的必住日期基础上增加必住日期,去重复 addby juesuchen V2.9.2 2009-7-30
                    if (!listHtlReservContTplt.contains(htlReservContTemplate)) {
                        // 如果不存在,则把必住日期加入集合中
                        HtlReservContTemplate htlReservContTplt = new HtlReservContTemplate();
                        htlReservContTplt.setContinueDate(htlReservContTemplate.getContinueDate());
                        htlReservContTplt.setHtlReservationTemplate(htlReservationTemplate);
                        listHtlReservContTplt.add(htlReservContTplt);
                    }
                }
            } else {// 必住星期
                for (HtlReservContWeekBean conWeekBean : htlWeekBeans) {
                    HtlReservContTemplate htlReservContTplt = new HtlReservContTemplate();
                    htlReservContTplt.setContinueDate(conWeekBean.getContinueWeekDateBegin());
                    htlReservContTplt.setContinueEndDate(conWeekBean.getContinueWeekDateEnd());
                    htlReservContTplt.setWeeks(conWeekBean.getStringContinueWeeks());
                    listHtlReservContTplt.add(htlReservContTplt);
                    // conWeekBean.addHtlReservContDateByWeek(
                    // listHtlReservContTplt,HtlReservContTemplate.class);
                }
            }
            htlReservationTemplate.setAheadDay(aheadDay);
            htlReservationTemplate.setAheadTime(aheadTime);
            htlReservationTemplate.setContinueNights(continueNights);
            
            
            /**
             * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 begin
             */
            htlReservationTemplate.setMustFromDate(mustFromDate);
            htlReservationTemplate.setMustToDate(mustToDate);
            htlReservationTemplate.setMustFromTime(mustFromTime);
            htlReservationTemplate.setMustToTime(mustToTime);
            /**
             * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 end
             */
            
            htlReservationTemplate.setMaxRestrictNights(maxRestrictNights);
            htlReservationTemplate.setMinRestrictNights(minRestrictNights);

            htlReservationTemplate.setMustAheadDate(mustAheadDate);
            htlReservationTemplate.setMustAheadTime(mustAheadTime);
            htlReservationTemplate.setContinueDatesRelation(continueDatesRelation);// 增加必住关系
            htlReservationTemplate.setHtlPreconcertItemTempletZ(htlPreconcertItemTempletZ);

            htlReservationTemplate.setLisHtlReservContTemplate(listHtlReservContTplt);

            htlReservationTemplateZ.add(htlReservationTemplate);

            htlPreconcertItemTempletZ.setHtlReservationTemplateZ(htlReservationTemplateZ);

        }

        /*
         * 担保条款模板
         */

        // htlAssureTemplateZ = new HtlAssureTemplate();
        // 第一种--取消和修改担保条款信息添加
        lisHtlAssureItemTemplateOne = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateOne.class, assuerItemOne);

        for (int i = 0; i < lisHtlAssureItemTemplateOne.size(); i++) {

            HtlAssureItemTemplate htlAssureItemTemplateEntiyOe = new HtlAssureItemTemplate();

            HtlAssureItemTemplateOne htlAssureItemTemplateOne = lisHtlAssureItemTemplateOne.get(i);

            htlAssureItemTemplateEntiyOe.setScope(htlAssureItemTemplateOne.getScopeOne());
            htlAssureItemTemplateEntiyOe.setDeductType(htlAssureItemTemplateOne.getDeductTypeOne());
            htlAssureItemTemplateEntiyOe.setPercentage(htlAssureItemTemplateOne.getPercentageOne());
            htlAssureItemTemplateEntiyOe.setType(ClaueType.ASSURE_TYPE_ONE);

            lisHtlAssureItemTemplate.add(htlAssureItemTemplateEntiyOe);

        }

        // 第二种--取消和修改担保条款信息添加
        lisHtlAssureItemTemplateTwo = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateTwo.class, assuerItemTwo);

        for (int i = 0; i < lisHtlAssureItemTemplateTwo.size(); i++) {

            HtlAssureItemTemplate htlAssureItemTemplateEntiyTw = new HtlAssureItemTemplate();

            HtlAssureItemTemplateTwo htlAssureItemTemplateTwo = lisHtlAssureItemTemplateTwo.get(i);

            htlAssureItemTemplateEntiyTw.setDeductType(htlAssureItemTemplateTwo.getDeductTypeTwo());
            htlAssureItemTemplateEntiyTw.setScope(htlAssureItemTemplateTwo.getScopeTwo());
            htlAssureItemTemplateEntiyTw.setFirstDateOrDays(htlAssureItemTemplateTwo
                .getFirstDateOrDaysTwo());
            htlAssureItemTemplateEntiyTw.setFirstTime(htlAssureItemTemplateTwo.getFirstTimeTwo());
            htlAssureItemTemplateEntiyTw.setSecondDateOrDays(htlAssureItemTemplateTwo
                .getSecondDateOrDaysTwo());
            htlAssureItemTemplateEntiyTw.setSecondTime(htlAssureItemTemplateTwo.getSecondTimeTwo());
            htlAssureItemTemplateEntiyTw.setPercentage(htlAssureItemTemplateTwo.getPercentageTwo());
            htlAssureItemTemplateEntiyTw.setType(ClaueType.ASSURE_TYPE_TWO);

            lisHtlAssureItemTemplate.add(htlAssureItemTemplateEntiyTw);

        }

        // 第三种--取消和修改担保条款信息添加

        lisHtlAssureItemTemplateThree = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateThree.class, assuerItemThree);

        for (int i = 0; i < lisHtlAssureItemTemplateThree.size(); i++) {

            HtlAssureItemTemplate htlAssureItemTemplateEntiyTr = new HtlAssureItemTemplate();

            HtlAssureItemTemplateThree htlAssureItemTemplateThree = lisHtlAssureItemTemplateThree
                .get(i);

            htlAssureItemTemplateEntiyTr.setDeductType(htlAssureItemTemplateThree
                .getDeductTypeThree());
            htlAssureItemTemplateEntiyTr.setScope(htlAssureItemTemplateThree.getScopeThree());
            htlAssureItemTemplateEntiyTr.setFirstDateOrDays(htlAssureItemTemplateThree
                .getFirstDateOrDaysThree());
            htlAssureItemTemplateEntiyTr.setFirstTime(htlAssureItemTemplateThree
                .getFirstTimeThree());
            htlAssureItemTemplateEntiyTr.setSecondTime(htlAssureItemTemplateThree
                .getSecondTimeThree());
            htlAssureItemTemplateEntiyTr.setPercentage(htlAssureItemTemplateThree
                .getPercentageThree());
            htlAssureItemTemplateEntiyTr.setType(ClaueType.ASSURE_TYPE_THREE);

            lisHtlAssureItemTemplate.add(htlAssureItemTemplateEntiyTr);

        }

        // 第四种--取消和修改担保条款信息添加

        lisHtlAssureItemTemplateFour = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateFour.class, assuerItemFour);

        for (int i = 0; i < lisHtlAssureItemTemplateFour.size(); i++) {

            HtlAssureItemTemplate htlAssureItemTemplateEntiyFu = new HtlAssureItemTemplate();

            HtlAssureItemTemplateFour htlAssureItemTemplateFour = lisHtlAssureItemTemplateFour
                .get(i);

            htlAssureItemTemplateEntiyFu.setDeductType(htlAssureItemTemplateFour
                .getDeductTypeFour());
            htlAssureItemTemplateEntiyFu.setScope(htlAssureItemTemplateFour.getScopeFour());
            htlAssureItemTemplateEntiyFu.setFirstDateOrDays(htlAssureItemTemplateFour
                .getFirstDateOrDaysFour());
            htlAssureItemTemplateEntiyFu.setFirstTime(htlAssureItemTemplateFour.getFirstTimeFour());
            htlAssureItemTemplateEntiyFu.setSecondDateOrDays(htlAssureItemTemplateFour
                .getSecondDateOrDaysFour());
            htlAssureItemTemplateEntiyFu.setSecondTime(htlAssureItemTemplateFour
                .getSecondTimeFour());
            htlAssureItemTemplateEntiyFu.setPercentage(htlAssureItemTemplateFour
                .getPercentageFour());
            htlAssureItemTemplateEntiyFu.setType(ClaueType.ASSURE_TYPE_FORV);

            lisHtlAssureItemTemplate.add(htlAssureItemTemplateEntiyFu);

        }

        // 第五种--取消和修改担保条款信息添加
        lisHtlAssureItemTemplateFive = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateFive.class, assuerItemFive);

        for (int i = 0; i < lisHtlAssureItemTemplateFive.size(); i++) {

            HtlAssureItemTemplate htlAssureItemTemplateEntiyFv = new HtlAssureItemTemplate();

            HtlAssureItemTemplateFive htlAssureItemTemplateFive = lisHtlAssureItemTemplateFive
                .get(i);

            htlAssureItemTemplateEntiyFv.setDeductType(htlAssureItemTemplateFive
                .getDeductTypeFive());
            htlAssureItemTemplateEntiyFv.setScope(htlAssureItemTemplateFive.getScopeFive());
            htlAssureItemTemplateEntiyFv.setBeforeOrAfter(htlAssureItemTemplateFive
                .getBeforeOrAfterFive());
            htlAssureItemTemplateEntiyFv.setFirstTime(htlAssureItemTemplateFive.getFirstTimeFive());
            htlAssureItemTemplateEntiyFv.setPercentage(htlAssureItemTemplateFive
                .getPercentageFive());
            htlAssureItemTemplateEntiyFv.setType(ClaueType.ASSURE_TYPE_FIVE);

            lisHtlAssureItemTemplate.add(htlAssureItemTemplateEntiyFv);

        }

        // 把5种列表值都添加进去
        /**
         * 如果担保条款没有设置值.则不插入数据库
         */
        if ((null != assureLetter && !"".equals(assureLetter))
            || (null != assureType && !"".equals(assureType))
            || (null != assureType && !"".equals(assureType))
            || (null != isNotConditional && !"".equals(isNotConditional))
            || (null != latestAssureTime && !"".equals(latestAssureTime))
            || (null != overRoomNumber && !"".equals(overRoomNumber.toString()))
            || (null != overNightsNumber && !overNightsNumber.toString().equals(""))) {

            HtlAssureTemplate htlAssureTplt = new HtlAssureTemplate();
            htlAssureTplt.setLisHtlAssureItemTemplate(lisHtlAssureItemTemplate);
            htlAssureTplt.setAssureLetter(assureLetter);
            htlAssureTplt.setAssureType(assureType);
            htlAssureTplt.setIsNotConditional(isNotConditional);
            htlAssureTplt.setLatestAssureTime(latestAssureTime);
            htlAssureTplt.setOverRoomNumber(overRoomNumber);
            htlAssureTplt.setOverNightsNumber(overNightsNumber);
            htlAssureTplt.setHtlPreconcertItemTempletZ(htlPreconcertItemTempletZ);

            htlAssureTemplateZ.add(htlAssureTplt);

            htlPreconcertItemTempletZ.setHtlAssureTemplateZ(htlAssureTemplateZ);

        }

        /*
         * 预付条款模板
         */

        // htlPrepayTemplateZ = new HtlPrepayTemplate();
        // 第一种--取消和修改预付条款种类
        lisHtlPrepayItemTemplateOne = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateOne.class, prepayItemOne);

        for (int i = 0; i < lisHtlPrepayItemTemplateOne.size(); i++) {

            HtlPrepayItemTemplate htlHtlPrepayItemTemplateEntiyOe = new HtlPrepayItemTemplate();

            HtlPrepayItemTemplateOne htlPrepayItemTemplateOne = lisHtlPrepayItemTemplateOne.get(i);

            htlHtlPrepayItemTemplateEntiyOe.setScope(htlPrepayItemTemplateOne.getScopePPOne());
            htlHtlPrepayItemTemplateEntiyOe.setDeductType(htlPrepayItemTemplateOne
                .getDeductTypePPOne());
            htlHtlPrepayItemTemplateEntiyOe.setPercentage(htlPrepayItemTemplateOne
                .getPercentagePPOne());
            htlHtlPrepayItemTemplateEntiyOe.setType(ClaueType.PREPAY_TYPE_ONE);
            lisHtlPrepayItemTemplate.add(htlHtlPrepayItemTemplateEntiyOe);

        }

        // 第二种--取消和修改预付条款种类
        lisHtlPrepayItemTemplateTwo = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateTwo.class, prepayItemTwo);

        for (int i = 0; i < lisHtlPrepayItemTemplateTwo.size(); i++) {

            HtlPrepayItemTemplate htlPrepayItemTemplateEntiyTw = new HtlPrepayItemTemplate();

            HtlPrepayItemTemplateTwo htlPrepayItemTemplateTwo = lisHtlPrepayItemTemplateTwo.get(i);

            htlPrepayItemTemplateEntiyTw.setDeductType(htlPrepayItemTemplateTwo
                .getDeductTypePPTwo());
            htlPrepayItemTemplateEntiyTw.setScope(htlPrepayItemTemplateTwo.getScopePPTwo());
            htlPrepayItemTemplateEntiyTw.setFirstDateOrDays(htlPrepayItemTemplateTwo
                .getFirstDateOrDaysPPTwo());
            htlPrepayItemTemplateEntiyTw.setFirstTime(htlPrepayItemTemplateTwo.getFirstTimePPTwo());
            htlPrepayItemTemplateEntiyTw.setSecondDateOrDays(htlPrepayItemTemplateTwo
                .getSecondDateOrDaysPPTwo());
            htlPrepayItemTemplateEntiyTw.setSecondTime(htlPrepayItemTemplateTwo
                .getSecondTimePPTwo());
            htlPrepayItemTemplateEntiyTw.setPercentage(htlPrepayItemTemplateTwo
                .getPercentagePPTwo());
            htlPrepayItemTemplateEntiyTw.setType(ClaueType.PREPAY_TYPE_TWO);

            lisHtlPrepayItemTemplate.add(htlPrepayItemTemplateEntiyTw);

        }

        // 第三种--取消和修改预付条款种类

        lisHtlPrepayItemTemplateThree = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateThree.class, prepayItemThree);

        for (int i = 0; i < lisHtlPrepayItemTemplateThree.size(); i++) {

            HtlPrepayItemTemplate HtlPrepayItemTemplateEntiyTr = new HtlPrepayItemTemplate();

            HtlPrepayItemTemplateThree htlPrepayItemTemplateThree = lisHtlPrepayItemTemplateThree
                .get(i);

            HtlPrepayItemTemplateEntiyTr.setDeductType(htlPrepayItemTemplateThree
                .getDeductTypePPThree());
            HtlPrepayItemTemplateEntiyTr.setScope(htlPrepayItemTemplateThree.getScopePPThree());
            HtlPrepayItemTemplateEntiyTr.setFirstDateOrDays(htlPrepayItemTemplateThree
                .getFirstDateOrDaysPPThree());
            HtlPrepayItemTemplateEntiyTr.setFirstTime(htlPrepayItemTemplateThree
                .getFirstTimePPThree());
            HtlPrepayItemTemplateEntiyTr.setSecondTime(htlPrepayItemTemplateThree
                .getSecondTimePPThree());
            HtlPrepayItemTemplateEntiyTr.setPercentage(htlPrepayItemTemplateThree
                .getPercentagePPThree());
            HtlPrepayItemTemplateEntiyTr.setType(ClaueType.PREPAY_TYPE_THREE);

            lisHtlPrepayItemTemplate.add(HtlPrepayItemTemplateEntiyTr);

        }

        // 第四种--取消和修改预付条款种类

        lisHtlPrepayItemTemplateFour = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateFour.class, prepayItemFour);

        for (int i = 0; i < lisHtlPrepayItemTemplateFour.size(); i++) {

            HtlPrepayItemTemplate htlPrepayItemTemplateEntiyFu = new HtlPrepayItemTemplate();

            HtlPrepayItemTemplateFour htlPrepayItemTemplateFour = lisHtlPrepayItemTemplateFour
                .get(i);

            htlPrepayItemTemplateEntiyFu.setDeductType(htlPrepayItemTemplateFour
                .getDeductTypePPFour());
            htlPrepayItemTemplateEntiyFu.setScope(htlPrepayItemTemplateFour.getScopePPFour());
            htlPrepayItemTemplateEntiyFu.setFirstDateOrDays(htlPrepayItemTemplateFour
                .getFirstDateOrDaysPPFour());
            htlPrepayItemTemplateEntiyFu.setFirstTime(htlPrepayItemTemplateFour
                .getFirstTimePPFour());
            htlPrepayItemTemplateEntiyFu.setSecondDateOrDays(htlPrepayItemTemplateFour
                .getSecondDateOrDaysPPFour());
            htlPrepayItemTemplateEntiyFu.setSecondTime(htlPrepayItemTemplateFour
                .getSecondTimePPFour());
            htlPrepayItemTemplateEntiyFu.setPercentage(htlPrepayItemTemplateFour
                .getPercentagePPFour());
            htlPrepayItemTemplateEntiyFu.setType(ClaueType.PREPAY_TYPE_FORV);

            lisHtlPrepayItemTemplate.add(htlPrepayItemTemplateEntiyFu);

        }

        // 第五种--取消和修改预付条款种类
        lisHtlPrepayItemTemplateFive = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateFive.class, prepayItemFive);

        for (int i = 0; i < lisHtlPrepayItemTemplateFive.size(); i++) {

            HtlPrepayItemTemplate htlPrepayItemTemplateEntiyFv = new HtlPrepayItemTemplate();

            HtlPrepayItemTemplateFive htlPrepayItemTemplateFive = lisHtlPrepayItemTemplateFive
                .get(i);

            htlPrepayItemTemplateEntiyFv.setDeductType(htlPrepayItemTemplateFive
                .getDeductTypePPFive());
            htlPrepayItemTemplateEntiyFv.setScope(htlPrepayItemTemplateFive.getScopePPFive());
            htlPrepayItemTemplateEntiyFv.setBeforeOrAfter(htlPrepayItemTemplateFive
                .getBeforeOrAfterPPFive());
            htlPrepayItemTemplateEntiyFv.setFirstTime(htlPrepayItemTemplateFive
                .getFirstTimePPFive());
            htlPrepayItemTemplateEntiyFv.setPercentage(htlPrepayItemTemplateFive
                .getPercentagePPFive());
            htlPrepayItemTemplateEntiyFv.setType(ClaueType.PREPAY_TYPE_FIVE);

            lisHtlPrepayItemTemplate.add(htlPrepayItemTemplateEntiyFv);

        }
        /**
         * 如果预付条款没有设置值,则不插入数据库
         */

        if ((null != amountType && !"".equals(amountType))
            || (null != balanceType && !"".equals(balanceType))
            || (null != daysAfterConfirm && !"".equals(daysAfterConfirm.toString()))
            || (null != limitAheadDays && !"".equals(limitAheadDays.toString()))
            || (null != limitAheadDaysTime && !"".equals(limitAheadDaysTime))
            || (null != limitDate && !"".equals(limitDate.toString()))
            || (null != limitTime && !"".equals(limitTime))
            || (null != paymentType && !"".equals(paymentType))
            || (null != timeAfterConfirm && !"".equals(timeAfterConfirm))
            || (null != timeLimitType && !"".equals(timeLimitType))
            || (null != prepayDeductType && !"".equals(prepayDeductType))) {

            // 添加所有的取消和修改预付条款信息
            HtlPrepayTemplate htlPrepayTplt = new HtlPrepayTemplate();
            htlPrepayTplt.setLisHtlPrepayItemTemplate(lisHtlPrepayItemTemplate);
            htlPrepayTplt.setAmountType(amountType);
            htlPrepayTplt.setBalanceType(balanceType);
            htlPrepayTplt.setDaysAfterConfirm(daysAfterConfirm);
            htlPrepayTplt.setLimitAheadDays(limitAheadDays);
            htlPrepayTplt.setLimitAheadDaysTime(limitAheadDaysTime);
            htlPrepayTplt.setLimitDate(limitDate);
            htlPrepayTplt.setLimitTime(limitTime);
            htlPrepayTplt.setPaymentType(paymentType);
            htlPrepayTplt.setTimeAfterConfirm(timeAfterConfirm);
            htlPrepayTplt.setTimeLimitType(timeLimitType);
            htlPrepayTplt.setPrepayDeductType(prepayDeductType);
            htlPrepayTplt.setHtlPreconcertItemTempletZ(htlPreconcertItemTempletZ);
            htlPrepayTemplateZ.add(htlPrepayTplt);
            htlPreconcertItemTempletZ.setHtlPrepayTemplateZ(htlPrepayTemplateZ);
        }

        /*
         * 添加酒店预订担保预付条款模板信息
         */

        try {
            claluseTemplateManage.createPreconcertItemTemplet(htlPreconcertItemTempletZ);
        } catch (Exception e) {
        	super.setErrorMessage("保存预订条款模板出错！");
        	return "forwardToError";
        }
        return SUCCESS;
    }

    /*
     * 查看选定的预定条款模板信息：
     */

    public String viewClTemp() {

        super.setEntityID(ID);
        super.setEntity(super.populateEntity());
        this.setHtlPreconcertItemTempletZ((HtlPreconcertItemTemplet) this.getEntity());

        if (0 < ID) {

            htlPreconcertItemTempletZ = claluseTemplateManage.findHtlPreconITById(ID);

            /*
             * 担保条款-取消和修改条款
             */

            htlAssureTemplateZ = htlPreconcertItemTempletZ.getHtlAssureTemplateZ();

            if (0 < htlAssureTemplateZ.size()) {

                HtlAssureTemplate tlAssureTemplate = htlAssureTemplateZ.get(0);

                if (null != tlAssureTemplate) {

                    lisHtlAssureItemTemplate = tlAssureTemplate.getLisHtlAssureItemTemplate();

                    for (int i = 0; i < lisHtlAssureItemTemplate.size(); i++) {

                        HtlAssureItemTemplate htlAssTpl = lisHtlAssureItemTemplate.get(i);

                        String assUpType = htlAssTpl.getType();

                        if (ClaueType.ASSURE_TYPE_ONE.equals(assUpType)
                            || assUpType.equals(ClaueType.ASSURE_TYPE_ONE)) {

                            HtlAssureItemTemplateOne htlAssTplOne = new HtlAssureItemTemplateOne();

                            htlAssTplOne.setScopeOne(htlAssTpl.getScope());
                            htlAssTplOne.setDeductTypeOne(htlAssTpl.getDeductType());
                            htlAssTplOne.setPercentageOne(htlAssTpl.getPercentage());

                            lisHtlAssureItemTemplateOne.add(htlAssTplOne);

                        } else if (ClaueType.ASSURE_TYPE_TWO.equals(assUpType)
                            || assUpType.equals(ClaueType.ASSURE_TYPE_TWO)) {

                            HtlAssureItemTemplateTwo htlAssTplTwo = new HtlAssureItemTemplateTwo();

                            htlAssTplTwo.setScopeTwo(htlAssTpl.getScope());
                            htlAssTplTwo.setDeductTypeTwo(htlAssTpl.getDeductType());
                            htlAssTplTwo.setPercentageTwo(htlAssTpl.getPercentage());
                            htlAssTplTwo.setFirstDateOrDaysTwo(htlAssTpl.getFirstDateOrDays());
                            htlAssTplTwo.setFirstTimeTwo(htlAssTpl.getFirstTime());
                            htlAssTplTwo.setSecondDateOrDaysTwo(htlAssTpl.getSecondDateOrDays());
                            htlAssTplTwo.setSecondTimeTwo(htlAssTpl.getSecondTime());

                            lisHtlAssureItemTemplateTwo.add(htlAssTplTwo);

                        } else if (ClaueType.ASSURE_TYPE_THREE.equals(assUpType)
                            || assUpType.equals(ClaueType.ASSURE_TYPE_THREE)) {

                            HtlAssureItemTemplateThree htlAssTplThree = 
                                new HtlAssureItemTemplateThree();

                            htlAssTplThree.setScopeThree(htlAssTpl.getScope());
                            htlAssTplThree.setDeductTypeThree(htlAssTpl.getDeductType());
                            htlAssTplThree.setPercentageThree(htlAssTpl.getPercentage());
                            htlAssTplThree.setFirstDateOrDaysThree(htlAssTpl.getFirstDateOrDays());
                            htlAssTplThree.setFirstTimeThree(htlAssTpl.getFirstTime());
                            htlAssTplThree.setSecondTimeThree(htlAssTpl.getSecondTime());

                            lisHtlAssureItemTemplateThree.add(htlAssTplThree);

                        } else if (ClaueType.ASSURE_TYPE_FORV.equals(assUpType)
                            || assUpType.equals(ClaueType.ASSURE_TYPE_FORV)) {

                            HtlAssureItemTemplateFour htlAssTplFour = 
                                new HtlAssureItemTemplateFour();

                            htlAssTplFour.setScopeFour(htlAssTpl.getScope());
                            htlAssTplFour.setDeductTypeFour(htlAssTpl.getDeductType());
                            htlAssTplFour.setPercentageFour(htlAssTpl.getPercentage());
                            htlAssTplFour.setFirstDateOrDaysFour(htlAssTpl.getFirstDateOrDays());
                            htlAssTplFour.setFirstTimeFour(htlAssTpl.getFirstTime());
                            htlAssTplFour.setSecondDateOrDaysFour(htlAssTpl.getSecondDateOrDays());
                            htlAssTplFour.setSecondTimeFour(htlAssTpl.getSecondTime());

                            lisHtlAssureItemTemplateFour.add(htlAssTplFour);

                        } else if (ClaueType.ASSURE_TYPE_FIVE.equals(assUpType)
                            || assUpType.equals(ClaueType.ASSURE_TYPE_FIVE)) {

                            HtlAssureItemTemplateFive htlAssTplFive = 
                                new HtlAssureItemTemplateFive();

                            htlAssTplFive.setScopeFive(htlAssTpl.getScope());
                            htlAssTplFive.setDeductTypeFive(htlAssTpl.getDeductType());
                            htlAssTplFive.setPercentageFive(htlAssTpl.getPercentage());
                            htlAssTplFive.setBeforeOrAfterFive(htlAssTpl.getBeforeOrAfter());
                            htlAssTplFive.setFirstTimeFive(htlAssTpl.getFirstTime());

                            lisHtlAssureItemTemplateFive.add(htlAssTplFive);

                        }

                    }
                }
            }
            /*
             * 预付条款-取消和修改条款
             */

            htlPrepayTemplateZ = htlPreconcertItemTempletZ.getHtlPrepayTemplateZ();

            if (0 < htlPrepayTemplateZ.size()) {

                HtlPrepayTemplate tlPrepayTemplate = htlPrepayTemplateZ.get(0);

                if (null != tlPrepayTemplate) {

                    lisHtlPrepayItemTemplate = tlPrepayTemplate.getLisHtlPrepayItemTemplate();

                    for (int i = 0; i < lisHtlPrepayItemTemplate.size(); i++) {

                        HtlPrepayItemTemplate htlpltp = lisHtlPrepayItemTemplate.get(i);

                        String ppType = htlpltp.getType();

                        if (ClaueType.PREPAY_TYPE_ONE.equals(ppType)
                            || ppType.equals(ClaueType.PREPAY_TYPE_ONE)) {

                            HtlPrepayItemTemplateOne htlpltpOne = new HtlPrepayItemTemplateOne();

                            htlpltpOne.setScopePPOne(htlpltp.getScope());
                            htlpltpOne.setDeductTypePPOne(htlpltp.getDeductType());
                            htlpltpOne.setPercentagePPOne(htlpltp.getPercentage());

                            lisHtlPrepayItemTemplateOne.add(htlpltpOne);

                        } else if (ClaueType.PREPAY_TYPE_TWO.equals(ppType)
                            || ppType.equals(ClaueType.PREPAY_TYPE_TWO)) {

                            HtlPrepayItemTemplateTwo htlpltpTwo = new HtlPrepayItemTemplateTwo();

                            htlpltpTwo.setScopePPTwo(htlpltp.getScope());
                            htlpltpTwo.setDeductTypePPTwo(htlpltp.getDeductType());
                            htlpltpTwo.setPercentagePPTwo(htlpltp.getPercentage());
                            htlpltpTwo.setFirstDateOrDaysPPTwo(htlpltp.getFirstDateOrDays());
                            htlpltpTwo.setFirstTimePPTwo(htlpltp.getFirstTime());
                            htlpltpTwo.setSecondDateOrDaysPPTwo(htlpltp.getSecondDateOrDays());
                            htlpltpTwo.setSecondTimePPTwo(htlpltp.getSecondTime());

                            lisHtlPrepayItemTemplateTwo.add(htlpltpTwo);

                        } else if (ClaueType.PREPAY_TYPE_THREE.equals(ppType)
                            || ppType.equals(ClaueType.PREPAY_TYPE_THREE)) {

                            HtlPrepayItemTemplateThree htlpltpThree = 
                                new HtlPrepayItemTemplateThree();

                            htlpltpThree.setScopePPThree(htlpltp.getScope());
                            htlpltpThree.setDeductTypePPThree(htlpltp.getDeductType());
                            htlpltpThree.setPercentagePPThree(htlpltp.getPercentage());
                            htlpltpThree.setFirstDateOrDaysPPThree(htlpltp.getFirstDateOrDays());
                            htlpltpThree.setFirstTimePPThree(htlpltp.getFirstTime());
                            htlpltpThree.setSecondTimePPThree(htlpltp.getSecondTime());

                            lisHtlPrepayItemTemplateThree.add(htlpltpThree);

                        } else if (ClaueType.PREPAY_TYPE_FORV.equals(ppType)
                            || ppType.equals(ClaueType.PREPAY_TYPE_FORV)) {

                            HtlPrepayItemTemplateFour htlpltpFour = new HtlPrepayItemTemplateFour();

                            htlpltpFour.setScopePPFour(htlpltp.getScope());
                            htlpltpFour.setDeductTypePPFour(htlpltp.getDeductType());
                            htlpltpFour.setPercentagePPFour(htlpltp.getPercentage());
                            htlpltpFour.setFirstDateOrDaysPPFour(htlpltp.getFirstDateOrDays());
                            htlpltpFour.setFirstTimePPFour(htlpltp.getFirstTime());
                            htlpltpFour.setSecondDateOrDaysPPFour(htlpltp.getSecondDateOrDays());
                            htlpltpFour.setSecondTimePPFour(htlpltp.getSecondTime());

                            lisHtlPrepayItemTemplateFour.add(htlpltpFour);

                        } else if (ClaueType.PREPAY_TYPE_FIVE.equals(ppType)
                            || ppType.equals(ClaueType.PREPAY_TYPE_FIVE)) {

                            HtlPrepayItemTemplateFive htlpltpFive = new HtlPrepayItemTemplateFive();

                            htlpltpFive.setScopePPFive(htlpltp.getScope());
                            htlpltpFive.setDeductTypePPFive(htlpltp.getDeductType());
                            htlpltpFive.setPercentagePPFive(htlpltp.getPercentage());
                            htlpltpFive.setFirstTimePPFive(htlpltp.getFirstTime());
                            htlpltpFive.setBeforeOrAfterPPFive(htlpltp.getBeforeOrAfter());

                            lisHtlPrepayItemTemplateFive.add(htlpltpFive);

                        }

                    }
                }
            }

        }

        return "viewClTp";
    }

    /*
     * 删除一个预定条款模板 add by shengwei.zuo 2009-02-18
     */
    public String delClTemp() {

        // 判断能否获取到当前登陆的用户信息
        if (null != super.getOnlineRoleUser()) {

            // 修改人工号
            modifyBy = super.getOnlineRoleUser().getName();
            // 修改人名称
            modifyByID = super.getOnlineRoleUser().getLoginName();
            // 修改时间
            modifyTime = DateUtil.getSystemDate();

            // 删除人工号
            delBy = super.getOnlineRoleUser().getName();
            // 删除人名称
            delByID = super.getOnlineRoleUser().getLoginName();
            // 删除时间
            delTime = DateUtil.getSystemDate();

        } else {
            return super.forwardError("获取登陆用户信息失效,请重新登陆");
        }

        // 是否删除的标记
        Active = BizRuleCheck.getFalseString();

        htlPreconcertItemTempletZ = new HtlPreconcertItemTemplet();

        htlPreconcertItemTempletZ.setID(ID);
        htlPreconcertItemTempletZ.setDelBy(delBy);
        htlPreconcertItemTempletZ.setDelByID(delByID);
        htlPreconcertItemTempletZ.setDelTime(delTime);

        htlPreconcertItemTempletZ.setModifyBy(modifyBy);
        htlPreconcertItemTempletZ.setModifyByID(modifyByID);
        htlPreconcertItemTempletZ.setModifyTime(modifyTime);

        htlPreconcertItemTempletZ.setActive(Active);

        try {
            claluseTemplateManage.delHtlPreconItTpltInfo(htlPreconcertItemTempletZ);
        } catch (Exception e) {
            super.setErrorMessage("删除预订条款模板出错！");
            return "forwardToError";
        }
        return SUCCESS;
    }

    public HtlPreconcertItemTemplet getHtlPreconcertItemTempletZ() {
        return htlPreconcertItemTempletZ;
    }

    public void setHtlPreconcertItemTempletZ(HtlPreconcertItemTemplet htlPreconcertItemTempletZ) {
        this.htlPreconcertItemTempletZ = htlPreconcertItemTempletZ;
    }

    public List<HtlAssureTemplate> getHtlAssureTemplateZ() {
        return htlAssureTemplateZ;
    }

    public void setHtlAssureTemplateZ(List<HtlAssureTemplate> htlAssureTemplateZ) {
        this.htlAssureTemplateZ = htlAssureTemplateZ;
    }

    public List<HtlPrepayTemplate> getHtlPrepayTemplateZ() {
        return htlPrepayTemplateZ;
    }

    public void setHtlPrepayTemplateZ(List<HtlPrepayTemplate> htlPrepayTemplateZ) {
        this.htlPrepayTemplateZ = htlPrepayTemplateZ;
    }

    public List<HtlReservationTemplate> getHtlReservationTemplateZ() {
        return htlReservationTemplateZ;
    }

    public void setHtlReservationTemplateZ(List<HtlReservationTemplate> htlReservationTemplateZ) {
        this.htlReservationTemplateZ = htlReservationTemplateZ;
    }

    public ClauseTemplateManage getClaluseTemplateManage() {
        return claluseTemplateManage;
    }

    public void setClaluseTemplateManage(ClauseTemplateManage claluseTemplateManage) {
        this.claluseTemplateManage = claluseTemplateManage;
    }

    public long getHotelId() {

        return hotelId;

    }

    public void setHotelId(long hotelId) {

        this.hotelId = hotelId;

    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateByID() {
        return createByID;
    }

    public void setCreateByID(String createByID) {
        this.createByID = createByID;
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

    public String getDelByID() {
        return delByID;
    }

    public void setDelByID(String delByID) {
        this.delByID = delByID;
    }

    public Date getDelTime() {
        return delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
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

    public String getModifyByID() {
        return modifyByID;
    }

    public void setModifyByID(String modifyByID) {
        this.modifyByID = modifyByID;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
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

    public Long getReservationTemplateId() {
        return reservationTemplateId;
    }

    public void setReservationTemplateId(Long reservationTemplateId) {
        this.reservationTemplateId = reservationTemplateId;
    }

    public String getTimeAfterConfirm() {
        return timeAfterConfirm;
    }

    public void setTimeAfterConfirm(String timeAfterConfirm) {
        this.timeAfterConfirm = timeAfterConfirm;
    }

    public List<HtlAssureItemTemplate> getLisHtlAssureItemTemplate() {
        return lisHtlAssureItemTemplate;
    }

    public void setLisHtlAssureItemTemplate(List<HtlAssureItemTemplate> lisHtlAssureItemTemplate) {
        this.lisHtlAssureItemTemplate = lisHtlAssureItemTemplate;
    }

    public List<HtlPrepayItemTemplate> getLisHtlPrepayItemTemplate() {
        return lisHtlPrepayItemTemplate;
    }

    public void setLisHtlPrepayItemTemplate(List<HtlPrepayItemTemplate> lisHtlPrepayItemTemplate) {
        this.lisHtlPrepayItemTemplate = lisHtlPrepayItemTemplate;
    }

    public List<HtlReservContTemplate> getLisHtlReservContTemplate() {
        return lisHtlReservContTemplate;
    }

    public void setLisHtlReservContTemplate(List<HtlReservContTemplate> lisHtlReservContTemplate) {
        this.lisHtlReservContTemplate = lisHtlReservContTemplate;
    }

    public List getLsmustDate() {
        return lsmustDate;
    }

    public void setLsmustDate(List lsmustDate) {
        this.lsmustDate = lsmustDate;
    }

    public int getDateNum() {
        return dateNum;
    }

    public void setDateNum(int dateNum) {
        this.dateNum = dateNum;
    }

    public int getAssuerItemOne() {
        return assuerItemOne;
    }

    public void setAssuerItemOne(int assuerItemOne) {
        this.assuerItemOne = assuerItemOne;
    }

    public int getAssuerItemTwo() {
        return assuerItemTwo;
    }

    public void setAssuerItemTwo(int assuerItemTwo) {
        this.assuerItemTwo = assuerItemTwo;
    }

    public void setLisHtlAssureItemTemplateFive(
        List<HtlAssureItemTemplateFive> lisHtlAssureItemTemplateFive) {
        this.lisHtlAssureItemTemplateFive = lisHtlAssureItemTemplateFive;
    }

    public void setLisHtlAssureItemTemplateFour(
        List<HtlAssureItemTemplateFour> lisHtlAssureItemTemplateFour) {
        this.lisHtlAssureItemTemplateFour = lisHtlAssureItemTemplateFour;
    }

    public void setLisHtlAssureItemTemplateOne(
        List<HtlAssureItemTemplateOne> lisHtlAssureItemTemplateOne) {
        this.lisHtlAssureItemTemplateOne = lisHtlAssureItemTemplateOne;
    }

    public void setLisHtlAssureItemTemplateThree(
        List<HtlAssureItemTemplateThree> lisHtlAssureItemTemplateThree) {
        this.lisHtlAssureItemTemplateThree = lisHtlAssureItemTemplateThree;
    }

    public void setLisHtlAssureItemTemplateTwo(
        List<HtlAssureItemTemplateTwo> lisHtlAssureItemTemplateTwo) {
        this.lisHtlAssureItemTemplateTwo = lisHtlAssureItemTemplateTwo;
    }

    public int getAssuerItemFive() {
        return assuerItemFive;
    }

    public void setAssuerItemFive(int assuerItemFive) {
        this.assuerItemFive = assuerItemFive;
    }

    public int getAssuerItemFour() {
        return assuerItemFour;
    }

    public void setAssuerItemFour(int assuerItemFour) {
        this.assuerItemFour = assuerItemFour;
    }

    public int getAssuerItemThree() {
        return assuerItemThree;
    }

    public void setAssuerItemThree(int assuerItemThree) {
        this.assuerItemThree = assuerItemThree;
    }

    public List<HtlAssureItemTemplateFive> getLisHtlAssureItemTemplateFive() {
        return lisHtlAssureItemTemplateFive;
    }

    public List<HtlAssureItemTemplateFour> getLisHtlAssureItemTemplateFour() {
        return lisHtlAssureItemTemplateFour;
    }

    public List<HtlAssureItemTemplateOne> getLisHtlAssureItemTemplateOne() {
        return lisHtlAssureItemTemplateOne;
    }

    public List<HtlAssureItemTemplateThree> getLisHtlAssureItemTemplateThree() {
        return lisHtlAssureItemTemplateThree;
    }

    public List<HtlAssureItemTemplateTwo> getLisHtlAssureItemTemplateTwo() {
        return lisHtlAssureItemTemplateTwo;
    }

    public int getPrepayItemOne() {
        return prepayItemOne;
    }

    public void setPrepayItemOne(int prepayItemOne) {
        this.prepayItemOne = prepayItemOne;
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

    public int getPrepayItemFive() {
        return prepayItemFive;
    }

    public void setPrepayItemFive(int prepayItemFive) {
        this.prepayItemFive = prepayItemFive;
    }

    public int getPrepayItemFour() {
        return prepayItemFour;
    }

    public void setPrepayItemFour(int prepayItemFour) {
        this.prepayItemFour = prepayItemFour;
    }

    public int getPrepayItemThree() {
        return prepayItemThree;
    }

    public void setPrepayItemThree(int prepayItemThree) {
        this.prepayItemThree = prepayItemThree;
    }

    public int getPrepayItemTwo() {
        return prepayItemTwo;
    }

    public void setPrepayItemTwo(int prepayItemTwo) {
        this.prepayItemTwo = prepayItemTwo;
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

    public List<HtlReservContTemplate> getListHtlReservContTplt() {
        return listHtlReservContTplt;
    }

    public void setListHtlReservContTplt(List<HtlReservContTemplate> listHtlReservContTplt) {
        this.listHtlReservContTplt = listHtlReservContTplt;
    }

    public HtlReservationTemplate getHtlReservationTemplate() {
        return htlReservationTemplate;
    }

    public void setHtlReservationTemplate(HtlReservationTemplate htlReservationTemplate) {
        this.htlReservationTemplate = htlReservationTemplate;
    }

    public HtlAssureTemplate getHtlAssureTemplate() {
        return htlAssureTemplate;
    }

    public void setHtlAssureTemplate(HtlAssureTemplate htlAssureTemplate) {
        this.htlAssureTemplate = htlAssureTemplate;
    }

    public HtlPrepayTemplate getHtlPrepayTemplate() {
        return htlPrepayTemplate;
    }

    public void setHtlPrepayTemplate(HtlPrepayTemplate htlPrepayTemplate) {
        this.htlPrepayTemplate = htlPrepayTemplate;
    }

    public Long getOverNightsNumber() {
        return overNightsNumber;
    }

    public void setOverNightsNumber(Long overNightsNumber) {
        this.overNightsNumber = overNightsNumber;
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

}
