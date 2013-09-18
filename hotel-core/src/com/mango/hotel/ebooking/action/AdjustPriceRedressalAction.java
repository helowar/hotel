package com.mango.hotel.ebooking.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mango.hotel.ebooking.constants.RequisitionState;
import com.mango.hotel.ebooking.dao.impl.LeavewordAnnalAndRightDaoImpl;
import com.mango.hotel.ebooking.dao.impl.PriceRedressalDaoImpl;
import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.service.ILeavewordAnnalAndRightService;
import com.mango.hotel.ebooking.service.IPriceRedressalService;
import com.mango.hotel.ebooking.service.IUpdateHotelPrice;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;


/**
 * EBooking调价审批Action
 * 
 * @author chenjiajie
 * 
 */
public class AdjustPriceRedressalAction extends PaginateAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 标识进入明细页面是否显示批量按钮
     */
    private String type;

    /**
     * 否决的时候标志是否批量审批 是批量审批：true 单个审批：false
     */
    private Boolean isBatch;

    /**
     * 页面传入参数
     */
    private Long hotelId;

    /**
     * 页面传入参数
     */
    private Long priceRedressalNameId;

    /**
     * Ebooking留言板实体
     */
    private LeavewordAnnalBean leavewordAnnalBean;

    /**
     * EBooking调价审核操作业务接口
     */
    private IPriceRedressalService priceRedressalService;

    /**
     * EBooking留言板和酒店区域查看权限业务接口
     */
    private ILeavewordAnnalAndRightService leaAndRightService;
    
    private HotelManage hotelManage;
    
    private HtlHotel hotel;
    
    private String changePriceHint;
    
    /**
     * EBooking调价审核操作DAO
     */
    private PriceRedressalDaoImpl priceRedressalDao;
    /**
     * EBooking留言板和区域权限操作DAO
     */
    private LeavewordAnnalAndRightDaoImpl leaAndRightDao;
    
    private IUpdateHotelPrice updateHotelPrice;
    public IUpdateHotelPrice getUpdateHotelPrice() {
		return updateHotelPrice;
	}

	public void setUpdateHotelPrice(IUpdateHotelPrice updateHotelPrice) {
		this.updateHotelPrice = updateHotelPrice;
	}

	/**
     * 进入查询页面
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String forwardToQuery() {
        Map session = super.getSession();
        roleUser = super.getCurrentUser();
        // 查询当前用户所在的区域，把其所属区域放入Session中
        if (null != roleUser) {
            String userArea = leaAndRightService.getHotelAreaByLoginName(roleUser.getLoginName());
            session.put("userArea", userArea);
        }
        return "adjustPriceApplication";
    }

    @SuppressWarnings("unchecked")
    public String execute() {
        Map params = super.getParams();
        Map session = super.getSession();
        String userArea = (String) params.get("userArea");
        // 如果页面有传入userArea参数，则不需要从Session取
        if (StringUtil.isValidStr(userArea)) {
            params.put("userArea", userArea);
        } else {
            userArea = (String) session.get("userArea");
            // 先从Session去当前用户的区域编码
            if (StringUtil.isValidStr(userArea)) {
                // 返回的区域不是ALL则增加查询条件，否则不放到参数中，即可以查询所有区域
                if (!userArea.equals("ALL")) {
                    params.put("userArea", userArea);
                }
            } 
            // 如果Session不存在，再从数据库查询
            else {
                roleUser = super.getCurrentUser();
                // 查询当前用户所在的区域，当前用户不为空则取用户所在区域，否则查不出结果
                if (null != roleUser) {
                    userArea = leaAndRightService.getHotelAreaByLoginName(roleUser.getLoginName());
                    if (StringUtil.isValidStr(userArea)) {
                        // 返回的区域不是ALL则增加查询条件，否则不放到参数中，即可以查询所有区域
                        if (!userArea.equals("ALL")) {
                            params.put("userArea", userArea);
                        }
                    }
                    // 取不到区域编码则查不出结果
                    else {
                        params.put("userArea", "NONE");
                    }
                } else {
                    params.put("userArea", "NONE");
                }
            }
        }
        return super.execute();
    }

    /**
     * 获取具体调价记录
     * @return
     */
    public String queryPriceRedressalDetail() {
        // Map params = super.getParams();
        // 验证参数是否有效
        if (null != hotelId) {
            try {
                List<HtlEbookingPriceRedressal> priceRedressalList = null;
                /** 回写数据库，待审核状态要改为审核中，并查询回写后的结果 begin * */
                priceRedressalList = priceRedressalService.updatePriceRedressal(
                    RequisitionState.UNAUDITED, hotelId, priceRedressalNameId);
                /** 回写数据库，待审核状态要改为审核中，并查询回写后的结果 end * */
                request.setAttribute("priceRedressalList", priceRedressalList);
                hotel = hotelManage.findHotel(hotelId);
                changePriceHint = hotel.getChangePriceHint();
            } catch (Exception e) {
                log.error("无该调价记录", e);
                return super.forwardError("无该调价记录");
            }
        } else {
            return super.forwardError("无该调价记录");
        }
        return "adjustPriceDetail";
    }

    /**
     * 审核某一调价记录
     * @return
     */
    public String auditPriceRedressal() {
        String returnStr = "adjustPriceDetail";
        Map params = super.getParams();
        String idStr = (String) params.get("priceRedressalID");
        // 界面传入的审核状态
        String requisitionStateStr = (String) params.get("requisitionState");
        // 验证参数是否有效
        if (StringUtil.isValidStr(idStr) && StringUtil.isValidStr(requisitionStateStr)) {
            UserWrapper user = super.getOnlineRoleUser();
            String loginName = ""; // 登录名
            String userName = ""; // 登陆人姓名
            if (null != user) {
                loginName = user.getLoginName();
                userName = user.getName();
            }
            try {
            	//e-booking的价格表
                HtlEbookingPriceRedressal oldPriceRedressal = priceRedressalService
                    .queryPriceRedressalById(Long.parseLong(idStr));
                // 修改审核状态
                oldPriceRedressal.setRequisitionState(Long.parseLong(requisitionStateStr));
                oldPriceRedressal.setAuditingDate(new Date());
                oldPriceRedressal.setAuditingID(loginName);
                oldPriceRedressal.setAuditingName(userName);
                // 否决的时候需要填充留言板实体，否则为空
                if (Long.parseLong(requisitionStateStr) == RequisitionState.REJECT) {
                    // 填充留言板实体
                    fillLeavewordAnnalBean(params, loginName, userName);
                    leaAndRightDao.save(leavewordAnnalBean);
                	/**
                	 * 更新E-Booking调价信息表
                	 */
                	priceRedressalService.updatePriceRedressalForSingle(oldPriceRedressal,leavewordAnnalBean, user);
                    returnStr = "adjustPriceLeaveWordResult";
                }
                // （审批通过）的时候需要填充佣金
                else if (Long.parseLong(requisitionStateStr) == RequisitionState.AUDITED) {
                    String commisionStr = (String) params.get("commision");
                    // 佣金
                    oldPriceRedressal.setCommision(Float.parseFloat(commisionStr));
                    
                 /** 填充留言板实体**/
//                    fillLeavewordAnnalBean(params, loginName, userName);
//                    String content=oldPriceRedressal.getRoomTypeName()+"("+oldPriceRedressal.getPriceTypeName()+")\n";
//                    content+=DateUtil.dateToString(oldPriceRedressal.getBeginDate())+"/"+DateUtil.dateToString(oldPriceRedressal.getEndDate());
//                    leavewordAnnalBean.setContent(content);
//                    leavewordAnnalBean.setTopic(DateUtil.dateToString(oldPriceRedressal.getOperationdate())+"您提交的变价主题：\t"+oldPriceRedressal.getPriceRedressalName()+"已通过审核。");
                    /**
                     * Ebooking批量调价同步到本部批量调价
                     */
                    String[] userInfos = new String[]{user.getLoginName(),user.getName()};
                    String retu = updateHotelPrice.batchUpdatePrice(oldPriceRedressal, userInfos);
                    if(retu == null){
                    	return super.forwardError("更新调价记录失败");
                    }else{
                    	/**
                    	 * 更新E-Booking调价信息表
                    	 */
                    	priceRedressalService.updatePriceRedressalForSingle(oldPriceRedressal,leavewordAnnalBean, user);
                    }
                }
                /**   
                leaAndRightDao.saveLeavewordAnnal(leavewordAnnalBean);
                */
                // 按酒店id和调价主题ID查询
                List<HtlEbookingPriceRedressal> priceRedressalList = priceRedressalService
                    .queryByHotelIdAndPriceThemeId(hotelId, priceRedressalNameId);
                request.setAttribute("priceRedressalList", priceRedressalList);
                request.setAttribute("msg", "审批成功");
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return super.forwardError("更新调价记录失败");
            }
        } else {
            return super.forwardError("无该调价记录");
        }
        return returnStr;
    }
    
    /**
     * 填充留言板实体
     * 
     * @param params
     * @param loginName
     * @param userName
     */
    private void fillLeavewordAnnalBean(Map params, String loginName, String userName) {
        if (null == leavewordAnnalBean) {
            leavewordAnnalBean = new LeavewordAnnalBean();
        }
        leavewordAnnalBean.setAddresser(LeavewordAnnalBean.MANGOCITY);
        leavewordAnnalBean.setAddressee(String.valueOf(hotelId));
        leavewordAnnalBean.setOperationdate(new Date());
        leavewordAnnalBean.setOperationer(userName);
        leavewordAnnalBean.setOperationerID(loginName);
    }

    /**
     * 返回到查询页面
     * @return
     */
    public String backToQuery() {
        // Map params = super.getParams();
        // 验证参数是否有效
        if (null != hotelId) {
            try {
                /** 回写数据库，待审中状态要改为待审核，不需要查询回写后的结果 begin * */
                priceRedressalService.updatePriceRedressal(RequisitionState.AUDITING, hotelId,
                    priceRedressalNameId);
                /** 回写数据库，待审中状态要改为待审核，不需要查询回写后的结果 end * */
            } catch (Exception e) {
                log.error("无该调价记录", e);
                return super.forwardError("无该调价记录");
            }
        } else {
            return super.forwardError("无该调价记录");
        }
        return "adjustPriceApplication";
    }

    /**
     * 批量审批调价
     * @return
     */
    public String auditPriceRedressalBantch() {
        String returnStr = "adjustPriceDetail";
        Map params = super.getParams();
        // 批量审批的调价id，用,分隔
        String priceRedressalBantchID = (String) params.get("priceRedressalBantchID");
        // 界面传入的审核状态
        String requisitionStateStr = (String) params.get("requisitionState");
        // 验证参数是否有效
        if (StringUtil.isValidStr(priceRedressalBantchID)
            && StringUtil.isValidStr(requisitionStateStr)) {
            UserWrapper user = super.getOnlineRoleUser();
            String loginName = ""; // 登录名
            String userName = ""; // 登陆人姓名
            if (null != user) {
                loginName = user.getLoginName();
                userName = user.getName();
            }
            try {
                // 用于存储界面传入批量审批的对象
                List<HtlEbookingPriceRedressal> tempPriceRedressalList = 
                    new ArrayList<HtlEbookingPriceRedressal>();
                /**否决的时候需要填充留言板实体，否则为空*/
                if (Long.parseLong(requisitionStateStr) == RequisitionState.REJECT) {
                    // 填充留言板实体
                    fillLeavewordAnnalBean(params, loginName, userName);
                    returnStr = "adjustPriceLeaveWordResult";
                    // 批量更新调价申请的状态(否决用)
                    priceRedressalService.updatePriceRedressalForReject(priceRedressalBantchID,
                        user, leavewordAnnalBean);
                }
                /** 审批通过的时候需要填充佣金*/
                else if (Long.parseLong(requisitionStateStr) == RequisitionState.AUDITED) {
                    // 批量审批通过的佣金，用,分隔
                    String commisionBatch = (String) params.get("commisionBatch");
                    String[] priceRedressalIDArr = priceRedressalBantchID.split(",");
                    String[] commisionArr = commisionBatch.split(",");
                    // 匹配调价id和佣金，封装到实体
                    for (int i = 0; i < priceRedressalIDArr.length; i++) {
                        HtlEbookingPriceRedressal tempEbookingPriceRedressal =
                            new HtlEbookingPriceRedressal();
                        tempEbookingPriceRedressal.setPriceRedressalID(Long
                            .parseLong(priceRedressalIDArr[i]));
                        tempEbookingPriceRedressal.setCommision(Float.parseFloat(commisionArr[i]));
                        tempPriceRedressalList.add(tempEbookingPriceRedressal);
                    }
                    // 填充留言板实体
                    //fillLeavewordAnnalBean(params, loginName, userName);
                    // 批量更新调价申请的状态(审批通过用)
                    updatePriceRedressalForAdjusted(priceRedressalBantchID,
                        user, tempPriceRedressalList);
                }
                
                // 按酒店id和调价主题ID查询
                List<HtlEbookingPriceRedressal> priceRedressalList = priceRedressalService
                    .queryByHotelIdAndPriceThemeId(hotelId, priceRedressalNameId);
                request.setAttribute("priceRedressalList", priceRedressalList);
                request.setAttribute("msg", "批量审批成功");
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
                return super.forwardError("批量更新调价记录失败");
            }
        } else {
            return super.forwardError("无该调价记录");
        }
        return returnStr;
    }
    
    /**
     * 批量更新调价申请的状态(审批通过用)
     */
    public void updatePriceRedressalForAdjusted(String priceRedressalBantchID, UserWrapper user,
        List<HtlEbookingPriceRedressal> tempPriceRedressalList) throws Exception {
        List<HtlEbookingPriceRedressal> oldPriceRedressalList = priceRedressalDao
            .queryPriceRedressal(priceRedressalBantchID);
        List<HtlEbookingPriceRedressal> oldChangedPriceRedressalList = new ArrayList<HtlEbookingPriceRedressal>();
        String Topic = "";//主题
        String Operationdate="";//操作时间
        /**
         * 审核通过后不再需要发留言
         String content = "";
        */
        // 把界面传入的佣金填充的新查询出来的实体中
        for (HtlEbookingPriceRedressal oldPriceRedressal : oldPriceRedressalList) {
            for (HtlEbookingPriceRedressal tempPriceRedressal : tempPriceRedressalList) {
                // 同一个实体填充佣金
                if (tempPriceRedressal.getPriceRedressalID() == oldPriceRedressal
                    .getPriceRedressalID()) {
                    oldPriceRedressal.setCommision(tempPriceRedressal.getCommision());
                }
                
                Topic = oldPriceRedressal.getPriceRedressalName();
                Operationdate = DateUtil.dateToString(oldPriceRedressal.getOperationdate());
                /**审核通过后不再需要发留言
                content+=oldPriceRedressal.getRoomTypeID()+"("+oldPriceRedressal.getPriceTypeName()+")\n";
                content+=DateUtil.dateToString(oldPriceRedressal.getBeginDate())+"/"+DateUtil.dateToString(oldPriceRedressal.getEndDate())+"\n";
                */
            }// 修改审核状态
            oldPriceRedressal.setRequisitionState(RequisitionState.AUDITED);
            oldPriceRedressal.setAuditingDate(new Date());
            oldPriceRedressal.setAuditingID(user.getLoginName());
            oldPriceRedressal.setAuditingName(user.getName());
            oldChangedPriceRedressalList.add(oldPriceRedressal);
        }
        
        try {
			// Ebooking批量调价同步到本部批量调价
        	String[] userInfos = new String[]{user.getLoginName(),user.getName()};
            String retu = updateHotelPrice.batchUpdatePriceList(oldChangedPriceRedressalList, userInfos);
            oldChangedPriceRedressalList = null;
			if(retu==null){
				log.info("EBooking====error====批量调价同步到本部时错误!");
			}
		} catch (RuntimeException e) {
			log.error(e.getMessage(),e);
		}
		
        /**审核通过后不再需要发留言
        leavewordAnnalBean.setContent(content);
      	leavewordAnnalBean.setTopic(Operationdate+"您提交的变价主题：\t"+Topic+"已通过审核。");
      	leaAndRightDao.saveLeavewordAnnal(leavewordAnnalBean);
      	*/
      	//批量保存EBooking调价记录
        priceRedressalDao.saveOrUpdateAll(oldPriceRedressalList);
    }
    
    /**
     * 转到留言板
     * @return
     */
    public String forwardToLeaveWord() {
        Map params = super.getParams();
        String idStr = (String) params.get("priceRedressalID");
        request.setAttribute("priceRedressalID", idStr);
        // 是批量审核，则取批量id
        if (isBatch) {
            String priceRedressalBantchID = (String) params.get("priceRedressalBantchID");
            request.setAttribute("priceRedressalBantchID", priceRedressalBantchID);
            // 批量id也为空则返回错误信息
            if (!StringUtil.isValidStr(priceRedressalBantchID)) {
                return super.forwardError("请选择要审核的调价记录！");
            } else {
                // 为了查询主题中文名，只需要取第一个id
                idStr = priceRedressalBantchID.split(",")[0];
            }
        } else if (!StringUtil.isValidStr(idStr)) {
            return super.forwardError("请选择要审核的调价记录！");
        }
        HtlEbookingPriceRedressal oldPriceRedressal = priceRedressalService
            .queryPriceRedressalById(Long.parseLong(idStr));
        // 初始化留言版主题
        leavewordAnnalBean = new LeavewordAnnalBean();
        leavewordAnnalBean.setTopic(DateUtil.dateToString(oldPriceRedressal.getOperationdate())+"您提交的变价主题：" + oldPriceRedressal.getPriceRedressalName()
            + " 未通过审核。");
        return "adjustPriceLeaveWord";
    }

    /** getter and setter * */

    public IPriceRedressalService getPriceRedressalService() {
        return priceRedressalService;
    }

    public void setPriceRedressalService(IPriceRedressalService priceRedressalService) {
        this.priceRedressalService = priceRedressalService;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getPriceRedressalNameId() {
        return priceRedressalNameId;
    }

    public void setPriceRedressalNameId(Long priceRedressalNameId) {
        this.priceRedressalNameId = priceRedressalNameId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ILeavewordAnnalAndRightService getLeaAndRightService() {
        return leaAndRightService;
    }

    public void setLeaAndRightService(ILeavewordAnnalAndRightService leaAndRightService) {
        this.leaAndRightService = leaAndRightService;
    }

    public LeavewordAnnalBean getLeavewordAnnalBean() {
        return leavewordAnnalBean;
    }

    public void setLeavewordAnnalBean(LeavewordAnnalBean leavewordAnnalBean) {
        this.leavewordAnnalBean = leavewordAnnalBean;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

	public PriceRedressalDaoImpl getPriceRedressalDao() {
		return priceRedressalDao;
	}

	public void setPriceRedressalDao(PriceRedressalDaoImpl priceRedressalDao) {
		this.priceRedressalDao = priceRedressalDao;
	}

	public LeavewordAnnalAndRightDaoImpl getLeaAndRightDao() {
		return leaAndRightDao;
	}

	public void setLeaAndRightDao(LeavewordAnnalAndRightDaoImpl leaAndRightDao) {
		this.leaAndRightDao = leaAndRightDao;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public HtlHotel getHotel() {
		return hotel;
	}

	public void setHotel(HtlHotel hotel) {
		this.hotel = hotel;
	}

	public String getChangePriceHint() {
		return changePriceHint;
	}

	public void setChangePriceHint(String changePriceHint) {
		this.changePriceHint = changePriceHint;
	}

}
