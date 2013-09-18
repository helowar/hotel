/**
 * 
 */
package com.mangocity.hotel.base.outservice;

/**
 * @author Administrator
 * 
 */
public interface IRateService {

    // 例如要查询人民币对美元的汇率，maincurreny就应为RMB(cdm中的人民币币种代码）,subcurreny为USD

    /**
     * @param maincurreny
     *            本币种
     * @param subcurreny
     *            外币
     * @return String 汇率
     */

    public String getRate(String maincurreny, String subcurreny);

}
