package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IPriceDao;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.manage.assistant.InputPrice;
import com.mangocity.hotel.base.persistence.HtlBatchMtnPrice;
import com.mangocity.hotel.base.persistence.HtlBatchSalePrice;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.resource.impl.BaseDataResourceDescriptor;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.ObjectBean;
import com.mangocity.util.log.MyLog;

/**
 * 调价Action
 * 
 * @author zhengxin
 * 
 */
@SuppressWarnings("unchecked")
public class AdjustPriceAction extends HotelBatchAction {
	
	private static final MyLog log = MyLog.getLogger(AdjustPriceAction.class);
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String BATCH_INPUT = "batchInput";

    private static final String BATCH_FORUPDATE = "batchForUpdate";

    private static final String BATCH_QUERY = "batchQuery";

    private static final String CONFIRM = "confirm";

    private static final String ADJUST = "adjust";

    private static final String SAVE_SUCCESS = "save";

    private static final String BATCHQUERYFORUPDATE = "batchQueryForUpdate";

    private IPriceDao priceDao;

    private HotelManage hotelManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private HtlHotel hotel;

    private List lstHoliday;

    private String htlWeek;

    private String htlRoomType;

    private List lstQueryDate;

    private int rowCount;

    /**
     * 显示合同的开始日期，只做显示用
     */
    private String bD;

    /**
     * 显示合同的结束日期，只做显示用
     */
    private String eD;

    /**
     * 录入价
     */
    private InputPrice inputPrice;
    
    private String hotelChnName;

    /**
     * 查询返回房间列表
     */
    private List lstRooms;

    // 价格行数
    private int priceRowNum;

    // 价格数据
    private List priceList;

    private IPriceManage priceManage;

    private String[] priceTypeIds;

    private String[] roomTypeIdStr;

    private List lstRoomType;

    private List lstPrice;

    /**
     * 从页面传入参数
     */
    private List lstDate;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 支付方式
     */
    private String payMethodForQurey;

    /**
     * 服务费率
     */
    private double serviceCharge;

    private String currencyS;

    /**
     *查询日期 ---修改价格批次用
     */

    private Date start;

    /**
     *查询日期 ---修改价格批次用
     */

    private Date end;

    private long roomTypeId;

    private String childRoomTypeId;

    private String strPayMethodS;

    private String updateFlag;

    private List lstBreakfastType;

    private List lstBreakfastNum;

    private List lstHtlBatchMtnPrice;

    private List roomTypePriceTypeLis;
    
    private ResourceManager resourceManager;

    // 专门只存储在页面上显示最晚和最早日期
    private List<HtlBatchSalePrice> lasAndEaList;

    /**
     * 保持前面页面的调价提示
     */
    private String changePriceHint;

    //@SuppressWarnings("unchecked")
	public String adjust() {
        super.clearSession();
        // 取得酒店信息
        if (null != hotelId) {
            hotel = hotelManage.findHotel(hotelId);
            this.setHotel(hotel);
            lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
            roomTypePriceTypeLis = hotelManage.findRoomTypePriceTypeLis(hotelId);
        }

        if (null != contractId) {
            HtlContract ct = hotelManage.findContract(contractId);
            this.setServiceCharge(ct.getServiceCharge());
            setCurrency(ct.getCurrency());
            super.putSession("currency", ct.getCurrency());
        }
        // 获取早餐类型和早餐数量
        Map<String, String> mapTypeObj = new HashMap<String, String>(1);
        mapTypeObj.put("type", "breakfastType");
        Map mapBreakfastType = new BaseDataResourceDescriptor().loadFromDatabaseT("breakfast_type", mapTypeObj);

        if( mapBreakfastType.size() > 0) {
        	lstBreakfastType = new ArrayList(mapBreakfastType.size());
	        for(Object objType:mapBreakfastType.keySet()){
	            ObjectBean ob = new ObjectBean();
	            ob.setName(objType.toString());
	            ob.setValue(mapBreakfastType.get(objType).toString());
	            lstBreakfastType.add(ob);
	        }
        }

        Map mapBreakfastNum = resourceManager.getDescription("breakfast_num_new");
        if( mapBreakfastNum.size() > 0) {
        	lstBreakfastNum = new ArrayList(mapBreakfastNum.size());
        	for(Object objNum : mapBreakfastNum.keySet()){
	            ObjectBean ob = new ObjectBean();
	            ob.setName(objNum.toString());
	            ob.setValue(mapBreakfastNum.get(objNum).toString());
	            lstBreakfastNum.add(ob);
	        }
        	
        	//对拿出来的早餐进行排序-1、1、2、3...
            lstBreakfastNum = sortBreakfastNum(lstBreakfastNum);
        }        

        super.putSession("lstBreakfastType", lstBreakfastType);
        super.putSession("lstBreakfastNum", lstBreakfastNum);
        changePriceHint = hotel.getChangePriceHint();
        // super.putSession("changePriceHint", hotel.getChangePriceHint());
        this.hotelChnName = hotel.getChnName();

        // 加历史录入最早与最晚日期 add by zhineng.zhuang
        lasAndEaList = priceManage.getAlreadyLatestAndEarliest(contractId);
        return ADJUST;
    }

    /**
     * 批量录入价格数据，并调用价格计算器，对录入的数据计算，并返回计算结果
     * 
     * @return
     */
    public String batchInput() {

        if ((null == hotelId) || (null == contractId || null == currencyS))
            return super.forwardError("hotelId,合同id,currency不能为空！");
        hotel = hotelManage.findHotel(hotelId);
        if (null != hotel) {
            changePriceHint = hotel.getChangePriceHint();
        }
        Map params = super.getParams();

        // 时间段
        dateList = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, dateRowNum);

        // 价格
        priceList = MyBeanUtil.getBatchObjectFromParam(params, InputPrice.class, priceRowNum);
        // 星期

        for (Object priceObj: priceList) {
        	InputPrice price = (InputPrice)priceObj;
            price.setQuotaType(quotaType);
            price.setFormulaId(formulaId);
            price.setCurrency(currencyS);
            price.setServiceCharge(serviceCharge);
            price.setHotelId(hotelId);
        }

        super.putSession("dateList", dateList);
        super.putSession("priceList", priceList);

        /**
         * 计算价格
         */
        priceManage.calculatePrice(priceList, contractId);

        // 周变成字符串
        StringBuilder weekBuilder = new StringBuilder();
        if (null != week && 0 < week.length) {
            for (int m = 0; m < week.length; m++) {
            	weekBuilder.append(String.valueOf(week[m]));
            	weekBuilder.append(",");
            }
        }
        
        this.strWeek = weekBuilder.toString();
        return BATCH_INPUT;
    }

    public String backToAdjustPrice() {

        // super.clearSession();
        // 取得酒店信息
        if (null != hotelId) {
            hotel = hotelManage.findHotel(hotelId);
            this.setHotel(hotel);
            lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
            roomTypePriceTypeLis = hotelManage.findRoomTypePriceTypeLis(hotelId);
            this.changePriceHint = hotel.getChangePriceHint();
        }

        if (null != contractId) {
            HtlContract ct = hotelManage.findContract(contractId);
            this.setServiceCharge(ct.getServiceCharge());
            lasAndEaList = priceManage.getAlreadyLatestAndEarliest(contractId);
        }
        // super.getSession().remove("priceList");
        return "backToAdjustPrice";
    }
    

    public String adjustForBreakfast() {
        super.clearSession();
        // 取得酒店信息
        if (null != hotelId) {
            hotel = hotelManage.findHotel(hotelId);
            changePriceHint = hotel.getChangePriceHint();
            this.setHotel(hotel);
            lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        }

        if (null != contractId) {
            HtlContract ct = hotelManage.findContract(contractId);
            this.setServiceCharge(ct.getServiceCharge());
            this.setCurrency(ct.getCurrency());
            super.putSession("currency", ct.getCurrency());
        }

        // this.changePriceHint=hotel.getChangePriceHint();
        // super.putSession("changePriceHint", hotel.getChangePriceHint());
        return "adjustForBreakfast";
    }

    public String backToAdjustBreakfast() {

        // 取得酒店信息
        if (null != hotelId) {
            hotel = hotelManage.findHotel(hotelId);
            this.setHotel(hotel);
            lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        }

        if (null != contractId) {
            HtlContract ct = hotelManage.findContract(contractId);
            this.setServiceCharge(ct.getServiceCharge());
            setCurrency(ct.getCurrency());
            super.putSession("currency", ct.getCurrency());
        }

        this.changePriceHint = hotel.getChangePriceHint();
        super.putSession("changePriceHint", hotel.getChangePriceHint());

        return "backToAdjustBreakfast";
    }

    public String batchInputOfBreakfast() {

        if ((null == hotelId) || (null == contractId || null == currencyS))
            return super.forwardError("hotelId,合同id,currency不能为空！");

        Map params = super.getParams();

        roleUser = getOnlineRoleUser();

        // 价格
        lstHtlBatchMtnPrice = MyBeanUtil.getBatchObjectFromParam(params, HtlBatchMtnPrice.class,
            priceRowNum);

        for (Object obj : lstHtlBatchMtnPrice) {
        	HtlBatchMtnPrice hbm = (HtlBatchMtnPrice)obj;
            hbm.setHotelId(hotelId);
            hbm.setUpdateFlag("A");
            if (null != roleUser){
            	if(null!=roleUser.getName()){
            		hbm.setCreateBy(roleUser.getName());}
            	if(null!=roleUser.getLoginName()){
            		hbm.setCreateById(roleUser.getLoginName());
            		hbm.setModifyBy(roleUser.getName());
            		hbm.setModifyById(roleUser.getLoginName());
            	}
            }
            hbm.setCreateTime(DateUtil.getSystemDate());
            hbm.setModifyTime(new Date());
        }

        super.putSession("lstHtlBatchMtnPrice", lstHtlBatchMtnPrice);
        return "batchInputOfBreakfast";
    }

    /**
	 * 
	 */
    public String adjustByBreakfast() {
        // 修改信息保存到批次修改表中
        List lst = (List) super.getFromSession("lstHtlBatchMtnPrice");
        if (null != lst) {
            priceManage.saveOrUpdatePrice(lst);
        }
        // 调用存储过程保存数据

        log.debug("用存储过程批量修改价格");
        priceManage.saveOrUpdatePrice();

        super.clearSession();
        // 获取早餐类型和早餐数量
        Map<String,String> mapTypeObj = new HashMap<String,String>(1);
        mapTypeObj.put("type", "breakfastType");
        Map mapBreakfastType = new BaseDataResourceDescriptor().loadFromDatabaseT("breakfast_type", mapTypeObj);

        if( mapBreakfastType.size() > 0) {
        	lstBreakfastType = new ArrayList(mapBreakfastType.size());
	        for(Object objType: mapBreakfastType.keySet()) {
	            ObjectBean ob = new ObjectBean();
	            ob.setName(objType.toString());
	            ob.setValue(mapBreakfastType.get(objType).toString());
	            lstBreakfastType.add(ob);
	        }
        }

        Map mapBreakfastNum = resourceManager.getDescription("breakfast_num_new");
        if( mapBreakfastNum.size() > 0) {
        	lstBreakfastNum = new ArrayList(mapBreakfastNum.size());
	        for(Object objNum : mapBreakfastNum.keySet()) {
	            ObjectBean ob = new ObjectBean();
	            ob.setName(objNum.toString());
	            ob.setValue(mapBreakfastNum.get(objNum).toString());
	            lstBreakfastNum.add(ob);
	        }
	        
	        //对拿出来的早餐进行排序-1、1、2、3...
	        lstBreakfastNum = sortBreakfastNum(lstBreakfastNum);
        }
        // 取得酒店信息
        if (null != hotelId) {
            hotel = hotelManage.findHotel(hotelId);
            this.setHotel(hotel);
            lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        }

        super.putSession("lstBreakfastType", lstBreakfastType);
        super.putSession("lstBreakfastNum", lstBreakfastNum);
        this.changePriceHint = hotel.getChangePriceHint();
        super.putSession("changePriceHint", hotel.getChangePriceHint());

        return ADJUST;
    }

    /**
     * 按天调价，批量修改价格记录
     * 
     * @return
     */
    public String batchEdit() {

        if (null == contractId)
            return super.forwardError("合同id不能为空！");
        Map params = super.getParams();
        roleUser = getOnlineRoleUser();
        // 价格
        priceList = MyBeanUtil.getBatchObjectFromParam(params, InputPrice.class, priceRowNum);
        for (int i = priceList.size(); 0 < i; i--) {
            InputPrice ip = (InputPrice) priceList.get(i - 1);
            ip.setCreate_time(DateUtil.getSystemDate());
            ip.setModify_time(new Date());
            if (null != roleUser){
                ip.setCreate_by(roleUser.getName());
            }
            if (null != roleUser && null != roleUser.getLoginName()){
            	ip.setCreate_by_id(roleUser.getLoginName());
            	ip.setModify_by(roleUser.getName());
            	ip.setModify_by_id(roleUser.getLoginName());
            }
            if (null == ip.getRoomTypeId() || null == ip.getChildRoomTypeId()) {
            	priceList.remove(i - 1);
            }

        }

        /**
         * 计算价格
         */
        // priceList先存到session中,这样根据公式修改的值就能保持住 update by kun.chen 2007-10-18
        super.putSession("priceList", priceList);
        priceList = priceManage.calculatePrice(priceList, contractId);
        // 如果按天调整的话，去掉时间段值标志 modify by zhineng.zhuang
        super.getSession().remove("dateList");

        hotel = hotelManage.findHotel(hotelId);
        if (null != hotel) {
            changePriceHint = hotel.getChangePriceHint();
        }

        return BATCH_INPUT;
    }

    /**
     * 批量录入
     * 
     * @return
     */
    public String save() {
        dateList = (List) super.getFromSession("dateList");
        priceList = (List) super.getFromSession("priceList");
        // 要区分批量修改和新增与按天调价的区别
        // 批量修改和新增的时候,价格表的信息也都更新,但按天调价的时候,就不涉及到批次表了,只要修改所涉及的天就可以了
        // 所以要有标记区分出是批次还是按天调价
        // dateList批量调整时候才有
        if (null != dateList) {

            if (null == priceList)
                return super.forwardError("找不到价格数据!");

            // 更新数据

            for (Object dsObj :dateList) {
            	DateSegment ds = (DateSegment)dsObj;
            	
                for (Object priceObj :priceList) {
                    // 插入数据到数据表中
                	InputPrice price = (InputPrice)priceObj;
                    HtlBatchSalePrice batchSalePrice = new HtlBatchSalePrice();
                    batchSalePrice.setAdjustWeek(super.getStrWeek());
                    batchSalePrice.setAdvicePrice(price.getAdvicePrice());
                    batchSalePrice.setBasePrice(price.getBasePrice());
                    batchSalePrice.setBeginDate(ds.getStart());
                    batchSalePrice.setCalcFormula(formulaId);
                    batchSalePrice.setPreCalcFormula(formulaId);
                    batchSalePrice.setCanAddScope(price.isFixed());
                    batchSalePrice.setRoomTypeId(price.getRoomTypeId());
                    if (null != price.getChildRoomTypeId())
                        batchSalePrice.setChildRoomTypeId(price.getChildRoomTypeId());
                    batchSalePrice.setCommission(price.getCalculatedCommission());
                    batchSalePrice.setCommissionRate(price.getCommissionRate());
                    batchSalePrice.setContractId(contractId);
                    if (null != super.getOnlineRoleUser() && null != super.getOnlineRoleUser().getLoginName()){
                        batchSalePrice.setCreateBy(super.getOnlineRoleUser().getName());
                        batchSalePrice.setCreateById(super.getOnlineRoleUser().getLoginName());
                        batchSalePrice.setModifyBy(super.getOnlineRoleUser().getName());
                        batchSalePrice.setModifyById(super.getOnlineRoleUser().getLoginName());
                    }
                    batchSalePrice.setCreateTime(DateUtil.getSystemDate());
                    batchSalePrice.setEndDate(ds.getEnd());
                    batchSalePrice.setIncBreakfastNumber(price.getBreakfastNum());
                    batchSalePrice.setIncBreakfastPrice(price.getBreakfastPrice());
                    batchSalePrice.setIncBreakfastType(price.getBreakfastType());
                    batchSalePrice.setModifyTime(new Date());
                    batchSalePrice.setPayMethod(price.getPayMethod());
                    batchSalePrice.setSalePrice(price.getSalePrice());
                    batchSalePrice.setSalesroomPrice(price.getRoomPrice());
                    batchSalePrice.setUpdateFlag("A");
                    // 服务费率，add by zhineng.zhuang
                    batchSalePrice.setServiceCharge(price.getServiceCharge());
                    priceManage.saveBatchSalePrice(batchSalePrice);
                }
            }
            // 周为空,默认就为全周
            if (!(null == week[0] && 0 < week[0].length())) {
                week[0] = "1,2,3,4,5,6,7,";
            }

            // 用存储过程替代下面的程序,更新或插入价格表
            //log.info("hotelId=" + hotelId + ";contractId=" + contractId + ";quotaType=" + quotaType
            //    + ";currency=" + currencyS + ";week=" + week[0]);
            priceManage.saveOrUpdatePrice(hotelId, contractId, quotaType, currencyS, week[0]);

        }
        if (null == dateList) {
            priceManage.updatePrice(hotelId, priceList);
        }

        this.changePriceHint = (String) super.getFromSession("changePriceHint");
        this.currencyS = (String) super.getFromSession("currency");
        /**
         * 清除缓存
         */
        super.clearSession();
        return SAVE_SUCCESS;
    }

    /**
     * 批量修改价格
     * 
     * @return
     */

    public String update() {
        dateList = (List) super.getFromSession("dateList");
        priceList = (List) super.getFromSession("priceList");
        /**
         * 按时间段
         */
        if (null == hotelId || null == this.contractId || null == quotaType) {
            log.error("hotelId或者contractId不能为空！");
            return super.forwardError("hotelId或者contractId不能为空！");
        }
        if (null == priceList)
            return super.forwardError("找不到价格数据!");

        // 每次保存,就往批次销售价格表里插入一次最新数据,记录最新得修改情况

        for (Object priceObj: priceList) {
            // 读取要修改的数据
        	InputPrice price = (InputPrice)priceObj;
            HtlBatchSalePrice batchSalePrice = priceManage.findPrice(price.getPriceID());

            batchSalePrice.setAdvicePrice(price.getAdvicePrice());
            batchSalePrice.setBasePrice(price.getBasePrice());

            batchSalePrice.setCanAddScope(price.isFixed());

            batchSalePrice.setCommission(price.getCalculatedCommission());
            batchSalePrice.setCommissionRate(price.getCommissionRate());

            batchSalePrice.setIncBreakfastNumber(price.getBreakfastNum());
            batchSalePrice.setIncBreakfastPrice(price.getBreakfastPrice());
            batchSalePrice.setIncBreakfastType(price.getBreakfastType());
            if (null != super.getOnlineRoleUser() && null != super.getOnlineRoleUser().getLoginName()){
                batchSalePrice.setModifyBy(super.getOnlineRoleUser().getName());
                batchSalePrice.setModifyById(super.getOnlineRoleUser().getLoginName());
            }
            batchSalePrice.setModifyTime(new Date());
            batchSalePrice.setSalePrice(price.getSalePrice());
            batchSalePrice.setSalesroomPrice(price.getRoomPrice());
            batchSalePrice.setUpdateFlag("A");
            priceManage.updateBatchSalePrice(batchSalePrice);
        }

        // 周为空,默认就为全周
        if (!(null != week[0] && 0 < week[0].length())) {
            week[0] = "1,2,3,4,5,6,7,";
        }
        // 用存储过程替代下面的程序,更新或插入价格表
        //log.info("hotelId=" + hotelId + ";contractId=" + contractId + ";quotaType=" + quotaType
        //    + ";currency=" + currencyS + ";week=" + week[0]);
        priceManage.saveOrUpdatePrice(hotelId, contractId, quotaType, currencyS, week[0]);

        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        this.changePriceHint = (String) super.getFromSession("changePriceHint");
        this.currencyS = (String) super.getFromSession("currency");
        /**
         * 清除缓存
         */
        super.clearSession();
        return "update";
    }

    /**
     * 批量查询
     * 
     * @return
     */
    public String batchQuery() {

        if (null == hotelId)
            return super.forwardError("hotelId不能为空！");

        Map params = super.getParams();

        // 时间段
        dateList = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, dateRowNum);
        lstQueryDate = dateList;

        this.rowCount = dateList.size();

        /**
         * 按查询条件查询结果，并返回页面
         */
        // super.putSession("dateList", dateList);
        lstRooms = priceManage.batchQueryPrice(getHotelId(), roomTypes, dateList, super.getIntWeek());

        return BATCH_QUERY;
    }

    public String batchQueryForUpdate() {
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        return BATCHQUERYFORUPDATE;
    }

    /**
     * 批量修改查询
     */
    public String queryPrice() {

        lstPrice = priceManage.queryPrice(contractId, start, end, childRoomId, payMethod);
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        // 价格类型变成字符串
        StringBuilder childRoomBuilder = new StringBuilder();
        if (null != childRoomId && 0 < childRoomId.length) {
            for (int m = 0; m < childRoomId.length; m++) {
            	childRoomBuilder.append(String.valueOf(childRoomId[m]));
            	childRoomBuilder.append(",");
            }
        }
        this.strChildRoomId = childRoomBuilder.toString();
        // 支付方式变成字符串
        StringBuilder payMethodBuilder = new StringBuilder();
        if (null != payMethod && 0 < payMethod.length) {
            for (int m = 0; m < payMethod.length; m++) {
            	payMethodBuilder.append(String.valueOf(payMethod[m]));
            	payMethodBuilder.append(",");
            }
        }
        this.strPayMethodS = payMethodBuilder.toString();

        return "queryPrice";
    }

    /**
     * 批量修改价格查看确认
     * 
     * @return
     */
    public String batchForUpdate() {

        if (null == contractId)
            return super.forwardError("合同id不能为空！");
        Map params = super.getParams();
        // 价格
        priceList = MyBeanUtil.getBatchObjectFromParam(params, InputPrice.class, priceRowNum);
        for (int i = priceList.size(); 0 < i; i--) {
            InputPrice ip = (InputPrice) priceList.get(i - 1);
            if (null == ip.getRoomTypeId() || null == ip.getChildRoomTypeId()) {
                priceList.remove(i - 1);
            }
        }

        /**
         * 计算价格
         */
        super.putSession("priceList", priceList);
        priceList = priceManage.calculatePrice(priceList, contractId);

        return BATCH_FORUPDATE;
    }

    /**
     * 初始化按日历查询加幅界面
     * 
     * @return
     */
    public String initQueryPrice() {
        super.getParams().put("ISINIT", "YES");
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        return "initQueryPrice";
    }

    /**
     * 查询要加幅的价格记录
     * 
     * @return
     */
    public String queryPriceByCalendar() {
        Map params = super.getParams();
        // super.getParams().put("ISINIT", "NO");
        hotel = hotelManage.findHotel(hotelId);
        hotelChnName = hotel.getChnName();
        if (null != hotel) {
            changePriceHint = hotel.getChangePriceHint();
        }
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        dateList = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, dateRowNum);

        lstPrice = priceManage.queryPriceInAddPrice(hotelId, dateList, week, priceTypeIds,
            quotaType, payMethodForQurey);

        if( lstPrice.size() > 0) {
	        Map mapRoomType = priceManage.getRoomtypeList(hotelId);
	        Map mapPriceType = priceManage.getPriceTypeList(hotelId);
	        for (Object priceObj: lstPrice) {
	        	HtlPrice price = (HtlPrice)priceObj;
	            price.setRoomTypeName((String) mapRoomType.get(price.getRoomTypeId()));
	            price.setChileRoomTypeName((String) mapPriceType.get(price.getChildRoomTypeId()));
	        }
        }

        return "queryPriceByCalendar";
    }

    /**
     * 查询要加幅的价格记录--供查询用
     * 
     * @return
     */
    
	public String queryPriceByCalendarView() {
        Map params = super.getParams();
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        lstDate = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, dateRowNum);
        this.lstQueryDate = lstDate;
        lstPrice = priceManage.queryPriceInAddPrice(hotelId, lstDate, week, priceTypeIds, quotaType, null);
        
        if( lstPrice.size() > 0) {
	        Map mapRoomType = priceManage.getRoomtypeList(hotelId);
	        Map mapPriceType = priceManage.getPriceTypeList(hotelId);
	        for (Object priceObj: lstPrice) {
	        	HtlPrice price = (HtlPrice)priceObj;
	            price.setRoomTypeName((String) mapRoomType.get(price.getRoomTypeId()));
	            price.setChileRoomTypeName((String) mapPriceType.get(price.getChildRoomTypeId()));
	        }
        }
        
        return "queryPriceByCalendarView";
    }

    /**
     * 选择假期
     * 
     * @return
     */

    public String selectHoliday() {
        lstHoliday = hotelManage.qryHoliday(hotelId);
        return "selectHoliday";
    }

    /**
     * 跳转到调价查看页面
     * 
     * @return
     */

    public String initQueryPriceView() {
        super.getParams().put("ISINIT", "YES");
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId.longValue());
        return "initQueryPriceView";
    }

    /**
     * 确认调价内容
     * 
     * @return
     */
    public String confirm() {
        return CONFIRM;
    }

    public List getPriceList() {
        return priceList;
    }

    public void setPriceList(List priceList) {
        this.priceList = priceList;
    }

    public int getPriceRowNum() {
        return priceRowNum;
    }

    public void setPriceRowNum(int priceRowNum) {
        this.priceRowNum = priceRowNum;
    }

    public InputPrice getInputPrice() {
        return inputPrice;
    }

    public void setInputPrice(InputPrice inputPrice) {
        this.inputPrice = inputPrice;
    }

    public List getLstRooms() {
        return lstRooms;
    }

    public void setLstRooms(List lstRooms) {
        this.lstRooms = lstRooms;
    }

    public IPriceManage getPriceManage() {
        return priceManage;
    }

    public void setPriceManage(IPriceManage priceManage) {
        this.priceManage = priceManage;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public HtlHotel getHotel() {
        return hotel;
    }

    public void setHotel(HtlHotel hotel) {
        this.hotel = hotel;
    }

    public IPriceDao getPriceDao() {
        return priceDao;
    }

    public void setPriceDao(IPriceDao priceDao) {
        this.priceDao = priceDao;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public String getBD() {
        return bD;
    }

    public void setBD(String bd) {
        bD = bd;
    }

    public String getED() {
        return eD;
    }

    public void setED(String ed) {
        eD = ed;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public List getLstHoliday() {
        return lstHoliday;
    }

    public void setLstHoliday(List lstHoliday) {
        this.lstHoliday = lstHoliday;
    }

    public String getHtlWeek() {
        return htlWeek;
    }

    public void setHtlWeek(String htlWeek) {
        this.htlWeek = htlWeek;
    }

    public String getHtlRoomType() {
        return htlRoomType;
    }

    public void setHtlRoomType(String htlRoomType) {
        this.htlRoomType = htlRoomType;
    }

    public List getLstQueryDate() {
        Map params = super.getParams();

        // 时间段
        dateList = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, dateRowNum);

        lstQueryDate = dateList;

        return lstQueryDate;
    }

    public void setLstQueryDate(List lstQueryDate) {
        Map params = super.getParams();

        // 时间段
        dateList = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, dateRowNum);
        this.lstQueryDate = dateList;

    }
    
    private List sortBreakfastNum(List lstBreakfastNum){
    	String tempName ="";
    	String tempValue ="";
    	for(int k=0 ;k<lstBreakfastNum.size();k++){
    		 for(int j=0 ;j<lstBreakfastNum.size()-k-1;j++){
    		    ObjectBean obItems1 = (ObjectBean)lstBreakfastNum.get(j);
    		    ObjectBean obItems2 = (ObjectBean)lstBreakfastNum.get(j+1);
    		    if(Integer.parseInt(obItems1.getName()) > Integer.parseInt(obItems2.getName())){
    		    	tempName = obItems1.getName();
    		    	tempValue = obItems1.getValue();
    		    	obItems1.setName(obItems2.getName());
    		    	obItems1.setValue(obItems2.getValue());
    		    	obItems2.setName(tempName);
    		    	obItems2.setValue(tempValue);
    		    }
    		  }
    	}
    	//升序排序，第一个为-1.需放最后
    	ObjectBean obTemp = (ObjectBean)lstBreakfastNum.get(0);
    	lstBreakfastNum.remove(0);
    	lstBreakfastNum.add(obTemp);
       return lstBreakfastNum;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List getLstDate() {
        return lstDate;
    }

    public void setLstDate(List lstDate) {
        this.lstDate = lstDate;
    }

    public List getLstPrice() {
        return lstPrice;
    }

    public void setLstPrice(List lstPrice) {
        this.lstPrice = lstPrice;
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lstRoomType) {
        this.lstRoomType = lstRoomType;
    }

    public String[] getPriceTypeIds() {
        return priceTypeIds;
    }

    public void setPriceTypeIds(String[] priceTypeIds) {
        this.priceTypeIds = priceTypeIds;
    }

    public String getCurrency() {
        return currencyS;
    }

    public void setCurrency(String currency) {
        this.currencyS = currency;
    }

    public String getChangePriceHint() {
        return changePriceHint;
    }

    public void setChangePriceHint(String changePriceHint) {
        this.changePriceHint = changePriceHint;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public static String getBATCHQUERYFORUPDATE() {
        return BATCHQUERYFORUPDATE;
    }

    public String getStrPayMethod() {
        return strPayMethodS;
    }

    public void setStrPayMethod(String strPayMethod) {
        this.strPayMethodS = strPayMethod;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public List getLstBreakfastNum() {
        return (List) super.getFromSession("lstBreakfastNum");
    }

    public void setLstBreakfastNum(List lstBreakfastNum) {
        this.lstBreakfastNum = lstBreakfastNum;
    }

    public List getLstBreakfastType() {
        return (List) super.getFromSession("lstBreakfastType");
    }

    public void setLstBreakfastType(List lstBreakfastType) {
        this.lstBreakfastType = lstBreakfastType;
    }

    public List getLstHtlBatchMtnPrice() {
        return lstHtlBatchMtnPrice;
    }

    public void setLstHtlBatchMtnPrice(List lstHtlBatchMtnPrice) {
        this.lstHtlBatchMtnPrice = lstHtlBatchMtnPrice;
    }

    public List getRoomTypePriceTypeLis() {
        return roomTypePriceTypeLis;
    }

    public void setRoomTypePriceTypeLis(List roomTypePriceTypeLis) {
        this.roomTypePriceTypeLis = roomTypePriceTypeLis;
    }

    public String[] getRoomTypeIdStr() {
        return roomTypeIdStr;
    }

    public void setRoomTypeIdStr(String[] roomTypeIdStr) {
        this.roomTypeIdStr = roomTypeIdStr;
    }

    public List<HtlBatchSalePrice> getLasAndEaList() {
        return lasAndEaList;
    }

    public void setLasAndEaList(List<HtlBatchSalePrice> lasAndEaList) {
        this.lasAndEaList = lasAndEaList;
    }

    public String getPayMethodForQurey() {
        return payMethodForQurey;
    }

    public void setPayMethodForQurey(String payMethodForQurey) {
        this.payMethodForQurey = payMethodForQurey;
    }

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
