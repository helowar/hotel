package com.mangocity.hotel.order.web;

import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.util.StringUtil;

/**
 * CC前台操作相关，包括前台未操作订单查询,系统外订单查询
 * 
 * @author chenkeming
 * 
 */
public class FrontOperateAction extends GenericCCAction {

    private static final long serialVersionUID = -7848870962436634545L;

    /**
     * 未提交订单查询
     * 
     * @return
     */
    public String frontNotSubmit() {

    	try {
            if (!handleMemberLogin()) {
                return this.forwardError("获取会员信息出错！");
            }
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return this.forwardError("会员不存在！");
        }

        member = getOnlineMember();
        roleUser = getOnlineRoleUser();

        if (null == member) {
            return "frontNotSubmit";
        }

        if (roleUser.isOrg114()) {
            if (member.isMango()) {
                return forwardError("114用户只能处理114的会员!");
            }
            if (!StringUtil.StringEquals1(member.getState(), roleUser.getState())) {
                return forwardError("114用户只能处理同一个省的114会员!");
            }
        }

        return "frontNotSubmit";
    }

    /**
     * 系统外订单查询
     * 
     * @return
     */
    public String firstWaitRefund() {
        return "firstWaitRefund";
    }

    /**
     * 管理酒店订单
     * 
     * @return
     */
    public String orderQuery() {

    	try {
            if (!handleMemberLogin()) {
                return this.forwardError("获取会员信息出错！");
            }
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return this.forwardError("会员不存在！");
        }
        
        member = getOnlineMember();
        roleUser = getOnlineRoleUser();

        putSession("HotelOrderManaSearchCond", null);

        if (null == member) {
            return "orderQuery";
        }

        if (roleUser.isOrg114()) {
            if (member.isMango()) {
                return forwardError("114用户只能处理114的会员!");
            }
            if (!StringUtil.StringEquals1(member.getState(), roleUser.getState())) {
                return forwardError("114用户只能处理同一个省的114会员!");
            }
        }

        return "orderQuery";
    }

    /**
     * 暂存前台”的订单查询
     * 
     */
    public String queryStopFront() {
    	try {
            if (!handleMemberLogin()) {
                return this.forwardError("获取会员信息出错！");
            }
        } catch (BusinessException e) {
        	log.error(e.getMessage(),e);
            return this.forwardError("会员不存在！");
        }
        
        member = getOnlineMember();
        roleUser = getOnlineRoleUser();

        putSession("HotelOrderManaSearchCond", null);

        if (null == member) {
            return "orderQuery";
        }

        if (roleUser.isOrg114()) {
            if (member.isMango()) {
                return forwardError("114用户只能处理114的会员!");
            }
            if (!StringUtil.StringEquals1(member.getState(), roleUser.getState())) {
                return forwardError("114用户只能处理同一个省的114会员!");
            }
        }
        return "queryStopFront";
    }

}
