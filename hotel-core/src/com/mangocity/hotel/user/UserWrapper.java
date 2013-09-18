package com.mangocity.hotel.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mangocity.security.domain.OrgInfo;
import com.mangocity.util.MemberUtil;

/**
 * 
 * 权限用户
 * 
 * @author chenkeming
 * 
 */
public class UserWrapper implements Serializable {

    private static final long serialVersionUID = -492694012608176482L;

    private Integer id;

    private String loginName;

    private String password;

    private String name;

    private String email;

    private String status;

    private Set roles;
    /**
     * 是否房控人员，通过本地权限表得知
     */
    private boolean RSCUser;
    /**
     * 是否房控管理人，通过本地权限表得知
     */
    private boolean RSCAdmin;

    private OrgInfo orgInfo;

    private Map areaMap;// ---包含用户所属区域

    /**
     * 114用户所属省份
     */
    private String state;

    /**
     * 该用户是否mango用户, 非114用户
     */
    public boolean isMango() {
        return !isOrg114();
    }

    /**
     * 是否114前台
     * 
     * @return
     */
    public boolean isOrg114() {
        return null != orgInfo && null != orgInfo.getParent() && null != orgInfo.getLinkman()
            && null != MemberUtil.codeMap.get(orgInfo.getLinkman());
    }

    /**
     * 是否114联通前台
     * 
     * @return
     */
    public boolean isOrg114Unicom() {
        return null != orgInfo && null != orgInfo.getParent() && null != orgInfo.getLinkman()
            && orgInfo.getLinkman().equals("LTT");
    }

    /**
     * 是否114网通专用前台
     * 
     * @return
     */
    public boolean isOrg114CNC() {
        return null != orgInfo && null != orgInfo.getParent() && null != orgInfo.getLinkman()
            && orgInfo.getLinkman().equals("WTT");
    }

    /**
     * 是否114网通(北京)前台
     * 
     * @return
     */
    public boolean isOrg114CNCBJ() {
        return null != orgInfo && null != orgInfo.getParent() && null != orgInfo.getLinkman()
            && orgInfo.getLinkman().equals("WTBJ");
    }

    /**
     * 是否前台用户
     * 
     * @return
     */
    public boolean isOrgFront() {
        return null != orgInfo && null != orgInfo.getParent() && null != orgInfo.getLinkman()
            && orgInfo.getLinkman().equals(UserUtil.ORG_FRONT_STR);
    }

    /**
     * 是否中台用户
     * 
     * @return
     */
    public boolean isOrgMid() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_MID_STR))
            || isOrgMidAdmin() || isOrgCCAdmin() || isOrgRSC() || isOrgTMC();
    }

    /**
     * 是否日审用户
     * 
     * @return
     */
    public boolean isOrgAudit() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_AUDIT_STR))
            || isOrgAuditAdmin() || isOrgCCAdmin();
    }

    /**
     * 是否中台管理员
     * 
     * @return
     */
    public boolean isOrgMidAdmin() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_MID_ADMIN_STR))
            || isOrgCCAdmin();
    }

    /**
     * 是否日审管理员
     * 
     * @return
     */
    public boolean isOrgAuditAdmin() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_AUDIT_ADMIN_STR))
            || isOrgCCAdmin();
    }

    /**
     * 是否CC管理员
     * 
     * @return
     */
    public boolean isOrgCCAdmin() {
        return null != orgInfo && null != orgInfo.getLinkman()
            && orgInfo.getLinkman().equals(UserUtil.ORG_CC_ADMIN_STR);
    }

    /**
     * 菜单是否能看中台
     * 
     * @return
     */
    public boolean isCanShowMid() {
        return isOrgMid() || isOrgAudit() || isOrgMidAdmin() || isOrgAuditAdmin() || isOrgCCAdmin()
            || isOrgRSC();
    }

    /**
     * 是否能编辑订单
     * 
     * @return
     */
    public boolean isCanEditOrder() {
        return isOrgCCAdmin() || (!isOrgFinance());
    }

    /**
     * 用户是否有补登日审角色
     * 
     * @return
     */
    public boolean isCanPatchAudit() {
        return UserUtil.isCanPatchAudit(this);
    }

    /**
     * 是否财务人员
     * 
     * @return
     */
    public boolean isOrgFinance() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().startsWith(
            UserUtil.ORG_FINANCE))
            || isOrgCCAdmin();
    }

    /**
     * 是否付款人员
     * 
     * @return
     */
    public boolean isOrgPayment() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_FINANCE_PAYMENT))
            || isOrgCCAdmin();
    }

    /**
     * 是否退款人员
     * 
     * @return
     */
    public boolean isOrgRefund() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_FINANCE_REFUND))
            || isOrgCCAdmin();
    }

    /**
     * 是否退款审批人员
     * 
     * @return
     */
    public boolean isOrgRefundAudit() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_FINANCE_AUDIT))
            || isOrgCCAdmin();
    }

    /**
     * 是房控疑难用户
     * 
     * @return
     * @author chenjiajie V2.4.2 2008-12-29
     */
    public boolean isOrgRSC() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_MID_RSC_STR))
            || isOrgMidAdmin() || isOrgCCAdmin();
    }

    /**
     * 是否TMC人员
     * @return
     */
    public boolean isOrgTMC() {
        return (null != orgInfo && null != orgInfo.getLinkman() && orgInfo.getLinkman().equals(
            UserUtil.ORG_TMC))
            || isOrgMidAdmin() || isOrgCCAdmin();
    }
    
    /*************** getter and setter begin ******************/

    public OrgInfo getOrgInfo() {
        return orgInfo;
    }

    public void setOrgInfo(OrgInfo orgInfo) {
        this.orgInfo = orgInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserWrapper() {
        roles = new HashSet();
    }

    public UserWrapper(Integer id) {
        roles = new HashSet();
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set getRoles() {
        return roles;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map getAreaMap() {
        return areaMap;
    }

    public void setAreaMap(Map areaMap) {
        this.areaMap = areaMap;
    }

	public boolean isRSCAdmin() {
		return RSCAdmin;
	}

	public void setRSCAdmin(boolean admin) {
		RSCAdmin = admin;
	}

	public boolean isRSCUser() {
		return RSCUser;
	}

	public void setRSCUser(boolean user) {
		RSCUser = user;
	}

    /*************** getter and setter end ******************/
}
