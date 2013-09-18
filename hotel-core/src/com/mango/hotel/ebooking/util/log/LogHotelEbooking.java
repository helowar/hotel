package com.mango.hotel.ebooking.util.log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.aop.MethodBeforeAdvice;

import com.mango.hotel.ebooking.persistence.HtlEbookingOperationlog;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlEbooking;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.FunctionalModuleUtil;
import com.mangocity.util.hotel.constant.LogOperationMode;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionContext;

/**
 * 设置E-Booking 酒店 的操作日志
 * 
 * @author zuoshengwei
 * 
 */
public class LogHotelEbooking extends DAOHibernateImpl implements MethodBeforeAdvice {

    // 日志记录
    private HtlEbookingOperationlog htlEbookingOperationlog = new HtlEbookingOperationlog();
    private static final MyLog log = MyLog.getLogger(LogHotelEbooking.class);
    private HotelManage hotelManage;

    private UserWrapper onlineRoleUser;

    public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {

        // 从session中取出当前的登录用户信息
        WebContext wct = WebContextFactory.get();
        if (null != wct) {
            HttpSession session = wct.getSession();
            onlineRoleUser = (UserWrapper) session.getAttribute("onlineRoleUser");
        }

        Map map = (Map) ActionContext.getContext().get("session");
        if (null != map) {
            onlineRoleUser = (UserWrapper) map.get("onlineRoleUser");
        }

        // 当前登陆用户不为空
        if (null != onlineRoleUser) {

            // 设置E-booking酒店
            HtlEbooking htlEbooking = new HtlEbooking();
            htlEbooking = (HtlEbooking) arg1[0];

            // 拦截的HtlEbooking对象不为空
            if (null != htlEbooking) {

                log.info("设置E-booking酒店操作日志。");

                StringBuffer insertlogBuffer = new StringBuffer();

                HtlHotel hotel = new HtlHotel();

                hotel = htlEbooking.getHtlHotel();

                List ebookingList = hotelManage.findHtlEbooking(hotel.getID());

                if (0 < ebookingList.size()) {

                    if (htlEbooking.getWhetherEbooking().equals("0")) {

                        insertlogBuffer.append(onlineRoleUser.getLoginName()
                            + "解除了一家E-booking酒店。酒店名称为" + hotel.getChnName());

                    } else {

                        insertlogBuffer
                            .append(onlineRoleUser.getLoginName() + "修改了E-booking酒店的设置。");

                        insertlogBuffer.append("酒店名称: " + hotel.getChnName() + ";");

                        HtlEbooking oldEbooking = (HtlEbooking) ebookingList.get(0);

                        insertlogBuffer.append("价格是否审核由："
                            + returnSorce(oldEbooking.getWhetherPrice()) + ",改为:"
                            + returnSorce(htlEbooking.getWhetherPrice()) + ";");

                        insertlogBuffer.append("房态是否审核由："
                            + returnSorce(oldEbooking.getWhetherRoomType()) + ",改为:"
                            + returnSorce(htlEbooking.getWhetherRoomType()) + ";");
                    }

                    // 操作方式
                    htlEbookingOperationlog.setOperationmode(LogOperationMode.MODIFY);

                } else {

                    insertlogBuffer.append(onlineRoleUser.getLoginName() + "新设置了E-booking酒店。");

                    insertlogBuffer.append("酒店名称: " + hotel.getChnName() + ";");

                    insertlogBuffer.append("价格是否审核：" + returnSorce(htlEbooking.getWhetherPrice())
                        + ";");

                    insertlogBuffer.append("房态是否审核："
                        + returnSorce(htlEbooking.getWhetherRoomType()) + ";");

                    // 操作方式
                    htlEbookingOperationlog.setOperationmode(LogOperationMode.ADD);

                }

                // 操作内容：
                htlEbookingOperationlog.setOperationcontent(insertlogBuffer.toString());

                // 原记录表名：
                htlEbookingOperationlog.setTablename("HTL_EBOOKING");

                // 原记录表ID:
                // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                // 功能模块ID
                htlEbookingOperationlog.setFunctionalmoduleid(FunctionalModuleUtil.HTL_EBOOKING);

                // 酒店ID:
                htlEbookingOperationlog.setHotelid(hotel.getID());

                // 操作人
                htlEbookingOperationlog.setOperationer(onlineRoleUser.getName());

                // 操作人ID
                htlEbookingOperationlog.setOperationerid(onlineRoleUser.getLoginName());

                // 操作日期
                htlEbookingOperationlog.setOperationdate(DateUtil.getSystemDate());

                super.save(htlEbookingOperationlog);

            } else {

                log.info("拦截的HtlEbooking对象为空，无法写入操作日志！");
            }

        } else {

            log.info("获取登陆用户信息失效,请重新登陆！");

        }

    }

    /**
     * 返回是否
     * 
     * @param Sorce
     * @return
     */
    public String returnSorce(String Sorce) {
        String SorceStr = "";
        if (null != Sorce && !Sorce.equals("")) {
            if ("1".equals(Sorce) || Sorce.equals("1")) {
                SorceStr = "是";
            } else if (Sorce.equals("0")) {
                SorceStr = "否";
            }
        } else {
            SorceStr = "无";
        }
        return SorceStr;
    }

    public HtlEbookingOperationlog getHtlEbookingOperationlog() {
        return htlEbookingOperationlog;
    }

    public void setHtlEbookingOperationlog(HtlEbookingOperationlog htlEbookingOperationlog) {
        this.htlEbookingOperationlog = htlEbookingOperationlog;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public UserWrapper getOnlineRoleUser() {
        return onlineRoleUser;
    }

    public void setOnlineRoleUser(UserWrapper onlineRoleUser) {
        this.onlineRoleUser = onlineRoleUser;
    }

}
