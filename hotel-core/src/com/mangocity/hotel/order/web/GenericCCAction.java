package com.mangocity.hotel.order.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.mangocube.corenut.commons.exception.ErrorCode;

import com.mangocity.hotel.base.constant.WorkType;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.util.CreditCardWrapper;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.base.web.TranslateUtil;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hotel.ext.member.exception.MemberException;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.model.mbrship.MbrshipCategory;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.web.GlobalForward;
import com.mangocity.webnew.constant.FITAliasConstant;

/**
 * 通用的CC的Action, 增加了会员,积分接口 处理会员登录
 * 
 * @author chenkeming
 * 
 */
public abstract class GenericCCAction extends GenericAction {
	
	public enum ExceptionCode{
		@ErrorCode(comment = "The member \"${1}\" is not found.")
		MEMBER_NOT_FOUND_ERROR
	}
    /**
     * 订单会员资料
     */
    protected MemberDTO member;
    
    /**
     * 会员接口
     */
    protected MemberInterfaceService memberInterfaceService;

	/**
     * 标记会员没有登陆时的返回字符串 v2.7.1 by chenjuesu 2009-02-26
     */
    protected static final String NOLOGIN = "forwardToNoLogin";

    /**
     * ATS代理传入的会员ID
     */
    protected String agentMemberId;
    

    
    //存放我浏览过的酒店的hotelId、hotelName
	private List hotelNameAndIdStr = new ArrayList();
   /**
    * 获取联名商家
    */
	protected List<MbrshipCategory>   mbrshipCategoryList = InitServlet.mbrshipCategoryList;

    /**
     * 从session中获取当前会员
     * 
     * @return MemberWrapper
     */
    protected MemberDTO getOnlineMember() {
        return (MemberDTO) getFromSession("onlineMember");
    }

    protected String getOnlineTaskId() {
        return (String) getFromSession("onlineTaskId");
    }

    /**
     * 根据memberCd获取会员简单信息
     * 
     * @param memberCd
     * @return
     */
    protected MemberDTO getMemberSimpleInfo(String memberCd, boolean needCred) {
        try {
        	if (StringUtils.isEmpty(memberCd)) {
        		log.error("Member Code is empty!");
        		return null;
            }
        	
        	MemberDTO memberDTO = this.getMemberByCode(memberCd.trim());

            if (null == memberDTO) {
                return null;
            }

            // 获取会员积分
            if (memberDTO.isMango()) {
            	PointDTO pointDTO;
            	try {
            		pointDTO = pointsDelegate.getPonitsByMemberCd(
            				memberDTO.getMembercd(), BaseConstant.USERNAME);
            	}catch(MemberException e) {
            		log.error("ErrorCode:M12579:GenericCCAction Get Member PointDto Exception===" + e.getMessage(),e);

            		pointDTO = new PointDTO();
            		pointDTO.setBalanceValue("0");
            	}
            	
            	memberDTO.setPoint(pointDTO);
            }

            return memberDTO;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据memberCd获取会员相关信息
     * 
     * @param memberCd
     * @return
     */
    protected MemberDTO getMemberInfo(String memberCd) {
        try {
        	if (StringUtils.isEmpty(memberCd)) {
        		log.error("Member Code is empty!");
        		return null;
            }
        	
        	MemberDTO memberDTO = this.getMemberByCode(memberCd.trim());

            if (memberDTO != null && memberDTO.isMango()) {
            	PointDTO pointDTO;
            	/*
            	 * 处理获取会员积分异常情况，不要影响会员数据的获取。
            	 * @suhaihua 2012-1-5
            	 */
            	try {
            		pointDTO = pointsDelegate.getPonitsByMemberCd(memberDTO.getMembercd(), BaseConstant.USERNAME);
            	}catch(MemberException e) {
            		log.error("ErrorCode:M12580:GenericCCAction Get Member PointDto Exception===" + e.getMessage(),e);
            		pointDTO = new PointDTO();
            		pointDTO.setBalanceValue("0");
            	}

            	memberDTO.setPoint(pointDTO);

       
                // 获取会员常入住人和常联系人
                memberInterfaceService.getFellowAndLinkmanByMemberCd(memberDTO);
            }

            return memberDTO;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 处理cusId,teskId参数
     * 
     * @return false:出异常
     * 
     */
    protected boolean handleMemberLogin() throws BusinessException {
        Map params = super.getParams();
        String cusId = (String) params.get("cusId");
        log.info("===handleMemberLogin.cusId:"+cusId);
        if (StringUtil.isValidStr(cusId)) {
            // TODO: taskId的用处
            String taskId = (String) params.get("taskId");
            putSession("onlineTaskId", taskId);

            MemberDTO memberDTO = (MemberDTO) getFromSession("onlineMember");

            if (null != memberDTO && (memberDTO.getMembercd()).equals(cusId)) {
            	log.info("====loginMember:not true====");
                return true;
            }
            //获取会员信并获取会员积分。
            memberDTO = getMemberInfo(cusId);
            if (null != memberDTO) {
                putSession("onlineMember", memberDTO);
            } else {
            	throw new BusinessException(ExceptionCode.MEMBER_NOT_FOUND_ERROR, new Object[]{"memberCD:"+cusId});
            }
        } else {
            putSession("onlineMember", null);
        }
        return true;
    }

    /**
     * 从COOKIE中获取memberCd，然后获取member对象
     */
    protected MemberDTO getMemberInfoForWeb(boolean isNeedPoint) {
    	MemberDTO loginMember = null;
         try {
             String memberCd = getMemberCdForWeb();        
             if(memberCd == null || "".equals(memberCd)){
            	 return null;
             }
             loginMember = this.getMemberByMemberCd(memberCd);
             if(loginMember == null){
            	 return null;
             }
             
             PointDTO pointWrapper = null;
			if (isNeedPoint) {//默认需要查询积分
				if (null != loginMember && loginMember.isMango()) {
					try {
						pointWrapper = pointsDelegate.getPonitsByMemberCd(loginMember.getMembercd(),BaseConstant.USERNAME);
					} catch (MemberException e) {
						log.error(
								"ErrorCode:M12581:GenericCCAction Get Member PointDto Exception==="+ e.getMessage(), e);
						pointWrapper = new PointDTO();
						pointWrapper.setBalanceValue("0");
					}
				}
			} else {//B2B系统不要查询积分
				pointWrapper = new PointDTO();
				pointWrapper.setBalanceValue("0");
			}
			loginMember.setPoint(pointWrapper);
             
        } catch (Exception e) {
                log.error("GenericCCAction getMemberInfoForWeb exception:" ,e);
        }
        
        //判断会员是否为芒果散客,用于现金返还判断
        if(null != loginMember){
        	log.info("FIT:"+loginMember.getMembercd()+":"+DateUtil.dateToString(new Date())+"================进行是否为芒果散客会籍判断："+loginMember.getMembercd());
        	String aliasId = FITAliasConstant.fitAliasObj.get(loginMember.getAliasid());
        	log.info("FIT:"+loginMember.getMembercd()+":"+DateUtil.dateToString(new Date())+"=================当前会员"+loginMember.getMembercd()+"的项目号为:"+loginMember.getAliasid());
        	boolean isFIT = aliasId==null?false:true;
        	log.info("FIT:"+loginMember.getMembercd()+":"+DateUtil.dateToString(new Date())+"=============会员:"+loginMember.getMembercd()+"是否为芒果散客会籍判断结果"+isFIT);
        	loginMember.setFitFlag(isFIT);
       }

        return loginMember;
    }

    /**
     * 操作结果提示页面
     * 
     * @param message
     *            - 提示信息
     * @param btns
     *            - 按钮文本数组
     * @param urls
     *            - 按钮对应的url数组
     * @param needReturn
     *            - 是否需要返回按钮
     */
    protected String forwardMsgEx(String message, 
                                  String[] btns, String[] urls, 
                                  boolean needReturn) {

        setErrorMessage(message);

        request.setAttribute("btns", btns);
        request.setAttribute("urls", urls);
        request.setAttribute("needReturn", needReturn);

        return GlobalForward.FORWARD_TO_MSG_EX;
    }
    
    /**
     * add by xiaojun.xiong 2010-10-9
     * 从cookie中获取网站登录会员的会籍号
     * @return
     */
    protected String getMemberShipForWeb(){
    	String memberShip = "";
    	Cookie[] cookies = request.getCookies();
    	for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("membercd")) {
				memberShip = cookies[i].getValue();
				break;
			}
		}
    	return memberShip;
    }

    /** setter and getter begin */

    /**
     * 电子地图二期 add by haibo.li 2009-8-16
     */
    public String getChangePriceStyleOne(double price) {
        try {
            if (0 == Double.compare(Double.parseDouble("999999"), price)) {
                return "***";
            }else if(0 == Double.compare(Double.parseDouble("99999.0"), price)){
            	return "免费";
            }else if(0 == Double.compare(Double.parseDouble("888888"), price)){
            	return "×";
            }else if (0 != Double.compare(price, 0.0)) {
                // 把传进来的金额四舍五入取整
                // int a = (int)Math.rint(price);
            	if(String.valueOf(price).indexOf(".")>0){
            		String priceStr = String.valueOf(price);
            		String endPrice = priceStr.substring(priceStr.length()-1,priceStr.length());
            		if(endPrice.equals("0")){
            			return String.valueOf(Double.valueOf(Math.rint(price)).intValue());
            		}else{
            			if(priceStr.replace('.',':' ).split(":")[1].length()>1){
            				return String.valueOf(Double.valueOf(Math.rint(price)).intValue());
            			}
            			return priceStr;
            		}
            	}else{
            		return String.valueOf(Double.valueOf(Math.rint(price)).intValue());
            	}
            } else if (0 == Double.compare(price, 0.0)) {
                return "--";
            }
            return "";
        } catch (Exception e) {
        	log.error(e);
            return forwardError("取金额发生错误");
        }

    }
    
    /**
     * 诺曼底商旅系统,即使房态计算
     * @param price
     * @return
     */
    public String getChangeRoomType(double price){
    	if(0 == price){
    		return "×";
    	}else if(0 == Double.compare(Double.parseDouble("999999"), price)){
    		return "×";
    	}else if(0 == Double.compare(Double.parseDouble("99999.0"), price)){
    		return "×";
    	}else if(0 == Double.compare(Double.parseDouble("888888"), price)){
    		return "×";
    	}else{
    		return "△";
    	}
    }
    
    public String getBackCash(Double price){
    	try{
    		if (0 == Double.compare(Double.parseDouble("999999"), price)) {
                return "***";
            }else if(0 == Double.compare(Double.parseDouble("99999.0"), price)){
            	return "免费";
            }else if(0 == Double.compare(Double.parseDouble("888888"), price)){
            	return "×";
            }else if(0 != Double.compare(price, 0.0)){
        		if(String.valueOf(price).indexOf(".")>0){
            		String priceStr = String.valueOf(price);
            		String endPrice = priceStr.substring(priceStr.length()-1,priceStr.length());
            		if(endPrice.equals("0")){
            			return String.valueOf(Double.valueOf(Math.rint(price)).intValue());
            		}else{
            			if(priceStr.replace('.',':' ).split(":")[1].length()>1){
            				return String.valueOf(Double.valueOf(Math.rint(price)).intValue());
            			}
            			return priceStr;
            		}
            	}else{
            		return String.valueOf(Double.valueOf(Math.rint(price)).intValue());
            	}
            }
//    		 返回现金的总金额 TMC-V2.0 add by shengwei.zuo 2010-3-16
    		
    		return "";
    	}catch(Exception e){
    		log.error(e);
    		return "";
    	}

    	
    }
    
    
    
    protected List findCookies(){
		List hotelIdCookies = new ArrayList();
		
		boolean hasExist = false;
		Cookie[] cookies = super.request.getCookies();
		if(null != cookies ){
			for (int j = 0; j < cookies.length; j++) {
				if (cookies[j].getName().indexOf("hotelId") > -1) {
					hotelIdCookies.add(cookies[j]);
	
				}
			}
	
			// 对cookie进行排序
			for (int ii = 0; ii < hotelIdCookies.size(); ii++) {
	
				Cookie cookieItems = (Cookie) hotelIdCookies.get(ii);
	
				for (int jj = 1; jj < hotelIdCookies.size(); jj++) {
					Cookie cookieItem = (Cookie) hotelIdCookies.get(jj);
					if (cookieItems.getMaxAge() < cookieItem.getMaxAge()) {
	
						hotelIdCookies.set(ii, cookieItem);
						hotelIdCookies.set(jj, cookieItems);
					}
	
				}
			}
			// 排序之后封装cookie中的酒店id、酒店中文名，用于在页面显示
			for (int kk = hotelIdCookies.size() - 1; kk >= 0; kk--) {				
				Cookie cookieIte = (Cookie) hotelIdCookies.get(kk);
				if (cookieIte.getMaxAge() != 0) {
					HtlHotel htlHotel = hotelManage.findHotel(Long.parseLong(cookieIte.getValue()));
					hotelNameAndIdStr.add(new String[]{cookieIte.getValue(), htlHotel.getChnName()});
				} else {
					hotelIdCookies.remove(kk);
				}
	
			}
		}
		return hotelNameAndIdStr;

		
	}
    
    public String getRoomPrice(double price){
    	if (price==0.0){
    		return "--";
    	}else {
    		return String.valueOf(price);
    	}
    }
	/**
	 * 判断用户为打开状态且是日审人员
	 * @return
	 */
	protected boolean checkAuditUser(){
		if(null == user)
			user = getOnlineWorkStates();
		if(null == roleUser)
			roleUser = getOnlineRoleUser();
		return 1 == user.getState() && (user.getType() == WorkType.AUDIT || roleUser.isOrgAudit());
	}
	
	public MemberDTO getMember() {
		return member;
	}

	public void setMember(MemberDTO member) {
		this.member = member;
	}

	public String getAgentMemberId() {
        return agentMemberId;
    }

    public void setAgentMemberId(String agentMemberId) {
        this.agentMemberId = agentMemberId;
    }
	
	public void setMemberInterfaceService(MemberInterfaceService memberInterfaceService) {
		this.memberInterfaceService = memberInterfaceService;
	}

	public List<MbrshipCategory> getMbrshipCategoryList() {
		return mbrshipCategoryList;
	}
    /** setter and getter end */
}
