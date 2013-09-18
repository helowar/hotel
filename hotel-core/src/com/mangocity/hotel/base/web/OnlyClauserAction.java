package com.mangocity.hotel.base.web;

import java.util.*;

import com.mangocity.hotel.base.constant.ClaueType;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.OnlyClauserManage;
import com.mangocity.hotel.base.persistence.*;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

;

/**
 * create 2009-2-6 hotelV2.6 用于单条款绑定一系列的增删改查
 * 
 * @author lihaibo
 * 
 */
public class OnlyClauserAction extends PersistenceAction {
	
	private HotelRoomTypeService hotelRoomTypeService;
    // 预定条款ID
    private long modelid;

    // 酒店ID
    private long hotelId;// 酒店ID
    
    private String hotelChnName;
    
    private  HotelManage hotelManage;

    private OnlyClauserManage onlyManage;

    private List list = new ArrayList();

    private List lstroomtype = new ArrayList();

    private List modelAll = new ArrayList();

    private List<HtlContract> lstContract = new ArrayList<HtlContract>();

    private String notes;

    // 合同id
    private long contractId;

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

    /*
     * 条款模板ID
     */
    private Long reservationTemplateId;

    /*
     * 注意事项
     */

    private String attention;

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
    private String continueNights;

    /*
     * 最多连住间夜
     */
    private String maxRestrictNights;

    /*
     * 最少连住间夜
     */
    private String minRestrictNights;

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

    private String prepayDeductType;

    private String timeLimitType;

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
     * 付款时限确认后时间点
     */
    private String timeAfterConfirm;

    // 担保和删除类型第一类型 取消修改范围
    private String scopeOne;

    // 扣款范围
    private String deductTypeOne;

    // 扣款百份比
    private String percentageOne;

    // 预付取消修改范围
    private String scopePPOne;

    // 扣款范围
    private String deductTypePPOne;

    // 扣款百分比
    private String percentagePPOne;

    private Date beginDate;// 合同开始时间

    private Date endDate;// 合同结束时间

    private static int countNum = 0;// 计数

    private List lstHtlBatch = new ArrayList();// 日志表list

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

    private List<HtlBinding> htlBinding = new ArrayList<HtlBinding>();

    /*
     * 必须连住日期模板
     */
    private List lisHtlReservContTemplate = new ArrayList();

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

    private HtlPreconcertItemTemplet preconcert;

    // 预定条款下拉值
    private String termsChoose;

    // 预定条款批次表实体类
    private HtlPreconcertItemBatch htlPreconcertItemBatch;

    // 预定条款担保批次实体
    private HtlAssureBatch htlAssureBatch;

    // 预定条款预付批次实体
    private HtlPrepayEverydayBatch htlPrepayEverydayBatch;

    // 担保取消修改批次表List
    private List<HtlAssureItemEverydayBatch> lsthtlAssureItemEverydayBatch = 
        new ArrayList<HtlAssureItemEverydayBatch>();

    // 预付取消与修改批次list
    private List<HtlPrepayItemEverydayBatch> htlPrepayItemEverydayBatchch = 
        new ArrayList<HtlPrepayItemEverydayBatch>();

    private List lsmustDate = new ArrayList();

    // 预定预计条款批次实体
    private HtlReservationBatch htlReservationBatch;

    private int dateNum;

    private int assuerItemOne;

    private int assuerItemTwo;

    private int assuerItemThree;

    private int assuerItemFour;

    private int assuerItemFive;

    private int onlyNum;

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

    /*
     * 常量星期
     */
    private final String pweeks = "1,2,3,4,5,6,7,";

    /*
     * 用于页面跳转的常量
     * 
     * @see com.mangocity.util.webwork.PersistenceAction#getEntityClass()
     */
    private String FORWARD;

    @Override
    protected Class getEntityClass() {

        return HtlPreconcertItemTemplet.class;

    }

    /**
     * 预定条款显示页面点单条款绑定去查询该酒店的预定条款模板，绑定在下拉列表中 add by haibo.li hotelV2.6 2009-2-6
     * 
     * @return
     */
    public String queryClauserModel() {
    	/*************************add by yong.zeng************************************/
    	String changePriceHint = "";
    	HtlHotel curhotel = hotelManage.findHotel(hotelId);
    	if(null!=curhotel){
        hotelChnName = curhotel.getChnName();
        
        changePriceHint = curhotel.getChangePriceHint();
    	}
        request.setAttribute("changePriceHint", changePriceHint);
        
        list = onlyManage.queryHotelModel(hotelId);
        // 获取该酒店下是否有预定模板，没有的话，跳转到提示页面；add by shengwei.zuo 2009-04-28 hotel V2.6
        /*
         * if(list==null||list.size()==0){
         * 
         * FORWARD="TmpltIsNull"; return FORWARD;
         * 
         * }
         */

        lstroomtype = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        // 获取该酒店下是否有价格类型，没有的话，跳转到提示页面；add by shengwei.zuo 2009-04-28 hotel V2.6
        if (null == lstroomtype || 0 == lstroomtype.size()) {

            FORWARD = "roomTypeIsNull";
            return FORWARD;

        }

        countNum = 0;
        lstContract = onlyManage.queryHtlContract(contractId);
        lstHtlBatch = onlyManage.queryHtlBatch(hotelId);// 取出批次表中的记录,在页面上有操作日志的显示
        for (int i = 0; i < lstContract.size(); i++) {
            HtlContract ht = lstContract.get(i);
            beginDate = ht.getBeginDate();
            endDate = ht.getEndDate();
        }
        FORWARD = "OnlyForwod";
        return FORWARD;
    }

    /**
     * 单条款页面查询预定条款模板 add by haibo.li HotelV2.6 2009-2-12
     * 
     * @return
     */
    public String queryModelAll() {
        super.setEntity(super.populateEntity());
        this.setPreconcert((HtlPreconcertItemTemplet) this.getEntity());
        Map params = super.getParams();

        list = onlyManage.queryHotelModel(hotelId);// 取出单个的预定条款

        lstContract = onlyManage.queryHtlContract(contractId);// 取出合同 得到合同的开始时间和结束时间 好在页面判断
        lstHtlBatch = onlyManage.queryHtlBatch(hotelId);// 取出批次表中的记录,在页面上有操作日志的显示
        for (int i = 0; i < lstContract.size(); i++) {
            HtlContract ht = lstContract.get(i);
            beginDate = ht.getBeginDate();// 合同开始时间
            endDate = ht.getEndDate();// 合同结束时间
        }

        modelAll = onlyManage.quertHotelModelAll(modelid, hotelId);// 还要取出所有的预定条款绑定在下拉列表

        lstroomtype = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);// 取出对应酒店的房型和价格类型

        htlBinding = MyBeanUtil.getBatchObjectFromParam(params, HtlBinding.class,
            onlyNum); // 拿的所有的日期一行的添加记录
        onlyNum = htlBinding.size();
        countNum = htlBinding.size();
        for (int i = 0; i < modelAll.size(); i++) {
            preconcert = new HtlPreconcertItemTemplet();
            preconcert = (HtlPreconcertItemTemplet) modelAll.get(i);
        }
        List lsthtlReservationTemplate = preconcert.getHtlReservationTemplateZ();
        List lstHtlAssureTemplate = preconcert.getHtlAssureTemplateZ();
        List lstHtlPrepayTemplate = preconcert.getHtlPrepayTemplateZ();
        // 初始化时得把可以添加删除的行的下标赋初始值,在页面就好判断
        for (int i = 0; i < lsthtlReservationTemplate.size(); i++) {
            HtlReservationTemplate htlReservationTemplate = (HtlReservationTemplate)
            lsthtlReservationTemplate
                .get(i);
            dateNum = htlReservationTemplate.getLisHtlReservContTemplate().size();
        }
        for (int i = 0; i < lstHtlAssureTemplate.size(); i++) {
            HtlAssureTemplate htlAssureTemplate = (HtlAssureTemplate) lstHtlAssureTemplate.get(i);
            assuerItemTwo = htlAssureTemplate.getLisHtlAssureItemTemplateTwo().size();
            assuerItemThree = htlAssureTemplate.getLisHtlAssureItemTemplateThree().size();
            assuerItemFour = htlAssureTemplate.getLisHtlAssureItemTemplateFour().size();
            assuerItemFive = htlAssureTemplate.getLisHtlAssureItemTemplateFive().size();
        }
        for (int i = 0; i < lstHtlPrepayTemplate.size(); i++) {
            HtlPrepayTemplate htlPrepayTemplate = (HtlPrepayTemplate) lstHtlPrepayTemplate.get(i);
            prepayItemTwo = htlPrepayTemplate.getLisHtlPrepayItemTemplateTwo().size();
            prepayItemThree = htlPrepayTemplate.getLisHtlPrepayItemTemplateThree().size();
            prepayItemFour = htlPrepayTemplate.getLisHtlPrepayItemTemplateFour().size();
            prepayItemFive = htlPrepayTemplate.getLisHtlPrepayItemTemplateFive().size();

        }

        FORWARD = "OnlyForwod";

        return FORWARD;
    }

    // 预览点返回方法
    public String back() {
        htlBinding = (List) super.getFromSession("htlBinding");// 从session中拿出对象 循环在页面中
        // 当htlBinding为1时返回的话开始日期等属性变为beginDate_1，若再次提交一个时间段，此时onlyNum必须大于0
        if (null != htlBinding && 1 == htlBinding.size() && 0 == onlyNum) {
            onlyNum += 1;
        }
        lstroomtype = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId); // 查的价格类型
        list = onlyManage.queryHotelModel(hotelId);
        lstContract = onlyManage.queryHtlContract(contractId);// 合同
        lstHtlBatch = onlyManage.queryHtlBatch(hotelId); // 批次表中操作记录
        for (int i = 0; i < lstContract.size(); i++) {
            HtlContract ht = lstContract.get(i);
            beginDate = ht.getBeginDate();// 开始时间
            endDate = ht.getEndDate();// 结束时间
        }
        super.getSession().remove("htlPreconcertItemBatch");// 清空session中的数据
        super.getSession().remove("htlBinding");

        return "OnlyForwod";
    }

    // 单条款预览方法 add haibo.li HotelV2.6
    public String review() {
        // 将表单数据存入到实体中。
        super.setEntity(super.populateEntity());
        this.setPreconcert((HtlPreconcertItemTemplet) this.getEntity());
        Map params = super.getParams();
        lstroomtype = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        htlPreconcertItemBatch = new HtlPreconcertItemBatch();
        List lsthtlReservationBatch = new ArrayList();
        List lsthtlReservContBatch = new ArrayList();
        // 预订条款的赋值

        lisHtlReservContTemplate = MyBeanUtil.getBatchObjectFromParam(params,
            HtlReservContTemplate.class, dateNum);
        // 增加从星期中筛选出来的必住日期 addby juesuchen V2.9.2 2009-7-30
        List<HtlReservContWeekBean> htlWeekBeans = MyBeanUtil.getBatchObjectFromParam(params,
            HtlReservContWeekBean.class, weekDateNum);
        
        /**
         * 判断预订条款是否有值,无值则不插入数据库
         */
        if ((null != aheadDay && !aheadDay.toString().equals(""))
            || (null != aheadTime && !aheadTime.equals(""))
            || (null != continueNights && !continueNights.equals(""))
            || (null != mustAheadDate && !mustAheadDate.toString().equals(""))
            || (null != mustFromDate && !"".equals(mustFromDate.toString()))
            || (null != mustToDate && !"".equals(mustToDate.toString()))
            || (null != mustFromTime && !"".equals(mustFromTime))
            || (null != mustToTime && !"".equals(mustToTime))
            || (null != maxRestrictNights && !maxRestrictNights.equals(""))
            || null != minRestrictNights && !minRestrictNights.equals("")
            || (null != mustAheadTime && !mustAheadTime.equals(""))
            || (!lisHtlReservContTemplate.isEmpty()) || 0 < htlWeekBeans.size()) {
        	boolean contTemple = false;
        	if(lisHtlReservContTemplate.size()>0){
        		HtlReservContTemplate contTemplate = (HtlReservContTemplate) lisHtlReservContTemplate.get(0);
        		if(null != contTemplate.getContinueDate())
        			contTemple = true;
        	}
            if (contTemple) {
                for (int i = 0; i < lisHtlReservContTemplate.size(); i++) {
                    HtlReservContBatch htlReservContBatch = new HtlReservContBatch();
                    HtlReservContTemplate htlReservContTemplate = 
                        (HtlReservContTemplate) lisHtlReservContTemplate
                        .get(i);
                    htlReservContBatch.setContinueDate(htlReservContTemplate.getContinueDate());
                    htlReservContBatch.setHtlReservationBatch(htlReservationBatch);
                    lsthtlReservContBatch.add(htlReservContBatch);
                }
            } else {// 增加必住星期
                for (HtlReservContWeekBean conWeekBean : htlWeekBeans) {
                    HtlReservContBatch htlReservContBatch = new HtlReservContBatch();
                    htlReservContBatch.setContinueDate(conWeekBean.getContinueWeekDateBegin());
                    htlReservContBatch.setContinueEndDate(conWeekBean.getContinueWeekDateEnd());
                    htlReservContBatch.setWeeks(conWeekBean.getStringContinueWeeks());
                    lsthtlReservContBatch.add(htlReservContBatch);
                    // conWeekBean.addHtlReservContDateByWeek(
                    // lsthtlReservContBatch,HtlReservContBatch.class);
                }
            }
            lisHtlReservContTemplate = lsthtlReservContBatch;
            // end
            htlReservationBatch = new HtlReservationBatch();
            htlReservationBatch.setHtlReservContBatch(lsthtlReservContBatch);
            htlReservationBatch.setAheadDay(aheadDay);
            htlReservationBatch.setAheadTime(aheadTime);
            htlReservationBatch.setContinueNights(continueNights);
            htlReservationBatch.setMaxRestrictNights(maxRestrictNights);
            htlReservationBatch.setMinRestrictNights(minRestrictNights);
            htlReservationBatch.setContinueDatesRelation(continueDatesRelation);// 增加必住关系
            htlReservationBatch.setMustAheadDate(mustAheadDate);
            /**
             * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 begin
             */
            htlReservationBatch.setMustFromDate(mustFromDate);
            htlReservationBatch.setMustToDate(mustToDate);
            htlReservationBatch.setMustFromTime(mustFromTime);
            htlReservationBatch.setMustToTime(mustToTime);
            /**
             * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 end
             */
            
            htlReservationBatch.setMustAheadTime(mustAheadTime);
            htlReservationBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);
            lsthtlReservationBatch.add(htlReservationBatch);
            htlPreconcertItemBatch.setHtlReservationBatch(lsthtlReservationBatch);

        }

        htlAssureBatch = new HtlAssureBatch();
        // 担保取消修改条款第一种.. scopeOne deductTypeOne percentageOne
        HtlAssureItemEverydayBatch htlAssureItemEverydayBatch = new HtlAssureItemEverydayBatch();
        htlAssureItemEverydayBatch.setScope(scopeOne);
        htlAssureItemEverydayBatch.setDeductType(deductTypeOne);
        htlAssureItemEverydayBatch.setPercentage(percentageOne);
        htlAssureItemEverydayBatch.setType(ClaueType.ASSURE_TYPE_ONE);
        htlAssureItemEverydayBatch.setHtlAssureBatch(htlAssureBatch);

        lsthtlAssureItemEverydayBatch.add(htlAssureItemEverydayBatch);

        // 第二种
        lisHtlAssureItemTemplateTwo = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateTwo.class, assuerItemTwo);
        for (int i = 0; i < lisHtlAssureItemTemplateTwo.size(); i++) {
            HtlAssureItemEverydayBatch htlAssureItemEverydayBatchtw = 
                new HtlAssureItemEverydayBatch();
            HtlAssureItemTemplateTwo htlAssureItemTemplateTwo = lisHtlAssureItemTemplateTwo.get(i);
            htlAssureItemEverydayBatchtw.setDeductType(htlAssureItemTemplateTwo.getDeductTypeTwo());
            htlAssureItemEverydayBatchtw.setScope(htlAssureItemTemplateTwo.getScopeTwo());
            htlAssureItemEverydayBatchtw.setFirstDateOrDays(htlAssureItemTemplateTwo
                .getFirstDateOrDaysTwo());
            htlAssureItemEverydayBatchtw.setFirstTime(htlAssureItemTemplateTwo.getFirstTimeTwo());
            htlAssureItemEverydayBatchtw.setSecondDateOrDays(htlAssureItemTemplateTwo
                .getSecondDateOrDaysTwo());
            htlAssureItemEverydayBatchtw.setSecondTime(htlAssureItemTemplateTwo.getSecondTimeTwo());
            htlAssureItemEverydayBatchtw.setPercentage(htlAssureItemTemplateTwo.getPercentageTwo());
            htlAssureItemEverydayBatchtw.setType(ClaueType.ASSURE_TYPE_TWO);
            htlAssureItemEverydayBatchtw.setHtlAssureBatch(htlAssureBatch);
            lsthtlAssureItemEverydayBatch.add(htlAssureItemEverydayBatchtw);
        }
        // 第三种
        lisHtlAssureItemTemplateThree = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateThree.class, assuerItemThree);
        for (int i = 0; i < lisHtlAssureItemTemplateThree.size(); i++) {
            HtlAssureItemEverydayBatch htlAssureItemEverydayBatchfo = 
                new HtlAssureItemEverydayBatch();
            HtlAssureItemTemplateThree htlAssureItemTemplateThree = lisHtlAssureItemTemplateThree
                .get(i);
            htlAssureItemEverydayBatchfo.setDeductType(htlAssureItemTemplateThree
                .getDeductTypeThree());
            htlAssureItemEverydayBatchfo.setScope(htlAssureItemTemplateThree.getScopeThree());
            htlAssureItemEverydayBatchfo.setFirstDateOrDays(htlAssureItemTemplateThree
                .getFirstDateOrDaysThree());
            htlAssureItemEverydayBatchfo.setFirstTime(htlAssureItemTemplateThree
                .getFirstTimeThree());
            htlAssureItemEverydayBatchfo.setSecondTime(htlAssureItemTemplateThree
                .getSecondTimeThree());
            htlAssureItemEverydayBatchfo.setPercentage(htlAssureItemTemplateThree
                .getPercentageThree());
            htlAssureItemEverydayBatchfo.setType(ClaueType.ASSURE_TYPE_THREE);
            htlAssureItemEverydayBatchfo.setHtlAssureBatch(htlAssureBatch);
            lsthtlAssureItemEverydayBatch.add(htlAssureItemEverydayBatchfo);

        }
        // 第四种
        lisHtlAssureItemTemplateFour = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateFour.class, assuerItemFour);
        for (int i = 0; i < lisHtlAssureItemTemplateFour.size(); i++) {
            HtlAssureItemEverydayBatch htlAssureItemTemplateEntiyFu = 
                new HtlAssureItemEverydayBatch();
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
            htlAssureItemTemplateEntiyFu.setHtlAssureBatch(htlAssureBatch);
            lsthtlAssureItemEverydayBatch.add(htlAssureItemTemplateEntiyFu);
        }
        // 第五种
        lisHtlAssureItemTemplateFive = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureItemTemplateFive.class, assuerItemFive);
        for (int i = 0; i < lisHtlAssureItemTemplateFive.size(); i++) {
            HtlAssureItemEverydayBatch htlAssureItemTemplateEntiyFv = 
                new HtlAssureItemEverydayBatch();
            HtlAssureItemTemplateFive htlAssureItemTemplateFive = lisHtlAssureItemTemplateFive
                .get(i);
            htlAssureItemTemplateEntiyFv.setDeductType(htlAssureItemTemplateFive
                .getDeductTypeFive());
            htlAssureItemTemplateEntiyFv.setScope(htlAssureItemTemplateFive.getScopeFive());
            htlAssureItemTemplateEntiyFv.setBeforeorafter(htlAssureItemTemplateFive
                .getBeforeOrAfterFive());
            htlAssureItemTemplateEntiyFv.setFirstTime(htlAssureItemTemplateFive.getFirstTimeFive());
            htlAssureItemTemplateEntiyFv.setPercentage(htlAssureItemTemplateFive
                .getPercentageFive());
            htlAssureItemTemplateEntiyFv.setType(ClaueType.ASSURE_TYPE_FIVE);
            htlAssureItemTemplateEntiyFv.setHtlAssureBatch(htlAssureBatch);
            lsthtlAssureItemEverydayBatch.add(htlAssureItemTemplateEntiyFv);
        }
        /**
         * 判断担保条款是否有值,无值则不插入数据库
         */
        if ((null != assureLetter && !assureLetter.equals(""))
            || (null != assureType && !assureType.equals(""))
            || (null != isNotConditional && !isNotConditional.equals(""))
            || (null != latestAssureTime && !latestAssureTime.equals(""))
            || (null != overRoomNumber && !overRoomNumber.toString().equals(""))
            || (null != overNightsNumber && !overNightsNumber.toString().equals(""))) {
            // 担保条款批次表把修改和取消list放进对象里面去
            htlAssureBatch.setHtlAssureItemEverydayBatch(lsthtlAssureItemEverydayBatch);
            htlAssureBatch.setAssureLetter(assureLetter);
            htlAssureBatch.setAssureType(assureType);
            htlAssureBatch.setIsNotConditional(isNotConditional);
            htlAssureBatch.setLatestAssureTime(latestAssureTime);
            htlAssureBatch.setOverRoomNumber(overRoomNumber);
            htlAssureBatch.setOverNightsNumber(overNightsNumber);
            htlAssureBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);
            List lsthtlAssureBatch = new ArrayList();
            lsthtlAssureBatch.add(htlAssureBatch);
            htlPreconcertItemBatch.setHtlAssureBatch(lsthtlAssureBatch);
        }

        htlPrepayEverydayBatch = new HtlPrepayEverydayBatch();// 预付条款批次实体
        // 预付条款修改和取消第一种
        HtlPrepayItemEverydayBatch htlPrepayItemEverydayBatch = new HtlPrepayItemEverydayBatch();
        htlPrepayItemEverydayBatch.setScope(scopePPOne);
        htlPrepayItemEverydayBatch.setDeductType(deductTypePPOne);
        htlPrepayItemEverydayBatch.setPercentage(percentagePPOne);
        htlPrepayItemEverydayBatch.setType(ClaueType.PREPAY_TYPE_ONE);
        htlPrepayItemEverydayBatch.setHtlPrepayEverydayBatch(htlPrepayEverydayBatch);
        htlPrepayItemEverydayBatchch.add(htlPrepayItemEverydayBatch);
        // 第二种
        lisHtlPrepayItemTemplateTwo = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateTwo.class, prepayItemTwo);
        for (int i = 0; i < lisHtlPrepayItemTemplateTwo.size(); i++) {
            HtlPrepayItemEverydayBatch htlPrepayItemEverydayBat = new HtlPrepayItemEverydayBatch();
            HtlPrepayItemTemplateTwo htlPrepayItemTemplateTwo = lisHtlPrepayItemTemplateTwo.get(i);
            htlPrepayItemEverydayBat.setDeductType(htlPrepayItemTemplateTwo.getDeductTypePPTwo());
            htlPrepayItemEverydayBat.setScope(htlPrepayItemTemplateTwo.getScopePPTwo());
            htlPrepayItemEverydayBat.setFirstDateOrDays(htlPrepayItemTemplateTwo
                .getFirstDateOrDaysPPTwo());
            htlPrepayItemEverydayBat.setFirstTime(htlPrepayItemTemplateTwo.getFirstTimePPTwo());
            htlPrepayItemEverydayBat.setSecondDateOrDays(htlPrepayItemTemplateTwo
                .getSecondDateOrDaysPPTwo());
            htlPrepayItemEverydayBat.setSecondTime(htlPrepayItemTemplateTwo.getSecondTimePPTwo());
            htlPrepayItemEverydayBat.setPercentage(htlPrepayItemTemplateTwo.getPercentagePPTwo());
            htlPrepayItemEverydayBat.setType(ClaueType.PREPAY_TYPE_TWO);
            htlPrepayItemEverydayBat.setHtlPrepayEverydayBatch(htlPrepayEverydayBatch);
            htlPrepayItemEverydayBatchch.add(htlPrepayItemEverydayBat);

        }
        // 第三种
        lisHtlPrepayItemTemplateThree = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateThree.class, prepayItemThree);
        for (int i = 0; i < lisHtlPrepayItemTemplateThree.size(); i++) {
            HtlPrepayItemEverydayBatch htlPrepayItemEverydayBat = new HtlPrepayItemEverydayBatch();
            HtlPrepayItemTemplateThree htlPrepayItemTemplateThree = lisHtlPrepayItemTemplateThree
                .get(i);
            htlPrepayItemEverydayBat.setDeductType(htlPrepayItemTemplateThree
                .getDeductTypePPThree());
            htlPrepayItemEverydayBat.setScope(htlPrepayItemTemplateThree.getScopePPThree());
            htlPrepayItemEverydayBat.setFirstDateOrDays(htlPrepayItemTemplateThree
                .getFirstDateOrDaysPPThree());
            htlPrepayItemEverydayBat.setFirstTime(htlPrepayItemTemplateThree.getFirstTimePPThree());
            htlPrepayItemEverydayBat.setSecondTime(htlPrepayItemTemplateThree
                .getSecondTimePPThree());
            htlPrepayItemEverydayBat.setPercentage(htlPrepayItemTemplateThree
                .getPercentagePPThree());
            htlPrepayItemEverydayBat.setType(ClaueType.PREPAY_TYPE_THREE);
            htlPrepayItemEverydayBat.setHtlPrepayEverydayBatch(htlPrepayEverydayBatch);
            htlPrepayItemEverydayBatchch.add(htlPrepayItemEverydayBat);

        }
        // 第四种
        lisHtlPrepayItemTemplateFour = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateFour.class, prepayItemFour);
        for (int i = 0; i < lisHtlPrepayItemTemplateFour.size(); i++) {
            HtlPrepayItemEverydayBatch htlPrepayItemEverydayBat = new HtlPrepayItemEverydayBatch();
            HtlPrepayItemTemplateFour htlPrepayItemTemplateFour = lisHtlPrepayItemTemplateFour
                .get(i);
            htlPrepayItemEverydayBat.setDeductType(htlPrepayItemTemplateFour.getDeductTypePPFour());
            htlPrepayItemEverydayBat.setScope(htlPrepayItemTemplateFour.getScopePPFour());
            htlPrepayItemEverydayBat.setFirstDateOrDays(htlPrepayItemTemplateFour
                .getFirstDateOrDaysPPFour());
            htlPrepayItemEverydayBat.setFirstTime(htlPrepayItemTemplateFour.getFirstTimePPFour());
            htlPrepayItemEverydayBat.setSecondDateOrDays(htlPrepayItemTemplateFour
                .getSecondDateOrDaysPPFour());
            htlPrepayItemEverydayBat.setSecondTime(htlPrepayItemTemplateFour.getSecondTimePPFour());
            htlPrepayItemEverydayBat.setPercentage(htlPrepayItemTemplateFour.getPercentagePPFour());
            htlPrepayItemEverydayBat.setType(ClaueType.PREPAY_TYPE_FORV);
            htlPrepayItemEverydayBat.setHtlPrepayEverydayBatch(htlPrepayEverydayBatch);
            htlPrepayItemEverydayBatchch.add(htlPrepayItemEverydayBat);
        }
        // 第五种
        lisHtlPrepayItemTemplateFive = MyBeanUtil.getBatchObjectFromParam(params,
            HtlPrepayItemTemplateFive.class, prepayItemFive);
        for (int i = 0; i < lisHtlPrepayItemTemplateFive.size(); i++) {
            HtlPrepayItemEverydayBatch htlPrepayItemEverydayBat = new HtlPrepayItemEverydayBatch();
            HtlPrepayItemTemplateFive htlPrepayItemTemplateFive = lisHtlPrepayItemTemplateFive
                .get(i);
            htlPrepayItemEverydayBat.setDeductType(htlPrepayItemTemplateFive.getDeductTypePPFive());
            htlPrepayItemEverydayBat.setScope(htlPrepayItemTemplateFive.getScopePPFive());
            htlPrepayItemEverydayBat.setBeforeorafter(htlPrepayItemTemplateFive
                .getBeforeOrAfterPPFive());
            htlPrepayItemEverydayBat.setFirstTime(htlPrepayItemTemplateFive.getFirstTimePPFive());
            htlPrepayItemEverydayBat.setPercentage(htlPrepayItemTemplateFive.getPercentagePPFive());
            htlPrepayItemEverydayBat.setType(ClaueType.PREPAY_TYPE_FIVE);
            htlPrepayItemEverydayBat.setHtlPrepayEverydayBatch(htlPrepayEverydayBatch);
            htlPrepayItemEverydayBatchch.add(htlPrepayItemEverydayBat);
        }
        /**
         * 判断预付条款是否有值,无值则不插入数据库
         */
        if ((null != amountType && !amountType.equals(""))
            || (null != balanceType && !balanceType.equals(""))
            || (null != daysAfterConfirm && !daysAfterConfirm.toString().equals(""))
            || (null != limitAheadDays && !limitAheadDays.toString().equals(""))
            || (null != limitAheadDaysTime && !limitAheadDaysTime.equals(""))
            || (null != limitDate && !limitDate.toString().equals(""))
            || (null != limitTime && !limitTime.equals(""))
            || (null != paymentType && !paymentType.equals(""))
            || (null != timeAfterConfirm && !timeAfterConfirm.equals(""))
            || (null != prepayDeductType && !prepayDeductType.equals(""))
            || (null != timeLimitType && !timeLimitType.equals(""))) {

            htlPrepayEverydayBatch.setHtlPrepayItemEverydayBatch(htlPrepayItemEverydayBatchch);
            htlPrepayEverydayBatch.setAmountType(amountType);
            htlPrepayEverydayBatch.setBalanceType(balanceType);
            htlPrepayEverydayBatch.setDaysAfterConfirm(daysAfterConfirm);
            htlPrepayEverydayBatch.setLimitAheadDays(limitAheadDays);
            htlPrepayEverydayBatch.setLimitAheadDaysTime(limitAheadDaysTime);
            htlPrepayEverydayBatch.setLimitDate(limitDate);
            htlPrepayEverydayBatch.setLimitTime(limitTime);
            htlPrepayEverydayBatch.setPaymentType(paymentType);
            htlPrepayEverydayBatch.setTimeAfterConfirm(timeAfterConfirm);
            htlPrepayEverydayBatch.setPrepaydebuctType(prepayDeductType);
            htlPrepayEverydayBatch.setTimelimitType(timeLimitType);
            htlPrepayEverydayBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);
            List lsthtlPrepayEverydayBatch = new ArrayList();
            lsthtlPrepayEverydayBatch.add(htlPrepayEverydayBatch);
            htlPreconcertItemBatch.setHtlPrepayEverydayBatch(lsthtlPrepayEverydayBatch);
        }

        // 设置预定条款批次表中的开始时间,结束时间,价格类型，面转预，是否绑定
        htlBinding = MyBeanUtil.getBatchObjectFromParam(params, HtlBinding.class, onlyNum);

        // 把实体放入session中
        super.putSession("htlBinding", htlBinding);
        super.putSession("htlPreconcertItemBatch", htlPreconcertItemBatch);
        return "review";
    }

    // 单条款保存方法
    @Override
    public String save() {

        // 获取不同的时间段； add by shengwei.zuo 2009-04-22
        htlBinding = (List) super.getFromSession("htlBinding");
        // 去除seesion中的值 add by shengwei.zuo 2009-04-22
        htlPreconcertItemBatch = (HtlPreconcertItemBatch) super
            .getFromSession("htlPreconcertItemBatch");

        for (int i = 0; i < htlBinding.size(); i++) {
            HtlBinding hb = htlBinding.get(i);
            for (int j = 0; j < hb.getPricetypes().length; j++) {
                /*
                 * 批次的相关实体；
                 */

                List<HtlAssureBatch> htlAssureBatchLstObj = new ArrayList<HtlAssureBatch>();

                List<HtlPrepayEverydayBatch> htlPrepayEverydayBatchLstObj = 
                    new ArrayList<HtlPrepayEverydayBatch>();

                List<HtlReservationBatch> htlReservationBatchLitObj = 
                    new ArrayList<HtlReservationBatch>();

                List<HtlAssureItemEverydayBatch> htlAssureBatchEveryLitObj = 
                    new ArrayList<HtlAssureItemEverydayBatch>();

                List<HtlPrepayItemEverydayBatch> htlPrepayEverydayBatchEveryLitObj =
                    new ArrayList<HtlPrepayItemEverydayBatch>();

                List<HtlReservContBatch> htlReservationBatchEveryLstObj = 
                    new ArrayList<HtlReservContBatch>();

                /*
                 * 批次session中的实体
                 */
                List<HtlAssureBatch> htlAssureBatchLitSesion = 
                    new ArrayList<HtlAssureBatch>();

                List<HtlPrepayEverydayBatch> htlPrepayEverydayBatchLitSession = 
                    new ArrayList<HtlPrepayEverydayBatch>();

                List<HtlReservationBatch> htlReservationBatchLitSession = 
                    new ArrayList<HtlReservationBatch>();

                List<HtlAssureItemEverydayBatch> htlAssureBatchEveryLitSession = 
                    new ArrayList<HtlAssureItemEverydayBatch>();

                List<HtlPrepayItemEverydayBatch> htlPrepayEverydayBatchEveryLitSession =
                    new ArrayList<HtlPrepayItemEverydayBatch>();

                List<HtlReservContBatch> htlReservationBatchEveryLitSession =
                    new ArrayList<HtlReservContBatch>();

                HtlPreconcertItemBatch tempHtlPreconcertItemBatch = 
                    new HtlPreconcertItemBatch();

                // 把session的值赋值到对象里面 add by shengwei.zuo 2009-04-22

                tempHtlPreconcertItemBatch.setAttention(attention);
                tempHtlPreconcertItemBatch.setBeginDate(hb.getBeginDate());
                tempHtlPreconcertItemBatch.setContractId(contractId);
                tempHtlPreconcertItemBatch.setDoubletofalg(ClaueType.CLAUS_ONLY);
                tempHtlPreconcertItemBatch.setEndDate(hb.getEndDate());
                tempHtlPreconcertItemBatch.setHotelId(hotelId);
                tempHtlPreconcertItemBatch.setPayToPrepay(hb.getPaytoprepay());
                if (null == hb.getWeeks()) {// 如果页面没有录入星期，则默认全选
                    hb.setWeeks(pweeks);
                    tempHtlPreconcertItemBatch.setWeeks(hb.getWeeks());// 设置星期
                } else {// 如果选择了星期,
                    tempHtlPreconcertItemBatch.setWeeks(hb.getWeeks());// 设置星期
                }

                if (null != hb.getPricetypes()[j] && !("".equals(hb.getPricetypes()[j]))) {
                    tempHtlPreconcertItemBatch.setPriceTypeId(Long.valueOf(hb.getPricetypes()[j]));
                }

                // 绑定的预定条款模板名称
                tempHtlPreconcertItemBatch.setReservationName(reservationName);
                // 绑定的预定条款模板ID
                tempHtlPreconcertItemBatch.setReservationTemplateId(modelid);

                if (hb.getTactive().equals(ClaueType.BINDING_TRUE)) {// 判断是否是绑定还是解除绑定
                    if (null != super.getOnlineRoleUser()) {
                        // 增加创建人..创建时间
                        tempHtlPreconcertItemBatch.setCreateBy(
                            super.getOnlineRoleUser().getName());// 创建人工号
                        tempHtlPreconcertItemBatch.setCreateById(super.getOnlineRoleUser()
                            .getLoginName());// 创建人名称
                        tempHtlPreconcertItemBatch.setCreateTime(DateUtil.getSystemDate()); // 创建时间

                        // add by shengwei.zuo 2009-04-14 用于操作日志的查询 begin
                        // 增加修改人，修改时间
                        tempHtlPreconcertItemBatch.setModifyBy(super.getOnlineRoleUser().getName());
                        tempHtlPreconcertItemBatch.setModifyById(super.getOnlineRoleUser()
                            .getLoginName());
                        tempHtlPreconcertItemBatch.setModifyTime(DateUtil.getSystemDate());

                        // add by shengwei.zuo 2009-04-14 用于操作日志的查询 end

                        tempHtlPreconcertItemBatch.setActive(ClaueType.BINDING_TRUE);// 是否绑定标志 1 为绑定
                    } else {// 如果拿不到登陆人信息,登陆的session失效,则跳到另外一个页面
                        super.getSession().remove("htlBinding");
                        super.getSession().remove("htlPreconcertItemBatch");
                        return super.forwardError("获取登陆用户信息失效,请重新登陆");

                    }
                } else if (hb.getTactive().equals(ClaueType.BINDING_FALSE)) {// 如果为删除绑定
                    if (null != super.getOnlineRoleUser()) {
                        tempHtlPreconcertItemBatch.setDelBy(
                            super.getOnlineRoleUser().getName());// 删除人工号
                        tempHtlPreconcertItemBatch.setDelById(super.getOnlineRoleUser()
                            .getLoginName());// 删除人名称
                        tempHtlPreconcertItemBatch.setDelTime(DateUtil.getSystemDate());// 删除时间
                        tempHtlPreconcertItemBatch.setActive(ClaueType.BINDING_FALSE);// 0为解除绑定

                        // add by shengwei.zuo 2009-04-14 用于操作日志的查询 begin
                        // 增加修改人，修改时间
                        tempHtlPreconcertItemBatch.setModifyBy(super.getOnlineRoleUser().getName());
                        tempHtlPreconcertItemBatch.setModifyById(super.getOnlineRoleUser()
                            .getLoginName());
                        tempHtlPreconcertItemBatch.setModifyTime(DateUtil.getSystemDate());

                        // add by shengwei.zuo 2009-04-14 用于操作日志的查询 end

                    } else {
                        super.getSession().remove("htlBinding");
                        super.getSession().remove("htlPreconcertItemBatch");
                        return super.forwardError("获取登陆用户信息失效,请重新登陆");
                    }
                }

                /*
                 * 向担保条款session中的值赋到实体对象中 add by shengwei.zuo 2009-04-22
                 */
                htlAssureBatchLitSesion = htlPreconcertItemBatch.getHtlAssureBatch();

                if (null != htlAssureBatchLitSesion && 0 < htlAssureBatchLitSesion.size()) {

                    HtlAssureBatch htlAssureBatchSession = htlAssureBatchLitSesion.get(0);

                    if (null != htlAssureBatchSession) {

                        HtlAssureBatch htlAssureBatchObj = new HtlAssureBatch();

                        // 当属性不为空时，
                        if ((null != htlAssureBatchSession.getAssureLetter() 
                            && !htlAssureBatchSession
                            .getAssureLetter().equals(""))
                            || (null != htlAssureBatchSession.getAssureType() 
                                && !htlAssureBatchSession
                                .getAssureType().equals(""))
                            || (null != htlAssureBatchSession.getIsNotConditional()
                                && !htlAssureBatchSession
                                .getIsNotConditional().equals(""))
                            || (null != htlAssureBatchSession.getLatestAssureTime() 
                                && !htlAssureBatchSession
                                .getLatestAssureTime().equals(""))
                                
                            || (!htlAssureBatchSession.getOverRoomNumber().toString().equals(""))
                            || (!htlAssureBatchSession.getOverNightsNumber().toString().equals(""))

                        ) {

                            htlAssureBatchObj.setAssureLetter(htlAssureBatchSession
                                .getAssureLetter());

                            htlAssureBatchObj.setAssureType(htlAssureBatchSession.getAssureType());

                            htlAssureBatchObj.setIsNotConditional(htlAssureBatchSession
                                .getIsNotConditional());

                            htlAssureBatchObj.setLatestAssureTime(htlAssureBatchSession
                                .getLatestAssureTime());

                            htlAssureBatchObj.setOverRoomNumber(htlAssureBatchSession
                                .getOverRoomNumber());

                            htlAssureBatchObj.setOverNightsNumber(htlAssureBatchSession
                                .getOverNightsNumber());

                            htlAssureBatchObj.setHtlPreconcertItemBatch(tempHtlPreconcertItemBatch);

                        }

                        htlAssureBatchEveryLitSession = htlAssureBatchSession
                            .getHtlAssureItemEverydayBatch();

                        for (int x = 0; x < htlAssureBatchEveryLitSession.size(); x++) {

                            HtlAssureItemEverydayBatch HtlAssureItemEverydayBatchSession = 
                                htlAssureBatchEveryLitSession
                                .get(x);

                            HtlAssureItemEverydayBatch htlAssureItemEverydayBatchObj = 
                                new HtlAssureItemEverydayBatch();

                            htlAssureItemEverydayBatchObj
                                .setBeforeorafter(HtlAssureItemEverydayBatchSession
                                    .getBeforeorafter());
                            htlAssureItemEverydayBatchObj
                                .setDeductType(HtlAssureItemEverydayBatchSession.getDeductType());
                            htlAssureItemEverydayBatchObj
                                .setFirstDateOrDays(HtlAssureItemEverydayBatchSession
                                    .getFirstDateOrDays());
                            htlAssureItemEverydayBatchObj
                                .setFirstTime(HtlAssureItemEverydayBatchSession.getFirstTime());

                            htlAssureItemEverydayBatchObj
                                .setPercentage(HtlAssureItemEverydayBatchSession.getPercentage());
                            htlAssureItemEverydayBatchObj
                                .setScope(HtlAssureItemEverydayBatchSession.getScope());
                            htlAssureItemEverydayBatchObj
                                .setSecondDateOrDays(HtlAssureItemEverydayBatchSession
                                    .getSecondDateOrDays());
                            htlAssureItemEverydayBatchObj
                                .setSecondTime(HtlAssureItemEverydayBatchSession.getSecondTime());
                            htlAssureItemEverydayBatchObj.setType(HtlAssureItemEverydayBatchSession
                                .getType());

                            htlAssureItemEverydayBatchObj.setHtlAssureBatch(htlAssureBatchObj);

                            htlAssureBatchEveryLitObj.add(htlAssureItemEverydayBatchObj);

                        }

                        htlAssureBatchObj.setHtlAssureItemEverydayBatch(htlAssureBatchEveryLitObj);

                        htlAssureBatchLstObj.add(htlAssureBatchObj);

                    }

                }

                /*
                 * 向预付条款session中的值赋给实体对象里 add by shengwei.zuo 2009-04-22
                 */
                htlPrepayEverydayBatchLitSession = htlPreconcertItemBatch
                    .getHtlPrepayEverydayBatch();

                if (null != htlPrepayEverydayBatchLitSession
                    && 0 < htlPrepayEverydayBatchLitSession.size()) {

                    HtlPrepayEverydayBatch htlPrepayEverydayBatchSession = 
                        htlPrepayEverydayBatchLitSession
                        .get(0);

                    if (null != htlPrepayEverydayBatchSession) {

                        HtlPrepayEverydayBatch htlPrepayEverydayBatchObj = 
                            new HtlPrepayEverydayBatch();
                        // 当属性不为空时，
                        if ((null != htlPrepayEverydayBatchSession.getAmountType()
                            && !htlPrepayEverydayBatchSession
                            .getAmountType().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getBalanceType() 
                                && !htlPrepayEverydayBatchSession
                                .getBalanceType().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getDaysAfterConfirm() 
                                && !htlPrepayEverydayBatchSession
                                .getDaysAfterConfirm().toString().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getLimitAheadDays() 
                                && !htlPrepayEverydayBatchSession
                                .getLimitAheadDays().toString().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getLimitAheadDaysTime()
                                && !htlPrepayEverydayBatchSession
                                .getLimitAheadDaysTime().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getLimitDate() 
                                && !htlPrepayEverydayBatchSession
                                .getLimitDate().toString().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getLimitTime() 
                                && !htlPrepayEverydayBatchSession
                                .getLimitTime().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getPaymentType() 
                                && !htlPrepayEverydayBatchSession
                                .getPaymentType().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getTimeAfterConfirm()
                                && !htlPrepayEverydayBatchSession
                                .getTimeAfterConfirm().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getPrepaydebuctType()
                                && !htlPrepayEverydayBatchSession
                                .getPrepaydebuctType().equals(""))
                            || (null != htlPrepayEverydayBatchSession.getTimelimitType() 
                                && !htlPrepayEverydayBatchSession
                                .getTimelimitType().equals(""))

                        ) {

                            htlPrepayEverydayBatchObj.setAmountType(htlPrepayEverydayBatchSession
                                .getAmountType());
                            htlPrepayEverydayBatchObj.setBalanceType(htlPrepayEverydayBatchSession
                                .getBalanceType());
                            htlPrepayEverydayBatchObj
                                .setDaysAfterConfirm(htlPrepayEverydayBatchSession
                                    .getDaysAfterConfirm());
                            htlPrepayEverydayBatchObj
                                .setLimitAheadDays(htlPrepayEverydayBatchSession
                                    .getLimitAheadDays());
                            htlPrepayEverydayBatchObj
                                .setLimitAheadDaysTime(htlPrepayEverydayBatchSession
                                    .getLimitAheadDaysTime());
                            htlPrepayEverydayBatchObj.setLimitDate(htlPrepayEverydayBatchSession
                                .getLimitDate());
                            htlPrepayEverydayBatchObj.setLimitTime(htlPrepayEverydayBatchSession
                                .getLimitTime());
                            htlPrepayEverydayBatchObj.setPaymentType(htlPrepayEverydayBatchSession
                                .getPaymentType());
                            htlPrepayEverydayBatchObj
                                .setTimeAfterConfirm(htlPrepayEverydayBatchSession
                                    .getTimeAfterConfirm());
                            htlPrepayEverydayBatchObj
                                .setPrepaydebuctType(htlPrepayEverydayBatchSession
                                    .getPrepaydebuctType());
                            htlPrepayEverydayBatchObj
                                .setTimelimitType(htlPrepayEverydayBatchSession.getTimelimitType());

                            htlPrepayEverydayBatchObj
                                .setHtlPreconcertItemBatch(tempHtlPreconcertItemBatch);

                        }

                        htlPrepayEverydayBatchEveryLitSession = htlPrepayEverydayBatchSession
                            .getHtlPrepayItemEverydayBatch();

                        for (int y = 0; y < htlPrepayEverydayBatchEveryLitSession.size(); y++) {

                            HtlPrepayItemEverydayBatch htlPrepayItemEverydayBatchSession = 
                                htlPrepayEverydayBatchEveryLitSession
                                .get(y);

                            HtlPrepayItemEverydayBatch htlPrepayItemEverydayBatchObj = 
                                new HtlPrepayItemEverydayBatch();

                            htlPrepayItemEverydayBatchObj
                                .setBeforeorafter(htlPrepayItemEverydayBatchSession
                                    .getBeforeorafter());
                            htlPrepayItemEverydayBatchObj
                                .setDeductType(htlPrepayItemEverydayBatchSession.getDeductType());
                            htlPrepayItemEverydayBatchObj
                                .setFirstDateOrDays(htlPrepayItemEverydayBatchSession
                                    .getFirstDateOrDays());
                            htlPrepayItemEverydayBatchObj
                                .setFirstTime(htlPrepayItemEverydayBatchSession.getFirstTime());

                            htlPrepayItemEverydayBatchObj
                                .setPercentage(htlPrepayItemEverydayBatchSession.getPercentage());
                            htlPrepayItemEverydayBatchObj
                                .setScope(htlPrepayItemEverydayBatchSession.getScope());
                            htlPrepayItemEverydayBatchObj
                                .setSecondDateOrDays(htlPrepayItemEverydayBatchSession
                                    .getSecondDateOrDays());
                            htlPrepayItemEverydayBatchObj
                                .setSecondTime(htlPrepayItemEverydayBatchSession.getSecondTime());
                            htlPrepayItemEverydayBatchObj.setType(htlPrepayItemEverydayBatchSession
                                .getType());

                            htlPrepayItemEverydayBatchObj
                                .setHtlPrepayEverydayBatch(htlPrepayEverydayBatchObj);

                            htlPrepayEverydayBatchEveryLitObj.add(htlPrepayItemEverydayBatchObj);

                        }

                        htlPrepayEverydayBatchObj
                            .setHtlPrepayItemEverydayBatch(htlPrepayEverydayBatchEveryLitObj);

                        htlPrepayEverydayBatchLstObj.add(htlPrepayEverydayBatchObj);

                    }

                }

                /*
                 * 向预订条款session中的值赋到实体对象里面
                 */

                htlReservationBatchLitSession = htlPreconcertItemBatch.getHtlReservationBatch();

                if (null != htlReservationBatchLitSession
                    && 0 < htlReservationBatchLitSession.size()) {

                    HtlReservationBatch htlReservationBatchSession = htlReservationBatchLitSession
                        .get(0);

                    if (null != htlReservationBatchSession) {

                        HtlReservationBatch htlReservationBatchObj = new HtlReservationBatch();

                        htlReservationBatchEveryLitSession = htlReservationBatchSession
                            .getHtlReservContBatch();

                        // 当属性不为空时
                        if ((null != htlReservationBatchSession.getAheadDay() 
                            && !htlReservationBatchSession
                            .getAheadDay().toString().equals(""))
                            || (null != htlReservationBatchSession.getAheadTime()
                                && !htlReservationBatchSession
                                .getAheadTime().equals(""))
                            || (null != htlReservationBatchSession.getContinueNights() 
                                && !htlReservationBatchSession
                                .getContinueNights().equals(""))
                            || (null != htlReservationBatchSession.getMustAheadDate()
                                && !htlReservationBatchSession
                                .getMustAheadDate().toString().equals(""))
                                
                            || (null != htlReservationBatchSession.getMustFromDate()
                                && !htlReservationBatchSession
                                .getMustFromDate().toString().equals(""))    
                            || (null != htlReservationBatchSession.getMustToDate()
                                && !htlReservationBatchSession
                                .getMustToDate().toString().equals(""))     
                            || (null != htlReservationBatchSession.getMustFromTime()
                                && !htlReservationBatchSession
                                .getMustFromTime().equals("")) 
                            || (null != htlReservationBatchSession.getMustToTime()
                                && !htlReservationBatchSession
                                .getMustToTime().equals("")) 
                                
                            || null != htlReservationBatchSession.getMaxRestrictNights()
                            && !htlReservationBatchSession.getMaxRestrictNights().equals("")
                            || null != htlReservationBatchSession.getMinRestrictNights()
                            && !htlReservationBatchSession.getMinRestrictNights().equals("")
                            || (null != htlReservationBatchSession.getMustAheadTime() 
                                && !htlReservationBatchSession
                                .getMustAheadTime().equals(""))
                            || (!htlReservationBatchEveryLitSession.isEmpty())) {

                            for (int z = 0; z < htlReservationBatchEveryLitSession.size(); z++) {

                                HtlReservContBatch HtlReservContBatchSession = 
                                    htlReservationBatchEveryLitSession
                                    .get(z);
                                HtlReservContBatch htlReservContBatchObj = new HtlReservContBatch();
                                htlReservContBatchObj.setContinueDate(HtlReservContBatchSession
                                    .getContinueDate());
                                htlReservContBatchObj.setContinueEndDate(HtlReservContBatchSession
                                    .getContinueEndDate());
                                htlReservContBatchObj
                                    .setWeeks(HtlReservContBatchSession.getWeeks());
                                htlReservContBatchObj.setHtlReservationBatch(htlReservationBatch);
                                htlReservationBatchEveryLstObj.add(htlReservContBatchObj);
                            }

                            htlReservationBatchObj.setAheadDay(htlReservationBatchSession
                                .getAheadDay());
                            htlReservationBatchObj.setAheadTime(htlReservationBatchSession
                                .getAheadTime());

                            // 增加必住日期的关系选择and or
                            continueDatesRelation = htlReservationBatchSession
                                .getContinueDatesRelation();
                            htlReservationBatchObj.setContinueDatesRelation(continueDatesRelation);

                            if (null != htlReservationBatchSession.getContinueNights()) {
                                htlReservationBatchObj.setContinueNights(htlReservationBatchSession
                                    .getContinueNights());
                            }
                            if (null != htlReservationBatchSession.getMaxRestrictNights()) {
                                htlReservationBatchObj
                                    .setMaxRestrictNights(htlReservationBatchSession
                                        .getMaxRestrictNights());
                            }
                            if (null != htlReservationBatchSession.getMinRestrictNights()) {
                                htlReservationBatchObj
                                    .setMinRestrictNights(htlReservationBatchSession
                                        .getMinRestrictNights());
                            }

                            htlReservationBatchObj.setMustAheadDate(htlReservationBatchSession
                                .getMustAheadDate());
                            
                            /**
                             * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 begin
                             */
                            htlReservationBatchObj.setMustFromDate(htlReservationBatchSession
                                    .getMustFromDate());
                            htlReservationBatchObj.setMustFromTime(htlReservationBatchSession
                                    .getMustFromTime());
                            htlReservationBatchObj.setMustToDate(htlReservationBatchSession
                                    .getMustToDate());
                            htlReservationBatchObj.setMustToTime(htlReservationBatchSession
                                    .getMustToTime());
                            /**
                             * add by shengwei.zuo 预定条款的时限 新增时间段的的预订规则 2009-09-03 end
                             */
                            
                            htlReservationBatchObj.setMustAheadTime(htlReservationBatchSession
                                .getMustAheadTime());

                            htlReservationBatchObj
                                .setHtlPreconcertItemBatch(tempHtlPreconcertItemBatch);
                        }

                        htlReservationBatchObj
                            .setHtlReservContBatch(htlReservationBatchEveryLstObj);

                        htlReservationBatchLitObj.add(htlReservationBatchObj);

                    }
                }

                tempHtlPreconcertItemBatch.setHtlAssureBatch(htlAssureBatchLstObj);
                tempHtlPreconcertItemBatch.setHtlPrepayEverydayBatch(htlPrepayEverydayBatchLstObj);
                tempHtlPreconcertItemBatch.setHtlReservationBatch(htlReservationBatchLitObj);

                try {

                    boolean sufalg = onlyManage.saveOrUpdateClause(tempHtlPreconcertItemBatch);

                    long id = tempHtlPreconcertItemBatch.getId();

                    if (sufalg) {

                    	log.info("单条款插入批次表中，成功:"
								+ tempHtlPreconcertItemBatch.getModify_time()
								+ "," + hotelId + "," + id + "," + ","
								+ contractId + "," + hb.getPricetypes()[j]
								+ "," + hb.getBeginDate() + ","
								+ hb.getEndDate() + "," + hb.getTactive());
                        boolean fals = onlyManage.saveOrUpdateClausePro(hotelId, id, contractId, hb
                            .getPricetypes()[j], hb.getBeginDate(), hb.getEndDate(), hb
                            .getTactive());
                        
                        if (fals) {
                        	log.info("单条款调用存储过程,往批次表中拿到数据再插入每天表，成功"+tempHtlPreconcertItemBatch.getModify_time());
                            // 保存成功后清除session
                            super.getSession().put("htlPreconcertItemBatch", null);
                        }else{
                        	log.info("单条款调用存储过程,往批次表中拿到数据再插入每天表，失败"+tempHtlPreconcertItemBatch.getModify_time());
                        }
                    } else {
                    	log.info("单条款插入批次表中，失败"+tempHtlPreconcertItemBatch.getModify_time());
                        // 保存不成功后清除session
                        super.getSession().put("htlPreconcertItemBatch", null);
                        super.forwardError("插入失败");
                    }

                } catch (Exception e) {
                	log.error(e.getMessage(),e);
                    super.forwardError("插入失败");
                }

            }
        }

        super.getSession().put("htlBinding", null);
        super.getSession().put("htlPreconcertItemBatch", null);

        return "SuccessForword";
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public long getModelid() {
        return modelid;
    }

    public void setModelid(long modelid) {
        this.modelid = modelid;
    }

    public List getLstroomtype() {
        return lstroomtype;
    }

    public void setLstroomtype(List lstroomtype) {
        this.lstroomtype = lstroomtype;
    }

    public List getModelAll() {
        return modelAll;
    }

    public void setModelAll(List modelAll) {
        this.modelAll = modelAll;
    }

    public HtlPreconcertItemTemplet getPreconcert() {
        return preconcert;
    }

    public void setPreconcert(HtlPreconcertItemTemplet preconcert) {
        this.preconcert = preconcert;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
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

    public int getAssuerItemOne() {
        return assuerItemOne;
    }

    public void setAssuerItemOne(int assuerItemOne) {
        this.assuerItemOne = assuerItemOne;
    }

    public int getAssuerItemThree() {
        return assuerItemThree;
    }

    public void setAssuerItemThree(int assuerItemThree) {
        this.assuerItemThree = assuerItemThree;
    }

    public int getAssuerItemTwo() {
        return assuerItemTwo;
    }

    public void setAssuerItemTwo(int assuerItemTwo) {
        this.assuerItemTwo = assuerItemTwo;
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

    public String getContinueNights() {
        return continueNights;
    }

    public void setContinueNights(String continueNights) {
        this.continueNights = continueNights;
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

    public int getDateNum() {
        return dateNum;
    }

    public void setDateNum(int dateNum) {
        this.dateNum = dateNum;
    }

    public Long getDaysAfterConfirm() {
        return daysAfterConfirm;
    }

    public void setDaysAfterConfirm(Long daysAfterConfirm) {
        this.daysAfterConfirm = daysAfterConfirm;
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

    public List<HtlAssureItemTemplate> getLisHtlAssureItemTemplate() {
        return lisHtlAssureItemTemplate;
    }

    public void setLisHtlAssureItemTemplate(List<HtlAssureItemTemplate> lisHtlAssureItemTemplate) {
        this.lisHtlAssureItemTemplate = lisHtlAssureItemTemplate;
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

    public List<HtlPrepayItemTemplate> getLisHtlPrepayItemTemplate() {
        return lisHtlPrepayItemTemplate;
    }

    public void setLisHtlPrepayItemTemplate(List<HtlPrepayItemTemplate> lisHtlPrepayItemTemplate) {
        this.lisHtlPrepayItemTemplate = lisHtlPrepayItemTemplate;
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

    public List getLisHtlReservContTemplate() {
        return lisHtlReservContTemplate;
    }

    public void setLisHtlReservContTemplate(List lisHtlReservContTemplate) {
        this.lisHtlReservContTemplate = lisHtlReservContTemplate;
    }

    public List getLsmustDate() {
        return lsmustDate;
    }

    public void setLsmustDate(List lsmustDate) {
        this.lsmustDate = lsmustDate;
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

    public int getPrepayItemOne() {
        return prepayItemOne;
    }

    public void setPrepayItemOne(int prepayItemOne) {
        this.prepayItemOne = prepayItemOne;
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

    public String getTermsChoose() {
        return termsChoose;
    }

    public void setTermsChoose(String termsChoose) {
        this.termsChoose = termsChoose;
    }

    public String getTimeAfterConfirm() {
        return timeAfterConfirm;
    }

    public void setTimeAfterConfirm(String timeAfterConfirm) {
        this.timeAfterConfirm = timeAfterConfirm;
    }

    public HtlAssureBatch getHtlAssureBatch() {
        return htlAssureBatch;
    }

    public void setHtlAssureBatch(HtlAssureBatch htlAssureBatch) {
        this.htlAssureBatch = htlAssureBatch;
    }

    public HtlPreconcertItemBatch getHtlPreconcertItemBatch() {
        return htlPreconcertItemBatch;
    }

    public void setHtlPreconcertItemBatch(HtlPreconcertItemBatch htlPreconcertItemBatch) {
        this.htlPreconcertItemBatch = htlPreconcertItemBatch;
    }

    public HtlPrepayEverydayBatch getHtlPrepayEverydayBatch() {
        return htlPrepayEverydayBatch;
    }

    public void setHtlPrepayEverydayBatch(HtlPrepayEverydayBatch htlPrepayEverydayBatch) {
        this.htlPrepayEverydayBatch = htlPrepayEverydayBatch;
    }

    public HtlReservationBatch getHtlReservationBatch() {
        return htlReservationBatch;
    }

    public void setHtlReservationBatch(HtlReservationBatch htlReservationBatch) {
        this.htlReservationBatch = htlReservationBatch;
    }

    public List<HtlBinding> getHtlBinding() {
        return htlBinding;
    }

    public void setHtlBinding(List<HtlBinding> htlBinding) {
        this.htlBinding = htlBinding;
    }

    public int getOnlyNum() {
        return onlyNum;
    }

    public void setOnlyNum(int onlyNum) {
        this.onlyNum = onlyNum;
    }

    public String getDeductTypeOne() {
        return deductTypeOne;
    }

    public void setDeductTypeOne(String deductTypeOne) {
        this.deductTypeOne = deductTypeOne;
    }

    public String getPercentageOne() {
        return percentageOne;
    }

    public void setPercentageOne(String percentageOne) {
        this.percentageOne = percentageOne;
    }

    public String getScopeOne() {
        return scopeOne;
    }

    public void setScopeOne(String scopeOne) {
        this.scopeOne = scopeOne;
    }

    public List<HtlAssureItemEverydayBatch> getLsthtlAssureItemEverydayBatch() {
        return lsthtlAssureItemEverydayBatch;
    }

    public void setLsthtlAssureItemEverydayBatch(
        List<HtlAssureItemEverydayBatch> lsthtlAssureItemEverydayBatch) {
        this.lsthtlAssureItemEverydayBatch = lsthtlAssureItemEverydayBatch;
    }

    public String getDeductTypePPOne() {
        return deductTypePPOne;
    }

    public void setDeductTypePPOne(String deductTypePPOne) {
        this.deductTypePPOne = deductTypePPOne;
    }

    public String getPercentagePPOne() {
        return percentagePPOne;
    }

    public void setPercentagePPOne(String percentagePPOne) {
        this.percentagePPOne = percentagePPOne;
    }

    public String getScopePPOne() {
        return scopePPOne;
    }

    public void setScopePPOne(String scopePPOne) {
        this.scopePPOne = scopePPOne;
    }

    public List<HtlPrepayItemEverydayBatch> getHtlPrepayItemEverydayBatchch() {
        return htlPrepayItemEverydayBatchch;
    }

    public void setHtlPrepayItemEverydayBatchch(
        List<HtlPrepayItemEverydayBatch> htlPrepayItemEverydayBatchch) {
        this.htlPrepayItemEverydayBatchch = htlPrepayItemEverydayBatchch;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OnlyClauserManage getOnlyManage() {
        return onlyManage;
    }

    public void setOnlyManage(OnlyClauserManage onlyManage) {
        this.onlyManage = onlyManage;
    }

    public static int getCountNum() {
        return countNum;
    }

    public static void setCountNum(int countNum) {
        OnlyClauserAction.countNum = countNum;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<HtlContract> getLstContract() {
        return lstContract;
    }

    public void setLstContract(List<HtlContract> lstContract) {
        this.lstContract = lstContract;
    }

    public String getPrepayDeductType() {
        return prepayDeductType;
    }

    public void setPrepayDeductType(String prepayDeductType) {
        this.prepayDeductType = prepayDeductType;
    }

    public String getTimeLimitType() {
        return timeLimitType;
    }

    public void setTimeLimitType(String timeLimitType) {
        this.timeLimitType = timeLimitType;
    }

    public List getLstHtlBatch() {
        return lstHtlBatch;
    }

    public void setLstHtlBatch(List lstHtlBatch) {
        this.lstHtlBatch = lstHtlBatch;
    }

    public String getFORWARD() {
        return FORWARD;
    }

    public void setFORWARD(String forward) {
        FORWARD = forward;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getMaxRestrictNights() {
        return maxRestrictNights;
    }

    public void setMaxRestrictNights(String maxRestrictNights) {
        this.maxRestrictNights = maxRestrictNights;
    }

    public String getMinRestrictNights() {
        return minRestrictNights;
    }

    public void setMinRestrictNights(String minRestrictNights) {
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

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
