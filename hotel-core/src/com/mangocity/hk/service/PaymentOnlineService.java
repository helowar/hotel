/**
 * 
 */
package com.mangocity.hk.service;

/**
 * @author wuyun
 * 
 */
public interface PaymentOnlineService {

    public String getCommBgreturl();

    public String getCommPagereturl();

    public String getAliPayreturl();

    public String getAliPaynotifyurl();

    public String getYeePayreturl();

    public String getIpsAddressurl();

    public String getIpsBgreturl();
    
    //add by shengwei.zuo 2009-11-21
	public String getMpmOnlinerequrl();

}
