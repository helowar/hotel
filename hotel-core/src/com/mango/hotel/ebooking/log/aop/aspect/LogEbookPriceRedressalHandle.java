package com.mango.hotel.ebooking.log.aop.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;

import com.mango.hotel.ebooking.persistence.HtlEbookingOperationlog;
import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.service.IPriceRedressalService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.hotel.constant.FunctionalModuleUtil;
import com.mangocity.util.hotel.constant.LogOperationMode;
import com.mangocity.util.log.MyLog;

/**
 * AOP EBooking价格审核记录日志的写入日志 类
 * 
 * add by shengwei.zuo 2010-3-29
 * 
 */
public class LogEbookPriceRedressalHandle {
	
	private static final MyLog log = MyLog.getLogger(LogEbookPriceRedressalHandle.class);
	
    private DAO  entityManager;

    private HotelManage hotelManage;
    
    private IPriceRedressalService priceRedressalService;
    
    /**
     * EBooking价格审核记录日志的写入
     * @param jp
     */
    public void logEbookPriceRedressal(JoinPoint jp) {
        
    	Object[] objLog =  jp.getArgs();
    	
    	String methodStr =jp.getSignature().getName();
    	
    	log.info("=====================EBooking价格审核记录日志的写入=====Aop method : "+methodStr);
    	
        // 记录审核通过或否决的日志写入
        StringBuffer insertlogBuffer = new StringBuffer();

        // 记录留言板的日志
        StringBuffer leavewordAnnalLogBuffer = new StringBuffer();
        
        HtlEbookingOperationlog htlEbookingOperationlog = new HtlEbookingOperationlog();

        // 单独审核通过或否决时写入日志
        if (methodStr.equals("updatePriceRedressalForSingle")) {

            HtlEbookingPriceRedressal priceRedressalBean = new HtlEbookingPriceRedressal();
            LeavewordAnnalBean leavewordAnnalBean = new LeavewordAnnalBean();
            UserWrapper user = new UserWrapper();

            priceRedressalBean = (HtlEbookingPriceRedressal) objLog[0];
            leavewordAnnalBean = (LeavewordAnnalBean) objLog[1];
            user = (UserWrapper) objLog[2];

            // 判断HtlEbookingOperationlog对象是否为空
            if (null != priceRedressalBean) {

                // 审核被否决
                if (null != leavewordAnnalBean) {

                    insertlogBuffer.append(user.getLoginName() + "否决了变价标题为"
                        + priceRedressalBean.getPriceRedressalName() + "的变价申请。");

                    leavewordAnnalLogBuffer.append(user.getLoginName() + "否决了变价标题为"
                        + priceRedressalBean.getPriceRedressalName() + "的变价申请。并给"
                        + priceRedressalBean.getHotelname() + "留言。");
                    leavewordAnnalLogBuffer.append("留言主题: " + leavewordAnnalBean.getTopic() + ";");
                    leavewordAnnalLogBuffer
                        .append("留言内容: " + leavewordAnnalBean.getContent() + ";");

                    // 操作方式
                    htlEbookingOperationlog.setOperationmode(LogOperationMode.ADD);

                    // 操作内容：
                    htlEbookingOperationlog.setOperationcontent(leavewordAnnalLogBuffer.toString());

                    // 原记录表名：
                    htlEbookingOperationlog.setTablename("HTL_Ebooking_Leaveword_Annal");

                    // 原记录表ID:
                    // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                    // 功能模块ID
                    htlEbookingOperationlog.setFunctionalmoduleid(FunctionalModuleUtil.DOC_MANAGE);

                    // 酒店ID:
                    htlEbookingOperationlog.setHotelid(Long.parseLong(priceRedressalBean
                        .getHotelid()));

                    // 操作人
                    htlEbookingOperationlog.setOperationer(user.getName());

                    // 操作人ID
                    htlEbookingOperationlog.setOperationerid(user.getLoginName());

                    // 操作日期
                    htlEbookingOperationlog.setOperationdate(DateUtil.getSystemDate());

                    entityManager.save(htlEbookingOperationlog);

                } else {// 审核通过

                    insertlogBuffer.append(user.getLoginName() + "审核通过了变价标题为"
                        + priceRedressalBean.getPriceRedressalName() + "的变价申请。");
                    insertlogBuffer.append("佣金更改为: " + priceRedressalBean.getCommision() + ";");

                }

                insertlogBuffer.append("申请单号: " + priceRedressalBean.getPriceRedressalID() + ";");
                insertlogBuffer.append("酒店名称: " + priceRedressalBean.getHotelname() + ";");
                insertlogBuffer.append("变价房型: " + priceRedressalBean.getRoomTypeName() + "("
                    + priceRedressalBean.getPriceTypeName() + ");");
                insertlogBuffer.append("开始日期: "
                    + DateUtil.dateToString(priceRedressalBean.getBeginDate()) + ";");
                insertlogBuffer.append("结束日期: "
                    + DateUtil.dateToString(priceRedressalBean.getEndDate()) + ";");

                insertlogBuffer.append("星期: " + priceRedressalBean.getWeek() + ";");
                insertlogBuffer.append("支付方式: " + returnPayStr(priceRedressalBean.getPayMethod())
                    + ";");

                htlEbookingOperationlog = new HtlEbookingOperationlog();

                // 操作方式
                htlEbookingOperationlog.setOperationmode(LogOperationMode.MODIFY);

                // 操作内容：
                htlEbookingOperationlog.setOperationcontent(insertlogBuffer.toString());

                // 原记录表名：
                htlEbookingOperationlog.setTablename("HTL_Ebooking_Price_Redressal");

                // 原记录表ID:
                // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                // 功能模块ID
                htlEbookingOperationlog
                    .setFunctionalmoduleid(FunctionalModuleUtil.ADJUST_PRICE_DETAIL);

                // 酒店ID:
                htlEbookingOperationlog.setHotelid(Long.parseLong(priceRedressalBean.getHotelid()));

                // 操作人
                htlEbookingOperationlog.setOperationer(user.getName());

                // 操作人ID
                htlEbookingOperationlog.setOperationerid(user.getLoginName());

                // 操作日期
                htlEbookingOperationlog.setOperationdate(DateUtil.getSystemDate());

                entityManager.save(htlEbookingOperationlog);

            } else {

                log.info("拦截的HtlEbookingPriceRedressal对象为空，无法写入操作日志！");

            }

        } else if (methodStr.equals("updatePriceRedressalForReject")) {// 批量否决的时候写入日志

            String priceRedressalIdStr = (String) objLog[0];

            LeavewordAnnalBean leavewordAnnalBean = new LeavewordAnnalBean();
            UserWrapper user = new UserWrapper();

            user = (UserWrapper) objLog[1];

            leavewordAnnalBean = (LeavewordAnnalBean) objLog[2];

            if (null != priceRedressalIdStr && !priceRedressalIdStr.equals("")) {

                // 拆分变价ID字符串
                String[] priceRedIdArry = priceRedressalIdStr.split(",");

                for (int i = 0; i < priceRedIdArry.length; i++) {

                    HtlEbookingPriceRedressal priceRedressal = new HtlEbookingPriceRedressal();

                    priceRedressal = priceRedressalService.queryPriceRedressalById(Long
                        .parseLong(priceRedIdArry[i]));

                    insertlogBuffer.append(user.getLoginName() + "否决了变价标题为"
                        + priceRedressal.getPriceRedressalName() + "的变价申请。");

                    insertlogBuffer.append("申请单号: " + priceRedressal.getPriceRedressalID() + ";");
                    insertlogBuffer.append("酒店名称: " + priceRedressal.getHotelname() + ";");
                    insertlogBuffer.append("变价房型: " + priceRedressal.getRoomTypeName() + "("
                        + priceRedressal.getPriceTypeName() + ");");
                    insertlogBuffer.append("开始日期: "
                        + DateUtil.dateToString(priceRedressal.getBeginDate()) + ";");
                    insertlogBuffer.append("结束日期: "
                        + DateUtil.dateToString(priceRedressal.getEndDate()) + ";");

                    insertlogBuffer.append("星期: " + priceRedressal.getWeek() + ";");
                    insertlogBuffer.append("支付方式: " + returnPayStr(priceRedressal.getPayMethod())
                        + ";");

                    htlEbookingOperationlog = new HtlEbookingOperationlog();

                    // 操作方式
                    htlEbookingOperationlog.setOperationmode(LogOperationMode.MODIFY);

                    // 操作内容：
                    htlEbookingOperationlog.setOperationcontent(insertlogBuffer.toString());

                    // 原记录表名：
                    htlEbookingOperationlog.setTablename("HTL_Ebooking_Price_Redressal");

                    // 原记录表ID:
                    // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                    // 功能模块ID
                    htlEbookingOperationlog
                        .setFunctionalmoduleid(FunctionalModuleUtil.ADJUST_PRICE_DETAIL);

                    // 酒店ID:
                    htlEbookingOperationlog.setHotelid(Long.parseLong(priceRedressal.getHotelid()));

                    // 操作人
                    htlEbookingOperationlog.setOperationer(user.getName());

                    // 操作人ID
                    htlEbookingOperationlog.setOperationerid(user.getLoginName());

                    // 操作日期
                    htlEbookingOperationlog.setOperationdate(DateUtil.getSystemDate());

                    entityManager.save(htlEbookingOperationlog);

                    if (0 == i) {

                        // 留言板也要写入日志
                        HtlHotel hotel = hotelManage.findHotel(Long.parseLong(leavewordAnnalBean
                            .getAddressee()));

                        leavewordAnnalLogBuffer.append(user.getLoginName() + "否决了变价标题为"
                            + priceRedressal.getPriceRedressalName() + "的变价申请。并给"
                            + hotel.getChnName() + "留言。");
                        leavewordAnnalLogBuffer.append("留言主题: " + leavewordAnnalBean.getTopic()
                            + ";");
                        leavewordAnnalLogBuffer.append("留言内容: " + leavewordAnnalBean.getContent()
                            + ";");

                        htlEbookingOperationlog = new HtlEbookingOperationlog();

                        // 操作方式
                        htlEbookingOperationlog.setOperationmode(LogOperationMode.ADD);

                        // 操作内容：
                        htlEbookingOperationlog.setOperationcontent(leavewordAnnalLogBuffer
                            .toString());

                        // 原记录表名：
                        htlEbookingOperationlog.setTablename("HTL_Ebooking_Leaveword_Annal");

                        // 原记录表ID:
                        // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                        // 功能模块ID
                        htlEbookingOperationlog
                            .setFunctionalmoduleid(FunctionalModuleUtil.DOC_MANAGE);

                        // 酒店ID:
                        htlEbookingOperationlog.setHotelid(Long.parseLong(priceRedressal
                            .getHotelid()));

                        // 操作人
                        htlEbookingOperationlog.setOperationer(user.getName());

                        // 操作人ID
                        htlEbookingOperationlog.setOperationerid(user.getLoginName());

                        // 操作日期
                        htlEbookingOperationlog.setOperationdate(DateUtil.getSystemDate());

                        entityManager.save(htlEbookingOperationlog);

                    }
                }

            } else {

                log.info("拦截的调价申请号为空，无法写入操作日志！");

            }

        } else if (methodStr.equals("updatePriceRedressalForAdjusted")) {// 批量审核通过的时候写入日志

            UserWrapper user = new UserWrapper();
            user = (UserWrapper) objLog[1];

            List<HtlEbookingPriceRedressal> tempPriceRedressalList =
                (List<HtlEbookingPriceRedressal>) objLog[2];

            if (!tempPriceRedressalList.isEmpty()) {

                for (int i = 0; i < tempPriceRedressalList.size(); i++) {

                    HtlEbookingPriceRedressal priceRedressal = new HtlEbookingPriceRedressal();

                    HtlEbookingPriceRedressal priceRedressalForList = 
                        new HtlEbookingPriceRedressal();

                    priceRedressalForList = tempPriceRedressalList.get(i);

                    priceRedressal = priceRedressalService
                        .queryPriceRedressalById(priceRedressalForList.getPriceRedressalID());

                    insertlogBuffer = new StringBuffer();

                    insertlogBuffer.append(user.getLoginName() + "审核通过了变价标题为"
                        + priceRedressal.getPriceRedressalName() + "的变价申请。");

                    insertlogBuffer.append("申请单号: " + priceRedressal.getPriceRedressalID() + ";");
                    insertlogBuffer.append("酒店名称: " + priceRedressal.getHotelname() + ";");
                    insertlogBuffer.append("变价房型: " + priceRedressal.getRoomTypeName() + "("
                        + priceRedressal.getPriceTypeName() + ");");
                    insertlogBuffer.append("开始日期: "
                        + DateUtil.dateToString(priceRedressal.getBeginDate()) + ";");
                    insertlogBuffer.append("结束日期: "
                        + DateUtil.dateToString(priceRedressal.getEndDate()) + ";");

                    insertlogBuffer.append("佣金更改为: " + priceRedressalForList.getCommision() + ";");

                    insertlogBuffer.append("星期: " + priceRedressal.getWeek() + ";");
                    insertlogBuffer.append("支付方式: " + returnPayStr(priceRedressal.getPayMethod())
                        + ";");

                    htlEbookingOperationlog = new HtlEbookingOperationlog();

                    // 操作方式
                    htlEbookingOperationlog.setOperationmode(LogOperationMode.MODIFY);

                    // 操作内容：
                    htlEbookingOperationlog.setOperationcontent(insertlogBuffer.toString());

                    // 原记录表名：
                    htlEbookingOperationlog.setTablename("HTL_Ebooking_Price_Redressal");

                    // 原记录表ID:
                    // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                    // 功能模块ID
                    htlEbookingOperationlog
                        .setFunctionalmoduleid(FunctionalModuleUtil.ADJUST_PRICE_DETAIL);

                    // 酒店ID:
                    htlEbookingOperationlog.setHotelid(Long.parseLong(priceRedressal.getHotelid()));

                    // 操作人
                    htlEbookingOperationlog.setOperationer(user.getName());

                    // 操作人ID
                    htlEbookingOperationlog.setOperationerid(user.getLoginName());

                    // 操作日期
                    htlEbookingOperationlog.setOperationdate(DateUtil.getSystemDate());

                    entityManager.save(htlEbookingOperationlog);

                }

            } else {
                log.info("拦截的调价申请号为空，无法写入操作日志！");
            }

        }
    	
    	
    }

    
    /**
     * 返回是面付或预付
     * 
     * @param Sorce
     * @return
     */
    public String returnPayStr(String Sorce) {
        String SorceStr = "";
        if (null != Sorce && !Sorce.equals("")) {
            if (Sorce.equals("pre_pay")) {
                SorceStr = "预付";
            } else if (Sorce.equals("pay")) {
                SorceStr = "面付";
            }
        } else {
            SorceStr = "无";
        }
        return SorceStr;
    }


    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

	public DAO getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(DAO entityManager) {
		this.entityManager = entityManager;
	}


	public IPriceRedressalService getPriceRedressalService() {
		return priceRedressalService;
	}


	public void setPriceRedressalService(
			IPriceRedressalService priceRedressalService) {
		this.priceRedressalService = priceRedressalService;
	}

	
}
