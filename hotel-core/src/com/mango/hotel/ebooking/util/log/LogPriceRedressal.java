package com.mango.hotel.ebooking.util.log;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.aop.MethodBeforeAdvice;

import com.mango.hotel.ebooking.persistence.HtlEbookingOperationlog;
import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.service.IPriceRedressalService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.FunctionalModuleUtil;
import com.mangocity.util.hotel.constant.LogOperationMode;
import com.mangocity.util.log.MyLog;

/**
 * EBooking调价审批日志记录
 * 
 * @author zuoshengwei
 * 
 */
public class LogPriceRedressal extends DAOHibernateImpl implements MethodBeforeAdvice {

    // 日志记录
    private HtlEbookingOperationlog htlEbookingOperationlog = new HtlEbookingOperationlog();
    private static final MyLog log = MyLog.getLogger(LogPriceRedressal.class);
    private IPriceRedressalService priceRedressalService;

    private HotelManage hotelManage;

    public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {

        // 记录审核通过或否决的日志写入
        StringBuffer insertlogBuffer = new StringBuffer();

        // 记录留言板的日志
        StringBuffer leavewordAnnalLogBuffer = new StringBuffer();

        // 单独审核通过或否决时写入日志
        if (arg0.getName().equals("updatePriceRedressalForSingle")) {

            HtlEbookingPriceRedressal priceRedressalBean = new HtlEbookingPriceRedressal();
            LeavewordAnnalBean leavewordAnnalBean = new LeavewordAnnalBean();
            UserWrapper user = new UserWrapper();

            priceRedressalBean = (HtlEbookingPriceRedressal) arg1[0];
            leavewordAnnalBean = (LeavewordAnnalBean) arg1[1];
            user = (UserWrapper) arg1[2];

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

                    htlEbookingOperationlog = new HtlEbookingOperationlog();

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

                    super.save(htlEbookingOperationlog);

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
                insertlogBuffer.append("支付方式: " + returnSorce(priceRedressalBean.getPayMethod())
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

                super.save(htlEbookingOperationlog);

            } else {

                log.info("拦截的HtlEbookingPriceRedressal对象为空，无法写入操作日志！");

            }

        } else if (arg0.getName().equals("updatePriceRedressalForReject")) {// 批量否决的时候写入日志

            String priceRedressalIdStr = (String) arg1[0];

            LeavewordAnnalBean leavewordAnnalBean = new LeavewordAnnalBean();
            UserWrapper user = new UserWrapper();

            user = (UserWrapper) arg1[1];

            leavewordAnnalBean = (LeavewordAnnalBean) arg1[2];

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
                    insertlogBuffer.append("支付方式: " + returnSorce(priceRedressal.getPayMethod())
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

                    super.save(htlEbookingOperationlog);

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

                        super.save(htlEbookingOperationlog);

                    }
                }

            } else {

                log.info("拦截的调价申请号为空，无法写入操作日志！");

            }

        } else if (arg0.getName().equals("updatePriceRedressalForAdjusted")) {// 批量审核通过的时候写入日志

            UserWrapper user = new UserWrapper();
            user = (UserWrapper) arg1[1];

            List<HtlEbookingPriceRedressal> tempPriceRedressalList =
                (List<HtlEbookingPriceRedressal>) arg1[2];

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
                    insertlogBuffer.append("支付方式: " + returnSorce(priceRedressal.getPayMethod())
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

                    super.save(htlEbookingOperationlog);

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
    public String returnSorce(String Sorce) {
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

    /**
     * 根据id的字符串，返回对应的name的字符串
     * 
     * @param PriceRedressalID
     * @return
     */
    public String returnStringPriceRedressalName(String PriceRedressalID) {

        String riceRedressalNameStr = "";

        String[] priceRedressalID = PriceRedressalID.split(",");
        for (int i = 0; i < priceRedressalID.length; i++) {

            HtlEbookingPriceRedressal priceRedressal = new HtlEbookingPriceRedressal();

            priceRedressal = priceRedressalService.queryPriceRedressalById(Long
                .parseLong(priceRedressalID[i]));

            riceRedressalNameStr += (0 < i ? "," : "") + priceRedressal.getPriceRedressalName();

        }
        return riceRedressalNameStr;
    }

    public HtlEbookingOperationlog getHtlEbookingOperationlog() {
        return htlEbookingOperationlog;
    }

    public void setHtlEbookingOperationlog(HtlEbookingOperationlog htlEbookingOperationlog) {
        this.htlEbookingOperationlog = htlEbookingOperationlog;
    }

    public IPriceRedressalService getPriceRedressalService() {
        return priceRedressalService;
    }

    public void setPriceRedressalService(IPriceRedressalService priceRedressalService) {
        this.priceRedressalService = priceRedressalService;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

}
