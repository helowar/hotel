package com.mango.hotel.ebooking.log.aop.aspect;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.mango.hotel.ebooking.persistence.HtlEbookingOperationlog;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.persistence.RightManageBean;
import com.mango.hotel.ebooking.service.ILeavewordAnnalAndRightService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.hotel.constant.FunctionalModuleUtil;
import com.mangocity.util.hotel.constant.LogOperationMode;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionContext;

/**
 * AOP EBooking留言板和酒店区域权限设置记录日志 类
 * 
 * add by shengwei.zuo 2010-3-29
 * 
 */
public class LogEbookLevwordAnaRightHandle {
	
	private static final MyLog log = MyLog.getLogger(LogEbookLevwordAnaRightHandle.class);
    
    private DAO  entityManager;

    private HotelManage hotelManage;

    private ILeavewordAnnalAndRightService leaAndRightService;
    
    /**
     * EBooking留言板和酒店区域权限设置记录日志的写入
     * @param jp
     */
    public void logEbookLevwordAnaRight(JoinPoint jp) {
        
    	Object[] objLog =  jp.getArgs();
    	
    	String methodStr =jp.getSignature().getName();
    	
    	log.info("=====================EBooking留言板和酒店区域权限设置日志的写入=====Aop method : "+methodStr);
    	
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

        if (null != onlineRoleUser) {
        	
        	// 日志记录
            HtlEbookingOperationlog htlEbookingOperationlog = new HtlEbookingOperationlog();

            StringBuffer insertlogBuffer = new StringBuffer();

            // 新增区域权限人员日志记录
            if (methodStr.equals("addRightMember")) {

                RightManageBean rightManageBean = new RightManageBean();
                rightManageBean = (RightManageBean) objLog[0];

                if (null != rightManageBean) {

                    insertlogBuffer.append(onlineRoleUser.getLoginName() + "新增加一个区域权限人员。");
                    insertlogBuffer.append("登陆名:" + rightManageBean.getMemberId() + ";");
                    insertlogBuffer.append("姓名:" + rightManageBean.getMemberName() + ";");
                    insertlogBuffer.append("查看区域:" + rightManageBean.getArea() + ";");

                    // 操作方式
                    htlEbookingOperationlog.setOperationmode(LogOperationMode.ADD);

                    // 原记录表名：
                    htlEbookingOperationlog.setTablename("HTL_EBOOKING_RIGHT_MANAGE");

                    // 原记录表ID:
                    // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                    // 酒店ID：
                    htlEbookingOperationlog.setHotelid(Long.valueOf("0"));

                    // 功能模块ID
                    htlEbookingOperationlog
                        .setFunctionalmoduleid(FunctionalModuleUtil.RIGHTS_MANAGEMENT);

                } else {

                    log.info("拦截的RightManageBean对象为空，无法写入操作日志！");

                }

            } else if (methodStr.equals("delRightMemberById")) {// 删除区域权限人员日志记录

                Long rightIdAdvice = (Long) objLog[0];

                if (null != rightIdAdvice) {

                    insertlogBuffer.append(onlineRoleUser.getLoginName() + "删除一个区域权限人员。权限ID为:"
                        + rightIdAdvice);

                    // 操作方式
                    htlEbookingOperationlog.setOperationmode(LogOperationMode.DELETE);

                    // 原记录表名：
                    htlEbookingOperationlog.setTablename("HTL_EBOOKING_RIGHT_MANAGE");

                    // 原记录表ID:
                    // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                    // 酒店ID：
                    htlEbookingOperationlog.setHotelid(Long.valueOf("0"));

                    // 功能模块ID
                    htlEbookingOperationlog
                        .setFunctionalmoduleid(FunctionalModuleUtil.RIGHTS_MANAGEMENT);

                } else {

                    log.info("拦截的RightManageBean对象ID为空，无法写入操作日志！");

                }

            } else if (methodStr.equals("saveLeavewordAnnal")) {// 留言板日志记录

                LeavewordAnnalBean leavewordAnnalBean = new LeavewordAnnalBean();
                leavewordAnnalBean = (LeavewordAnnalBean) objLog[0];

                if (null != leavewordAnnalBean) {

                    // 如果是1,就表示是回复
                    if (1 == leavewordAnnalBean.getRevert()) {

                        HtlHotel hotel = hotelManage.findHotel(Long.parseLong(leavewordAnnalBean
                            .getAddressee()));
                        insertlogBuffer.append(onlineRoleUser.getLoginName() + "回复了主题为"
                            + leavewordAnnalBean.getTopic() + "的留言,发送给" + hotel.getChnName() + ";");
                        insertlogBuffer.append("回复内容:" + leavewordAnnalBean.getContent() + ";");

                        // 酒店ID:
                        htlEbookingOperationlog.setHotelid(hotel.getID());
                    } else {

                        HtlHotel hotel = hotelManage.findHotel(Long.parseLong(leavewordAnnalBean
                            .getAddressee()));
                        insertlogBuffer.append(onlineRoleUser.getLoginName() + "发送了一条主题为"
                            + leavewordAnnalBean.getTopic() + "的留言给" + hotel.getChnName() + ";");
                        insertlogBuffer.append("留言内容:" + leavewordAnnalBean.getContent() + ";");

                        // 酒店ID:
                        htlEbookingOperationlog.setHotelid(hotel.getID());
                    }

                    // 操作方式
                    htlEbookingOperationlog.setOperationmode(LogOperationMode.ADD);

                    // 原记录表名：
                    htlEbookingOperationlog.setTablename("HTL_Ebooking_Leaveword_Annal");

                    // 原记录表ID:
                    // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                    // 功能模块ID
                    htlEbookingOperationlog.setFunctionalmoduleid(FunctionalModuleUtil.DOC_MANAGE);

                } else {

                   log.info("拦截的LeavewordAnnalBean对象为空，无法写入操作日志！");

                }

            } else if (methodStr.equals("delLeavewordAnnalById")) {// 删除留言
                Long leavewordAnnaID = (Long) objLog[0];
                LeavewordAnnalBean oldLeavewordAnnal = leaAndRightService
                    .getLeaveWordById(leavewordAnnaID);
                insertlogBuffer.append(onlineRoleUser.getLoginName() + "删除了一条留言ID为"
                    + leavewordAnnaID + "的留言。");
                insertlogBuffer.append("留言主题为:" + oldLeavewordAnnal.getTopic() + ";");
                insertlogBuffer.append("留言内容:" + oldLeavewordAnnal.getContent() + ";");

                // 酒店ID:
                htlEbookingOperationlog
                    .setHotelid(Long.parseLong(oldLeavewordAnnal.getAddressee()));

                // 操作方式
                htlEbookingOperationlog.setOperationmode(LogOperationMode.DELETE);

                // 原记录表名：
                htlEbookingOperationlog.setTablename("HTL_Ebooking_Leaveword_Annal");

                // 原记录表ID:
                // htlEbookingOperationlog.setTableID(personnelInfoBean.getPersonnelID());

                // 功能模块ID
                htlEbookingOperationlog.setFunctionalmoduleid(FunctionalModuleUtil.DOC_MANAGE);

            }

            // 操作内容：
            htlEbookingOperationlog.setOperationcontent(insertlogBuffer.toString());

            // 操作人
            htlEbookingOperationlog.setOperationer(onlineRoleUser.getName());

            // 操作人ID
            htlEbookingOperationlog.setOperationerid(onlineRoleUser.getLoginName());

            // 操作日期
            htlEbookingOperationlog.setOperationdate(DateUtil.getSystemDate());

            entityManager.save(htlEbookingOperationlog);

        } else {

            log.info("获取登陆用户信息失效,请重新登陆！");

        }
    	
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

	public ILeavewordAnnalAndRightService getLeaAndRightService() {
		return leaAndRightService;
	}

	public void setLeaAndRightService(
			ILeavewordAnnalAndRightService leaAndRightService) {
		this.leaAndRightService = leaAndRightService;
	}
	
}
