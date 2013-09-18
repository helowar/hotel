package com.mango.hotel.ebooking.log.aop.aspect;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.mango.hotel.ebooking.persistence.HtlEbookingOperationlog;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlEbooking;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.hotel.constant.FunctionalModuleUtil;
import com.mangocity.util.hotel.constant.LogOperationMode;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionContext;

/**
 * AOP 设置E-booking酒店的操作日志 类
 * 
 * add by shengwei.zuo 2010-3-29
 * 
 */
public class LogHtlEbookingHandle {
    
	private static final MyLog log = MyLog.getLogger(LogHtlEbookingHandle.class);
	
    private DAO  entityManager;

    private HotelManage hotelManage;
    
    /**
     * 拦截设置E-booking酒店的操作类，进行日志写入
     */
    public void logHtlEbooking(JoinPoint jp) {
    	
    	// 设置E-booking酒店
    	Object[] objLog =  jp.getArgs();
    	
    	String methodStr =jp.getSignature().getName();
        	
     	log.info("=====================设置E-booking酒店日志的写入=====Aop method : "+methodStr);
  	
    	
    	//用户实体类
    	UserWrapper onlineRoleUser = new UserWrapper();
    	
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
        	
        	HtlEbooking htlEbooking = new HtlEbooking();
            htlEbooking = (HtlEbooking) objLog[0];

            // 拦截的HtlEbooking对象不为空
            if (null != htlEbooking) {
            	
            	 // 设置E-booking酒店日志类
                HtlEbookingOperationlog htlEbookingOperationlog = new HtlEbookingOperationlog();

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

                entityManager.save(htlEbookingOperationlog);

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

}
