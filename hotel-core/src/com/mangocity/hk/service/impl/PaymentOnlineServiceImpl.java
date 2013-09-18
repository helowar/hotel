/**
 * 
 */
package com.mangocity.hk.service.impl;

import com.mangocity.hk.service.PaymentOnlineService;

/**
 * @author wuyun 读取在线支付的配置参数
 */
public class PaymentOnlineServiceImpl implements PaymentOnlineService {

    private String commBgreturl;

    private String commPagereturl;

    private String aliPayreturl;

    private String aliPaynotifyurl;

    private String yeePayreturl;

    private String ipsAddressurl;

    private String ipsBgreturl;
    
    //新的在线支付接口的请求URL，add by shengwei.zuo 2009-11-21
    private String mpmOnlinerequrl;
    

    public PaymentOnlineServiceImpl() {

    }

    public String getCommBgreturl() {
        return commBgreturl;
    }

    public void setCommBgreturl(String commBgreturl) {
        this.commBgreturl = commBgreturl;
    }

    public String getCommPagereturl() {
        return commPagereturl;
    }

    public void setCommPagereturl(String commPagereturl) {
        this.commPagereturl = commPagereturl;
    }

    public String getAliPayreturl() {
        return aliPayreturl;
    }

    public void setAliPayreturl(String aliPayreturl) {
        this.aliPayreturl = aliPayreturl;
    }

    public String getAliPaynotifyurl() {
        return aliPaynotifyurl;
    }

    public void setAliPaynotifyurl(String aliPaynotifyurl) {
        this.aliPaynotifyurl = aliPaynotifyurl;
    }

    public String getYeePayreturl() {
        return yeePayreturl;
    }

    public void setYeePayreturl(String yeePayreturl) {
        this.yeePayreturl = yeePayreturl;
    }

    public String getIpsAddressurl() {
        return ipsAddressurl;
    }

    public void setIpsAddressurl(String ipsAddressurl) {
        this.ipsAddressurl = ipsAddressurl;
    }

    public String getIpsBgreturl() {
        return ipsBgreturl;
    }

    public void setIpsBgreturl(String ipsBgreturl) {
        this.ipsBgreturl = ipsBgreturl;
    }

	public String getMpmOnlinerequrl() {
		return mpmOnlinerequrl;
	}

	public void setMpmOnlinerequrl(String mpmOnlinerequrl) {
		this.mpmOnlinerequrl = mpmOnlinerequrl;
	}

}
