package com.mangocity.util;

import java.io.Serializable;

/**
 * 
 * @author pengfeng
 * 
 *         本类是系统启动时读取了jdbc.properties中系统值的类
 */
public class ConfigParaBean implements Serializable {

    // 网站集体定房发送EMAIL给后台的EMAIL值
    private String webRecEmail;

    // 网站会员登陆URL
    private String hwebMemberLoginURL;

    // 酒店网站会员登陆返回URL
    private String hwebMemberLoginReturnURL;

    // 网站在线支付IPS-URL
    private String hwebIPSPayURL;

    // #网站IPS在线支付返回URL
    private String hwebIPSPayReturnURL;

    // 网站在线支付CMB-URL
    private String hwebCMBPayURL;

    // #网站CMB在线支付返回URL
    private String hwebCMBPayReturnURL;
    
    /**
	 * CC调用的代金券页面接口URL
	 * hotel2.9.3 add by chenjiajie 2009-08-27
	 */
	private String ccVoucherRequestURL;
	
	/**
	 * 网站调用的代金券页面接口URL
	 * hotel2.9.3 add by chenjiajie 2009-08-27
	 */
	private String hwebVoucherRequestURL;
	
    public String getHwebMemberLoginURL() {
        return hwebMemberLoginURL;
    }

    public void setHwebMemberLoginURL(String hwebMemberLoginURL) {
        this.hwebMemberLoginURL = hwebMemberLoginURL;
    }

    public String getHwebMemberLoginReturnURL() {
        return hwebMemberLoginReturnURL;
    }

    public void setHwebMemberLoginReturnURL(String hwebMemberLoginReturnURL) {
        this.hwebMemberLoginReturnURL = hwebMemberLoginReturnURL;
    }

    public String getWebRecEmail() {
        return webRecEmail;
    }

    public void setWebRecEmail(String webRecEmail) {
        this.webRecEmail = webRecEmail;
    }

    public String getHwebIPSPayURL() {
        return hwebIPSPayURL;
    }

    public void setHwebIPSPayURL(String hwebIPSPayURL) {
        this.hwebIPSPayURL = hwebIPSPayURL;
    }

    public String getHwebIPSPayReturnURL() {
        return hwebIPSPayReturnURL;
    }

    public void setHwebIPSPayReturnURL(String hwebIPSPayReturnURL) {
        this.hwebIPSPayReturnURL = hwebIPSPayReturnURL;
    }

    public String getHwebCMBPayReturnURL() {
        return hwebCMBPayReturnURL;
    }

    public void setHwebCMBPayReturnURL(String hwebCMBPayReturnURL) {
        this.hwebCMBPayReturnURL = hwebCMBPayReturnURL;
    }

    public String getHwebCMBPayURL() {
        return hwebCMBPayURL;
    }

    public void setHwebCMBPayURL(String hwebCMBPayURL) {
        this.hwebCMBPayURL = hwebCMBPayURL;
    }

	public String getCcVoucherRequestURL() {
		return ccVoucherRequestURL;
	}

	public void setCcVoucherRequestURL(String ccVoucherRequestURL) {
		this.ccVoucherRequestURL = ccVoucherRequestURL;
	}

	public String getHwebVoucherRequestURL() {
		return hwebVoucherRequestURL;
	}

	public void setHwebVoucherRequestURL(String hwebVoucherRequestURL) {
		this.hwebVoucherRequestURL = hwebVoucherRequestURL;
	}

}
