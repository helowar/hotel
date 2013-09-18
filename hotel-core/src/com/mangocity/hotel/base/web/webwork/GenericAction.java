package com.mangocity.hotel.base.web.webwork;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.mangocube.corenut.commons.exception.ErrorCode;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.OrWorkStatesManager;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.web.TranslateUtil;
import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.hotel.ext.member.service.MemberBaseInfoDelegate;
import com.mangocity.hotel.ext.member.service.PointsDelegate;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.lang.StringUtils;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.hotel.ext.member.exception.MemberException;
import com.mangocity.security.acegi.user.UserWithDomain;
import com.mangocity.security.domain.User;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.web.DateConverter;
import com.mangocity.util.web.GlobalForward;
import com.mangocity.util.web.WebUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 通用的Action，提供Request参数的获取。
 * 
 * @author zhengxin
 * 
 */
public class GenericAction extends ActionSupport implements ServletRequestAware {

	protected static final Log log = LogFactory.getLog(GenericAction.class);
	private String errorCode;
	
	public enum ExceptionCode{
		@ErrorCode(comment = "The exception occured when calling member service.")
		CALL_MEMBER_SERVICE_ERROR,
		
		@ErrorCode(comment = "The exception occured when calling credit card service.")
		CALL_CREDITCARD_SERVICE_ERROR
	}

    /**
     * 当前登录权限用户
     */
    protected UserWrapper roleUser;

    /**
     * CC用户
     */
    protected OrWorkStates user;

    // protected OrWorkStates workStates;

    protected OrWorkStatesManager workStatesManager;

	/**
     * 积分接口 改用会员Jar的接口 modify by chenjiajie 2009-05-12
     */
    protected PointsDelegate pointsDelegate;

    private MemberBaseInfoDelegate memberBaseInfoDelegate;

	/**
     * 会员转换辅助
     */
    protected TranslateUtil translateUtil;

	protected HttpServletRequest request;
    
    /**
     * 增加response对象 add by chenjiajie 2009-10-19
     */
    protected HttpServletResponse httpResponse;
    
    /*
     * refactor
     * this two fields comes from com.mangocity.hotel.order.constant.WorkType
     */
    private int  WORKTYPE_AUDIT =2;
    private int  WORKTYPE_HRA = 1;
      

    private Map innerParameters;

    private String errorMessage;

    /**
     * 供弹出提示窗口页面用,retMethod为关闭弹出窗口时 要刷新主窗口页面所调用的主页面的script函数名
     */
    private String retMethod;

    /**
     * 保存用户名，防止因为session过期取不到用户名，保存Name
     */

    private String backUserName;

    /**
     * 保存登陆名，防止因为session过期取不到登陆名，保存LoginName
     */
    private String backLoginName;

    
    protected HotelManage hotelManage;
    
    static {
        register();
    }

    /**
     * 注册日期格式转换类 主要是为struts的BeanUtils的类，提供日期转换格式的插件。 否则，BeanUtils则将字符串转为日期时，会报错
     */
    private static void register() {
        ConvertUtils.register(new DateConverter(), Date.class);
    }

    /**
     * 获得Request的参数数据 之所以是Public方法，是为了单元测试时，可以直接拿到Map并注入模拟数据
     * 
     * @return
     */
    public Map getParams() {
        if (null == innerParameters)
            innerParameters = WebUtil.getParameterMap(request);

        return innerParameters;
    }

    /**
     * 获得Request的参数数据 之所以是Public方法，是为了单元测试时，可以直接拿到Map并注入模拟数据
     * 
     * @return
     */
    public List getSafeList(String para) {
        return WebUtil.getSafeList(request, para);
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;

    }

    /**
     * 清除Session
     */
    protected void clearSession() {
        roleUser = (UserWrapper) getFromSession("onlineRoleUser");
        ActionContext.getContext().getSession().clear();
        putSession("onlineRoleUser", roleUser);
    }

    /**
     * 应当对此方法进行限制使用
     * 
     * @return
     */
    protected Map getSession() {
        return ActionContext.getContext().getSession();
    }

    protected void putSession(Object key, Object value) {
        getSession().put(key, value);
    }

    protected Object getFromSession(Object key) {
        return getSession().get(key);
    }

    /**
     * 跳转错误页面 记录错误信息到日志中
     * 
     * @param message
     * @return
     */
    protected String forwardError(String message) {
        log.error(message +"  "+errorCode);

        super.addActionError(message);

        this.setErrorMessage(message);

        return GlobalForward.FORWARD_TO_ERROR;
    }

    /**
     * 弹出操作结果提示窗口页面
     */
    protected String forwardMsgBox(String message, String method) {

        this.setErrorMessage(message);
        this.setRetMethod(method);

        return GlobalForward.FORWARD_TO_MSGBOX;
    }

    /**
     * 操作结果提示页面
     */
    protected String forwardMsg(String message) {

        this.setErrorMessage(message);

        return GlobalForward.FORWARD_TO_MSG;
    }
    
    /**
     * 操作结果提示页面,并关闭当前页 add by shengwei.zuo 2009-10-26
     */
    protected String forwardMsgAndColse(String message) {

        this.setErrorMessage(message);

        return GlobalForward.FORWARD_TO_MSG_CLOSE;
    }
    /**
     * 验证房控用户是否打开
     * @return
     * addby chenjuesu at 2010-1-4上午09:45:44
     */
    protected boolean checkRSCUserAvalible(){
		if(null == roleUser)
			roleUser = getOnlineRoleUser();
		if(roleUser.isRSCUser()){
			log.info("========================"+roleUser.getLoginName()+"是房控人员");
			user = getOnlineWorkStates();
			return 1 == user.getState();
		}
		return false;
	}

    /**
     * 返回当前在线用户并且是不一定要检查权限的，如果采用了getOnlineRoleUser是一定要检查权限，
     * 当用户去访问资源的时候，返回一个anonymousUser用户，这个函数就是解决这个问题的。
     * 
     * @return
     */
    public UserWrapper getCurrentUser() {
        roleUser = (UserWrapper) getFromSession("onlineRoleUser");
        if (null != roleUser) {
            return roleUser;
        }
        return getOnlineRoleUser();
    }

    /**
     * 获取当前登录的用户
     * 
     * @return
     */

    public UserWrapper getOnlineRoleUser() {
        roleUser = (UserWrapper) getFromSession("onlineRoleUser");
        if (null != roleUser) {
            setBackUserName(roleUser.getName());
            setBackLoginName(roleUser.getLoginName());
            return roleUser;
        }
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (null != ctx) {
            Authentication auth = ctx.getAuthentication();
            if (null != auth) {
                Object principal = auth.getPrincipal();
                if (principal instanceof UserWithDomain) {
                    User loginUser = ((UserWithDomain) principal).getDomainUser();
                    if (null!=loginUser && null != loginUser.getLoginName()
                        && !loginUser.getLoginName().equals("anonymousUser")) {
                        roleUser = (UserWrapper) getFromSession("onlineRoleUser");
                        if (null == roleUser
                            || !loginUser.getLoginName().equals(roleUser.getLoginName())) {
                            roleUser = TranslateUtil.translateUser(loginUser);
                            putSession("onlineRoleUser", roleUser);
                            setBackUserName(roleUser.getName());
                            setBackLoginName(roleUser.getLoginName());
                            //标记房控人员的相关权限
                            workStatesManager.markRSCRights(roleUser);
                            //把当前登录名写入本地cookie add by chenjiajie 2009-10-19
                            Cookie cookie = new Cookie("userCookie", roleUser.getLoginName());                                    
                            getHttpResponse().addCookie(cookie);
                        }
                        return roleUser;
                    }
                }
            }
        } else {
            log.info("getOnlineRoleUser SecurityContextHolder.getContext()=null");
        }
        log.info("can't get login name from session 'onlineRoleUser'!");
        return null;
    }

    /**
     * 返回中台操作人员工作状态对象
     * 
     * @return
     */

    public OrWorkStates getOnlineWorkStates() {

        UserWrapper userWrapper = getOnlineRoleUser();
        OrWorkStates user = null;

        if (null != userWrapper && null != userWrapper.getLoginName()
            && 0 < userWrapper.getLoginName().length()) {
            if (userWrapper.isOrgMid() || userWrapper.isOrgAudit() || userWrapper.isRSCUser()) {
                user = workStatesManager.returnWorkStatesBylogonId(userWrapper.getLoginName());

                if (null == user) {
                    user = workStatesManager.handleLoginUser(userWrapper.getLoginName(),
                        userWrapper.getName(), userWrapper.isOrgAudit() ? WORKTYPE_AUDIT
                            : WORKTYPE_HRA);
                }
            }
        }

        return user;

    }

    /**
     * 记录有中台权限的人员的登陆时间 add by chenjiajie 2008-12-29
     */
    protected void markOrgMidLoginTime() {
        setRoleUser(getOnlineRoleUser());
        if (roleUser.isCanShowMid()) {
            workStatesManager.saveOrgMidLoginTime(roleUser);
        }
    }
    
    /**
     * 代理判断登陆方法
     * 
     * 
     * @return
     */
    protected boolean checkLogin() {
    	try {
    		boolean loginFlag = false;
    		HttpSession session = request.getSession();
    		if(session == null) {
    			log.info("session is null!");
    		}else {
    			if(session.getAttribute("agentCode") != null) {
        			log.info("get agentCode from session where agentCode :=" + session.getAttribute("agentCode"));
        			return true;
        		}else {
        			log.info("agentCode is not in session!");
    			}
    		}
        	if(!loginFlag){
        		Cookie[] cookies = request.getCookies();
            	if(cookies == null || cookies.length == 0) {
            		log.info("cookies is null!");
            	}
            	for(Cookie cookie : cookies){
        			if(cookie.getName().indexOf("agentCode") > -1){
        				session.setAttribute("agentCode", cookie.getValue());
        				return true;
        			}
            	}
        	}
        	return false;
    	}catch(Exception e) {
    		log.info("get agentCode occor a error!"+e.getMessage());
    		return false;
    	}
    }
    
    /**
     * Refactor---这个方法是从GenericCCAction中迁移过来
     * 从cookie中获取网站登陆的会员资料
     * 
     * @param innerParameters
     */

    protected String getMemberCdForWeb() {    	
    	String memberCd = null;
        Cookie[] cookies = request.getCookies();
        
        try {
			if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("membercd".equals(cookie.getName())) {
	            	if(!StringUtils.isEmpty(cookie.getValue())){
	                memberCd = cookie.getValue();
	            }
	            }
	        }
			}
		} catch (Exception e) {
			log.error("GenericAction getMemberCdForWeb get member cd exception:", e);
		}
		
		log.info("GenericAction getMemberCdForWeb get member cd is:" + memberCd);
        return memberCd;
    }
    /**
     * 获取当前会员ID
     * @return
     */
    protected String getMbrIdForWeb() {    	
    	String mbrID = null;
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("mbrID".equals(cookie.getName())) {
						if (!StringUtils.isEmpty(cookie.getValue())) {
							mbrID = cookie.getValue();
						}
					}
				}
			}
		
		log.info("GenericAction getMemberCdForWeb get mbrID is:" + mbrID);
		return mbrID;
    }
    /**
     * 从cookie中获取当前会员加密后的的密文
     * @return
     */
    protected String getMbrSignForWeb() {    	
    	String mbrID = null;
        Cookie[] cookies = request.getCookies();
        
        try {
        	if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("mbrsign".equals(cookie.getName())) {
						if (!StringUtils.isEmpty(cookie.getValue())) {
							mbrID = cookie.getValue();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("GenericAction getMbrSignForWeb get mbrsign exception:",e);
		}
		
        log.info("GenericAction getMbrSignForWeb get mbrsign is:" + mbrID);
        return mbrID;
    }
    
    /**
     * 根据memberCd获取会员简单信息
     * 
     * @param memberCd
     * @return
     */
    @SuppressWarnings("unchecked")
    protected MemberDTO getMemberSimpleInfoByMemberCd(String memberCd, boolean needCred) {
        MemberDTO memberDTO = null;
        if (memberCd == null || "".equals(memberCd)) {
            log.error("Member cd is null or empty string!");
            return null;
        }
        
        try {       	
        	memberDTO = this.getMemberByMemberCd(memberCd);
            if (memberDTO != null) {
				// 获取会员积分
				if (memberDTO.isMango()) {
					PointDTO pointDTO;
					try {
						pointDTO = pointsDelegate.getPonitsByMemberCd(memberDTO
								.getMembercd(), BaseConstant.USERNAME);
					} catch (MemberException e) {
						log.error(
								"ErrorMSG-MP100000:query member points error by membercd:="
										+ memberDTO.getMembercd(), e);

						pointDTO = new PointDTO();
						pointDTO.setBalanceValue("0");
					}
					memberDTO.setPoint(pointDTO);
				}
			}else {
				log.error("ErrorMSG-MI100000:can't find the member by memberCd :=" + memberCd);
				
				return null;
			}
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

        return memberDTO;
    }
    
    /**
     * 根据会员代码获得会员信息。
     * 
     * @param code	会员代码。
     * @return	会员
     */
    protected MemberDTO getMemberByCode(String code) throws BusinessException {
            return memberBaseInfoDelegate.getMemberByMemberCd(code);
    }
    
    /**
     * 根据会员CD获得会员信息。
     * 
     * @param memberCd	会员CD。
     * @return	会员
     */
    protected MemberDTO getMemberByMemberCd(String memberCd) throws BusinessException {
            return memberBaseInfoDelegate.getMemberByMemberCd(memberCd);

    }
    

    public void setInnerParameters(Map innerParameters) {
        this.innerParameters = innerParameters;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }

    public String getRetMethod() {
        return retMethod;
    }

    public void setRetMethod(String retMethod) {
        this.retMethod = retMethod;
    }

    public UserWrapper getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(UserWrapper roleUser) {
        this.roleUser = roleUser;
    }

    public OrWorkStates getUser() {
        return user;
    }

    public void setUser(OrWorkStates user) {
        this.user = user;
    }

    public OrWorkStatesManager getWorkStatesManager() {
        return workStatesManager;
    }

    public void setWorkStatesManager(OrWorkStatesManager workStatesManager) {
        this.workStatesManager = workStatesManager;
    }

    public String getBackUserName() {
        return backUserName;
    }

    public void setBackUserName(String backUserName) {
        this.backUserName = backUserName;
    }

    public String getBackLoginName() {
        return backLoginName;
    }

    public void setBackLoginName(String backLoginName) {
        this.backLoginName = backLoginName;
    }

    public HttpServletResponse getHttpResponse() {
        ActionContext ctx = ActionContext.getContext();        
        httpResponse = (HttpServletResponse)ctx.get(ServletActionContext.HTTP_RESPONSE);
        return httpResponse;
    }

    public void setHttpResponse(HttpServletResponse httpResponse) {
        this.httpResponse = httpResponse;
    }    
    
    //add by shengwei.zuo 2009-11-04  begin
    public HttpServletRequest getRequest() {
    	ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setTranslateUtil(TranslateUtil translateUtil) {
		this.translateUtil = translateUtil;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public PointsDelegate getPointsDelegate() {
		return pointsDelegate;
	}

	public void setPointsDelegate(PointsDelegate pointsDelegate) {
		this.pointsDelegate = pointsDelegate;
	}

	public MemberBaseInfoDelegate getMemberBaseInfoDelegate() {
		return memberBaseInfoDelegate;
	}

	public void setMemberBaseInfoDelegate(
			MemberBaseInfoDelegate memberBaseInfoDelegate) {
		this.memberBaseInfoDelegate = memberBaseInfoDelegate;
	}
	
	
	
}
